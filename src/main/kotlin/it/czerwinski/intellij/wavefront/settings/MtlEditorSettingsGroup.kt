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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.bindItem
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.settings.ui.MaterialPreviewMeshListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.MaterialShadingMethodListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.MtlSplitEditorLayoutListCellRenderer
import it.czerwinski.intellij.wavefront.settings.ui.enumComboBox
import javax.swing.JComponent

class MtlEditorSettingsGroup : SettingsGroup, MtlEditorSettingsState.Holder {

    private lateinit var defaultEditorLayoutComboBox: ComboBox<TextEditorWithPreview.Layout>
    private lateinit var verticalSplitCheckBox: JBRadioButton
    private lateinit var horizontalSplitCheckBox: JBRadioButton
    private lateinit var defaultPreviewMesh: ComboBox<MaterialPreviewMesh>
    private lateinit var defaultShadingMethod: ComboBox<ShadingMethod>

    override var mtlEditorSettings: MtlEditorSettingsState
        get() = MtlEditorSettingsState(
            editorLayout = defaultEditorLayoutComboBox.item ?: WavefrontObjSettingsState.DEFAULT_LAYOUT,
            isVerticalSplit = verticalSplitCheckBox.isSelected,
            defaultPreviewMesh = defaultPreviewMesh.item ?: MaterialPreviewMesh.DEFAULT,
            defaultShadingMethod = defaultShadingMethod.item ?: ShadingMethod.MTL_DEFAULT
        )
        set(value) {
            defaultEditorLayoutComboBox.item = value.editorLayout
            verticalSplitCheckBox.isSelected = value.isVerticalSplit
            horizontalSplitCheckBox.isSelected = !value.isVerticalSplit
            defaultPreviewMesh.item = value.defaultPreviewMesh
            defaultShadingMethod.item = value.defaultShadingMethod
        }

    override fun createGroupContents(panel: Panel) {
        with(panel) {
            row(WavefrontObjBundle.message("settings.editor.fileTypes.mtl.layout.default")) {
                defaultEditorLayoutComboBox = enumComboBox(
                    defaultValue = WavefrontObjSettingsState.DEFAULT_LAYOUT,
                    renderer = MtlSplitEditorLayoutListCellRenderer()
                ).component
            }
            buttonsGroup(title = WavefrontObjBundle.message("settings.editor.fileTypes.mtl.layout.split")) {
                row {
                    horizontalSplitCheckBox = radioButton(
                        WavefrontObjBundle.message("settings.editor.fileTypes.mtl.layout.split.horizontal")
                    ).component
                }
                row {
                    verticalSplitCheckBox = radioButton(
                        WavefrontObjBundle.message("settings.editor.fileTypes.mtl.layout.split.vertical")
                    ).component
                }
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.mtl.material.previewMesh")) {
                defaultPreviewMesh = enumComboBox(
                    defaultValue = MaterialPreviewMesh.DEFAULT,
                    renderer = MaterialPreviewMeshListCellRenderer()
                ).component
            }
            row(WavefrontObjBundle.message("settings.editor.fileTypes.mtl.material.shadingMethod")) {
                defaultShadingMethod = comboBox(
                    items = ShadingMethod.materialValues,
                    renderer = MaterialShadingMethodListCellRenderer()
                ).bindItem(
                    getter = { ShadingMethod.MTL_DEFAULT },
                    setter = { }
                ).component
            }
        }
    }

    override fun getPreferredFocusedComponent(): JComponent = defaultPreviewMesh

    override fun validateForm() = Unit
}
