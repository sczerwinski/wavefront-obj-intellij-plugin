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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

class MtlSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = MtlLexerAdapter()

    @Suppress("LongMethod")
    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
        when (tokenType) {
            MtlTypes.COMMENT ->
                arrayOf(ATTR_COMMENT)

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
            MtlTypes.REFLECTION_MAP_KEYWORD ->
                arrayOf(ATTR_KEYWORD)

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
            MtlTypes.REFLECTION_TYPE_OPTION_NAME ->
                arrayOf(ATTR_OPTION_NAME)

            MtlTypes.MATERIAL_NAME ->
                arrayOf(ATTR_DECLARATION)

            MtlTypes.FLOAT,
            MtlTypes.INTEGER ->
                arrayOf(ATTR_NUMBER)

            MtlTypes.FLAG,
            MtlTypes.ILLUMINATION_VALUE,
            MtlTypes.SCALAR_CHANNEL,
            MtlTypes.REFLECTION_TYPE ->
                arrayOf(ATTR_CONSTANT)

            MtlTypes.TEXTURE_FILE ->
                arrayOf(ATTR_REFERENCE)

            TokenType.BAD_CHARACTER ->
                arrayOf(ATTR_BAD_CHARACTER)

            else ->
                emptyArray()
        }

    companion object {
        internal val ATTR_COMMENT: TextAttributesKey =
            createTextAttributesKey("MTL_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        internal val ATTR_OPTION_NAME: TextAttributesKey =
            createTextAttributesKey("MTL_OPTION_NAME", DefaultLanguageHighlighterColors.METADATA)
        internal val ATTR_KEYWORD: TextAttributesKey =
            createTextAttributesKey("MTL_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        internal val ATTR_DECLARATION: TextAttributesKey =
            createTextAttributesKey("MTL_DECLARATION", DefaultLanguageHighlighterColors.FUNCTION_DECLARATION)
        internal val ATTR_NUMBER: TextAttributesKey =
            createTextAttributesKey("MTL_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        internal val ATTR_CONSTANT: TextAttributesKey =
            createTextAttributesKey("MTL_CONSTANT", DefaultLanguageHighlighterColors.CONSTANT)
        internal val ATTR_REFERENCE: TextAttributesKey =
            createTextAttributesKey("MTL_REFERENCE", DefaultLanguageHighlighterColors.IDENTIFIER)
        internal val ATTR_BAD_CHARACTER: TextAttributesKey =
            createTextAttributesKey("MTL_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}
