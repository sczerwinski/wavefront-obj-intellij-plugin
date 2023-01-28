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

import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.progressSink
import com.intellij.openapi.progress.withModalProgressIndicator
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.ui.ValidationInfo
import com.intellij.openapi.vcs.vfs.VcsFileSystem
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.ui.components.JBTextField
import com.intellij.ui.dsl.builder.Panel
import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.actions.DiffuseIrradianceMapGenerator
import it.czerwinski.intellij.wavefront.settings.ui.textField
import java.awt.event.FocusEvent
import java.awt.event.FocusListener
import java.io.File
import javax.swing.JComponent
import javax.swing.JSlider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class GenerateDiffuseIrradianceMapDialog(private val project: Project) : DialogWrapper(project) {

    private var samplesTextField: JBTextField? = null
    private var samplesSlider: JSlider? = null
    private var suffixTextField: JBTextField? = null

    private var inputFiles: List<VirtualFile> = emptyList()

    init {
        title = WavefrontObjBundle.message(
            key = "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.title"
        )
        init()
    }

    override fun createCenterPanel(): JComponent = panel {
        samplesRow()
        suffixRow()
    }

    private fun Panel.samplesRow() {
        row(
            label = WavefrontObjBundle.message(
                key = "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.samples"
            )
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
            label = WavefrontObjBundle.message(
                key = "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.suffix"
            )
        ) {
            suffixTextField = textField(
                defaultValue = DEFAULT_SUFFIX,
                columns = DEFAULT_SUFFIX.length
            ).component
        }
    }

    fun show(inputFiles: List<VirtualFile>) {
        this.inputFiles = inputFiles
        show()
    }

    override fun doValidate(): ValidationInfo? =
        if (suffixTextField?.text.isNullOrBlank()) {
            val errorText = WavefrontObjBundle.message(
                "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.suffix.errorEmpty"
            )
            ValidationInfo(errorText, suffixTextField)
        } else if (invalidCharacters.any { character -> character in suffixTextField?.text.orEmpty() }) {
            val errorText = WavefrontObjBundle.message(
                "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.suffix.errorChar"
            )
            ValidationInfo(errorText, suffixTextField)
        } else {
            super.doValidate()
        }

    override fun doOKAction() {
        val samples = samplesSlider?.value ?: DEFAULT_SAMPLES
        val outputSuffix = suffixTextField?.text ?: DEFAULT_SUFFIX

        CoroutineScope(Job() + Dispatchers.Unconfined).launch {
            @Suppress("UnstableApiUsage")
            val outputFiles = withModalProgressIndicator(
                project,
                WavefrontObjBundle.message(
                    "action.it.czerwinski.intellij.wavefront.actions.GenerateDiffuseIrradianceMapAction.progress"
                )
            ) {
                progressSink?.fraction(0.0)
                inputFiles.mapIndexed { index, inputFile ->
                    progressSink?.text(inputFile.name)
                    progressSink?.fraction(index.toDouble() / inputFiles.size.toDouble())
                    DiffuseIrradianceMapGenerator.generate(inputFile, samples, outputSuffix)
                }
            }
            invokeLater {
                refreshFiles(outputFiles)
            }
        }

        super.doOKAction()
    }

    private fun refreshFiles(outputFiles: List<File>) {
        val virtualOutputFile = runReadAction {
            val dirs = inputFiles.mapNotNull { it.parent }.distinct()
            LocalFileSystem.getInstance().refreshFiles(dirs)
            VcsFileSystem.getInstance().refresh(true)
            LocalFileSystem.getInstance().findFileByIoFile(outputFiles.first())
        }
        if (virtualOutputFile != null) {
            val project = ProjectLocator.getInstance().guessProjectForFile(virtualOutputFile)
            if (project != null) {
                FileEditorManager.getInstance(project).openFile(virtualOutputFile, true)
            }
        }
    }

    companion object {
        private const val DEFAULT_SAMPLES = 100
        private const val DEFAULT_SUFFIX = ".irradiance"

        private const val SAMPLES_MIN = 50
        private const val SAMPLES_MAX = 200
        private const val SAMPLES_MINOR_TICK = 10
        private const val SAMPLES_MAJOR_TICK = 50

        private val invalidCharacters = listOf('\\', '/', ':', '*', '?', '"', '<', '>', '|', '%')
    }
}
