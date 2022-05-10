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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.common.editor.PreviewEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState
import javax.swing.JComponent

class ObjPreviewEditor(
    private val project: Project,
    virtualFile: VirtualFile
) : PreviewEditor() {

    private val myComponent: ObjPreviewComponent = ObjPreviewComponent(project, virtualFile, this)

    var shadingMethod: ShadingMethod
        get() = myComponent.shadingMethod
        set(value) {
            myComponent.shadingMethod = value
        }

    var environment: PBREnvironment
        get() = myComponent.environment
        set(value) {
            myComponent.environment = value
        }

    var isCroppingTextures: Boolean
        get() = myComponent.isCroppingTextures
        set(value) {
            myComponent.isCroppingTextures = value
        }

    var upVector: UpVector
        get() = myComponent.upVector
        set(value) {
            myComponent.upVector = value
        }

    var isShowingAxes: Boolean
        get() = myComponent.isShowingAxes
        set(value) {
            myComponent.isShowingAxes = value
        }

    var isShowingGrid: Boolean
        get() = myComponent.isShowingGrid
        set(value) {
            myComponent.isShowingGrid = value
        }

    private val settingsChangedListener: WavefrontObjSettingsState.SettingsChangedListener =
        object : WavefrontObjSettingsState.SettingsChangedListener {

            override fun beforeSettingsChanged(newSettings: WavefrontObjSettingsState?) {
                val newPreviewSettings = newSettings?.objPreviewSettings
                val oldPreviewSettings = WavefrontObjSettingsState.getInstance()?.objPreviewSettings

                if (shadingMethod === oldPreviewSettings?.defaultShadingMethod) {
                    shadingMethod = newPreviewSettings?.defaultShadingMethod ?: ShadingMethod.DEFAULT
                }

                if (environment === oldPreviewSettings?.defaultPBREnvironment) {
                    environment = newPreviewSettings?.defaultPBREnvironment ?: PBREnvironment.DEFAULT
                }

                if (isCroppingTextures == oldPreviewSettings?.cropTextures) {
                    isCroppingTextures = newPreviewSettings?.cropTextures
                        ?: ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
                }

                if (upVector === oldPreviewSettings?.defaultUpVector) {
                    upVector = newPreviewSettings?.defaultUpVector ?: UpVector.DEFAULT
                }

                if (isShowingAxes == oldPreviewSettings?.showAxes) {
                    isShowingAxes = newPreviewSettings?.showAxes ?: ObjPreviewSettingsState.DEFAULT_SHOW_AXES
                }

                if (isShowingGrid == oldPreviewSettings?.showGrid) {
                    isShowingGrid = newPreviewSettings?.showGrid ?: ObjPreviewSettingsState.DEFAULT_SHOW_GRID
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
        val settings = WavefrontObjSettingsState.getInstance()?.objPreviewSettings

        shadingMethod = settings?.defaultShadingMethod ?: ShadingMethod.DEFAULT
        environment = settings?.defaultPBREnvironment ?: PBREnvironment.DEFAULT
        isCroppingTextures = settings?.cropTextures ?: ObjPreviewSettingsState.DEFAULT_CROP_TEXTURES
        upVector = settings?.defaultUpVector ?: UpVector.DEFAULT
        isShowingAxes = settings?.showAxes ?: ObjPreviewSettingsState.DEFAULT_SHOW_AXES
        isShowingGrid = settings?.showGrid ?: ObjPreviewSettingsState.DEFAULT_SHOW_GRID

        if (settings != null) {
            myComponent.previewSceneConfig = PreviewSceneConfig.fromObjPreviewSettingsState(settings)
        }
    }

    private fun observeSettingsChanges() {
        ApplicationManager.getApplication().messageBus
            .connect(this)
            .subscribe(WavefrontObjSettingsState.SettingsChangedListener.TOPIC, settingsChangedListener)
    }

    fun initPreview() {
        val startupManager = StartupManager.getInstance(project)
        if (!startupManager.postStartupActivityPassed()) {
            startupManager.registerPostStartupActivity {
                myComponent.initialize()
            }
        } else {
            myComponent.initialize()
        }
    }

    override fun getComponent(): JComponent = myComponent

    override fun getPreferredFocusedComponent(): JComponent = myComponent

    override fun getName(): String = WavefrontObjBundle.message("editor.fileTypes.obj.preview.name")

    override fun setState(state: FileEditorState) = Unit

    override fun dispose() {
        Disposer.dispose(myComponent)
    }

    fun toggleCropTextures() {
        myComponent.toggleCropTextures()
    }

    fun toggleAxes() {
        myComponent.toggleAxes()
    }

    fun toggleGrid() {
        myComponent.toggleGrid()
    }

    fun zoomIn() {
        myComponent.zoomIn()
    }

    fun zoomOut() {
        myComponent.zoomOut()
    }

    fun zoomFit() {
        myComponent.zoomFit()
    }

    fun refresh() {
        myComponent.refresh()
    }
}
