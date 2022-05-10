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
import com.intellij.openapi.util.NlsActions.ActionText
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import java.util.Locale

class PBREnvironmentAction(
    private val environment: PBREnvironment
) : ObjPreviewFileEditorAction(text = text(environment)), DumbAware, Toggleable {

    override fun update(event: AnActionEvent) {
        val editor = findObjPreviewFileEditor(event)

        event.presentation.isEnabled = editor?.shadingMethod === ShadingMethod.PBR

        if (editor != null) {
            Toggleable.setSelected(event.presentation, editor.environment === environment)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val editor = findObjPreviewFileEditor(event)

        editor?.environment = environment
    }

    companion object {

        @ActionText
        fun text(environment: PBREnvironment): String = WavefrontObjBundle.message(
            key = "action.${PBREnvironmentAction::class.java.name}.text.${environment.name.lowercase(Locale.ENGLISH)}"
        )
    }
}
