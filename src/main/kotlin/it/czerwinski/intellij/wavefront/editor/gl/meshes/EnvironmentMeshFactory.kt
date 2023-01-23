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
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.meshes.MeshDataBuilder
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3

object EnvironmentMeshFactory {

    private val vertices: List<Vec3> = listOf(
        Vec3(x = -1f, y = -1f, z = 1f),
        Vec3(x = 1f, y = -1f, z = 1f),
        Vec3(x = 1f, y = 1f, z = 1f),
        Vec3(x = -1f, y = 1f, z = 1f),
        Vec3(x = -1f, y = -1f, z = -1f),
        Vec3(x = 1f, y = -1f, z = -1f),
        Vec3(x = 1f, y = 1f, z = -1f),
        Vec3(x = -1f, y = 1f, z = -1f),
    )

    @Suppress("MagicNumber")
    private val faces: List<List<Int>> = listOf(
        listOf(1, 0, 4, 5),
        listOf(2, 1, 5, 6),
        listOf(3, 2, 6, 7),
        listOf(0, 3, 7, 4),
        listOf(0, 1, 2, 3),
        listOf(7, 6, 5, 4),
    )

    fun create(gl: GlimpseAdapter): Mesh {
        val meshDataBuilder = MeshDataBuilder()
        for (vertex in vertices) {
            meshDataBuilder.addVertex(vertex)
        }
        meshDataBuilder.addTextureCoordinates(Vec2(x = 0f, y = 0f))
        meshDataBuilder.addNormal(Vec3.nullVector)
        for (face in faces) {
            meshDataBuilder.addFace(
                face.map { positionIndex ->
                    MeshDataBuilder.FaceVertex(positionIndex, texCoordIndex = 0, normalIndex = 0)
                }
            )
        }
        val meshData = meshDataBuilder.buildArrayMeshData()
        return Mesh.Factory.newInstance(gl).createMesh(meshData)
    }
}
