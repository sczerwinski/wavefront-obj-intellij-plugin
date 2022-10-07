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

package it.czerwinski.intellij.wavefront.editor.model

import graphics.glimpse.types.Angle
import graphics.glimpse.types.cos
import graphics.glimpse.types.sin
import graphics.glimpse.types.tan
import kotlin.math.atan

data class GLCameraModel(
    val distance: Float,
    val longitude: Angle,
    val latitude: Angle,
    val fov: Angle,
    val upVector: UpVector
) {

    val x
        get() =
            upVector.vector.x * distance * sin(latitude) +
                upVector.vector.y * distance * cos(longitude) * cos(latitude) +
                upVector.vector.z * distance * sin(longitude) * cos(latitude)
    val y
        get() =
            upVector.vector.x * distance * sin(longitude) * cos(latitude) +
                upVector.vector.y * distance * sin(latitude) +
                upVector.vector.z * distance * cos(longitude) * cos(latitude)
    val z
        get() =
            upVector.vector.x * distance * cos(longitude) * cos(latitude) +
                upVector.vector.y * distance * sin(longitude) * cos(latitude) +
                upVector.vector.z * distance * sin(latitude)

    val near get() = distance * NEAR_RATIO
    val far get() = distance * FAR_RATIO

    fun fovY(aspect: Float): Angle =
        Angle.fromRad(atan(tan(fov) / aspect.coerceAtMost(1f)))

    companion object {
        private const val NEAR_RATIO = 0.1f
        private const val FAR_RATIO = 10f
    }
}
