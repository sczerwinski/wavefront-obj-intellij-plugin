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
import graphics.glimpse.meshes.ArrayMeshData
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.meshes.obj.ObjMeshDataParser
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh

class MtlPreviewMeshesManager {

    private val meshesData = mutableMapOf<MaterialPreviewMesh, ArrayMeshData>()

    private val meshes = mutableMapOf<MaterialPreviewMesh, Mesh>()

    fun prepare() {
        val objMeshDataParser = ObjMeshDataParser()
        for (previewMesh in enumValues<MaterialPreviewMesh>()) {
            val lines = javaClass.classLoader.getResourceAsStream(getMeshResourceName(previewMesh))
                ?.use { inputStream -> inputStream.bufferedReader().readLines() }
            meshesData[previewMesh] = objMeshDataParser.parseArrayMeshData(lines.orEmpty())
        }
    }

    fun initialize(gl: GlimpseAdapter) {
        val factory = Mesh.Factory.newInstance(gl)
        for (previewMesh in enumValues<MaterialPreviewMesh>()) {
            val meshData = meshesData[previewMesh]
            if (meshData != null) {
                meshes[previewMesh] = factory.createMesh(meshData)
            }
        }
    }

    private fun getMeshResourceName(previewMesh: MaterialPreviewMesh): String =
        "objects/${previewMesh.name.lowercase()}.obj"

    fun getMesh(previewMesh: MaterialPreviewMesh): Mesh? = meshes[previewMesh]

    fun dispose(gl: GlimpseAdapter) {
        for (mesh in meshes.values) {
            mesh.dispose(gl)
        }
    }
}
