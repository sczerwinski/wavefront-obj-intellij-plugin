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

import com.intellij.ui.components.JBLabel
import it.czerwinski.intellij.wavefront.icons.Icons
import java.awt.Color

class ColorLabel(color: Color?) : JBLabel() {

    private var myColor: Color? = null

    var color: Color?
        get() = myColor
        set(value) {
            updateColor(value)
        }

    init {
        updateColor(color)
    }

    private fun updateColor(value: Color?) {
        myColor = value
        icon = value?.let(Icons::ColorIcon)
        text = color?.rgb?.toUInt()?.toString(radix = 16).orEmpty()
    }
}
