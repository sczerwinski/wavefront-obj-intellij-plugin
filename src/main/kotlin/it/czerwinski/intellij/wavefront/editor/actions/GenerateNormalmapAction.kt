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
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.DumbAware

class GenerateNormalmapAction : AnAction(), DumbAware {

    override fun update(event: AnActionEvent) {
        val project = event.project
        val file = event.getData(CommonDataKeys.VIRTUAL_FILE)
//        val image = file != null && ImageFileTypeManager.getInstance().isImage(file)
//        val visible = !ActionPlaces.isPopupPlace(event.place) || image
//        event.presentation.isEnabled = project != null
//        event.presentation.isVisible = visible
    }
    override fun actionPerformed(event: AnActionEvent) {
        TODO("Not yet implemented")
    }
}