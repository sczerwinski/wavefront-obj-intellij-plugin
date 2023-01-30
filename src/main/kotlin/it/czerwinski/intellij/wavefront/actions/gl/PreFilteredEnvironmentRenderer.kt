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

package it.czerwinski.intellij.wavefront.actions.gl

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.ProgramExecutor
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import graphics.glimpse.textures.Texture
import java.awt.image.BufferedImage

class PreFilteredEnvironmentRenderer(
    environmentImage: BufferedImage,
    private val roughness: Float,
    private val samples: Int,
    width: Int,
    height: Int
) : BaseMapRenderer<PreFilteredEnvironment>(environmentImage, width, height) {

    override fun createProgramExecutor(gl: GlimpseAdapter): ProgramExecutor<PreFilteredEnvironment> {
        val classLoader = javaClass.classLoader
        val shadersFactory = Shader.Factory.newInstance(gl)
        val program = Program.Builder.newInstance(gl)
            .withVertexShader(
                shadersFactory.createShader(
                    type = ShaderType.VERTEX_SHADER,
                    source = classLoader.getResource(RESOURCE_VERTEX_SHADER)?.readText().orEmpty()
                )
            )
            .withFragmentShader(
                shadersFactory.createShader(
                    type = ShaderType.FRAGMENT_SHADER,
                    source = classLoader.getResource(RESOURCE_FRAGMENT_SHADER)?.readText().orEmpty()
                )
            )
            .build()
        return PreFilteredEnvironmentProgramExecutor(program)
    }

    override fun createShaderParams(environmentTexture: Texture): PreFilteredEnvironment =
        PreFilteredEnvironment(environmentTexture, roughness, samples)

    companion object {
        private const val RESOURCE_VERTEX_SHADER = "shaders/pre-filtered_env_vertex_shader.glsl"
        private const val RESOURCE_FRAGMENT_SHADER = "shaders/pre-filtered_env_fragment_shader.glsl"
    }
}
