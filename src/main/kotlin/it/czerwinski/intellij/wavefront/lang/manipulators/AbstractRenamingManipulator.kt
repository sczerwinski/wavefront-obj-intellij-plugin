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

package it.czerwinski.intellij.wavefront.lang.manipulators

import com.intellij.lang.ASTNode
import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.PsiElement

abstract class AbstractRenamingManipulator<T : PsiElement> : AbstractElementManipulator<T>() {

    final override fun handleContentChange(element: T, range: TextRange, newContent: String?): T? {
        val nameNode = extractNameNode(element)
        if (nameNode != null) {
            val nameRange = TextRange(nameNode.startOffsetInParent, nameNode.startOffsetInParent + nameNode.textLength)
            if (range in nameRange) {
                val newName: String = nameNode.text.substring(0, range.startOffset - nameNode.startOffsetInParent) +
                        newContent +
                        nameNode.text.substring(
                            range.startOffset - nameNode.startOffsetInParent + range.length,
                            nameNode.textLength
                        )

                val newElement = createRenamedElement(element, newName)
                val newNameNode = extractNameNode(newElement)

                if (newNameNode != null) {
                    element.node.replaceChild(nameNode, newNameNode)

                    return element
                }
            }
        }
        return null
    }

    protected abstract fun extractNameNode(element: T): ASTNode?

    protected abstract fun createRenamedElement(element: T, newName: String): T
}
