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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import graphics.glimpse.types.Vec3
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlFloatValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifier
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

abstract class MtlMaterialElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlMaterialElement {

    private val material: MtlMaterial? get() = this as? MtlMaterial

    override val ambientColorElement: MtlColorElement? get() = material?.ambientColorList?.firstOrNull()
    override val diffuseColorElement: MtlColorElement? get() = material?.diffuseColorList?.firstOrNull()
    override val specularColorElement: MtlColorElement? get() = material?.specularColorList?.firstOrNull()
    override val transmissionFilterElement: MtlColorElement? get() = material?.transmissionFilterList?.firstOrNull()
    override val emissionColorElement: MtlColorElement? get() = material?.emissionColorList?.firstOrNull()

    override val illuminationElement: MtlIlluminationValueElement? get() = material?.illuminationList?.firstOrNull()
    override val dissolveElement: MtlFloatValueElement? get() = material?.dissolveList?.firstOrNull()
    override val specularExponentElement: MtlFloatValueElement? get() = material?.specularExponentList?.firstOrNull()
    override val sharpnessElement: MtlFloatValueElement? get() = material?.sharpnessList?.firstOrNull()
    override val opticalDensityElement: MtlFloatValueElement? get() = material?.opticalDensityList?.firstOrNull()
    override val roughnessElement: MtlFloatValueElement? get() = material?.roughnessList?.firstOrNull()
    override val metalnessElement: MtlFloatValueElement? get() = material?.metalnessList?.firstOrNull()

    override val ambientColorMapElement: MtlTextureElement? get() = material?.ambientColorMapList?.firstOrNull()
    override val diffuseColorMapElement: MtlTextureElement? get() = material?.diffuseColorMapList?.firstOrNull()
    override val specularColorMapElement: MtlTextureElement? get() = material?.specularColorMapList?.firstOrNull()
    override val emissionColorMapElement: MtlTextureElement? get() = material?.emissionColorMapList?.firstOrNull()
    override val specularExponentMapElement: MtlTextureElement? get() = material?.specularExponentMapList?.firstOrNull()
    override val dissolveMapElement: MtlTextureElement? get() = material?.dissolveMapList?.firstOrNull()
    override val displacementMapElement: MtlTextureElement? get() = material?.displacementMapList?.firstOrNull()
    override val stencilDecalMapElement: MtlTextureElement? get() = material?.stencilDecalMapList?.firstOrNull()
    override val roughnessMapElement: MtlTextureElement? get() = material?.roughnessMapList?.firstOrNull()
    override val metalnessMapElement: MtlTextureElement? get() = material?.metalnessMapList?.firstOrNull()
    override val normalMapElement: MtlTextureElement? get() = material?.normalMapList?.firstOrNull()
    override val bumpMapElement: MtlTextureElement? get() = material?.bumpMapList?.firstOrNull()
    override val reflectionMapElement: MtlTextureElement? get() = material?.reflectionMapList?.firstOrNull()

    override val ambientColorVector: Vec3? get() = ambientColorElement?.colorVector
    override val diffuseColorVector: Vec3? get() = diffuseColorElement?.colorVector
    override val specularColorVector: Vec3? get() = specularColorElement?.colorVector
    override val transmissionFilterVector: Vec3? get() = transmissionFilterElement?.colorVector
    override val emissionColorVector: Vec3? get() = emissionColorElement?.colorVector

    override val illumination: MtlIlluminationValueElement.Illumination? get() = illuminationElement?.value
    override val dissolve: Float? get() = dissolveElement?.value
    override val specularExponent: Float? get() = specularExponentElement?.value
    override val sharpness: Float? get() = sharpnessElement?.value
    override val opticalDensity: Float? get() = opticalDensityElement?.value
    override val roughness: Float? get() = roughnessElement?.value
    override val metalness: Float? get() = metalnessElement?.value

    override val ambientColorMap: String? get() = ambientColorMapElement?.textureFilename
    override val diffuseColorMap: String? get() = diffuseColorMapElement?.textureFilename
    override val specularColorMap: String? get() = specularColorMapElement?.textureFilename
    override val emissionColorMap: String? get() = emissionColorMapElement?.textureFilename
    override val specularExponentMap: String? get() = specularExponentMapElement?.textureFilename
    override val specularExponentBase: Float?
        get() = specularExponentMapElement?.valueModifierOptionElement?.values?.getOrNull(0)
    override val specularExponentGain: Float?
        get() = specularExponentMapElement?.valueModifierOptionElement?.values?.getOrNull(1)
    override val dissolveMap: String? get() = dissolveMapElement?.textureFilename
    override val displacementMap: String? get() = displacementMapElement?.textureFilename
    override val displacementGain: Float?
        get() = displacementMapElement?.valueModifierOptionElement?.values?.getOrNull(1)
    override val stencilDecalMap: String? get() = stencilDecalMapElement?.textureFilename
    override val roughnessMap: String? get() = roughnessMapElement?.textureFilename
    override val metalnessMap: String? get() = metalnessMapElement?.textureFilename
    override val normalMap: String? get() = normalMapElement?.textureFilename
    override val bumpMap: String? get() = bumpMapElement?.textureFilename
    override val bumpMapMultiplier: Float?
        get() = material?.bumpMapList?.firstOrNull()?.bumpMultiplierOptionList?.firstOrNull()?.value
    override val reflectionMap: String? get() = reflectionMapElement?.textureFilename

    override val texturePsiFiles: List<PsiFile>
        get() = material
            ?.getChildrenOfType<MtlTextureElement>()
            ?.flatMap { textureElement -> textureElement.textureFiles }
            .orEmpty()

    override fun getName(): String? =
        getChildrenOfType<MtlMaterialIdentifier>().singleOrNull()?.name
}
