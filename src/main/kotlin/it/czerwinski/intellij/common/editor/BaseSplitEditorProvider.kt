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

package it.czerwinski.intellij.common.editor

import com.intellij.openapi.fileEditor.AsyncFileEditorProvider
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile

/**
 * Editor provider for a [BaseSplitEditor].
 */
abstract class BaseSplitEditorProvider(
    private val textEditorProvider: TextEditorProvider,
    private val previewEditorProvider: FileEditorProvider
) : AsyncFileEditorProvider {

    /**
     * Returns `true` if the given [file] is accepted by both [textEditorProvider] and [previewEditorProvider].
     */
    final override fun accept(project: Project, file: VirtualFile): Boolean =
        textEditorProvider.accept(project, file) && previewEditorProvider.accept(project, file)

    /**
     * Creates a new [BaseSplitEditor].
     */
    final override fun createEditor(project: Project, file: VirtualFile): FileEditor =
        createEditorAsync(project, file).build()

    /**
     * Creates a new [BaseSplitEditor] asynchronously.
     */
    final override fun createEditorAsync(project: Project, file: VirtualFile): AsyncFileEditorProvider.Builder =
        createAsyncEditorBuilder(project, file)
            .withTextEditorProvider(textEditorProvider)
            .withPreviewEditorProvider(previewEditorProvider)

    /**
     * Implement this method to provide a split editor [Builder].
     */
    protected abstract fun createAsyncEditorBuilder(project: Project, file: VirtualFile): Builder

    /**
     * Split editor builder.
     */
    abstract class Builder(
        private val project: Project,
        private val file: VirtualFile
    ) : AsyncFileEditorProvider.Builder() {

        private lateinit var asyncTextEditorBuilder: AsyncFileEditorProvider.Builder

        private lateinit var asyncPreviewEditorBuilder: AsyncFileEditorProvider.Builder

        /**
         * Sets text editor provider.
         */
        fun withTextEditorProvider(textEditorProvider: TextEditorProvider): Builder {
            asyncTextEditorBuilder = getAsyncFileEditorBuilder(textEditorProvider)
            return this
        }

        private fun getAsyncFileEditorBuilder(provider: FileEditorProvider): AsyncFileEditorProvider.Builder =
            if (provider is AsyncFileEditorProvider) provider.createEditorAsync(project, file)
            else object : AsyncFileEditorProvider.Builder() {
                override fun build(): FileEditor = provider.createEditor(project, file)
            }

        /**
         * Sets preview editor provider.
         */
        fun withPreviewEditorProvider(previewEditorProvider: FileEditorProvider): Builder {
            asyncPreviewEditorBuilder = getAsyncFileEditorBuilder(previewEditorProvider)
            return this
        }

        /**
         * Builds text editor asynchronously.
         */
        protected fun buildTextEditor(): TextEditor = asyncTextEditorBuilder.build() as TextEditor

        /**
         * Builds preview editor asynchronously.
         */
        protected fun buildPreviewEditor(): FileEditor = asyncPreviewEditorBuilder.build()
    }
}
