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

@file:Suppress("TooManyFunctions")

package it.czerwinski.intellij.wavefront.editor.gl

import com.jogamp.opengl.GL2
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex

private const val ZERO = 0f
private const val INDEX_X = 0
private const val INDEX_Y = 1
private const val INDEX_Z = 2
private const val INDEX_W = 3
private const val INDEX_TU = 0
private const val INDEX_TV = 1
private const val INDEX_TW = 2

fun GL2.glFaces(model: GLModel) {
    glTriangles(model)
    glQuads(model)
    glPolygons(model)
}

fun GL2.glTriangles(model: GLModel) {
    glBegin(GL2.GL_TRIANGLES)
    glFaceVertices(model, model.triangles)
    glEnd()
}

fun GL2.glQuads(model: GLModel) {
    glBegin(GL2.GL_QUADS)
    glFaceVertices(model, model.quads)
    glEnd()
}

fun GL2.glFaceVertices(model: GLModel, faces: List<ObjFace>) {
    for (faceVertex in faces.flatMap { face -> face.faceVertexList }) {
        glFaceVertex(model, faceVertex)
    }
}

fun GL2.glPolygons(model: GLModel) {
    for (face in model.polygons) {
        glPolygon(model, face)
    }
}

fun GL2.glPolygon(model: GLModel, face: ObjFace) {
    glBegin(GL2.GL_POLYGON)
    glFaceVertices(model, face)
    glEnd()
}

fun GL2.glFaceVertices(model: GLModel, face: ObjFace) {
    for (faceVertex in face.faceVertexList) {
        glFaceVertex(model, faceVertex)
    }
}

fun GL2.glFaceVertex(model: GLModel, faceVertex: ObjFaceVertex) {
    glNormal(model, faceVertex.vertexNormalIndex)
    glTexCoord(model, faceVertex.textureCoordinatesIndex)
    glVertex(model, faceVertex.vertexIndex)
}

fun GL2.glNormal(model: GLModel, vertexNormalIndex: ObjVertexNormalIndex?) {
    val vertex = model.vertexNormalAtIndex(vertexNormalIndex)
    if (vertex != null) glNormal(vertex)
}

fun GL2.glNormal(vertexNormal: ObjVertexNormal) {
    val coordinates = vertexNormal.coordinates
    val x = coordinates.getOrNull(INDEX_X) ?: ZERO
    val y = coordinates.getOrNull(INDEX_Y) ?: ZERO
    val z = coordinates.getOrNull(INDEX_Z) ?: ZERO
    glNormal3f(x, y, z)
}

fun GL2.glTexCoord(model: GLModel, textureCoordinatesIndex: ObjTextureCoordinatesIndex?) {
    val textureCoordinates = model.textureCoordinatesAtIndex(textureCoordinatesIndex)
    if (textureCoordinates != null) glTexCoord(textureCoordinates)
}

fun GL2.glTexCoord(textureCoordinates: ObjTextureCoordinates) {
    val coordinates = textureCoordinates.coordinates
    val u = coordinates.getOrNull(INDEX_TU) ?: ZERO
    val v = coordinates.getOrNull(INDEX_TV)
    val w = coordinates.getOrNull(INDEX_TW)
    when {
        v == null -> glTexCoord1f(u)
        w == null -> glTexCoord2f(u, v)
        else -> glTexCoord3f(u, v, w)
    }
}

fun GL2.glVertex(model: GLModel, vertexIndex: ObjVertexIndex) {
    val vertex = model.vertexAtIndex(vertexIndex)
    if (vertex != null) glVertex(vertex)
    else glVertex3f(ZERO, ZERO, ZERO)
}

fun GL2.glVertex(vertex: ObjVertex) {
    val coordinates = vertex.coordinates
    val x = coordinates.getOrNull(INDEX_X) ?: ZERO
    val y = coordinates.getOrNull(INDEX_Y) ?: ZERO
    val z = coordinates.getOrNull(INDEX_Z) ?: ZERO
    val w = coordinates.getOrNull(INDEX_W)
    if (w == null) glVertex3f(x, y, z)
    else glVertex4f(x, y, z, w)
}

fun GL2.glLines(model: GLModel) {
    for (line in model.lines) {
        glLine(model, line)
    }
}

fun GL2.glLine(model: GLModel, line: ObjLine) {
    glBegin(GL2.GL_LINE_STRIP)
    glLineVertices(model, line)
    glEnd()
}

fun GL2.glLineVertices(model: GLModel, line: ObjLine) {
    for (lineVertexIndex in line.vertexIndexList) {
        glVertex(model, lineVertexIndex)
    }
}

fun GL2.glPoints(model: GLModel) {
    glBegin(GL2.GL_POINTS)
    glAllPoints(model)
    glEnd()
}

fun GL2.glAllPoints(model: GLModel) {
    for (point in model.points) {
        glPoint(model, point)
    }
}

fun GL2.glPoint(model: GLModel, point: ObjPoint) {
    glVertex(model, point.vertexIndex)
}
