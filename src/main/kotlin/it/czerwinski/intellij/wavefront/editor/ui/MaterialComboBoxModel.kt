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

package it.czerwinski.intellij.wavefront.editor.ui

import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import javax.swing.AbstractListModel
import javax.swing.ComboBoxModel
import kotlin.math.max

class MaterialComboBoxModel(
    materials: Iterable<MtlMaterialElement>
) : AbstractListModel<MtlMaterialElement>(), ComboBoxModel<MtlMaterialElement> {

    private val myList: MutableList<MtlMaterialElement> = materials.toMutableList()
    private var mySelected: MtlMaterialElement? = myList.firstOrNull()

    override fun getSize(): Int = myList.size

    override fun getElementAt(index: Int): MtlMaterialElement = myList[index]

    override fun setSelectedItem(anItem: Any?) {
        mySelected = (anItem as? MtlMaterialElement).takeIf { it in myList } ?: myList.firstOrNull()
        fireContentsChanged(this, 0, myList.size)
    }

    override fun getSelectedItem(): Any? = mySelected

    fun updateMaterials(materials: Iterable<MtlMaterialElement>) {
        val oldSize = myList.size
        myList.clear()
        myList.addAll(materials)
        if (mySelected !in myList) {
            mySelected = myList.firstOrNull()
        }
        fireContentsChanged(this, 0, max(oldSize, myList.size))
    }
}
