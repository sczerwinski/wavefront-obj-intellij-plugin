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
import it.czerwinski.intellij.wavefront.language.psi.ObjObject
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormalIndex
import org.jetbrains.annotations.NonNls

class ObjFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> =
        listOf(
            buildObjObjectFoldRegions(root),
            buildObjGroupFoldRegions(root),
            buildObjVertexIndexFoldRegions(root),
            buildObjTextureCoordinatesIndexFoldRegions(root),
            buildObjVertexNormalIndexFoldRegions(root)
        ).flatten().toTypedArray()

    private fun buildObjObjectFoldRegions(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjObject::class.java)
            .map(::createFoldingDescriptor)

    private fun buildObjGroupFoldRegions(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjGroup::class.java)
            .map(::createFoldingDescriptor)

    private fun buildObjVertexIndexFoldRegions(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjVertexIndex::class.java)
            .filter { index -> checkVertexExists(index.containingFile, index.value ?: 0) }
            .map(::createFoldingDescriptor)

    private fun buildObjTextureCoordinatesIndexFoldRegions(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjTextureCoordinatesIndex::class.java)
            .filter { index -> checkTextureCoordinatesExist(index.containingFile, index.value ?: 0) }
            .map(::createFoldingDescriptor)

    private fun buildObjVertexNormalIndexFoldRegions(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjVertexNormalIndex::class.java)
            .filter { index -> checkVertexNormalExists(index.containingFile, index.value ?: 0) }
            .map(::createFoldingDescriptor)

    private fun createFoldingDescriptor(element: PsiElement): FoldingDescriptor =
        FoldingDescriptor(element.node, element.textRange)

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
            ?.let { findVertex(element.containingFile, it) }
            ?.let { vertex -> joinCoordinates(coordinates = vertex.coordinates) }
            ?: DEFAULT_PLACEHOLDER_TEXT

    private fun getTextureCoordinatesPlaceholder(element: ObjTextureCoordinatesIndex): String? =
        element.value
            ?.let { findTextureCoordinates(element.containingFile, it) }
            ?.let { textureCoordinates -> joinCoordinates(coordinates = textureCoordinates.coordinates) }
            ?: DEFAULT_PLACEHOLDER_TEXT

    private fun getVertexNormalPlaceholder(element: ObjVertexNormalIndex): String? =
        element.value
            ?.let { findVertexNormal(element.containingFile, it) }
            ?.let { vertexNormal -> joinCoordinates(vertexNormal.coordinates) }
            ?: DEFAULT_PLACEHOLDER_TEXT

    private fun joinCoordinates(coordinates: MutableList<Float>): String =
        coordinates.joinToString(prefix = "[", separator = " ", postfix = "]")

    override fun isCollapsedByDefault(node: ASTNode): Boolean =
        node.psi is ObjVertexIndex || node.psi is ObjTextureCoordinatesIndex || node.psi is ObjVertexNormalIndex

    companion object {
        @NonNls
        private const val DEFAULT_PLACEHOLDER_TEXT = "..."
        @NonNls
        private const val OBJECT_PLACEHOLDER_TEXT_FORMAT = "o %s ..."
        @NonNls
        private const val GROUP_PLACEHOLDER_TEXT_FORMAT = "g %s ..."
    }
}
