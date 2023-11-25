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
import it.czerwinski.intellij.wavefront.lang.psi.DocumentedElement
import org.jetbrains.annotations.Nls

abstract class WavefrontDocumentationProvider<T : DocumentedElement> : AbstractDocumentationProvider() {

    protected fun generateDocumentation(element: T): String =
        StringBuilder()
            .addDefinition(element)
            .addContent(element)
            .addSections(element)
            .toString()

    protected abstract fun StringBuilder.addDefinition(element: T): StringBuilder

    private fun StringBuilder.addContent(element: DocumentedElement): StringBuilder {
        val content = element.documentation.commentBlock?.commentText
        if (content != null) {
            append(DocumentationMarkup.CONTENT_START)
            append(content)
            append(DocumentationMarkup.CONTENT_END)
        }
        return this
    }

    protected abstract fun StringBuilder.addSections(parentElement: T): StringBuilder

    protected fun StringBuilder.addSection(@Nls label: String, value: Any?): StringBuilder {
        append(DocumentationMarkup.SECTION_HEADER_START)
        append(label)
        append(DocumentationMarkup.SECTION_SEPARATOR)
        append(value)
        append(DocumentationMarkup.SECTION_END)
        return this
    }
}
