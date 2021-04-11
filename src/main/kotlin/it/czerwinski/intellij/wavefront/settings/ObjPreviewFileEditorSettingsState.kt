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

import com.intellij.util.xmlb.annotations.Attribute
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector

data class ObjPreviewFileEditorSettingsState(
    @field:Attribute var defaultShadingMethod: ShadingMethod = ShadingMethod.DEFAULT,
    @field:Attribute var defaultUpVector: UpVector = UpVector.DEFAULT,
    @field:Attribute var showAxes: Boolean = DEFAULT_SHOW_AXES,
    @field:Attribute var axisLineWidth: Float = DEFAULT_AXIS_LINE_WIDTH,
    @field:Attribute var showGrid: Boolean = DEFAULT_SHOW_GRID,
    @field:Attribute var showFineGrid: Boolean = DEFAULT_SHOW_FINE_GRID,
    @field:Attribute var gridLineWidth: Float = DEFAULT_GRID_LINE_WIDTH,
    @field:Attribute var lineWidth: Float = DEFAULT_LINE_WIDTH,
    @field:Attribute var pointSize: Float = DEFAULT_POINT_SIZE,
) {

    companion object {
        val DEFAULT = ObjPreviewFileEditorSettingsState()

        const val DEFAULT_SHOW_AXES = true
        const val DEFAULT_AXIS_LINE_WIDTH = 3f
        const val DEFAULT_SHOW_GRID = true
        const val DEFAULT_SHOW_FINE_GRID = false
        const val DEFAULT_GRID_LINE_WIDTH = 1f
        const val DEFAULT_LINE_WIDTH = 1f
        const val DEFAULT_POINT_SIZE = 3f
    }

    interface Holder {
        var objPreviewFileEditorSettings: ObjPreviewFileEditorSettingsState
    }
}
