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

import com.intellij.openapi.application.invokeLater
import javax.swing.tree.DefaultMutableTreeNode
import javax.swing.tree.DefaultTreeModel

/**
 * Provides a tree model containing error log.
 */
class ErrorLogTreeModel : DefaultTreeModel(DefaultMutableTreeNode()), ErrorLog {

    private val rootNode: DefaultMutableTreeNode
        get() = root as DefaultMutableTreeNode

    private val entries: Sequence<Any>
        get() = rootNode.children().asSequence()
            .mapNotNull { node -> (node as? DefaultMutableTreeNode)?.userObject }

    override val hasErrors: Boolean
        get() = errorsCount > 0

    override val errorsCount: Int
        get() = rootNode.childCount

    override fun addError(entry: ErrorLog.Entry) {
        if (entry.headline in entries) return

        val entryNode = DefaultMutableTreeNode(entry.headline)
        val stackTraceNode = DefaultMutableTreeNode(entry.stackTrace, false)

        rootNode.add(entryNode)
        entryNode.add(stackTraceNode)

        reload()
    }

    override fun reload() {
        try {
            invokeLater {
                synchronized(root) {
                    reload(root)
                }
            }
        } catch (ignored: RuntimeException) {
        }
    }

    override fun clearErrors() {
        rootNode.removeAllChildren()
        reload()
    }
}
