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

package it.czerwinski.intellij.common.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.Toggleable
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.common.editor.SplitEditor

/**
 * An action for split editor layout selection.
 */
sealed class SplitLayoutAction(
    private val splitLayout: SplitEditor.Layout
) : AnAction(), DumbAware, Toggleable {

    override fun update(event: AnActionEvent) {
        val editor = findObjSplitEditor(event)

        event.presentation.isEnabled = editor != null

        if (editor != null) {
            Toggleable.setSelected(event.presentation, editor.layout === splitLayout)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val editor = findObjSplitEditor(event)

        if (editor != null) {
            editor.layout = splitLayout
            Toggleable.setSelected(event.presentation, true)
        }
    }

    companion object {

        private fun findObjSplitEditor(event: AnActionEvent): SplitEditor<*>? =
            findObjSplitEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

        private fun findObjSplitEditor(editor: FileEditor?): SplitEditor<*>? =
            if (editor is SplitEditor<*>) editor
            else SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor]
    }

    /**
     * An action for selecting text layout.
     */
    class TextOnly : SplitLayoutAction(SplitEditor.Layout.TEXT)

    /**
     * An action for selecting text and preview layout.
     */
    class TextAndPreview : SplitLayoutAction(SplitEditor.Layout.SPLIT)

    /**
     * An action for selecting preview layout.
     */
    class PreviewOnly : SplitLayoutAction(SplitEditor.Layout.PREVIEW)
}
