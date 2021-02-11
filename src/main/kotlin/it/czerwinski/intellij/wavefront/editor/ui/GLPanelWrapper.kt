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
import com.jetbrains.rd.util.AtomicReference
import com.jetbrains.rd.util.string.printToString
import com.jogamp.opengl.math.FloatUtil
import com.jogamp.opengl.util.FPSAnimator
import graphics.glimpse.types.Angle
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.PreviewScene
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
import java.util.concurrent.atomic.AtomicInteger
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

    private lateinit var scene: PreviewScene

    private var model: GLModel? = null
        set(value) {
            field = value
            invokeLater(modalityState) {
                if (::scene.isInitialized) {
                    scene.updateModel(value)
                }
            }
        }

    private val modelSize: Float
        get() = (model?.size?.takeUnless { it == 0f }) ?: EMPTY_MODEL_SIZE

    private val cameraModel: AtomicReference<GLCameraModel> =
        AtomicReference(GLCameraModelFactory.createDefault())

    private val showAxis: AtomicBoolean = AtomicBoolean(false)

    private val showGrid: AtomicBoolean = AtomicBoolean(false)

    private val gridRotation: AtomicInteger = AtomicInteger(0)

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
            val canvas = GlimpsePanel()
            val animator = FPSAnimator(canvas, DEFAULT_FPS_LIMIT)

            scene = PreviewScene(animator)
            scene.updateModel(model)
            updatePresenter()
            canvas.setCallback(scene)

            canvas.addMouseWheelListener(ZoomingMouseWheelListener())
            val panningMouseInputListener = PanningMouseInputListener()
            canvas.addMouseListener(panningMouseInputListener)
            canvas.addMouseMotionListener(panningMouseInputListener)

            add(canvas, BorderLayout.CENTER)
            remove(placeholder)
            invalidate()

            scene.start()
        } catch (expected: Throwable) {
            showError(expected)
        }
    }

    private fun updatePresenter() {
        invokeLater(modalityState) {
            if (::scene.isInitialized) {
                scene.updateCameraModel(cameraModel.get())
                scene.updateAxes(this.showAxis.get())
                scene.updateGrid(showGrid.get())
                scene.updateSettings(settings.get())
            }
        }
    }

    private fun showError(exception: Throwable) {
        if (::scene.isInitialized) {
            scene.stop()
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
                distance = oldCameraModel.distance.coerceIn(
                    minimumValue = modelSize * DEFAULT_DISTANCE_FACTOR,
                    maximumValue = modelSize * MAX_DISTANCE_FACTOR
                )
            )
        }
    }

    private fun updateCameraModel(transform: (GLCameraModel) -> GLCameraModel) {
        cameraModel.getAndUpdate { oldCameraModel ->
            val newCameraModel = transform(oldCameraModel)
            updatePresenter()
            return@getAndUpdate newCameraModel
        }
    }

    fun updateUpVector(upVector: UpVector) {
        gridRotation.set(upVector.ordinal + 1)
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(upVector = upVector)
        }
    }

    fun updateAxes(showAxis: Boolean) {
        this.showAxis.set(showAxis)
        updatePresenter()
    }

    fun updateGrid(showGrid: Boolean) {
        this.showGrid.set(showGrid)
        updatePresenter()
    }

    fun updateGLPresenterSettings(newSettings: ObjPreviewFileEditorSettingsState) {
        if (newSettings != settings.getAndSet(newSettings)) {
            updatePresenter()
        }
    }

    fun zoomIn() {
        zoomBy(-ZOOM_PRECISION)
    }

    private fun zoomBy(zoomAmount: Float) {
        if (zoomAmount != 0f) {
            updateCameraModel { oldCameraModel ->
                oldCameraModel.zoomed(zoomAmount)
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

    fun zoomOut() {
        zoomBy(ZOOM_PRECISION)
    }

    fun zoomFit() {
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(distance = modelSize * DEFAULT_DISTANCE_FACTOR)
        }
    }

    override fun dispose() {
        if (::scene.isInitialized) {
            scene.stop()
        }
    }

    inner class ZoomingMouseWheelListener : MouseWheelListener {

        override fun mouseWheelMoved(event: MouseWheelEvent?) {
            zoomBy(zoomAmount = ZOOM_PRECISION * (event?.wheelRotation ?: 0))
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
            panningLongitude: Float,
            panningLatitude: Float
        ): GLCameraModel {
            val newLongitude = longitude + Angle.fromDeg(panningLongitude)
            val newLatitude = latitude + Angle.fromDeg(panningLatitude)
            return copy(
                longitude = newLongitude % Angle.fullAngle,
                latitude = newLatitude.coerceIn(
                    minimumValue = minLatitude,
                    maximumValue = maxLatitude
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

        private val minLatitude = Angle.fromDeg(deg = -89f)
        private val maxLatitude = Angle.fromDeg(deg = 89f)
    }
}
