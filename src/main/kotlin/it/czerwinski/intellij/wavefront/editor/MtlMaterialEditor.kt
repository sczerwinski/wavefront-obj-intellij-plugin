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

import com.intellij.openapi.fileEditor.FileEditorState
import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.StartupManager
import com.intellij.openapi.util.Disposer
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.common.editor.PreviewEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import javax.swing.JComponent

class MtlMaterialEditor(
    private val project: Project,
    virtualFile: VirtualFile
) : PreviewEditor() {

    private val myComponent: MtlMaterialComponent = MtlMaterialComponent(project, virtualFile, this)

    val material: MtlMaterialElement? get() = myComponent.material

    fun initPreview() {
        val startupManager = StartupManager.getInstance(project)
        if (!startupManager.postStartupActivityPassed()) {
            startupManager.registerPostStartupActivity {
                myComponent.initialize()
            }
        } else {
            myComponent.initialize()
        }
    }

    override fun getComponent(): JComponent = myComponent

    override fun getPreferredFocusedComponent(): JComponent = myComponent

    override fun getName(): String = WavefrontObjBundle.message("editor.fileTypes.mtl.material.name")

    override fun setState(state: FileEditorState) = Unit

    override fun dispose() {
        Disposer.dispose(myComponent)
    }
}
