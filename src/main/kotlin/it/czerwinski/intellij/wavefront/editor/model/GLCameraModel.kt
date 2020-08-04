/*
 * Copyright 2020 Slawomir Czerwinski
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

import com.jogamp.opengl.math.FloatUtil

data class GLCameraModel(
    val distance: Float,
    val angle: Float,
    val elevation: Float,
    val fov: Float
) {

    val x get() = distance * sinDeg(angle) * cosDeg(elevation)
    val y get() = distance * cosDeg(angle) * cosDeg(elevation)
    val z get() = distance * sinDeg(elevation)

    val near get() = distance * NEAR_RATIO
    val far get() = distance * FAR_RATIO

    private fun sinDeg(angleDeg: Float): Float = FloatUtil.sin(angleDeg * DEG_TO_RAD_RATIO)
    private fun cosDeg(angleDeg: Float): Float = FloatUtil.cos(angleDeg * DEG_TO_RAD_RATIO)

    companion object {
        private const val DEG_TO_RAD_RATIO = FloatUtil.PI / 180
        private const val NEAR_RATIO = 0.1f
        private const val FAR_RATIO = 2f
    }
}
