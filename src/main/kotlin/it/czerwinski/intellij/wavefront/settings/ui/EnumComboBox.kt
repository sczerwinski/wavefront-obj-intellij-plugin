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

package it.czerwinski.intellij.wavefront.settings.ui

import com.intellij.openapi.ui.ComboBox
import com.intellij.ui.EnumComboBoxModel
import com.intellij.ui.dsl.builder.Cell
import com.intellij.ui.dsl.builder.Row
import com.intellij.ui.dsl.builder.bindItem
import javax.swing.ListCellRenderer

/**
 * Builds a combo box cell for enum values.
 */
inline fun <reified E : Enum<E>> Row.enumComboBox(
    defaultValue: E,
    renderer: ListCellRenderer<E?>?
): Cell<ComboBox<E>> =
    comboBox(EnumComboBoxModel(E::class.java), renderer)
        .bindItem(
            getter = { defaultValue },
            setter = { },
        )
