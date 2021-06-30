/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

import com.intellij.ui.components.JBTextField
import com.intellij.ui.layout.Cell
import com.intellij.ui.layout.CellBuilder

/**
 * Simplified cell builder method for a text field with a default value.
 */
fun Cell.textField(defaultValue: Any, columns: Int): CellBuilder<JBTextField> = textField(
    getter = { defaultValue.toString() },
    setter = { },
    columns = columns
)
