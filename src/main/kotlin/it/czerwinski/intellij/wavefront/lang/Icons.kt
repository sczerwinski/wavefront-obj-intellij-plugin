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

@file:JvmName("Icons")

package it.czerwinski.intellij.wavefront.lang

import com.intellij.openapi.util.IconLoader
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import javax.swing.Icon

private fun getIcon(path: String): Icon = IconLoader.getIcon(path, WavefrontObjBundle.javaClass)

val MTL_FILE_ICON: Icon = getIcon("/icons/mtlFileIcon.svg")

val MTL_MATERIAL_ICON: Icon = getIcon("/icons/mtlMaterialIcon.svg")

val MTL_PROPERTY_ICON: Icon = getIcon("/icons/mtlPropertyIcon.svg")
val MTL_TEXTURE_ICON: Icon = getIcon("/icons/mtlTextureIcon.svg")
val MTL_OPTION_ICON: Icon = getIcon("/icons/mtlOptionIcon.svg")

val OBJ_FILE_ICON: Icon = getIcon("/icons/objFileIcon.svg")

val OBJ_OBJECT_ICON: Icon = getIcon("/icons/objObjectIcon.svg")
val OBJ_GROUP_ICON: Icon = getIcon("/icons/objGroupIcon.svg")

val OBJ_VERTEX_ICON: Icon = getIcon("/icons/objVertexIcon.svg")
val OBJ_TEXTURE_COORDINATES_ICON: Icon = getIcon("/icons/objTextureCoordinatesIcon.svg")
val OBJ_VERTEX_NORMAL_ICON: Icon = getIcon("/icons/objVertexNormalIcon.svg")

val OBJ_FACE_TRIANGLE_ICON: Icon = getIcon("/icons/objFaceTriangleIcon.svg")
val OBJ_FACE_QUAD_ICON: Icon = getIcon("/icons/objFaceQuadIcon.svg")
val OBJ_FACE_POLYGON_ICON: Icon = getIcon("/icons/objFacePolygonIcon.svg")
val OBJ_LINE_ICON: Icon = getIcon("/icons/objLineIcon.svg")
val OBJ_POINT_ICON: Icon = getIcon("/icons/objPointIcon.svg")

val OBJ_FACE_VERTEX_ICON: Icon = getIcon("/icons/objFaceVertexIcon.svg")
val OBJ_LINE_VERTEX_ICON: Icon = OBJ_FACE_VERTEX_ICON

val OBJ_SMOOTHING_ON_ICON: Icon = getIcon("/icons/objSmoothingOnIcon.svg")
val OBJ_SMOOTHING_OFF_ICON: Icon = getIcon("/icons/objSmoothingOffIcon.svg")

val OBJ_MATERIAL_FILE_ICON: Icon = MTL_FILE_ICON
val OBJ_MATERIAL_ICON: Icon = MTL_MATERIAL_ICON
