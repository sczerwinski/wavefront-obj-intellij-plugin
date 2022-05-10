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

import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.TextEditor
import it.czerwinski.intellij.common.editor.SplitEditor
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.settings.WavefrontObjSettingsState

/**
 * Split editor for Wavefront OBJ files.
 *
 * The preview editor is a 3D preview of the OBJ file.
 */
class ObjSplitEditor(
    textEditor: TextEditor,
    previewEditor: ObjPreviewEditor
) : SplitEditor<ObjPreviewEditor>(textEditor, previewEditor) {

    private val settingsChangedListener: WavefrontObjSettingsState.SettingsChangedListener =
        object : WavefrontObjSettingsState.SettingsChangedListener {

            override fun beforeSettingsChanged(newSettings: WavefrontObjSettingsState?) {
                val oldDefaultLayout = WavefrontObjSettingsState.getInstance()?.defaultEditorLayout ?: Layout.DEFAULT
                val oldVerticalSplit = WavefrontObjSettingsState.getInstance()?.isVerticalSplit
                    ?: WavefrontObjSettingsState.DEFAULT_VERTICAL_SPLIT

                if (layout === oldDefaultLayout) {
                    layout = newSettings?.defaultEditorLayout ?: Layout.DEFAULT
                }

                if (mySplitVertically == oldVerticalSplit) {
                    mySplitVertically = newSettings?.isVerticalSplit ?: WavefrontObjSettingsState.DEFAULT_VERTICAL_SPLIT
                }
            }
        }

    init {
        initializeEditorsVisibilityListener(previewEditor)
        initializeFromSettings()
        observeSettingsChanges()
    }

    private fun initializeEditorsVisibilityListener(previewEditor: ObjPreviewEditor) {
        myComponent.addEditorsVisibilityListener { _, isShowingPreviewEditor ->
            if (isShowingPreviewEditor) {
                previewEditor.initPreview()
            }
        }
    }

    private fun initializeFromSettings() {
        val settings = WavefrontObjSettingsState.getInstance()

        layout = settings?.defaultEditorLayout ?: Layout.DEFAULT
        mySplitVertically = settings?.isVerticalSplit ?: WavefrontObjSettingsState.DEFAULT_VERTICAL_SPLIT
    }

    private fun observeSettingsChanges() {
        ApplicationManager.getApplication().messageBus
            .connect(this)
            .subscribe(WavefrontObjSettingsState.SettingsChangedListener.TOPIC, settingsChangedListener)
    }

    override fun getName(): String = WavefrontObjBundle.message("editor.fileTypes.obj.split.name")
}
