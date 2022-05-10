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

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.module.Module
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.testFramework.LightProjectDescriptor
import com.intellij.testFramework.PlatformTestUtil
import com.intellij.testFramework.UsefulTestCase
import com.intellij.testFramework.fixtures.CodeInsightTestFixture
import com.intellij.testFramework.fixtures.IdeaTestExecutionPolicy
import com.intellij.testFramework.fixtures.IdeaTestFixtureFactory
import com.intellij.testFramework.fixtures.TempDirTestFixture
import com.intellij.testFramework.fixtures.impl.LightTempDirTestFixtureImpl
import java.io.File

abstract class CodeInsightFixtureTestCase : UsefulTestCase() {

    private var _myFixture: CodeInsightTestFixture? = null

    protected val myFixture: CodeInsightTestFixture
        get() = requireNotNull(_myFixture)

    protected open val tempDirFixture: TempDirTestFixture
        get() {
            val policy = IdeaTestExecutionPolicy.current()
            return if (policy != null) policy.createTempDirTestFixture()
            else LightTempDirTestFixtureImpl(true)
        }

    protected open val basePath: String get() = ""

    protected open val testDataPath: String
        get() {
            val communityPath = PlatformTestUtil.getCommunityPath()
                .replace(File.separatorChar, '/')
            val path = communityPath + basePath
            return if (File(path).exists()) path else "$communityPath/../$basePath"
        }

    protected open val project: Project get() = myFixture.project

    protected open val file: PsiFile get() = myFixture.file

    protected open val editor: Editor get() = myFixture.editor

    protected open val module: Module get() = myFixture.module

    protected open val psiManager: PsiManager
        get() = project.let { PsiManager.getInstance(it) }

    override fun setUp() {
        super.setUp()

        val factory = IdeaTestFixtureFactory.getFixtureFactory()
        val fixture = factory.createLightFixtureBuilder(projectDescriptor).fixture
        _myFixture = factory.createCodeInsightFixture(fixture, tempDirFixture)

        myFixture.testDataPath = testDataPath
        myFixture.setUp()
    }

    override fun tearDown() {
        try {
            myFixture.tearDown()
        } catch (e: Throwable) {
            addSuppressedException(e)
        } finally {
            _myFixture = null
            super.tearDown()
        }
    }

    class ObjProjectDescriptor : LightProjectDescriptor()

    companion object {
        private val projectDescriptor = ObjProjectDescriptor()
    }
}
