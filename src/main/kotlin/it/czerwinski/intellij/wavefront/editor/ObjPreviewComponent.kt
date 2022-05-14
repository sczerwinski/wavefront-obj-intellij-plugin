/*
 * Copyright 2020-2022 Slawomir Czerwinski
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
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.Magnificator
import com.intellij.util.ui.JBUI
import com.jogamp.opengl.math.FloatUtil
import com.jogamp.opengl.util.FPSAnimator
import graphics.glimpse.types.Angle
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.common.editor.EditorFooterComponent
import it.czerwinski.intellij.common.ui.ActionToolbarBuilder
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
import it.czerwinski.intellij.common.ui.ErrorLogSplitter
import it.czerwinski.intellij.common.ui.GlimpseViewport
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.ObjPreviewScene
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModelFactory
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.GLModelFactory
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
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
    private val project: Project,
    private val file: VirtualFile,
    editor: ObjPreviewEditor
) : JBLoadingPanel(BorderLayout(), editor), Disposable {

    private val isInitialized = AtomicBoolean(false)

    private var psiTreeChangeListener: MyPsiTreeChangeListener? = null

    private val myErrorLogSplitter: ErrorLogSplitter = ErrorLogSplitter()

    private val myLeftActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(LEFT_TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(myErrorLogSplitter)
            .build()

    private val myStatusBar: JBLabel = JBLabel()

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

    var environment: PBREnvironment = PBREnvironment.DEFAULT
        set(value) {
            field = value
            updateScene()
            myLeftActionToolbar.updateActionsImmediately()
        }

    var isCroppingTextures: Boolean = ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
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
            val oldValue = field
            field = value
            if (value.shaderQuality != oldValue.shaderQuality) {
                refresh()
            } else {
                updateScene()
            }
        }

    private var cameraModel: GLCameraModel = GLCameraModelFactory.createDefault()

    init {
        setLoadingText(WavefrontObjBundle.message("editor.fileTypes.obj.preview.placeholder"))
        startLoading()
        myStatusBar.border = JBUI.Borders.empty(STATUS_BAR_VERTICAL_BORDER, STATUS_BAR_HORIZONTAL_BORDER)
        add(
            EditorWithToolbar(
                toolbarComponent = EditorToolbarHeader(leftActionToolbar = myLeftActionToolbar),
                editorComponent = myErrorLogSplitter,
                statusBarComponent = EditorFooterComponent().apply {
                    add(myStatusBar, BorderLayout.CENTER)
                }
            ),
            BorderLayout.CENTER
        )
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
            myStatusBar.text = if (objFile != null) {
                WavefrontObjBundle.message(
                    key = "editor.fileTypes.obj.preview.statusFormat",
                    objFile.objectsCount,
                    objFile.groupsCount,
                    objFile.verticesCount,
                    objFile.facesCount,
                    objFile.trianglesCount
                )
            } else ""
        }
    }

    private fun updateCameraModel(transform: (GLCameraModel) -> GLCameraModel) {
        cameraModel = transform(cameraModel)
        updateScene()
    }

    private fun updateScene() {
        invokeLater(ModalityState.stateForComponent(this)) {
            if (::myScene.isInitialized) {
                myScene.cameraModel = cameraModel
                myScene.shadingMethod = shadingMethod
                myScene.environment = environment
                myScene.cropTextures = isCroppingTextures
                myScene.showAxes = isShowingAxes
                myScene.showGrid = isShowingGrid
                myScene.config = previewSceneConfig
            }
        }
    }

    fun initialize() {
        if (project.isInitialized && isInitialized.compareAndSet(false, true)) {
            try {
                initializeObjFile()

                val glimpsePanel = createGlimpsePanel()

                BackgroundTaskUtil.executeOnPooledThread(this) {
                    try {
                        val animator = FPSAnimator(glimpsePanel, DEFAULT_FPS_LIMIT)

                        myScene = ObjPreviewScene(glimpsePanel.glProfile, animator, myErrorLogSplitter)

                        myScene.model = myModel
                        updateScene()
                        glimpsePanel.setCallback(myScene)

                        myScene.start()
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

    private fun initializeObjFile() {
        val psiManager = PsiManager.getInstance(project)
        val objFile = requireNotNull(psiManager.findFile(file) as? ObjFile)

        psiTreeChangeListener?.let { listener -> psiManager.removePsiTreeChangeListener(listener) }
        psiTreeChangeListener = MyPsiTreeChangeListener(objFile)
        psiTreeChangeListener?.let { listener -> psiManager.addPsiTreeChangeListener(listener, this) }

        updateObjFile(objFile)
    }

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
            WavefrontObjBundle.message("editor.fileTypes.obj.preview.error"),
            expected
        )
        deinitialize()
        stopLoading()
    }

    private fun deinitialize() {
        if (::myScene.isInitialized) myScene.stop()
        isInitialized.set(false)
    }

    fun toggleCropTextures() {
        isCroppingTextures = !isCroppingTextures
    }

    fun toggleAxes() {
        isShowingAxes = !isShowingAxes
    }

    fun toggleGrid() {
        isShowingGrid = !isShowingGrid
    }

    fun zoomIn() {
        zoomBy(FloatUtil.pow(ZOOM_BASE, -ZOOM_PRECISION))
    }

    private fun zoomBy(zoomFactor: Float) {
        if (zoomFactor != 1f) {
            updateCameraModel { oldCameraModel ->
                oldCameraModel.zoomed(zoomFactor)
            }
        }
    }

    private fun GLCameraModel.zoomed(zoomFactor: Float): GLCameraModel {
        val newDistance = distance * zoomFactor
        return copy(
            distance = newDistance.coerceIn(
                minimumValue = modelSize * MIN_DISTANCE_FACTOR,
                maximumValue = modelSize * MAX_DISTANCE_FACTOR
            )
        )
    }

    fun zoomOut() {
        zoomBy(FloatUtil.pow(ZOOM_BASE, ZOOM_PRECISION))
    }

    fun zoomFit() {
        updateCameraModel { oldCameraModel ->
            oldCameraModel.copy(distance = modelSize * DEFAULT_DISTANCE_FACTOR)
        }
    }

    fun refresh() {
        startLoading()
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

        val referencedFiles: List<PsiFile>
            get() = file.referencedMtlFiles.flatMap { mtlFile ->
                mtlFile.materials.flatMap { material -> material.texturePsiFiles } + mtlFile
            }

        private fun onPsiTreeChangeEvent(event: PsiTreeChangeEvent) {
            if (event.file == file || event.file in referencedFiles) {
                updateObjFile(file)
            }
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            onPsiTreeChangeEvent(event)
        }
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

        private const val STATUS_BAR_VERTICAL_BORDER = 2
        private const val STATUS_BAR_HORIZONTAL_BORDER = 4

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
