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
import it.czerwinski.intellij.wavefront.actions.gl.DiffuseIrradianceRenderer
import it.czerwinski.intellij.wavefront.editor.gl.textures.toBufferedImage
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO
import kotlin.math.max

object DiffuseIrradianceMapGenerator {

    fun generate(inputFile: VirtualFile, samples: Int, outputSuffix: String): File {
        val outputFile = getOutputFile(inputFile, outputSuffix)
        generate(inputFile, samples, outputFile)
        return outputFile
    }

    private fun getOutputFile(inputFile: VirtualFile, filenameSuffix: String): File {
        val outputDir = LocalFileSystem.getInstance().getNioPath(inputFile.parent)?.toFile()
        val outputFileName = "${inputFile.nameWithoutExtension}$filenameSuffix.${inputFile.extension}"
        return File(outputDir, outputFileName)
    }

    private fun generate(inputFile: VirtualFile, samples: Int, outputFile: File) {
        val diffuseIrradianceMap = generateDiffuseIrradianceMap(inputFile, samples)
        if (diffuseIrradianceMap != null) {
            invokeLater {
                saveDiffuseIrradianceMap(diffuseIrradianceMap, outputFile)
            }
        }
    }

    private fun generateDiffuseIrradianceMap(inputFile: VirtualFile, samples: Int): BufferedImage? {
        val inputImage = ImageIO.read(inputFile.inputStream)
        val outputHeight = max(inputImage.width / 2, inputImage.height)
        val outputWidth = outputHeight * 2

        val renderer = DiffuseIrradianceRenderer(inputImage, samples, outputWidth, outputHeight)
        renderer.render()

        return renderer.outputImage?.toBufferedImage(type = BufferedImage.TYPE_INT_RGB)
    }

    private fun saveDiffuseIrradianceMap(image: RenderedImage, outputFile: File) {
        runWriteAction {
            outputFile.delete()
            ImageIO.write(image, outputFile.extension, outputFile)
        }
    }
}
