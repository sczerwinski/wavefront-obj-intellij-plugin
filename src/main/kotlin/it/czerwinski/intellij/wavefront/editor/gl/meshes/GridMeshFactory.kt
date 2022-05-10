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

package it.czerwinski.intellij.wavefront.editor.gl.meshes

import com.jogamp.opengl.math.FloatUtil
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.buffers.Buffer
import graphics.glimpse.buffers.FloatBufferData
import graphics.glimpse.buffers.toFloatBufferData
import graphics.glimpse.meshes.Mesh
import kotlin.math.floor
import kotlin.math.log10

object GridMeshFactory {

    private const val GRID_STEPS = 10
    private const val GRID_VERTEX_COUNT = (GRID_STEPS * 2 + 1) * 4
    private const val GRID_SIZE = 10f

    private val gridPositionsData: FloatBufferData by lazy {
        (-GRID_STEPS..GRID_STEPS).flatMap { index ->
            val position = index.toFloat()
            listOf(
                -GRID_SIZE, position, 0f, GRID_SIZE, position, 0f,
                position, -GRID_SIZE, 0f, position, GRID_SIZE, 0f,
            )
        }.toFloatBufferData()
    }

    private const val FINE_GRID_VERTEX_COUNT = GRID_STEPS * (GRID_STEPS - 1) * 8

    private val fineGridPositionsData: FloatBufferData by lazy {
        (-GRID_STEPS until GRID_STEPS).flatMap { index ->
            (1 until GRID_STEPS).flatMap { fineIndex ->
                val position = index.toFloat() + fineIndex.toFloat() / GRID_STEPS
                listOf(
                    -GRID_SIZE, position, 0f, GRID_SIZE, position, 0f,
                    position, -GRID_SIZE, 0f, position, GRID_SIZE, 0f,
                )
            }
        }.toFloatBufferData()
    }

    fun createGrid(gl: GlimpseAdapter): Mesh {
        val bufferFactory = Buffer.Factory.newInstance(gl)

        return LinesMesh(
            vertexCount = GRID_VERTEX_COUNT,
            buffers = bufferFactory.createArrayBuffers(gridPositionsData)
        )
    }

    fun createFineGrid(gl: GlimpseAdapter): Mesh {
        val bufferFactory = Buffer.Factory.newInstance(gl)

        return LinesMesh(
            vertexCount = FINE_GRID_VERTEX_COUNT,
            buffers = bufferFactory.createArrayBuffers(fineGridPositionsData)
        )
    }

    fun calculateGridScale(modelSize: Float): Float =
        FloatUtil.pow(GRID_STEPS.toFloat(), floor(log10(modelSize)))
}
