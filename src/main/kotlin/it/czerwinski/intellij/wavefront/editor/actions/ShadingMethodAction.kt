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

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Toggleable
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

abstract class ShadingMethodAction(
    private val shadingMethod: ShadingMethod
) : ObjPreviewFileEditorAction(), DumbAware, Toggleable {

    override fun update(event: AnActionEvent) {
        val editor = findObjPreviewFileEditor(event)

        event.presentation.isEnabled = editor != null

        if (editor != null) {
            Toggleable.setSelected(event.presentation, editor.shadingMethod === shadingMethod)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val editor = findObjPreviewFileEditor(event)

        if (editor != null) {
            editor.triggerShadingMethodChange(shadingMethod)
            Toggleable.setSelected(event.presentation, true)
        }
    }
}