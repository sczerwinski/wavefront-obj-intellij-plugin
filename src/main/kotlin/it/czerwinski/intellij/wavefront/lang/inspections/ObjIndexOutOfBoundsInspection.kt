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
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormCurveIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormPointIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex

class ObjIndexOutOfBoundsInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        MyPsiElementVisitor(holder)

    private class MyPsiElementVisitor(
        private val holder: ProblemsHolder
    ) : PsiElementVisitor() {

        override fun visitElement(element: PsiElement) {
            when (element) {
                is ObjVertexIndex -> visitVertexIndex(element)
                is ObjTextureCoordinatesIndex -> visitTextureCoordinatesIndex(element)
                is ObjVertexNormalIndex -> visitVertexNormalIndex(element)
                is ObjFreeFormPointIndex -> visitFreeFormPointIndex(element)
                is ObjFreeFormCurveIndex -> visitFreeForm2DCurveIndex(element)
            }
        }

        private fun visitVertexIndex(element: ObjVertexIndex) {
            val index = element.value
            if (index != null && index != 0 && !element.isValidIndex()) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.indexOutOfBounds.description.vertex",
                        index
                    )
                )
            }
        }

        private fun visitTextureCoordinatesIndex(element: ObjTextureCoordinatesIndex) {
            val index = element.value
            if (index != null && index != 0 && !element.isValidIndex()) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.indexOutOfBounds.description.textureCoordinates",
                        index
                    )
                )
            }
        }

        private fun visitVertexNormalIndex(element: ObjVertexNormalIndex) {
            val index = element.value
            if (index != null && index != 0 && !element.isValidIndex()) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.indexOutOfBounds.description.normal",
                        index
                    )
                )
            }
        }

        private fun visitFreeFormPointIndex(element: ObjFreeFormPointIndex) {
            val index = element.value
            if (index != null && index != 0 && !element.isValidIndex()) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.indexOutOfBounds.description.freeFormPoint",
                        index
                    )
                )
            }
        }

        private fun visitFreeForm2DCurveIndex(element: ObjFreeFormCurveIndex) {
            val index = element.value
            if (index != null && index != 0 && !element.isValidIndex()) {
                holder.registerProblem(
                    element,
                    WavefrontObjBundle.message(
                        key = "fileTypes.obj.inspection.indexOutOfBounds.description.freeForm2DCurve",
                        index
                    )
                )
            }
        }
    }
}
