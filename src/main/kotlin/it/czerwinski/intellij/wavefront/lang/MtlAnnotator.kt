/*
 * Copyright 2020-2022 Slawomir Czerwinski
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

import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement

class MtlAnnotator : Annotator {

    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder
    ) {
        when (element) {
            is MtlTextureElement -> annotateTextureFileReference(element, holder)
        }
    }

    private fun annotateTextureFileReference(element: MtlTextureElement, holder: AnnotationHolder) {
        val textureFilenameNode = element.textureFilenameNode
        if (textureFilenameNode != null && element.textureFile == null) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                WavefrontObjBundle.message("fileTypes.mtl.annotation.error.textureFileNotFound")
            ).range(textureFilenameNode).create()
        }
    }
}
