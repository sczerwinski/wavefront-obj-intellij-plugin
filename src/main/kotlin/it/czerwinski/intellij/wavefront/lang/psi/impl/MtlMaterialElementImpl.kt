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
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

abstract class MtlMaterialElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlMaterialElement {

    private val nameNode get() = node.findChildByType(MtlTypes.MATERIAL_NAME)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(newName: String): PsiElement {
        val oldNameNode = nameNode
        if (oldNameNode != null) {
            val mtlMaterial = MtlElementFactory.createMaterial(project, name = newName)
            val newNameNode = mtlMaterial.nameIdentifier?.node
            if (newNameNode != null) {
                node.replaceChild(oldNameNode, newNameNode)
            }
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? = nameNode?.psi
}