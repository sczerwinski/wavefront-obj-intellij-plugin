/*
 * Copyright 2020 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.language.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.language.psi.ObjElementFactory
import it.czerwinski.intellij.wavefront.language.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.language.psi.ObjTypes

abstract class ObjGroupingElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjGroupingElement {

    private val nameNode get() = node.findChildByType(ObjTypes.STRING)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(newName: String): PsiElement {
        val oldNameNode = nameNode
        if (oldNameNode != null) {
            val objObject = ObjElementFactory.createObject(project, name = newName)
            val newNameNode = objObject.nameIdentifier?.node
            if (newNameNode != null) {
                node.replaceChild(oldNameNode, newNameNode)
            }
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? = nameNode?.psi
}
