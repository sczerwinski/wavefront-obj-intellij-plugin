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

package it.czerwinski.intellij.common.ui

import com.intellij.ui.components.fields.ExtendableTextComponent
import com.intellij.ui.components.fields.ExtendableTextField
import it.czerwinski.intellij.wavefront.icons.Icons
import java.awt.Color
import javax.swing.Icon

class ColorTextField : ExtendableTextField() {

    private var myColor: Color? = null
    private var myIcon: Icon? = null

    var selectedColor: Color?
        get() = myColor
        set(value) {
            updateColor(value)
        }

    init {
        isFocusable = true
        addExtension(
            object : ExtendableTextComponent.Extension {
                override fun getIcon(hovered: Boolean): Icon? = myIcon
                override fun isIconBeforeText(): Boolean = true
            }
        )
        updateColor(selectedColor)
    }

    private fun updateColor(value: Color?) {
        myColor = value
        myIcon = value?.let(Icons::ColorIcon)
        text = selectedColor?.rgb?.toUInt()?.toString(radix = 16).orEmpty()
    }
}
