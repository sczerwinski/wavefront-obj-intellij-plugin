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

import com.intellij.util.xmlb.annotations.Attribute
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.ShaderQuality
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector

data class ObjPreviewSettingsState(
    @field:Attribute var defaultShadingMethod: ShadingMethod = ShadingMethod.DEFAULT,
    @field:Attribute var defaultPBREnvironment: PBREnvironment = PBREnvironment.DEFAULT,
    @field:Attribute var defaultUpVector: UpVector = UpVector.DEFAULT,
    @field:Attribute var showAxes: Boolean = DEFAULT_SHOW_AXES,
    @field:Attribute var axisLineWidth: Float = DEFAULT_AXIS_LINE_WIDTH,
    @field:Attribute var showAxesLabels: Boolean = DEFAULT_SHOW_AXES_LABELS,
    @field:Attribute var axisLabelFontSize: Float = DEFAULT_AXIS_LABEL_FONT_SIZE,
    @field:Attribute var showGrid: Boolean = DEFAULT_SHOW_GRID,
    @field:Attribute var showFineGrid: Boolean = DEFAULT_SHOW_FINE_GRID,
    @field:Attribute var gridLineWidth: Float = DEFAULT_GRID_LINE_WIDTH,
    @field:Attribute var lineWidth: Float = DEFAULT_LINE_WIDTH,
    @field:Attribute var pointSize: Float = DEFAULT_POINT_SIZE,
    @field:Attribute var cropTextures: Boolean = DEFAULT_CROP_TEXTURES,
    @field:Attribute var shaderQuality: ShaderQuality = ShaderQuality.DEFAULT,
    @field:Attribute var displacementQuality: Float = DEFAULT_DISPLACEMENT_QUALITY,
) {

    companion object {
        val DEFAULT = ObjPreviewSettingsState()

        const val DEFAULT_SHOW_AXES = true
        const val DEFAULT_AXIS_LINE_WIDTH = 3f
        const val DEFAULT_SHOW_AXES_LABELS = true
        const val DEFAULT_AXIS_LABEL_FONT_SIZE = 18f
        const val DEFAULT_SHOW_GRID = true
        const val DEFAULT_SHOW_FINE_GRID = false
        const val DEFAULT_GRID_LINE_WIDTH = 1f
        const val DEFAULT_LINE_WIDTH = 1f
        const val DEFAULT_POINT_SIZE = 3f
        const val DEFAULT_CROP_TEXTURES = false
        const val DEFAULT_DISPLACEMENT_QUALITY = 7.5f
    }

    interface Holder {
        var objPreviewSettings: ObjPreviewSettingsState
    }
}
