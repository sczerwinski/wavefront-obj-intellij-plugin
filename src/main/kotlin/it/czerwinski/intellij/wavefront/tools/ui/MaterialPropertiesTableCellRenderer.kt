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

package it.czerwinski.intellij.wavefront.tools.ui

import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.JBEmptyBorder
import it.czerwinski.intellij.common.ui.ColorLabel
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionType
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarChannel
import it.czerwinski.intellij.wavefront.tools.model.MaterialPropertiesModel
import java.awt.Color
import java.awt.Component
import javax.swing.JTable
import javax.swing.SwingConstants
import javax.swing.table.TableCellRenderer

class MaterialPropertiesTableCellRenderer : TableCellRenderer {

    private val myMaterialPropertiesModel = MaterialPropertiesModel.getInstance()

    override fun getTableCellRendererComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        hasFocus: Boolean,
        row: Int,
        column: Int
    ): Component = when (value) {
        is String -> JBLabel(value)
        is Float -> JBLabel(value.toString(), SwingConstants.TRAILING)
        is Color -> ColorLabel(value)
        is MtlIlluminationValueElement.Illumination -> JBLabel(value.description)
        is MtlScalarChannel -> JBLabel(value.description)
        is MtlReflectionType -> JBLabel(value.description)
        else -> JBLabel(value?.toString().orEmpty())
    }.apply {
        border = labelBorder
        myMaterialPropertiesModel.materialProperties[row].applyStyle(component = this)
    }

    companion object {
        private const val BORDER_HORIZONTAL = 4
        private const val BORDER_VERTICAL = 2

        private val labelBorder =
            JBEmptyBorder(BORDER_VERTICAL, BORDER_HORIZONTAL, BORDER_VERTICAL, BORDER_HORIZONTAL)
    }
}
