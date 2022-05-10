/*
 * Copyright 2020-2022 Slawomir Czerwinski
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
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementResolveResult
import com.intellij.psi.PsiPolyVariantReference
import com.intellij.psi.PsiReference
import com.intellij.psi.PsiReferenceBase
import com.intellij.psi.PsiReferenceProvider
import com.intellij.psi.ResolveResult
import com.intellij.util.ProcessingContext
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifier
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.findRelativePath

class MtlMaterialReference(
    element: PsiElement,
    textRange: TextRange
) : PsiReferenceBase<PsiElement>(element, textRange), PsiPolyVariantReference {

    private val objFile = element.containingFile.originalFile as? ObjFile
    private val materialName = element.text.substring(textRange.startOffset, textRange.endOffset)

    override fun multiResolve(incompleteCode: Boolean): Array<ResolveResult> =
        findMatchingMaterials()
            .map(::PsiElementResolveResult)
            .toTypedArray()

    private fun findMatchingMaterials(): List<MtlMaterialIdentifier> =
        findAvailableMaterials()
            .filter { material -> material.name == materialName }

    private fun findAvailableMaterials(): List<MtlMaterialIdentifier> =
        objFile?.referencedMtlFiles
            ?.flatMap { mtlFile -> mtlFile.materialIdentifiers }
            .orEmpty()

    override fun resolve(): PsiElement? =
        findMatchingMaterials().singleOrNull()

    override fun getVariants(): Array<Any> =
        findAvailableMaterials()
            .map { material ->
                val typeText: String? = findRelativePath(myElement.containingFile.originalFile, material.containingFile)
                    ?: material.containingFile?.name
                LookupElementBuilder.create(material)
                    .withIcon(MTL_MATERIAL_ICON)
                    .withTypeText(typeText, MTL_FILE_ICON, false)
            }
            .toTypedArray()

    object Provider : PsiReferenceProvider() {

        override fun getReferencesByElement(
            element: PsiElement,
            context: ProcessingContext
        ): Array<PsiReference> {
            val materialNameNode = element.node.findChildByType(ObjTypes.MATERIAL_NAME)
            val materialName = materialNameNode?.text
            return if (materialName != null) {
                val textRange = TextRange.from(
                    materialNameNode.startOffsetInParent,
                    materialNameNode.textLength
                )
                arrayOf(MtlMaterialReference(element, textRange))
            } else {
                PsiReference.EMPTY_ARRAY
            }
        }
    }
}
