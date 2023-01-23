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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.ObjObjectOrGroupIdentifierElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

abstract class ObjObjectOrGroupIdentifierElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjObjectOrGroupIdentifierElement {

    private val nameNode get() = node.findChildByType(ObjTypes.OBJECT_OR_GROUP_NAME)

    override fun getName(): String? = nameIdentifier?.text

    override fun setName(newName: String): PsiElement {
        val oldNameNode = nameNode
        if (oldNameNode != null) {
            val objObject = ObjElementFactory.createObjectOrGroupIdentifier(project, name = newName)
            val newNameNode = objObject.node.firstChildNode
            if (newNameNode != null) {
                node.replaceChild(oldNameNode, newNameNode)
            }
        }
        return this
    }

    override fun getNameIdentifier(): PsiElement? = nameNode?.psi
}
