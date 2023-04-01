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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.util.xmlb.annotations.Attribute
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

data class MtlEditorSettingsState(
    @field:Attribute var editorLayout: TextEditorWithPreview.Layout = WavefrontObjSettingsState.DEFAULT_LAYOUT,
    @field:Attribute var isVerticalSplit: Boolean = DEFAULT_VERTICAL_SPLIT,
    @field:Attribute var defaultPreviewMesh: MaterialPreviewMesh = MaterialPreviewMesh.DEFAULT,
    @field:Attribute var defaultShadingMethod: ShadingMethod = ShadingMethod.MTL_DEFAULT,
) {

    companion object {
        val DEFAULT = MtlEditorSettingsState()

        const val DEFAULT_VERTICAL_SPLIT = false
    }

    interface Holder {
        var mtlEditorSettings: MtlEditorSettingsState
    }
}
