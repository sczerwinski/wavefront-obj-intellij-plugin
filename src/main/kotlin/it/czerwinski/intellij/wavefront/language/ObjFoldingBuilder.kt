/*
 * Copyright 2020 Slawomir Czerwinski
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

@file:Suppress("TooManyFunctions")

package it.czerwinski.intellij.wavefront.language

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import it.czerwinski.intellij.wavefront.language.psi.ObjGroup
import it.czerwinski.intellij.wavefront.language.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.language.psi.ObjIndexElement
import it.czerwinski.intellij.wavefront.language.psi.ObjObject
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.language.psi.coordinatesString
import org.jetbrains.annotations.NonNls

class ObjFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> =
        listOf(
            buildObjGroupingElements(root),
            buildObjVertexIndices(root),
            buildObjTextureCoordinatesIndices(root),
            buildObjVertexNormalIndices(root)
        )
            .flatten()
            .map { element -> FoldingDescriptor(element.node, element.textRange) }
            .toTypedArray()

    private fun buildObjGroupingElements(root: PsiElement): List<PsiElement> =
        PsiTreeUtil.findChildrenOfType(root, ObjGroupingElement::class.java).toList()

    private fun buildObjVertexIndices(root: PsiElement): List<PsiElement> =
        PsiTreeUtil.findChildrenOfType(root, ObjVertexIndex::class.java)
            .filter { index -> checkVertexExists(index.containingFile, index.value ?: 0) }

    private fun buildObjTextureCoordinatesIndices(root: PsiElement): List<PsiElement> =
        PsiTreeUtil.findChildrenOfType(root, ObjTextureCoordinatesIndex::class.java)
            .filter { index -> checkTextureCoordinatesExist(index.containingFile, index.value ?: 0) }

    private fun buildObjVertexNormalIndices(root: PsiElement): List<PsiElement> =
        PsiTreeUtil.findChildrenOfType(root, ObjVertexNormalIndex::class.java)
            .filter { index -> checkVertexNormalExists(index.containingFile, index.value ?: 0) }

    override fun getPlaceholderText(node: ASTNode): String? =
        when (val element = node.psi) {

            is ObjObject -> OBJECT_PLACEHOLDER_TEXT_FORMAT.format(element.name)
            is ObjGroup -> GROUP_PLACEHOLDER_TEXT_FORMAT.format(element.name)

            is ObjVertexIndex -> getVertexPlaceholder(element)
            is ObjTextureCoordinatesIndex -> getTextureCoordinatesPlaceholder(element)
            is ObjVertexNormalIndex -> getVertexNormalPlaceholder(element)

            else -> DEFAULT_PLACEHOLDER_TEXT
        }

    private fun getVertexPlaceholder(element: ObjVertexIndex): String? =
        element.value
            ?.let { findVertex(element.containingFile, it)?.coordinatesString }
            ?: DEFAULT_PLACEHOLDER_TEXT

    private fun getTextureCoordinatesPlaceholder(element: ObjTextureCoordinatesIndex): String? =
        element.value
            ?.let { findTextureCoordinates(element.containingFile, it)?.coordinatesString }
            ?: DEFAULT_PLACEHOLDER_TEXT

    private fun getVertexNormalPlaceholder(element: ObjVertexNormalIndex): String? =
        element.value
            ?.let { findVertexNormal(element.containingFile, it)?.coordinatesString }
            ?: DEFAULT_PLACEHOLDER_TEXT

    override fun isCollapsedByDefault(node: ASTNode): Boolean = node.psi is ObjIndexElement

    companion object {
        @NonNls
        private const val DEFAULT_PLACEHOLDER_TEXT = "..."
        @NonNls
        private const val OBJECT_PLACEHOLDER_TEXT_FORMAT = "o %s ..."
        @NonNls
        private const val GROUP_PLACEHOLDER_TEXT_FORMAT = "g %s ..."
    }
}
