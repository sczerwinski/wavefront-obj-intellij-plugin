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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifier
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType
import java.awt.Color

abstract class MtlMaterialElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlMaterialElement {

    private val material: MtlMaterial? get() = this as? MtlMaterial

    override val ambientColor: Color? get() = material?.ambientColorList?.firstOrNull()?.color
    override val diffuseColor: Color? get() = material?.diffuseColorList?.firstOrNull()?.color
    override val specularColor: Color? get() = material?.specularColorList?.firstOrNull()?.color
    override val transmissionFilter: Color? get() = material?.transmissionFilterList?.firstOrNull()?.color

    override val illumination: MtlIlluminationValueElement.Illumination?
        get() = material?.illuminationList?.firstOrNull()?.value
    override val dissolve: Float? get() = material?.dissolveList?.firstOrNull()?.value
    override val specularExponent: Float? get() = material?.specularExponentList?.firstOrNull()?.value
    override val sharpness: Float? get() = material?.sharpnessList?.firstOrNull()?.value
    override val opticalDensity: Float? get() = material?.opticalDensityList?.firstOrNull()?.value

    override val ambientColorMap: VirtualFile?
        get() = material?.ambientColorMapList?.firstOrNull()?.textureVirtualFile
    override val diffuseColorMap: VirtualFile?
        get() = material?.diffuseColorMapList?.firstOrNull()?.textureVirtualFile
    override val specularColorMap: VirtualFile?
        get() = material?.specularColorMapList?.firstOrNull()?.textureVirtualFile
    override val specularExponentMap: VirtualFile?
        get() = material?.specularExponentMapList?.firstOrNull()?.textureVirtualFile
    override val specularExponentBase: Float?
        get() = material?.specularExponentMapList?.firstOrNull()?.valueModifierOptionList
            ?.firstOrNull()?.values?.getOrNull(0)
    override val specularExponentGain: Float?
        get() = material?.specularExponentMapList?.firstOrNull()?.valueModifierOptionList
            ?.firstOrNull()?.values?.getOrNull(1)
    override val dissolveMap: VirtualFile?
        get() = material?.dissolveMapList?.firstOrNull()?.textureVirtualFile
    override val displacementMap: VirtualFile?
        get() = material?.displacementMapList?.firstOrNull()?.textureVirtualFile
    override val stencilDecalMap: VirtualFile?
        get() = material?.stencilDecalMapList?.firstOrNull()?.textureVirtualFile
    override val bumpMap: VirtualFile?
        get() = material?.bumpMapList?.firstOrNull()?.textureVirtualFile
    override val bumpMapMultiplier: Float?
        get() = material?.bumpMapList?.firstOrNull()?.bumpMultiplierOptionList?.firstOrNull()?.value
    override val reflectionMap: VirtualFile?
        get() = material?.reflectionMapList?.firstOrNull()?.textureVirtualFile

    override fun getName(): String? =
        getChildrenOfType<MtlMaterialIdentifier>().singleOrNull()?.name
}
