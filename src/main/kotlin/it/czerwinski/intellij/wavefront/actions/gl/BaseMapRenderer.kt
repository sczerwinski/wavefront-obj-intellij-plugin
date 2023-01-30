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
import graphics.glimpse.shaders.ProgramExecutor
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureImageSource
import graphics.glimpse.textures.TextureWrap
import graphics.glimpse.textures.fromBufferedImage
import graphics.glimpse.types.Vec4
import java.awt.Image
import java.awt.image.BufferedImage

abstract class BaseMapRenderer<T>(
    private val environmentImage: BufferedImage,
    override val width: Int,
    override val height: Int
) : OffscreenRenderer() {

    var outputImage: Image? = null
        protected set

    override fun doRender(gl: GlimpseAdapter) {
        gl.glClearColor(Vec4(x = 0f, y = 0f, z = 0f, w = 0f))
        gl.glClearDepth(depth = 1f)
        gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)

        val programExecutor = createProgramExecutor(gl)
        val mesh = Mesh.Factory.newInstance(gl).createMesh(quad)
        val environmentTexture = createTexture(gl)

        val shaderParams = createShaderParams(environmentTexture)

        programExecutor.useProgram(gl)
        programExecutor.applyParams(gl, shaderParams)
        programExecutor.drawMesh(gl, mesh)

        outputImage = gl.createImage(width = width, height = height)

        environmentTexture.dispose(gl)
        mesh.dispose(gl)
        programExecutor.dispose(gl)
    }

    protected abstract fun createProgramExecutor(gl: GlimpseAdapter): ProgramExecutor<T>

    protected abstract fun createShaderParams(environmentTexture: Texture): T

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

        internal val quad = ArrayMeshData(
            vertexCount = 6,
            positionsData = listOf(-1f, -1f, -1f, 1f, 1f, 1f, -1f, -1f, 1f, 1f, 1f, -1f).toFloatBufferData(),
            texCoordsData = listOf(0f, 0f, 0f, 1f, 1f, 1f, 0f, 0f, 1f, 1f, 1f, 0f).toFloatBufferData(),
            normalsData = emptyList<Float>().toFloatBufferData(),
            tangentsData = emptyList<Float>().toFloatBufferData(),
            bitangentsData = emptyList<Float>().toFloatBufferData()
        )
    }
}
