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
import graphics.glimpse.geom.freeform.Curve3
import graphics.glimpse.geom.freeform.Curve4
import graphics.glimpse.geom.freeform.FreeformType
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormDirection
import it.czerwinski.intellij.wavefront.lang.psi.ObjFreeFormTypeValue
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex

object CurveMeshFactory {

    fun create(
        gl: GlimpseAdapter,
        model: GLModel,
        curve: GLModel.Curve,
        freeFormCurveResolution: Int
    ): Mesh? {
        val curveType = when (curve.type?.value) {
            ObjFreeFormTypeValue.BEZIER -> FreeformType.BEZIER
            ObjFreeFormTypeValue.BSPLINE -> FreeformType.B_SPLINE
            else -> return null
        }
        val isRationalForm = curve.type.isRationalForm
        val controlPointsIndices = curve.curve?.freeFormCurveDefinition?.vertexIndexList.orEmpty()
        val knots = curve.getKnots(ObjFreeFormDirection.U)
        val parameterValues = generateParameterValues(curve, controlPointsIndices.size, freeFormCurveResolution)

        val vertices = if (!isRationalForm) {
            createCurve3Vertices(model, curveType, controlPointsIndices, knots, parameterValues)
        } else {
            createCurve4Vertices(model, curveType, controlPointsIndices, knots, parameterValues)
        }.zipWithNext { a, b ->
            listOf(a, b)
        }.flatten()

        val linesPositionsData = vertices.flatMap { it.toList() }.toFloatBufferData()
        val linesTextureCoordinatesData = FloatArray(size = vertices.size * 2).toFloatBufferData()

        return LinesMesh(
            vertexCount = vertices.size,
            buffers = Buffer.Factory.newInstance(gl)
                .createArrayBuffers(linesPositionsData, linesTextureCoordinatesData)
        )
    }

    private fun generateParameterValues(
        curve: GLModel.Curve,
        controlPointsSize: Int,
        freeFormCurveResolution: Int
    ): Sequence<Float> {
        val segments = (controlPointsSize - 1) * freeFormCurveResolution
        val (start, end) = curve.curve?.freeFormCurveDefinition?.floatList
            ?.mapIndexed { index, value -> value.floatValue ?: index.toFloat() }
            ?: listOf(0f, 1f)
        return (0 until segments).asSequence().map { start + it * (end - start) / segments } + end
    }

    private fun createCurve3Vertices(
        model: GLModel,
        curveType: FreeformType,
        controlPointsIndices: List<ObjVertexIndex>,
        knots: List<Float>,
        parameterValues: Sequence<Float>
    ): List<Vec3<Float>> =
        Curve3.Builder.getInstance<Float>()
            .ofType(curveType)
            .withControlPoints(controlPointsIndices.map { index -> Vec3.fromList(model.getVertexPosition(index)) })
            .withKnots(knots)
            .build()
            .toPolygonalChain(parameterValues)
            .vertices

    private fun createCurve4Vertices(
        model: GLModel,
        curveType: FreeformType,
        controlPointsIndices: List<ObjVertexIndex>,
        knots: List<Float>,
        parameterValues: Sequence<Float>
    ): List<Vec3<Float>> =
        Curve4.Builder.getInstance<Float>()
            .ofType(curveType)
            .withControlPoints(
                controlPointsIndices.map { index -> Vec4.fromList(model.getRationalVertexPosition(index)) }
            )
            .withKnots(knots)
            .build()
            .toPolygonalChain(parameterValues)
            .vertices
            .map { it.toNonRationalForm() }
}
