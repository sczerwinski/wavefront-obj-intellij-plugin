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

import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.editor.colors.TextAttributesKey.createTextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

class ObjSyntaxHighlighter : SyntaxHighlighterBase() {

    override fun getHighlightingLexer(): Lexer = ObjLexerAdapter()

    override fun getTokenHighlights(tokenType: IElementType?): Array<TextAttributesKey> =
        when (tokenType) {
            ObjTypes.COMMENT ->
                arrayOf(ATTR_COMMENT)

            ObjTypes.OBJECT_KEYWORD,
            ObjTypes.GROUP_KEYWORD,
            ObjTypes.VERTEX_KEYWORD,
            ObjTypes.TEXTURE_COORDINATES_KEYWORD,
            ObjTypes.VERTEX_NORMAL_KEYWORD,
            ObjTypes.FACE_KEYWORD,
            ObjTypes.LINE_KEYWORD,
            ObjTypes.POINT_KEYWORD,
            ObjTypes.SMOOTH_SHADING_KEYWORD,
            ObjTypes.MATERIAL_FILE_REF_KEYWORD,
            ObjTypes.MATERIAL_REFERENCE_KEYWORD ->
                arrayOf(ATTR_KEYWORD)

            ObjTypes.STRING ->
                arrayOf(ATTR_STRING)

            ObjTypes.FLOAT,
            ObjTypes.INDEX,
            ObjTypes.FLAG ->
                arrayOf(ATTR_NUMBER)

            ObjTypes.VERTEX_INDEX_SEPARATOR ->
                arrayOf(ATTR_OPERATOR)

            ObjTypes.REFERENCE ->
                arrayOf(ATTR_REFERENCE)

            TokenType.BAD_CHARACTER ->
                arrayOf(ATTR_BAD_CHARACTER)

            else ->
                emptyArray()
        }

    companion object {
        internal val ATTR_COMMENT: TextAttributesKey =
            createTextAttributesKey("OBJ_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        internal val ATTR_OPERATOR: TextAttributesKey =
            createTextAttributesKey("OBJ_OPERATOR", DefaultLanguageHighlighterColors.OPERATION_SIGN)
        internal val ATTR_KEYWORD: TextAttributesKey =
            createTextAttributesKey("OBJ_KEYWORD", DefaultLanguageHighlighterColors.KEYWORD)
        internal val ATTR_STRING: TextAttributesKey =
            createTextAttributesKey("OBJ_STRING", DefaultLanguageHighlighterColors.STRING)
        internal val ATTR_NUMBER: TextAttributesKey =
            createTextAttributesKey("OBJ_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        internal val ATTR_REFERENCE: TextAttributesKey =
            createTextAttributesKey("OBJ_REFERENCE", DefaultLanguageHighlighterColors.IDENTIFIER)
        internal val ATTR_BAD_CHARACTER: TextAttributesKey =
            createTextAttributesKey("OBJ_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
    }
}
