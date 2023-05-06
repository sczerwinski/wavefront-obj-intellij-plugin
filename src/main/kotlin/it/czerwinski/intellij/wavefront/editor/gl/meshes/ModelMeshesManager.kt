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
    private val myCurvesMeshes = mutableListOf<Mesh>()
    private val mySurfacesMeshes = mutableListOf<Mesh>()

    val facesMeshes: List<Mesh> get() = myFacesMeshes
    val linesMeshes: List<Mesh> get() = myLinesMeshes
    val pointsMeshes: List<Mesh> get() = myPointsMeshes
    val curvesMeshes: List<Mesh> get() = myCurvesMeshes
    val surfacesMeshes: List<Mesh> get() = mySurfacesMeshes

    /**
     * Initializes meshes for given [model] and [shadingMethod].
     */
    fun initialize(
        gl: GlimpseAdapter,
        model: GLModel,
        shadingMethod: ShadingMethod,
        freeFormCurveResolution: Int
    ) {
        dispose(gl)
        createFacesMeshes(gl, model, shadingMethod)
        createLinesMeshes(gl, model)
        createPointsMeshes(gl, model)
        createCurvesMeshes(gl, model, freeFormCurveResolution)
        createSurfacesMeshes(gl, model, shadingMethod, freeFormCurveResolution)
    }

    private fun createFacesMeshes(gl: GlimpseAdapter, model: GLModel, shadingMethod: ShadingMethod) {
        val facesMeshFactory = when (shadingMethod) {
            ShadingMethod.WIREFRAME -> WireframeFacesMeshFactory
            ShadingMethod.SOLID,
            ShadingMethod.MATERIAL,
            ShadingMethod.PBR -> SolidFacesMeshFactory
        }
        myFacesMeshes += model.groupingElements.flatMap { element ->
            element.materialParts.map { part ->
                facesMeshFactory.create(gl, model, part)
            }
        }
    }

    private fun createLinesMeshes(gl: GlimpseAdapter, model: GLModel) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        myLinesMeshes += model.groupingElements.flatMap { element ->
            element.materialParts.map { part ->
                val linesPositionsData = part.lines.flatMap { line ->
                    line.lineVertexList
                        .map { it.vertexIndex }
                        .zipWithNext()
                        .flatMap { (index1, index2) ->
                            model.getVertexPosition(index1) + model.getVertexPosition(index2)
                        }
                }.toFloatBufferData()
                val linesTextureCoordinatesData = part.lines.flatMap { line ->
                    line.lineVertexList
                        .map { it.textureCoordinatesIndex }
                        .zipWithNext()
                        .flatMap { (index1, index2) ->
                            model.getTextureCoordinates(index1) + model.getTextureCoordinates(index2)
                        }
                }.toFloatBufferData()
                LinesMesh(
                    vertexCount = part.lines.sumOf { line -> (line.lineVertexList.size - 1) * 2 },
                    buffers = bufferFactory.createArrayBuffers(linesPositionsData, linesTextureCoordinatesData)
                )
            }
        }
    }

    private fun createPointsMeshes(gl: GlimpseAdapter, model: GLModel) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        myPointsMeshes += model.groupingElements.flatMap { element ->
            element.materialParts.map { part ->
                val pointsPositionsData = part.points.flatMap { point ->
                    model.getVertexPosition(point.vertexIndex)
                }.toFloatBufferData()
                PointsMesh(
                    vertexCount = part.points.size,
                    buffers = bufferFactory.createArrayBuffers(pointsPositionsData)
                )
            }
        }
    }

    private fun createCurvesMeshes(gl: GlimpseAdapter, model: GLModel, freeFormCurveResolution: Int) {
        myCurvesMeshes += model.groupingElements.flatMap { element ->
            element.materialParts.flatMap { part ->
                part.curves.mapNotNull { curve ->
                    CurveMeshFactory.create(gl, model, curve, freeFormCurveResolution)
                }
            }
        }
    }

    private fun createSurfacesMeshes(
        gl: GlimpseAdapter,
        model: GLModel,
        shadingMethod: ShadingMethod,
        freeFormCurveResolution: Int
    ) {
        val surfaceMeshFactory = when (shadingMethod) {
            ShadingMethod.WIREFRAME -> WireframeSurfaceMeshFactory
            ShadingMethod.SOLID,
            ShadingMethod.MATERIAL,
            ShadingMethod.PBR -> SolidSurfaceMeshFactory
        }
        mySurfacesMeshes += model.groupingElements.flatMap { element ->
            element.materialParts.flatMap { part ->
                part.surfaces.mapNotNull { surface ->
                    surfaceMeshFactory.create(gl, model, surface, freeFormCurveResolution)
                }
            }
        }
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
        myCurvesMeshes.clear()
        mySurfacesMeshes.clear()
    }
}
