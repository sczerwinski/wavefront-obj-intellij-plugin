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

package it.czerwinski.intellij.wavefront.lang.inspections

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReferenceStatement
import it.czerwinski.intellij.wavefront.lang.quickfix.ObjOptimizeMtlFileReferencesQuickFix

class ObjUnusedMtlFileReferenceInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        MyPsiElementVisitor(holder)

    private class MyPsiElementVisitor(
        private val holder: ProblemsHolder
    ) : PsiElementVisitor() {

        override fun visitElement(element: PsiElement) {
            when (element) {
                is ObjMaterialFileReferenceStatement -> visitMaterialFileReferenceStatement(element)
                is ObjMaterialFileReference -> visitMaterialFileReference(element)
            }
        }

        private fun visitMaterialFileReferenceStatement(element: ObjMaterialFileReferenceStatement) {
            if (element.materialFileReferenceList.all { reference -> reference.isUnused }) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.unusedMtlReference.description.statement"
                    ),
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    ObjOptimizeMtlFileReferencesQuickFix()
                )
            }
        }

        private fun visitMaterialFileReference(element: ObjMaterialFileReference) {
            if (element.mtlFile != null && element.isUnused) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(key = "fileTypes.obj.inspection.unusedMtlReference.description"),
                    ProblemHighlightType.LIKE_UNUSED_SYMBOL,
                    ObjOptimizeMtlFileReferencesQuickFix()
                )
            }
        }
    }
}
