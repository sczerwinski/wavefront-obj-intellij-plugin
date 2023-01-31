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

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.jogamp.opengl.GLProfile
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.fromBufferedImage
import it.czerwinski.intellij.wavefront.lang.psi.util.findMatchingTextureVirtualFiles
import java.awt.image.BufferedImage
import javax.imageio.ImageIO

/**
 * GL textures manager.
 */
class TexturesManager {

    private val textureImageSourceBuilder = TextureImageSource.builder()

    private val imageSources = mutableMapOf<String, TextureImageSource>()
    private val textures = mutableMapOf<String, Texture>()

    /**
     * Loads texture image from a file with given [filename] before creating a texture.
     *
     * This method can be executed without GL thread.
     */
    fun prepare(profile: GLProfile, project: Project, filename: String) {
        if (imageSources[filename] == null) {
            val file = runReadAction { project.findMatchingTextureVirtualFiles(filename).firstOrNull() }
            if (file != null) {
                val originalImage = ImageIO.read(file.inputStream)
                val mirroredImage = originalImage.mirrorY(BufferedImage.TYPE_INT_ARGB)
                imageSources[filename] = textureImageSourceBuilder
                    .fromBufferedImage(mirroredImage)
                    .buildPrepared(profile)
                originalImage.flush()
                mirroredImage.flush()
            }
        }
    }

    /**
     * Returns texture created from a file with given [filename].
     *
     * Creates the texture if it does not exist.
     */
    operator fun get(gl: GlimpseAdapter, filename: String, withMipmaps: Boolean): Texture =
        textures.getOrPut(filename) {
            createTexture(gl, filename, withMipmaps)
        }

    private fun createTexture(gl: GlimpseAdapter, filename: String, withMipmaps: Boolean): Texture {
        val imageSource = imageSources.getOrElse(filename) { error("Image not loaded: $filename") }
        val texture = Texture.Builder.getInstance(gl)
            .addTexture(imageSource)
            .apply {
                if (withMipmaps) {
                    generateMipmaps()
                }
            }
            .build()
            .first()
        imageSource.dispose()
        return texture
    }

    /**
     * Disposes all previously created textures.
     */
    fun dispose(gl: GlimpseAdapter) {
        for (imageSource in imageSources.values) {
            imageSource.dispose()
        }
        imageSources.clear()

        for (texture in textures.values) {
            texture.dispose(gl)
        }
        textures.clear()
    }
}
