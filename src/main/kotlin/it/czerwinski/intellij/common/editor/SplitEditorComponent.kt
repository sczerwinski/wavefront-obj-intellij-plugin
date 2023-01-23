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

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.ui.JBSplitter
import it.czerwinski.intellij.common.ui.ActionToolbarBuilder
import it.czerwinski.intellij.common.ui.EditorSplitter
import it.czerwinski.intellij.common.ui.EditorToolbarHeader
import it.czerwinski.intellij.common.ui.EditorWithToolbar
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * Split editor UI component.
 */
class SplitEditorComponent(
    private val textEditor: TextEditor,
    private val previewEditor: FileEditor
) : JPanel(BorderLayout()) {

    private val myTextEditorComponent get() = textEditor.component
    private val myPreviewEditorComponent get() = previewEditor.component

    private val mySplitter: JBSplitter
    private val myActionToolbar: ActionToolbar

    private val myEditorsVisibilityListeners = mutableListOf<EditorsVisibilityListener>()

    /**
     * Indicates whether the text editor is being shown.
     */
    var isShowingTextEditor: Boolean = false
        set(value) {
            field = value
            myTextEditorComponent.isVisible = value
            myActionToolbar.updateActionsImmediately()
            notifyEditorsVisibilityChanged()
            repaint()
        }

    /**
     * Indicates whether the preview editor is being shown.
     */
    var isShowingPreviewEditor: Boolean = false
        set(value) {
            field = value
            myPreviewEditorComponent.isVisible = value
            myActionToolbar.updateActionsImmediately()
            notifyEditorsVisibilityChanged()
            repaint()
        }

    /**
     * Indicates whether the splitter should be vertical.
     */
    var isSplitVertically: Boolean = false
        set(value) {
            field = value
            mySplitter.orientation = value
            repaint()
        }

    /**
     * Preferred focused component when gaining focus.
     */
    val preferredFocusedComponent: JComponent?
        get() = when {
            isShowingTextEditor -> textEditor.preferredFocusedComponent
            isShowingPreviewEditor -> previewEditor.preferredFocusedComponent
            else -> null
        }

    init {
        mySplitter = createSplitter()
        myActionToolbar = ActionToolbarBuilder()
            .setActionGroupId(TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(mySplitter)
            .build()
        layoutChildren()
    }

    private fun createSplitter(): JBSplitter {
        val splitter = EditorSplitter(vertical = false)
        splitter.splitterProportionKey = "${previewEditor.javaClass.simpleName}.Proportion"
        splitter.components = myTextEditorComponent to myPreviewEditorComponent
        return splitter
    }

    private fun layoutChildren() {
        val editorWithToolbar = EditorWithToolbar(
            toolbarComponent = EditorToolbarHeader(rightActionToolbar = myActionToolbar),
            editorComponent = mySplitter
        )
        add(editorWithToolbar, BorderLayout.CENTER)
    }

    /**
     * Adds a text and preview editors visibility [listener] to this component.
     */
    fun addEditorsVisibilityListener(listener: EditorsVisibilityListener) {
        myEditorsVisibilityListeners.add(listener)
    }

    /**
     * Removes a text and preview editors visibility [listener] from this component.
     */
    fun removeEditorsVisibilityListener(listener: EditorsVisibilityListener) {
        myEditorsVisibilityListeners.remove(listener)
    }

    private fun notifyEditorsVisibilityChanged() {
        for (listener in myEditorsVisibilityListeners) {
            listener.editorsVisibilityChanged(isShowingTextEditor, isShowingPreviewEditor)
        }
    }

    /**
     * Text and preview editors visibility listener.
     */
    fun interface EditorsVisibilityListener {

        /**
         * Triggered when editors visibility changes.
         */
        fun editorsVisibilityChanged(isShowingTextEditor: Boolean, isShowingPreviewEditor: Boolean)
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "SplitEditor.Toolbar"
    }
}
