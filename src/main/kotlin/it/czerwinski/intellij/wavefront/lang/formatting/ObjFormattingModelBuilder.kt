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

package it.czerwinski.intellij.wavefront.lang.formatting

import com.intellij.formatting.FormattingContext
import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.codeStyle.CodeStyleSettings
import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.lang.ObjFileType
import it.czerwinski.intellij.wavefront.lang.ObjLanguage
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

class ObjFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel =
        FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.psiElement.containingFile,
            ObjBlock(
                node = formattingContext.psiElement.node,
                spacingBuilder = createSpaceBuilder(formattingContext.codeStyleSettings),
                indent = formattingContext.codeStyleSettings.getContinuationIndentSize(ObjFileType)
            ),
            formattingContext.codeStyleSettings
        )

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder =
        SpacingBuilder(settings, ObjLanguage)
            .around(comments).none()
            .before(keywords).none()
            .after(paramKeywords).spaces(1)
            .after(noParamKeywords).none()
            .between(keywords, params).spaces(1)
            .between(params, params).spaces(1)
            .around(ObjTypes.INDEX_SEPARATOR).none()

    companion object {

        private val comments = TokenSet.create(
            ObjTypes.COMMENT,
            ObjTypes.COMMENT_LINE,
            ObjTypes.COMMENT_BLOCK,
        )

        private val keywords = TokenSet.create(
            ObjTypes.OBJECT_KEYWORD,
            ObjTypes.GROUP_KEYWORD,
            ObjTypes.VERTEX_KEYWORD,
            ObjTypes.TEXTURE_COORDINATES_KEYWORD,
            ObjTypes.VERTEX_NORMAL_KEYWORD,
            ObjTypes.FACE_KEYWORD,
            ObjTypes.LINE_KEYWORD,
            ObjTypes.POINT_KEYWORD,
            ObjTypes.FREE_FORM_POINT_KEYWORD,
            ObjTypes.FREE_FORM_TYPE_KEYWORD,
            ObjTypes.FREE_FORM_DEGREE_KEYWORD,
            ObjTypes.FREE_FORM_BASIS_MATRIX_KEYWORD,
            ObjTypes.FREE_FORM_STEP_SIZE_KEYWORD,
            ObjTypes.FREE_FORM_CURVE_KEYWORD,
            ObjTypes.FREE_FORM_2D_CURVE_KEYWORD,
            ObjTypes.FREE_FORM_SURFACE_KEYWORD,
            ObjTypes.FREE_FORM_PARAMETERS_KEYWORD,
            ObjTypes.FREE_FORM_OUTER_TRIMMING_LOOP_KEYWORD,
            ObjTypes.FREE_FORM_INNER_TRIMMING_LOOP_KEYWORD,
            ObjTypes.FREE_FORM_SPECIAL_CURVE_KEYWORD,
            ObjTypes.FREE_FORM_SPECIAL_POINTS_KEYWORD,
            ObjTypes.FREE_FORM_BODY_END_KEYWORD,
            ObjTypes.SMOOTHING_GROUP_KEYWORD,
            ObjTypes.MATERIAL_FILE_REF_KEYWORD,
            ObjTypes.MATERIAL_REFERENCE_KEYWORD
        )

        private val noParamKeywords = TokenSet.create(ObjTypes.FREE_FORM_BODY_END_KEYWORD)
        private val paramKeywords = TokenSet.andNot(keywords, noParamKeywords)

        private val params = TokenSet.create(
            ObjTypes.OBJECT_OR_GROUP_IDENTIFIER,
            ObjTypes.OBJECT_OR_GROUP_NAME,
            ObjTypes.FREE_FORM_TYPE_NAME,
            ObjTypes.FREE_FORM_DIRECTION,
            ObjTypes.FREE_FORM_CURVE_REFERENCE,
            ObjTypes.SMOOTHING_GROUP_NUMBER,
            ObjTypes.NUMBER,
            ObjTypes.FLOAT,
            ObjTypes.INTEGER,
            ObjTypes.INDEX,
            ObjTypes.VERTEX_INDEX,
            ObjTypes.TEXTURE_COORDINATES_INDEX,
            ObjTypes.VERTEX_NORMAL_INDEX,
            ObjTypes.FREE_FORM_POINT_INDEX,
            ObjTypes.FREE_FORM_CURVE_INDEX,
            ObjTypes.LINE_VERTEX,
            ObjTypes.FACE_VERTEX,
            ObjTypes.MATERIAL_FILE_REFERENCE,
            ObjTypes.MATERIAL_FILE_NAME,
            ObjTypes.MATERIAL_REFERENCE,
            ObjTypes.MATERIAL_NAME
        )
    }
}
