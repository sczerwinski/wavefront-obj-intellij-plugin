/*
 * Copyright 2020-2022 Slawomir Czerwinski
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
            .configureKeywordsSpacing()
            .configureOptionsSpacing()
            .configureValuesSpacing()

    private fun SpacingBuilder.configureKeywordsSpacing(): SpacingBuilder = this
        .before(MtlTypes.NEW_MATERIAL_KEYWORD).none()
        .before(MtlTypes.AMBIENT_COLOR_KEYWORD).none()
        .before(MtlTypes.DIFFUSE_COLOR_KEYWORD).none()
        .before(MtlTypes.SPECULAR_COLOR_KEYWORD).none()
        .before(MtlTypes.TRANSMISSION_FILTER_KEYWORD).none()
        .before(MtlTypes.EMISSION_COLOR_KEYWORD).none()
        .before(MtlTypes.ILLUMINATION_KEYWORD).none()
        .before(MtlTypes.DISSOLVE_KEYWORD).none()
        .before(MtlTypes.SPECULAR_EXPONENT_KEYWORD).none()
        .before(MtlTypes.SHARPNESS_KEYWORD).none()
        .before(MtlTypes.OPTICAL_DENSITY_KEYWORD).none()
        .before(MtlTypes.ROUGHNESS_KEYWORD).none()
        .before(MtlTypes.METALNESS_KEYWORD).none()
        .before(MtlTypes.AMBIENT_COLOR_MAP_KEYWORD).none()
        .before(MtlTypes.DIFFUSE_COLOR_MAP_KEYWORD).none()
        .before(MtlTypes.SPECULAR_COLOR_MAP_KEYWORD).none()
        .before(MtlTypes.EMISSION_COLOR_MAP_KEYWORD).none()
        .before(MtlTypes.SPECULAR_EXPONENT_MAP_KEYWORD).none()
        .before(MtlTypes.DISSOLVE_MAP_KEYWORD).none()
        .before(MtlTypes.DISPLACEMENT_MAP_KEYWORD).none()
        .before(MtlTypes.STENCIL_DECAL_MAP_KEYWORD).none()
        .before(MtlTypes.ROUGHNESS_MAP_KEYWORD).none()
        .before(MtlTypes.METALNESS_MAP_KEYWORD).none()
        .before(MtlTypes.NORMAL_MAP_KEYWORD).none()
        .before(MtlTypes.BUMP_MAP_KEYWORD).none()
        .before(MtlTypes.REFLECTION_MAP_KEYWORD).none()
        .after(MtlTypes.NEW_MATERIAL_KEYWORD).spaces(1)
        .after(MtlTypes.AMBIENT_COLOR_KEYWORD).spaces(1)
        .after(MtlTypes.DIFFUSE_COLOR_KEYWORD).spaces(1)
        .after(MtlTypes.SPECULAR_COLOR_KEYWORD).spaces(1)
        .after(MtlTypes.TRANSMISSION_FILTER_KEYWORD).spaces(1)
        .after(MtlTypes.EMISSION_COLOR_KEYWORD).spaces(1)
        .after(MtlTypes.ILLUMINATION_KEYWORD).spaces(1)
        .after(MtlTypes.DISSOLVE_KEYWORD).spaces(1)
        .after(MtlTypes.SPECULAR_EXPONENT_KEYWORD).spaces(1)
        .after(MtlTypes.SHARPNESS_KEYWORD).spaces(1)
        .after(MtlTypes.OPTICAL_DENSITY_KEYWORD).spaces(1)
        .after(MtlTypes.ROUGHNESS_KEYWORD).spaces(1)
        .after(MtlTypes.METALNESS_KEYWORD).spaces(1)
        .after(MtlTypes.AMBIENT_COLOR_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.DIFFUSE_COLOR_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.SPECULAR_COLOR_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.EMISSION_COLOR_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.SPECULAR_EXPONENT_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.DISSOLVE_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.DISPLACEMENT_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.STENCIL_DECAL_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.ROUGHNESS_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.METALNESS_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.NORMAL_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.BUMP_MAP_KEYWORD).spaces(1)
        .after(MtlTypes.REFLECTION_MAP_KEYWORD).spaces(1)

    private fun SpacingBuilder.configureOptionsSpacing(): SpacingBuilder = this
        .around(MtlTypes.BLEND_U_OPTION_NAME).spaces(1)
        .around(MtlTypes.BLEND_V_OPTION_NAME).spaces(1)
        .around(MtlTypes.COLOR_CORRECTION_OPTION_NAME).spaces(1)
        .around(MtlTypes.CLAMP_OPTION_NAME).spaces(1)
        .around(MtlTypes.SCALAR_CHANNEL_OPTION_NAME).spaces(1)
        .around(MtlTypes.VALUE_MODIFIER_OPTION_NAME).spaces(1)
        .around(MtlTypes.OFFSET_OPTION_NAME).spaces(1)
        .around(MtlTypes.SCALE_OPTION_NAME).spaces(1)
        .around(MtlTypes.TURBULENCE_OPTION_NAME).spaces(1)
        .around(MtlTypes.RESOLUTION_OPTION_NAME).spaces(1)
        .around(MtlTypes.BUMP_MULTIPLIER_OPTION_NAME).spaces(1)
        .around(MtlTypes.REFLECTION_TYPE_OPTION_NAME).spaces(1)
        .around(MtlTypes.BLEND_U_OPTION).spaces(1)
        .around(MtlTypes.BLEND_V_OPTION).spaces(1)
        .around(MtlTypes.COLOR_CORRECTION_OPTION).spaces(1)
        .around(MtlTypes.CLAMP_OPTION).spaces(1)
        .around(MtlTypes.SCALAR_CHANNEL_OPTION).spaces(1)
        .around(MtlTypes.VALUE_MODIFIER_OPTION).spaces(1)
        .around(MtlTypes.OFFSET_OPTION).spaces(1)
        .around(MtlTypes.SCALE_OPTION).spaces(1)
        .around(MtlTypes.TURBULENCE_OPTION).spaces(1)
        .around(MtlTypes.RESOLUTION_OPTION).spaces(1)
        .around(MtlTypes.BUMP_MULTIPLIER_OPTION).spaces(1)
        .around(MtlTypes.REFLECTION_TYPE_OPTION).spaces(1)

    private fun SpacingBuilder.configureValuesSpacing(): SpacingBuilder = this
        .before(MtlTypes.MATERIAL_NAME).spaces(1)
        .after(MtlTypes.MATERIAL_NAME).none()
        .before(MtlTypes.FLOAT).spaces(1)
        .before(MtlTypes.FLAG).spaces(1)
        .before(MtlTypes.ILLUMINATION_VALUE).spaces(1)
        .before(MtlTypes.SCALAR_CHANNEL).spaces(1)
        .before(MtlTypes.INTEGER).spaces(1)
        .before(MtlTypes.TEXTURE_FILE).spaces(1)
        .after(MtlTypes.TEXTURE_FILE).none()
}
