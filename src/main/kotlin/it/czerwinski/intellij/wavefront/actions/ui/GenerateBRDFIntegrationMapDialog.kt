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

import com.intellij.openapi.fileTypes.FileTypeRegistry
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.actions.BRDFIntegrationMapGenerator
import it.czerwinski.intellij.wavefront.settings.ui.textField
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.io.File
import java.util.Hashtable
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JSlider
import org.intellij.images.fileTypes.ImageFileTypeManager

class GenerateBRDFIntegrationMapDialog(project: Project) : BaseGenerateMapDialog(project) {

    private var sizeTextField: JBTextField? = null
    private var sizeSlider: JSlider? = null
    private var samplesTextField: JBTextField? = null
    private var samplesSlider: JSlider? = null
    private var filenameTextField: JBTextField? = null

    override val progressIndicatorTitle: String
        get() = WavefrontObjBundle.message("action.GenerateBRDFIntegrationMapAction.progress")

    init {
        title = WavefrontObjBundle.message("action.GenerateBRDFIntegrationMapAction.title")
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        sizeRow()
        samplesRow()
        filenameRow()
    }

    private fun Panel.sizeRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GenerateBRDFIntegrationMapAction.size")
        ) {
            sizeTextField = textField(
                defaultValue = 1 shl DEFAULT_SIZE,
                columns = (1 shl SIZE_MAX).toString().length
            ).component
            sizeTextField?.isEditable = false

            sizeSlider = slider(
                min = SIZE_MIN,
                max = SIZE_MAX,
                minorTickSpacing = SIZE_MINOR_TICK,
                majorTickSpacing = SIZE_MAJOR_TICK
            ).component
            sizeSlider?.value = DEFAULT_SIZE
            sizeSlider?.labelTable = Hashtable(sizeLabels.mapValues { (_, label) -> JLabel(label) })

            sizeSlider?.addChangeListener {
                sizeTextField?.text = sizeSlider?.value?.let { value -> 1 shl value }?.toString().orEmpty()
            }
        }
    }

    private fun Panel.samplesRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GenerateBRDFIntegrationMapAction.samples")
        ) {
            samplesTextField = textField(
                defaultValue = DEFAULT_SAMPLES,
                columns = SAMPLES_MAX.toString().length
            ).component
            samplesSlider = slider(
                min = SAMPLES_MIN,
                max = SAMPLES_MAX,
                minorTickSpacing = SAMPLES_MINOR_TICK,
                majorTickSpacing = SAMPLES_MAJOR_TICK
            ).component
            samplesSlider?.value = DEFAULT_SAMPLES

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

    private fun Panel.filenameRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GenerateBRDFIntegrationMapAction.filename")
        ) {
            filenameTextField = textField(
                defaultValue = DEFAULT_FILENAME,
                columns = FILENAME_COLUMNS
            ).component
        }
    }

    override fun doValidate(): ValidationInfo? {
        return validateFilename()
    }

    private fun validateFilename(): ValidationInfo? {
        val filename = filenameTextField?.text.orEmpty()
        val extension = File(filenameTextField?.text.orEmpty()).extension
        val imageFileType = ImageFileTypeManager.getInstance().imageFileType

        val errorText = if (filename.isBlank()) {
            WavefrontObjBundle.message("action.GenerateBRDFIntegrationMapAction.filename.errorEmpty")
        } else if (invalidCharacters.any { character -> character in filename }) {
            WavefrontObjBundle.message("action.GenerateBRDFIntegrationMapAction.filename.errorChar")
        } else if (FileTypeRegistry.getInstance().getFileTypeByExtension(extension) != imageFileType) {
            WavefrontObjBundle.message(
                key = "action.GenerateBRDFIntegrationMapAction.filename.errorType",
                extension
            )
        } else {
            null
        }

        return if (errorText != null) ValidationInfo(errorText, filenameTextField) else super.doValidate()
    }

    override fun processFile(inputFile: VirtualFile): List<File> {
        val size = 1 shl (sizeSlider?.value ?: DEFAULT_SIZE)
        val samples = samplesSlider?.value ?: DEFAULT_SAMPLES
        val filename = filenameTextField?.text ?: DEFAULT_FILENAME

        return listOf(BRDFIntegrationMapGenerator.generate(inputFile, size, samples, filename))
    }

    companion object {
        private const val DEFAULT_SIZE = 9
        private const val DEFAULT_SAMPLES = 100
        private const val DEFAULT_FILENAME = "brdf.png"

        private const val SIZE_MIN = 6
        private const val SIZE_MAX = 12
        private const val SIZE_MINOR_TICK = 1
        private const val SIZE_MAJOR_TICK = 2

        private val sizeLabels = (SIZE_MIN..SIZE_MAX step SIZE_MAJOR_TICK)
            .associateWith { value -> (1 shl value).toString() }

        private const val SAMPLES_MIN = 50
        private const val SAMPLES_MAX = 400
        private const val SAMPLES_MINOR_TICK = 10
        private const val SAMPLES_MAJOR_TICK = 50

        private const val FILENAME_COLUMNS = 16

        private val invalidCharacters = listOf('\\', '/', ':', '*', '?', '"', '<', '>', '|', '%')
    }
}
