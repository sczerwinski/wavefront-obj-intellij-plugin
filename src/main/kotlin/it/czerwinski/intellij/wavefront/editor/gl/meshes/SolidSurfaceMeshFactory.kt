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
import graphics.glimpse.geom.freeform.ControlVertex3
import graphics.glimpse.geom.freeform.ControlVertex4
import graphics.glimpse.geom.freeform.FreeformType
import graphics.glimpse.geom.freeform.Surface3
import graphics.glimpse.geom.freeform.Surface4
import graphics.glimpse.meshes.ArrayMeshData
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormDirection
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormTypeValue

object SolidSurfaceMeshFactory : SurfaceMeshFactory {

    override fun create(
        gl: GlimpseAdapter,
        model: GLModel,
        surface: GLModel.Surface,
        freeFormCurveResolution: Int
    ): Mesh? {
        val surfaceType = when (surface.type?.value) {
            ObjFreeFormTypeValue.BEZIER -> FreeformType.BEZIER
            ObjFreeFormTypeValue.BSPLINE -> FreeformType.B_SPLINE
            else -> return null
        }
        val isRationalForm = surface.type.isRationalForm
        val degree = surface.degree?.asVec2 ?: Vec2.nullVector()
        val indices = surface.surface?.freeFormSurfaceDefinition?.faceVertexList.orEmpty()
        val knotsU = surface.getKnots(ObjFreeFormDirection.U)
        val knotsV = surface.getKnots(ObjFreeFormDirection.V)

        val surfaceMeshData = if (!isRationalForm) {
            createSurface3MeshData(
                model,
                surface,
                surfaceType,
                degree,
                indices,
                knotsU,
                knotsV,
                freeFormCurveResolution
            )
        } else {
            createSurface4MeshData(
                model,
                surface,
                surfaceType,
                degree,
                indices,
                knotsU,
                knotsV,
                freeFormCurveResolution
            )
        }

        return Mesh.Factory.newInstance(gl).createMesh(surfaceMeshData)
    }

    @Suppress("LongParameterList")
    private fun createSurface3MeshData(
        model: GLModel,
        surface: GLModel.Surface,
        surfaceType: FreeformType,
        degree: Vec2<Int>,
        indices: List<ObjFaceVertex>,
        knotsU: List<Float>,
        knotsV: List<Float>,
        freeFormCurveResolution: Int
    ): ArrayMeshData {
        val surface3 = Surface3.Builder.getInstance<Float>()
            .ofType(surfaceType)
            .withDegree(degree)
            .withControlVertices(
                indices.map { faceVertex ->
                    ControlVertex3(
                        controlPoint = Vec3.fromList(model.getVertexPosition(faceVertex.vertexIndex)),
                        textureCoordinates = Vec2.fromList(
                            model.getTextureCoordinates(faceVertex.textureCoordinatesIndex)
                        ),
                        normal = Vec3.fromList(model.getVertexNormal(faceVertex.vertexNormalIndex))
                    )
                }
            )
            .withKnotsU(knotsU)
            .withKnotsV(knotsV)
            .build()
        val parametersValues = generateParametersValues(surface, surface3.gridSize, freeFormCurveResolution)
        return surface3.toMeshData(parametersValues.first, parametersValues.second)
    }

    @Suppress("DestructuringDeclarationWithTooManyEntries")
    private fun generateParametersValues(
        surface: GLModel.Surface,
        gridSize: Vec2<Int>,
        freeFormCurveResolution: Int
    ): Pair<List<Float>, List<Float>> {
        val segmentsU = (gridSize.u - 1) * freeFormCurveResolution
        val segmentsV = (gridSize.v - 1) * freeFormCurveResolution

        val (startU, endU, startV, endV) = surface.surface?.freeFormSurfaceDefinition?.floatList
            ?.mapIndexed { index, value -> value.floatValue ?: (index.toFloat() % 2) }
            ?: listOf(0f, 1f)

        val parameterValuesU = (0 until segmentsU).map { startU + it * (endU - startU) / segmentsU } + endU
        val parameterValuesV = (0 until segmentsV).map { startV + it * (endV - startV) / segmentsV } + endV

        return parameterValuesU to parameterValuesV
    }

    @Suppress("LongParameterList")
    private fun createSurface4MeshData(
        model: GLModel,
        surface: GLModel.Surface,
        surfaceType: FreeformType,
        degree: Vec2<Int>,
        indices: List<ObjFaceVertex>,
        knotsU: List<Float>,
        knotsV: List<Float>,
        freeFormCurveResolution: Int
    ): ArrayMeshData {
        val surface4 = Surface4.Builder.getInstance<Float>()
            .ofType(surfaceType)
            .withDegree(degree)
            .withControlVertices(
                indices.map { faceVertex ->
                    ControlVertex4(
                        controlPoint = Vec4.fromList(model.getRationalVertexPosition(faceVertex.vertexIndex)),
                        textureCoordinates = Vec2.fromList(
                            model.getTextureCoordinates(faceVertex.textureCoordinatesIndex)
                        ),
                        normal = Vec3.fromList(model.getVertexNormal(faceVertex.vertexNormalIndex))
                    )
                }
            )
            .withKnotsU(knotsU)
            .withKnotsV(knotsV)
            .build()
        val parametersValues = generateParametersValues(surface, surface4.gridSize, freeFormCurveResolution)
        return surface4.toMeshData(parametersValues.first, parametersValues.second)
    }
}
