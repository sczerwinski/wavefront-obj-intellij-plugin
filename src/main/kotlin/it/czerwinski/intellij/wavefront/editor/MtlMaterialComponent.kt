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

package it.czerwinski.intellij.wavefront.editor

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBLabel
import com.intellij.ui.components.JBLoadingPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.panel
import com.intellij.ui.table.JBTable
import it.czerwinski.intellij.common.ui.EditorSplitter
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.ui.MaterialComboBoxModel
import it.czerwinski.intellij.wavefront.editor.ui.MaterialListCellRenderer
import it.czerwinski.intellij.wavefront.editor.ui.MaterialPropertiesTableCellRenderer
import it.czerwinski.intellij.wavefront.editor.ui.MaterialPropertiesTableModel
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import java.awt.BorderLayout
import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JPanel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener
import javax.swing.table.TableCellRenderer

class MtlMaterialComponent(
    private val project: Project,
    private val file: VirtualFile,
    editor: MtlMaterialEditor
) : JBLoadingPanel(BorderLayout(), editor), Disposable {

    private val isInitialized = AtomicBoolean(false)

    private var psiTreeChangeListener: MtlMaterialComponent.MyPsiTreeChangeListener? = null

    private var myMtlFile: MtlFile? = null

    private val myMaterialComboBoxModel = MaterialComboBoxModel(emptyList())
    private val myMaterialPropertiesTableModel = MaterialPropertiesTableModel()

    val material: MtlMaterialElement? get() = myMaterialComboBoxModel.selectedItem as? MtlMaterialElement

    init {
        myMaterialComboBoxModel.addListDataListener(
            object : ListDataListener {

                override fun intervalAdded(e: ListDataEvent?) = Unit

                override fun intervalRemoved(e: ListDataEvent?) = Unit

                override fun contentsChanged(e: ListDataEvent?) {
                    myMaterialPropertiesTableModel.updateMaterial(material)
                }
            }
        )

        val selectMaterialPanel = panel {
            row(WavefrontObjBundle.message("editor.fileTypes.mtl.material.material")) {
                comboBox(model = myMaterialComboBoxModel, renderer = MaterialListCellRenderer())
            }
        }
        add(selectMaterialPanel, BorderLayout.BEFORE_FIRST_LINE)

        val splitter = EditorSplitter(vertical = true)
        splitter.splitterProportionKey = "${javaClass.simpleName}.Proportion"
        splitter.firstComponent = JPanel(BorderLayout()).apply {
            background = Color.BLACK
            add (JBLabel("TODO: Preview Placeholder"))
        }
        splitter.secondComponent = JBScrollPane(
            object : JBTable(myMaterialPropertiesTableModel) {
                override fun getCellRenderer(row: Int, column: Int): TableCellRenderer =
                    MaterialPropertiesTableCellRenderer()
            }
        )

        add(splitter, BorderLayout.CENTER)
    }

    private fun updateMtlFile(mtlFile: MtlFile?) {
        runReadAction {
            myMtlFile = mtlFile
            myMaterialComboBoxModel.updateMaterials(mtlFile?.materials.orEmpty())
        }
    }

    fun initialize() {
        if (project.isInitialized && isInitialized.compareAndSet(false, true)) {
            try {
                initializeMtlFile()
            } catch (expected: Throwable) {
                // TODO: Handle errors
            }
        }
    }

    private fun initializeMtlFile() {
        val psiManager = PsiManager.getInstance(project)
        val mtlFile = requireNotNull(psiManager.findFile(file) as? MtlFile)

        psiTreeChangeListener?.let { listener -> psiManager.removePsiTreeChangeListener(listener) }
        psiTreeChangeListener = MyPsiTreeChangeListener(mtlFile)
        psiTreeChangeListener?.let { listener -> psiManager.addPsiTreeChangeListener(listener, this) }

        updateMtlFile(mtlFile)
    }

    override fun dispose() {
        // TODO: Dispose preview
    }

    private inner class MyPsiTreeChangeListener(private val file: MtlFile) : PsiTreeChangeAdapter() {

        val referencedFiles: List<PsiFile>
            get() = file.materials.flatMap { material -> material.texturePsiFiles }

        private fun onPsiTreeChangeEvent(event: PsiTreeChangeEvent) {
            if (event.file == file || event.file in referencedFiles) {
                updateMtlFile(file)
            }
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            onPsiTreeChangeEvent(event)
        }
    }
}
