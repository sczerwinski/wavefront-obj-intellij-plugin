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

package it.czerwinski.intellij.common.editor

import com.intellij.ide.ui.UISettings
import com.intellij.ide.ui.UISettingsListener
import com.intellij.ui.JBColor
import com.intellij.ui.border.CustomLineBorder
import java.awt.BorderLayout
import java.awt.Graphics
import javax.swing.JPanel

class EditorFooterComponent : JPanel(BorderLayout()), UISettingsListener {

    init {
        border = CustomLineBorder(JBColor.border(), 1, 0, 0, 0)
    }

    override fun paint(g: Graphics) {
        UISettings.setupAntialiasing(g)
        super.paint(g)
    }

    override fun uiSettingsChanged(uiSettings: UISettings) = Unit
}
