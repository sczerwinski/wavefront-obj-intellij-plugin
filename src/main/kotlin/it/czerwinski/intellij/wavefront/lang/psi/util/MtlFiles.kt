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

package it.czerwinski.intellij.wavefront.lang.psi.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import it.czerwinski.intellij.wavefront.lang.MtlFileType
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference

fun findMtlFile(element: ObjMaterialFileReference): MtlFile? =
    findMtlFile(element.containingFile, element.filename.orEmpty())

fun findMtlFile(sourceFile: PsiFile?, filePath: String): MtlFile? =
    sourceFile?.let { file ->
        findRelativeFile(file, filePath)
    } as? MtlFile

fun findAllMtlFiles(project: Project): List<MtlFile> =
    FileTypeIndex.getFiles(MtlFileType, GlobalSearchScope.allScope(project))
        .mapNotNull { virtualFile -> PsiManager.getInstance(project).findFile(virtualFile) }
        .filterIsInstance<MtlFile>()

fun findReferencedMtlFiles(objFile: ObjFile): List<MtlFile> {
    val materialFileReferences = objFile.getChildrenOfType<ObjMaterialFileReference>() +
        objFile.getChildrenOfType<ObjGroupingElement>()
            .flatMap { it.getChildrenOfType<ObjMaterialFileReference>() }
    return materialFileReferences.mapNotNull { element -> findMtlFile(element) }
}
