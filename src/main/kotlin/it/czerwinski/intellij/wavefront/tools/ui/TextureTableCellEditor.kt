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

package it.czerwinski.intellij.wavefront.tools.ui

import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.openapi.Disposable
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
import com.intellij.openapi.roots.ProjectFileIndex
import com.intellij.openapi.vfs.VfsUtil
import com.intellij.psi.PsiManager
import com.intellij.psi.PsiTreeChangeAdapter
import com.intellij.psi.PsiTreeChangeEvent
import com.intellij.ui.TextFieldWithAutoCompletion
import com.intellij.util.ui.AbstractTableCellEditor
import it.czerwinski.intellij.wavefront.icons.Icons
import it.czerwinski.intellij.wavefront.lang.psi.util.findAllTextureFiles
import it.czerwinski.intellij.wavefront.lang.psi.util.isTextureFile
import java.awt.Component
import java.util.concurrent.atomic.AtomicBoolean
import javax.swing.JTable

class TextureTableCellEditor(
    private val project: Project,
    private val parent: Disposable?
) : AbstractTableCellEditor() {

    private val myPsiTreeChangeListener = MyPsiTreeChangeListener()

    private val myCompletionProvider = TexturesAutoCompletionProvider()

    private val myComponent = runReadAction {
        TextFieldWithAutoCompletion(project, myCompletionProvider, true, "")
    }

    private val requiresUpdate = AtomicBoolean(true)

    init {
        updateTextureFiles()
        PsiManager.getInstance(project)
            .addPsiTreeChangeListener(myPsiTreeChangeListener, parent ?: project)
    }

    private fun updateTextureFiles() {
        if (requiresUpdate.compareAndSet(true, false)) {
            BackgroundTaskUtil.executeOnPooledThread(parent ?: project) {
                val items = runReadAction {
                    project.findAllTextureFiles().map { file ->
                        val root = ProjectFileIndex.getInstance(file.project)
                            .getContentRootForFile(file.virtualFile)
                        val typeText = root?.let { VfsUtil.getRelativePath(file.virtualFile.parent, it) }
                            ?: file.containingDirectory?.name
                        LookupElementBuilder.create(file.name)
                            .withIcon(file.fileType.icon ?: Icons.Structure.Mtl.Texture)
                            .withTypeText(typeText, Icons.General.Folder, false)
                    }
                }
                invokeLater {
                    myCompletionProvider.setItems(items)
                }
            }
        }
    }

    override fun getCellEditorValue(): Any = myComponent.text

    override fun getTableCellEditorComponent(
        table: JTable?,
        value: Any?,
        isSelected: Boolean,
        row: Int,
        column: Int
    ): Component {
        updateTextureFiles()
        myComponent.text = (value as? String).orEmpty()
        return myComponent
    }

    private inner class MyPsiTreeChangeListener : PsiTreeChangeAdapter() {

        override fun childAdded(event: PsiTreeChangeEvent) {
            if (event.child.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }

        override fun childRemoved(event: PsiTreeChangeEvent) {
            if (event.child.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }

        override fun childReplaced(event: PsiTreeChangeEvent) {
            if (event.oldChild.isTextureFile() || event.newChild.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }

        override fun childrenChanged(event: PsiTreeChangeEvent) {
            if (event.file.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }

        override fun childMoved(event: PsiTreeChangeEvent) {
            if (event.child.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }

        override fun propertyChanged(event: PsiTreeChangeEvent) {
            if (event.element.isTextureFile()) {
                requiresUpdate.set(true)
            }
        }
    }
}
