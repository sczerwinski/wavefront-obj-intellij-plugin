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

package it.czerwinski.intellij.common.ui

/**
 * A component containing error log.
 *
 * Make a [PreviewEditor][it.czerwinski.intellij.common.editor.PreviewEditor]
 * implement this interface if it contains an error log.
 */
interface ErrorLogContainer {

    /**
     * Number of errors in this log.
     */
    val errorsCount: Int

    /**
     * Sets desired error log [visibility][visible].
     *
     * If there are no errors in the error log, it will always be hidden.
     *
     * When new errors are added to the error log, it will always become shown.
     * New call to [setErrorLogVisibility] must be made in order to hide it.
     */
    fun setErrorLogVisibility(visible: Boolean)
}
