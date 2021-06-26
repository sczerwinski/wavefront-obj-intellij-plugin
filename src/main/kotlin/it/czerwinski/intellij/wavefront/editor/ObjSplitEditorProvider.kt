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

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorPolicy
import com.intellij.openapi.fileEditor.impl.text.PsiAwareTextEditorProvider
import com.intellij.openapi.project.DumbAware
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.common.editor.SplitEditorProvider

/**
 * Wavefront OBJ split editor provider.
 */
class ObjSplitEditorProvider :
    SplitEditorProvider(
        textEditorProvider = PsiAwareTextEditorProvider(),
        previewEditorProvider = ObjPreviewFileEditorProvider()
    ),
    DumbAware {

    override fun createAsyncEditorBuilder(project: Project, file: VirtualFile): Builder = Builder(project, file)

    override fun getPolicy(): FileEditorPolicy = FileEditorPolicy.HIDE_DEFAULT_EDITOR

    override fun getEditorTypeId(): String = "wavefront-obj-split-editor"

    /**
     * Concrete builder of Wavefront OBJ split editor.
     */
    class Builder(project: Project, file: VirtualFile) : SplitEditorProvider.Builder(project, file) {

        override fun build(): FileEditor =
            ObjSplitEditor(
                textEditor = buildTextEditor(),
                previewEditor = buildPreviewEditor() as ObjPreviewFileEditor
            )
    }
}
