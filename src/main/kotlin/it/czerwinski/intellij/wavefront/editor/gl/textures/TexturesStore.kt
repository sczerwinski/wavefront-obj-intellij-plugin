/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

import com.intellij.openapi.vfs.VirtualFile
import com.jogamp.opengl.GLProfile
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.TextureMagFilter
import graphics.glimpse.textures.TextureMinFilter
import graphics.glimpse.textures.TextureType
import graphics.glimpse.textures.TextureWrap

class TexturesStore {

    private val textureImageSourceBuilder = TextureImageSource.builder()

    private val imageSources = mutableMapOf<String, TextureImageSource>()
    private val textures = mutableMapOf<String, Texture>()

    fun prepare(profile: GLProfile, file: VirtualFile) {
        if (imageSources[file.path] == null) {
            imageSources[file.path] = textureImageSourceBuilder
                .withFilename(file.name)
                .fromInputStream { file.inputStream }
                .buildPrepared(profile)
        }
    }

    operator fun get(gl: GlimpseAdapter, file: VirtualFile): Texture? = textures.getOrPut(file.path) {
        createTexture(gl, file)
    }

    private fun createTexture(gl: GlimpseAdapter, file: VirtualFile): Texture {
        val texture = Texture.Builder.getInstance(gl)
            .addTexture(imageSources.getOrPut(file.path) { createTextureImageSource(file) })
            .generateMipmaps()
            .build()
            .first()
        gl.glTexParameterWrap(TextureType.TEXTURE_2D, TextureWrap.REPEAT, TextureWrap.REPEAT)
        gl.glTexParameterFilter(TextureType.TEXTURE_2D, TextureMinFilter.LINEAR_MIPMAP_LINEAR, TextureMagFilter.LINEAR)
        return texture
    }

    private fun createTextureImageSource(file: VirtualFile): TextureImageSource =
        textureImageSourceBuilder
            .withFilename(file.name)
            .fromInputStream { file.inputStream }
            .build()

    fun dispose(gl: GlimpseAdapter) {
        for (texture in textures.values) {
            texture.dispose(gl)
        }
        textures.clear()
    }
}
