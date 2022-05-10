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

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.meshes.MeshDataBuilder
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3

object TextMeshFactory {

    private const val FONT_COLS = 16
    private const val FONT_ROWS = 8
    private const val CHAR_MAX = 127
    private const val VERTICES_PER_BORDER = 2

    fun createText(
        gl: GlimpseAdapter,
        text: String,
        align: Align = Align.CENTER,
        verticalAlign: VerticalAlign = VerticalAlign.MIDDLE
    ): Mesh {
        val textLength = text.length
        val meshDataBuilder = MeshDataBuilder()
        for (index in 0..textLength) {
            meshDataBuilder.addVertex(
                Vec3(x = index.toFloat() - textLength * align.shiftX, y = verticalAlign.topY, z = 0f)
            )
            meshDataBuilder.addVertex(
                Vec3(x = index.toFloat() - textLength * align.shiftX, y = verticalAlign.bottomY, z = 0f)
            )
        }
        for (row in 0..FONT_ROWS) {
            for (col in 0..FONT_COLS) {
                meshDataBuilder.addTextureCoordinates(
                    Vec2(x = col.toFloat() / FONT_COLS, y = 1f - row.toFloat() / FONT_ROWS)
                )
            }
        }
        meshDataBuilder.addNormal(Vec3.nullVector)
        for ((index, char) in text.withIndex()) {
            val charPosition = char.code
            val charRow = charPosition / (FONT_COLS)
            require(value = charPosition in 0..CHAR_MAX) { "Not an ASCII character: $char" }
            val firstVertexIndex = index * VERTICES_PER_BORDER
            meshDataBuilder.addFace(
                listOf(
                    MeshDataBuilder.FaceVertex(
                        positionIndex = firstVertexIndex,
                        texCoordIndex = charPosition + charRow,
                        normalIndex = 0
                    ),
                    MeshDataBuilder.FaceVertex(
                        positionIndex = firstVertexIndex + VERTICES_PER_BORDER,
                        texCoordIndex = charPosition + charRow + 1,
                        normalIndex = 0
                    ),
                    MeshDataBuilder.FaceVertex(
                        positionIndex = firstVertexIndex + VERTICES_PER_BORDER + 1,
                        texCoordIndex = charPosition + charRow + FONT_COLS + 2,
                        normalIndex = 0
                    ),
                    MeshDataBuilder.FaceVertex(
                        positionIndex = firstVertexIndex + 1,
                        texCoordIndex = charPosition + charRow + FONT_COLS + 1,
                        normalIndex = 0
                    )
                )
            )
        }
        val meshData = meshDataBuilder.buildArrayMeshData()
        return Mesh.Factory.newInstance(gl).createMesh(meshData)
    }

    enum class Align(internal val shiftX: Float) {
        LEFT(shiftX = 0f),
        CENTER(shiftX = 0.5f),
        RIGHT(shiftX = 1f)
    }

    enum class VerticalAlign(internal val topY: Float, internal val bottomY: Float) {
        TOP(topY = 0f, bottomY = -2f),
        MIDDLE(topY = 1f, bottomY = -1f),
        BOTTOM(topY = 2f, bottomY = 0f)
    }
}
