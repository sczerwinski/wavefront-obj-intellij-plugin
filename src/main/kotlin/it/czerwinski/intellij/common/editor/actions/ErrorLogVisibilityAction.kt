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

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.common.ui.ErrorLogContainer
import it.czerwinski.intellij.wavefront.WavefrontObjBundle

sealed class ErrorLogVisibilityAction : PreviewEditorAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    protected fun findErrorLogContainer(event: AnActionEvent): ErrorLogContainer? =
        findPreviewEditor(event) as? ErrorLogContainer

    class Show : ErrorLogVisibilityAction() {

        override fun update(event: AnActionEvent) {
            val editor = findErrorLogContainer(event)

            event.presentation.isEnabled = editor != null

            event.presentation.text = if (editor != null) {
                WavefrontObjBundle.message(
                    key = "action.PreviewComponent.ErrorLog.Floating.Show.text.count",
                    editor.errorsCount
                )
            } else {
                WavefrontObjBundle.message(key = "action.PreviewComponent.ErrorLog.Floating.Show.text")
            }
        }

        override fun actionPerformed(event: AnActionEvent) {
            findErrorLogContainer(event)?.setErrorLogVisibility(true)
        }
    }

    class Hide : ErrorLogVisibilityAction() {

        override fun update(event: AnActionEvent) {
            val editor = findPreviewEditor(event)

            event.presentation.isEnabled = editor != null
        }

        override fun actionPerformed(event: AnActionEvent) {
            findErrorLogContainer(event)?.setErrorLogVisibility(false)
        }
    }
}
