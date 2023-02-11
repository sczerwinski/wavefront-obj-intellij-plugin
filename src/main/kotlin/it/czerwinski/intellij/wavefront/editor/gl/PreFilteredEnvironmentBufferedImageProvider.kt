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

package it.czerwinski.intellij.wavefront.editor.gl

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import graphics.glimpse.textures.BufferedImageProvider
import it.czerwinski.intellij.wavefront.actions.gl.PreFilteredEnvironmentRenderer
import it.czerwinski.intellij.wavefront.editor.gl.textures.toBufferedImage
import it.czerwinski.intellij.wavefront.lang.psi.util.findMatchingTextureVirtualFiles
import java.awt.image.BufferedImage
import javax.imageio.ImageIO
import kotlin.math.max

class PreFilteredEnvironmentBufferedImageProvider(
    private val project: Project,
    private val environmentTextureFilename: String,
    private val roughness: Float
) : BufferedImageProvider {

    override fun createBufferedImage(): BufferedImage? {
        val inputFile = checkNotNull(
            runReadAction { project.findMatchingTextureVirtualFiles(environmentTextureFilename).firstOrNull() }
        ) {
            "Could not find texture file: $environmentTextureFilename"
        }
        val inputImage = ImageIO.read(inputFile.inputStream)
        val outputHeight = max(inputImage.width / 2, inputImage.height)
        val outputWidth = outputHeight * 2

        val renderer = PreFilteredEnvironmentRenderer(inputImage, roughness, SAMPLES, outputWidth, outputHeight)
        renderer.render()

        val outputImage = renderer.outputImage?.toBufferedImage(type = BufferedImage.TYPE_INT_RGB)
        inputImage.flush()
        if (outputImage != renderer.outputImage) {
            renderer.outputImage?.flush()
        }
        return outputImage
    }

    companion object {
        private const val SAMPLES = 100
    }
}
