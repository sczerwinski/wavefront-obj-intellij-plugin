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

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorProvider
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.TextEditorWithPreview
import com.intellij.openapi.fileEditor.impl.text.TextEditorProvider
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import org.jdom.Element

/**
 * Editor provider for a [BaseSplitEditor].
 */
abstract class BaseSplitEditorProvider(
    private val previewEditorProvider: FileEditorProvider,
    private val textEditorProvider: TextEditorProvider = TextEditorProvider.getInstance()
) : FileEditorProvider {

    /**
     * Returns `true` if the given [file] is accepted by both [textEditorProvider] and [previewEditorProvider].
     */
    final override fun accept(project: Project, file: VirtualFile): Boolean =
        textEditorProvider.accept(project, file) && previewEditorProvider.accept(project, file)

    /**
     * Creates a new [BaseSplitEditor].
     */
    final override fun createEditor(project: Project, file: VirtualFile): FileEditor {
        val textEditor = textEditorProvider.createEditor(project, file) as TextEditor
        val previewEditor = previewEditorProvider.createEditor(project, file)
        return createEditor(textEditor, previewEditor)
    }

    /**
     * Implement this method to create an actual split editor,
     * containing given [textEditor] and [previewEditor].
     */
    protected abstract fun createEditor(textEditor: TextEditor, previewEditor: FileEditor): FileEditor

    /**
     * Deserializes state of split editor.
     */
    override fun readState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState =
        TextEditorWithPreview.MyFileEditorState(
            readSplitLayoutState(sourceElement),
            readTextEditorState(sourceElement, project, file),
            readPreviewEditorState(sourceElement, project, file),
            readVerticalState(sourceElement)
        )

    private fun readSplitLayoutState(sourceElement: Element): TextEditorWithPreview.Layout? =
        sourceElement.getAttribute(SPLIT_LAYOUT)?.value
            ?.let { enumValueOf<TextEditorWithPreview.Layout>(it) }

    private fun readTextEditorState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState? =
        sourceElement.getChild(TEXT_EDITOR)
            ?.let { childElement -> textEditorProvider.readState(childElement, project, file) }

    private fun readPreviewEditorState(sourceElement: Element, project: Project, file: VirtualFile): FileEditorState? =
        sourceElement.getChild(PREVIEW_EDITOR)
            ?.let { childElement -> previewEditorProvider.readState(childElement, project, file) }

    private fun readVerticalState(sourceElement: Element): Boolean =
        sourceElement.getAttribute(IS_VERTICAL)?.value?.toBoolean() ?: false

    /**
     * Serializes state of split editor.
     */
    override fun writeState(state: FileEditorState, project: Project, targetElement: Element) {
        val splitEditorState = state as? TextEditorWithPreview.MyFileEditorState ?: return
        writeSplitLayoutState(splitEditorState.splitLayout, targetElement)
        writeTextEditorState(splitEditorState.firstState, project, targetElement)
        writePreviewEditorState(splitEditorState.secondState, project, targetElement)
        writeVerticalState(splitEditorState.isVerticalSplit, targetElement)
    }

    private fun writeSplitLayoutState(layout: TextEditorWithPreview.Layout?, targetElement: Element) {
        targetElement.setAttribute(SPLIT_LAYOUT, layout?.name)
    }

    private fun writeTextEditorState(state: FileEditorState?, project: Project, targetElement: Element) {
        if (state != null) {
            val childElement = Element(TEXT_EDITOR)
            textEditorProvider.writeState(state, project, childElement)
            targetElement.addContent(childElement)
        }
    }

    private fun writePreviewEditorState(state: FileEditorState?, project: Project, targetElement: Element) {
        if (state != null) {
            val childElement = Element(PREVIEW_EDITOR)
            previewEditorProvider.writeState(state, project, childElement)
            targetElement.addContent(childElement)
        }
    }

    private fun writeVerticalState(isVertical: Boolean, targetElement: Element) {
        targetElement.setAttribute(IS_VERTICAL, isVertical.toString())
    }

    companion object {
        private const val TEXT_EDITOR = "text_editor"
        private const val PREVIEW_EDITOR = "preview_editor"
        private const val SPLIT_LAYOUT = "split_layout"
        private const val IS_VERTICAL = "is_vertical"
    }
}
