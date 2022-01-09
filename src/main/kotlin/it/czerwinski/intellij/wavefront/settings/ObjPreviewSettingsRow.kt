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
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder
import com.intellij.ui.layout.Row
import com.intellij.ui.layout.RowBuilder
import com.intellij.ui.layout.slider
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.ShaderQuality
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.settings.ui.PBREnvironmentListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.ShaderQualityListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.ShadingMethodListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.UpVectorListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.enumComboBox
import it.czerwinski.intellij.wavefront.settings.ui.textField
import java.util.Hashtable
import javax.swing.Icon
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JSlider
import kotlin.math.roundToInt

/**
 * UI component for OBJ file 3D preview settings.
 */
class ObjPreviewSettingsRow : SettingsRow, ObjPreviewSettingsState.Holder {

    private lateinit var defaultShadingMethod: ComboBox<ShadingMethod>
    private lateinit var defaultPBREnvironment: ComboBox<PBREnvironment>
    private lateinit var defaultUpVector: ComboBox<UpVector>
    private lateinit var showAxesCheckBox: JBCheckBox
    private lateinit var axisLineWidthInput: JBTextField
    private lateinit var showAxesLabelsCheckBox: JBCheckBox
    private lateinit var axisLabelFontSizeInput: JBTextField
    private lateinit var showGridCheckBox: JBCheckBox
    private lateinit var showFineGridCheckBox: JBCheckBox
    private lateinit var gridLineWidthInput: JBTextField
    private lateinit var lineWidthInput: JBTextField
    private lateinit var pointSizeInput: JBTextField
    private lateinit var cropTexturesCheckBox: JBCheckBox
    private lateinit var shaderQualityComboBox: ComboBox<ShaderQuality>
    private lateinit var displacementQualitySlider: JSlider

    override var objPreviewSettings: ObjPreviewSettingsState
        get() = ObjPreviewSettingsState(
            defaultShadingMethod = defaultShadingMethod.item ?: ShadingMethod.DEFAULT,
            defaultPBREnvironment  = defaultPBREnvironment.item ?: PBREnvironment.DEFAULT,
            defaultUpVector = defaultUpVector.item ?: UpVector.DEFAULT,
            showAxes = showAxesCheckBox.isSelected,
            axisLineWidth = axisLineWidthInput.text.toFloatOrNull() ?: INVALID_FLOAT_VALUE,
            showAxesLabels = showAxesLabelsCheckBox.isSelected,
            axisLabelFontSize = axisLabelFontSizeInput.text.toFloatOrNull() ?: INVALID_FLOAT_VALUE,
            showGrid = showGridCheckBox.isSelected,
            showFineGrid = showFineGridCheckBox.isSelected,
            gridLineWidth = gridLineWidthInput.text.toFloatOrNull() ?: INVALID_FLOAT_VALUE,
            lineWidth = lineWidthInput.text.toFloatOrNull() ?: INVALID_FLOAT_VALUE,
            pointSize = pointSizeInput.text.toFloatOrNull() ?: INVALID_FLOAT_VALUE,
            cropTextures = cropTexturesCheckBox.isSelected,
            shaderQuality = shaderQualityComboBox.item ?: ShaderQuality.DEFAULT,
            displacementQuality = displacementQualitySlider.value * DISPLACEMENT_QUALITY_FACTOR
        )
        set(value) {
            defaultShadingMethod.item = value.defaultShadingMethod
            defaultPBREnvironment.item = value.defaultPBREnvironment
            defaultUpVector.item = value.defaultUpVector
            showAxesCheckBox.isSelected = value.showAxes
            axisLineWidthInput.text = value.axisLineWidth.toString()
            showAxesLabelsCheckBox.isSelected = value.showAxesLabels
            axisLabelFontSizeInput.text = value.axisLabelFontSize.toString()
            showGridCheckBox.isSelected = value.showGrid
            showFineGridCheckBox.isSelected = value.showFineGrid
            gridLineWidthInput.text = value.gridLineWidth.toString()
            lineWidthInput.text = value.lineWidth.toString()
            pointSizeInput.text = value.pointSize.toString()
            cropTexturesCheckBox.isSelected = value.cropTextures
            shaderQualityComboBox.item = value.shaderQuality
            displacementQualitySlider.value = (value.displacementQuality / DISPLACEMENT_QUALITY_FACTOR).roundToInt()
        }

    override fun createRow(rowBuilder: RowBuilder): Row = with(rowBuilder) {
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.shadingMethod")) {
            defaultShadingMethod = enumComboBox(
                defaultValue = ShadingMethod.DEFAULT,
                renderer = ShadingMethodListCellRenderer()
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment")) {
            defaultPBREnvironment = enumComboBox(
                defaultValue = PBREnvironment.DEFAULT,
                renderer = PBREnvironmentListCellRenderer()
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector"), separated = true) {
            defaultUpVector = enumComboBox(
                defaultValue = UpVector.DEFAULT,
                renderer = UpVectorListCellRenderer()
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axes"), separated = true) {
            createAxesRow()
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.grid"), separated = true) {
            createGridRow()
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.rendering")) {
            createRenderingRow()
        }
    }

    private fun Row.createAxesRow() {
        row {
            showAxesCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showAxes")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axisLineWidth")) {
            cell {
                axisLineWidthInput = textField(
                    defaultValue = ObjPreviewSettingsState.DEFAULT_AXIS_LINE_WIDTH,
                    columns = FLOAT_INPUT_COLUMNS
                ).component
                contextHelpLabel(description = lineWidthContextHelpMessage)
            }
        }
        row {
            showAxesLabelsCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showAxesLabels")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axisLabelFontSize")) {
            cell {
                axisLabelFontSizeInput = textField(
                    defaultValue = ObjPreviewSettingsState.DEFAULT_AXIS_LABEL_FONT_SIZE,
                    columns = FLOAT_INPUT_COLUMNS
                ).component
            }
        }
    }

