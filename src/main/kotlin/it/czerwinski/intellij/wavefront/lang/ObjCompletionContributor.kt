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

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.util.ProcessingContext
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType
import it.czerwinski.intellij.wavefront.lang.util.findMaterialFiles

class ObjCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(ObjTypes.MATERIAL_FILE_NAME),
            MaterialFileNameCompletionProvider
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(ObjTypes.MATERIAL_NAME),
            MaterialNameCompletionProvider
        )
    }

    object MaterialFileNameCompletionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            result.addAllElements(
                parameters.editor.project
                    ?.let { findMaterialFiles(it) }
                    ?.map { LookupElementBuilder.create(it) }
                    .orEmpty()
            )
        }
    }

    object MaterialNameCompletionProvider : CompletionProvider<CompletionParameters>() {

        override fun addCompletions(
            parameters: CompletionParameters,
            context: ProcessingContext,
            result: CompletionResultSet
        ) {
            result.addAllElements(
                (parameters.originalFile as? ObjFile)
                    ?.let { findMaterialFiles(it) }
                    ?.flatMap { file -> file.getChildrenOfType<MtlMaterial>() }
                    ?.map { LookupElementBuilder.create(it) }
                    .orEmpty()
            )
        }
    }
}
