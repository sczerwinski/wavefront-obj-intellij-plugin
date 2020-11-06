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

@file:Suppress("TooManyFunctions")

package it.czerwinski.intellij.wavefront.editor

import com.intellij.ide.structureView.StructureViewBuilder
import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.fileEditor.FileEditorStateLevel
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.Key
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.IdeFocusManager
import com.intellij.pom.Navigatable
import com.intellij.ui.JBSplitter
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.SplitEditorLayout
import it.czerwinski.intellij.wavefront.editor.ui.EditorSplitter
import it.czerwinski.intellij.wavefront.editor.ui.EditorToolbarHeader
import it.czerwinski.intellij.wavefront.editor.ui.EditorWithToolbar
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState
import java.beans.PropertyChangeListener
import javax.swing.JComponent

class ObjSplitEditor(
    val textEditor: TextEditor,
    val previewEditor: ObjPreviewFileEditor
) : UserDataHolderBase(), TextEditor {

    private val settingsState get() = WavefrontObjSettingsState.getInstance()

    private val textEditorComponent get() = textEditor.component
    private val previewEditorComponent get() = previewEditor.component

    private val splitter: JBSplitter by lazy { createSplitter() }

    private val actionToolbar: ActionToolbar by lazy { createActionToolbar() }

    private val _component: JComponent by lazy { createComponent() }

    var splitEditorLayout: SplitEditorLayout =
        settingsState?.defaultEditorLayout ?: SplitEditorLayout.TEXT
        private set

    init {
        updateEditorsVisibility()

        textEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)
        previewEditor.putUserData(KEY_PARENT_SPLIT_EDITOR, this)

        ApplicationManager.getApplication().messageBus.connect(this).subscribe(
            WavefrontObjSettingsState.SettingsChangedListener.TOPIC,
            object : WavefrontObjSettingsState.SettingsChangedListener {
                override fun settingsChanged(settings: WavefrontObjSettingsState?) {
                    splitEditorLayout =
                        if (settings?.isPreviewDisabled == true) SplitEditorLayout.TEXT
                        else settings?.defaultEditorLayout ?: SplitEditorLayout.TEXT
                    splitter.orientation = settings?.isVerticalSplit ?: false
                    component.repaint()
                }
            }
        )
    }

    private fun createSplitter(): JBSplitter {
        val splitter = EditorSplitter(vertical = settingsState?.isVerticalSplit ?: false)
        splitter.splitterProportionKey = "ObjSplitEditor.Proportion"
        splitter.components = textEditorComponent to previewEditorComponent
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
        toolbar.setTargetComponent(splitter)
        toolbar.setReservePlaceAutoPopupIcon(false)
        return toolbar
    }

    private fun createComponent(): JComponent {
        val result = EditorWithToolbar(
            toolbarComponent = EditorToolbarHeader(rightActionToolbar = actionToolbar),
            editorComponent = splitter
        )
        updateEditorsVisibility()
        return result
    }

    private fun updateEditorsVisibility() {
        textEditorComponent.isVisible = splitEditorLayout.isShowingTextEditor
        previewEditorComponent.isVisible = splitEditorLayout.isShowingPreviewEditor
        if (splitEditorLayout.isShowingPreviewEditor) {
            previewEditor.initPreview()
        }
    }

    override fun getComponent(): JComponent = _component

    override fun getPreferredFocusedComponent(): JComponent? = when {
        splitEditorLayout.isShowingTextEditor -> textEditor.preferredFocusedComponent
        splitEditorLayout.isShowingPreviewEditor -> previewEditor.preferredFocusedComponent
        else -> null
    }

    override fun getStructureViewBuilder(): StructureViewBuilder? = textEditor.structureViewBuilder

    override fun getName(): String =
        WavefrontObjBundle.message("editor.fileTypes.obj.split.name")

    override fun getFile(): VirtualFile? = textEditor.file

    override fun getEditor(): Editor = textEditor.editor

    override fun canNavigateTo(navigatable: Navigatable): Boolean =
        textEditor.canNavigateTo(navigatable)

    override fun navigateTo(navigatable: Navigatable) {
        textEditor.navigateTo(navigatable)
    }

    override fun getState(level: FileEditorStateLevel): FileEditorState =
        ObjSplitEditorState(
            textEditorState = textEditor.getState(level),
            previewEditorState = previewEditor.getState(level),
            layout = splitEditorLayout
        )

    override fun setState(state: FileEditorState) {
        if (state is ObjSplitEditorState) {
            state.textEditorState?.let(textEditor::setState)
            state.previewEditorState?.let(previewEditor::setState)
            state.layout?.let { splitEditorLayout = it }
        }
    }

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

    override fun getCurrentLocation(): FileEditorLocation? = textEditor.currentLocation

    override fun dispose() {
        Disposer.dispose(textEditor)
        Disposer.dispose(previewEditor)
    }

    fun triggerSplitEditorLayoutChange(splitEditorLayout: SplitEditorLayout) {
        this.splitEditorLayout = splitEditorLayout

        updateEditorsVisibility()
        actionToolbar.updateActionsImmediately()
        component.repaint()

        preferredFocusedComponent?.let {
            IdeFocusManager.findInstanceByComponent(it).requestFocus(it, true)
        }
    }

    class ObjSplitEditorState(
        val textEditorState: FileEditorState?,
        val previewEditorState: FileEditorState?,
        layout: SplitEditorLayout?
    ) : FileEditorState {

        private val layoutName: String? = layout?.name

        val layout: SplitEditorLayout? get() = layoutName?.let { SplitEditorLayout.valueOf(it) }

        override fun canBeMergedWith(
            otherState: FileEditorState?,
            level: FileEditorStateLevel?
        ): Boolean = otherState is ObjSplitEditorState &&
            (textEditorState?.canBeMergedWith(otherState.textEditorState, level) != false) &&
            (previewEditorState?.canBeMergedWith(otherState.previewEditorState, level) != false)
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "ObjSplitEditor.Toolbar"

        val KEY_PARENT_SPLIT_EDITOR: Key<ObjSplitEditor> =
            Key.create("ObjSplitEditor.parentSplitEditor")
    }
}
