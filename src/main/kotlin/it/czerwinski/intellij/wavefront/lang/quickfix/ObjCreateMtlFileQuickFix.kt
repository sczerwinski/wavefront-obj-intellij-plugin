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

package it.czerwinski.intellij.wavefront.lang.quickfix

import com.intellij.codeInsight.intention.HighPriorityAction
import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.LocalQuickFixOnPsiElement
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDirectory
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.util.createRelativeFile

class ObjCreateMtlFileQuickFix(
    element: PsiElement,
    private val filename: String
) : LocalQuickFixOnPsiElement(element), IntentionAction, HighPriorityAction {

    override fun getText(): String =
        WavefrontObjBundle.getMessage("fileTypes.mtl.quickfix.createMtlFile", filename)

    override fun getFamilyName(): String =
        WavefrontObjBundle.getMessage("fileTypes.mtl.quickfix.createMtlFile.family")

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        runCreateMtlFileAction(project)
    }

    private fun runCreateMtlFileAction(project: Project) {
        val dir = startElement.containingFile?.containingDirectory
        if (dir != null) {
            WriteCommandAction.writeCommandAction(project).run<RuntimeException> {
                createMtlFile(dir)
            }
        }
    }

    private fun createMtlFile(dir: PsiDirectory) {
        val file = createRelativeFile(dir, filename)
        file?.navigate(true)
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        runCreateMtlFileAction(project)
    }
}
