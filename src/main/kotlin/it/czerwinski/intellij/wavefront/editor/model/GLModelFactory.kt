/*
 * Copyright 2020 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.editor.model

import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroupingElement
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

object GLModelFactory {

    fun create(objFile: ObjFile): GLModel =
        GLModel(
            vertices = getAllVertices(objFile),
            textureCoordinates = getAllTextureCoordinates(objFile),
            vertexNormals = getAllVertexNormals(objFile),
            faces = getAllFaces(objFile)
        )

    private fun getAllVertices(file: PsiFile): List<ObjVertex> =
        file.getChildrenOfType<ObjVertex>() +
            file.getChildrenOfType<ObjGroupingElement>()
                .flatMap { it.getChildrenOfType<ObjVertex>() }

    private fun getAllTextureCoordinates(file: PsiFile): List<ObjTextureCoordinates> =
        file.getChildrenOfType<ObjTextureCoordinates>() +
            file.getChildrenOfType<ObjGroupingElement>()
                .flatMap { it.getChildrenOfType<ObjTextureCoordinates>() }

    private fun getAllVertexNormals(file: PsiFile): List<ObjVertexNormal> =
        file.getChildrenOfType<ObjVertexNormal>() +
            file.getChildrenOfType<ObjGroupingElement>()
                .flatMap { it.getChildrenOfType<ObjVertexNormal>() }

    private fun getAllFaces(file: PsiFile): List<ObjFace> =
        file.getChildrenOfType<ObjFace>() +
            file.getChildrenOfType<ObjGroupingElement>()
                .flatMap { it.getChildrenOfType<ObjFace>() }
}
