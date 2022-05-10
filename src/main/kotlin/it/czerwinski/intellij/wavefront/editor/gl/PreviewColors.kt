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
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import java.awt.Color

internal object PreviewColors {

    internal val COLOR_FACE: ColorKey = ColorKey.createColorKey("OBJ_3D_FACE", Color.LIGHT_GRAY)
    internal val COLOR_LINE: ColorKey = ColorKey.createColorKey("OBJ_3D_LINE", Color.GRAY)
    internal val COLOR_POINT: ColorKey = ColorKey.createColorKey("OBJ_3D_POINT", Color.GRAY)

    internal val COLOR_AXIS_X: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_X", Color.RED)
    internal val COLOR_AXIS_Y: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Y", Color.GREEN)
    internal val COLOR_AXIS_Z: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Z", Color.BLUE)
    internal val COLOR_GRID: ColorKey = ColorKey.createColorKey("OBJ_3D_GRID", Color.GRAY)
}

fun Vec3(colorKey: ColorKey): Vec3 =
    Vec3(color = EditorColorsManager.getInstance().globalScheme.getColor(colorKey) ?: colorKey.defaultColor)

fun Vec4(colorKey: ColorKey, alpha: Float = 1f): Vec4 =
    Vec3(colorKey).toVec4(w = alpha)
