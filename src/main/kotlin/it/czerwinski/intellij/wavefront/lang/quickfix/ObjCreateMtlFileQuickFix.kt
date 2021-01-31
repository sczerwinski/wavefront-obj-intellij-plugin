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

package it.czerwinski.intellij.wavefront.lang.quickfix

import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle

class ObjCreateMtlFileQuickFix(
    private val dir: PsiDirectory,
    private val filename: String
) : BaseIntentionAction() {

    override fun getText(): String =
        WavefrontObjBundle.getMessage("fileTypes.mtl.quickfix.createMtlFile", filename)

    override fun getFamilyName(): String =
        WavefrontObjBundle.getMessage("fileTypes.mtl.quickfix.createMtlFile.family")

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        ApplicationManager.getApplication().invokeLater {
            runCreateMtlFileAction(project)
        }
    }

    private fun runCreateMtlFileAction(project: Project) {
        WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
            createMtlFile()
        }
    }

    private fun createMtlFile() {
        val file = dir.createFile(filename)
        file.navigate(true)
    }
}
