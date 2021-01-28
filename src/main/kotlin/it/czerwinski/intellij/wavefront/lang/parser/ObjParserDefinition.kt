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

package it.czerwinski.intellij.wavefront.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.lang.ObjLanguage
import it.czerwinski.intellij.wavefront.lang.ObjLexerAdapter
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

class ObjParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = ObjLexerAdapter()

    override fun getWhitespaceTokens(): TokenSet = TokenSet.create(TokenType.WHITE_SPACE)

    override fun getCommentTokens(): TokenSet = TokenSet.create(ObjTypes.COMMENT)

    override fun getStringLiteralElements(): TokenSet = TokenSet.create(ObjTypes.STRING)

    override fun createParser(project: Project?): PsiParser = ObjParser()

    override fun getFileNodeType(): IFileElementType = IFileElementType(ObjLanguage)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = ObjFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
        if (ObjTypes.VERTEX_INDEX_SEPARATOR in listOf(left.elementType, right.elementType)) {
            ParserDefinition.SpaceRequirements.MUST_NOT
        } else {
            ParserDefinition.SpaceRequirements.MUST
        }

    override fun createElement(node: ASTNode): PsiElement = ObjTypes.Factory.createElement(node)
}
