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

import graphics.glimpse.ClearableBufferType
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.createImage
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.offscreen.OffscreenRenderer
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.ProgramExecutor
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import graphics.glimpse.types.Vec4
import java.awt.Image

class BRDFRenderer(
    private val samples: Int,
    override val width: Int,
    override val height: Int
) : OffscreenRenderer(), MapRenderer {

    override var outputImage: Image? = null
        private set

    override fun doRender(gl: GlimpseAdapter) {
        gl.glClearColor(Vec4(x = 0f, y = 0f, z = 0f, w = 0f))
        gl.glClearDepth(depth = 1f)
        gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)

        val programExecutor = createProgramExecutor(gl)
        val mesh = Mesh.Factory.newInstance(gl).createMesh(BaseMapRenderer.quad)

        val shaderParams = BRDF(samples)

        programExecutor.useProgram(gl)
        programExecutor.applyParams(gl, shaderParams)
        programExecutor.drawMesh(gl, mesh)

        outputImage = gl.createImage(width = width, height = height)

        mesh.dispose(gl)
        programExecutor.dispose(gl)
    }

    private fun createProgramExecutor(gl: GlimpseAdapter): ProgramExecutor<BRDF> {
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
        return BRDFProgramExecutor(program)
    }

    companion object {
        private const val RESOURCE_VERTEX_SHADER = "shaders/brdf_vertex_shader.glsl"
        private const val RESOURCE_FRAGMENT_SHADER = "shaders/brdf_fragment_shader.glsl"
    }
}
