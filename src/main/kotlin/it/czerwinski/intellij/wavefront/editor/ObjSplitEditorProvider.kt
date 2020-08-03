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

package it.czerwinski.intellij.wavefront.editor

import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

class ObjSplitEditorProvider : AsyncFileEditorProvider, DumbAware {

    private val textEditorProvider = PsiAwareTextEditorProvider()
    private val previewEditorProvider = ObjPreviewFileEditorProvider()

    override fun accept(project: Project, file: VirtualFile): Boolean =
        textEditorProvider.accept(project, file) && previewEditorProvider.accept(project, file)

    override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    override fun createEditorAsync(
        project: Project,
        file: VirtualFile
    ): AsyncFileEditorProvider.Builder {
        val textEditorBuilder: AsyncFileEditorProvider.Builder =
            getAsyncFileEditorBuilder(textEditorProvider, project, file)
        val previewEditorBuilder: AsyncFileEditorProvider.Builder =
            getAsyncFileEditorBuilder(previewEditorProvider, project, file)

        return object : AsyncFileEditorProvider.Builder() {
            override fun build(): FileEditor = ObjSplitEditor(
                textEditor = textEditorBuilder.build() as TextEditor,
                previewEditor = previewEditorBuilder.build() as ObjPreviewFileEditor
            )
        }
    }

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun getEditorTypeId(): String = "wavefront-obj-split-editor"

    companion object {
        private fun getAsyncFileEditorBuilder(
            provider: FileEditorProvider,
            project: Project,
            file: VirtualFile
        ): AsyncFileEditorProvider.Builder =
            if (provider is AsyncFileEditorProvider) provider.createEditorAsync(project, file)
            else object : AsyncFileEditorProvider.Builder() {
                override fun build(): FileEditor = provider.createEditor(project, file)
            }
    }
}
