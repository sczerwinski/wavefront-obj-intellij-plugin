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

@file:JvmName("ObjIcons")

package it.czerwinski.intellij.wavefront.lang

import com.intellij.openapi.util.IconLoader
import javax.swing.Icon

private fun getIcon(path: String): Icon = IconLoader.getIcon(path, ObjLanguage.javaClass)

val OBJ_FILE_ICON: Icon = getIcon("/icons/objFileIcon.svg")

val OBJ_ERROR_ICON: Icon = getIcon("/icons/objErrorIcon.svg")

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

val OBJ_SMOOTH_SHADING_ON_ICON: Icon = getIcon("/icons/objSmoothShadingOnIcon.svg")
val OBJ_SMOOTH_SHADING_OFF_ICON: Icon = getIcon("/icons/objSmoothShadingOffIcon.svg")

val OBJ_MATERIAL_FILE_ICON: Icon = getIcon("/icons/objMaterialFileIcon.svg")
val OBJ_MATERIAL_ICON: Icon = getIcon("/icons/objMaterialIcon.svg")
