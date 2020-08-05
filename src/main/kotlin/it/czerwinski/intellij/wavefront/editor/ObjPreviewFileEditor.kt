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

import com.intellij.openapi.actionSystem.ActionGroup
import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.util.UserDataHolderBase
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiManager
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.editor.ui.EditorToolbarHeader
import it.czerwinski.intellij.wavefront.editor.ui.EditorWithToolbar
import it.czerwinski.intellij.wavefront.editor.ui.GLPanelWrapper
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import java.beans.PropertyChangeListener
import javax.swing.JComponent

class ObjPreviewFileEditor(
    project: Project,
    virtualFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val glPanel: GLPanelWrapper by lazy { GLPanelWrapper() }

    private val actionToolbar: ActionToolbar by lazy { createActionToolbar() }

    private val _component: JComponent by lazy { createComponent() }

    var upVector: UpVector = UpVector.Z_UP
        private set

    init {
        glPanel.updateObjFile(
            PsiManager.getInstance(project).findFile(virtualFile) as? ObjFile
        )
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
        toolbar.setTargetComponent(glPanel)
        toolbar.setReservePlaceAutoPopupIcon(false)
        return toolbar
    }

    private fun createComponent(): JComponent {
        return EditorWithToolbar(
            toolbarComponent = EditorToolbarHeader(rightActionToolbar = actionToolbar),
            editorComponent = glPanel
        )
    }

    override fun getComponent(): JComponent = _component

    override fun getPreferredFocusedComponent(): JComponent? = _component

    override fun getName(): String =
        WavefrontObjBundle.message("editor.fileTypes.obj.preview.name")

    override fun setState(state: FileEditorState) = Unit

    override fun isModified(): Boolean = false

    override fun isValid(): Boolean = true

    override fun addPropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun removePropertyChangeListener(listener: PropertyChangeListener) = Unit

    override fun getCurrentLocation(): FileEditorLocation? = null

    override fun dispose() {
        Disposer.dispose(glPanel)
    }

    fun triggerUpVectorChange(upVector: UpVector) {
        this.upVector = upVector
        glPanel.updateUpVector(upVector)
        actionToolbar.updateActionsImmediately()
        component.repaint()
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewFileEditor.Toolbar"
    }
}
