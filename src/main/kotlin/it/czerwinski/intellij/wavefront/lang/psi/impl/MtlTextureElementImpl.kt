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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiReference
import com.intellij.psi.impl.source.resolve.reference.ReferenceProvidersRegistry
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes
import it.czerwinski.intellij.wavefront.lang.psi.MtlValueModifierOption
import it.czerwinski.intellij.wavefront.lang.psi.util.findMatchingTextureFiles

abstract class MtlTextureElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlTextureElement {

    override val textureFilenameNode: ASTNode?
        get() = node.findChildByType(MtlTypes.TEXTURE_FILE)

    override val textureFilename: String?
        get() = textureFilenameNode?.text

    override val textureFiles: Collection<PsiFile>
        get() = textureFilename?.let { filename -> project.findMatchingTextureFiles(filename) }.orEmpty()

    override val valueModifierOptionElement: MtlValueModifierOption?
        get() = valueModifierOptionList.firstOrNull()

    override fun getReferences(): Array<PsiReference> =
        ReferenceProvidersRegistry.getReferencesFromProviders(this)
}
