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
import it.czerwinski.intellij.wavefront.lang.inspections.ObjDuplicatedMtlFileReferenceInspection
import it.czerwinski.intellij.wavefront.lang.inspections.ObjIndexOutOfBoundsInspection
import it.czerwinski.intellij.wavefront.lang.inspections.ObjInvalidIndexInspection
import it.czerwinski.intellij.wavefront.lang.inspections.ObjUnknownMaterialReferenceInspection
import it.czerwinski.intellij.wavefront.lang.inspections.ObjUnknownMtlFileReferenceInspection
import it.czerwinski.intellij.wavefront.lang.inspections.ObjUnusedMtlFileReferenceInspection

class ObjCodeInsightTest : BasePlatformTestCase() {

    override fun getBasePath(): String = "src/test/testData/obj"

    override fun setUp() {
        super.setUp()
        myFixture.testDataPath = basePath
    }

    fun testInspections() {
        myFixture.enableInspections(
            ObjDuplicatedMtlFileReferenceInspection::class.java,
            ObjIndexOutOfBoundsInspection::class.java,
            ObjInvalidIndexInspection::class.java,
            ObjUnknownMaterialReferenceInspection::class.java,
            ObjUnknownMtlFileReferenceInspection::class.java,
            ObjUnusedMtlFileReferenceInspection::class.java
        )
        myFixture.configureByFiles("InspectionsTestData.obj")
        myFixture.checkHighlighting(true, false, true)
    }

    fun testFormatter() {
        myFixture.configureByFile("FormatterTestDataBefore.obj")
        WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
            CodeStyleManager.getInstance(project)
                .reformatText(myFixture.file, ContainerUtil.newArrayList(myFixture.file.textRange))
        }
        myFixture.checkResultByFile("FormatterTestDataExpected.obj")
    }

    fun testFoldingObject() {
        myFixture.testFolding("$basePath/FoldingObjectTestData.obj")
    }

    fun testFoldingComment() {
        myFixture.testFolding("$basePath/FoldingCommentTestData.obj")
    }

    fun testCommenter() {
        myFixture.configureByText(ObjFileType, "<caret>f 1/2/3 4/5/6 7/8/9")
        val commentAction = CommentByLineCommentAction()
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult("# f 1/2/3 4/5/6 7/8/9")
        commentAction.actionPerformedImpl(project, myFixture.editor)
        myFixture.checkResult("f 1/2/3 4/5/6 7/8/9")
    }
}
