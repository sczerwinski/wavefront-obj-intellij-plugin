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
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.rotationX
import graphics.glimpse.types.rotationY

enum class UpVector(
    val vector: Vec3<Float>,
    val modelMatrix: Mat4<Float>
) {
    X_UP(
        vector = Vec3.unitX(),
        modelMatrix = rotationY(-Angle.rightAngle<Float>()) * rotationX(-Angle.rightAngle<Float>())
    ),
    Y_UP(
        vector = Vec3.unitY(),
        modelMatrix = rotationX(Angle.rightAngle<Float>()) * rotationY(Angle.rightAngle())
    ),
    Z_UP(
        vector = Vec3.unitZ(),
        modelMatrix = Mat4.identity()
    );

    val normalMatrix: Mat4<Float> = modelMatrix.inverse().transpose()

    companion object {
        val DEFAULT = Z_UP
    }
}
