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

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.util.Key
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.ui.JBSplitter
import it.czerwinski.intellij.common.ui.EditorSplitter
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
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

    private val myTextEditorComponent get() = textEditor.component
    private val myPreviewEditorComponent get() = previewEditor.component

    private val mySplitter: JBSplitter
    private val myActionToolbar: ActionToolbar
    private val myComponent: JComponent

    /**
     * Current split editor layout.
     */
    var layout: Layout = Layout.DEFAULT
        protected set

    init {
        mySplitter = createSplitter()
        myActionToolbar = createActionToolbar()
        myComponent = createComponent()
        textEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)
        previewEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)
    }

    private fun createSplitter(): JBSplitter {
        val splitter = EditorSplitter(vertical = false)
        splitter.splitterProportionKey = "${javaClass.simpleName}.Proportion"
        splitter.components = myTextEditorComponent to myPreviewEditorComponent
        return splitter
    }

    private fun createActionToolbar(): ActionToolbar {
        val actionManager = ActionManager.getInstance()

        check(actionManager.isGroup(TOOLBAR_ACTIONS_GROUP_ID)) {
            "Actions group not found: $TOOLBAR_ACTIONS_GROUP_ID"
        }

        val group = actionManager.getAction(TOOLBAR_ACTIONS_GROUP_ID) as ActionGroup
        val toolbar = actionManager.createActionToolbar(
            ActionPlaces.EDITOR_TOOLBAR,
            group,
            true
        )
        toolbar.setTargetComponent(mySplitter)
        toolbar.setReservePlaceAutoPopupIcon(false)
        return toolbar
    }

    private fun createComponent(): JComponent {
        val result = EditorWithToolbar(
            toolbarComponent = EditorToolbarHeader(rightActionToolbar = myActionToolbar),
            editorComponent = mySplitter
        )
        updateEditorsVisibility()
        return result
    }

    private fun updateEditorsVisibility() {
        myTextEditorComponent.isVisible = layout.isShowingTextEditor
        myPreviewEditorComponent.isVisible = layout.isShowingPreviewEditor
        onEditorsVisibilityChanged()
    }

    /**
     * Override this method to handle editor visibility changes.
     */
    protected open fun onEditorsVisibilityChanged() = Unit

    override fun getComponent(): JComponent = myComponent

    override fun getPreferredFocusedComponent(): JComponent? = when {
        layout.isShowingTextEditor -> textEditor.preferredFocusedComponent
        layout.isShowingPreviewEditor -> previewEditor.preferredFocusedComponent
        else -> null
    }

    override fun setState(state: FileEditorState) {
        if (state is SplitEditorState) {
            state.textEditorState?.let(textEditor::setState)
            state.previewEditorState?.let(previewEditor::setState)
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
     * Triggers split editor layout orientation change.
     *
     * `true` means vertical split.
     */
    fun triggerSplitterOrientationChange(newSplitterOrientation: Boolean) {
        mySplitter.orientation = newSplitterOrientation
        component.repaint()
    }

    /**
     * Triggers split editor layout change.
     */
    fun triggerLayoutChange(newLayout: Layout) {
        this.layout = newLayout

        updateEditorsVisibility()
        myActionToolbar.updateActionsImmediately()
        component.repaint()

        preferredFocusedComponent?.let {
            IdeFocusManager.findInstanceByComponent(it).requestFocus(it, true)
        }
    }

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
        val textEditorState: FileEditorState?,
        val previewEditorState: FileEditorState?,
        layout: Layout?
    ) : FileEditorState {

        private val layoutName: String? = layout?.name

        val layout: Layout? get() = layoutName?.let { Layout.valueOf(it) }

        override fun canBeMergedWith(
            otherState: FileEditorState?,
            level: FileEditorStateLevel?
        ): Boolean = otherState is SplitEditorState &&
            (textEditorState?.canBeMergedWith(otherState.textEditorState, level) != false) &&
            (previewEditorState?.canBeMergedWith(otherState.previewEditorState, level) != false)
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "SplitEditor.Toolbar"

        val KEY_PARENT_SPLIT_EDITOR: Key<SplitEditor<*>> = Key.create("SplitEditor.parentSplitEditor")
    }
}
