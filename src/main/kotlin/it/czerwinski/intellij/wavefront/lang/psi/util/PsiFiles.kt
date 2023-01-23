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

import com.intellij.openapi.vfs.VfsUtilCore
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiFileSystemItem

private const val PATH_DELIMITER = '/'
private const val CURRENT_DIR = "."
private const val PARENT_DIR = ".."

fun createRelativeFile(directory: PsiDirectory, filePath: String): PsiFile? {
    tailrec fun createRelativeFile(dir: PsiDirectory?, path: List<String>): PsiFile? =
        when {
            dir == null || path.isEmpty() -> null
            path.size == 1 -> dir.createFile(path.first())
            else -> createRelativeFile(
                when (val subdirectory = path.first()) {
                    CURRENT_DIR -> dir
                    PARENT_DIR -> dir.parentDirectory
                    else -> dir.findSubdirectory(subdirectory) ?: dir.createSubdirectory(subdirectory)
                },
                path.drop(1)
            )
        }

    return createRelativeFile(
        dir = directory,
        path = filePath.split(PATH_DELIMITER)
    )
}

fun findRelativeFile(sourceFile: PsiFile, filePath: String): PsiFile? {
    tailrec fun findRelativeFile(dir: PsiDirectory?, path: List<String>): PsiFile? =
        when {
            dir == null || path.isEmpty() -> null
            path.size == 1 -> dir.findFile(path.first())
            else -> findRelativeFile(
                when (val subdirectory = path.first()) {
                    CURRENT_DIR -> dir
                    PARENT_DIR -> dir.parentDirectory
                    else -> dir.findSubdirectory(subdirectory)
                },
                path.drop(1)
            )
        }

    return findRelativeFile(
        dir = sourceFile.containingDirectory,
        path = filePath.split(PATH_DELIMITER)
    )
}

fun findRelativePath(sourceFile: PsiFile, targetFile: PsiFileSystemItem): String? =
    VfsUtilCore.findRelativePath(sourceFile.virtualFile.parent, targetFile.virtualFile, PATH_DELIMITER)
