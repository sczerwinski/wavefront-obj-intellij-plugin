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
import graphics.glimpse.types.Mat3
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3

@ShaderParams(
    attributes = [
        Attribute(name = "aPos", role = AttributeRole.POSITIONS, vectorSize = 3),
        Attribute(name = "aTexCoord", role = AttributeRole.TEX_COORDS, vectorSize = 2),
        Attribute(name = "aNormal", role = AttributeRole.NORMALS, vectorSize = 3),
        Attribute(name = "aTangent", role = AttributeRole.TANGENTS, vectorSize = 3),
        Attribute(name = "aBitangent", role = AttributeRole.BITANGENTS, vectorSize = 3),
    ]
)
data class MaterialShader(

    @Uniform(name = "uProjMat")
    val projectionMatrix: Mat4<Float>,

    @Uniform(name = "uViewMat")
    val viewMatrix: Mat4<Float>,

    @Uniform(name = "uModelMat")
    val modelMatrix: Mat4<Float>,

    @Uniform(name = "uNormalMat")
    val normalMatrix: Mat3<Float>,

    @Uniform(name = "uCameraPos")
    val cameraPosition: Vec3<Float>,

    @Uniform(name = "uAmbColor")
    val ambientColor: Vec3<Float>,

    @Uniform(name = "uDiffColor")
    val diffuseColor: Vec3<Float>,

    @Uniform(name = "uSpecColor")
    val specularColor: Vec3<Float>,

    @Uniform(name = "uEmissionColor")
    val emissionColor: Vec3<Float>,

    @Uniform(name = "uSpecExp")
    val specularExponent: Float,

    @Sampler2D(name = "uAmbTex")
    val ambientTexture: Texture,

    @Sampler2D(name = "uDiffTex")
    val diffuseTexture: Texture,

    @Sampler2D(name = "uSpecTex")
    val specularTexture: Texture,

    @Sampler2D(name = "uEmissionTex")
    val emissionTexture: Texture,

    @Sampler2D(name = "uSpecExpTex")
    val specularExponentTexture: Texture,

    @Uniform(name = "uSpecExpBase")
    val specularExponentBase: Float,

    @Uniform(name = "uSpecExpGain")
    val specularExponentGain: Float,

    @Uniform(name = "uSpecExpChan")
    val specularExponentChannel: Int,

    @Sampler2D(name = "uNormalTex")
    val normalmapTexture: Texture,

    @Uniform(name = "uBumpMult")
    val normalmapMultiplier: Float,

    @Sampler2D(name = "uDispTex")
    val displacementTexture: Texture,

    @Uniform(name = "uDispGain")
    val displacementGain: Float,

    @Uniform(name = "uDispQuality")
    val displacementQuality: Float,

    @Uniform(name = "uDispChan")
    val displacementChannel: Int,

    @Uniform(name = "uCropTex")
    val cropTexture: Boolean,
)
