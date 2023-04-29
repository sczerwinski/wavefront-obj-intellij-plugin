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

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.buffers.Buffer
import graphics.glimpse.buffers.FloatBufferData
import graphics.glimpse.buffers.toFloatBufferData
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.cos
import graphics.glimpse.types.rotationX
import graphics.glimpse.types.rotationY
import graphics.glimpse.types.sin

object AxisMeshFactory {

    private const val AXIS_LENGTH = 1f
    private const val AXIS_VERTEX_COUNT = 2

    private val axisPositionsData: FloatBufferData by lazy {
        floatArrayOf(0f, 0f, -AXIS_LENGTH, 0f, 0f, AXIS_LENGTH).toFloatBufferData()
    }

    private const val AXIS_CONE_LENGTH = 0.05f
    private const val AXIS_CONE_RADIUS = 0.02f
    private const val AXIS_CONE_SEGMENTS = 12
    private const val AXIS_CONE_VERTEX_COUNT = AXIS_CONE_SEGMENTS + 2

    private val axisConePositionsData: FloatBufferData by lazy {
        val apex = listOf(0f, 0f, AXIS_LENGTH + AXIS_CONE_LENGTH)
        val base = (0..AXIS_CONE_SEGMENTS).flatMap { index ->
            val angle = Angle.fullAngle<Float>() / AXIS_CONE_SEGMENTS.toFloat() * index.toFloat()
            listOf(AXIS_CONE_RADIUS * cos(angle), AXIS_CONE_RADIUS * sin(angle), AXIS_LENGTH - AXIS_CONE_LENGTH)
        }
        (apex + base).toFloatBufferData()
    }

    internal val xAxisModelMatrix: Mat4<Float> = rotationY(Angle.rightAngle())
    internal val yAxisModelMatrix: Mat4<Float> = rotationX(-Angle.rightAngle<Float>())
    internal val zAxisModelMatrix: Mat4<Float> = Mat4.identity()

    fun createAxis(gl: GlimpseAdapter): Mesh {
        val bufferFactory = Buffer.Factory.newInstance(gl)

        return LinesMesh(
            vertexCount = AXIS_VERTEX_COUNT,
            buffers = bufferFactory.createArrayBuffers(axisPositionsData)
        )
    }

    fun createAxisCone(gl: GlimpseAdapter): Mesh {
        val bufferFactory = Buffer.Factory.newInstance(gl)

        return TriangleFanMesh(
            vertexCount = AXIS_CONE_VERTEX_COUNT,
            buffers = bufferFactory.createArrayBuffers(axisConePositionsData)
        )
    }
}
