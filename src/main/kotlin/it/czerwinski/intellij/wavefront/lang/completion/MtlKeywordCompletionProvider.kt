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

import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.util.ProcessingContext
import it.czerwinski.intellij.wavefront.WavefrontObjBundle

internal class MtlKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        result.addAllElements(
            keywords.map { element ->
                LookupElementBuilder.create(element.text)
                    .withTypeText(element.description)
            }
        )
    }

    companion object {
        private val keywords = listOf(
            CompletionElement(
                text = "newmtl",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.NEW_MATERIAL_KEYWORD")
            ),
            CompletionElement(
                text = "Ka",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.AMBIENT_COLOR_KEYWORD")
            ),
            CompletionElement(
                text = "Kd",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.DIFFUSE_COLOR_KEYWORD")
            ),
            CompletionElement(
                text = "Ks",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.SPECULAR_COLOR_KEYWORD")
            ),
            CompletionElement(
                text = "Tf",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.mtl.tokenType.TRANSMISSION_FILTER_KEYWORD"
                )
            ),
            CompletionElement(
                text = "Ke",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.EMISSION_COLOR_KEYWORD")
            ),
            CompletionElement(
                text = "illum",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.ILLUMINATION_KEYWORD")
            ),
            CompletionElement(
                text = "d",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.DISSOLVE_KEYWORD")
            ),
            CompletionElement(
                text = "Ns",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.SPECULAR_EXPONENT_KEYWORD")
            ),
            CompletionElement(
                text = "sharpness",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.SHARPNESS_KEYWORD")
            ),
            CompletionElement(
                text = "Ni",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.OPTICAL_DENSITY_KEYWORD")
            ),
            CompletionElement(
                text = "Tr",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.TRANSPARENCY_KEYWORD")
            ),
            CompletionElement(
                text = "Pr",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.ROUGHNESS_KEYWORD")
            ),
            CompletionElement(
                text = "Pm",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.METALNESS_KEYWORD")
            ),
            CompletionElement(
                text = "map_Ka",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.AMBIENT_COLOR_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Kd",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.DIFFUSE_COLOR_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Ks",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.SPECULAR_COLOR_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Ke",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.EMISSION_COLOR_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Ns",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.mtl.tokenType.SPECULAR_EXPONENT_MAP_KEYWORD"
                )
            ),
            CompletionElement(
                text = "map_d",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.DISSOLVE_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "disp",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.DISPLACEMENT_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "decal",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.STENCIL_DECAL_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Pr",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.ROUGHNESS_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_Pm",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.METALNESS_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "norm",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.NORMAL_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "bump",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.BUMP_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "map_bump",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.BUMP_MAP_KEYWORD")
            ),
            CompletionElement(
                text = "refl",
                description = WavefrontObjBundle.message(key = "fileTypes.mtl.tokenType.REFLECTION_MAP_KEYWORD")
            )
        )
    }
}
