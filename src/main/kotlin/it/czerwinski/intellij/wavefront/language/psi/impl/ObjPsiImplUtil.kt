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
import it.czerwinski.intellij.wavefront.language.findAllTextureCoordinates
import it.czerwinski.intellij.wavefront.language.findAllVertexNormals
import it.czerwinski.intellij.wavefront.language.findAllVertices
import it.czerwinski.intellij.wavefront.language.psi.ObjFace
import it.czerwinski.intellij.wavefront.language.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.language.psi.ObjFlagElement
import it.czerwinski.intellij.wavefront.language.psi.ObjIndexElement
import it.czerwinski.intellij.wavefront.language.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.language.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.language.psi.ObjTypes
import it.czerwinski.intellij.wavefront.language.psi.ObjVectorElement
import it.czerwinski.intellij.wavefront.language.psi.ObjVertex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormal

object ObjPsiImplUtil {

    @JvmStatic
    fun getIndex(element: ObjVectorElement): Int {
        val psiFile = element.containingFile
        return when (element) {
            is ObjVertex -> findAllVertices(psiFile)
            is ObjTextureCoordinates -> findAllTextureCoordinates(psiFile)
            is ObjVertexNormal -> findAllVertexNormals(psiFile)
            else -> emptyList()
        }.indexOf(element) + 1
    }

    @JvmStatic
    fun getCoordinates(element: ObjVectorElement): List<Float?> =
        element.node.getChildren(TokenSet.create(ObjTypes.FLOAT))
            .map { it.text.toFloatOrNull() }

    @JvmStatic
    fun getType(element: ObjFace): ObjFaceType? =
        ObjFaceType.fromVerticesCount(element.faceVertexList.size)

    @JvmStatic
    fun getValue(element: ObjIndexElement): Int? = element.text.toIntOrNull()

    @JvmStatic
    fun getValue(element: ObjFlagElement): Boolean? =
        when (element.node.findChildByType(ObjTypes.FLAG)?.text) {
            "1" -> true
            "off" -> false
            else -> null
        }

    @JvmStatic
    fun getFilename(element: ObjMaterialFileReference): String? =
        element.node.findChildByType(ObjTypes.REFERENCE)?.text

    @JvmStatic
    fun getMaterialName(element: ObjMaterialReference): String? =
        element.node.findChildByType(ObjTypes.REFERENCE)?.text
}
