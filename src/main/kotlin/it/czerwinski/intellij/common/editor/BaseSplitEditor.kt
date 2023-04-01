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

package it.czerwinski.intellij.common.editor

import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.util.Key
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState

/**
 * Split editor with three possible layouts: text, preview, text and preview (split).
 *
 * This editor can be split either horizontally or vertically.

 * @param P Preview editor type.
 */
open class BaseSplitEditor<P : FileEditor>(
    textEditor: TextEditor,
    previewEditor: P,
    editorName: String
) : TextEditorWithPreview(
    textEditor,
    previewEditor,
    editorName,
    WavefrontObjSettingsState.DEFAULT_LAYOUT,
    WavefrontObjSettingsState.DEFAULT_VERTICAL_SPLIT
) {

    init {
        this.putUserData(KEY_CARET_MODEL, textEditor.editor.caretModel)
        KEY_PARENT_SPLIT_EDITOR[textEditor] = this
        KEY_PARENT_SPLIT_EDITOR[previewEditor] = this
        KEY_CARET_MODEL[previewEditor] = textEditor.editor.caretModel
    }

    companion object {
        val KEY_PARENT_SPLIT_EDITOR: Key<BaseSplitEditor<*>> = Key.create("SplitEditor.parentSplitEditor")
        val KEY_CARET_MODEL: Key<CaretModel> = Key.create("SplitEditor.caretModel")
    }
}
