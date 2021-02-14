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

package it.czerwinski.intellij.wavefront.editor.gl.meshes

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.buffers.Buffer
import graphics.glimpse.buffers.toFloatBufferData
import graphics.glimpse.meshes.Mesh
import it.czerwinski.intellij.wavefront.editor.model.GLModel

object WireframeFacesMeshFactory : FacesMeshFactory {

    private const val VERTICES_PER_LINE = 2

    override fun create(gl: GlimpseAdapter, model: GLModel, materialPart: GLModel.MaterialPart): Mesh {
        val lines = materialPart.faces.flatMap { face ->
            (face.faceVertexList + face.faceVertexList.first())
                .zipWithNext()
                .map { (v1, v2) ->
                    v1.vertexIndex to v2.vertexIndex
                }
        }.distinct()
        val positionsData = lines.flatMap { (index1, index2) ->
            (model.vertices[(index1.value ?: 1) - 1].coordinates + model.vertices[(index2.value ?: 1) - 1].coordinates)
                .map { it ?: 0f }
        }.toFloatBufferData()
        val bufferFactory = Buffer.Factory.newInstance(gl)
        return LinesMesh(
            vertexCount = lines.size * VERTICES_PER_LINE,
            buffers = bufferFactory.createArrayBuffers(positionsData)
        )
    }
}
