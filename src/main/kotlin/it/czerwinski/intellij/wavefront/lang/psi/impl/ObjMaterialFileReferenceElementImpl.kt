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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReferenceElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile

abstract class ObjMaterialFileReferenceElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjMaterialFileReferenceElement {

    override val filename: String?
        get() = node.findChildByType(ObjTypes.MATERIAL_FILE_NAME)?.text

    override val mtlFile: MtlFile?
        get() = containingObjFile?.let { sourceFile ->
            filename?.let { filename ->
                sourceFile.findMtlFile(filename)
            }
        }

    override val isDuplicated: Boolean
        get() = containingObjFile?.materialFileReferences.orEmpty()
            .filterNot { reference -> reference == this }
            .any { reference -> reference.mtlFile == this.mtlFile }

    override val isUnused: Boolean
        get() = containingObjFile?.referencedMaterials.orEmpty()
            .none { material -> material in mtlFile?.materials.orEmpty() }

    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
