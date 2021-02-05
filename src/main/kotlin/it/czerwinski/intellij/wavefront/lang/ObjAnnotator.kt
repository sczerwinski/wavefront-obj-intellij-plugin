/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.lang.psi.util.findMaterialIdentifiers
import it.czerwinski.intellij.wavefront.lang.psi.util.findMtlFile
import it.czerwinski.intellij.wavefront.lang.psi.util.findReferencedMtlFiles
import it.czerwinski.intellij.wavefront.lang.quickfix.ObjCreateMaterialQuickFix
import it.czerwinski.intellij.wavefront.lang.quickfix.ObjCreateMtlFileQuickFix

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
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.vertexNotFound",
                        index
                    )
                ).range(element).create()
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
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.textureCoordinatesNotFound",
                        index
                    )
                ).range(element).create()
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
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    WavefrontObjBundle.message(
                        "fileTypes.obj.annotation.error.vertexNormalNotFound",
                        index
                    )
                ).range(element).create()
            }
        }
    }

    private fun parseIndex(element: PsiElement): Int? =
        element.text.toIntOrNull()

    private fun AnnotationHolder.createInvalidIndexAnnotation(element: PsiElement) {
        newAnnotation(
            HighlightSeverity.ERROR,
            WavefrontObjBundle.message("fileTypes.obj.annotation.error.invalidIndex")
        ).range(element).create()
    }

    private fun annotateMaterialFileReference(
        element: ObjMaterialFileReference,
        holder: AnnotationHolder
    ) {
        val materialFilenameNode = element.node.findChildByType(ObjTypes.MATERIAL_FILE_NAME)
        if (materialFilenameNode != null && findMtlFile(element) == null) {
            val materialFilename = materialFilenameNode.text
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                WavefrontObjBundle.message("fileTypes.obj.annotation.warning.mtlFileNotFound")
            ).range(materialFilenameNode)
                .withFix(ObjCreateMtlFileQuickFix(element.containingFile.containingDirectory, materialFilename))
                .create()
        }
    }

    private fun annotateMaterialReference(
        element: ObjMaterialReference,
        holder: AnnotationHolder
    ) {
        val materialNameNode = element.node.findChildByType(ObjTypes.MATERIAL_NAME)
        if (materialNameNode != null) {
            val materialName = element.materialName
            val materialFiles = findReferencedMtlFiles(element.containingFile as ObjFile)
            val materials = materialFiles.flatMap { file -> file.findMaterialIdentifiers() }
            if (!materialName.isNullOrBlank() && materialName !in materials.mapNotNull { it.name }) {
                holder.newAnnotation(
                    HighlightSeverity.WARNING,
                    WavefrontObjBundle.message("fileTypes.obj.annotation.warning.materialNotFound")
                ).range(materialNameNode)
                    .withFixes(
                        materialFiles.map { file ->
                            ObjCreateMaterialQuickFix(element.containingFile, file, materialName)
                        }
                    )
                    .create()
            }
        }
    }

    private fun AnnotationBuilder.withFixes(fixes: Iterable<IntentionAction>): AnnotationBuilder {
        for (fix in fixes) {
            withFix(fix)
        }
        return this
    }
}
