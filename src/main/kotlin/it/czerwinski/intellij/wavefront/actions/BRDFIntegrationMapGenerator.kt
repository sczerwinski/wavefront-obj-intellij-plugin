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
import it.czerwinski.intellij.wavefront.actions.gl.BRDFRenderer
import it.czerwinski.intellij.wavefront.editor.gl.textures.toBufferedImage
import java.awt.image.BufferedImage
import java.awt.image.RenderedImage
import java.io.File
import javax.imageio.ImageIO

object BRDFIntegrationMapGenerator {

    fun generate(inputFile: VirtualFile, size: Int, samples: Int, outputFilename: String): File {
        val outputFile = getOutputFile(inputFile, outputFilename)
        generate(size, samples, outputFile)
        return outputFile
    }

    private fun getOutputFile(inputFile: VirtualFile, outputFilename: String): File {
        val outputDir = getOutputDir(inputFile)
        return File(outputDir, outputFilename)
    }

    private fun getOutputDir(inputFile: VirtualFile): File? {
        val virtualDir = if (inputFile.isDirectory) inputFile else inputFile.parent
        return LocalFileSystem.getInstance().getNioPath(virtualDir)?.toFile()
    }

    private fun generate(size: Int, samples: Int, outputFile: File) {
        val brdfIntegrationMap = generateBRDFIntegrationMap(size, samples)
        if (brdfIntegrationMap != null) {
            invokeLater {
                saveBRDFIntegrationMap(brdfIntegrationMap, outputFile)
            }
        }
    }

    private fun generateBRDFIntegrationMap(size: Int, samples: Int): BufferedImage? {
        val renderer = BRDFRenderer(samples, size, size)
        renderer.render()
        return renderer.outputImage?.toBufferedImage(type = BufferedImage.TYPE_INT_RGB)
    }

    private fun saveBRDFIntegrationMap(image: RenderedImage, outputFile: File) {
        runWriteAction {
            outputFile.delete()
            ImageIO.write(image, outputFile.extension, outputFile)
        }
    }
}
