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

package it.czerwinski.intellij.wavefront.editor.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.util.NlsActions.ActionDescription
import com.intellij.openapi.util.NlsActions.ActionText
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.editor.Zoomable
import javax.swing.Icon

abstract class ZoomableFileEditorAction(
    @ActionText text: String? = null,
    @ActionDescription description: String? = null,
    icon: Icon? = null
) : AnAction(text, description, icon) {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val editor = findZoomableFileEditor(event)

        event.presentation.isEnabled = editor != null
    }

    protected fun findZoomableFileEditor(event: AnActionEvent): Zoomable? =
        findZoomableFileEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

    private fun findZoomableFileEditor(editor: FileEditor?): Zoomable? =
        editor as? Zoomable ?: findSplitEditor(editor)?.previewEditor as? Zoomable

    private fun findSplitEditor(editor: FileEditor?): SplitEditor<*>? =
        editor as? SplitEditor<*> ?: SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor]
}
