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

import com.intellij.lang.ASTNode
import com.intellij.lang.folding.FoldingBuilderEx
import com.intellij.lang.folding.FoldingDescriptor
import com.intellij.openapi.editor.Document
import com.intellij.openapi.project.DumbAware
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjIndexElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import org.jetbrains.annotations.NonNls

class ObjFoldingBuilder : FoldingBuilderEx(), DumbAware {

    override fun buildFoldRegions(root: PsiElement, document: Document, quick: Boolean): Array<FoldingDescriptor> =
        buildObjGroupingElements(root)
            .map { element -> FoldingDescriptor(element.node, element.textRange) }
            .toTypedArray()

    private fun buildObjGroupingElements(root: PsiElement): List<PsiElement> =
        PsiTreeUtil.findChildrenOfType(root, ObjGroupingElement::class.java).toList()

    override fun getPlaceholderText(node: ASTNode): String =
        when (val element = node.psi) {
            is ObjObject -> OBJECT_PLACEHOLDER_TEXT_FORMAT.format(element.getName())
            is ObjGroup -> GROUP_PLACEHOLDER_TEXT_FORMAT.format(element.getName())

            else -> DEFAULT_PLACEHOLDER_TEXT
        }

    override fun isCollapsedByDefault(node: ASTNode): Boolean = node.psi is ObjIndexElement

    companion object {
        @NonNls private const val DEFAULT_PLACEHOLDER_TEXT = "..."
        @NonNls private const val OBJECT_PLACEHOLDER_TEXT_FORMAT = "o %s ..."
        @NonNls private const val GROUP_PLACEHOLDER_TEXT_FORMAT = "g %s ..."
    }
}
