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
import graphics.glimpse.types.Vec2
import it.czerwinski.intellij.wavefront.lang.psi.ObjIntListElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

abstract class ObjIntListElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjIntListElement {

    private val valuesNodes: List<ASTNode> get() = node.getChildren(TokenSet.create(ObjTypes.INTEGER)).toList()

    override val values: List<Int>
        get() = valuesNodes.mapNotNull { it.text.toIntOrNull() }

    override val valuesString: String
        get() = values.joinToString()

    override val asVec2: Vec2<Int>
        get() = Vec2(
            x = values.getOrNull(COORDINATE_U) ?: 0,
            y = values.getOrNull(COORDINATE_V) ?: 0
        )

    companion object {
        private const val COORDINATE_U = 0
        private const val COORDINATE_V = 1
    }
}
