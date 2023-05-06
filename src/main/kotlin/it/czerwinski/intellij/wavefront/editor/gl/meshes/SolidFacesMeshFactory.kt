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
import it.czerwinski.intellij.wavefront.editor.model.GLModel

object SolidFacesMeshFactory : FacesMeshFactory {

    override fun create(gl: GlimpseAdapter, model: GLModel, materialPart: GLModel.MaterialPart): Mesh {
        val meshDataBuilder = MeshDataBuilder()
        for (vertex in model.vertices) {
            meshDataBuilder.addVertex(vertex.asVec3)
        }
        for (texCoord in model.textureCoordinates) {
            meshDataBuilder.addTextureCoordinates(texCoord.asVec2)
        }
        for (normal in model.vertexNormals) {
            meshDataBuilder.addNormal(normal.asVec3)
        }
        meshDataBuilder.addTextureCoordinates(Vec2(x = 0f, y = 0f))
        meshDataBuilder.addNormal(Vec3.nullVector())
        for (face in materialPart.faces) {
            meshDataBuilder.addFace(
                face.faceVertexList.map { faceVertex ->
                    MeshDataBuilder.FaceVertex(
                        positionIndex = faceVertex.vertexIndex.asListIndex(),
                        texCoordIndex = faceVertex.textureCoordinatesIndex?.asListIndex() ?: 0,
                        normalIndex = faceVertex.vertexNormalIndex?.asListIndex() ?: 0
                    )
                }
            )
        }
        val meshData = meshDataBuilder.buildArrayMeshData()
        return Mesh.Factory.newInstance(gl).createMesh(meshData)
    }
}
