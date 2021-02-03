/*
 * Copyright 2020-2021 Slawomir Czerwinski
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package it.czerwinski.intellij.wavefront.editor.ui

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.util.Disposer
import com.jetbrains.rd.util.AtomicReference
import com.jetbrains.rd.util.string.printToString
import com.jogamp.opengl.GLCapabilities
import com.jogamp.opengl.GLException
import com.jogamp.opengl.GLProfile
import com.jogamp.opengl.awt.GLJPanel
import com.jogamp.opengl.math.FloatUtil
import com.jogamp.opengl.util.FPSAnimator
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModelFactory
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.GLModelFactory
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextArea
import javax.swing.SwingConstants
import javax.swing.event.MouseInputAdapter

class GLPanelWrapper : JPanel(BorderLayout()), Disposable {

    private val initPreviewTriggered = AtomicBoolean(false)

    private val placeholder = JLabel(
        WavefrontObjBundle.message("editor.fileTypes.obj.preview.placeholder"),
        SwingConstants.CENTER
    )

    private val modalityState get() = ModalityState.stateForComponent(this)

    private lateinit var presenter: GLPresenter<*>

    private var model: GLModel? = null
        set(value) {
            field = value
            invokeLater(modalityState) {
                if (::presenter.isInitialized) {
                    presenter.updateModel(value)
                }
            }
        }

    private val modelSize: Float
        get() = (model?.size?.takeUnless { it == 0f }) ?: EMPTY_MODEL_SIZE

    private val cameraModel: AtomicReference<GLCameraModel> =
        AtomicReference(GLCameraModelFactory.createDefault())

    private val settings: AtomicReference<ObjPreviewFileEditorSettingsState> =
        AtomicReference(ObjPreviewFileEditorSettingsState())

    init {
        invokeLater(modalityState, ::attachPlaceholder)
    }

    private fun attachPlaceholder() {
        removeAll()
        add(placeholder, BorderLayout.CENTER)
        invalidate()
    }

    fun initPreview() {
        if (initPreviewTriggered.compareAndSet(false, true)) {
            invokeLater(modalityState, ::attachJPanel)
        }
    }

    private fun attachJPanel() {
        try {
            val canvas = GLJPanel(getGLCapabilities())
            val animator = FPSAnimator(canvas, DEFAULT_FPS_LIMIT)

            presenter = GL2Presenter(animator, ::showError)
            presenter.updateModel(model)
            presenter.updateCameraModel(cameraModel.get())
            presenter.updateSettings(settings.get())
            canvas.addGLEventListener(presenter)

            canvas.addMouseWheelListener(ZoomingMouseWheelListener())
            val panningMouseInputListener = PanningMouseInputListener()
            canvas.addMouseListener(panningMouseInputListener)
            canvas.addMouseMotionListener(panningMouseInputListener)

            add(canvas, BorderLayout.CENTER)
            remove(placeholder)
            invalidate()

            presenter.start()
        } catch (expected: Throwable) {
            showError(expected)
        }
    }

    private fun getGLCapabilities(): GLCapabilities {
        val supportedProfiles = listOf(
            GLProfile.GL2ES1,
            GLProfile.GLES1,
            GLProfile.GL3bc,
            GLProfile.GL4bc,
            null,
        )
        for (profileName in supportedProfiles) {
            try {
                val profile = GLProfile.get(profileName)
                return GLCapabilities(profile)
            } catch (ignored: GLException) {
            }
        }
        throw UnsupportedOperationException("Could not find any supported GL profile")
    }

    private fun showError(exception: Throwable) {
        if (::presenter.isInitialized) {
            presenter.stop()
            Disposer.dispose(presenter)
        }
        removeAll()
        add(
            JLabel(WavefrontObjBundle.message("editor.fileTypes.obj.preview.error")),
            BorderLayout.BEFORE_FIRST_LINE
        )
        add(
            JTextArea(exception.printToString()),
            BorderLayout.CENTER
        )
        invalidate()
    }

    fun updateObjFile(objFile: ObjFile?) {
        runReadAction { createModel(objFile) }
    }

    private fun createModel(objFile: ObjFile?) {
        model = objFile?.let(GLModelFactory::create)
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(
                distance = oldCameraModel.distance.coerceAtLeast(
                    minimumValue = modelSize * DEFAULT_DISTANCE_FACTOR
                )
            )
        }
    }

    private fun updateCameraModel(transform: (GLCameraModel) -> GLCameraModel) {
        cameraModel.getAndUpdate { oldCameraModel ->
            val newCameraModel = transform(oldCameraModel)
            invokeLater(modalityState) {
                if (::presenter.isInitialized) {
                    presenter.updateCameraModel(cameraModel.get())
                }
            }
            return@getAndUpdate newCameraModel
        }
    }

    fun updateUpVector(upVector: UpVector) {
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(upVector = upVector)
        }
    }

    fun updateGLPresenterSettings(newSettings: ObjPreviewFileEditorSettingsState) {
        if (newSettings != settings.getAndSet(newSettings)) {
            invokeLater(modalityState) {
                if (::presenter.isInitialized) {
                    presenter.updateSettings(settings.get())
                }
            }
        }
    }

    override fun dispose() {
        if (::presenter.isInitialized) {
            Disposer.dispose(presenter)
        }
    }

    inner class ZoomingMouseWheelListener : MouseWheelListener {

        override fun mouseWheelMoved(event: MouseWheelEvent?) {
            val scrollAmount = ZOOM_PRECISION * (event?.wheelRotation ?: 0)

            if (scrollAmount != 0f) {
                updateCameraModel { oldCameraModel ->
                    oldCameraModel.zoomed(scrollAmount)
                }
            }
        }

        private fun GLCameraModel.zoomed(zoomAmount: Float): GLCameraModel {
            val newDistance = distance * FloatUtil.pow(ZOOM_BASE, zoomAmount)
            return copy(
                distance = newDistance.coerceIn(
                    minimumValue = modelSize * MIN_DISTANCE_FACTOR,
                    maximumValue = modelSize * MAX_DISTANCE_FACTOR
                )
            )
        }
    }

    inner class PanningMouseInputListener : MouseInputAdapter() {

        private var anchorX: Int? = null
        private var anchorY: Int? = null

        override fun mousePressed(event: MouseEvent?) {
            anchorX = event?.x
            anchorY = event?.y
        }

        override fun mouseDragged(event: MouseEvent?) {
            if (event != null) {
                updatePanning(event.x, event.y)
            }
        }

        override fun mouseReleased(event: MouseEvent?) {
            if (event != null) {
                updatePanning(event.x, event.y)
            }
            mousePressed(null)
        }

        private fun updatePanning(x: Int, y: Int) {
            val dx = anchorX?.let { (x - it) * PAN_PRECISION } ?: 0f
            val dy = anchorY?.let { (y - it) * PAN_PRECISION } ?: 0f
            anchorX = x
            anchorY = y
            if (dx != 0f || dy != 0f) {
                updateCameraModel { oldCameraModel ->
                    oldCameraModel.panned(dx, dy)
                }
            }
        }

        private fun GLCameraModel.panned(
            panningAngle: Float,
            panningElevation: Float
        ): GLCameraModel {
            val newAngle = angle + panningAngle
            val newElevation = elevation + panningElevation
            return copy(
                angle = newAngle % FULL_ANGLE,
                elevation = newElevation.coerceIn(
                    minimumValue = MIN_ELEVATION,
                    maximumValue = MAX_ELEVATION
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_FPS_LIMIT = 10

        private const val EMPTY_MODEL_SIZE = 1f

        private const val MIN_DISTANCE_FACTOR = .1f
        private const val MAX_DISTANCE_FACTOR = 10f
        private const val DEFAULT_DISTANCE_FACTOR = 5f

        private const val ZOOM_BASE = 2f
        private const val ZOOM_PRECISION = .1f

        private const val PAN_PRECISION = .5f

        private const val FULL_ANGLE = 360f
        private const val MIN_ELEVATION = -89f
        private const val MAX_ELEVATION = 89f
    }
}
