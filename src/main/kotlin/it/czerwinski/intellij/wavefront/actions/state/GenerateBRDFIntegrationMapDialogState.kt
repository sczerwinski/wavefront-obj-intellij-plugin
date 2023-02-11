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

package it.czerwinski.intellij.wavefront.actions.state

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute
import it.czerwinski.intellij.wavefront.state.PROJECT_STATE_FILENAME

@State(
    name = "it.czerwinski.intellij.wavefront.actions.ui.GenerateBRDFIntegrationMapDialog",
    storages = [Storage(PROJECT_STATE_FILENAME)]
)
data class GenerateBRDFIntegrationMapDialogState(
    @field:Attribute var size: Int = DEFAULT_SIZE,
    @field:Attribute var samples: Int = DEFAULT_SAMPLES,
    @field:Attribute var filename: String = DEFAULT_FILENAME
) : PersistentStateComponent<GenerateBRDFIntegrationMapDialogState> {

    override fun getState(): GenerateBRDFIntegrationMapDialogState = this

    override fun loadState(state: GenerateBRDFIntegrationMapDialogState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun setFrom(other: GenerateBRDFIntegrationMapDialogState) {
        this.size = other.size
        this.samples = other.samples
        this.filename = other.filename
    }

    companion object {
        private const val DEFAULT_SIZE = 9
        private const val DEFAULT_SAMPLES = 100
        private const val DEFAULT_FILENAME = "brdf.png"
    }
}
