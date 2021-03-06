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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.RowBuilder
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.settings.ui.SplitEditorLayoutListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.enumComboBox
import javax.swing.JComponent

/**
 * UI component for OBJ split editor settings.
 */
class ObjSplitEditorSettingsRow : SettingsRow {

    private lateinit var defaultEditorLayoutComboBox: ComboBox<SplitEditor.Layout>
    private lateinit var verticalSplitCheckBox: JBRadioButton
    private lateinit var horizontalSplitCheckBox: JBRadioButton

    var defaultEditorLayout: SplitEditor.Layout
        get() = defaultEditorLayoutComboBox.selectedItem as? SplitEditor.Layout ?: SplitEditor.Layout.DEFAULT
        set(value) {
            defaultEditorLayoutComboBox.selectedItem = value
        }

    var isVerticalSplit: Boolean
        get() = verticalSplitCheckBox.isSelected
        set(value) {
            verticalSplitCheckBox.isSelected = value
            horizontalSplitCheckBox.isSelected = !value
        }

    override fun createRow(rowBuilder: RowBuilder): Row = with(rowBuilder) {
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.default")) {
            defaultEditorLayoutComboBox = enumComboBox(
                defaultValue = SplitEditor.Layout.DEFAULT,
                renderer = SplitEditorLayoutListCellRenderer()
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.split")) {
            buttonGroup {
                row {
                    horizontalSplitCheckBox = radioButton(
                        WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.split.horizontal")
                    ).component
                }
                row {
                    verticalSplitCheckBox = radioButton(
                        WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.split.vertical")
                    ).component
                }
            }
        }
    }

    override fun getPreferredFocusedComponent(): JComponent = defaultEditorLayoutComboBox

    override fun validateForm() = Unit
}
