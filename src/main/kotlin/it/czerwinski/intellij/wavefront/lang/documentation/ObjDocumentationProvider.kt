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

import com.intellij.lang.documentation.DocumentationMarkup
import com.intellij.psi.PsiElement
import com.intellij.psi.util.parentOfType
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import it.czerwinski.intellij.wavefront.lang.psi.ObjObjectOrGroupIdentifier

class ObjDocumentationProvider : WavefrontDocumentationProvider<ObjGroupingElement>() {

    override fun generateDoc(element: PsiElement?, originalElement: PsiElement?): String? {
        return when (element) {
            is ObjObjectOrGroupIdentifier ->
                element.parentOfType<ObjGroupingElement>()?.let { generateDocumentation(it) }

            else ->
                null
        }
    }

    override fun StringBuilder.addDefinition(element: ObjGroupingElement): StringBuilder {
        append(DocumentationMarkup.DEFINITION_START)
        append(
            when (element) {
                is ObjObject -> DEFINITION_FORMAT_OBJECT
                is ObjGroup -> DEFINITION_FORMAT_GROUP
                else -> DEFINITION_FORMAT_UNKNOWN
            }.format(element.getName())
        )
        append(DocumentationMarkup.DEFINITION_END)
        return this
    }

    override fun StringBuilder.addSections(parentElement: ObjGroupingElement): StringBuilder {
        append(DocumentationMarkup.SECTIONS_START)
        addSection(
            label = WavefrontObjBundle.message(key = "fileTypes.obj.documentation.vertices"),
            value = parentElement.verticesCount
        )
        addSection(
            label = WavefrontObjBundle.message(key = "fileTypes.obj.documentation.faces"),
            value = parentElement.facesCount
        )
        addSection(
            label = WavefrontObjBundle.message(key = "fileTypes.obj.documentation.triangles"),
            value = parentElement.trianglesCount
        )
        append(DocumentationMarkup.SECTIONS_END)
        return this
    }

    companion object {
        private const val DEFINITION_FORMAT_OBJECT = "o %s"
        private const val DEFINITION_FORMAT_GROUP = "o %s"
        private const val DEFINITION_FORMAT_UNKNOWN = "%s"
    }
}
