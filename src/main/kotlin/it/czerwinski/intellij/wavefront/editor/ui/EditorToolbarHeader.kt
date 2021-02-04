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

package it.czerwinski.intellij.wavefront.editor.ui

import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.openapi.editor.impl.EditorHeaderComponent
import com.intellij.util.ui.JBUI
import java.awt.GridBagConstraints
import java.awt.GridBagLayout

class EditorToolbarHeader(
    leftActionToolbar: ActionToolbar? = null,
    rightActionToolbar: ActionToolbar? = null
) : EditorHeaderComponent() {

    init {
        require(leftActionToolbar != null || rightActionToolbar != null) {
            "No toolbar set for EditorToolbarHeader"
        }

        layout = GridBagLayout()

        leftActionToolbar?.component?.let {
            add(it, constraints(index = 0, anchor = GridBagConstraints.WEST, fill = GridBagConstraints.HORIZONTAL))
        }
        rightActionToolbar?.component?.let {
            add(it, constraints(index = 1, anchor = GridBagConstraints.EAST, fill = GridBagConstraints.NONE))
        }

        leftActionToolbar?.updateActionsImmediately()
        rightActionToolbar?.updateActionsImmediately()
    }

    private fun constraints(index: Int, anchor: Int, fill: Int): GridBagConstraints =
        GridBagConstraints(index, 0, 1, 1, 1.0, 0.0, anchor, fill, JBUI.emptyInsets(), 0, 0)
}
