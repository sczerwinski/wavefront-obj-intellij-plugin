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

package it.czerwinski.intellij.wavefront.lang.psi.util

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.vfs.findFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jogamp.opengl.util.texture.TextureIO
import java.io.File

private val textureFilesExtensions = listOf(
    TextureIO.DDS,
    TextureIO.SGI,
    TextureIO.SGI_RGB,
    TextureIO.GIF,
    TextureIO.JPG,
    TextureIO.PNG,
    TextureIO.TGA,
    TextureIO.TIFF,
    TextureIO.PAM,
    TextureIO.PPM
)

fun Project.findAllTextureFiles(): List<PsiFile> =
    textureFilesExtensions.flatMap { extension ->
        FilenameIndex.getAllFilesByExt(this, extension, GlobalSearchScope.allScope(this))
    }.mapNotNull { virtualFile ->
        PsiManager.getInstance(this).findFile(virtualFile)
    }

fun Project.findMatchingTextureFiles(filename: String, relativeTo: VirtualFile? = null): List<PsiFile> =
    findMatchingTextureVirtualFiles(filename, relativeTo)
        .mapNotNull { virtualFile -> PsiManager.getInstance(this).findFile(virtualFile) }

fun Project.findMatchingTextureVirtualFiles(
    filename: String,
    relativeTo: VirtualFile? = null
): Collection<VirtualFile> {
    val baseDirectory = if (relativeTo?.isDirectory != false) relativeTo else relativeTo.parent
    val relativeFile = baseDirectory?.findFile(relativePath = filename)
    if (relativeFile != null) {
        return listOf(relativeFile)
    }
    val rawFilename = File(filename).name
    return FilenameIndex.getVirtualFilesByName(rawFilename, GlobalSearchScope.allScope(this))
}

fun PsiElement?.isTextureFile(): Boolean =
    this is PsiFile && textureFilesExtensions.any { ext -> this.name.endsWith(".$ext", ignoreCase = true) }
