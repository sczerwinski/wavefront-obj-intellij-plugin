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
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.project.Project
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.util.findRelativePath

class ObjCreateMaterialQuickFix(
    objFile: PsiFile,
    mtlFile: MtlFile,
    private val name: String
) : LocalQuickFixOnPsiElement(mtlFile), IntentionAction, HighPriorityAction {

    private val path = findRelativePath(objFile, mtlFile) ?: mtlFile.virtualFile.path

    override fun getText(): String = WavefrontObjBundle.getMessage(
        "fileTypes.mtl.quickfix.createMaterial",
        name,
        path
    )

    override fun getFamilyName(): String =
        WavefrontObjBundle.getMessage("fileTypes.mtl.quickfix.createMaterial.family")

    override fun isAvailable(project: Project, editor: Editor?, file: PsiFile?): Boolean = true

    override fun startInWriteAction(): Boolean = false

    override fun invoke(project: Project, editor: Editor?, file: PsiFile?) {
        runCreateMaterialAction(project)
    }

    private fun runCreateMaterialAction(project: Project) {
        WriteCommandAction.writeCommandAction(project, startElement.containingFile).run<RuntimeException> {
            createMaterial(project)
        }
    }

    private fun createMaterial(project: Project) {
        val material = startElement?.node?.run {
            if (lastChildNode != null) {
                addChild(MtlElementFactory.createCRLF(project).node)
            }

            val material = MtlElementFactory.createMaterial(project, name)
            addChild(material.node)

            addChild(MtlElementFactory.createCRLF(project).node)
            addChild(MtlElementFactory.createTODO(project).node)
            addChild(MtlElementFactory.createCRLF(project).node)
            return@run material
        }

        val navigatableElement = material?.lastChild?.navigationElement as? NavigatablePsiElement
        if (navigatableElement != null) {
            navigatableElement.navigate(true)
            FileEditorManager.getInstance(project)
                .selectedTextEditor
                ?.caretModel
                ?.moveCaretRelatively(0, 2, false, false, false)
        }
    }

    override fun invoke(project: Project, file: PsiFile, startElement: PsiElement, endElement: PsiElement) {
        runCreateMaterialAction(project)
    }
}
