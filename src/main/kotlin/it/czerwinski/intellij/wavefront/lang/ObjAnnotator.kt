/*
 * Copyright 2020 Slawomir Czerwinski
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
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex

class ObjAnnotator : Annotator {

    override fun annotate(
        element: PsiElement,
        holder: AnnotationHolder
    ) {
        when (element) {
            is ObjVertexIndex -> annotateVertexIndex(element, holder)
            is ObjTextureCoordinatesIndex -> annotateTextureCoordinatesIndex(element, holder)
            is ObjVertexNormalIndex -> annotateVertexNormalIndex(element, holder)
            is ObjMaterialFileReference -> annotateMaterialFileReference(element, holder)
            is ObjMaterialReference -> annotateMaterialReference(element, holder)
        }
    }

    private fun annotateVertexIndex(
        element: ObjVertexIndex,
        holder: AnnotationHolder
    ) {
        val index = parseIndex(element)
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else {
            if (!checkVertexExists(element.containingFile, index)) {
                holder.createErrorAnnotation(
                    element,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.vertexNotFound",
                        index
                    )
                )
            }
        }
    }

    private fun annotateTextureCoordinatesIndex(
        element: ObjTextureCoordinatesIndex,
        holder: AnnotationHolder
    ) {
        val index = parseIndex(element)
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else {
            if (!checkTextureCoordinatesExist(element.containingFile, index)) {
                holder.createErrorAnnotation(
                    element,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.textureCoordinatesNotFound",
                        index
                    )
                )
            }
        }
    }

    private fun annotateVertexNormalIndex(
        element: ObjVertexNormalIndex,
        holder: AnnotationHolder
    ) {
        val index = parseIndex(element)
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else {
            if (!checkVertexNormalExists(element.containingFile, index)) {
                holder.createErrorAnnotation(
                    element,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.vertexNormalNotFound",
                        index
                    )
                )
            }
        }
    }

    private fun parseIndex(element: PsiElement): Int? =
        element.text.toIntOrNull()

    private fun AnnotationHolder.createInvalidIndexAnnotation(element: PsiElement) {
        createErrorAnnotation(
            element,
            WavefrontObjBundle.message("fileTypes.obj.annotation.error.invalidIndex")
        )
    }

    private fun annotateMaterialFileReference(
        element: ObjMaterialFileReference,
        holder: AnnotationHolder
    ) {
        val materialFilenameNode = element.node.findChildByType(ObjTypes.REFERENCE)
        if (materialFilenameNode != null) {
            if (!materialFilenameNode.text.endsWith(suffix = ".mtl")) {
                holder.createWarningAnnotation(
                    materialFilenameNode,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.warning.mtlFileExtension"
                    )
                )
            }
        }
    }

    private fun annotateMaterialReference(
        element: ObjMaterialReference,
        holder: AnnotationHolder
    ) {
        val materialNameNode = element.node.findChildByType(ObjTypes.REFERENCE)
        if (materialNameNode != null) {
            holder.createWeakWarningAnnotation(
                materialNameNode,
                WavefrontObjBundle.message(
                    "fileTypes.obj.annotation.warning.cannotValidateMaterial"
                )
            )
        }
    }
}
