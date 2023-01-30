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

import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.actions.PreFilteredEnvironmentMapsGenerator
import it.czerwinski.intellij.wavefront.settings.ui.textField
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.io.File
import javax.imageio.ImageIO
import javax.swing.JComponent
import javax.swing.JSlider
import kotlin.math.log2
import kotlin.math.min

class GeneratePreFilteredEnvironmentMapsDialog(project: Project) : BaseGenerateMapDialog(project) {

    private var levelsTextField: JBTextField? = null
    private var levelsSlider: JSlider? = null
    private var samplesTextField: JBTextField? = null
    private var samplesSlider: JSlider? = null
    private var suffixTextField: JBTextField? = null

    override val progressIndicatorTitle: String
        get() = WavefrontObjBundle.message("action.GeneratePreFilteredEnvironmentMapsAction.progress")

    init {
        title = WavefrontObjBundle.message(key = "action.GeneratePreFilteredEnvironmentMapsAction.title")
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        levelsRow()
        samplesRow()
        suffixRow()
    }

    private fun Panel.levelsRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GeneratePreFilteredEnvironmentMapsAction.levels")
        ) {
            levelsTextField = textField(
                defaultValue = DEFAULT_LEVELS,
                columns = LEVELS_MAX.toString().length
            ).component
            levelsSlider = slider(
                min = LEVELS_MIN,
                max = LEVELS_MAX,
                minorTickSpacing = LEVELS_MINOR_TICK,
                majorTickSpacing = LEVELS_MAJOR_TICK
            ).component
            levelsSlider?.value = DEFAULT_LEVELS

            levelsSlider?.addChangeListener { levelsTextField?.text = levelsSlider?.value?.toString().orEmpty() }
            levelsTextField?.addFocusListener(
                object : FocusListener {

                    override fun focusGained(e: FocusEvent?) = Unit

                    override fun focusLost(e: FocusEvent?) {
                        val value = levelsTextField?.text.orEmpty()
                        try {
                            val numberValue = value.toInt()
                            levelsSlider?.value = numberValue.coerceIn(
                                minimumValue = levelsSlider?.minimum ?: LEVELS_MIN,
                                maximumValue = levelsSlider?.maximum ?: LEVELS_MAX
                            )
                        } catch (e: NumberFormatException) {
                            levelsTextField?.text = levelsSlider?.value?.toString().orEmpty()
                        }
                    }
                }
            )
        }
    }

    private fun Panel.samplesRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GeneratePreFilteredEnvironmentMapsAction.samples")
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

    private fun Panel.suffixRow() {
        row(
            label = WavefrontObjBundle.message(key = "action.GeneratePreFilteredEnvironmentMapsAction.suffix")
        ) {
            suffixTextField = textField(
                defaultValue = DEFAULT_SUFFIX,
                columns = DEFAULT_SUFFIX.length
            ).component
        }
    }

    override fun show(inputFiles: List<VirtualFile>) {
        recalculateLevelsConstraints(inputFiles)
        super.show(inputFiles)
    }

    private fun recalculateLevelsConstraints(inputFiles: List<VirtualFile>) {
        val minImageSize = findMinImageSize(inputFiles)
        val maxLevels = log2(minImageSize.toFloat() / MIN_OUTPUT_HEIGHT.toFloat()).toInt()
            .coerceIn(LEVELS_MIN, LEVELS_MAX)
        val defaultLevels = (maxLevels - 1).coerceAtLeast(LEVELS_MIN)

        levelsTextField?.text = defaultLevels.toString()
        levelsSlider?.maximum = maxLevels
        levelsSlider?.value = defaultLevels
    }

    private fun findMinImageSize(inputFiles: List<VirtualFile>): Int =
        inputFiles
            .mapNotNull { file ->
                file.inputStream.use { inputStream ->
                    val imageInputStream = ImageIO.createImageInputStream(inputStream)
                    val imageReaders = ImageIO.getImageReaders(imageInputStream)
                    if (!imageReaders.hasNext()) {
                        return@use null
                    }
                    val imageReader = imageReaders.next()
                    imageReader.input = imageInputStream
                    val width = imageReader.getWidth(0)
                    val height = imageReader.getHeight(0)
                    return@use min(width / 2, height)
                }
            }
            .minOrNull()
            ?: 0

    override fun doValidate(): ValidationInfo? =
        if (suffixTextField?.text.isNullOrBlank()) {
            val errorText = WavefrontObjBundle.message(
                "action.GeneratePreFilteredEnvironmentMapsAction.suffix.errorEmpty"
            )
            ValidationInfo(errorText, suffixTextField)
        } else if (invalidCharacters.any { character -> character in suffixTextField?.text.orEmpty() }) {
            val errorText = WavefrontObjBundle.message(
                "action.GeneratePreFilteredEnvironmentMapsAction.suffix.errorChar"
            )
            ValidationInfo(errorText, suffixTextField)
        } else {
            super.doValidate()
        }

    override fun processFile(inputFile: VirtualFile): List<File> {
        val levels = levelsSlider?.value ?: DEFAULT_LEVELS
        val samples = samplesSlider?.value ?: DEFAULT_SAMPLES
        val outputSuffix = suffixTextField?.text ?: DEFAULT_SUFFIX

        return PreFilteredEnvironmentMapsGenerator.generate(inputFile, levels, samples, outputSuffix)
    }

    companion object {
        private const val DEFAULT_LEVELS = 8
        private const val DEFAULT_SAMPLES = 100
        private const val DEFAULT_SUFFIX = ".reflection"

        private const val MIN_OUTPUT_HEIGHT = 4

        private const val LEVELS_MIN = 2
        private const val LEVELS_MAX = 16
        private const val LEVELS_MINOR_TICK = 1
        private const val LEVELS_MAJOR_TICK = 2

        private const val SAMPLES_MIN = 50
        private const val SAMPLES_MAX = 400
        private const val SAMPLES_MINOR_TICK = 10
        private const val SAMPLES_MAJOR_TICK = 50

        private val invalidCharacters = listOf('\\', '/', ':', '*', '?', '"', '<', '>', '|', '%')
    }
}
