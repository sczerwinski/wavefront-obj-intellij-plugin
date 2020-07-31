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

import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.language.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.language.psi.ObjIndexElement
import it.czerwinski.intellij.wavefront.language.psi.ObjTypes
import it.czerwinski.intellij.wavefront.language.psi.ObjVectorElement

object ObjPsiImplUtil {

    @JvmStatic
    fun getName(element: ObjGroupingElement): String? =
        element.node.findChildByType(ObjTypes.STRING)?.text

    @JvmStatic
    fun getCoordinates(element: ObjVectorElement): List<Float?> =
        element.node.getChildren(TokenSet.create(ObjTypes.FLOAT))
            .map { it.text.toFloatOrNull() }

    @JvmStatic
    fun getValue(element: ObjIndexElement): Int? = element.text.toIntOrNull()
}
