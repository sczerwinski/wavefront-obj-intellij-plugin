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

package it.czerwinski.intellij.common.ui

import com.intellij.ui.components.Magnificator
import com.intellij.ui.components.ZoomableViewport
import graphics.glimpse.ui.GlimpsePanel
import java.awt.BorderLayout
import java.awt.Point
import javax.swing.JPanel

class GlimpseViewport(private val glimpsePanel: GlimpsePanel) : JPanel(BorderLayout()), ZoomableViewport {

    private var at: Point? = null
    private var scale: Double = 1.0

    init {
        add(glimpsePanel, BorderLayout.CENTER)
    }

    override fun getMagnificator(): Magnificator? =
        glimpsePanel.getClientProperty(Magnificator.CLIENT_PROPERTY_KEY) as? Magnificator

    override fun magnificationStarted(at: Point?) {
        this.at = at
    }

    override fun magnificationFinished(magnification: Double) {
        scale = 1.0
        at = null
    }

    override fun magnify(magnification: Double) {
        val newScale = magnificationToScale(magnification)
        magnificator?.magnify(newScale / scale, at)
        scale = newScale
    }

    private fun magnificationToScale(magnification: Double): Double =
        if (magnification < 0) 1.0 / (1 - magnification)
        else 1 + magnification
}
