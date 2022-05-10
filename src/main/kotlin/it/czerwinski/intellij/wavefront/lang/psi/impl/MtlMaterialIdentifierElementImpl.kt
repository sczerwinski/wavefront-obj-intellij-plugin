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
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.MTL_MATERIAL_ICON
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifierElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes
import javax.swing.Icon

abstract class MtlMaterialIdentifierElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlMaterialIdentifierElement {

    private val nameNode get() = node.findChildByType(MtlTypes.MATERIAL_NAME)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(newName: String): PsiElement {
        val oldNameNode = nameNode
        if (oldNameNode != null) {
            val mtlMaterial = MtlElementFactory.createMaterialIdentifier(project, name = newName)
            val newNameNode = mtlMaterial.node.firstChildNode
            if (newNameNode != null) {
                node.replaceChild(oldNameNode, newNameNode)
            }
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? = nameNode?.psi

    override fun getPresentation(): ItemPresentation =
        object : ItemPresentation {
            override fun getPresentableText(): String? = name
            override fun getLocationString(): String? = containingFile.name
            override fun getIcon(unused: Boolean): Icon? = MTL_MATERIAL_ICON
        }

    override fun getIcon(flags: Int): Icon? = MTL_MATERIAL_ICON
}
