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

package it.czerwinski.intellij.common.editor.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.util.NlsActions.ActionDescription
import com.intellij.openapi.util.NlsActions.ActionText
import it.czerwinski.intellij.common.editor.BaseSplitEditor
import it.czerwinski.intellij.common.editor.PreviewEditor
import javax.swing.Icon

abstract class PreviewEditorAction(
    @ActionText text: String? = null,
    @ActionDescription description: String? = null,
    icon: Icon? = null
) : AnAction(text, description, icon) {

    protected fun findPreviewEditor(event: AnActionEvent): PreviewEditor? =
        findPreviewEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

    private fun findPreviewEditor(editor: FileEditor?): PreviewEditor? =
        editor as? PreviewEditor ?: findSplitEditor(editor)?.previewEditor as? PreviewEditor

    private fun findSplitEditor(editor: FileEditor?): BaseSplitEditor<*>? =
        editor as? BaseSplitEditor<*> ?: BaseSplitEditor.KEY_PARENT_SPLIT_EDITOR[editor]
}
