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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.common.editor.PreviewEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState
import javax.swing.JComponent

class MtlMaterialEditor(
    project: Project,
    virtualFile: VirtualFile
) : PreviewEditor(), GLPreviewEditor {

    private val myComponent: MtlMaterialComponent = MtlMaterialComponent(project, virtualFile, this)

    var previewMesh: MaterialPreviewMesh
        get() = myComponent.previewMesh
        set(value) {
            myComponent.previewMesh = value
        }

    override var shadingMethod: ShadingMethod
        get() = myComponent.shadingMethod
        set(value) {
            myComponent.shadingMethod = value
        }

    override var environment: PBREnvironment
        get() = myComponent.environment
        set(value) {
            myComponent.environment = value
        }

    override var isCroppingTextures: Boolean
        get() = myComponent.isCroppingTextures
        set(value) {
            myComponent.isCroppingTextures = value
        }

    val material: MtlMaterialElement? get() = myComponent.material

    private val settingsChangedListener: WavefrontObjSettingsState.SettingsChangedListener =
        object : WavefrontObjSettingsState.SettingsChangedListener {

            override fun beforeSettingsChanged(newSettings: WavefrontObjSettingsState?) {
                val newMtlEditorSettings = newSettings?.mtlEditorSettings
                val oldMtlEditorSettings = WavefrontObjSettingsState.getInstance()?.mtlEditorSettings
                val newPreviewSettings = newSettings?.objPreviewSettings
                val oldPreviewSettings = WavefrontObjSettingsState.getInstance()?.objPreviewSettings

                if (previewMesh === oldMtlEditorSettings?.defaultPreviewMesh) {
                    previewMesh = newMtlEditorSettings?.defaultPreviewMesh ?: MaterialPreviewMesh.DEFAULT
                }

                if (shadingMethod === oldMtlEditorSettings?.defaultShadingMethod) {
                    shadingMethod = newMtlEditorSettings?.defaultShadingMethod ?: ShadingMethod.MTL_DEFAULT
                }

                if (environment === oldPreviewSettings?.defaultPBREnvironment) {
                    environment = newPreviewSettings?.defaultPBREnvironment ?: PBREnvironment.DEFAULT
                }

                if (isCroppingTextures == oldPreviewSettings?.cropTextures) {
                    isCroppingTextures = newPreviewSettings?.cropTextures
                        ?: ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
                }

                if (newPreviewSettings != null) {
                    myComponent.previewSceneConfig = PreviewSceneConfig.fromObjPreviewSettingsState(newPreviewSettings)
                }
            }
        }

    init {
        initializeFromSettings()
        observeSettingsChanges()
    }

    private fun initializeFromSettings() {
        val settings = WavefrontObjSettingsState.getInstance()
        val mtlEditorSettings = settings?.mtlEditorSettings
        val objPreviewSettings = settings?.objPreviewSettings

        previewMesh = mtlEditorSettings?.defaultPreviewMesh ?: MaterialPreviewMesh.DEFAULT
        shadingMethod = mtlEditorSettings?.defaultShadingMethod ?: ShadingMethod.MTL_DEFAULT

        environment = objPreviewSettings?.defaultPBREnvironment ?: PBREnvironment.DEFAULT
        isCroppingTextures = objPreviewSettings?.cropTextures ?: ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES

        if (objPreviewSettings != null) {
            myComponent.previewSceneConfig = PreviewSceneConfig.fromObjPreviewSettingsState(objPreviewSettings)
        }
    }

    private fun observeSettingsChanges() {
        ApplicationManager.getApplication().messageBus
            .connect(this)
            .subscribe(WavefrontObjSettingsState.SettingsChangedListener.TOPIC, settingsChangedListener)
    }

    override fun initPreview() {
        BackgroundTaskUtil.executeOnPooledThread(this) {
            myComponent.initialize()
        }
    }

    override fun getComponent(): JComponent = myComponent

    override fun getPreferredFocusedComponent(): JComponent = myComponent

    override fun getName(): String = WavefrontObjBundle.message("editor.fileTypes.mtl.material.name")

    override fun setState(state: FileEditorState) = Unit

    override fun dispose() {
        Disposer.dispose(myComponent)
    }

    override fun toggleCropTextures() {
        myComponent.toggleCropTextures()
    }

    override fun zoomIn() {
        myComponent.zoomIn()
    }

    override fun zoomOut() {
        myComponent.zoomOut()
    }

    override fun zoomFit() {
        myComponent.zoomFit()
    }

    override fun refresh() {
        myComponent.refresh()
    }
}
