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
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.SplitEditorLayout
import it.czerwinski.intellij.wavefront.settings.ui.SplitEditorLayoutListCellRenderer
import javax.swing.JComponent
import javax.swing.JPanel

class WavefrontObjSettingsComponent {

    private val mainPanel: JPanel

    private val previewDisabledCheckBox = JBCheckBox(
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.disabled")
    )

    private val layoutComboBox = ComboBox(EnumComboBoxModel(SplitEditorLayout::class.java)).apply {
        renderer = SplitEditorLayoutListCellRenderer()
    }

    private val horizontalSplitRadioButton = JBRadioButton(
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.split.horizontal")
    )
    private val verticalSplitRadioButton = JBRadioButton(
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.split.vertical")
    )

    var isPreviewDisabled: Boolean
        get() = previewDisabledCheckBox.isSelected
        set(value) {
            previewDisabledCheckBox.isSelected = value
        }

    var defaultEditorLayout: SplitEditorLayout
        get() = layoutComboBox.selectedItem as? SplitEditorLayout ?: SplitEditorLayout.DEFAULT
        set(value) {
            layoutComboBox.selectedItem = value
        }

    var isVerticalSplit: Boolean
        get() = verticalSplitRadioButton.isSelected
        set(value) {
            horizontalSplitRadioButton.isSelected = !value
            verticalSplitRadioButton.isSelected = value
        }

    init {
        mainPanel = panel {
            titledRow(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.title")) {
                row {
                    previewDisabledCheckBox()
                }
                row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout")) {
                    previewDisabledCheckBox.addChangeListener {
                        visible = !previewDisabledCheckBox.isSelected
                        subRowsVisible = visible
                    }
                    layoutComboBox()
                }
                row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.split")) {
                    previewDisabledCheckBox.addChangeListener {
                        visible = !previewDisabledCheckBox.isSelected
                        subRowsVisible = visible
                    }
                    buttonGroup {
                        row { horizontalSplitRadioButton() }
                        row { verticalSplitRadioButton() }
                    }
                }
            }
        }
    }

    fun getComponent(): JComponent = mainPanel

    fun getPreferredFocusedComponent(): JComponent = layoutComboBox
}
