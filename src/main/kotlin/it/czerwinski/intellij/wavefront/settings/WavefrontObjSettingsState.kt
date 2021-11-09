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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage
import com.intellij.util.messages.Topic
import com.intellij.util.xmlb.XmlSerializerUtil
import com.intellij.util.xmlb.annotations.Attribute
import com.intellij.util.xmlb.annotations.Property
import it.czerwinski.intellij.common.editor.SplitEditor

@State(
    name = "it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState",
    storages = [Storage("wavefrontObjSettings.xml")]
)
data class WavefrontObjSettingsState(
    @field:Property override var objPreviewSettings: ObjPreviewSettingsState = ObjPreviewSettingsState.DEFAULT,
    @field:Attribute var defaultEditorLayout: SplitEditor.Layout = SplitEditor.Layout.DEFAULT,
    @field:Attribute var isVerticalSplit: Boolean = DEFAULT_VERTICAL_SPLIT
) : PersistentStateComponent<WavefrontObjSettingsState>, ObjPreviewSettingsState.Holder {

    var isHorizontalSplit: Boolean
        get() = !isVerticalSplit
        set(value) { isVerticalSplit = !value }

    override fun getState(): WavefrontObjSettingsState = this

    override fun loadState(state: WavefrontObjSettingsState) {
        XmlSerializerUtil.copyBean(state, this)
    }

    fun setFrom(other: WavefrontObjSettingsState): WavefrontObjSettingsState {
        objPreviewSettings = other.objPreviewSettings
        defaultEditorLayout = other.defaultEditorLayout
        isVerticalSplit = other.isVerticalSplit
        return this
    }

    companion object {

        val DEFAULT = WavefrontObjSettingsState()

        const val DEFAULT_VERTICAL_SPLIT = false

        fun getInstance(): WavefrontObjSettingsState? {
            return ApplicationManager.getApplication().getService(WavefrontObjSettingsState::class.java)
        }
    }

    interface Holder {
        var wavefrontObjSettings: WavefrontObjSettingsState
    }

    interface SettingsChangedListener {

        fun beforeSettingsChanged(newSettings: WavefrontObjSettingsState?) = Unit

        fun settingsChanged(newSettings: WavefrontObjSettingsState?) = Unit

        companion object {
            val TOPIC = Topic.create(
                "WavefrontObjSettingsStateChanged",
                SettingsChangedListener::class.java
            )
        }
    }
}
