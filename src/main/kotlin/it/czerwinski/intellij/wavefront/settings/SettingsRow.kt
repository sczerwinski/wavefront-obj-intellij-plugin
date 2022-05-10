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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.ui.layout.Row
import com.intellij.ui.layout.RowBuilder
import javax.swing.JComponent

/**
 * A common interface implemented by settings component rows.
 */
interface SettingsRow {

    /**
     * Creates UI components in this settings row.
     */
    fun createRow(rowBuilder: RowBuilder): Row

    /**
     * Returns the UI component preferred to gain focus.
     */
    fun getPreferredFocusedComponent(): JComponent

    /**
     * Validates the settings inputs of this component.
     */
    fun validateForm()
}
