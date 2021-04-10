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
import it.czerwinski.intellij.wavefront.lang.psi.ObjIndexElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile

abstract class ObjIndexElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjIndexElement {

    override val value: Int? get() = text.toIntOrNull()

    override val countReferencesBefore: Int get() = when (this) {
        is ObjVertexIndex -> containingObjFile?.countVerticesBefore(this) ?: 0
        is ObjTextureCoordinatesIndex -> containingObjFile?.countTextureCoordinatesBefore(this) ?: 0
        is ObjVertexNormalIndex -> containingObjFile?.countVertexNormalsBefore(this) ?: 0
        else -> 0
    }

    override fun asListIndex(): Int {
        val objIndex = value ?: 0
        return when {
            objIndex > 0 -> objIndex - 1
            objIndex < 0 -> countReferencesBefore + objIndex
            else -> 0
        }
    }
}
