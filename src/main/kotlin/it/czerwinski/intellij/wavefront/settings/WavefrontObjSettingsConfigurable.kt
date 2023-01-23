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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.options.SearchableConfigurable
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import javax.swing.JComponent
import org.jetbrains.annotations.Nls

class WavefrontObjSettingsConfigurable : SearchableConfigurable {

    private lateinit var component: WavefrontObjSettingsComponent

    private val settings = WavefrontObjSettingsState.getInstance()

    override fun getId(): String = "Settings.WavefrontObj"

    @Nls(capitalization = Nls.Capitalization.Title)
    override fun getDisplayName(): String =
        WavefrontObjBundle.message("settings.editor.fileTypes.obj.displayName")

    override fun createComponent(): JComponent {
        component = WavefrontObjSettingsComponent()
        return component.getComponent()
    }

    override fun getPreferredFocusedComponent(): JComponent =
        component.getPreferredFocusedComponent()

    override fun isModified(): Boolean {
        return ::component.isInitialized && settings != component.wavefrontObjSettings
    }

    override fun apply() {
        component.validateForm()
        val messageBus = ApplicationManager.getApplication().messageBus
        messageBus.syncPublisher(WavefrontObjSettingsState.SettingsChangedListener.TOPIC)
            .beforeSettingsChanged(component.wavefrontObjSettings)
        settings?.setFrom(component.wavefrontObjSettings)
        messageBus.syncPublisher(WavefrontObjSettingsState.SettingsChangedListener.TOPIC)
            .settingsChanged(settings)
    }

    override fun reset() {
        component.wavefrontObjSettings = settings ?: WavefrontObjSettingsState.DEFAULT
    }
}
