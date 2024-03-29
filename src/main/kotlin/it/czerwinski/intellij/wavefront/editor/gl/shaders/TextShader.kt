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
import graphics.glimpse.shaders.annotations.Sampler2D
import graphics.glimpse.shaders.annotations.ShaderParams
import graphics.glimpse.shaders.annotations.Uniform
import graphics.glimpse.textures.Texture
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4

@ShaderParams(
    attributes = [
        Attribute(name = "aPos", role = AttributeRole.POSITIONS, vectorSize = 3),
        Attribute(name = "aTexCoord", role = AttributeRole.TEX_COORDS, vectorSize = 2),
    ]
)
data class TextShader(

    @Uniform(name = "uPos")
    val position: Vec3<Float>,

    @Uniform(name = "uScale")
    val scale: Vec2<Float>,

    @Uniform(name = "uColor")
    val color: Vec4<Float>,

    @Sampler2D(name = "uTexture")
    val texture: Texture,
)
