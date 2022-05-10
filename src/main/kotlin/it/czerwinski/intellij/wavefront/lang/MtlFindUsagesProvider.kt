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

import com.intellij.lang.cacheBuilder.DefaultWordsScanner
import com.intellij.lang.cacheBuilder.WordsScanner
import com.intellij.lang.findUsages.FindUsagesProvider
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.TokenSet
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifier
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes

class MtlFindUsagesProvider : FindUsagesProvider {

    override fun getWordsScanner(): WordsScanner? =
        DefaultWordsScanner(
            MtlLexerAdapter(),
            TokenSet.create(MtlTypes.MATERIAL_NAME),
            TokenSet.create(MtlTypes.COMMENT),
            TokenSet.EMPTY
        )

    override fun canFindUsagesFor(psiElement: PsiElement): Boolean =
        psiElement is MtlMaterialIdentifier

    override fun getHelpId(psiElement: PsiElement): String? = null

    override fun getType(element: PsiElement): String =
        if (element is MtlMaterialIdentifier) WavefrontObjBundle.getMessage("fileTypes.mtl.usages.material")
        else ""

    override fun getDescriptiveName(element: PsiElement): String =
        if (element is MtlMaterialIdentifier) element.name.orEmpty()
        else ""

    override fun getNodeText(element: PsiElement, useFullName: Boolean): String =
        if (element is MtlMaterialIdentifier) "newmtl ${element.text}"
        else ""
}
