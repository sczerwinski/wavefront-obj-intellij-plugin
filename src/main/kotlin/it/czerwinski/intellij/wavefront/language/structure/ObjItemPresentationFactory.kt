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

package it.czerwinski.intellij.wavefront.language.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.language.OBJ_ERROR_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_FACE_POLYGON_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_FACE_QUAD_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_FACE_TRIANGLE_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_FACE_VERTEX_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_FILE_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_GROUP_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_LINE_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_OBJECT_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_POINT_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_SMOOTH_SHADING_OFF_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_SMOOTH_SHADING_ON_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_TEXTURE_COORDINATES_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_VERTEX_ICON
import it.czerwinski.intellij.wavefront.language.OBJ_VERTEX_NORMAL_ICON
import it.czerwinski.intellij.wavefront.language.findTextureCoordinates
import it.czerwinski.intellij.wavefront.language.findVertex
import it.czerwinski.intellij.wavefront.language.findVertexNormal
import it.czerwinski.intellij.wavefront.language.psi.ObjFace
import it.czerwinski.intellij.wavefront.language.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.language.psi.ObjFaceVertex
import it.czerwinski.intellij.wavefront.language.psi.ObjFile
import it.czerwinski.intellij.wavefront.language.psi.ObjGroup
import it.czerwinski.intellij.wavefront.language.psi.ObjLine
import it.czerwinski.intellij.wavefront.language.psi.ObjObject
import it.czerwinski.intellij.wavefront.language.psi.ObjPoint
import it.czerwinski.intellij.wavefront.language.psi.ObjSmoothShading
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.language.psi.coordinatesString
import javax.swing.Icon

object ObjItemPresentationFactory {

    @Suppress("ComplexMethod")
    fun createPresentation(element: PsiElement): ItemPresentation = when (element) {
        is ObjFile -> createPresentation(element)

        is ObjObject -> createPresentation(element)
        is ObjGroup -> createPresentation(element)

        is ObjVertex -> createPresentation(element)
        is ObjTextureCoordinates -> createPresentation(element)
        is ObjVertexNormal -> createPresentation(element)

        is ObjFace -> createPresentation(element)
        is ObjLine -> createPresentation(element)
        is ObjPoint -> createPresentation(element)

        is ObjFaceVertex -> createPresentation(element)

        is ObjVertexIndex -> createPresentation(element)
        is ObjTextureCoordinatesIndex -> createPresentation(element)
        is ObjVertexNormalIndex -> createPresentation(element)

        is ObjSmoothShading -> createPresentation(element)

        else -> createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_unknownElement"),
            elementText = element.text
        )
    }

    private fun createPresentation(file: ObjFile): ItemPresentation = createPresentation(
        presentableText = file.name,
        icon = OBJ_FILE_ICON
    )

    private fun createPresentation(obj: ObjObject): ItemPresentation = createPresentation(
        presentableText = obj.name ?: WavefrontObjBundle.message("structure_presentation_object"),
        icon = OBJ_OBJECT_ICON
    )

    private fun createPresentation(group: ObjGroup): ItemPresentation = createPresentation(
        presentableText = group.name ?: WavefrontObjBundle.message("structure_presentation_group"),
        icon = OBJ_GROUP_ICON
    )

    private fun createPresentation(vertex: ObjVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "structure_presentation_vertex",
            vertex.index
        ),
        locationString = vertex.coordinatesString,
        icon = OBJ_VERTEX_ICON
    )

    private fun createPresentation(textureCoordinates: ObjTextureCoordinates): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "structure_presentation_textureCoordinates",
            textureCoordinates.index
        ),
        locationString = textureCoordinates.coordinatesString,
        icon = OBJ_TEXTURE_COORDINATES_ICON
    )

    private fun createPresentation(vertexNormal: ObjVertexNormal): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "structure_presentation_vertexNormal",
            vertexNormal.index
        ),
        locationString = vertexNormal.coordinatesString,
        icon = OBJ_VERTEX_NORMAL_ICON
    )

    private fun createPresentation(face: ObjFace): ItemPresentation {
        val faceType = face.type
        return if (faceType == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_faceType"),
            elementText = face.text
        )
        else createPresentation(
            presentableText = WavefrontObjBundle.message("structure_presentation_face"),
            icon = when (faceType) {
                ObjFaceType.TRIANGLE -> OBJ_FACE_TRIANGLE_ICON
                ObjFaceType.QUAD -> OBJ_FACE_QUAD_ICON
                ObjFaceType.POLYGON -> OBJ_FACE_POLYGON_ICON
            }
        )
    }

    private fun createPresentation(line: ObjLine): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message("structure_presentation_line"),
        icon = OBJ_LINE_ICON
    )

    private fun createPresentation(point: ObjPoint): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message("structure_presentation_point"),
        icon = OBJ_POINT_ICON
    )

    private fun createPresentation(faceVertex: ObjFaceVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message("structure_presentation_face_vertex"),
        icon = OBJ_FACE_VERTEX_ICON
    )

    private fun createPresentation(vertexIndex: ObjVertexIndex): ItemPresentation {
        val vertex = vertexIndex.value
            ?.let { findVertex(vertexIndex.containingFile, it) }
        return if (vertex == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_vertexNotFound"),
            elementText = vertexIndex.text
        )
        else createPresentation(vertex)
    }

    private fun createPresentation(textureCoordinatesIndex: ObjTextureCoordinatesIndex): ItemPresentation {
        val textureCoordinates = textureCoordinatesIndex.value
            ?.let { findTextureCoordinates(textureCoordinatesIndex.containingFile, it) }
        return if (textureCoordinates == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_textureCoordinatesNotFound"),
            elementText = textureCoordinatesIndex.text
        )
        else createPresentation(textureCoordinates)
    }

    private fun createPresentation(vertexNormalIndex: ObjVertexNormalIndex): ItemPresentation {
        val vertexNormal = vertexNormalIndex.value
            ?.let { findVertexNormal(vertexNormalIndex.containingFile, it) }
        return if (vertexNormal == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_vertexNormalNotFound"),
            elementText = vertexNormalIndex.text
        )
        else createPresentation(vertexNormal)
    }

    private fun createPresentation(smoothShading: ObjSmoothShading): ItemPresentation {
        val value = smoothShading.value
        return if (value == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("structure_presentation_error_smoothShading"),
            elementText = smoothShading.text
        )
        else createPresentation(
            presentableText = WavefrontObjBundle.message("structure_presentation_smoothShading"),
            locationString = WavefrontObjBundle.message(
                if (value) "structure_presentation_flagTrue"
                else "structure_presentation_flagFalse"
            ),
            icon = if (value) OBJ_SMOOTH_SHADING_ON_ICON else OBJ_SMOOTH_SHADING_OFF_ICON
        )
    }

    private fun createErrorPresentation(
        errorMessage: String,
        elementText: String
    ): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "structure_presentation_error",
            errorMessage
        ),
        locationString = elementText,
        icon = OBJ_ERROR_ICON
    )

    private fun createPresentation(
        presentableText: String,
        locationString: String = "",
        icon: Icon
    ): ItemPresentation =
        PresentationData(presentableText, locationString, icon, null)
}
