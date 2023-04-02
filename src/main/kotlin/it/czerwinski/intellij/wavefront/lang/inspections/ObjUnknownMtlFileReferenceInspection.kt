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
import it.czerwinski.intellij.wavefront.lang.quickfix.ObjCreateMtlFileQuickFix

class ObjUnknownMtlFileReferenceInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        MyPsiElementVisitor(holder)

    private class MyPsiElementVisitor(
        private val holder: ProblemsHolder
    ) : PsiElementVisitor() {

        override fun visitElement(element: PsiElement) {
            if (element is ObjMaterialFileReference) {
                visitMaterialFileReference(element)
            }
        }

        private fun visitMaterialFileReference(element: ObjMaterialFileReference) {
            val filename = element.filename
            if (!filename.isNullOrBlank() && element.mtlFile == null) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.unknownMtlReference.description",
                        filename
                    ),
                    ProblemHighlightType.LIKE_UNKNOWN_SYMBOL,
                    ObjCreateMtlFileQuickFix(element, filename)
                )
            }
        }
    }
}
