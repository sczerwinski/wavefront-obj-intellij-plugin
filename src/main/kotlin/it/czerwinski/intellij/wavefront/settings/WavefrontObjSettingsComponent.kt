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

import com.intellij.icons.AllIcons
import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.ContextHelpLabel
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import com.intellij.ui.layout.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.SplitEditorLayout
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.settings.ui.SplitEditorLayoutListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.UpVectorListCellRenderer
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JPanel

class WavefrontObjSettingsComponent : WavefrontObjSettingsState.Holder, ObjPreviewFileEditorSettingsState.Holder {

    private lateinit var defaultUpVector: ComboBox<UpVector>
    private lateinit var showAxesCheckBox: JBCheckBox
    private lateinit var axisLineWidthInput: JBTextField
    private lateinit var showGridCheckBox: JBCheckBox
    private lateinit var showFineGridCheckBox: JBCheckBox
    private lateinit var gridLineWidthInput: JBTextField
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
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector"), separated = true) {
                defaultUpVector = comboBox(
                    EnumComboBoxModel(UpVector::class.java),
                    getter = { UpVector.DEFAULT },
                    setter = { },
                    UpVectorListCellRenderer()
                ).component
            }
            row(separated = true) {
                showAxesCheckBox = checkBox(
                    WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showAxes")
                ).component
                showAxesCheckBox.addChangeListener {
                    subRowsEnabled = showAxesCheckBox.isSelected
                }
                row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axisLineWidth")) {
                    cell {
                        axisLineWidthInput = floatTextField(
                            defaultValue = ObjPreviewFileEditorSettingsState.DEFAULT_AXIS_LINE_WIDTH,
                            errorMessage = { getLineWidthErrorMessage(it) }
                        ).component
                        contextHelpLabel(
                            description = WavefrontObjBundle.message(
                                "settings.editor.fileTypes.obj.preview.lineWidth.help"
                            )
                        )
                    }
                }
            }
            row(separated = true) {
                showGridCheckBox = checkBox(
                    WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showGrid")
                ).component
                showGridCheckBox.addChangeListener {
                    subRowsEnabled = showGridCheckBox.isSelected
                }
                row {
                    showFineGridCheckBox = checkBox(
                        WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showFineGrid")
                    ).component
                }
                row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.gridLineWidth")) {
                    cell {
                        gridLineWidthInput = floatTextField(
                            defaultValue = ObjPreviewFileEditorSettingsState.DEFAULT_GRID_LINE_WIDTH,
                            errorMessage = { getLineWidthErrorMessage(it) }
                        ).component
                        contextHelpLabel(
                            description = WavefrontObjBundle.message(
                                "settings.editor.fileTypes.obj.preview.lineWidth.help"
                            )
                        )
                    }
                }
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.lineWidth")) {
                cell {
                    lineWidthInput = floatTextField(
                        defaultValue = ObjPreviewFileEditorSettingsState.DEFAULT_LINE_WIDTH,
                        errorMessage = { getLineWidthErrorMessage(it) }
                    ).component
                    contextHelpLabel(
                        description = WavefrontObjBundle.message(
                            "settings.editor.fileTypes.obj.preview.lineWidth.help"
                        )
                    )
                }
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pointSize")) {
                pointSizeInput = floatTextField(
                    defaultValue = ObjPreviewFileEditorSettingsState.DEFAULT_POINT_SIZE,
                    errorMessage = { getPointSizeErrorMessage(it) }
                ).component
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
            showAxes = showAxesCheckBox.isSelected,
            axisLineWidth = axisLineWidthInput.text.toFloatOrNull()
                ?: ObjPreviewFileEditorSettingsState.DEFAULT_AXIS_LINE_WIDTH,
            showGrid = showGridCheckBox.isSelected,
            showFineGrid = showFineGridCheckBox.isSelected,
            gridLineWidth = gridLineWidthInput.text.toFloatOrNull()
                ?: ObjPreviewFileEditorSettingsState.DEFAULT_GRID_LINE_WIDTH,
            lineWidth = lineWidthInput.text.toFloatOrNull()
                ?: ObjPreviewFileEditorSettingsState.DEFAULT_LINE_WIDTH,
            pointSize = pointSizeInput.text.toFloatOrNull()
                ?: ObjPreviewFileEditorSettingsState.DEFAULT_POINT_SIZE
        )
        set(value) {
            defaultUpVector.selectedItem = value.defaultUpVector
            showAxesCheckBox.isSelected = value.showAxes
            axisLineWidthInput.text = value.axisLineWidth.toString()
            showGridCheckBox.isSelected = value.showGrid
            showFineGridCheckBox.isSelected = value.showFineGrid
            gridLineWidthInput.text = value.gridLineWidth.toString()
            lineWidthInput.text = value.lineWidth.toString()
            pointSizeInput.text = value.pointSize.toString()
        }

    private fun Cell.floatTextField(
        defaultValue: Float,
        errorMessage: (String) -> String
    ): CellBuilder<JBTextField> = textField(
        getter = { defaultValue.toString() },
        setter = { },
        columns = FLOAT_INPUT_COLUMNS
    ).withValidationOnInput {
        val value = it.text.toFloatOrNull()
        if (value == null) error(errorMessage)
        else null
    }

    private fun Cell.contextHelpLabel(
        label: String? = null,
        description: String,
        icon: Icon? = AllIcons.General.ContextHelp
    ): CellBuilder<ContextHelpLabel> =
        ContextHelpLabel(label.orEmpty(), description)
            .apply { this.icon = icon }
            .invoke()

    fun getComponent(): JComponent = mainPanel

    fun getPreferredFocusedComponent(): JComponent = defaultUpVector

    fun validateForm() {
        val errorMessage = when {
            axisLineWidthInput.text.toFloatOrNull() == null -> getLineWidthErrorMessage(axisLineWidthInput.text)
            gridLineWidthInput.text.toFloatOrNull() == null -> getLineWidthErrorMessage(gridLineWidthInput.text)
            lineWidthInput.text.toFloatOrNull() == null -> getLineWidthErrorMessage(lineWidthInput.text)
            pointSizeInput.text.toFloatOrNull() == null -> getPointSizeErrorMessage(pointSizeInput.text)
            else -> null
        }
        if (errorMessage != null) throw ConfigurationException(errorMessage)
    }

    private fun getLineWidthErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.lineWidth.error",
        value
    )

    private fun getPointSizeErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.pointSize.error",
        value
    )

    companion object {
        private const val FLOAT_INPUT_COLUMNS = 5
    }
}
