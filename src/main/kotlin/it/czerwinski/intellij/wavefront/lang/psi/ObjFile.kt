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

    val verticesCount get() = objectLikeElements.sumBy { it.countChildrenOfType<ObjVertex>() }
    val textureCoordinatesCount get() = objectLikeElements.sumBy { it.countChildrenOfType<ObjTextureCoordinates>() }
    val vertexNormalsCount get() = objectLikeElements.sumBy { it.countChildrenOfType<ObjVertexNormal>() }

    val groupingElements: List<ObjGroupingElement> get() = getChildrenOfType()

    val objectLikeElements: List<PsiElement> get() = listOf(this) + groupingElements

    val materialFileReferences: List<ObjMaterialFileReference>
        get() = objectLikeElements
            .flatMap { it.getChildrenOfType<ObjMaterialFileReferenceStatement>() }
            .flatMap { it.getChildrenOfType() }

    val referencedMtlFiles: List<MtlFile> get() = materialFileReferences.mapNotNull { element -> element.mtlFile }

    override fun getFileType(): FileType = ObjFileType

    fun countVerticesBefore(element: PsiElement): Int =
        objectLikeElements.sumBy { it.countChildrenOfTypeBefore<ObjVertex>(element) }

    fun countTextureCoordinatesBefore(element: PsiElement): Int =
        objectLikeElements.sumBy { it.countChildrenOfTypeBefore<ObjTextureCoordinates>(element) }

    fun countVertexNormalsBefore(element: PsiElement): Int =
        objectLikeElements.sumBy { it.countChildrenOfTypeBefore<ObjVertexNormal>(element) }

    fun findMtlFile(filePath: String): MtlFile? = findRelativeFile(this, filePath) as? MtlFile

    override fun toString(): String = "Wavefront OBJ File"
}
