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

package it.czerwinski.intellij.common.editor

import com.intellij.openapi.fileEditor.FileEditor
import com.intellij.openapi.fileEditor.FileEditorLocation
import com.intellij.openapi.util.UserDataHolderBase
import java.beans.PropertyChangeListener

/**
 * A file editor intended as a preview.
 *
 * This type of editor will not provide any means of editing the file.
 */
abstract class PreviewEditor : UserDataHolderBase(), FileEditor {

    final override fun isModified(): Boolean = false

    final override fun isValid(): Boolean = true

    final override fun addPropertyChangeListener(listener: PropertyChangeListener) = Unit

    final override fun removePropertyChangeListener(listener: PropertyChangeListener) = Unit

    final override fun getCurrentLocation(): FileEditorLocation? = null
}
