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
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.editor.Refreshable

class RefreshAction : AnAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val editor = findRefreshableFileEditor(event)

        event.presentation.isEnabled = editor != null
    }

    override fun actionPerformed(event: AnActionEvent) {
        findRefreshableFileEditor(event)?.refresh()
    }

    private fun findRefreshableFileEditor(event: AnActionEvent): Refreshable? =
        findRefreshableFileEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

    private fun findRefreshableFileEditor(editor: FileEditor?): Refreshable? =
        editor as? Refreshable ?: findSplitEditor(editor)?.previewEditor as? Refreshable

    private fun findSplitEditor(editor: FileEditor?): SplitEditor<*>? =
        editor as? SplitEditor<*> ?: SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor]
}
