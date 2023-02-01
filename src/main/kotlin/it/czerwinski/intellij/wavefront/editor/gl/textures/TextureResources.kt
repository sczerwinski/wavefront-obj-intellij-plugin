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

import com.jogamp.opengl.GLProfile
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.fromResource

object TextureResources {

    private const val ENVIRONMENTS_COUNT = 8
    internal const val REFLECTION_LEVELS_COUNT = 8

    private const val ENVIRONMENT_TEXTURE_RESOURCE_FORMAT = "/textures/environment%d.jpg"
    private const val IRRADIANCE_TEXTURE_RESOURCE_FORMAT = "/textures/environment%d.irradiance.jpg"
    private const val REFLECTION_TEXTURE_RESOURCE_FORMAT = "/textures/environment%d.reflection%d.jpg"

    private const val BRDF_TEXTURE_RESOURCE = "/textures/brdf.png"
    private const val FONT_TEXTURE_RESOURCE = "/textures/jb_mono.png"
    private const val BOLD_FONT_TEXTURE_RESOURCE = "/textures/jb_mono_bold.png"
    private const val FALLBACK_TEXTURE_RESOURCE = "/textures/fallback_texture.png"
    private const val FALLBACK_NORMALMAP_RESOURCE = "/textures/fallback_normalmap.png"

    var environmentTextureImageSources: List<TextureImageSource> = (1..ENVIRONMENTS_COUNT).map { env ->
        getTextureImageSource(ENVIRONMENT_TEXTURE_RESOURCE_FORMAT.format(env))
    }

    var irradianceTextureImageSources: List<TextureImageSource> = (1..ENVIRONMENTS_COUNT).map { env ->
        getTextureImageSource(IRRADIANCE_TEXTURE_RESOURCE_FORMAT.format(env))
    }

    var reflectionTextureImageSources: List<List<TextureImageSource>> = (1..ENVIRONMENTS_COUNT).map { env ->
        (1 until REFLECTION_LEVELS_COUNT).map { level ->
            getTextureImageSource(REFLECTION_TEXTURE_RESOURCE_FORMAT.format(env, level))
        }
    }

    var brdfTextureImageSource: TextureImageSource = getTextureImageSource(BRDF_TEXTURE_RESOURCE)
        private set

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
        environmentTextureImageSources = (1..ENVIRONMENTS_COUNT).map { env ->
            getPreparedTextureImageSource(ENVIRONMENT_TEXTURE_RESOURCE_FORMAT.format(env), profile)
        }
        irradianceTextureImageSources = (1..ENVIRONMENTS_COUNT).map { env ->
            getPreparedTextureImageSource(IRRADIANCE_TEXTURE_RESOURCE_FORMAT.format(env), profile)
        }
        reflectionTextureImageSources = (1..ENVIRONMENTS_COUNT).map { env ->
            (1 until REFLECTION_LEVELS_COUNT).map { level ->
                getPreparedTextureImageSource(REFLECTION_TEXTURE_RESOURCE_FORMAT.format(env, level), profile)
            }
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
