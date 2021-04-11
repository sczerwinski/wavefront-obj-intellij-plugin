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

import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

class ErrorLogTreeModel : DefaultTreeModel(DefaultMutableTreeNode()), ErrorLog {

    private val rootNode: DefaultMutableTreeNode get() = root as DefaultMutableTreeNode

    override fun addError(entry: ErrorLog.Entry) {
        for (node in rootNode.children()) {
            if ((node as? DefaultMutableTreeNode)?.userObject == entry.headline) return
        }
        rootNode.add(
            DefaultMutableTreeNode(entry.headline).apply {
                add(DefaultMutableTreeNode(entry.stackTrace, false))
            }
        )
        reload()
    }

    override fun clearErrors() {
        rootNode.removeAllChildren()
        reload()
    }
}
