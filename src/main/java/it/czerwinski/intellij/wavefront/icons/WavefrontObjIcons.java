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

package it.czerwinski.intellij.wavefront.icons;

import com.intellij.openapi.util.IconLoader;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

public class WavefrontObjIcons {

    @NotNull
    public static final Icon CUBE_MESH_ACTION = getIcon("/icons/editor_actions/cubeMesh.svg");

    @NotNull
    public static final Icon CYLINDER_MESH_ACTION = getIcon("/icons/editor_actions/cylinderMesh.svg");

    @NotNull
    public static final Icon SPHERE_MESH_ACTION = getIcon("/icons/editor_actions/sphereMesh.svg");

    @NotNull
    public static final Icon WIREFRAME_SHADING_ACTION = getIcon("/icons/editor_actions/wireframeShading.svg");

    @NotNull
    public static final Icon SOLID_SHADING_ACTION = getIcon("/icons/editor_actions/solidShading.svg");

    @NotNull
    public static final Icon MATERIAL_SHADING_ACTION = getIcon("/icons/editor_actions/materialShading.svg");

    @NotNull
    public static final Icon PBR_SHADING_ACTION = getIcon("/icons/editor_actions/pbrShading.svg");

    @NotNull
    public static final Icon TOGGLE_CROP_TEXTURES_ACTION = getIcon("/icons/editor_actions/cropTextures.svg");

    @NotNull
    public static final Icon TOGGLE_AXES_ACTION = getIcon("/icons/editor_actions/axes.svg");

    @NotNull
    public static final Icon TOGGLE_GRID_ACTION = getIcon("/icons/editor_actions/grid.svg");

    @NotNull
    public static final Icon X_UP_ACTION = getIcon("/icons/editor_actions/xUp.svg");

    @NotNull
    public static final Icon Y_UP_ACTION = getIcon("/icons/editor_actions/yUp.svg");

    @NotNull
    public static final Icon Z_UP_ACTION = getIcon("/icons/editor_actions/zUp.svg");

    private static Icon getIcon(String path) {
        return IconLoader.getIcon(path, WavefrontObjIcons.class);
    }
}
