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

import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.fromResource

object TextureResources {

    private const val MISSING_TEXTURE_RESOURCE = "/textures/missing_texture.png"
    private const val MISSING_NORMALMAP_RESOURCE = "/textures/missing_normalmap.png"

    val missingTextureImageSource: TextureImageSource
        get() = TextureImageSource.builder()
            .fromResource(owner = this, MISSING_TEXTURE_RESOURCE)
            .build()

    val missingNormalmapImageSource: TextureImageSource
        get() = TextureImageSource.builder()
            .fromResource(owner = this, MISSING_NORMALMAP_RESOURCE)
            .build()
}
