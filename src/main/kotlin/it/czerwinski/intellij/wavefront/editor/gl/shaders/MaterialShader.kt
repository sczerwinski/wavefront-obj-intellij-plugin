/*
 * Copyright 2020-2021 Slawomir Czerwinski
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
    ]
)
data class MaterialShader(

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

    @Uniform(name = "uUpVector")
    val upVector: Vec3,

    @Uniform(name = "uAmbColor")
    val ambientColor: Vec3,

    @Uniform(name = "uDiffColor")
    val diffuseColor: Vec3,

    @Uniform(name = "uSpecColor")
    val specularColor: Vec3,

    @Uniform(name = "uSpecExp")
    val specularExponent: Float,

    @Uniform(name = "uAmbTex")
    val ambientTexture: Texture,

    @Uniform(name = "uDiffTex")
    val diffuseTexture: Texture,

    @Uniform(name = "uSpecTex")
    val specularTexture: Texture,

    @Uniform(name = "uSpecExpTex")
    val specularExponentTexture: Texture,

    @Uniform(name = "uSpecExpBase")
    val specularExponentBase: Float,

    @Uniform(name = "uSpecExpGain")
    val specularExponentGain: Float,

    @Uniform(name = "uNormalTex")
    val normalmapTexture: Texture,

    @Uniform(name = "uBumpMult")
    val normalmapMultiplier: Float,

    @Uniform(name = "uDispTex")
    val displacementTexture: Texture,

    @Uniform(name = "uDispGain")
    val displacementGain: Float,

    @Uniform(name = "uDispQuality")
    val displacementQuality: Float,

    @Uniform(name = "uCropTex")
    val cropTexture: Int,
)
