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

package it.czerwinski.intellij.wavefront.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.invokeLater
import it.czerwinski.intellij.wavefront.actions.ui.GenerateBRDFIntegrationMapDialog

class GenerateBRDFIntegrationMapAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        val project = event.project
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val fileIsValid = file?.isDirectory == true || file?.parent?.isDirectory == true

        if (project != null && file != null && fileIsValid) {
            invokeLater {
                GenerateBRDFIntegrationMapDialog(project).show(listOf(file))
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val fileIsValid = file?.isDirectory == true || file?.parent?.isDirectory == true

        event.presentation.isEnabled = project != null && fileIsValid
        event.presentation.isVisible = project != null && fileIsValid
    }
}
