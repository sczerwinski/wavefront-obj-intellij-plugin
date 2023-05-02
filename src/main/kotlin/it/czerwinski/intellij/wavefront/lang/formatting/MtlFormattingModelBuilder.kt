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
import it.czerwinski.intellij.wavefront.lang.MtlLanguage
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

class MtlFormattingModelBuilder : FormattingModelBuilder {

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
        SpacingBuilder(settings, MtlLanguage)
            .around(comments).none()
            .before(keywords).none()
            .after(keywords).spaces(1)
            .between(keywords, optionNames).spaces(1)
            .between(keywords, options).spaces(1)
            .around(optionNames).spaces(1)
            .around(options).spaces(1)
            .before(params).spaces(1)
            .between(keywords, params).spaces(1)
            .between(optionNames, params).spaces(1)
            .between(options, params).spaces(1)
            .between(params, optionNames).spaces(1)
            .between(params, options).spaces(1)

    companion object {

        private val comments = TokenSet.create(
            MtlTypes.COMMENT,
            MtlTypes.COMMENT_LINE,
            MtlTypes.COMMENT_BLOCK,
        )

        private val keywords = TokenSet.create(
            MtlTypes.NEW_MATERIAL_KEYWORD,
            MtlTypes.AMBIENT_COLOR_KEYWORD,
            MtlTypes.DIFFUSE_COLOR_KEYWORD,
            MtlTypes.SPECULAR_COLOR_KEYWORD,
            MtlTypes.TRANSMISSION_FILTER_KEYWORD,
            MtlTypes.EMISSION_COLOR_KEYWORD,
            MtlTypes.ILLUMINATION_KEYWORD,
            MtlTypes.DISSOLVE_KEYWORD,
            MtlTypes.SPECULAR_EXPONENT_KEYWORD,
            MtlTypes.SHARPNESS_KEYWORD,
            MtlTypes.OPTICAL_DENSITY_KEYWORD,
            MtlTypes.TRANSPARENCY_KEYWORD,
            MtlTypes.ROUGHNESS_KEYWORD,
            MtlTypes.METALNESS_KEYWORD,
            MtlTypes.AMBIENT_COLOR_MAP_KEYWORD,
            MtlTypes.DIFFUSE_COLOR_MAP_KEYWORD,
            MtlTypes.SPECULAR_COLOR_MAP_KEYWORD,
            MtlTypes.EMISSION_COLOR_MAP_KEYWORD,
            MtlTypes.SPECULAR_EXPONENT_MAP_KEYWORD,
            MtlTypes.DISSOLVE_MAP_KEYWORD,
            MtlTypes.DISPLACEMENT_MAP_KEYWORD,
            MtlTypes.STENCIL_DECAL_MAP_KEYWORD,
            MtlTypes.ROUGHNESS_MAP_KEYWORD,
            MtlTypes.METALNESS_MAP_KEYWORD,
            MtlTypes.NORMAL_MAP_KEYWORD,
            MtlTypes.BUMP_MAP_KEYWORD,
            MtlTypes.REFLECTION_MAP_KEYWORD
        )

        private val optionNames = TokenSet.create(
            MtlTypes.BLEND_U_OPTION_NAME,
            MtlTypes.BLEND_V_OPTION_NAME,
            MtlTypes.COLOR_CORRECTION_OPTION_NAME,
            MtlTypes.CLAMP_OPTION_NAME,
            MtlTypes.SCALAR_CHANNEL_OPTION_NAME,
            MtlTypes.VALUE_MODIFIER_OPTION_NAME,
            MtlTypes.OFFSET_OPTION_NAME,
            MtlTypes.SCALE_OPTION_NAME,
            MtlTypes.TURBULENCE_OPTION_NAME,
            MtlTypes.RESOLUTION_OPTION_NAME,
            MtlTypes.BUMP_MULTIPLIER_OPTION_NAME,
            MtlTypes.REFLECTION_TYPE_OPTION_NAME
        )

        private val options = TokenSet.create(
            MtlTypes.BLEND_U_OPTION,
            MtlTypes.BLEND_V_OPTION,
            MtlTypes.COLOR_CORRECTION_OPTION,
            MtlTypes.CLAMP_OPTION,
            MtlTypes.SCALAR_CHANNEL_OPTION,
            MtlTypes.VALUE_MODIFIER_OPTION,
            MtlTypes.OFFSET_OPTION,
            MtlTypes.SCALE_OPTION,
            MtlTypes.TURBULENCE_OPTION,
            MtlTypes.RESOLUTION_OPTION,
            MtlTypes.BUMP_MULTIPLIER_OPTION,
            MtlTypes.REFLECTION_TYPE_OPTION
        )

        private val params = TokenSet.create(
            MtlTypes.MATERIAL_NAME,
            MtlTypes.FLOAT,
            MtlTypes.FLAG,
            MtlTypes.ILLUMINATION_VALUE,
            MtlTypes.SCALAR_CHANNEL,
            MtlTypes.INTEGER,
            MtlTypes.TEXTURE_FILE
        )
    }
}
