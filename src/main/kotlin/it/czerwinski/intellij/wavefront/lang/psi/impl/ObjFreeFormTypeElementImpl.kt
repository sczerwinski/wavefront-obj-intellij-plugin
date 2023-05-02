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
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormTypeElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormTypeValue
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

abstract class ObjFreeFormTypeElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjFreeFormTypeElement {

    override val isRationalForm: Boolean
        get() = node.findChildByType(ObjTypes.FREE_FORM_RATIONAL_KEYWORD) != null

    private val valueElement: ASTNode? get() = node.findChildByType(ObjTypes.FREE_FORM_TYPE_NAME)

    override val value: ObjFreeFormTypeValue?
        get() = valueElement?.text?.let { ObjFreeFormTypeValue.fromValue(it) }
}
