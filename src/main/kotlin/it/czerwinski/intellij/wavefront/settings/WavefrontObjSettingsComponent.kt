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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * UI component for Wavefront OBJ plugin settings.
 */
class WavefrontObjSettingsComponent : SettingsComponent, WavefrontObjSettingsState.Holder {

    private val objSplitEditorSettingsGroup = ObjSplitEditorSettingsGroup()
    private val objPreviewSettingsGroup = ObjPreviewSettingsGroup()
    private val mtlEditorSettingsGroup = MtlEditorSettingsGroup()

    private val mainPanel: JPanel = panel {
        group(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.title")) {
            objSplitEditorSettingsGroup.createGroupContents(this)
        }
        group(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.title")) {
            objPreviewSettingsGroup.createGroupContents(this)
        }
        group(WavefrontObjBundle.message("settings.editor.fileTypes.mtl.material.title")) {
            mtlEditorSettingsGroup.createGroupContents(this)
        }
    }

    override var wavefrontObjSettings: WavefrontObjSettingsState
        get() = WavefrontObjSettingsState(
            objPreviewSettings = objPreviewSettingsGroup.objPreviewSettings,
            mtlEditorSettings = mtlEditorSettingsGroup.mtlEditorSettings,
            defaultEditorLayout = objSplitEditorSettingsGroup.defaultEditorLayout,
            isVerticalSplit = objSplitEditorSettingsGroup.isVerticalSplit
        )
        set(value) {
            objPreviewSettingsGroup.objPreviewSettings = value.objPreviewSettings
            mtlEditorSettingsGroup.mtlEditorSettings = value.mtlEditorSettings
            objSplitEditorSettingsGroup.defaultEditorLayout = value.defaultEditorLayout
            objSplitEditorSettingsGroup.isVerticalSplit = value.isVerticalSplit
        }

    override fun getComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent = objPreviewSettingsGroup.getPreferredFocusedComponent()

    override fun validateForm() {
        objSplitEditorSettingsGroup.validateForm()
        objPreviewSettingsGroup.validateForm()
    }
}
