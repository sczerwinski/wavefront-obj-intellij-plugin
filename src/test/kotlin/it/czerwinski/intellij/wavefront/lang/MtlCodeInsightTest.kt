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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.codeInsight.generation.actions.CommentByLineCommentAction
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.codeStyle.CodeStyleManager
import com.intellij.testFramework.fixtures.BasePlatformTestCase
import com.intellij.util.containers.ContainerUtil
import it.czerwinski.intellij.wavefront.lang.inspections.MtlUnknownTextureFileInspection
import it.czerwinski.intellij.wavefront.lang.inspections.MtlUnusedMaterialInspection

class MtlCodeInsightTest : BasePlatformTestCase() {

    override fun getBasePath(): String = "src/test/testData/mtl"

    override fun setUp() {
        super.setUp()
        myFixture.testDataPath = basePath
    }

    fun testInspections() {
        myFixture.enableInspections(
            MtlUnusedMaterialInspection::class.java,
            MtlUnknownTextureFileInspection::class.java
        )
        myFixture.configureByFiles("InspectionsTestData.mtl")
        myFixture.checkHighlighting(true, false, true)
    }

    fun testFormatter() {
        myFixture.configureByFile("FormatterTestDataBefore.mtl")
        WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
            CodeStyleManager.getInstance(project)
                .reformatText(myFixture.file, ContainerUtil.newArrayList(myFixture.file.textRange))
        }
        myFixture.checkResultByFile("FormatterTestDataExpected.mtl")
    }

    fun testFoldingMaterial() {
        myFixture.testFolding("$basePath/FoldingMaterialTestData.mtl")
    }

    fun testFoldingComment() {
        myFixture.testFolding("$basePath/FoldingCommentTestData.mtl")
    }

    fun testCommenter() {
        myFixture.configureByText(MtlFileType, "<caret>newmtl Marble")
        val commentAction = CommentByLineCommentAction()
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult("# newmtl Marble")
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult("newmtl Marble")
    }
}
