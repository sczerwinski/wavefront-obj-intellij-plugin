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

package it.czerwinski.intellij.wavefront.editor.model

import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormCurve
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormDegree
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormSurface
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormType
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal

object GLModelFactory {

    @Suppress("CyclomaticComplexMethod")
    fun create(objFile: ObjFile): GLModel {
        val vertices = mutableListOf<ObjVertex>()
        val textureCoordinates = mutableListOf<ObjTextureCoordinates>()
        val normals = mutableListOf<ObjVertexNormal>()
        val groupingElements = mutableListOf<GLModel.GroupingElement>()

        var freeFormType: ObjFreeFormType? = null
        var freeFormDegree: ObjFreeFormDegree? = null
        var materialReference: ObjMaterialReference? = null
        val materialParts = mutableListOf<GLModel.MaterialPart>()

        val faces = mutableListOf<ObjFace>()
        val lines = mutableListOf<ObjLine>()
        val points = mutableListOf<ObjPoint>()
        val freeFormCurves = mutableListOf<GLModel.Curve>()
        val freeFormSurfaces = mutableListOf<GLModel.Surface>()

        fun finalizeMaterialPart() {
            materialParts += GLModel.MaterialPart(
                materialReference = materialReference,
                faces = faces.toList(),
                lines = lines.toList(),
                points = points.toList(),
                curves = freeFormCurves.toList(),
                surfaces = freeFormSurfaces.toList()
            )
            faces.clear()
            lines.clear()
            points.clear()
            freeFormCurves.clear()
            freeFormSurfaces.clear()
        }

        fun finalizeGroupingElement(parent: PsiElement) {
            finalizeMaterialPart()
            groupingElements += GLModel.GroupingElement(parent, materialParts.filterNot { it.isEmpty })
            materialParts.clear()
        }

        fun processElement(parent: PsiElement) {
            for (element in parent.children) {
                when (element) {
                    is ObjVertex -> vertices += element
                    is ObjTextureCoordinates -> textureCoordinates += element
                    is ObjVertexNormal -> normals += element
                    is ObjFreeFormType -> freeFormType = element
                    is ObjFreeFormDegree -> freeFormDegree = element
                    is ObjFace -> faces += element
                    is ObjLine -> lines += element
                    is ObjPoint -> points += element
                    is ObjFreeFormCurve -> freeFormCurves += GLModel.Curve(
                        type = freeFormType,
                        degree = freeFormDegree,
                        curve = element
                    )
                    is ObjFreeFormSurface -> freeFormSurfaces += GLModel.Surface(
                        type = freeFormType,
                        degree = freeFormDegree,
                        surface = element
                    )
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
