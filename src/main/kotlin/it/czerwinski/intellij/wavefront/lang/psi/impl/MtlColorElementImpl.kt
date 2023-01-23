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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.google.common.primitives.Floats.max
import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.TokenSet
import graphics.glimpse.types.Vec3
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTypes
import java.awt.Color

@Suppress("UseJBColor")
abstract class MtlColorElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), MtlColorElement {

    private val channelNodes get() = node.getChildren(TokenSet.create(MtlTypes.FLOAT))

    private val channels get() = channelNodes.map { it.text.toFloatOrNull() ?: 0f }

    override val color: Color?
        get() = channels
            .takeIf { it.size == COLOR_CHANNELS_COUNT }
            ?.let { (r, g, b) ->
                try {
                    val value = max(r, g, b)
                    if (value > 1f) Color(r / value, g / value, b / value)
                    else Color(r, g, b)
                } catch (ignored: IllegalArgumentException) {
                    null
                }
            }

    override val colorVector: Vec3?
        get() = channels
            .takeIf { it.size == COLOR_CHANNELS_COUNT }
            ?.let { (r, g, b) -> Vec3(r, g, b) }

    override val colorString: String?
        get() = channels.joinToString(prefix = "RGB(", separator = ", ", postfix = ")")

    companion object {
        private const val COLOR_CHANNELS_COUNT = 3
    }
}
