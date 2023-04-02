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

package it.czerwinski.intellij.wavefront.lang.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.patterns.PlatformPatterns
import it.czerwinski.intellij.wavefront.lang.psi.MtlUnknownTypes

class MtlCompletionContributor : CompletionContributor() {

    init {
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_KEYWORD),
            MtlKeywordCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_OPTION),
            MtlOptionCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_REFLECTION_TYPE_OPTION),
            MtlReflectionTypeOptionCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_REFLECTION_TYPE),
            MtlReflectionTypeCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_FLAG),
            MtlFlagCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_ILLUMINATION_VALUE),
            MtlIlluminationValueCompletionProvider()
        )
        extend(
            CompletionType.BASIC,
            PlatformPatterns.psiElement(MtlUnknownTypes.UNKNOWN_SCALAR_CHANNEL),
            MtlScalarChannelCompletionProvider()
        )
    }
}
