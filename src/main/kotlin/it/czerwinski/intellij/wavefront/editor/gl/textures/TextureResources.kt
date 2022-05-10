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

package it.czerwinski.intellij.wavefront.editor.gl.textures

import com.jogamp.opengl.GLProfile
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.fromResource

object TextureResources {

    private const val ENVIRONMENT_TEXTURES_COUNT = 6
    private const val ENVIRONMENT_TEXTURE_RESOURCE_FORMAT = "/textures/environment%d.jpg"
    private const val RADIANCE_TEXTURE_RESOURCE_FORMAT = "/textures/radiance%d.jpg"

    private const val FONT_TEXTURE_RESOURCE = "/textures/jb_mono.png"
    private const val BOLD_FONT_TEXTURE_RESOURCE = "/textures/jb_mono_bold.png"
    private const val FALLBACK_TEXTURE_RESOURCE = "/textures/fallback_texture.png"
    private const val FALLBACK_NORMALMAP_RESOURCE = "/textures/fallback_normalmap.png"

    var environmentTextureImageSources: List<TextureImageSource> = (1..ENVIRONMENT_TEXTURES_COUNT).map { index ->
        getTextureImageSource(ENVIRONMENT_TEXTURE_RESOURCE_FORMAT.format(index))
    }

    var radianceTextureImageSources: List<TextureImageSource> = (1..ENVIRONMENT_TEXTURES_COUNT).map { index ->
        getTextureImageSource(RADIANCE_TEXTURE_RESOURCE_FORMAT.format(index))
    }

    var fontTextureImageSource: TextureImageSource = getTextureImageSource(FONT_TEXTURE_RESOURCE)
        private set

    var boldFontTextureImageSource: TextureImageSource = getTextureImageSource(BOLD_FONT_TEXTURE_RESOURCE)
        private set

    var fallbackTextureImageSource: TextureImageSource = getTextureImageSource(FALLBACK_TEXTURE_RESOURCE)
        private set

    var fallbackNormalmapImageSource: TextureImageSource = getTextureImageSource(FALLBACK_NORMALMAP_RESOURCE)
        private set

    private fun getTextureImageSource(path: String): TextureImageSource =
        TextureImageSource.builder()
            .fromResource(owner = this, path)
            .build()

    fun prepare(profile: GLProfile) {
        environmentTextureImageSources = (1..ENVIRONMENT_TEXTURES_COUNT).map { index ->
            getPreparedTextureImageSource(ENVIRONMENT_TEXTURE_RESOURCE_FORMAT.format(index), profile)
        }
        radianceTextureImageSources = (1..ENVIRONMENT_TEXTURES_COUNT).map { index ->
            getPreparedTextureImageSource(RADIANCE_TEXTURE_RESOURCE_FORMAT.format(index), profile)
        }
        fontTextureImageSource = getPreparedTextureImageSource(FONT_TEXTURE_RESOURCE, profile)
        boldFontTextureImageSource = getPreparedTextureImageSource(BOLD_FONT_TEXTURE_RESOURCE, profile)
        fallbackTextureImageSource = getPreparedTextureImageSource(FALLBACK_TEXTURE_RESOURCE, profile)
        fallbackNormalmapImageSource = getPreparedTextureImageSource(FALLBACK_NORMALMAP_RESOURCE, profile)
    }

    private fun getPreparedTextureImageSource(path: String, profile: GLProfile): TextureImageSource =
        TextureImageSource.builder()
            .fromResource(owner = this, path)
            .buildPrepared(profile)
}
