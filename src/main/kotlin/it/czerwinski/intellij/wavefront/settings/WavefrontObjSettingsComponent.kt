/*
 * Copyright 2020 Slawomir Czerwinski
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

import com.intellij.ui.components.JBRadioButton
import com.intellij.ui.layout.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import javax.swing.JComponent
import javax.swing.JPanel

class WavefrontObjSettingsComponent {

    val mainPanel: JPanel

    private val horizontalSplitRadioButton = JBRadioButton(
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.split.horizontal")
    )
    private val verticalSplitRadioButton = JBRadioButton(
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.split.vertical")
    )

    var isVerticalSplit: Boolean
        get() = verticalSplitRadioButton.isSelected
        set(value) {
            horizontalSplitRadioButton.isSelected = !value
            verticalSplitRadioButton.isSelected = value
        }

    init {
        mainPanel = panel {
            row(WavefrontObjBundle.message("settings.editor.fileTypes.obj.split")) {
                buttonGroup {
                    row { horizontalSplitRadioButton() }
                    row { verticalSplitRadioButton() }
                }
            }
        }
    }

    fun getPreferredFocusedComponent(): JComponent = horizontalSplitRadioButton
}
