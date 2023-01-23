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

package it.czerwinski.intellij.wavefront.editor.gl.meshes

import graphics.glimpse.DrawingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.buffers.Buffer
import graphics.glimpse.meshes.Mesh

class TriangleFanMesh(
    override val vertexCount: Int,
    override val buffers: List<Buffer>
) : Mesh {

    override var isDisposed: Boolean = false
        private set

    override fun useBuffer(gl: GlimpseAdapter, bufferIndex: Int) {
        buffers[bufferIndex].use(gl)
    }

    override fun draw(gl: GlimpseAdapter) {
        gl.glDrawArrays(DrawingMode.TRIANGLE_FAN, vertexCount)
    }

    override fun dispose(gl: GlimpseAdapter) {
        gl.glDeleteBuffers(buffers.map { it.handle }.toIntArray())
        isDisposed = true
    }
}
