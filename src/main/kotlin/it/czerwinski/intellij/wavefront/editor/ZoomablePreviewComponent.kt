/*
 * Copyright 2020-2023 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.editor

import com.intellij.openapi.Disposable
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.Magnificator
import com.jogamp.opengl.math.FloatUtil
import com.jogamp.opengl.util.AnimatorBase
import com.jogamp.opengl.util.FPSAnimator
import graphics.glimpse.types.Angle
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.common.ui.ErrorLogSplitter
import it.czerwinski.intellij.common.ui.GlimpseViewport
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.PreviewScene
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModelFactory
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.event.MouseInputAdapter

abstract class ZoomablePreviewComponent(
    private val project: Project,
    parent: Disposable
) : JBLoadingPanel(BorderLayout(), parent), Zoomable, Refreshable, Disposable {

    private val isInitialized = AtomicBoolean(false)

    protected val myErrorLogSplitter: ErrorLogSplitter = ErrorLogSplitter()

    protected abstract val scene: PreviewScene?

    protected abstract val modelSize: Float

    protected var cameraModel: GLCameraModel = GLCameraModelFactory.createDefault()

    fun initialize() {
        if (project.isInitialized && isInitialized.compareAndSet(false, true)) {
            try {
                onInitialize()

                val glimpsePanel = createGlimpsePanel()

                BackgroundTaskUtil.executeOnPooledThread(this) {
                    try {
                        val animator = FPSAnimator(glimpsePanel, DEFAULT_FPS_LIMIT)
                        createScene(glimpsePanel, animator)
                        updateScene()
                        scene?.let(glimpsePanel::setCallback)
                        scene?.start()
                        stopLoading()
                    } catch (expected: Throwable) {
                        onInitializeError(expected)
                    }
                }
            } catch (expected: Throwable) {
                onInitializeError(expected)
            }
        }
    }

    abstract fun onInitialize()

    abstract fun createScene(glimpsePanel: GlimpsePanel, animator: AnimatorBase)

    private fun createGlimpsePanel(): GlimpsePanel {
        val glimpsePanel = GlimpsePanel()

        glimpsePanel.addMouseWheelListener(ZoomingMouseWheelListener())
        val panningMouseInputListener = PanningMouseInputListener()
        glimpsePanel.addMouseListener(panningMouseInputListener)
        glimpsePanel.addMouseMotionListener(panningMouseInputListener)

        glimpsePanel.putClientProperty(
            Magnificator.CLIENT_PROPERTY_KEY,
            Magnificator { scale, at ->
                zoomBy(zoomFactor = 1f / scale.toFloat())
                return@Magnificator at
            }
        )

        myErrorLogSplitter.component = GlimpseViewport(glimpsePanel)

        return glimpsePanel
    }

    private fun onInitializeError(expected: Throwable) {
        myErrorLogSplitter.addError(
            WavefrontObjBundle.message("editor.common.preview.error"),
            expected
        )
        deinitialize()
        stopLoading()
    }

    private fun deinitialize() {
        scene?.stop()
        isInitialized.set(false)
    }

    override fun zoomIn() {
        zoomBy(FloatUtil.pow(ZOOM_BASE, -ZOOM_PRECISION))
    }

    private fun zoomBy(zoomFactor: Float) {
        if (zoomFactor != 1f) {
            updateCameraModel { oldCameraModel ->
                oldCameraModel.zoomed(zoomFactor)
            }
        }
    }

    protected fun updateCameraModel(transform: (GLCameraModel) -> GLCameraModel) {
        cameraModel = transform(cameraModel)
        updateScene()
    }

    protected abstract fun updateScene()

    private fun GLCameraModel.zoomed(zoomFactor: Float): GLCameraModel {
        val newDistance = distance * zoomFactor
        return copy(
            distance = newDistance.coerceIn(
                minimumValue = modelSize * MIN_DISTANCE_FACTOR,
                maximumValue = modelSize * MAX_DISTANCE_FACTOR
            )
        )
    }

    override fun zoomOut() {
        zoomBy(FloatUtil.pow(ZOOM_BASE, ZOOM_PRECISION))
    }

    override fun zoomFit() {
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(distance = modelSize * GLCameraModelFactory.DEFAULT_DISTANCE)
        }
    }

    override fun refresh() {
        startLoading()
        deinitialize()
        myErrorLogSplitter.clearErrors()
        initialize()
    }

    override fun dispose() {
        scene?.stop()
    }

    inner class ZoomingMouseWheelListener : MouseWheelListener {

        override fun mouseWheelMoved(event: MouseWheelEvent?) {
            zoomBy(zoomFactor = FloatUtil.pow(ZOOM_BASE, ZOOM_PRECISION * (event?.wheelRotation ?: 0)))
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
                longitude = newLongitude % Angle.fullAngle(),
                latitude = newLatitude.coerceIn(
                    minimumValue = minLatitude,
                    maximumValue = maxLatitude
                )
            )
        }
    }

    companion object {
        private const val DEFAULT_FPS_LIMIT = 10

        private const val MIN_DISTANCE_FACTOR = .1f
        internal const val MAX_DISTANCE_FACTOR = 10f

        private const val ZOOM_BASE = 2f
        private const val ZOOM_PRECISION = .1f

        private const val PAN_PRECISION = .5f

        private val minLatitude = Angle.fromDeg(deg = -89f)
        private val maxLatitude = Angle.fromDeg(deg = 89f)
    }
}
