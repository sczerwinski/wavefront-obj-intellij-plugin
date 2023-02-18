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
import javax.swing.Icon

object Icons {

    object General {
        @JvmField val Refresh = AllIcons.Actions.Refresh
        @JvmField val Settings = AllIcons.General.Settings
        @JvmField val Error = AllIcons.General.Error
        @JvmField val Folder = AllIcons.Nodes.Folder
    }

    object Zoom {
        @JvmField val In = AllIcons.General.ZoomIn
        @JvmField val Out = AllIcons.General.ZoomOut
        @JvmField val Fit = AllIcons.General.FitContent
    }

    object Layout {
        @JvmField val EditorOnly = AllIcons.General.LayoutEditorOnly
        @JvmField val EditorPreviewHorizontal = AllIcons.General.LayoutEditorPreview
        @JvmField val EditorPreviewVertical = getIcon(path = "/icons/editor_actions/layoutEditorPreviewVertical.svg")
        @JvmField val PreviewOnly = AllIcons.General.LayoutPreviewOnly
    }

    object Mesh {
        @JvmField val Cube = getIcon(path = "/icons/editor_actions/cubeMesh.svg")
        @JvmField val Cylinder = getIcon(path = "/icons/editor_actions/cylinderMesh.svg")
        @JvmField val Sphere = getIcon(path = "/icons/editor_actions/sphereMesh.svg")
    }

    object Shading {
        @JvmField val Wireframe = getIcon(path = "/icons/editor_actions/wireframeShading.svg")
        @JvmField val Solid = getIcon(path = "/icons/editor_actions/solidShading.svg")
        @JvmField val Material = getIcon(path = "/icons/editor_actions/materialShading.svg")
        @JvmField val PBR = getIcon(path = "/icons/editor_actions/pbrShading.svg")
    }

    object Toggle {
        @JvmField val CropTextures = getIcon(path = "/icons/editor_actions/cropTextures.svg")
        @JvmField val Axes = getIcon(path = "/icons/editor_actions/axes.svg")
        @JvmField val Grid = getIcon(path = "/icons/editor_actions/grid.svg")
    }

    object Axis {
        @JvmField val X = getIcon(path = "/icons/editor_actions/xUp.svg")
        @JvmField val Y = getIcon(path = "/icons/editor_actions/yUp.svg")
        @JvmField val Z = getIcon(path = "/icons/editor_actions/zUp.svg")
    }

    object Generator {
        @JvmField val IBL = getIcon(path = "/icons/editor_actions/ibl.svg")
    }

    private fun getIcon(path: String): Icon =
        checkNotNull(
            IconLoader.getIcon(path, Icons::class.java)
        ) {
            "Missing resource: '$path'"
        }
}
