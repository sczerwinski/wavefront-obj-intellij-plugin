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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.tree.TokenSet
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.ObjVectorElement

abstract class ObjVectorElementImpl(
    node: ASTNode
) : ASTWrapperPsiElement(node), ObjVectorElement {

    private val coordinatesNodes get() = node.getChildren(TokenSet.create(ObjTypes.FLOAT))

    override val coordinates: List<Float?>
        get() = coordinatesNodes.map { it.text.toFloatOrNull() }

    override val asVec2: Vec2<Float>
        get() = asVec4.toVec2()

    override val asVec3: Vec3<Float>
        get() = asVec4.toNonRationalForm()

    override val asVec4: Vec4<Float>
        get() = Vec4(
            x = coordinates.getOrNull(COORDINATE_X) ?: 0f,
            y = coordinates.getOrNull(COORDINATE_Y) ?: 0f,
            z = coordinates.getOrNull(COORDINATE_Z) ?: 0f,
            w = coordinates.getOrNull(COORDINATE_W) ?: 1f
        )

    companion object {
        private const val COORDINATE_X = 0
        private const val COORDINATE_Y = 1
        private const val COORDINATE_Z = 2
        private const val COORDINATE_W = 3
    }
}
