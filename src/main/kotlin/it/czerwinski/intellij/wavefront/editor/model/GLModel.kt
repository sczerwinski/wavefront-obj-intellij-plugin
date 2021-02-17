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

package it.czerwinski.intellij.wavefront.editor.model

import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import kotlin.math.abs

data class GLModel(
    val vertices: List<ObjVertex>,
    val textureCoordinates: List<ObjTextureCoordinates>,
    val vertexNormals: List<ObjVertexNormal>,
    val groupingElements: List<GroupingElement>
) {

    val size: Float get() = vertices.flatMap { vertex ->
        vertex.coordinates.filterNotNull().map { abs(it) }
    }.max() ?: 0f

    val materials: List<MtlMaterial?>
        get() = groupingElements.flatMap { groupingElement ->
            groupingElement.materialParts.map { materialPart ->
                materialPart.material
            }
        }

    data class GroupingElement(
        val psiElement: PsiElement,
        val materialParts: List<MaterialPart>
    ) {
        val isEmpty: Boolean get() = materialParts.isEmpty()
    }

    data class MaterialPart(
        val materialReference: ObjMaterialReference?,
        val faces: List<ObjFace>,
        val lines: List<ObjLine>,
        val points: List<ObjPoint>
    ) {
        val isEmpty: Boolean get() = faces.isEmpty() && lines.isEmpty() && points.isEmpty()
        val material: MtlMaterial? = materialReference?.material
    }
}
