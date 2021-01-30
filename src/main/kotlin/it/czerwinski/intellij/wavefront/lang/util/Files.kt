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

package it.czerwinski.intellij.wavefront.lang.util

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import it.czerwinski.intellij.wavefront.lang.MtlFileType
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

fun findTextureFile(element: MtlTextureElement): PsiFile? =
    findTextureFiles(element).firstOrNull()

fun findTextureFiles(element: MtlTextureElement): List<PsiFile> =
    element.textureFilename?.let { findTextureFiles(element.project, it) }.orEmpty()

fun findTextureFiles(project: Project, filename: String): List<PsiFile> =
    FilenameIndex.getFilesByName(project, filename, GlobalSearchScope.allScope(project))
        .toList()

fun findMaterialFile(element: ObjMaterialFileReference): MtlFile? =
    findMaterialFiles(element).firstOrNull()

fun findMaterialFiles(element: ObjMaterialFileReference): List<MtlFile> =
    findMaterialFiles(element.project)
        .filter { file -> file.name == element.filename }

fun findMaterialFiles(project: Project): List<MtlFile> =
    FileTypeIndex.getFiles(MtlFileType, GlobalSearchScope.allScope(project))
        .mapNotNull { virtualFile -> PsiManager.getInstance(project).findFile(virtualFile) }
        .filterIsInstance<MtlFile>()

fun findMaterialFiles(objFile: ObjFile): List<MtlFile> {
    val materialFiles = findMaterialFiles(objFile.project)
    val materialFileReferences = objFile.getChildrenOfType<ObjMaterialFileReference>() +
        objFile.getChildrenOfType<ObjGroupingElement>()
            .flatMap { it.getChildrenOfType<ObjMaterialFileReference>() }
    val materialFileNames = materialFileReferences.mapNotNull { fileReference -> fileReference.filename }
    return materialFiles.filter { file -> file.name in materialFileNames }
}
