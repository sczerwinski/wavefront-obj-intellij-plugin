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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.TextEditor
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState

/**
 * Split editor for Wavefront OBJ files.
 *
 * The preview editor is a 3D preview of the OBJ file.
 */
class ObjSplitEditor(
    textEditor: TextEditor,
    previewEditor: ObjPreviewFileEditor
) : SplitEditor<ObjPreviewFileEditor>(textEditor, previewEditor),
    WavefrontObjSettingsState.SettingsChangedListener {

    init {
        settingsChanged(WavefrontObjSettingsState.getInstance())

        ApplicationManager.getApplication().messageBus
            .connect(this)
            .subscribe(WavefrontObjSettingsState.SettingsChangedListener.TOPIC, this)
    }

    override fun settingsChanged(settings: WavefrontObjSettingsState?) {
        triggerLayoutChange(newLayout = settings?.defaultEditorLayout ?: Layout.DEFAULT)
        triggerSplitterOrientationChange(newSplitterOrientation = settings?.isVerticalSplit ?: false)
        previewEditor.triggerSettingsChange(
            settings = settings?.objPreviewFileEditorSettings ?: ObjPreviewFileEditorSettingsState.DEFAULT
        )
    }

    override fun onEditorsVisibilityChanged() {
        if (layout.isShowingPreviewEditor) {
            previewEditor.initPreview()
        }
    }

    override fun getName(): String = WavefrontObjBundle.message("editor.fileTypes.obj.split.name")
}
