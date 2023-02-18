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

import com.intellij.ui.MultilineTreeCellRenderer
import com.intellij.ui.treeStructure.Tree
import it.czerwinski.intellij.wavefront.icons.Icons
import javax.swing.JTree
import javax.swing.tree.DefaultMutableTreeNode

/**
 * Displays error log in the form of a tree.
 */
class ErrorLogTree(treeModel: ErrorLogTreeModel) : Tree(treeModel) {

    init {
        isRootVisible = false
        setRowHeight(-1)
        setCellRenderer(TreeCellRenderer())
    }

    private class TreeCellRenderer : MultilineTreeCellRenderer() {

        override fun initComponent(
            tree: JTree?,
            value: Any?,
            selected: Boolean,
            expanded: Boolean,
            leaf: Boolean,
            row: Int,
            hasFocus: Boolean
        ) {
            val nodeText = (value as? DefaultMutableTreeNode)?.userObject?.toString()
            val lines = nodeText?.lines()?.filter { it.isNotBlank() }.orEmpty()
            if (lines.size > 1) {
                setText(lines.toTypedArray(), "")
            } else {
                setText(emptyArray(), nodeText.orEmpty())
            }
            icon = if (leaf) null else Icons.General.Error
        }
    }
}
