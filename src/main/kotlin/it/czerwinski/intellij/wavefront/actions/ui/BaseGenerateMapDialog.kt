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
import com.intellij.openapi.vcs.vfs.VcsFileSystem
import com.intellij.openapi.vfs.LocalFileSystem
import com.intellij.openapi.vfs.VirtualFile
import java.io.File
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

abstract class BaseGenerateMapDialog(
    protected val project: Project
) : DialogWrapper(project), GenerateMapDialog {

    private var inputFiles: List<VirtualFile> = emptyList()

    protected abstract val progressIndicatorTitle: String

    override fun show(inputFiles: List<VirtualFile>) {
        this.inputFiles = inputFiles
        show()
    }

    override fun doOKAction() {
        beforeProcessFiles()
        CoroutineScope(Job() + Dispatchers.Unconfined).launch {
            @Suppress("UnstableApiUsage")
            val outputFiles = withModalProgressIndicator(project, progressIndicatorTitle) {
                progressSink?.fraction(0.0)
                inputFiles.flatMapIndexed { index, inputFile ->
                    progressSink?.text(inputFile.name)
                    progressSink?.fraction(index.toDouble() / inputFiles.size.toDouble())
                    processFile(inputFile)
                }
            }
            invokeLater {
                handleOutputFiles(outputFiles)
            }
        }

        super.doOKAction()
    }

    protected abstract fun beforeProcessFiles()

    protected abstract fun processFile(inputFile: VirtualFile): List<File>

    private fun handleOutputFiles(outputFiles: List<File>) {
        val virtualOutputFile = runReadAction {
            val dirs = inputFiles.mapNotNull { it.parent }.distinct()
            LocalFileSystem.getInstance().refreshFiles(inputFiles + dirs)
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
}
