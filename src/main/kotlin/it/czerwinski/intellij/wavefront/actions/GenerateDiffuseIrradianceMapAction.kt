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

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.wavefront.actions.ui.GenerateDiffuseIrradianceMapDialog
import org.intellij.images.fileTypes.impl.ImageFileType

class GenerateDiffuseIrradianceMapAction : AnAction(), DumbAware {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(event: AnActionEvent) {
        event.actionManager
        val project = event.project
        val files = event.getData(CommonDataKeys.VIRTUAL_FILE_ARRAY)
            ?.filter { file ->
                file != null && FileTypeRegistry.getInstance().isFileOfType(file, ImageFileType.INSTANCE)
            }
            .orEmpty()

        if (project != null && files.isNotEmpty()) {
            invokeLater {
                val dialog = GenerateDiffuseIrradianceMapDialog(project)
                dialog.show(files)
            }
        }
    }

    override fun update(event: AnActionEvent) {
        val project = event.project
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
        val fileIsImage = file != null && FileTypeRegistry.getInstance().isFileOfType(file, ImageFileType.INSTANCE)

        event.presentation.isEnabled = project != null && fileIsImage
        event.presentation.isVisible = !ActionPlaces.isPopupPlace(event.place) || fileIsImage
    }
}
