/*
 * Copyright 2020-2022 Slawomir Czerwinski
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
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.util.Key
import javax.swing.JComponent

/**
 * Split editor with three possible layouts: text, preview, text and preview (split).
 *
 * This editor can be split either horizontally or vertically.

 * @param P Preview editor type.
 */
@Suppress("TooManyFunctions")
abstract class SplitEditor<P : FileEditor>(
    textEditor: TextEditor,
    previewEditor: P
) : TextAndPreviewEditor<P>(textEditor, previewEditor) {

    protected val myComponent: SplitEditorComponent

    /**
     * Current split editor layout.
     */
    var layout: Layout = Layout.DEFAULT
        set(value) {
            field = value
            myComponent.isShowingTextEditor = value.isShowingTextEditor
            myComponent.isShowingPreviewEditor = value.isShowingPreviewEditor
        }

    /**
     * Indicates whether text and preview editors are split vertically.
     */
    protected var mySplitVertically: Boolean
        get() = myComponent.isSplitVertically
        set(value) {
            myComponent.isSplitVertically = value
        }

    init {
        textEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)
        previewEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)
        myComponent = SplitEditorComponent(textEditor, previewEditor)
    }

    override fun getComponent(): JComponent = myComponent

    override fun getPreferredFocusedComponent(): JComponent? = myComponent.preferredFocusedComponent

    override fun setState(state: FileEditorState) {
        if (state is SplitEditorState) {
            textEditor.setState(state.textEditorState)
            previewEditor.setState(state.previewEditorState)
            state.layout?.let { layout = it }
        }
    }

    override fun getState(level: FileEditorStateLevel): FileEditorState =
        SplitEditorState(
            textEditorState = textEditor.getState(level),
            previewEditorState = previewEditor.getState(level),
            layout = layout
        )

    /**
     * Split editor layout.
     */
    enum class Layout(

        /**
         * Indicates whether the layout includes text editor.
         */
        val isShowingTextEditor: Boolean,

        /**
         * Indicates whether the layout includes preview.
         */
        val isShowingPreviewEditor: Boolean
    ) {
        /**
         * Text only.
         */
        TEXT(isShowingTextEditor = true, isShowingPreviewEditor = false),

        /**
         * Text and preview.
         */
        SPLIT(isShowingTextEditor = true, isShowingPreviewEditor = true),

        /**
         * Preview only
         */
        PREVIEW(isShowingTextEditor = false, isShowingPreviewEditor = true);

        companion object {

            /**
             * Default split editor layout.
             */
            val DEFAULT = TEXT
        }
    }

    private class SplitEditorState(
        val textEditorState: FileEditorState,
        val previewEditorState: FileEditorState,
        layout: Layout?
    ) : FileEditorState {

        private val layoutName: String? = layout?.name

        val layout: Layout? get() = layoutName?.let { Layout.valueOf(it) }

        override fun canBeMergedWith(
            otherState: FileEditorState,
            level: FileEditorStateLevel
        ): Boolean = otherState is SplitEditorState &&
            textEditorState.canBeMergedWith(otherState.textEditorState, level) &&
            previewEditorState.canBeMergedWith(otherState.previewEditorState, level)
    }

    companion object {
        val KEY_PARENT_SPLIT_EDITOR: Key<SplitEditor<*>> = Key.create("SplitEditor.parentSplitEditor")
    }
}
