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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.Configurable
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import org.jetbrains.annotations.Nls
import javax.swing.JComponent

class WavefrontObjSettingsConfigurable : Configurable {

    private lateinit var component: WavefrontObjSettingsComponent

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String =
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.displayName")

    override fun createComponent(): JComponent? {
        component = WavefrontObjSettingsComponent()
        return component.mainPanel
    }

    override fun getPreferredFocusedComponent(): JComponent =
        component.getPreferredFocusedComponent()

    override fun isModified(): Boolean {
        val settings = WavefrontObjSettingsState.getInstance()
        return component.isVerticalSplit != settings?.isVerticalSplit
    }

    override fun apply() {
        val settings = WavefrontObjSettingsState.getInstance()
        settings?.isVerticalSplit = component.isVerticalSplit
        ApplicationManager.getApplication().messageBus
            .syncPublisher(WavefrontObjSettingsState.SettingsChangedListener.TOPIC)
            .settingsChanged(settings)
    }

    override fun reset() {
        val settings = WavefrontObjSettingsState.getInstance()
        component.isVerticalSplit = settings?.isVerticalSplit ?: false
    }
}
