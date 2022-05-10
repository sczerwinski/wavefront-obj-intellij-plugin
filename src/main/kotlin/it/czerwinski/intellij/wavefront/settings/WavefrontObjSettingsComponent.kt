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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.ui.layout.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * UI component for Wavefront OBJ plugin settings.
 */
class WavefrontObjSettingsComponent : SettingsComponent, WavefrontObjSettingsState.Holder {

    private val objSplitEditorSettingsRow = ObjSplitEditorSettingsRow()
    private val objPreviewSettingsRow = ObjPreviewSettingsRow()

    private val mainPanel: JPanel = panel {
        titledRow(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.title")) {
            objSplitEditorSettingsRow.createRow(this)
        }
        titledRow(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.title")) {
            objPreviewSettingsRow.createRow(this)
        }
    }

    override var wavefrontObjSettings: WavefrontObjSettingsState
        get() = WavefrontObjSettingsState(
            objPreviewSettings = objPreviewSettingsRow.objPreviewSettings,
            defaultEditorLayout = objSplitEditorSettingsRow.defaultEditorLayout,
            isVerticalSplit = objSplitEditorSettingsRow.isVerticalSplit
        )
        set(value) {
            objPreviewSettingsRow.objPreviewSettings = value.objPreviewSettings
            objSplitEditorSettingsRow.defaultEditorLayout = value.defaultEditorLayout
            objSplitEditorSettingsRow.isVerticalSplit = value.isVerticalSplit
        }

    override fun getComponent(): JComponent = mainPanel

    override fun getPreferredFocusedComponent(): JComponent = objPreviewSettingsRow.getPreferredFocusedComponent()

    override fun validateForm() {
        objSplitEditorSettingsRow.validateForm()
        objPreviewSettingsRow.validateForm()
    }
}
