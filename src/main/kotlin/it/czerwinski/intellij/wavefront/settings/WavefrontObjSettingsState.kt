/*
 * Copyright 2020 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute
import it.czerwinski.intellij.wavefront.editor.model.SplitEditorLayout

@State(
    name = "it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState",
    storages = [Storage("wavefrontObjSettings.xml")]
)
class WavefrontObjSettingsState : PersistentStateComponent<WavefrontObjSettingsState> {

    @field:Attribute
    var isPreviewDisabled: Boolean = false

    @field:Attribute
    var defaultEditorLayout: SplitEditorLayout = SplitEditorLayout.TEXT

    @field:Attribute
    var isVerticalSplit: Boolean = false

    override fun getState(): WavefrontObjSettingsState = this

    override fun loadState(state: WavefrontObjSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    companion object {
        fun getInstance(): WavefrontObjSettingsState? {
            return ServiceManager.getService(WavefrontObjSettingsState::class.java)
        }
    }

    interface SettingsChangedListener {

        fun settingsChanged(settings: WavefrontObjSettingsState?)

        companion object {
            val TOPIC = Topic.create(
                "WavefrontObjSettingsStateChanged",
                SettingsChangedListener::class.java
            )
        }
    }
}
