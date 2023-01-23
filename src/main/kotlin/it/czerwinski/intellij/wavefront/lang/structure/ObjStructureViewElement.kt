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

package it.czerwinski.intellij.wavefront.lang.structure

import com.intellij.ide.structureView.StructureViewTreeElement
import com.intellij.ide.util.treeView.smartTree.TreeElement
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.NavigatablePsiElement
import com.intellij.psi.util.PsiTreeUtil
import it.czerwinski.intellij.wavefront.lang.psi.ObjObjectOrGroupIdentifier

class ObjStructureViewElement(
    private val element: NavigatablePsiElement
) : StructureViewTreeElement {

    override fun getValue(): Any = element

    override fun navigate(requestFocus: Boolean) {
        element.navigate(requestFocus)
    }

    override fun canNavigate(): Boolean = element.canNavigate()

    override fun canNavigateToSource(): Boolean = element.canNavigateToSource()

    override fun getPresentation(): ItemPresentation =
        ObjItemPresentationFactory.createPresentation(element)

    override fun getChildren(): Array<TreeElement> =
        PsiTreeUtil.getChildrenOfTypeAsList(
            element,
            NavigatablePsiElement::class.java
        ).filterNot { element ->
            element is ObjObjectOrGroupIdentifier
        }.map { element ->
            ObjStructureViewElement(element)
        }.toTypedArray()
}
