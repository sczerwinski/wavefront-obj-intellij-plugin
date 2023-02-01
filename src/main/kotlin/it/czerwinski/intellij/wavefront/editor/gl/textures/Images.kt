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

package it.czerwinski.intellij.wavefront.editor.gl.textures

import com.intellij.util.ui.UIUtil
import java.awt.Graphics2D
import java.awt.Image
import java.awt.image.BufferedImage

/**
 * Converts this [Image] to a [BufferedImage] of the given [type].
 */
fun Image.toBufferedImage(type: Int): BufferedImage =
    if (this is BufferedImage && this.type == type) {
        this
    } else {
        val bufferedImage = UIUtil.createImage(null, getWidth(null), getHeight(null), type)

        val graphics: Graphics2D = bufferedImage.createGraphics()
        graphics.drawImage(this, 0, 0, null)
        graphics.dispose()

        bufferedImage
    }

/**
 * Mirrors this [Image] along Y axis and returns as a [BufferedImage] of the given [type].
 */
fun Image.mirrorY(type: Int): BufferedImage {
    val outputImage = UIUtil.createImage(null, getWidth(null), getHeight(null), type)

    with(outputImage.createGraphics()) {
        scale(1.0, -1.0)
        drawImage(this@mirrorY, 0, -getHeight(null), null)
        dispose()
    }

    return outputImage
}
