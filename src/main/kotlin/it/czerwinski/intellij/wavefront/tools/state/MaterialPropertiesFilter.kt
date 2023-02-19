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

package it.czerwinski.intellij.wavefront.tools.state

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.openapi.components.service
import com.intellij.psi.PsiElement
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Tag
import it.czerwinski.intellij.wavefront.state.APPLICATION_STATE_FILENAME
import it.czerwinski.intellij.wavefront.tools.model.MaterialProperty
import it.czerwinski.intellij.wavefront.tools.model.ShadingMethodPropertiesFilter

@State(
    name = "it.czerwinski.intellij.wavefront.tools.state.MaterialPropertiesFilter",
    storages = [Storage(APPLICATION_STATE_FILENAME)]
)
data class MaterialPropertiesFilter(
    @field:Tag var shadingMethods: Set<ShadingMethodPropertiesFilter> = ShadingMethodPropertiesFilter.DEFAULT_SET
) : PersistentStateComponent<MaterialPropertiesFilter> {

    override fun getState(): MaterialPropertiesFilter = this

    override fun loadState(state: MaterialPropertiesFilter) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun setFrom(other: MaterialPropertiesFilter) {
        this.shadingMethods = other.shadingMethods

        ApplicationManager.getApplication().messageBus
            .syncPublisher(MaterialPropertiesFilterListener.TOPIC)
            .filterChanged(newFilter = this)
    }

    fun matches(materialProperty: MaterialProperty<out PsiElement, out Any?>): Boolean =
        shadingMethods.any { shadingMethod -> shadingMethod.matches(materialProperty.shadingMethods) }

    companion object {

        fun getInstance(): MaterialPropertiesFilter = service()

        val DEFAULT = MaterialPropertiesFilter()
    }
}
