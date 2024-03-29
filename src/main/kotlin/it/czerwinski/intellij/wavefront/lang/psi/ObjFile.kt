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

package it.czerwinski.intellij.wavefront.lang.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.ObjFileType
import it.czerwinski.intellij.wavefront.lang.ObjLanguage
import it.czerwinski.intellij.wavefront.lang.psi.util.countChildrenOfType
import it.czerwinski.intellij.wavefront.lang.psi.util.countChildrenOfTypeBefore
import it.czerwinski.intellij.wavefront.lang.psi.util.findRelativeFile
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

class ObjFile(
    viewProvider: FileViewProvider
) : PsiFileBase(viewProvider, ObjLanguage) {

    val objectsCount get() = countChildrenOfType<ObjObject>()
    val groupsCount get() = countChildrenOfType<ObjGroup>()

    val verticesCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjVertex>() }
    val textureCoordinatesCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjTextureCoordinates>() }
    val vertexNormalsCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjVertexNormal>() }
    val freeFormPointsCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjFreeFormPoint>() }
    val curvesCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjFreeForm2DCurve>() }

    val facesCount get() = objectLikeElements.sumOf { it.countChildrenOfType<ObjFaceElement>() }
    val trianglesCount
        get() = objectLikeElements.sumOf { element ->
            element.getChildrenOfType<ObjFaceElement>().sumOf { face -> face.trianglesCount }
        }

    private val groupingElements: List<ObjGroupingElement> get() = getChildrenOfType()

    private val objectLikeElements: List<PsiElement> get() = listOf(this) + groupingElements

    val materialFileReferences: List<ObjMaterialFileReference>
        get() = objectLikeElements
            .flatMap { it.getChildrenOfType<ObjMaterialFileReferenceStatement>() }
            .flatMap { it.getChildrenOfType() }

    val referencedMtlFiles: List<MtlFile>
        get() = materialFileReferences.mapNotNull { element -> element.mtlFile }.distinct()

    private val materialReferences: List<ObjMaterialReference>
        get() = objectLikeElements
            .flatMap { it.getChildrenOfType() }

    val referencedMaterials: List<MtlMaterial>
        get() = materialReferences.mapNotNull { element -> element.material }.distinct()

    override fun getFileType(): FileType = ObjFileType

    fun countVerticesBefore(element: PsiElement): Int =
        objectLikeElements.sumOf { it.countChildrenOfTypeBefore<ObjVertex>(element) }

    fun countTextureCoordinatesBefore(element: PsiElement): Int =
        objectLikeElements.sumOf { it.countChildrenOfTypeBefore<ObjTextureCoordinates>(element) }

    fun countVertexNormalsBefore(element: PsiElement): Int =
        objectLikeElements.sumOf { it.countChildrenOfTypeBefore<ObjVertexNormal>(element) }

    fun countFreeFormPointsBefore(element: PsiElement): Int =
        objectLikeElements.sumOf { it.countChildrenOfTypeBefore<ObjFreeFormPoint>(element) }

    fun countCurvesBefore(element: PsiElement): Int =
        objectLikeElements.sumOf { it.countChildrenOfTypeBefore<ObjFreeForm2DCurve>(element) }

    fun findMtlFile(filePath: String): MtlFile? = findRelativeFile(this, filePath) as? MtlFile

    override fun toString(): String = "Wavefront OBJ File"
}
