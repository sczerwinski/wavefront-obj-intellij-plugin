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

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReferenceElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile

abstract class ObjMaterialReferenceElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjMaterialReferenceElement {

    override val materialName: String?
        get() = node.findChildByType(ObjTypes.MATERIAL_NAME)?.text

    override val material: MtlMaterial?
        get() = containingObjFile?.let { sourceFile ->
            sourceFile.referencedMtlFiles
                .flatMap { mtlFile -> mtlFile.materials }
                .firstOrNull { material -> material.getName() == materialName }
        }

    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
