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

import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.ui.SimpleListCellRenderer
import it.czerwinski.intellij.wavefront.WavefrontObjBundle

@Suppress("FunctionName")
fun MtlSplitEditorLayoutListCellRenderer(): SimpleListCellRenderer<TextEditorWithPreview.Layout> =
    SimpleListCellRenderer.create("") { value ->
        when (value) {
            TextEditorWithPreview.Layout.SHOW_EDITOR ->
                WavefrontObjBundle.message(key = "settings.editor.fileTypes.mtl.layout.default.text")
            TextEditorWithPreview.Layout.SHOW_EDITOR_AND_PREVIEW ->
                WavefrontObjBundle.message(key = "settings.editor.fileTypes.mtl.layout.default.split")
            TextEditorWithPreview.Layout.SHOW_PREVIEW ->
                WavefrontObjBundle.message(key = "settings.editor.fileTypes.mtl.layout.default.preview")
            else ->
                null
        }
    }
