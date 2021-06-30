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

package it.czerwinski.intellij.wavefront.editor

import com.intellij.openapi.Disposable
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBLoadingPanel
import com.jogamp.opengl.math.FloatUtil
import com.jogamp.opengl.util.FPSAnimator
import graphics.glimpse.types.Angle
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.common.ui.ActionToolbarBuilder
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
import it.czerwinski.intellij.common.ui.ErrorLogSplitter
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.ObjPreviewScene
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModelFactory
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.GLModelFactory
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState
import java.awt.BorderLayout
import java.awt.event.MouseEvent
import java.awt.event.MouseWheelEvent
import java.awt.event.MouseWheelListener
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.event.MouseInputAdapter

class ObjPreviewComponent(
    project: Project,
    file: VirtualFile,
    editor: ObjPreviewEditor
) : JBLoadingPanel(BorderLayout(), editor), Disposable {

    private val isInitialized = AtomicBoolean(false)

    private val myErrorLogSplitter: ErrorLogSplitter = ErrorLogSplitter()

    private val myLeftActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(LEFT_TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(myErrorLogSplitter)
            .build()

    private val myRightActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(RIGHT_TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(myErrorLogSplitter)
            .build()

    private lateinit var myScene: ObjPreviewScene

    private var myModel: GLModel? = null
        set(value) {
            field = value
            BackgroundTaskUtil.executeOnPooledThread(this) {
                if (::myScene.isInitialized) {
                    myScene.model = value
                }
            }
        }

    private val modelSize: Float
        get() = (myModel?.size?.takeUnless { it == 0f }) ?: EMPTY_MODEL_SIZE

    var shadingMethod: ShadingMethod = ShadingMethod.DEFAULT
        set(value) {
            field = value
            updateScene()
            myLeftActionToolbar.updateActionsImmediately()
        }

    var upVector: UpVector = UpVector.DEFAULT
        set(value) {
            field = value
            updateCameraModel { oldCameraModel ->
                oldCameraModel.copy(upVector = upVector)
            }
            updateScene()
            myLeftActionToolbar.updateActionsImmediately()
        }

    var isShowingAxes: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_AXES
        set(value) {
            field = value
            updateScene()
            myLeftActionToolbar.updateActionsImmediately()
        }

    var isShowingGrid: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_GRID
        set(value) {
            field = value
            updateScene()
            myLeftActionToolbar.updateActionsImmediately()
        }

    var previewSceneConfig: PreviewSceneConfig = PreviewSceneConfig()
        set(value) {
            field = value
            updateScene()
        }

    private var cameraModel: GLCameraModel = GLCameraModelFactory.createDefault()

    init {
        setLoadingText(WavefrontObjBundle.message("editor.fileTypes.obj.preview.placeholder"))
        startLoading()
        add(
            EditorWithToolbar(
                toolbarComponent = EditorToolbarHeader(
                    leftActionToolbar = myLeftActionToolbar,
                    rightActionToolbar = myRightActionToolbar
                ),
                editorComponent = myErrorLogSplitter
            ),
            BorderLayout.CENTER
        )
        val objFile = PsiManager.getInstance(project).findFile(file) as? ObjFile
        updateObjFile(objFile)
        objFile?.let {
            PsiManager.getInstance(objFile.project)
                .addPsiTreeChangeListener(MyPsiTreeChangeListener(objFile), this)
        }
    }

    private fun updateObjFile(objFile: ObjFile?) {
        myErrorLogSplitter.clearErrors()
        runReadAction {
            myModel = objFile?.let(GLModelFactory::create)
            updateCameraModel { oldCameraModel ->
                oldCameraModel.copy(
                    distance = oldCameraModel.distance.coerceIn(
                        minimumValue = modelSize * DEFAULT_DISTANCE_FACTOR,
                        maximumValue = modelSize * MAX_DISTANCE_FACTOR
                    )
                )
            }
        }
    }

    private fun updateCameraModel(transform: (GLCameraModel) -> GLCameraModel) {
        cameraModel = transform(cameraModel)
        updateScene()
    }

    fun initialize() {
        myRightActionToolbar.updateActionsImmediately()
        if (isInitialized.compareAndSet(false, true)) {
            BackgroundTaskUtil.executeOnPooledThread(this) {
                try {
                    val glimpsePanel = GlimpsePanel()
                    val animator = FPSAnimator(glimpsePanel, DEFAULT_FPS_LIMIT)

                    myScene = ObjPreviewScene(glimpsePanel.glProfile, animator, myErrorLogSplitter)

                    myScene.model = myModel
                    updateScene()
                    glimpsePanel.setCallback(myScene)

                    glimpsePanel.addMouseWheelListener(ZoomingMouseWheelListener())
                    val panningMouseInputListener = PanningMouseInputListener()
                    glimpsePanel.addMouseListener(panningMouseInputListener)
                    glimpsePanel.addMouseMotionListener(panningMouseInputListener)

                    myErrorLogSplitter.component = glimpsePanel
                    myScene.start()
                    stopLoading()
                } catch (expected: Throwable) {
                    myErrorLogSplitter.addError(
                        WavefrontObjBundle.message("editor.fileTypes.obj.preview.error"),
                        expected
                    )
                    deinitialize()
                }
            }
        }
    }

    private fun updateScene() {
        invokeLater(ModalityState.stateForComponent(this)) {
            if (::myScene.isInitialized) {
                myScene.cameraModel = cameraModel
                myScene.shadingMethod = shadingMethod
                myScene.showAxes = isShowingAxes
                myScene.showGrid = isShowingGrid
                myScene.config = previewSceneConfig
            }
        }
    }

    private fun deinitialize() {
        if (::myScene.isInitialized) myScene.stop()
        startLoading()
        isInitialized.set(false)
    }

    fun toggleAxes() {
        isShowingAxes = !isShowingAxes
    }

    fun toggleGrid() {
        isShowingGrid = !isShowingGrid
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

    fun refresh() {
        deinitialize()
        myErrorLogSplitter.clearErrors()
        initialize()
    }

    override fun dispose() {
        if (::myScene.isInitialized) {
            myScene.stop()
        }
    }

    private inner class MyPsiTreeChangeListener(private val file: ObjFile) : PsiTreeChangeAdapter() {

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            if (event.file == file) {
                updateObjFile(file)
            }
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
        private const val LEFT_TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewFileEditor.Toolbar"
        private const val RIGHT_TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewFileEditor.AdditionalToolbar"

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
