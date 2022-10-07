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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBUI
import com.jogamp.opengl.util.AnimatorBase
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.common.editor.EditorFooterComponent
import it.czerwinski.intellij.common.ui.ActionToolbarBuilder
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.ObjPreviewScene
import it.czerwinski.intellij.wavefront.editor.gl.PreviewScene
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModelFactory
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.GLModelFactory
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.util.isTextureFile
import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState
import java.awt.BorderLayout

class ObjPreviewComponent(
    private val project: Project,
    private val file: VirtualFile,
    parent: Disposable
) : ZoomablePreviewComponent(project, parent) {

    private var psiTreeChangeListener: MyPsiTreeChangeListener? = null

    private val myActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(myErrorLogSplitter)
            .build()

    private val myStatusBar: JBLabel = JBLabel()

    private lateinit var myScene: ObjPreviewScene

    override val scene: PreviewScene? get() = if (::myScene.isInitialized) myScene else null

    private var myModel: GLModel? = null
        set(value) {
            field = value
            BackgroundTaskUtil.executeOnPooledThread(this) {
                if (::myScene.isInitialized) {
                    myScene.model = value
                }
            }
        }

    override val modelSize: Float
        get() = (myModel?.size?.takeUnless { it == 0f }) ?: EMPTY_MODEL_SIZE

    var shadingMethod: ShadingMethod = ShadingMethod.DEFAULT
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsImmediately()
        }

    var environment: PBREnvironment = PBREnvironment.DEFAULT
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsImmediately()
        }

    var isCroppingTextures: Boolean = ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsImmediately()
        }

    var upVector: UpVector = UpVector.DEFAULT
        set(value) {
            field = value
            updateCameraModel { oldCameraModel ->
                oldCameraModel.copy(upVector = upVector)
            }
            updateScene()
            myActionToolbar.updateActionsImmediately()
        }

    var isShowingAxes: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_AXES
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsImmediately()
        }

    var isShowingGrid: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_GRID
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsImmediately()
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

    init {
        setLoadingText(WavefrontObjBundle.message("editor.fileTypes.obj.preview.placeholder"))
        startLoading()
        myStatusBar.border = JBUI.Borders.empty(STATUS_BAR_VERTICAL_BORDER, STATUS_BAR_HORIZONTAL_BORDER)
        add(
            EditorWithToolbar(
                toolbarComponent = EditorToolbarHeader(leftActionToolbar = myActionToolbar),
                editorComponent = myErrorLogSplitter,
                statusBarComponent = EditorFooterComponent().apply {
                    add(myStatusBar, BorderLayout.CENTER)
                }
            ),
            BorderLayout.CENTER
        )
    }

    override fun updateScene() {
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

    override fun onInitialize() {
        val psiManager = PsiManager.getInstance(project)
        val objFile = requireNotNull(psiManager.findFile(file) as? ObjFile)

        psiTreeChangeListener?.let { listener -> psiManager.removePsiTreeChangeListener(listener) }
        psiTreeChangeListener = MyPsiTreeChangeListener(objFile)
        psiTreeChangeListener?.let { listener -> psiManager.addPsiTreeChangeListener(listener, this) }

        updateObjFile(objFile)
    }

    private fun updateObjFile(objFile: ObjFile?) {
        myErrorLogSplitter.clearErrors()
        runReadAction {
            myModel = objFile?.let(GLModelFactory::create)
            updateCameraModel { oldCameraModel ->
                oldCameraModel.copy(
                    distance = oldCameraModel.distance.coerceIn(
                        minimumValue = modelSize * GLCameraModelFactory.DEFAULT_DISTANCE,
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

    override fun createScene(glimpsePanel: GlimpsePanel, animator: AnimatorBase) {
        myScene = ObjPreviewScene(glimpsePanel.glProfile, animator, myErrorLogSplitter)
        myScene.model = myModel
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

    private inner class MyPsiTreeChangeListener(private val file: ObjFile) : PsiTreeChangeAdapter() {

        val referencedFiles: List<PsiFile>
            get() = file.referencedMtlFiles.flatMap { mtlFile ->
                mtlFile.materials.flatMap { material -> material.texturePsiFiles } + mtlFile
            }

        private fun handlePsiTreeChange(element: PsiElement?) {
            if (element == file || element in referencedFiles || element.isTextureFile()) {
                updateObjFile(file)
            }
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            handlePsiTreeChange(event.file)
        }
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewComponent.Toolbar"

        private const val STATUS_BAR_VERTICAL_BORDER = 2
        private const val STATUS_BAR_HORIZONTAL_BORDER = 4

        private const val EMPTY_MODEL_SIZE = 1f
    }
}
