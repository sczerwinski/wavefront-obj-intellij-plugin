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
import it.czerwinski.intellij.wavefront.language.psi.ObjGroup
import it.czerwinski.intellij.wavefront.language.psi.ObjObject
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjTypes
import it.czerwinski.intellij.wavefront.language.psi.ObjVertex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormalIndex

object ObjPsiImplUtil {

    @JvmStatic
    fun getName(obj: ObjObject): String? =
        obj.node.findChildByType(ObjTypes.STRING)?.text

    @JvmStatic
    fun getName(group: ObjGroup): String? =
        group.node.findChildByType(ObjTypes.STRING)?.text

    @JvmStatic
    fun getCoordinates(vertex: ObjVertex): List<Float?> =
        vertex.node.getChildren(TokenSet.create(ObjTypes.FLOAT))
            .map { it.text.toFloatOrNull() }

    @JvmStatic
    fun getCoordinates(textureCoordinates: ObjTextureCoordinates): List<Float?> =
        textureCoordinates.node.getChildren(TokenSet.create(ObjTypes.FLOAT))
            .map { it.text.toFloatOrNull() }

    @JvmStatic
    fun getCoordinates(vertexNormal: ObjVertexNormal): List<Float?> =
        vertexNormal.node.getChildren(TokenSet.create(ObjTypes.FLOAT))
            .map { it.text.toFloatOrNull() }

    @JvmStatic
    fun getValue(index: ObjVertexIndex): Int? = index.text.toIntOrNull()

    @JvmStatic
    fun getValue(index: ObjTextureCoordinatesIndex): Int? = index.text.toIntOrNull()

    @JvmStatic
    fun getValue(index: ObjVertexNormalIndex): Int? = index.text.toIntOrNull()
}
