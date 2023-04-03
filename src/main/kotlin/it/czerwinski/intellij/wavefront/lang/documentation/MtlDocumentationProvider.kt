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

package it.czerwinski.intellij.wavefront.lang.documentation

import com.intellij.lang.documentation.AbstractDocumentationProvider
import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifierElement

class MtlDocumentationProvider : AbstractDocumentationProvider() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return when (element) {
            is MtlMaterialIdentifierElement ->
                element.parentOfType<MtlMaterialElement>()?.let { generateMaterialIdentifierDocumentation(it) }

            else ->
                null
        }
    }

    private fun generateMaterialIdentifierDocumentation(element: MtlMaterialElement): String =
        StringBuilder()
            .addDefinition(element)
            .addContent(element)
            .toString()

    private fun StringBuilder.addDefinition(element: MtlMaterialElement): StringBuilder {
        append(DocumentationMarkup.DEFINITION_START)
        append(DEFINITION_FORMAT_MATERIAL.format(element.getName()))
        append(DocumentationMarkup.DEFINITION_END)
        return this
    }

    private fun StringBuilder.addContent(element: MtlMaterialElement): StringBuilder {
        val content = element.documentation.commentBlock?.commentLineList
            ?.joinToString(
                prefix = DocumentationMarkup.CONTENT_START,
                separator = "",
                postfix = DocumentationMarkup.CONTENT_END
            ) { it.lastChild.text }
        if (content != null) {
            append(content)
        }
        return this
    }

    companion object {
        private const val DEFINITION_FORMAT_MATERIAL = "newmtl %s"
    }
}
