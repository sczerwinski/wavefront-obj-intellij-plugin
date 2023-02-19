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

package it.czerwinski.intellij.wavefront.tools.model

import com.intellij.openapi.application.ApplicationManager
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.tools.state.MaterialPropertiesFilter
import it.czerwinski.intellij.wavefront.tools.state.MaterialPropertiesFilterListener

class MaterialPropertiesModelImpl : MaterialPropertiesModel {

    private val materialPropertiesListeners = mutableListOf<MaterialPropertiesModel.MaterialPropertiesListener>()

    init {
        ApplicationManager.getApplication().messageBus
            .connect()
            .subscribe(MaterialPropertiesFilterListener.TOPIC, MyMaterialPropertiesFilterListener())
    }

    override var materialProperties: List<MaterialProperty<out PsiElement, out Any?>> =
        allMaterialProperties.filter { property -> MaterialPropertiesFilter.getInstance().matches(property) }
        private set

    override fun addMaterialPropertiesListener(listener: MaterialPropertiesModel.MaterialPropertiesListener) {
        materialPropertiesListeners.add(listener)
    }

    override fun removeMaterialPropertiesListener(listener: MaterialPropertiesModel.MaterialPropertiesListener) {
        materialPropertiesListeners.remove(listener)
    }

    private fun notifyMaterialPropertiesChanged() {
        for (listener in materialPropertiesListeners) {
            listener.materialPropertiesChanged()
        }
    }

    private inner class MyMaterialPropertiesFilterListener : MaterialPropertiesFilterListener {

        override fun filterChanged(newFilter: MaterialPropertiesFilter) {
            materialProperties = allMaterialProperties.filter { property -> newFilter.matches(property) }
            notifyMaterialPropertiesChanged()
        }
    }
}
