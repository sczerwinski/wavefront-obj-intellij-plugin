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
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.editor.ui.EditorToolbarHeader
import it.czerwinski.intellij.wavefront.editor.ui.EditorWithToolbar
import it.czerwinski.intellij.wavefront.editor.ui.GLPanelWrapper
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import java.beans.PropertyChangeListener
import javax.swing.JComponent

class ObjPreviewFileEditor(
    project: Project,
    virtualFile: VirtualFile
) : UserDataHolderBase(), FileEditor {

    private val glPanel: GLPanelWrapper by lazy { GLPanelWrapper() }

    private val actionToolbar: ActionToolbar by lazy { createActionToolbar() }

    private val additionalActionToolbar: ActionToolbar by lazy { createAdditionalActionToolbar() }

    private val _component: JComponent by lazy { createComponent() }

    var shadingMethod: ShadingMethod = ShadingMethod.DEFAULT
        private set

    var upVector: UpVector = UpVector.DEFAULT
        private set

    var isShowingAxes: Boolean = ObjPreviewFileEditorSettingsState.DEFAULT_SHOW_AXES
        private set

    var isShowingGrid: Boolean = ObjPreviewFileEditorSettingsState.DEFAULT_SHOW_GRID
        private set

    init {
        val objFile = PsiManager.getInstance(project).findFile(virtualFile) as? ObjFile
        glPanel.updateObjFile(objFile)
        objFile?.let {
            PsiManager.getInstance(objFile.project)
                .addPsiTreeChangeListener(MyPsiTreeChangeListener(objFile), this)
        }
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

    private fun createAdditionalActionToolbar(): ActionToolbar {
        val actionManager = ActionManager.getInstance()

        check(actionManager.isGroup(ADDITIONAL_TOOLBAR_ACTIONS_GROUP_ID)) {
            "Actions group not found: $ADDITIONAL_TOOLBAR_ACTIONS_GROUP_ID"
        }

        val group = actionManager.getAction(ADDITIONAL_TOOLBAR_ACTIONS_GROUP_ID) as ActionGroup
        val toolbar = actionManager.createActionToolbar(
            ActionPlaces.EDITOR_TOOLBAR,
            group,
            true
        )
        toolbar.setTargetComponent(glPanel)
        toolbar.setReservePlaceAutoPopupIcon(false)
        return toolbar
    }

    private fun createComponent(): JComponent = EditorWithToolbar(
        toolbarComponent = EditorToolbarHeader(
            leftActionToolbar = actionToolbar,
            rightActionToolbar = additionalActionToolbar
        ),
        editorComponent = glPanel
    )

    fun initPreview() {
        additionalActionToolbar.updateActionsImmediately()
        glPanel.initPreview()
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

    fun triggerShadingMethodChange(shadingMethod: ShadingMethod) {
        this.shadingMethod = shadingMethod
        glPanel.updateShadingMethod(shadingMethod)
        actionToolbar.updateActionsImmediately()
        component.repaint()
    }

    fun triggerUpVectorChange(upVector: UpVector) {
        this.upVector = upVector
        glPanel.updateUpVector(upVector)
        actionToolbar.updateActionsImmediately()
        component.repaint()
    }

    fun toggleAxes() {
        triggerAxesChange(!isShowingAxes)
    }

    private fun triggerAxesChange(showAxes: Boolean) {
        isShowingAxes = showAxes
        glPanel.updateAxes(showAxes)
        actionToolbar.updateActionsImmediately()
        component.repaint()
    }

    fun toggleGrid() {
        triggerGridChange(!isShowingGrid)
    }

    private fun triggerGridChange(showGrid: Boolean) {
        isShowingGrid = showGrid
        glPanel.updateGrid(showGrid)
        actionToolbar.updateActionsImmediately()
        component.repaint()
    }

    fun triggerSettingsChange(settings: ObjPreviewFileEditorSettingsState) {
        glPanel.updateSceneSettings(settings)
        triggerUpVectorChange(settings.defaultUpVector)
        triggerAxesChange(settings.showAxes)
        triggerGridChange(settings.showGrid)
    }

    fun zoomIn() {
        glPanel.zoomIn()
    }

    fun zoomOut() {
        glPanel.zoomOut()
    }

    fun zoomFit() {
        glPanel.zoomFit()
    }

    private inner class MyPsiTreeChangeListener(private val file: ObjFile) : PsiTreeChangeAdapter() {

        override fun childAdded(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }

        private fun psiTreeChanged(event: PsiTreeChangeEvent) {
            if (event.file == file) {
                glPanel.updateObjFile(file)
            }
        }

        override fun childRemoved(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }

        override fun childReplaced(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }

        override fun childMoved(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }

        override fun propertyChanged(event: PsiTreeChangeEvent) {
            psiTreeChanged(event)
        }
    }

    companion object {
        private const val TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewFileEditor.Toolbar"
        private const val ADDITIONAL_TOOLBAR_ACTIONS_GROUP_ID = "ObjPreviewFileEditor.AdditionalToolbar"
    }
}
