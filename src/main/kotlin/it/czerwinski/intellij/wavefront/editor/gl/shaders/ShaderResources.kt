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

package it.czerwinski.intellij.wavefront.editor.gl.shaders

import graphics.glimpse.shaders.ShaderType
import it.czerwinski.intellij.wavefront.editor.model.ShaderQuality
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import java.util.Locale

object ShaderResources {

    private val <E : Enum<E>> Enum<E>.lowerCaseName
        get() = name.lowercase(Locale.ENGLISH)

    fun getShaderSource(shadingMethod: ShadingMethod, shaderType: ShaderType, shaderQuality: ShaderQuality): String =
        loadShaderSource(
            path = "/shaders/${shadingMethod.lowerCaseName}_${shaderType.lowerCaseName}.glsl",
            shaderQuality
        )

    private fun loadShaderSource(path: String, shaderQuality: ShaderQuality): String =
        javaClass.getResourceAsStream(path)
            ?.use { inputStream -> inputStream.bufferedReader().readText() }
            .orEmpty()
            .let { source ->
                """
                    #version 100
                    precision ${shaderQuality.lowerCaseName}p float;
                    $source
                """.trimIndent()
            }

    fun getNamedShaderSource(name: String, shaderType: ShaderType, shaderQuality: ShaderQuality): String =
        loadShaderSource(path = "/shaders/${name}_${shaderType.lowerCaseName}.glsl", shaderQuality)
}
