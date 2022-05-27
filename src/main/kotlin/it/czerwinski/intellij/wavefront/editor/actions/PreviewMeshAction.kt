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

package it.czerwinski.intellij.wavefront.editor.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.PlatformDataKeys
import com.intellij.openapi.actionSystem.Toggleable
import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.project.DumbAware
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.editor.MtlMaterialEditor
import it.czerwinski.intellij.wavefront.editor.MtlSplitEditor
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh

sealed class PreviewMeshAction(
    private val previewMesh: MaterialPreviewMesh
) : AnAction(), DumbAware, Toggleable {

    override fun update(event: AnActionEvent) {
        val editor = findMtlMaterialFileEditor(event)

        event.presentation.isEnabled = editor != null

        if (editor != null) {
            Toggleable.setSelected(event.presentation, editor.previewMesh === previewMesh)
        }
    }

    override fun actionPerformed(event: AnActionEvent) {
        val editor = findMtlMaterialFileEditor(event)

        if (editor != null) {
            editor.previewMesh = previewMesh
            Toggleable.setSelected(event.presentation, true)
        }
    }

    private fun findMtlMaterialFileEditor(event: AnActionEvent): MtlMaterialEditor? =
        findMtlMaterialFileEditor(event.getData(PlatformDataKeys.FILE_EDITOR))

    private fun findMtlMaterialFileEditor(editor: FileEditor?): MtlMaterialEditor? =
        editor as? MtlMaterialEditor ?: findMtlSplitEditor(editor)?.previewEditor

    private fun findMtlSplitEditor(editor: FileEditor?): MtlSplitEditor? =
        editor as? MtlSplitEditor ?: SplitEditor.KEY_PARENT_SPLIT_EDITOR[editor] as? MtlSplitEditor

    class Cube : PreviewMeshAction(MaterialPreviewMesh.CUBE)

    class Cylinder : PreviewMeshAction(MaterialPreviewMesh.CYLINDER)

    class Sphere : PreviewMeshAction(MaterialPreviewMesh.SPHERE)
}
