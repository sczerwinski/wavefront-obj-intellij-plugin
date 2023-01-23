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

import graphics.glimpse.types.Angle

object GLCameraModelFactory {

    const val DEFAULT_DISTANCE = 5f

    private val longitude = Angle.fromDeg(deg = 150f)
    private val latitude = Angle.fromDeg(deg = 30f)
    private val fov = Angle.fromDeg(deg = 50f)

    fun createDefault(): GLCameraModel = GLCameraModel(
        distance = DEFAULT_DISTANCE,
        longitude = longitude,
        latitude = latitude,
        fov = fov,
        upVector = UpVector.Z_UP
    )
}
