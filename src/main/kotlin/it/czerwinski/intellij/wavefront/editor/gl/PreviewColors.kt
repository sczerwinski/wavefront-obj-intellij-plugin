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

package it.czerwinski.intellij.wavefront.editor.gl

import com.intellij.openapi.editor.colors.ColorKey
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.ui.JBColor
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4

internal object PreviewColors {

    internal val COLOR_FACE: ColorKey = ColorKey.createColorKey("OBJ_3D_FACE", JBColor.LIGHT_GRAY)
    internal val COLOR_LINE: ColorKey = ColorKey.createColorKey("OBJ_3D_LINE", JBColor.GRAY)
    internal val COLOR_POINT: ColorKey = ColorKey.createColorKey("OBJ_3D_POINT", JBColor.GRAY)

    internal val COLOR_AXIS_X: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_X", JBColor.RED)
    internal val COLOR_AXIS_Y: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Y", JBColor.GREEN)
    internal val COLOR_AXIS_Z: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Z", JBColor.BLUE)
    internal val COLOR_GRID: ColorKey = ColorKey.createColorKey("OBJ_3D_GRID", JBColor.GRAY)
}

fun Vec3(colorKey: ColorKey): Vec3 =
    Vec4(color = EditorColorsManager.getInstance().globalScheme.getColor(colorKey) ?: colorKey.defaultColor).toVec3()

fun Vec4(colorKey: ColorKey, alpha: Float = 1f): Vec4 =
    Vec3(colorKey).toVec4(w = alpha)
