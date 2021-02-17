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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.psi.PsiElement
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile

object ObjPsiImplUtil {

    @JvmStatic
    fun getType(element: ObjFace): ObjFaceType? =
        ObjFaceType.fromVerticesCount(element.faceVertexList.size)

    @JvmStatic
    fun getFilename(element: ObjMaterialFileReference): String? =
        element.node.findChildByType(ObjTypes.MATERIAL_FILE_NAME)?.text

    @JvmStatic
    fun getMtlFile(element: ObjMaterialFileReference): MtlFile? =
        element.containingObjFile?.let { sourceFile ->
            getFilename(element)?.let { filename ->
                sourceFile.findMtlFile(filename)
            }
        }

    @JvmStatic
    fun getMaterialName(element: ObjMaterialReference): String? =
        element.node.findChildByType(ObjTypes.MATERIAL_NAME)?.text

    @JvmStatic
    fun getMaterial(element: ObjMaterialReference): MtlMaterial? =
        element.containingObjFile?.let { sourceFile ->
            sourceFile.referencedMtlFiles
                .flatMap { mtlFile -> mtlFile.materials }
                .firstOrNull { material -> material.getName() == element.materialName }
        }

    @JvmStatic
    fun getReferences(element: PsiElement): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(element)
}
