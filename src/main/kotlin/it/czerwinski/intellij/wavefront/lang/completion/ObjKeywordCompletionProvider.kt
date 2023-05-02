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

internal class ObjKeywordCompletionProvider : CompletionProvider<CompletionParameters>() {

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
                text = "o",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.OBJECT_KEYWORD")
            ),
            CompletionElement(
                text = "g",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.GROUP_KEYWORD")
            ),
            CompletionElement(
                text = "v",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.VERTEX_KEYWORD")
            ),
            CompletionElement(
                text = "vt",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.obj.tokenType.TEXTURE_COORDINATES_KEYWORD"
                )
            ),
            CompletionElement(
                text = "vn",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.VERTEX_NORMAL_KEYWORD")
            ),
            CompletionElement(
                text = "f",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FACE_KEYWORD")
            ),
            CompletionElement(
                text = "l",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.LINE_KEYWORD")
            ),
            CompletionElement(
                text = "p",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.POINT_KEYWORD")
            ),
            CompletionElement(
                text = "vp",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_POINT_KEYWORD")
            ),
            CompletionElement(
                text = "cstype",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_TYPE_KEYWORD")
            ),
            CompletionElement(
                text = "cstype rat",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_TYPE_KEYWORD.rat")
            ),
            CompletionElement(
                text = "deg",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_DEGREE_KEYWORD")
            ),
            CompletionElement(
                text = "bmat",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_BASIS_MATRIX_KEYWORD")
            ),
            CompletionElement(
                text = "step",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_STEP_SIZE_KEYWORD")
            ),
            CompletionElement(
                text = "curv",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_CURVE_KEYWORD")
            ),
            CompletionElement(
                text = "curv2",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_2D_CURVE_KEYWORD")
            ),
            CompletionElement(
                text = "surf",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_SURFACE_KEYWORD")
            ),
            CompletionElement(
                text = "parm",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_PARAMETERS_KEYWORD")
            ),
            CompletionElement(
                text = "trim",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.obj.tokenType.FREE_FORM_OUTER_TRIMMING_LOOP_KEYWORD"
                )
            ),
            CompletionElement(
                text = "hole",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.obj.tokenType.FREE_FORM_INNER_TRIMMING_LOOP_KEYWORD"
                )
            ),
            CompletionElement(
                text = "scrv",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.obj.tokenType.FREE_FORM_SPECIAL_CURVE_KEYWORD"
                )
            ),
            CompletionElement(
                text = "sp",
                description = WavefrontObjBundle.message(
                    key = "fileTypes.obj.tokenType.FREE_FORM_SPECIAL_POINTS_KEYWORD"
                )
            ),
            CompletionElement(
                text = "end",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.FREE_FORM_BODY_END_KEYWORD")
            ),
            CompletionElement(
                text = "s",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.SMOOTHING_GROUP_KEYWORD")
            ),
            CompletionElement(
                text = "mtllib",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.MATERIAL_FILE_REF_KEYWORD")
            ),
            CompletionElement(
                text = "usemtl",
                description = WavefrontObjBundle.message(key = "fileTypes.obj.tokenType.MATERIAL_REFERENCE_KEYWORD")
            )
        )
    }
}
