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

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FilenameIndex
import com.intellij.psi.search.GlobalSearchScope
import com.jogamp.opengl.util.texture.TextureIO

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

fun Project.findMatchingTextureFiles(filename: String): List<PsiFile> = runReadAction {
    FilenameIndex.getVirtualFilesByName(filename, GlobalSearchScope.allScope(this))
        .mapNotNull { virtualFile -> PsiManager.getInstance(this).findFile(virtualFile) }
}
