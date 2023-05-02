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
import it.czerwinski.intellij.wavefront.lang.ObjLanguage
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

class ObjFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(formattingContext: FormattingContext): FormattingModel =
        FormattingModelProvider.createFormattingModelForPsiFile(
            formattingContext.psiElement.containingFile,
            WavefrontBlock(
                node = formattingContext.psiElement.node,
                spacingBuilder = createSpaceBuilder(formattingContext.codeStyleSettings)
            ),
            formattingContext.codeStyleSettings
        )

    private fun createSpaceBuilder(settings: CodeStyleSettings): SpacingBuilder =
        SpacingBuilder(settings, ObjLanguage)
            .before(ObjTypes.OBJECT_KEYWORD).none()
            .before(ObjTypes.GROUP_KEYWORD).none()
            .before(ObjTypes.VERTEX_KEYWORD).none()
            .before(ObjTypes.TEXTURE_COORDINATES_KEYWORD).none()
            .before(ObjTypes.VERTEX_NORMAL_KEYWORD).none()
            .before(ObjTypes.FACE_KEYWORD).none()
            .before(ObjTypes.LINE_KEYWORD).none()
            .before(ObjTypes.POINT_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_POINT_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_TYPE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_DEGREE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_BASIS_MATRIX_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_STEP_SIZE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_CURVE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_2D_CURVE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_SURFACE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_PARAMETERS_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_OUTER_TRIMMING_LOOP_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_INNER_TRIMMING_LOOP_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_SPECIAL_CURVE_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_SPECIAL_POINTS_KEYWORD).none()
            .before(ObjTypes.FREE_FORM_BODY_END_KEYWORD).none()
            .before(ObjTypes.SMOOTHING_GROUP_KEYWORD).none()
            .before(ObjTypes.MATERIAL_FILE_REF_KEYWORD).none()
            .before(ObjTypes.MATERIAL_REFERENCE_KEYWORD).none()
            .between(ObjTypes.OBJECT_KEYWORD, ObjTypes.OBJECT_OR_GROUP_IDENTIFIER).spaces(1)
            .between(ObjTypes.GROUP_KEYWORD, ObjTypes.OBJECT_OR_GROUP_IDENTIFIER).spaces(1)
            .between(ObjTypes.VERTEX_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.TEXTURE_COORDINATES_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.VERTEX_NORMAL_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FACE_KEYWORD, ObjTypes.FACE_VERTEX).spaces(1)
            .between(ObjTypes.LINE_KEYWORD, ObjTypes.VERTEX_INDEX).spaces(1)
            .between(ObjTypes.POINT_KEYWORD, ObjTypes.VERTEX_INDEX).spaces(1)
            .between(ObjTypes.FREE_FORM_POINT_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FREE_FORM_TYPE_KEYWORD, ObjTypes.FREE_FORM_RATIONAL_KEYWORD).spaces(1)
            .between(ObjTypes.FREE_FORM_TYPE_KEYWORD, ObjTypes.FREE_FORM_TYPE_NAME).spaces(1)
            .between(ObjTypes.FREE_FORM_RATIONAL_KEYWORD, ObjTypes.FREE_FORM_TYPE_NAME).spaces(1)
            .between(ObjTypes.FREE_FORM_DEGREE_KEYWORD, ObjTypes.INTEGER).spaces(1)
            .between(ObjTypes.FREE_FORM_BASIS_MATRIX_KEYWORD, ObjTypes.FREE_FORM_DIRECTION).spaces(1)
            .between(ObjTypes.FREE_FORM_STEP_SIZE_KEYWORD, ObjTypes.INTEGER).spaces(1)
            .between(ObjTypes.FREE_FORM_CURVE_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FREE_FORM_2D_CURVE_KEYWORD, ObjTypes.FREE_FORM_POINT_INDEX).spaces(1)
            .between(ObjTypes.FREE_FORM_SURFACE_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FREE_FORM_PARAMETERS_KEYWORD, ObjTypes.FREE_FORM_DIRECTION).spaces(1)
            .between(ObjTypes.FREE_FORM_OUTER_TRIMMING_LOOP_KEYWORD, ObjTypes.CURVE_REFERENCE).spaces(1)
            .between(ObjTypes.FREE_FORM_INNER_TRIMMING_LOOP_KEYWORD, ObjTypes.CURVE_REFERENCE).spaces(1)
            .between(ObjTypes.FREE_FORM_SPECIAL_CURVE_KEYWORD, ObjTypes.CURVE_REFERENCE).spaces(1)
            .between(ObjTypes.FREE_FORM_SPECIAL_POINTS_KEYWORD, ObjTypes.FREE_FORM_POINT_INDEX).spaces(1)
            .between(ObjTypes.SMOOTHING_GROUP_KEYWORD, ObjTypes.SMOOTHING_GROUP_NUMBER).spaces(1)
            .between(ObjTypes.MATERIAL_FILE_REF_KEYWORD, ObjTypes.MATERIAL_FILE_REFERENCE).spaces(1)
            .between(ObjTypes.MATERIAL_REFERENCE_KEYWORD, ObjTypes.MATERIAL_NAME).spaces(1)
            .after(ObjTypes.OBJECT_OR_GROUP_NAME).none()
            .after(ObjTypes.OBJECT_OR_GROUP_IDENTIFIER).none()
            .between(ObjTypes.INTEGER, ObjTypes.INTEGER).spaces(1)
            .between(ObjTypes.FLOAT, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FLOAT, ObjTypes.VERTEX_INDEX).spaces(1)
            .between(ObjTypes.FLOAT, ObjTypes.FACE_VERTEX).spaces(1)
            .between(ObjTypes.FLOAT, ObjTypes.CURVE_INDEX).spaces(1)
            .between(ObjTypes.FACE_VERTEX, ObjTypes.FACE_VERTEX).spaces(1)
            .between(ObjTypes.FACE_VERTEX, ObjTypes.FLOAT).spaces(1)
            .around(ObjTypes.VERTEX_INDEX_SEPARATOR).none()
            .after(ObjTypes.FREE_FORM_TYPE_NAME).none()
            .between(ObjTypes.FREE_FORM_DIRECTION, ObjTypes.FLOAT).spaces(1)
            .after(ObjTypes.FREE_FORM_BODY_END_KEYWORD).none()
            .between(ObjTypes.FREE_FORM_POINT_INDEX, ObjTypes.FREE_FORM_POINT_INDEX).spaces(1)
            .after(ObjTypes.SMOOTHING_GROUP_NUMBER).none()
            .between(ObjTypes.MATERIAL_FILE_REFERENCE, ObjTypes.MATERIAL_FILE_REFERENCE).spaces(1)
            .after(ObjTypes.MATERIAL_NAME).none()
}
