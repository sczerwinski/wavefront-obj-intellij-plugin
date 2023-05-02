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
import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormBasisMatrixElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormDirection
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

abstract class ObjFreeFormBasisMatrixElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjFreeFormBasisMatrixElement {

    private val directionNode: ASTNode? get() = node.findChildByType(ObjTypes.FREE_FORM_DIRECTION)

    override val direction: ObjFreeFormDirection?
        get() = directionNode?.text?.let { ObjFreeFormDirection.fromValue(it) }

    private val matrixElementsNodes: List<ASTNode> get() = node.getChildren(TokenSet.create(ObjTypes.FLOAT)).toList()

    override val matrixElements: List<Float>
        get() = matrixElementsNodes.mapNotNull { it.text.toFloatOrNull() }

    override val matrixElementsString: String
        get() = matrixElements.joinToString(prefix = "[", separator = " ", postfix = "]")
}
