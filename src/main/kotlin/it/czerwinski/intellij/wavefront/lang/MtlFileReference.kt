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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.icons.AllIcons
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.util.ProcessingContext
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile
import it.czerwinski.intellij.wavefront.lang.psi.util.findAllMtlFiles
import it.czerwinski.intellij.wavefront.lang.psi.util.findRelativePath

class MtlFileReference(
    element: PsiElement,
    textRange: TextRange
) : PsiReferenceBase<PsiElement>(element, textRange) {

    private val filename = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun resolve(): PsiElement? = myElement.containingObjFile?.findMtlFile(filename)

    override fun getVariants(): Array<Any> =
        myElement.project
            .findAllMtlFiles()
            .map { file ->
                val root = ProjectFileIndex.SERVICE.getInstance(myElement.project)
                    .getContentRootForFile(file.virtualFile)
                val typeText = root?.let { VfsUtil.getRelativePath(file.virtualFile.parent, it) }
                    ?: file.containingDirectory?.name
                val text = findRelativePath(myElement.containingFile.originalFile, file)
                    ?: file.name
                LookupElementBuilder.create(text)
                    .withIcon(MTL_FILE_ICON)
                    .withTypeText(typeText, AllIcons.Nodes.Folder, false)
            }
            .toTypedArray()

    override fun handleElementRename(newElementName: String): PsiElement =
        super.handleElementRename(filename.replaceAfterLast(PATH_DELIMITER, newElementName, newElementName))

    companion object {
        private const val PATH_DELIMITER = '/'
    }

    object Provider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val materialFilenameNode = element.node.findChildByType(ObjTypes.MATERIAL_FILE_NAME)
            val materialFilename = materialFilenameNode?.text
            return if (materialFilename != null) {
                val textRange = TextRange.from(
                    materialFilenameNode.startOffsetInParent,
                    materialFilenameNode.textLength
                )
                arrayOf(MtlFileReference(element, textRange))
            } else {
                PsiReference.EMPTY_ARRAY
            }
        }
    }
}
