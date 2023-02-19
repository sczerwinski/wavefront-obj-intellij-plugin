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

package it.czerwinski.intellij.wavefront.tools.actions

import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.Toggleable
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.wavefront.tools.model.ShadingMethodPropertiesFilter
import it.czerwinski.intellij.wavefront.tools.state.MaterialPropertiesFilter

sealed class ShadingMethodPropertiesAction(
    private val shadingMethod: ShadingMethodPropertiesFilter
) : AnAction(), DumbAware, Toggleable {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun update(event: AnActionEvent) {
        val shadingMethods = MaterialPropertiesFilter.getInstance().shadingMethods
        Toggleable.setSelected(event.presentation, shadingMethod in shadingMethods)
    }

    override fun actionPerformed(event: AnActionEvent) {
        val filter = MaterialPropertiesFilter.getInstance()
        val shadingMethods = filter.shadingMethods

        val newFilter = if (shadingMethod in shadingMethods) {
            filter.copy(shadingMethods = shadingMethods - shadingMethod)
        } else {
            filter.copy(shadingMethods = shadingMethods + shadingMethod)
        }
        filter.setFrom(newFilter)
        Toggleable.setSelected(event.presentation, shadingMethod in newFilter.shadingMethods)
    }

    class Unsupported : ShadingMethodPropertiesAction(ShadingMethodPropertiesFilter.Unsupported)

    class Material : ShadingMethodPropertiesAction(ShadingMethodPropertiesFilter.Material)

    class PBR : ShadingMethodPropertiesAction(ShadingMethodPropertiesFilter.PBR)
}
