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

package it.czerwinski.intellij.wavefront.editor.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditor
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.editor.ObjPreviewEditor
import it.czerwinski.intellij.wavefront.editor.ObjSplitEditor

abstract class ObjPreviewFileEditorAction : AnAction() {

    protected fun findObjPreviewFileEditor(event: AnActionEvent): ObjPreviewEditor? =
        findObjPreviewFileEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

    private fun findObjPreviewFileEditor(editor: FileEditor?): ObjPreviewEditor? =
        if (editor is ObjPreviewEditor) editor
        else findObjSplitEditor(editor)?.previewEditor

    private fun findObjSplitEditor(editor: FileEditor?): ObjSplitEditor? =
        if (editor is ObjSplitEditor) editor
        else SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor] as? ObjSplitEditor
}
