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
import it.czerwinski.intellij.wavefront.lang.psi.MtlFloatVectorValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

abstract class MtlFloatVectorValueElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlFloatVectorValueElement {

    private val valuesNodes get() = node.getChildren(TokenSet.create(MtlTypes.FLOAT))

    override val values: List<Float?>
        get() = valuesNodes.map { it.text.toFloatOrNull() }

    override val base: Float?
        get() = values.getOrNull(VALUE_INDEX_BASE)

    override val gain: Float?
        get() = values.getOrNull(VALUE_INDEX_GAIN)

    companion object {
        const val VALUE_INDEX_BASE = 0
        const val VALUE_INDEX_GAIN = 1
    }
}
