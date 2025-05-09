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

package it.czerwinski.intellij.wavefront.lang.psi

import com.intellij.psi.PsiFile
import graphics.glimpse.types.Vec3

interface MtlMaterialElement : DocumentedElement {

    override val documentation: MtlDocumentation

    val identifierElement: MtlMaterialIdentifierElement?

    val ambientColorElement: MtlColorElement?
    val diffuseColorElement: MtlColorElement?
    val specularColorElement: MtlColorElement?
    val transmissionFilterElement: MtlColorElement?
    val emissionColorElement: MtlColorElement?

    val illuminationElement: MtlIlluminationValueElement?
    val dissolveElement: MtlFloatValueElement?
    val specularExponentElement: MtlFloatValueElement?
    val sharpnessElement: MtlFloatValueElement?
    val opticalDensityElement: MtlFloatValueElement?
    val transparencyElement: MtlFloatValueElement?
    val roughnessElement: MtlFloatValueElement?
    val metalnessElement: MtlFloatValueElement?
    val sheenElement: MtlFloatValueElement?
    val clearcoatThicknessElement: MtlFloatValueElement?
    val clearcoatRoughnessElement: MtlFloatValueElement?

    val ambientColorMapElement: MtlTextureElement?
    val diffuseColorMapElement: MtlTextureElement?
    val specularColorMapElement: MtlTextureElement?
    val emissionColorMapElement: MtlTextureElement?
    val specularExponentMapElement: MtlScalarTextureElement?
    val dissolveMapElement: MtlScalarTextureElement?
    val displacementMapElement: MtlScalarTextureElement?
    val stencilDecalMapElement: MtlScalarTextureElement?
    val roughnessMapElement: MtlScalarTextureElement?
    val metalnessMapElement: MtlScalarTextureElement?
    val sheenMapElement: MtlScalarTextureElement?
    val normalMapElement: MtlTextureElement?
    val bumpMapElement: MtlTextureElement?
    val reflectionMapElement: MtlReflectionTextureElement?

    val ambientColorVector: Vec3<Float>?
    val diffuseColorVector: Vec3<Float>?
    val specularColorVector: Vec3<Float>?
    val transmissionFilterVector: Vec3<Float>?
    val emissionColorVector: Vec3<Float>?

    val illumination: MtlIlluminationValueElement.Illumination?
    val dissolve: Float?
    val specularExponent: Float?
    val sharpness: Float?
    val opticalDensity: Float?
    val transparency: Float?
    val roughness: Float?
    val metalness: Float?
    val sheen: Float?
    val clearcoatThickness: Float?
    val clearcoatRoughness: Float?

    val ambientColorMap: String?
    val diffuseColorMap: String?
    val specularColorMap: String?
    val emissionColorMap: String?
    val specularExponentMap: String?
    val specularExponentBase: Float?
    val specularExponentGain: Float?
    val specularExponentChannel: MtlScalarChannel?
    val dissolveMap: String?
    val displacementMap: String?
    val displacementGain: Float?
    val displacementChannel: MtlScalarChannel?
    val stencilDecalMap: String?
    val roughnessMap: String?
    val roughnessChannel: MtlScalarChannel?
    val metalnessMap: String?
    val metalnessChannel: MtlScalarChannel?
    val sheenMap: String?
    val sheenChannel: MtlScalarChannel?
    val normalMap: String?
    val bumpMap: String?
    val bumpMapMultiplier: Float?
    val reflectionMap: String?
    val reflectionMapType: MtlReflectionType?

    val texturePsiFiles: List<PsiFile>

    fun getName(): String?
}
