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

import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.materialProperties
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import javax.swing.table.AbstractTableModel

class MaterialPropertiesTableModel(
    material: MtlMaterialElement? = null
) : AbstractTableModel() {

    private var myMaterial: MtlMaterialElement? = material

    fun updateMaterial(material: MtlMaterialElement?) {
        myMaterial = material
        fireTableDataChanged()
    }

    override fun getRowCount(): Int = materialProperties.size

    override fun getColumnCount(): Int = COLUMNS_COUNT

    override fun getColumnName(column: Int): String? = when (column) {
        COLUMN_LABEL -> WavefrontObjBundle.message("editor.fileTypes.mtl.material.properties.column.label")
        COLUMN_VALUE -> WavefrontObjBundle.message("editor.fileTypes.mtl.material.properties.column.value")
        else -> null
    }

    override fun isCellEditable(rowIndex: Int, columnIndex: Int): Boolean = columnIndex == COLUMN_VALUE

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val property = materialProperties[rowIndex]
        return when (columnIndex) {
            COLUMN_LABEL -> property.label
            COLUMN_VALUE -> property.getValue(myMaterial)
            else -> null
        }
    }

    override fun setValueAt(aValue: Any?, rowIndex: Int, columnIndex: Int) {
        materialProperties[rowIndex].setValue(myMaterial, aValue)
    }

    companion object {
        private const val COLUMNS_COUNT = 2

        private const val COLUMN_LABEL = 0
        private const val COLUMN_VALUE = 1
    }
}
