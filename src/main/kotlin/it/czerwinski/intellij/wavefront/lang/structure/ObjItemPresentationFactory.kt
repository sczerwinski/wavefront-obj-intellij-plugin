/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.lang.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.OBJ_ERROR_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_FACE_POLYGON_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_FACE_QUAD_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_FACE_TRIANGLE_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_FACE_VERTEX_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_FILE_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_GROUP_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_LINE_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_MATERIAL_FILE_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_MATERIAL_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_OBJECT_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_POINT_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_SMOOTH_SHADING_OFF_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_SMOOTH_SHADING_ON_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_TEXTURE_COORDINATES_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_VERTEX_ICON
import it.czerwinski.intellij.wavefront.lang.OBJ_VERTEX_NORMAL_ICON
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjSmoothShading
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinatesIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexIndex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormalIndex
import it.czerwinski.intellij.wavefront.lang.psi.coordinatesString
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

        is ObjMaterialFileReference -> createPresentation(element)
        is ObjMaterialReference -> createPresentation(element)

        else -> createErrorPresentation(
            errorMessage = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.error.unknownElement"
            ),
            elementText = element.text
        )
    }

    private fun createPresentation(file: ObjFile): ItemPresentation = createPresentation(
        presentableText = file.name,
        icon = OBJ_FILE_ICON
    )

    private fun createPresentation(obj: ObjObject): ItemPresentation = createPresentation(
        presentableText = obj.name ?: WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.object"
        ),
        icon = OBJ_OBJECT_ICON
    )

    private fun createPresentation(group: ObjGroup): ItemPresentation = createPresentation(
        presentableText = group.name ?: WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.group"
        ),
        icon = OBJ_GROUP_ICON
    )

    private fun createPresentation(vertex: ObjVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertex"
        ),
        locationString = vertex.coordinatesString,
        icon = OBJ_VERTEX_ICON
    )

    private fun createPresentation(textureCoordinates: ObjTextureCoordinates): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.textureCoordinates"
        ),
        locationString = textureCoordinates.coordinatesString,
        icon = OBJ_TEXTURE_COORDINATES_ICON
    )

    private fun createPresentation(vertexNormal: ObjVertexNormal): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertexNormal"
        ),
        locationString = vertexNormal.coordinatesString,
        icon = OBJ_VERTEX_NORMAL_ICON
    )

    private fun createPresentation(face: ObjFace): ItemPresentation {
        val faceType = face.type
        return if (faceType == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.face.type.error"
            ),
            elementText = face.text
        )
        else createPresentation(
            presentableText = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.face"
            ),
            icon = when (faceType) {
                ObjFaceType.TRIANGLE -> OBJ_FACE_TRIANGLE_ICON
                ObjFaceType.QUAD -> OBJ_FACE_QUAD_ICON
                ObjFaceType.POLYGON -> OBJ_FACE_POLYGON_ICON
            }
        )
    }

    private fun createPresentation(line: ObjLine): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.line"
        ),
        icon = OBJ_LINE_ICON
    )

    private fun createPresentation(point: ObjPoint): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.point"
        ),
        icon = OBJ_POINT_ICON
    )

    private fun createPresentation(faceVertex: ObjFaceVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.faceVertex"
        ),
        icon = OBJ_FACE_VERTEX_ICON
    )

    private fun createPresentation(vertexIndex: ObjVertexIndex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertex"
        ),
        locationString = vertexIndex.value.toString(),
        icon = OBJ_VERTEX_ICON
    )

    private fun createPresentation(
        textureCoordinatesIndex: ObjTextureCoordinatesIndex
    ): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.textureCoordinates"
        ),
        locationString = textureCoordinatesIndex.value.toString(),
        icon = OBJ_TEXTURE_COORDINATES_ICON
    )

    private fun createPresentation(vertexNormalIndex: ObjVertexNormalIndex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertexNormal"
        ),
        locationString = vertexNormalIndex.value.toString(),
        icon = OBJ_VERTEX_NORMAL_ICON
    )

    private fun createPresentation(smoothShading: ObjSmoothShading): ItemPresentation {
        val value = smoothShading.value
        return if (value == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.smoothShading.error"
            ),
            elementText = smoothShading.text
        )
        else createPresentation(
            presentableText = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.smoothShading"
            ),
            locationString = WavefrontObjBundle.message(
                if (value) "fileTypes.obj.structure.presentation.flag.true"
                else "fileTypes.obj.structure.presentation.flag.false"
            ),
            icon = if (value) OBJ_SMOOTH_SHADING_ON_ICON else OBJ_SMOOTH_SHADING_OFF_ICON
        )
    }

    private fun createPresentation(materialFileReference: ObjMaterialFileReference): ItemPresentation =
        createPresentation(
            presentableText = materialFileReference.filename ?: WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.materialFile"
            ),
            icon = OBJ_MATERIAL_FILE_ICON
        )

    private fun createPresentation(materialReference: ObjMaterialReference): ItemPresentation =
        createPresentation(
            presentableText = materialReference.materialName ?: WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.material"
            ),
            icon = OBJ_MATERIAL_ICON
        )

    private fun createErrorPresentation(
        errorMessage: String,
        elementText: String
    ): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.error",
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
