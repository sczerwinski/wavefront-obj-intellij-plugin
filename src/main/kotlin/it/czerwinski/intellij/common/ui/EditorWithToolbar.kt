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

package it.czerwinski.intellij.common.ui

import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * A panel containing an editor component and a toolbar on top.
 */
class EditorWithToolbar(
    toolbarComponent: JComponent,
    editorComponent: JComponent
) : JPanel(BorderLayout()) {

    init {
        add(toolbarComponent, BorderLayout.BEFORE_FIRST_LINE)
        add(editorComponent, BorderLayout.CENTER)
    }
}
