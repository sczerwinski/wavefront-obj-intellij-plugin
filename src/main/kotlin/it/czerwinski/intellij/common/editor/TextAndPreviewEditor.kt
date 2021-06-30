/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

import com.intellij.codeHighlighting.BackgroundEditorHighlighter
import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.pom.Navigatable
import java.beans.PropertyChangeListener

/**
 * Compound file editor with text editor and preview.
 *
 * This editor does not implement [getComponent]. Its implementations can arrange
 * the text editor and the preview into any layout.
 *
 * @param P Preview editor type.
 */
@Suppress("TooManyFunctions")
abstract class TextAndPreviewEditor<P : FileEditor>(
    val textEditor: TextEditor,
    val previewEditor: P
) : UserDataHolderBase(), TextEditor {

    override fun isModified(): Boolean = textEditor.isModified || previewEditor.isModified

    override fun isValid(): Boolean = textEditor.isValid && previewEditor.isValid

    override fun selectNotify() {
        textEditor.selectNotify()
        previewEditor.selectNotify()
    }

    override fun deselectNotify() {
        previewEditor.deselectNotify()
        previewEditor.deselectNotify()
    }

    override fun addPropertyChangeListener(listener: PropertyChangeListener) {
        textEditor.addPropertyChangeListener(listener)
        previewEditor.addPropertyChangeListener(listener)
    }

    override fun removePropertyChangeListener(listener: PropertyChangeListener) {
        textEditor.removePropertyChangeListener(listener)
        previewEditor.removePropertyChangeListener(listener)
    }

    override fun getBackgroundHighlighter(): BackgroundEditorHighlighter? = textEditor.backgroundHighlighter

    override fun getCurrentLocation(): FileEditorLocation? = textEditor.currentLocation

    override fun getStructureViewBuilder(): StructureViewBuilder? = textEditor.structureViewBuilder

    override fun getFile(): VirtualFile? = textEditor.file

    override fun canNavigateTo(navigatable: Navigatable): Boolean =
        textEditor.canNavigateTo(navigatable)

    override fun navigateTo(navigatable: Navigatable) {
        textEditor.navigateTo(navigatable)
    }

    override fun getEditor(): Editor = textEditor.editor

    override fun dispose() {
        Disposer.dispose(textEditor)
        Disposer.dispose(previewEditor)
    }
}
