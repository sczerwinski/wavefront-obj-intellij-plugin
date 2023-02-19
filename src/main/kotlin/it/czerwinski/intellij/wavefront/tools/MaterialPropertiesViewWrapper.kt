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

package it.czerwinski.intellij.wavefront.tools

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.editor.CaretModel
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.openapi.wm.ToolWindow
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.content.ContentFactory
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.editor.MtlSplitEditor
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.tools.ui.MaterialPropertiesTable
import it.czerwinski.intellij.wavefront.tools.ui.MaterialPropertiesTableModel

class MaterialPropertiesViewWrapper(
    private val project: Project,
    private val toolWindow: ToolWindow
) : FileEditorManagerListener, Disposable {

    private val disposable: Disposable get() = toolWindow.disposable

    private val myFileEditorManagerListener = MyFileEditorManagerListener()

    private val myMessageBusConnection = project.messageBus.connect(disposable)

    private val myTableModel = MaterialPropertiesTableModel()
    private val myTable = MaterialPropertiesTable(
        project = project,
        model = myTableModel,
        parent = disposable
    )

    init {
        Disposer.register(toolWindow.disposable, this)

        val contentFactory = ContentFactory.getInstance()
        val tableContent = contentFactory.createContent(JBScrollPane(myTable), null, false)

        toolWindow.contentManager.addContent(tableContent)

        myTable.isEnabled = false

        myMessageBusConnection.subscribe(FileEditorManagerListener.FILE_EDITOR_MANAGER, myFileEditorManagerListener)
    }

    private fun updateMaterial(material: MtlMaterialElement?) {
        myTableModel.updateMaterial(material)
        myTable.isEnabled = material != null
    }

    override fun dispose() {
        myMessageBusConnection.disconnect()
    }

    private inner class MyFileEditorManagerListener : FileEditorManagerListener {

        private var myCaretListener: MyCaretListener? = null
        private var myPsiTreeChangeListener: MyPsiTreeChangeListener? = null

        override fun selectionChanged(event: FileEditorManagerEvent) {
            cleanUpOldEditor(event.oldEditor)

            val mtlSplitEditor = findMtlSplitEditor(event.newEditor) ?: return
            val mtlFile = findMtlFile(event.newFile) ?: return

            setUpNewEditor(mtlFile, mtlSplitEditor)
        }

        private fun cleanUpOldEditor(oldEditor: FileEditor?) {
            val psiManager = PsiManager.getInstance(project)
            myPsiTreeChangeListener?.let { listener -> psiManager.removePsiTreeChangeListener(listener) }

            val oldCaretListener = myCaretListener
            if (oldCaretListener != null) {
                SplitEditor.KEY_CARET_MODEL[oldEditor]?.removeCaretListener(oldCaretListener)
                myCaretListener = null
            }
            updateMaterial(material = null)
        }

        private fun findMtlSplitEditor(editor: FileEditor?): MtlSplitEditor? =
            editor as? MtlSplitEditor ?: SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor] as? MtlSplitEditor

        private fun findMtlFile(file: VirtualFile?): MtlFile? {
            return if (file == null) {
                null
            } else {
                val psiManager = PsiManager.getInstance(project)
                runReadAction { psiManager.findFile(file) as? MtlFile }
            }
        }

        private fun setUpNewEditor(
            mtlFile: MtlFile,
            mtlSplitEditor: MtlSplitEditor
        ) {
            val newCaretListener = MyCaretListener(mtlFile)
            val caretModel = SplitEditor.KEY_CARET_MODEL[mtlSplitEditor]
            caretModel?.addCaretListener(newCaretListener)
            newCaretListener.updateCaretOffset(offset = caretModel?.offset ?: 0)
            myCaretListener = newCaretListener

            val psiManager = PsiManager.getInstance(project)
            myPsiTreeChangeListener = MyPsiTreeChangeListener(mtlFile, caretModel).also { listener ->
                psiManager.addPsiTreeChangeListener(listener, disposable)
            }
        }

        private inner class MyPsiTreeChangeListener(
            private val mtlFile: MtlFile,
            private val caretModel: CaretModel
        ) : PsiTreeChangeAdapter() {

            private fun handlePsiTreeChange(element: PsiElement?) {
                if (element == mtlFile) {
                    myCaretListener?.updateCaretOffset(caretModel.offset)
                }
            }

            override fun childrenChanged(event: PsiTreeChangeEvent) {
                handlePsiTreeChange(event.file)
            }
        }

        private inner class MyCaretListener(private val mtlFile: MtlFile) : CaretListener {

            private val myMaterials get() = mtlFile.materials

            override fun caretPositionChanged(event: CaretEvent) {
                updateCaretOffset(offset = event.editor.caretModel.offset)
            }

            fun updateCaretOffset(offset: Int) {
                updateMaterial(material = findMaterialAtOffset(offset))
            }

            private fun findMaterialAtOffset(offset: Int): MtlMaterialElement? =
                myMaterials.lastOrNull { material -> material.textRange.startOffset <= offset }
        }
    }
}
