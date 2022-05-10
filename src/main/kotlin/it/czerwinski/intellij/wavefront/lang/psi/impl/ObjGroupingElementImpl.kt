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
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjObjectOrGroupIdentifier
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

abstract class ObjGroupingElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjGroupingElement {

    override val trianglesCount: Int
        get() = getChildrenOfType<ObjFaceElement>().sumOf { it.trianglesCount }

    override fun getName(): String? =
        getChildrenOfType<ObjObjectOrGroupIdentifier>().singleOrNull()?.name
}
