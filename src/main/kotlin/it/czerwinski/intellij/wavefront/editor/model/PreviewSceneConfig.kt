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

package it.czerwinski.intellij.wavefront.editor.model

import it.czerwinski.intellij.wavefront.settings.ObjPreviewSettingsState

data class PreviewSceneConfig(
    val axisLineWidth: Float = ObjPreviewSettingsState.DEFAULT_AXIS_LINE_WIDTH,
    val showAxesLabels: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_AXES_LABELS,
    val axisLabelFontSize: Float = ObjPreviewSettingsState.DEFAULT_AXIS_LABEL_FONT_SIZE,
    val showFineGrid: Boolean = ObjPreviewSettingsState.DEFAULT_SHOW_FINE_GRID,
    val gridLineWidth: Float = ObjPreviewSettingsState.DEFAULT_GRID_LINE_WIDTH,
    val lineWidth: Float = ObjPreviewSettingsState.DEFAULT_LINE_WIDTH,
    val pointSize: Float = ObjPreviewSettingsState.DEFAULT_POINT_SIZE,
    val shaderQuality: ShaderQuality = ShaderQuality.DEFAULT,
    val mipmapping: Boolean = ObjPreviewSettingsState.DEFAULT_MIPMAPPING,
    val displacementQuality: Float = ObjPreviewSettingsState.DEFAULT_DISPLACEMENT_QUALITY
) {

    fun needsFullRefresh(oldConfig: PreviewSceneConfig): Boolean =
        shaderQuality != oldConfig.shaderQuality || mipmapping != oldConfig.mipmapping

    companion object {
        fun fromObjPreviewSettingsState(settings: ObjPreviewSettingsState): PreviewSceneConfig =
            PreviewSceneConfig(
                settings.axisLineWidth,
                settings.showAxesLabels,
                settings.axisLabelFontSize,
                settings.showFineGrid,
                settings.gridLineWidth,
                settings.lineWidth,
                settings.pointSize,
                settings.shaderQuality,
                settings.mipmapping,
                settings.displacementQuality
            )
    }
}
