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

import com.intellij.lang.ImportOptimizer
import com.intellij.openapi.util.EmptyRunnable
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.util.childrenOfType
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReferenceStatement

class ObjImportOptimizer : ImportOptimizer {

    override fun supports(file: PsiFile): Boolean = file is ObjFile

    override fun processFile(file: PsiFile): Runnable {
        val objFile = file as? ObjFile ?: return EmptyRunnable.getInstance()

        return Runnable {
            val forRemoval = collectMaterialFileReferencesForRemoval(objFile)
            for (statement in objFile.childrenOfType<ObjMaterialFileReferenceStatement>()) {
                if (statement.materialFileReferenceList.all { it in forRemoval }) {
                    removeElement(element = statement)
                } else {
                    for (reference in statement.materialFileReferenceList) {
                        if (reference in forRemoval) {
                            removeElement(element = reference, parent = statement)
                        }
                    }
                }
            }
        }
    }

    private fun collectMaterialFileReferencesForRemoval(objFile: ObjFile): List<ObjMaterialFileReference> {
        val allReferences = objFile.materialFileReferences
        val validReferences = allReferences
            .distinctBy { reference -> reference.mtlFile ?: reference.filename }
            .filterNot { reference -> reference.isUnused }
            .toSet()
        return (allReferences - validReferences)
    }

    private fun removeElement(element: PsiElement, parent: PsiElement = element.parent) {
        val otherNode = element.prevSibling?.node ?: element.nextSibling?.node
        parent.node.removeChild(element.node)
        if (otherNode != null) {
            parent.node.removeChild(otherNode)
        }
    }
}
