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

package it.czerwinski.intellij.wavefront.actions

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runWriteAction
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.wavefront.actions.gl.PreFilteredEnvironmentRenderer
import it.czerwinski.intellij.wavefront.editor.gl.textures.toBufferedImage
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max
import kotlin.math.pow

object PreFilteredEnvironmentMapsGenerator {

    fun generate(inputFile: VirtualFile, levels: Int, samples: Int, outputSuffix: String): List<File> =
        (1..levels)
            .map { level ->
                val roughness = level.toFloat() / levels.toFloat()
                generate(inputFile, level, roughness, samples, outputSuffix)
            }

    private fun generate(
        inputFile: VirtualFile,
        level: Int,
        roughness: Float,
        samples: Int,
        outputSuffix: String
    ): File {
        val outputFile = getOutputFile(inputFile, level, outputSuffix)
        generate(inputFile, level, roughness, samples, outputFile)
        return outputFile
    }

    private fun getOutputFile(inputFile: VirtualFile, level: Int, filenameSuffix: String): File {
        val outputDir = LocalFileSystem.getInstance().getNioPath(inputFile.parent)?.toFile()
        val outputFileName = "${inputFile.nameWithoutExtension}$filenameSuffix$level.${inputFile.extension}"
        return File(outputDir, outputFileName)
    }

    private fun generate(inputFile: VirtualFile, level: Int, roughness: Float, samples: Int, outputFile: File) {
        val preFilteredMap = generatePreFilteredMap(inputFile, level, roughness, samples)
        if (preFilteredMap != null) {
            invokeLater {
                savePreFilteredMap(preFilteredMap, outputFile)
                preFilteredMap.flush()
            }
        }
    }

    private fun generatePreFilteredMap(
        inputFile: VirtualFile,
        level: Int,
        roughness: Float,
        samples: Int
    ): BufferedImage? {
        val inputImage = ImageIO.read(inputFile.inputStream)
        val outputHeight = (max(inputImage.width / 2, inputImage.height) / 2f.pow(level)).toInt()
        val outputWidth = outputHeight * 2

        val renderer = PreFilteredEnvironmentRenderer(inputImage, roughness, samples, outputWidth, outputHeight)
        renderer.render()

        val outputImage = renderer.outputImage?.toBufferedImage(type = BufferedImage.TYPE_INT_RGB)
        inputImage.flush()
        if (outputImage != renderer.outputImage) {
            renderer.outputImage?.flush()
        }
        return outputImage
    }

    private fun savePreFilteredMap(image: RenderedImage, outputFile: File) {
        runWriteAction {
            outputFile.delete()
            ImageIO.write(image, outputFile.extension, outputFile)
        }
    }
}
