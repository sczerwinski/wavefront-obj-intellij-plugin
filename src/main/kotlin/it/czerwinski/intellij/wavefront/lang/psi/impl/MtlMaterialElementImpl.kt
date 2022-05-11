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

    override val ambientColorVector: Vec3? get() = material?.ambientColorList?.firstOrNull()?.colorVector
    override val diffuseColorVector: Vec3? get() = material?.diffuseColorList?.firstOrNull()?.colorVector
    override val specularColorVector: Vec3? get() = material?.specularColorList?.firstOrNull()?.colorVector
    override val transmissionFilterVector: Vec3? get() = material?.transmissionFilterList?.firstOrNull()?.colorVector
    override val emissionColorVector: Vec3? get() = material?.emissionColorList?.firstOrNull()?.colorVector

    override val illumination: MtlIlluminationValueElement.Illumination?
        get() = material?.illuminationList?.firstOrNull()?.value
    override val dissolve: Float? get() = material?.dissolveList?.firstOrNull()?.value
    override val specularExponent: Float? get() = material?.specularExponentList?.firstOrNull()?.value
    override val sharpness: Float? get() = material?.sharpnessList?.firstOrNull()?.value
    override val opticalDensity: Float? get() = material?.opticalDensityList?.firstOrNull()?.value
    override val roughness: Float? get() = material?.roughnessList?.firstOrNull()?.value
    override val metalness: Float? get() = material?.metalnessList?.firstOrNull()?.value

    override val ambientColorMap: String?
        get() = material?.ambientColorMapList?.firstOrNull()?.textureFilename
    override val diffuseColorMap: String?
        get() = material?.diffuseColorMapList?.firstOrNull()?.textureFilename
    override val specularColorMap: String?
        get() = material?.specularColorMapList?.firstOrNull()?.textureFilename
    override val emissionColorMap: String?
        get() = material?.emissionColorMapList?.firstOrNull()?.textureFilename
    override val specularExponentMap: String?
        get() = material?.specularExponentMapList?.firstOrNull()?.textureFilename
    override val specularExponentBase: Float?
        get() = material?.specularExponentMapList?.firstOrNull()?.valueModifierOptionList
            ?.firstOrNull()?.values?.getOrNull(0)
    override val specularExponentGain: Float?
        get() = material?.specularExponentMapList?.firstOrNull()?.valueModifierOptionList
            ?.firstOrNull()?.values?.getOrNull(1)
    override val dissolveMap: String?
        get() = material?.dissolveMapList?.firstOrNull()?.textureFilename
    override val displacementMap: String?
        get() = material?.displacementMapList?.firstOrNull()?.textureFilename
    override val displacementGain: Float?
        get() = material?.displacementMapList?.firstOrNull()?.valueModifierOptionList
            ?.firstOrNull()?.values?.getOrNull(1)
    override val stencilDecalMap: String?
        get() = material?.stencilDecalMapList?.firstOrNull()?.textureFilename
    override val roughnessMap: String?
        get() = material?.roughnessMapList?.firstOrNull()?.textureFilename
    override val metalnessMap: String?
        get() = material?.metalnessMapList?.firstOrNull()?.textureFilename
    override val normalMap: String?
        get() = material?.normalMapList?.firstOrNull()?.textureFilename
    override val bumpMap: String?
        get() = material?.bumpMapList?.firstOrNull()?.textureFilename
    override val bumpMapMultiplier: Float?
        get() = material?.bumpMapList?.firstOrNull()?.bumpMultiplierOptionList?.firstOrNull()?.value
    override val reflectionMap: String?
        get() = material?.reflectionMapList?.firstOrNull()?.textureFilename

    override val texturePsiFiles: List<PsiFile>
        get() = material
            ?.getChildrenOfType<MtlTextureElement>()
            ?.flatMap { textureElement -> textureElement.textureFiles }
            .orEmpty()

    override fun getName(): String? =
        getChildrenOfType<MtlMaterialIdentifier>().singleOrNull()?.name
}
