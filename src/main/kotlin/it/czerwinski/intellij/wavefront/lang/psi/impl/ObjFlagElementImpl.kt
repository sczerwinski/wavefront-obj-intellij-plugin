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
import it.czerwinski.intellij.wavefront.lang.psi.ObjFlagElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

abstract class ObjFlagElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjFlagElement {

    private val valueNode get() = node.findChildByType(ObjTypes.SMOOTHING_GROUP_NUMBER)

    override val value: Int?
        get() = when (val text = valueNode?.text) {
            "off" -> 0
            else -> text?.toIntOrNull()
        }
}