    private fun Row.createGridRow() {
        row {
            showGridCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showGrid")
            ).component
        }
        row {
            showFineGridCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showFineGrid")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.gridLineWidth")) {
            cell {
                gridLineWidthInput = textField(
                    defaultValue = ObjPreviewSettingsState.DEFAULT_GRID_LINE_WIDTH,
                    columns = FLOAT_INPUT_COLUMNS
                ).component
                contextHelpLabel(description = lineWidthContextHelpMessage)
            }
        }
    }

    private fun Row.createRenderingRow() {
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.lineWidth")) {
            cell {
                lineWidthInput = textField(
                    defaultValue = ObjPreviewSettingsState.DEFAULT_LINE_WIDTH,
                    columns = FLOAT_INPUT_COLUMNS
                ).component
                contextHelpLabel(description = lineWidthContextHelpMessage)
            }
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pointSize")) {
            pointSizeInput = textField(
                defaultValue = ObjPreviewSettingsState.DEFAULT_POINT_SIZE,
                columns = FLOAT_INPUT_COLUMNS
            ).component
        }
        row {
            cropTexturesCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.cropTextures")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.shaderQuality")) {
            cell {
                shaderQualityComboBox = enumComboBox(
                    defaultValue = ShaderQuality.DEFAULT,
                    renderer = ShaderQualityListCellRenderer()
                ).component
                contextHelpLabel(
                    description = WavefrontObjBundle.message(
                        "settings.editor.fileTypes.obj.preview.shaderQuality.contextHelp"
                    )
                )
            }
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.displacementQuality")) {
            cell {
                displacementQualitySlider = slider(
                    DISPLACEMENT_QUALITY_MIN,
                    DISPLACEMENT_QUALITY_MAX,
                    DISPLACEMENT_QUALITY_MINOR_TICK,
                    DISPLACEMENT_QUALITY_MAJOR_TICK
                ).component
                displacementQualitySlider.labelTable = Hashtable(
                    displacementQualityLabels.map { (value, text) -> value to JLabel(text) }.toMap()
                )
                contextHelpLabel(
                    description = WavefrontObjBundle.message(
                        "settings.editor.fileTypes.obj.preview.displacementQuality.contextHelp"
                    )
                )
            }
        }
    }

    private fun Cell.contextHelpLabel(
        label: String? = null,
        description: String,
        icon: Icon? = AllIcons.General.ContextHelp
    ): CellBuilder<ContextHelpLabel> =
        ContextHelpLabel(label.orEmpty(), description)
            .apply { this.icon = icon }
            .invoke()

    override fun getPreferredFocusedComponent(): JComponent = defaultShadingMethod

    override fun validateForm() {
        val errorMessages = mutableListOf<String>()

        if (axisLineWidthInput.text.isInvalidPositiveFloat()) {
            errorMessages.add(getAxisLineWidthErrorMessage(axisLineWidthInput.text))
        }
        if (axisLabelFontSizeInput.text.isInvalidPositiveFloat()) {
            errorMessages.add(getAxisLabelFontSizeErrorMessage(axisLabelFontSizeInput.text))
        }
        if (gridLineWidthInput.text.isInvalidPositiveFloat()) {
            errorMessages.add(getGridLineWidthErrorMessage(gridLineWidthInput.text))
        }
        if (lineWidthInput.text.isInvalidPositiveFloat()) {
            errorMessages.add(getLineWidthErrorMessage(lineWidthInput.text))
        }
        if (pointSizeInput.text.isInvalidPositiveFloat()) {
            errorMessages.add(getPointSizeErrorMessage(pointSizeInput.text))
        }

        if (errorMessages.isNotEmpty()) {
            throw ConfigurationException(errorMessages.joinToString(separator = "\n"))
        }
    }

    private fun String.isInvalidPositiveFloat(): Boolean =
        toFloatOrNull() == null || toFloat() <= 0f

    private fun getAxisLineWidthErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.axisLineWidth.error",
        value
    )

    private fun getAxisLabelFontSizeErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.axisLabelFontSize.error",
        value
    )

    private fun getGridLineWidthErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.gridLineWidth.error",
        value
    )

    private fun getLineWidthErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.lineWidth.error",
        value
    )

    private fun getPointSizeErrorMessage(value: String): String = WavefrontObjBundle.message(
        "settings.editor.fileTypes.obj.preview.pointSize.error",
        value
    )

    companion object {
        private const val DISPLACEMENT_QUALITY_MIN = 25
        private const val DISPLACEMENT_QUALITY_MAX = 100
        private const val DISPLACEMENT_QUALITY_MINOR_TICK = 5
        private const val DISPLACEMENT_QUALITY_MAJOR_TICK = 25
        private const val DISPLACEMENT_QUALITY_FACTOR = .1f

        private val displacementQualityLabels = mapOf(
            DISPLACEMENT_QUALITY_MIN to WavefrontObjBundle.message(
                "settings.editor.fileTypes.obj.preview.displacementQuality.lowest"
            ),
            DISPLACEMENT_QUALITY_MAX to WavefrontObjBundle.message(
                "settings.editor.fileTypes.obj.preview.displacementQuality.highest"
            )
        )

        private const val FLOAT_INPUT_COLUMNS = 5
        private const val INVALID_FLOAT_VALUE = -1f

        private val lineWidthContextHelpMessage: String =
            WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.lineWidth.contextHelp")
    }
}
