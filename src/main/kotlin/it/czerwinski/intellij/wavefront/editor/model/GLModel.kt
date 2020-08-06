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

package it.czerwinski.intellij.wavefront.editor.model

import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex
import kotlin.math.abs

data class GLModel(
    val vertices: List<ObjVertex>,
    val textureCoordinates: List<ObjTextureCoordinates>,
    val vertexNormals: List<ObjVertexNormal>,
    val faces: List<ObjFace>
) {

    val size: Float
        get() = vertices.flatMap { vertex ->
            vertex.coordinates.filterNotNull().map { abs(it) }
        }.max() ?: 0f

    val triangles get() = faces.filter { it.type === ObjFaceType.TRIANGLE }
    val quads get() = faces.filter { it.type === ObjFaceType.QUAD }
    val polygons get() = faces.filter { it.type === ObjFaceType.POLYGON }

    fun vertexAtIndex(
        vertexIndex: ObjVertexIndex
    ): ObjVertex? =
        vertexIndex.value?.minus(1)?.let { vertices.getOrNull(it) }

    fun textureCoordinatesAtIndex(
        textureCoordinatesIndex: ObjTextureCoordinatesIndex?
    ): ObjTextureCoordinates? =
        textureCoordinatesIndex?.value?.minus(1)?.let { textureCoordinates.getOrNull(it) }

    fun vertexNormalAtIndex(
        vertexNormalIndex: ObjVertexNormalIndex?
    ): ObjVertexNormal? =
        vertexNormalIndex?.value?.minus(1)?.let { vertexNormals.getOrNull(it) }
}
