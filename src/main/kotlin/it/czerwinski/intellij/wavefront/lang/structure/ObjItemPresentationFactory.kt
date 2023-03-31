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

@file:Suppress("TooManyFunctions")

package it.czerwinski.intellij.wavefront.lang.structure

import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.icons.Icons
import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjLine
import it.czerwinski.intellij.wavefront.lang.psi.ObjLineVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReferenceStatement
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import it.czerwinski.intellij.wavefront.lang.psi.ObjPoint
import it.czerwinski.intellij.wavefront.lang.psi.ObjSmoothingGroup
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
        is ObjLineVertex -> createPresentation(element)

        is ObjVertexIndex -> createPresentation(element)
        is ObjTextureCoordinatesIndex -> createPresentation(element)
        is ObjVertexNormalIndex -> createPresentation(element)

        is ObjSmoothingGroup -> createPresentation(element)

        is ObjMaterialFileReferenceStatement -> createPresentation(element)
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
        icon = Icons.Structure.Obj.File
    )

    private fun createPresentation(obj: ObjObject): ItemPresentation = createPresentation(
        presentableText = obj.getName() ?: WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.object"
        ),
        locationString = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.object.triangles",
            obj.trianglesCount
        ),
        icon = Icons.Structure.Obj.Object
    )

    private fun createPresentation(group: ObjGroup): ItemPresentation = createPresentation(
        presentableText = group.getName() ?: WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.group"
        ),
        locationString = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.group.triangles",
            group.trianglesCount
        ),
        icon = Icons.Structure.Obj.Group
    )

    private fun createPresentation(vertex: ObjVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertex"
        ),
        locationString = vertex.coordinatesString,
        icon = Icons.Structure.Obj.Vertex
    )

    private fun createPresentation(textureCoordinates: ObjTextureCoordinates): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.textureCoordinates"
        ),
        locationString = textureCoordinates.coordinatesString,
        icon = Icons.Structure.Obj.TextureCoordinates
    )

    private fun createPresentation(vertexNormal: ObjVertexNormal): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertexNormal"
        ),
        locationString = vertexNormal.coordinatesString,
        icon = Icons.Structure.Obj.VertexNormal
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
                ObjFaceType.TRIANGLE -> Icons.Structure.Obj.FaceTriangle
                ObjFaceType.QUAD -> Icons.Structure.Obj.FaceQuad
                ObjFaceType.POLYGON -> Icons.Structure.Obj.FacePolygon
            }
        )
    }

    @Suppress("UnusedPrivateMember")
    private fun createPresentation(line: ObjLine): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.line"
        ),
        icon = Icons.Structure.Obj.Line
    )

    @Suppress("UnusedPrivateMember")
    private fun createPresentation(point: ObjPoint): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.point"
        ),
        icon = Icons.Structure.Obj.Point
    )

    @Suppress("UnusedPrivateMember")
    private fun createPresentation(faceVertex: ObjFaceVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.faceVertex"
        ),
        icon = Icons.Structure.Obj.FaceVertex
    )

    @Suppress("UnusedPrivateMember")
    private fun createPresentation(lineVertex: ObjLineVertex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.lineVertex"
        ),
        icon = Icons.Structure.Obj.LineVertex
    )

    private fun createPresentation(vertexIndex: ObjVertexIndex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertex"
        ),
        locationString = vertexIndex.value.toString(),
        icon = Icons.Structure.Obj.Vertex
    )

    private fun createPresentation(
        textureCoordinatesIndex: ObjTextureCoordinatesIndex
    ): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.textureCoordinates"
        ),
        locationString = textureCoordinatesIndex.value.toString(),
        icon = Icons.Structure.Obj.TextureCoordinates
    )

    private fun createPresentation(vertexNormalIndex: ObjVertexNormalIndex): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.obj.structure.presentation.vertexNormal"
        ),
        locationString = vertexNormalIndex.value.toString(),
        icon = Icons.Structure.Obj.VertexNormal
    )

    private fun createPresentation(smoothShading: ObjSmoothingGroup): ItemPresentation {
        val value = smoothShading.value
        return if (value == null) createErrorPresentation(
            errorMessage = WavefrontObjBundle.message("fileTypes.obj.structure.presentation.smoothingGroup.error"),
            elementText = smoothShading.text
        )
        else createPresentation(
            presentableText = WavefrontObjBundle.message("fileTypes.obj.structure.presentation.smoothingGroup"),
            locationString = if (value == 0) {
                WavefrontObjBundle.message("fileTypes.obj.structure.presentation.smoothingGroup.false")
            } else {
                value.toString()
            },
            icon = Icons.Structure.Obj.SmoothingGroup
        )
    }

    @Suppress("UnusedPrivateMember")
    private fun createPresentation(statement: ObjMaterialFileReferenceStatement): ItemPresentation =
        createPresentation(
            presentableText = WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.materialFileReference"
            ),
            icon = Icons.Structure.Obj.MaterialFile
        )

    private fun createPresentation(materialFileReference: ObjMaterialFileReference): ItemPresentation =
        createPresentation(
            presentableText = materialFileReference.filename ?: WavefrontObjBundle.message(
                "fileTypes.obj.structure.presentation.materialFile"
            ),
            icon = Icons.Structure.Obj.MaterialFile
        )

    private fun createPresentation(materialReference: ObjMaterialReference): ItemPresentation =
        createPresentation(
            presentableText = WavefrontObjBundle.message("fileTypes.obj.structure.presentation.material"),
            locationString = materialReference.materialName.orEmpty(),
            icon = Icons.Structure.Obj.Material
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
        icon = Icons.General.Error
    )

    private fun createPresentation(
        presentableText: String,
        locationString: String = "",
        icon: Icon
    ): ItemPresentation =
        PresentationData(presentableText, locationString, icon, null)
}
