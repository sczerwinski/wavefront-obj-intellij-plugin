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
import com.intellij.codeInsight.intention.impl.BaseIntentionAction
import com.intellij.codeInspection.LocalQuickFix
import com.intellij.codeInspection.ProblemDescriptor
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.ObjImportOptimizer
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile

class ObjOptimizeMtlFileReferencesQuickFix : BaseIntentionAction(), LocalQuickFix, HighPriorityAction {

    override fun getText(): String =
        WavefrontObjBundle.message(key = "fileTypes.obj.quickfix.optimizeMtlFileReferences")

    override fun getFamilyName(): String =
        WavefrontObjBundle.message(key = "fileTypes.obj.quickfix.optimizeMtlFileReferences.family")

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean =
        file is ObjFile

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        optimizeMtlFileReferences(project, file = file ?: return)
    }

    private fun optimizeMtlFileReferences(project: Project, file: PsiFile) {
        val optimizer = ObjImportOptimizer()
        val runnable = optimizer.processFile(file)

        WriteCommandAction.writeCommandAction(project, file)
            .withName(WavefrontObjBundle.message(key = "fileTypes.obj.quickfix.optimizeMtlFileReferences.action"))
            .run<RuntimeException> { runnable.run() }
    }

    override fun applyFix(project: Project, descriptor: ProblemDescriptor) {
        val file = descriptor.psiElement?.containingFile ?: return
        optimizeMtlFileReferences(project, file)
    }
}
