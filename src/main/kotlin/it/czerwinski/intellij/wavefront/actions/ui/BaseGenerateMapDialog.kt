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

import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.progress.ProgressIndicator
import com.intellij.openapi.progress.ProgressManager
import com.intellij.openapi.progress.Task
import com.intellij.openapi.project.Project
import com.intellij.openapi.project.ProjectLocator
import com.intellij.openapi.ui.DialogWrapper
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import java.io.File

abstract class BaseGenerateMapDialog(
    protected val project: Project
) : DialogWrapper(project), GenerateMapDialog {

    private var inputFiles: List<VirtualFile> = emptyList()

    private val isMultipleFiles: Boolean get() = inputFiles.size > 1

    protected abstract val progressIndicatorTitle: String

    override fun show(inputFiles: List<VirtualFile>) {
        this.inputFiles = inputFiles
        show()
    }

    override fun doOKAction() {
        super.doOKAction()
        beforeProcessFiles()
        val outputFiles = ProgressManager.getInstance().run(ProcessFilesTask())
        handleOutputFiles(outputFiles)
    }

    protected abstract fun beforeProcessFiles()

    protected abstract fun processFile(inputFile: VirtualFile): List<File>

    private fun handleOutputFiles(outputFiles: List<File>) {
        val virtualOutputFile = runReadAction {
            val dirs = (inputFiles.filter { it.isDirectory } + inputFiles.mapNotNull { it.parent }).distinct()
            LocalFileSystem.getInstance().refreshFiles(dirs)
            LocalFileSystem.getInstance().findFileByIoFile(outputFiles.first())
        }
        if (virtualOutputFile != null) {
            val project = ProjectLocator.getInstance().guessProjectForFile(virtualOutputFile)
            if (project != null) {
                FileEditorManager.getInstance(project).openFile(virtualOutputFile, true)
            }
        }
    }

    private inner class ProcessFilesTask : Task.WithResult<List<File>, Exception>(
        project,
        progressIndicatorTitle,
        true
    ) {

        override fun compute(indicator: ProgressIndicator): List<File> {
            indicator.isIndeterminate = !isMultipleFiles

            return inputFiles.flatMapIndexed { index, inputFile ->
                if (indicator.isCanceled) {
                    emptyList()
                } else {
                    if (isMultipleFiles) {
                        indicator.fraction = index.toDouble() / inputFiles.size.toDouble()
                        indicator.text = WavefrontObjBundle.message(
                            key = "action.BaseGenerateMapAction.progressText",
                            inputFile.name
                        )
                    }
                    processFile(inputFile)
                }
            }
        }
    }
}
