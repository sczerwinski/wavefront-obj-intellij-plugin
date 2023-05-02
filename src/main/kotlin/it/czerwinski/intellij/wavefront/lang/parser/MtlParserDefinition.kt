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

package it.czerwinski.intellij.wavefront.lang.parser

import com.intellij.lang.ASTNode
import com.intellij.lang.ParserDefinition
import com.intellij.lang.PsiParser
import com.intellij.lexer.Lexer
import com.intellij.openapi.project.Project
import com.intellij.psi.FileViewProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IFileElementType
import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.lang.MtlLanguage
import it.czerwinski.intellij.wavefront.lang.MtlLexerAdapter
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

class MtlParserDefinition : ParserDefinition {

    override fun createLexer(project: Project?): Lexer = MtlLexerAdapter()

    override fun getCommentTokens(): TokenSet = TokenSet.create(MtlTypes.COMMENT_BLOCK)

    override fun getStringLiteralElements(): TokenSet = TokenSet.EMPTY

    override fun createParser(project: Project?): PsiParser = MtlParser()

    override fun getFileNodeType(): IFileElementType = IFileElementType(MtlLanguage)

    override fun createFile(viewProvider: FileViewProvider): PsiFile = MtlFile(viewProvider)

    override fun spaceExistenceTypeBetweenTokens(left: ASTNode, right: ASTNode): ParserDefinition.SpaceRequirements =
        ParserDefinition.SpaceRequirements.MUST

    override fun createElement(node: ASTNode): PsiElement = MtlTypes.Factory.createElement(node)
}
