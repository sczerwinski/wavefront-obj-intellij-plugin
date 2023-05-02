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

package it.czerwinski.intellij.wavefront.icons

import com.intellij.icons.AllIcons
import com.intellij.openapi.util.IconLoader
import com.intellij.util.ui.ColorIcon
import java.awt.Color
import javax.swing.Icon

object Icons {

    private const val ColorIconSize = 14
    private const val ColorIconColorSize = 12

    fun ColorIcon(color: Color): ColorIcon = ColorIcon(ColorIconSize, ColorIconColorSize, color, false)

    object General {
        @JvmField val Refresh = AllIcons.Actions.Refresh
        @JvmField val Settings = AllIcons.General.Settings
        @JvmField val Error = AllIcons.General.BalloonError
        @JvmField val Folder = AllIcons.Nodes.Folder
    }

    object Zoom {
        @JvmField val In = AllIcons.Graph.ZoomIn
        @JvmField val Out = AllIcons.Graph.ZoomOut
        @JvmField val Fit = AllIcons.General.FitContent
    }

    object Mesh {
        @JvmField val Plane = getIcon(path = "/icons/actions/planeMesh.svg")
        @JvmField val Cube = getIcon(path = "/icons/actions/cubeMesh.svg")
        @JvmField val Cylinder = getIcon(path = "/icons/actions/cylinderMesh.svg")
        @JvmField val Sphere = getIcon(path = "/icons/actions/sphereMesh.svg")
    }

    object Shading {
        @JvmField val Wireframe = getIcon(path = "/icons/actions/wireframeShading.svg")
        @JvmField val Solid = getIcon(path = "/icons/actions/solidShading.svg")
        @JvmField val Material = getIcon(path = "/icons/actions/materialShading.svg")
        @JvmField val PBR = getIcon(path = "/icons/actions/pbrShading.svg")
    }

    object Toggle {
        @JvmField val CropTextures = getIcon(path = "/icons/actions/cropTextures.svg")
        @JvmField val Axes = getIcon(path = "/icons/actions/axes.svg")
        @JvmField val Grid = AllIcons.Graph.Grid
    }

    object Axis {
        @JvmField val X = getIcon(path = "/icons/actions/xUp.svg")
        @JvmField val Y = getIcon(path = "/icons/actions/yUp.svg")
        @JvmField val Z = getIcon(path = "/icons/actions/zUp.svg")
    }

    object ToolWindow {
        @JvmField val Material = getIcon(path = "/icons/actions/materialProperties.svg")
    }

    object Filter {
        @JvmField val Unsupported = AllIcons.General.ShowWarning
        @JvmField val Material = getIcon(path = "/icons/actions/materialShading.svg")
        @JvmField val PBR = getIcon(path = "/icons/actions/pbrShading.svg")
    }

    object Generator {
        @JvmField val IBL = getIcon(path = "/icons/actions/ibl.svg")
    }

    object Structure {

        object Mtl {
            @JvmField val File: Icon = getIcon(path = "/icons/structure/mtlFile.svg")

            @JvmField val Material: Icon = getIcon(path = "/icons/structure/mtlMaterial.svg")

            @JvmField val Property: Icon = getIcon(path = "/icons/structure/mtlProperty.svg")
            @JvmField val Texture: Icon = getIcon(path = "/icons/structure/mtlTexture.svg")
            @JvmField val Option: Icon = getIcon(path = "/icons/structure/mtlOption.svg")
        }

        object Obj {
            @JvmField val File: Icon = getIcon(path = "/icons/structure/objFile.svg")

            @JvmField val Object: Icon = getIcon(path = "/icons/structure/objObject.svg")
            @JvmField val Group: Icon = getIcon(path = "/icons/structure/objGroup.svg")

            @JvmField val Vertex: Icon = getIcon(path = "/icons/structure/objVertex.svg")
            @JvmField val TextureCoordinates: Icon = getIcon(path = "/icons/structure/objTextureCoordinates.svg")
            @JvmField val VertexNormal: Icon = getIcon(path = "/icons/structure/objVertexNormal.svg")

            @JvmField val FaceTriangle: Icon = getIcon(path = "/icons/structure/objFaceTriangle.svg")
            @JvmField val FaceQuad: Icon = getIcon(path = "/icons/structure/objFaceQuad.svg")
            @JvmField val FacePolygon: Icon = getIcon(path = "/icons/structure/objFacePolygon.svg")
            @JvmField val Line: Icon = getIcon(path = "/icons/structure/objLine.svg")
            @JvmField val Point: Icon = getIcon(path = "/icons/structure/objPoint.svg")

            val FaceVertex: Icon get() = Vertex
            val LineVertex: Icon get() = Vertex

            @JvmField val FreeFormPoint: Icon = getIcon(path = "/icons/structure/objParameterizedPoint.svg")
            @JvmField val FreeFormType: Icon = getIcon(path = "/icons/structure/objFreeFormType.svg")
            @JvmField val FreeFormDegree: Icon = getIcon(path = "/icons/structure/objPolynomialDegree.svg")
            @JvmField val FreeFormBasisMatrix: Icon = getIcon(path = "/icons/structure/objBasisMatrix.svg")
            val FreeFormStep: Icon = AllIcons.Actions.TraceOver

            @JvmField val FreeFormCurve: Icon = getIcon(path = "/icons/structure/objCurve.svg")
            @JvmField val FreeForm2DCurve: Icon = getIcon(path = "/icons/structure/objParameterizedCurve.svg")
            @JvmField val FreeFormSurface: Icon = getIcon(path = "/icons/structure/objSurface.svg")

            @JvmField val FreeFormCurveControlPoints: Icon = getIcon(path = "/icons/structure/objControlPoints.svg")
            @JvmField val FreeFormSurfaceControlVertices: Icon =
                getIcon(path = "/icons/structure/objControlVertices.svg")

            @JvmField val FreeFormParameters: Icon = getIcon(path = "/icons/structure/objFreeFormParameters.svg")
            @JvmField val FreeFormInnerTrimmingLoop: Icon = getIcon(path = "/icons/structure/objInnerTrimmingLoop.svg")
            @JvmField val FreeFormOuterTrimmingLoop: Icon = getIcon(path = "/icons/structure/objOuterTrimmingLoop.svg")
            @JvmField val FreeFormSpecialCurve: Icon = getIcon(path = "/icons/structure/objSpecialCurve.svg")
            @JvmField val FreeFormSpecialPoints: Icon = getIcon(path = "/icons/structure/objSpecialPoints.svg")

            @JvmField val FreeFormCurveFragment: Icon = getIcon(path = "/icons/structure/objCurveFragment.svg")

            @JvmField val SmoothingGroup: Icon = getIcon(path = "/icons/structure/objSmoothing.svg")

            val MaterialFile: Icon get() = Mtl.File
            val Material: Icon get() = Mtl.Material
        }
    }

    private fun getIcon(path: String): Icon =
        checkNotNull(
            IconLoader.getIcon(path, Icons::class.java)
        ) {
            "Missing resource: '$path'"
        }
}
