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

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.SimpleListCellRenderer
import com.intellij.ui.components.JBTextField
import com.intellij.ui.table.JBTable
import it.czerwinski.intellij.wavefront.editor.model.MaterialProperty
import it.czerwinski.intellij.wavefront.editor.model.materialProperties
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionType
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarChannel
import javax.swing.DefaultCellEditor
import javax.swing.table.TableCellEditor
import javax.swing.table.TableCellRenderer
import javax.swing.table.TableColumnModel
import javax.swing.table.TableModel

class MaterialPropertiesTable(
    project: Project,
    model: TableModel,
    columnModel: TableColumnModel? = null,
    editor: FileEditor? = null
) : JBTable(model, columnModel) {

    private val myRenderer = MaterialPropertiesTableCellRenderer()

    private val myColorEditor = ColorTableCellEditor(editor)

    private val myIlluminationEditor = DefaultCellEditor(
        ComboBox((listOf(null) + enumValues<MtlIlluminationValueElement.Illumination>()).toTypedArray()).apply {
            renderer = SimpleListCellRenderer.create("") { value -> value?.description }
        }
    )

    private val myFloatEditor = DefaultCellEditor(JBTextField()).apply { clickCountToStart = 1 }

    private val myTextureEditor = TextureTableCellEditor(project, parent = editor)

    private val myScalarChannelEditor = DefaultCellEditor(
        ComboBox((listOf(null) + enumValues<MtlScalarChannel>()).toTypedArray()).apply {
            renderer = SimpleListCellRenderer.create("") { value -> value?.description }
        }
    )

    private val myReflectionTypeEditor = DefaultCellEditor(
        ComboBox((listOf(null) + enumValues<MtlReflectionType>()).toTypedArray()).apply {
            renderer = SimpleListCellRenderer.create("") { value -> value?.description }
        }
    )

    override fun getCellRenderer(row: Int, column: Int): TableCellRenderer = myRenderer

    override fun getCellEditor(row: Int, column: Int): TableCellEditor = when (materialProperties[row]) {
        is MaterialProperty.MaterialColor -> myColorEditor
        is MaterialProperty.MaterialIlluminationValue -> myIlluminationEditor
        is MaterialProperty.MaterialFloatValue -> myFloatEditor
        is MaterialProperty.MaterialTexture -> myTextureEditor
        is MaterialProperty.MaterialTextureScalarChannel -> myScalarChannelEditor
        is MaterialProperty.MaterialTextureValueModifier -> myFloatEditor
        is MaterialProperty.MaterialReflectionTexture -> myTextureEditor
        is MaterialProperty.MaterialReflectionTextureType -> myReflectionTypeEditor
    }
}
