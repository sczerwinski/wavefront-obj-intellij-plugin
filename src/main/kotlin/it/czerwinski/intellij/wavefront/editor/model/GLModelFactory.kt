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

package it.czerwinski.intellij.wavefront.editor.model

import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal

object GLModelFactory {

    fun create(objFile: ObjFile): GLModel {
        val vertices = mutableListOf<ObjVertex>()
        val textureCoordinates = mutableListOf<ObjTextureCoordinates>()
        val normals = mutableListOf<ObjVertexNormal>()
        val groupingElements = mutableListOf<GLModel.GroupingElement>()

        var materialReference: ObjMaterialReference? = null
        val materialParts = mutableListOf<GLModel.MaterialPart>()
        val faces = mutableListOf<ObjFace>()
        val lines = mutableListOf<ObjLine>()
        val points = mutableListOf<ObjPoint>()

        fun finalizeMaterialPart() {
            materialParts.add(GLModel.MaterialPart(materialReference, faces.toList(), lines.toList(), points.toList()))
            faces.clear()
            lines.clear()
            points.clear()
        }

        fun finalizeGroupingElement(parent: PsiElement) {
            finalizeMaterialPart()
            groupingElements.add(GLModel.GroupingElement(parent, materialParts.filterNot { it.isEmpty }))
            materialParts.clear()
        }

        fun processElement(parent: PsiElement) {
            for (element in parent.children) {
                when (element) {
                    is ObjVertex -> vertices.add(element)
                    is ObjTextureCoordinates -> textureCoordinates.add(element)
                    is ObjVertexNormal -> normals.add(element)
                    is ObjFace -> faces.add(element)
                    is ObjLine -> lines.add(element)
                    is ObjPoint -> points.add(element)
                    is ObjMaterialReference -> {
                        finalizeMaterialPart()
                        materialReference = element
                    }
                    is ObjGroupingElement -> {
                        finalizeGroupingElement(parent)
                        processElement(element)
                    }
                }
            }
            finalizeGroupingElement(parent)
        }

        processElement(objFile)

        return GLModel(vertices, textureCoordinates, normals, groupingElements.filterNot { it.isEmpty })
    }
}
