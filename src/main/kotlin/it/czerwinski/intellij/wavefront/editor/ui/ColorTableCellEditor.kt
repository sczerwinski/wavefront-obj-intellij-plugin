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

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.ui.ColorPicker
import com.intellij.ui.ColorPickerListener
import com.intellij.util.ui.AbstractTableCellEditor
import it.czerwinski.intellij.common.ui.ColorTextField
import java.awt.Color
import java.awt.Component
import javax.swing.JTable

class ColorTableCellEditor(
    private val editor: FileEditor? = null,
    private val enableOpacity: Boolean = false,
    private val opacityInPercent: Boolean = false
) : AbstractTableCellEditor() {

    private val myComponent: ColorTextField = ColorTextField()

    override fun getCellEditorValue(): Any? = myComponent.selectedColor

    override fun getTableCellEditorComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        row: Int,
        column: Int
    ): Component {
        myComponent.selectedColor = value as? Color
        invokeLater {
            showColorPickerDialog(value as? Color)
            editor?.preferredFocusedComponent?.requestFocus()
        }
        return myComponent
    }

    private fun showColorPickerDialog(preselectedColor: Color?) {
        ColorPicker.showDialog(
            myComponent,
            null,
            preselectedColor,
            enableOpacity,
            listOf(MyColorPickerListener()),
            opacityInPercent
        )
    }

    private inner class MyColorPickerListener : ColorPickerListener {

        override fun colorChanged(color: Color?) = Unit

        override fun closed(color: Color?) {
            if (color != null) {
                myComponent.selectedColor = color
                stopCellEditing()
            } else {
                cancelCellEditing()
            }
        }
    }
}
