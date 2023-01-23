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

package it.czerwinski.intellij.wavefront.settings.ui

import com.intellij.ui.SimpleListCellRenderer
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.UpVector

@Suppress("FunctionName")
fun UpVectorListCellRenderer(): SimpleListCellRenderer<UpVector> =
    SimpleListCellRenderer.create("") { value ->
        when (value) {
            UpVector.X_UP ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector.x")
            UpVector.Y_UP ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector.y")
            UpVector.Z_UP ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.upVector.z")
            else ->
                null
        }
    }
