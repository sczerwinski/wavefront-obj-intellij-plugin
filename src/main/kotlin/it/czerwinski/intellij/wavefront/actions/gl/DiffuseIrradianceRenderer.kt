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

import com.intellij.openapi.application.runReadAction
import graphics.glimpse.ClearableBufferType
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.buffers.toFloatBufferData
import graphics.glimpse.createImage
import graphics.glimpse.meshes.ArrayMeshData
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.offscreen.OffscreenRenderer
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.TextureWrap
import graphics.glimpse.textures.fromBufferedImage
import graphics.glimpse.types.Vec4
import java.awt.Image
import java.awt.image.BufferedImage

class DiffuseIrradianceRenderer(
    private val environmentImage: BufferedImage,
    private val samples: Int,
    override val width: Int,
    override val height: Int
) : OffscreenRenderer() {

    var outputImage: Image? = null
        private set

    override fun doRender(gl: GlimpseAdapter) {
        gl.glClearColor(Vec4(x = 0f, y = 0f, z = 0f, w = 0f))
        gl.glClearDepth(depth = 1f)
        gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)

        val programExecutor = createProgramExecutor(gl)
        val mesh = Mesh.Factory.newInstance(gl).createMesh(quad)
        val environmentTexture = createTexture(gl)

        val shaderParams = DiffuseIrradiance(environmentTexture, samples = samples)

        programExecutor.useProgram(gl)
        programExecutor.applyParams(gl, shaderParams)
        programExecutor.drawMesh(gl, mesh)

        outputImage = gl.createImage(width = width, height = height)

        environmentTexture.dispose(gl)
        mesh.dispose(gl)
        programExecutor.dispose(gl)
    }

    private fun createProgramExecutor(gl: GlimpseAdapter): DiffuseIrradianceProgramExecutor {
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
        return DiffuseIrradianceProgramExecutor(program)
    }

    private fun createTexture(gl: GlimpseAdapter) = runReadAction {
        Texture.Builder.getInstance(gl)
            .addTexture(
                TextureImageSource.builder()
                    .fromBufferedImage(environmentImage)
                    .build()
            )
            .setTextureWrap(TextureWrap.REPEAT, TextureWrap.REPEAT)
            .build()
            .first()
    }

    companion object {
        private const val RESOURCE_VERTEX_SHADER = "shaders/diffuse_irradiance_vertex_shader.glsl"
        private const val RESOURCE_FRAGMENT_SHADER = "shaders/diffuse_irradiance_fragment_shader.glsl"

        private val quad = ArrayMeshData(
            vertexCount = 6,
            positionsData = listOf(-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f).toFloatBufferData(),
            texCoordsData = listOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 1f, 0f).toFloatBufferData(),
            normalsData = emptyList<Float>().toFloatBufferData(),
            tangentsData = emptyList<Float>().toFloatBufferData(),
            bitangentsData = emptyList<Float>().toFloatBufferData()
        )
    }
}
