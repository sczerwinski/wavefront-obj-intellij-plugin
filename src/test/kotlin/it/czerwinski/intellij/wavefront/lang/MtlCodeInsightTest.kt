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

import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.util.containers.ContainerUtil

class MtlCodeInsightTest : CodeInsightFixtureTestCase() {

    override val testDataPath: String = "src/test/testData/mtl"

    fun testAnnotator() {
        myFixture.configureByFiles("AnnotatorTestData.mtl")
        myFixture.checkHighlighting(true, false, true)
    }

    fun testFormatter() {
        myFixture.configureByFile("FormatterTestDataBefore.mtl")
        WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
            CodeStyleManager.getInstance(project)
                .reformatText(file, ContainerUtil.newArrayList(file.textRange))
        }
        myFixture.checkResultByFile("FormatterTestDataExpected.mtl")
    }

    fun testFolding() {
        myFixture.testFolding("$testDataPath/FoldingTestData.mtl")
    }

    fun testCommenter() {
        myFixture.configureByText(MtlFileType, "<caret>newmtl Marble")
        val commentAction = CommentByLineCommentAction()
        commentAction.actionPerformedImpl(project, editor)
        myFixture.checkResult("# newmtl Marble")
        commentAction.actionPerformedImpl(project, editor)
        myFixture.checkResult("newmtl Marble")
    }
}
