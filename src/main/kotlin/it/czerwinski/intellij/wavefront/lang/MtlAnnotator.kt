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

import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifierElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.util.findAllObjFiles

class MtlAnnotator : Annotator {

    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder
    ) {
        when (element) {
            is MtlMaterialIdentifierElement -> annotateMaterial(element, holder)
            is MtlTextureElement -> annotateTextureFileReference(element, holder)
        }
    }

    private fun annotateMaterial(element: MtlMaterialIdentifierElement, holder: AnnotationHolder) {
        if (element.parent !in element.project.findAllObjFiles().flatMap { it.referencedMaterials }) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                WavefrontObjBundle.message(key = "fileTypes.mtl.annotation.warning.materialUnused")
            ).range(element)
                .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                .create()
        }
    }

    private fun annotateTextureFileReference(element: MtlTextureElement, holder: AnnotationHolder) {
        val textureFilenameNode = element.textureFilenameNode
        if (textureFilenameNode != null && element.textureFile == null) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                WavefrontObjBundle.message("fileTypes.mtl.annotation.error.textureFileNotFound")
            ).range(textureFilenameNode)
                .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                .create()
        }
    }
}
