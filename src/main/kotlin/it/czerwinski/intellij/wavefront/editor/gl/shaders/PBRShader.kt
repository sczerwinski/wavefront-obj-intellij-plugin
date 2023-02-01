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
import it.czerwinski.intellij.wavefront.editor.gl.textures.TextureResources

@ShaderParams(
    attributes = [
        Attribute(name = "aPos", role = AttributeRole.POSITIONS, vectorSize = 3),
        Attribute(name = "aTexCoord", role = AttributeRole.TEX_COORDS, vectorSize = 2),
        Attribute(name = "aNormal", role = AttributeRole.NORMALS, vectorSize = 3),
        Attribute(name = "aTangent", role = AttributeRole.TANGENTS, vectorSize = 3),
        Attribute(name = "aBitangent", role = AttributeRole.BITANGENTS, vectorSize = 3),
    ]
)
data class PBRShader(

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

    @Uniform(name = "uLightPos")
    val lightPosition: Vec3,

    @Uniform(name = "uDiffColor")
    val diffuseColor: Vec3,

    @Uniform(name = "uEmissionColor")
    val emissionColor: Vec3,

    @Uniform(name = "uRoughness")
    val roughness: Float,

    @Uniform(name = "uMetalness")
    val metalness: Float,

    @Sampler2D(name = "uDiffTex")
    val diffuseTexture: Texture,

    @Sampler2D(name = "uEmissionTex")
    val emissionTexture: Texture,

    @Sampler2D(name = "uRoughnessTex")
    val roughnessTexture: Texture,

    @Sampler2D(name = "uMetalnessTex")
    val metalnessTexture: Texture,

    @Sampler2D(name = "uNormalTex")
    val normalmapTexture: Texture,

    @Sampler2D(name = "uDispTex")
    val displacementTexture: Texture,

    @Uniform(name = "uDispGain")
    val displacementGain: Float,

    @Uniform(name = "uDispQuality")
    val displacementQuality: Float,

    @Sampler2D(name = "uEnvTex")
    val environmentTexture: Texture,

    @Sampler2D(name = "uIrradianceTex")
    val irradianceTexture: Texture,

    @Sampler2D(name = "uReflectionTex", size = TextureResources.REFLECTION_LEVELS_COUNT)
    val reflectionTextures: List<Texture>,

    @Sampler2D(name = "uBRDFTex")
    val brdfTexture: Texture,

    @Uniform(name = "uCropTex")
    val cropTexture: Boolean,
)
