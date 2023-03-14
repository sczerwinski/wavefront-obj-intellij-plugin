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

package it.czerwinski.intellij.wavefront.editor.gl.shaders

import graphics.glimpse.shaders.annotations.Attribute
import graphics.glimpse.shaders.annotations.AttributeRole
import graphics.glimpse.shaders.annotations.ShaderParams
import graphics.glimpse.shaders.annotations.Uniform
import graphics.glimpse.types.Mat3
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3

@ShaderParams(
    attributes = [
        Attribute(name = "aPos", role = AttributeRole.POSITIONS, vectorSize = 3),
        Attribute(name = "aNormal", role = AttributeRole.NORMALS, vectorSize = 3),
    ]
)
data class SolidShader(

    @Uniform(name = "uProjMat")
    val projectionMatrix: Mat4,

    @Uniform(name = "uViewMat")
    val viewMatrix: Mat4,

    @Uniform(name = "uModelMat")
    val modelMatrix: Mat4,

    @Uniform(name = "uNormalMat")
    val normalMatrix: Mat3,

    @Uniform(name = "uCameraPos")
    val cameraPosition: Vec3,

    @Uniform(name = "uColor")
    val color: Vec3,
)
