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

import com.intellij.codeInsight.intention.IntentionAction
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.lang.annotation.AnnotationBuilder
import com.intellij.lang.annotation.AnnotationHolder
import com.intellij.lang.annotation.Annotator
import com.intellij.lang.annotation.HighlightSeverity
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReferenceStatement
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile
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
            is ObjMaterialFileReferenceStatement -> annotateMaterialFileReferenceStatement(element, holder)
            is ObjMaterialFileReference -> annotateMaterialFileReference(element, holder)
            is ObjMaterialReference -> annotateMaterialReference(element, holder)
        }
    }

    private fun annotateVertexIndex(
        element: ObjVertexIndex,
        holder: AnnotationHolder
    ) {
        val index = element.value
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else if (!element.isValidIndex()) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                WavefrontObjBundle.message(
                    "fileTypes.obj.annotation.error.vertexNotFound",
                    index
                )
            ).range(element).create()
        }
    }

    private fun annotateTextureCoordinatesIndex(
        element: ObjTextureCoordinatesIndex,
        holder: AnnotationHolder
    ) {
        val index = element.value
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else if (!element.isValidIndex()) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                WavefrontObjBundle.message(
                    "fileTypes.obj.annotation.error.textureCoordinatesNotFound",
                    index
                )
            ).range(element).create()
        }
    }

    private fun annotateVertexNormalIndex(
        element: ObjVertexNormalIndex,
        holder: AnnotationHolder
    ) {
        val index = element.value
        if (index == null) {
            holder.createInvalidIndexAnnotation(element)
        } else if (!element.isValidIndex()) {
            holder.newAnnotation(
                HighlightSeverity.ERROR,
                WavefrontObjBundle.message(
                    "fileTypes.obj.annotation.error.vertexNormalNotFound",
                    index
                )
            ).range(element).create()
        }
    }

    private fun AnnotationHolder.createInvalidIndexAnnotation(element: PsiElement) {
        newAnnotation(
            HighlightSeverity.ERROR,
            WavefrontObjBundle.message("fileTypes.obj.annotation.error.invalidIndex")
        ).range(element).create()
    }

    private fun annotateMaterialFileReferenceStatement(
        element: ObjMaterialFileReferenceStatement,
        holder: AnnotationHolder
    ) {
        if (isUnused(element)) {
            holder.newAnnotation(
                HighlightSeverity.WARNING,
                WavefrontObjBundle.message(key = "fileTypes.obj.annotation.warning.mtlFileStatementUnused")
            ).range(element)
                .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                .create()
        }
    }

    private fun isUnused(element: ObjMaterialFileReferenceStatement): Boolean {
        val materialsInReferencedFiles = element.materialFileReferenceList
            .flatMap { materialFileReference -> materialFileReference.mtlFile?.materials.orEmpty() }
        val referencedMaterials = element.containingObjFile?.referencedMaterials.orEmpty()
        return referencedMaterials.none { it in materialsInReferencedFiles }
    }

    private fun annotateMaterialFileReference(
        element: ObjMaterialFileReference,
        holder: AnnotationHolder
    ) {
        if (!element.filename.isNullOrBlank()) {
            val mtlFile = element.mtlFile
            if (mtlFile == null) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    WavefrontObjBundle.message("fileTypes.obj.annotation.error.mtlFileNotFound")
                ).range(element)
                    .applyMaterialFileQuickFix(element)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create()
            } else {
                if (isDuplicated(element)) {
                    holder.newAnnotation(
                        HighlightSeverity.WARNING,
                        WavefrontObjBundle.message(key = "fileTypes.obj.annotation.warning.mtlFileDuplicate")
                    ).range(element)
                        .create()
                }
                if (isUnused(element)) {
                    holder.newAnnotation(
                        HighlightSeverity.WARNING,
                        WavefrontObjBundle.message(key = "fileTypes.obj.annotation.warning.mtlFileUnused")
                    ).range(element)
                        .highlightType(ProblemHighlightType.LIKE_UNUSED_SYMBOL)
                        .create()
                }
            }
        }
    }

    private fun AnnotationBuilder.applyMaterialFileQuickFix(element: ObjMaterialFileReference): AnnotationBuilder {
        val containingDirectory = element.containingFile?.containingDirectory
        val filename = element.filename
        if (containingDirectory != null && filename != null) {
            withFix(ObjCreateMtlFileQuickFix(containingDirectory, filename))
        }
        return this
    }

    private fun isDuplicated(element: ObjMaterialFileReference): Boolean =
        element.containingObjFile?.materialFileReferences.orEmpty()
            .filterNot { it == element }
            .any { it.mtlFile == element.mtlFile }

    private fun isUnused(element: ObjMaterialFileReference): Boolean =
        element.containingObjFile?.referencedMaterials.orEmpty()
            .none { it in element.mtlFile?.materials.orEmpty() }

    private fun annotateMaterialReference(
        element: ObjMaterialReference,
        holder: AnnotationHolder
    ) {
        val materialNameNode = element.node.findChildByType(ObjTypes.MATERIAL_NAME)
        if (materialNameNode != null) {
            val materialName = element.materialName
            val materialFiles = element.containingObjFile?.referencedMtlFiles.orEmpty()
            val materials = materialFiles.flatMap { file -> file.materialIdentifiers }
            if (!materialName.isNullOrBlank() && materialName !in materials.mapNotNull { it.name }) {
                holder.newAnnotation(
                    HighlightSeverity.ERROR,
                    WavefrontObjBundle.message("fileTypes.obj.annotation.error.materialNotFound")
                ).range(materialNameNode)
                    .applyMaterialQuickFixes(element, materialFiles, materialName)
                    .highlightType(ProblemHighlightType.LIKE_UNKNOWN_SYMBOL)
                    .create()
            }
        }
    }

    private fun AnnotationBuilder.applyMaterialQuickFixes(
        element: ObjMaterialReference,
        materialFiles: Iterable<MtlFile>,
        materialName: String
    ): AnnotationBuilder {
        val containingFile = element.containingFile
        if (containingFile != null) {
            withFixes(
                materialFiles.map { file ->
                    ObjCreateMaterialQuickFix(containingFile, file, materialName)
                }
            )
        }
        return this
    }

    private fun AnnotationBuilder.withFixes(fixes: Iterable<IntentionAction>): AnnotationBuilder {
        for (fix in fixes) {
            withFix(fix)
        }
        return this
    }
}
