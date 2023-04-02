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
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile

class MtlRemoveMaterialQuickFix(
    private val materialName: String,
    materialElement: PsiElement
) : LocalQuickFixOnPsiElement(materialElement), IntentionAction, HighPriorityAction {

    override fun getText(): String =
        WavefrontObjBundle.message(key = "fileTypes.mtl.quickfix.removeMaterial", materialName)

    override fun getFamilyName(): String =
        WavefrontObjBundle.message(key = "fileTypes.mtl.quickfix.removeMaterial.family")

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean =
        file is MtlFile

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        removeMaterial(project, file = file ?: return, element = startElement ?: return)
    }

    private fun removeMaterial(project: Project, file: PsiFile, element: PsiElement) {
        WriteCommandAction.writeCommandAction(project, file)
            .withName(WavefrontObjBundle.message(key = "fileTypes.mtl.quickfix.removeMaterial.action"))
            .run<RuntimeException> {
                val otherNode = element.prevSibling?.node ?: element.nextSibling?.node
                val parentElement = element.parent
                parentElement.node.removeChild(element.node)
                if (otherNode != null) {
                    parentElement.node.removeChild(otherNode)
                }
            }
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        removeMaterial(project, file, startElement)
    }
}
