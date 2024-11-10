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
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.project.Project
import com.jogamp.opengl.util.AnimatorBase
import graphics.glimpse.ui.GlimpsePanel
import it.czerwinski.intellij.common.ui.ActionToolbarBuilder
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.MtlPreviewScene
import it.czerwinski.intellij.wavefront.editor.gl.PreviewScene
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState
import java.awt.BorderLayout

class MtlMaterialPreviewComponent(
    project: Project,
    parent: Disposable
) : ZoomablePreviewComponent(project, parent) {

    private var myMaterial: MtlMaterialElement? = null

    private val myActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(myErrorLogSplitter)
            .build()

    private lateinit var myScene: MtlPreviewScene

    override val scene: PreviewScene? get() = if (::myScene.isInitialized) myScene else null

    override val modelSize: Float get() = MtlPreviewScene.MODEL_SIZE

    var previewMesh: MaterialPreviewMesh = MaterialPreviewMesh.DEFAULT
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsAsync()
        }

    var shadingMethod: ShadingMethod = ShadingMethod.MTL_DEFAULT
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsAsync()
        }

    var environment: PBREnvironment = PBREnvironment.DEFAULT
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsAsync()
        }

    var isCroppingTextures: Boolean = ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
        set(value) {
            field = value
            updateScene()
            myActionToolbar.updateActionsAsync()
        }

    var previewSceneConfig: PreviewSceneConfig = PreviewSceneConfig()
        set(value) {
            val oldValue = field
            field = value
            if (value.needsFullRefresh(oldValue)) {
                refresh()
            } else {
                updateScene()
            }
        }

    init {
        setLoadingText(WavefrontObjBundle.message("editor.fileTypes.mtl.material.preview.placeholder"))
        startLoading()
        add(
            EditorWithToolbar(
                toolbarComponent = EditorToolbarHeader(leftActionToolbar = myActionToolbar),
                editorComponent = myErrorLogSplitter
            ),
            BorderLayout.CENTER
        )
    }

    override fun onInitialize() = Unit

    override fun createScene(glimpsePanel: GlimpsePanel, animator: AnimatorBase) {
        myScene = MtlPreviewScene(glimpsePanel.glProfile, parent = this, animator, myErrorLogSplitter)
            .apply { addLoadingListener { _, loading -> if (loading) startLoading() else stopLoading() } }
        myScene.material = myMaterial
    }

    override fun updateScene() {
        invokeLater(ModalityState.stateForComponent(this)) {
            if (::myScene.isInitialized) {
                myScene.cameraModel = cameraModel
                myScene.previewMesh = previewMesh
                myScene.shadingMethod = shadingMethod
                myScene.environment = environment
                myScene.cropTextures = isCroppingTextures
                myScene.showAxes = false
                myScene.showGrid = false
                myScene.config = previewSceneConfig
            }
        }
    }

    fun updateMaterial(material: MtlMaterialElement?) {
        myErrorLogSplitter.clearErrors()
        myMaterial = material
        if (::myScene.isInitialized) {
            myScene.material = myMaterial
        }
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "MtlMaterialPreviewComponent.Toolbar"
    }
}
