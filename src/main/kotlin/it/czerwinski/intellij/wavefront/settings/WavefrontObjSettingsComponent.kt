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

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.SplitEditorLayout
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.settings.ui.SplitEditorLayoutListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.UpVectorListCellRenderer
import javax.swing.JComponent
import javax.swing.JPanel

class WavefrontObjSettingsComponent : WavefrontObjSettingsState.Holder, ObjPreviewFileEditorSettingsState.Holder {

    private lateinit var defaultUpVector: ComboBox<UpVector>
    private lateinit var lineWidthInput: JBTextField
    private lateinit var pointSizeInput: JBTextField
    private lateinit var defaultEditorLayout: ComboBox<SplitEditorLayout>
    private lateinit var verticalSplitCheckBox: JBRadioButton
    private lateinit var horizontalSplitCheckBox: JBRadioButton

    private val mainPanel: JPanel = panel {
        titledRow(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.title")) {
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.layout.default")) {
                defaultEditorLayout = comboBox(
                    EnumComboBoxModel(SplitEditorLayout::class.java),
                    getter = { SplitEditorLayout.DEFAULT },
                    setter = { },
                    SplitEditorLayoutListCellRenderer()
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
        titledRow(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.title")) {
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector")) {
                defaultUpVector = comboBox(
                    EnumComboBoxModel(UpVector::class.java),
                    getter = { UpVector.DEFAULT },
                    setter = { },
                    UpVectorListCellRenderer()
                ).component
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.lineWidth")) {
                lineWidthInput = textField(
                    getter = { ObjPreviewFileEditorSettingsState.DEFAULT_LINE_WIDTH.toString() },
                    setter = { }
                ).withValidationOnInput {
                    val value = it.text.toFloatOrNull()
                    if (value == null) error(getLineWidthErrorMessage(it.text))
                    else null
                }.component
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pointSize")) {
                pointSizeInput = textField(
                    getter = { ObjPreviewFileEditorSettingsState.DEFAULT_POINT_SIZE.toString() },
                    setter = { }
                ).withValidationOnInput {
                    val value = it.text.toFloatOrNull()
                    if (value == null) error(getPointSizeErrorMessage(it.text))
                    else null
                }.component
            }
        }
    }

    override var wavefrontObjSettings: WavefrontObjSettingsState
        get() = WavefrontObjSettingsState(
            objPreviewFileEditorSettings = objPreviewFileEditorSettings,
            defaultEditorLayout = defaultEditorLayout.selectedItem as? SplitEditorLayout ?: SplitEditorLayout.DEFAULT,
            isVerticalSplit = verticalSplitCheckBox.isSelected
        )
        set(value) {
            objPreviewFileEditorSettings = value.objPreviewFileEditorSettings
            defaultEditorLayout.selectedItem = value.defaultEditorLayout
            verticalSplitCheckBox.isSelected = value.isVerticalSplit
            horizontalSplitCheckBox.isSelected = value.isHorizontalSplit
        }

    override var objPreviewFileEditorSettings: ObjPreviewFileEditorSettingsState
        get() = ObjPreviewFileEditorSettingsState(
            defaultUpVector = defaultUpVector.selectedItem as? UpVector ?: UpVector.DEFAULT,
            lineWidth = lineWidthInput.text.toFloatOrNull() ?: ObjPreviewFileEditorSettingsState.DEFAULT_LINE_WIDTH,
            pointSize = pointSizeInput.text.toFloatOrNull() ?: ObjPreviewFileEditorSettingsState.DEFAULT_POINT_SIZE
        )
        set(value) {
            defaultUpVector.selectedItem = value.defaultUpVector
            lineWidthInput.text = value.lineWidth.toString()
            pointSizeInput.text = value.pointSize.toString()
        }

    fun getComponent(): JComponent = mainPanel

    fun getPreferredFocusedComponent(): JComponent = defaultUpVector

    fun validateForm() {
        if (lineWidthInput.text.toFloatOrNull() == null) {
            throw ConfigurationException(getLineWidthErrorMessage(lineWidthInput.text))
        }
        if (pointSizeInput.text.toFloatOrNull() == null) {
            throw ConfigurationException(getPointSizeErrorMessage(pointSizeInput.text))
        }
    }

    private fun getLineWidthErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.lineWidth.error",
        value
    )

    private fun getPointSizeErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.pointSize.error",
        value
    )
}
