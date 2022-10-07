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
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.Project
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFile
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.dsl.builder.panel
import it.czerwinski.intellij.common.ui.EditorSplitter
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.ui.MaterialComboBoxModel
import it.czerwinski.intellij.wavefront.editor.ui.MaterialListCellRenderer
import it.czerwinski.intellij.wavefront.editor.ui.MaterialPropertiesTable
import it.czerwinski.intellij.wavefront.editor.ui.MaterialPropertiesTableModel
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.util.isTextureFile
import java.awt.BorderLayout
import javax.swing.JPanel
import javax.swing.event.ListDataEvent
import javax.swing.event.ListDataListener

class MtlMaterialComponent(
    private val project: Project,
    private val file: VirtualFile,
    private val editor: FileEditor
) : JPanel(BorderLayout()), Zoomable, Refreshable, Disposable {

    private var psiTreeChangeListener: MyPsiTreeChangeListener? = null

    private var myMtlFile: MtlFile? = null

    private var myMaterialPreviewComponent = MtlMaterialPreviewComponent(project, editor)

    private val myMaterialComboBoxModel = MaterialComboBoxModel(emptyList())
    private val myMaterialPropertiesTableModel = MaterialPropertiesTableModel()

    private val mySplitter: EditorSplitter

    val material: MtlMaterialElement? get() = myMaterialComboBoxModel.selectedItem as? MtlMaterialElement

    var previewMesh: MaterialPreviewMesh
        get() = myMaterialPreviewComponent.previewMesh
        set(value) {
            myMaterialPreviewComponent.previewMesh = value
        }

    var shadingMethod: ShadingMethod
        get() = myMaterialPreviewComponent.shadingMethod
        set(value) {
            myMaterialPreviewComponent.shadingMethod = value
        }

    var environment: PBREnvironment
        get() = myMaterialPreviewComponent.environment
        set(value) {
            myMaterialPreviewComponent.environment = value
        }

    var isCroppingTextures: Boolean
        get() = myMaterialPreviewComponent.isCroppingTextures
        set(value) {
            myMaterialPreviewComponent.isCroppingTextures = value
        }

    var previewSceneConfig: PreviewSceneConfig
        get() = myMaterialPreviewComponent.previewSceneConfig
        set(value) {
            myMaterialPreviewComponent.previewSceneConfig = value
        }

    init {
        myMaterialComboBoxModel.addListDataListener(
            object : ListDataListener {

                override fun intervalAdded(e: ListDataEvent?) = Unit

                override fun intervalRemoved(e: ListDataEvent?) = Unit

                override fun contentsChanged(e: ListDataEvent?) {
                    myMaterialPropertiesTableModel.updateMaterial(material)
                    myMaterialPreviewComponent.updateMaterial(material)
                }
            }
        )

        val selectMaterialPanel = panel {
            row(WavefrontObjBundle.message("editor.fileTypes.mtl.material.material")) {
                comboBox(model = myMaterialComboBoxModel, renderer = MaterialListCellRenderer())
            }
        }
        add(selectMaterialPanel, BorderLayout.BEFORE_FIRST_LINE)

        mySplitter = EditorSplitter(vertical = true)
        mySplitter.splitterProportionKey = "${javaClass.simpleName}.Proportion"
        mySplitter.firstComponent = myMaterialPreviewComponent

        add(mySplitter, BorderLayout.CENTER)
    }

    fun initialize() {
        if (project.isInitialized) {
            initializeMtlFile()
            mySplitter.secondComponent = JBScrollPane(
                MaterialPropertiesTable(project, myMaterialPropertiesTableModel, editor = editor)
            )
            myMaterialPreviewComponent.initialize()
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

    private fun updateMtlFile(mtlFile: MtlFile?) {
        runReadAction {
            myMtlFile = mtlFile
            myMaterialComboBoxModel.updateMaterials(mtlFile?.materials.orEmpty())
        }
    }

    fun toggleCropTextures() {
        isCroppingTextures = !isCroppingTextures
    }

    override fun zoomIn() {
        myMaterialPreviewComponent.zoomIn()
    }

    override fun zoomOut() {
        myMaterialPreviewComponent.zoomOut()
    }

    override fun zoomFit() {
        myMaterialPreviewComponent.zoomFit()
    }

    override fun refresh() {
        myMaterialPreviewComponent.refresh()
    }

    override fun dispose() {
        Disposer.dispose(myMaterialPreviewComponent)
    }

    private inner class MyPsiTreeChangeListener(private val file: MtlFile) : PsiTreeChangeAdapter() {

        val referencedFiles: List<PsiFile>
            get() = file.materials.flatMap { material -> material.texturePsiFiles }

        private fun handlePsiTreeChange(element: PsiElement?) {
            if (element == file || element in referencedFiles || element.isTextureFile()) {
                updateMtlFile(file)
            }
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            handlePsiTreeChange(event.file)
        }
    }
}
