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

package it.czerwinski.intellij.wavefront.actions.ui

import com.intellij.openapi.components.service
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.actions.DiffuseIrradianceMapGenerator
import it.czerwinski.intellij.wavefront.actions.state.GenerateDiffuseIrradianceMapDialogState
import it.czerwinski.intellij.wavefront.settings.ui.textField
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.io.File
import javax.swing.JComponent
import javax.swing.JSlider

class GenerateDiffuseIrradianceMapDialog(project: Project) : BaseGenerateMapDialog(project) {

    private val myState = project.service<GenerateDiffuseIrradianceMapDialogState>()

    private var samplesTextField: JBTextField? = null
    private var samplesSlider: JSlider? = null
    private var suffixTextField: JBTextField? = null

    override val progressIndicatorTitle: String
        get() = WavefrontObjBundle.message("action.GenerateDiffuseIrradianceMapAction.progress")

    init {
        title = WavefrontObjBundle.message(key = "action.GenerateDiffuseIrradianceMapAction.title")
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        samplesRow()
        suffixRow()
    }

    private fun Panel.samplesRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GenerateDiffuseIrradianceMapAction.samples")
        ) {
            samplesTextField = textField(
                defaultValue = myState.samples,
                columns = SAMPLES_MAX.toString().length
            ).component
            samplesSlider = slider(
                min = SAMPLES_MIN,
                max = SAMPLES_MAX,
                minorTickSpacing = SAMPLES_MINOR_TICK,
                majorTickSpacing = SAMPLES_MAJOR_TICK
            ).component
            samplesSlider?.value = myState.samples

            samplesSlider?.addChangeListener { samplesTextField?.text = samplesSlider?.value?.toString().orEmpty() }
            samplesTextField?.addFocusListener(
                object : FocusListener {

                    override fun focusGained(e: FocusEvent?) = Unit

                    override fun focusLost(e: FocusEvent?) {
                        val value = samplesTextField?.text.orEmpty()
                        try {
                            val numberValue = value.toInt()
                            samplesSlider?.value = numberValue.coerceIn(SAMPLES_MIN, SAMPLES_MAX)
                        } catch (e: NumberFormatException) {
                            samplesTextField?.text = samplesSlider?.value?.toString().orEmpty()
                        }
                    }
                }
            )
        }
    }

    private fun Panel.suffixRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GenerateDiffuseIrradianceMapAction.suffix")
        ) {
            suffixTextField = textField(
                defaultValue = myState.suffix,
                columns = myState.suffix.length
            ).component
        }
    }

    override fun doValidate(): ValidationInfo? =
        if (suffixTextField?.text.isNullOrBlank()) {
            val errorText = WavefrontObjBundle.message("action.GenerateDiffuseIrradianceMapAction.suffix.errorEmpty")
            ValidationInfo(errorText, suffixTextField)
        } else if (invalidCharacters.any { character -> character in suffixTextField?.text.orEmpty() }) {
            val errorText = WavefrontObjBundle.message("action.GenerateDiffuseIrradianceMapAction.suffix.errorChar")
            ValidationInfo(errorText, suffixTextField)
        } else {
            super.doValidate()
        }

    override fun beforeProcessFiles() {
        val newState = GenerateDiffuseIrradianceMapDialogState(
            samples = samplesSlider?.value ?: myState.samples,
            suffix = suffixTextField?.text ?: myState.suffix
        )
        myState.setFrom(newState)
    }

    override fun processFile(inputFile: VirtualFile): List<File> =
        listOf(DiffuseIrradianceMapGenerator.generate(inputFile, myState.samples, myState.suffix))

    companion object {
        private const val SAMPLES_MIN = 50
        private const val SAMPLES_MAX = 200
        private const val SAMPLES_MINOR_TICK = 10
        private const val SAMPLES_MAJOR_TICK = 50

        private val invalidCharacters = listOf('\\', '/', ':', '*', '?', '"', '<', '>', '|', '%')
    }
}
