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

package it.czerwinski.intellij.wavefront.lang.folding

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.util.TextRange
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.psi.util.childrenOfType
import it.czerwinski.intellij.wavefront.lang.psi.ObjCommentBlock
import it.czerwinski.intellij.wavefront.lang.psi.ObjDocumentation
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeForm2DCurve
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormCurve
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormSurface
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import org.jetbrains.annotations.NonNls

class ObjFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> =
        listOf(
            buildObjGroupingElements(root),
            buildObjFreeFormGeometry(root),
            buildObjCommentBlocks(root)
        ).flatten().toTypedArray()

    private fun buildObjGroupingElements(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjGroupingElement::class.java)
            .map { element ->
                val startOffset = element.childrenOfType<ObjDocumentation>().singleOrNull()?.textRange?.endOffset
                    ?: element.textRange.startOffset
                val endOffset = element.textRange.endOffset
                FoldingDescriptor(element.node, TextRange.create(startOffset, endOffset))
            }

    private fun buildObjFreeFormGeometry(root: PsiElement): List<FoldingDescriptor> =
        sequenceOf(
            PsiTreeUtil.findChildrenOfType(root, ObjFreeFormCurve::class.java),
            PsiTreeUtil.findChildrenOfType(root, ObjFreeForm2DCurve::class.java),
            PsiTreeUtil.findChildrenOfType(root, ObjFreeFormSurface::class.java)
        )
            .flatten()
            .map { element -> FoldingDescriptor(element.node, element.textRange) }
            .toList()

    private fun buildObjCommentBlocks(root: PsiElement): List<FoldingDescriptor> =
        PsiTreeUtil.findChildrenOfType(root, ObjCommentBlock::class.java)
            .filter { commentBlock -> commentBlock.commentLineList.size > 1 }
            .map { element ->
                val startOffset = element.textRange.startOffset
                val endOffset = element.textRange.startOffset + element.text.trimEnd().length
                FoldingDescriptor(element.node, TextRange.create(startOffset, endOffset))
            }

    override fun getPlaceholderText(node: ASTNode): String =
        when (val element = node.psi) {
            is ObjObject -> OBJECT_PLACEHOLDER_TEXT_FORMAT.format(element.getName())
            is ObjGroup -> GROUP_PLACEHOLDER_TEXT_FORMAT.format(element.getName())
            is ObjFreeFormCurve -> CURVE_PLACEHOLDER_TEXT
            is ObjFreeForm2DCurve -> CURVE_2D_PLACEHOLDER_TEXT
            is ObjFreeFormSurface -> SURFACE_PLACEHOLDER_TEXT
            is ObjCommentBlock -> COMMENT_BLOCK_PLACEHOLDER_TEXT_FORMAT.format(
                element.commentLineList.firstOrNull()?.text?.trim().orEmpty()
            )

            else -> DEFAULT_PLACEHOLDER_TEXT
        }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = false

    companion object {
        @NonNls private const val DEFAULT_PLACEHOLDER_TEXT = "..."
        @NonNls private const val OBJECT_PLACEHOLDER_TEXT_FORMAT = "o %s ..."
        @NonNls private const val GROUP_PLACEHOLDER_TEXT_FORMAT = "g %s ..."
        @NonNls private const val CURVE_PLACEHOLDER_TEXT = "curv ..."
        @NonNls private const val CURVE_2D_PLACEHOLDER_TEXT = "curv2 ..."
        @NonNls private const val SURFACE_PLACEHOLDER_TEXT = "surf ..."
        @NonNls private const val COMMENT_BLOCK_PLACEHOLDER_TEXT_FORMAT = "%s ..."
    }
}
