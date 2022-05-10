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
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

/**
 * GL model meshes manager.
 */
class ModelMeshesManager {

    private val myFacesMeshes = mutableListOf<Mesh>()
    private val myLinesMeshes = mutableListOf<Mesh>()
    private val myPointsMeshes = mutableListOf<Mesh>()

    val facesMeshes: List<Mesh> get() = myFacesMeshes
    val linesMeshes: List<Mesh> get() = myLinesMeshes
    val pointsMeshes: List<Mesh> get() = myPointsMeshes

    /**
     * Initializes meshes for given [model] and [shadingMethod].
     */
    fun initialize(gl: GlimpseAdapter, model: GLModel, shadingMethod: ShadingMethod) {
        dispose(gl)
        createFacesMeshes(gl, model, shadingMethod)
        createLinesMeshes(gl, model)
        createPointsMeshes(gl, model)
    }

    private fun createFacesMeshes(gl: GlimpseAdapter, model: GLModel, shadingMethod: ShadingMethod) {
        val facesMeshFactory = when (shadingMethod) {
            ShadingMethod.WIREFRAME -> WireframeFacesMeshFactory
            ShadingMethod.SOLID,
            ShadingMethod.MATERIAL,
            ShadingMethod.PBR -> SolidFacesMeshFactory
        }
        myFacesMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    facesMeshFactory.create(gl, model, part)
                }
            }
        )
    }

    private fun createLinesMeshes(gl: GlimpseAdapter, model: GLModel) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        myLinesMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    val linesPositionsData = part.lines.flatMap { line ->
                        line.lineVertexList
                            .map { it.vertexIndex }
                            .zipWithNext()
                            .flatMap { (index1, index2) ->
                                model.vertices[(index1.value ?: 1) - 1].coordinates.map { it ?: 0f } +
                                    model.vertices[(index2.value ?: 1) - 1].coordinates.map { it ?: 0f }
                            }
                    }.toFloatBufferData()
                    val linesTextureCoordinatesData = part.lines.flatMap { line ->
                        line.lineVertexList
                            .map { it.textureCoordinatesIndex }
                            .zipWithNext()
                            .flatMap { (index1, index2) ->
                                model.textureCoordinates[(index1?.value ?: 1) - 1].coordinates.map { it ?: 0f } +
                                    model.textureCoordinates[(index2?.value ?: 1) - 1].coordinates.map { it ?: 0f }
                            }
                    }.toFloatBufferData()
                    LinesMesh(
                        vertexCount = part.lines.sumOf { line -> (line.lineVertexList.size - 1) * 2 },
                        buffers = bufferFactory.createArrayBuffers(linesPositionsData, linesTextureCoordinatesData)
                    )
                }
            }
        )
    }

    private fun createPointsMeshes(gl: GlimpseAdapter, model: GLModel) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        myPointsMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    val pointsPositionsData = part.points.flatMap { point ->
                        model.vertices[(point.vertexIndex.value ?: 1) - 1].coordinates.map { it ?: 0f }
                    }.toFloatBufferData()
                    PointsMesh(
                        vertexCount = part.points.size,
                        buffers = bufferFactory.createArrayBuffers(pointsPositionsData)
                    )
                }
            }
        )
    }

    /**
     * Disposes all previously created meshes.
     */
    fun dispose(gl: GlimpseAdapter) {
        (myFacesMeshes + myLinesMeshes + myPointsMeshes)
            .forEach { mesh -> mesh.dispose(gl) }
        myFacesMeshes.clear()
        myLinesMeshes.clear()
        myPointsMeshes.clear()
    }
}
