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

import com.intellij.openapi.options.ConfigurationException
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBCheckBox
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.FaceCulling
import it.czerwinski.intellij.wavefront.editor.gl.isBackCulling
import it.czerwinski.intellij.wavefront.editor.gl.isFrontCulling
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
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JSlider
import kotlin.math.roundToInt

/**
 * UI component for OBJ file 3D preview settings.
 */
class ObjPreviewSettingsGroup : SettingsGroup, ObjPreviewSettingsState.Holder {

    private lateinit var defaultShadingMethod: ComboBox<ShadingMethod>
    private lateinit var defaultPBREnvironment: ComboBox<PBREnvironment>
    private lateinit var defaultUpVector: ComboBox<UpVector>
    private lateinit var freeFormCurveResolutionSlider: JSlider
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
    private lateinit var mipmappingCheckBox: JBCheckBox
    private lateinit var frontFaceCullingCheckBox: JBCheckBox
    private lateinit var backFaceCullingCheckBox: JBCheckBox
    private lateinit var shaderQualityComboBox: ComboBox<ShaderQuality>
    private lateinit var displacementQualitySlider: JSlider

    override var objPreviewSettings: ObjPreviewSettingsState
        get() = ObjPreviewSettingsState(
            defaultShadingMethod = defaultShadingMethod.item ?: ShadingMethod.DEFAULT,
            defaultPBREnvironment = defaultPBREnvironment.item ?: PBREnvironment.DEFAULT,
            defaultUpVector = defaultUpVector.item ?: UpVector.DEFAULT,
            freeFormCurveResolution = freeFormCurveResolutionSlider.value,
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
            mipmapping = mipmappingCheckBox.isSelected,
            faceCulling = FaceCulling(
                front = frontFaceCullingCheckBox.isSelected,
                back = backFaceCullingCheckBox.isSelected
            ),
            shaderQuality = shaderQualityComboBox.item ?: ShaderQuality.DEFAULT,
            displacementQuality = displacementQualitySlider.value * DISPLACEMENT_QUALITY_FACTOR
        )
        set(value) {
            defaultShadingMethod.item = value.defaultShadingMethod
            defaultPBREnvironment.item = value.defaultPBREnvironment
            defaultUpVector.item = value.defaultUpVector
            freeFormCurveResolutionSlider.value = value.freeFormCurveResolution
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
            mipmappingCheckBox.isSelected = value.mipmapping
            frontFaceCullingCheckBox.isSelected = value.faceCulling?.isFrontCulling == true
            backFaceCullingCheckBox.isSelected = value.faceCulling?.isBackCulling == true
            shaderQualityComboBox.item = value.shaderQuality
            displacementQualitySlider.value = (value.displacementQuality / DISPLACEMENT_QUALITY_FACTOR).roundToInt()
        }

    override fun createGroupContents(panel: Panel) {
        with(panel) {
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
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector")) {
                defaultUpVector = enumComboBox(
                    defaultValue = UpVector.DEFAULT,
                    renderer = UpVectorListCellRenderer()
                ).component
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.freeFormCurveResolution")) {
                freeFormCurveResolutionSlider = slider(
                    FREE_FORM_CURVE_RESOLUTION_MIN,
                    FREE_FORM_CURVE_RESOLUTION_MAX,
                    FREE_FORM_CURVE_RESOLUTION_MINOR_TICK,
                    FREE_FORM_CURVE_RESOLUTION_MAJOR_TICK
                ).comment(
                    comment = WavefrontObjBundle.message(
                        "settings.editor.fileTypes.obj.preview.freeFormCurveResolution.contextHelp"
                    )
                ).component
                freeFormCurveResolutionSlider.labelTable = Hashtable(
                    freeFormCurveResolutionLabels.associateWith { JLabel(it.toString()) }
                )
            }
            collapsibleGroup(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axes")) {
                createAxesGroup()
            }
            collapsibleGroup(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.grid")) {
                createGridGroup()
            }
            collapsibleGroup(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.advanced")) {
                createAdvancedGroup()
            }
        }
    }

    private fun Panel.createAxesGroup() {
        row {
            showAxesCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showAxes")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axisLineWidth")) {
            axisLineWidthInput = textField(
                defaultValue = ObjPreviewSettingsState.DEFAULT_AXIS_LINE_WIDTH,
                columns = FLOAT_INPUT_COLUMNS
            ).comment(comment = lineWidthContextHelpMessage).component
        }
        row {
            showAxesLabelsCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.showAxesLabels")
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.axisLabelFontSize")) {
            axisLabelFontSizeInput = textField(
                defaultValue = ObjPreviewSettingsState.DEFAULT_AXIS_LABEL_FONT_SIZE,
                columns = FLOAT_INPUT_COLUMNS
            ).component
        }
    }

    private fun Panel.createGridGroup() {
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
            gridLineWidthInput = textField(
                defaultValue = ObjPreviewSettingsState.DEFAULT_GRID_LINE_WIDTH,
                columns = FLOAT_INPUT_COLUMNS
            ).comment(comment = lineWidthContextHelpMessage).component
        }
    }

    @Suppress("LongMethod")
    private fun Panel.createAdvancedGroup() {
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.lineWidth")) {
            lineWidthInput = textField(
                defaultValue = ObjPreviewSettingsState.DEFAULT_LINE_WIDTH,
                columns = FLOAT_INPUT_COLUMNS
            ).comment(comment = lineWidthContextHelpMessage).component
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
        row {
            mipmappingCheckBox = checkBox(
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.mipmapping")
            ).component
        }
        buttonsGroup(title = WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.faceCulling")) {
            row {
                frontFaceCullingCheckBox = checkBox(
                    WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.faceCulling.front")
                ).component
            }
            row {
                backFaceCullingCheckBox = checkBox(
                    WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.faceCulling.back")
                ).component
            }
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.shaderQuality")) {
            shaderQualityComboBox = enumComboBox(
                defaultValue = ShaderQuality.DEFAULT,
                renderer = ShaderQualityListCellRenderer()
            ).comment(
                comment = WavefrontObjBundle.message(
                    "settings.editor.fileTypes.obj.preview.shaderQuality.contextHelp"
                )
            ).component
        }
        row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.displacementQuality")) {
            displacementQualitySlider = slider(
                DISPLACEMENT_QUALITY_MIN,
                DISPLACEMENT_QUALITY_MAX,
                DISPLACEMENT_QUALITY_MINOR_TICK,
                DISPLACEMENT_QUALITY_MAJOR_TICK
            ).comment(
                comment = WavefrontObjBundle.message(
                    "settings.editor.fileTypes.obj.preview.displacementQuality.contextHelp"
                )
            ).component
            displacementQualitySlider.labelTable = Hashtable(
                displacementQualityLabels.map { (value, text) -> value to JLabel(text) }.toMap()
            )
        }
    }

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
        private const val FREE_FORM_CURVE_RESOLUTION_MIN = 1
        private const val FREE_FORM_CURVE_RESOLUTION_MAX = 20
        private const val FREE_FORM_CURVE_RESOLUTION_MINOR_TICK = 1
        private const val FREE_FORM_CURVE_RESOLUTION_MAJOR_TICK = FREE_FORM_CURVE_RESOLUTION_MAX + 1
        private val freeFormCurveResolutionLabels = listOf(1, 5, 10, 15, 20)

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
