/*
 * Copyright 2020 Slawomir Czerwinski
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

import com.intellij.formatting.FormattingModel
import com.intellij.formatting.FormattingModelBuilder
import com.intellij.formatting.FormattingModelProvider
import com.intellij.formatting.SpacingBuilder
import com.intellij.psi.PsiElement
import com.intellij.psi.codeStyle.CodeStyleSettings
import it.czerwinski.intellij.wavefront.lang.ObjLanguage
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

class ObjFormattingModelBuilder : FormattingModelBuilder {

    override fun createModel(element: PsiElement, settings: CodeStyleSettings): FormattingModel =
        FormattingModelProvider.createFormattingModelForPsiFile(
            element.containingFile,
            ObjBlock(
                node = element.node,
                spacingBuilder = createSpaceBuilder(settings)
            ),
            settings
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
            .before(ObjTypes.SMOOTH_SHADING_KEYWORD).none()
            .before(ObjTypes.MATERIAL_FILE_REF_KEYWORD).none()
            .before(ObjTypes.MATERIAL_REFERENCE_KEYWORD).none()
            .between(ObjTypes.OBJECT_KEYWORD, ObjTypes.STRING).spaces(1)
            .between(ObjTypes.GROUP_KEYWORD, ObjTypes.STRING).spaces(1)
            .between(ObjTypes.VERTEX_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.TEXTURE_COORDINATES_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.VERTEX_NORMAL_KEYWORD, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FACE_KEYWORD, ObjTypes.FACE_VERTEX).spaces(1)
            .between(ObjTypes.LINE_KEYWORD, ObjTypes.VERTEX_INDEX).spaces(1)
            .between(ObjTypes.POINT_KEYWORD, ObjTypes.VERTEX_INDEX).spaces(1)
            .between(ObjTypes.SMOOTH_SHADING_KEYWORD, ObjTypes.FLAG).spaces(1)
            .between(ObjTypes.MATERIAL_FILE_REF_KEYWORD, ObjTypes.REFERENCE).spaces(1)
            .between(ObjTypes.MATERIAL_REFERENCE_KEYWORD, ObjTypes.REFERENCE).spaces(1)
            .after(ObjTypes.STRING).none()
            .between(ObjTypes.FLOAT, ObjTypes.FLOAT).spaces(1)
            .between(ObjTypes.FACE_VERTEX, ObjTypes.FACE_VERTEX).spaces(1)
            .around(ObjTypes.VERTEX_INDEX_SEPARATOR).none()
            .after(ObjTypes.FLAG).none()
            .after(ObjTypes.REFERENCE).none()
}