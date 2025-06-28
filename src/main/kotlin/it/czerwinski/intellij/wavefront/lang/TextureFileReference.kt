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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.ResolveResult
import com.intellij.util.ProcessingContext
import it.czerwinski.intellij.wavefront.icons.Icons
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.util.findAllTextureFiles
import it.czerwinski.intellij.wavefront.lang.psi.util.findMatchingTextureFiles

class TextureFileReference(
    element: PsiElement,
    textRange: TextRange
) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private val filename = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findMatchingMtlFiles()
            .map(::PsiElementResolveResult)
            .toTypedArray()

    private fun findMatchingMtlFiles(): List<PsiFile> =
        myElement.project.findMatchingTextureFiles(filename, relativeTo = element.containingFile?.virtualFile)

    override fun resolve(): PsiElement? =
        findMatchingMtlFiles().singleOrNull()

    override fun getVariants(): Array<Any> =
        myElement.project
            .findAllTextureFiles()
            .map { file ->
                val root = ProjectFileIndex.getInstance(myElement.project)
                    .getContentRootForFile(file.virtualFile)
                val typeText = root?.let { VfsUtil.getRelativePath(file.virtualFile.parent, it) }
                    ?: file.containingDirectory?.name
                LookupElementBuilder.create(file.name)
                    .withIcon(file.fileType.icon ?: Icons.Structure.Mtl.Texture)
                    .withTypeText(typeText, Icons.General.Folder, false)
            }
            .toTypedArray()

    object Provider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val textureFilenameNode = (element as? MtlTextureElement)?.textureFilenameNode
            val textureFilename = textureFilenameNode?.text
            return if (textureFilename != null) {
                val textRange = TextRange.from(
                    textureFilenameNode.startOffsetInParent,
                    textureFilenameNode.textLength
                )
                arrayOf(TextureFileReference(element, textRange))
            } else {
                PsiReference.EMPTY_ARRAY
            }
        }
    }
}
