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

package it.czerwinski.intellij.wavefront.editor.model

import com.intellij.lang.ASTNode
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.MtlFloatValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlValueModifierOption
import java.awt.Color
import org.jetbrains.annotations.Nls

sealed class MaterialProperty<E : PsiElement, T> {

    @get:Nls abstract val label: String
    @get:Nls(capitalization = Nls.Capitalization.Title) abstract val actionName: String
    abstract val propertyKeyword: String
    abstract val elementGetter: (MtlMaterialElement) -> E?

    abstract fun getValue(material: MtlMaterialElement?): T?

    abstract fun setValue(material: MtlMaterialElement?, value: Any?)

    protected fun setValueWriteCommandAction(material: MtlMaterialElement?, value: T?) {
        if (material != null && getValue(material) != value) {
            WriteCommandAction.writeCommandAction(material.project, material.containingFile)
                .withUndoConfirmationPolicy(UndoConfirmationPolicy.DO_NOT_REQUEST_CONFIRMATION)
                .withName(actionName)
                .run<Throwable> { applyValue(material, value) }
        }
    }

    private fun applyValue(material: MtlMaterialElement, value: T?) {
        val element = elementGetter(material)
        if (element != null) {
            if (value == null) {
                removeElement(material, element)
            } else {
                updateElement(material, element, value)
            }
        } else if (value != null) {
            addElement(material, value)
        }
    }

    private fun removeElement(material: MtlMaterialElement, element: E) {
        val prevNode = element.prevSibling.node
        material.node.removeChild(element.node)
        material.node.removeChild(prevNode)
    }

    protected abstract fun updateElement(material: MtlMaterialElement, element: E, value: T)

    protected abstract fun addElement(material: MtlMaterialElement, value: T)

    data class MaterialColor(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlColorElement?
    ) : MaterialProperty<MtlColorElement, Color>() {

        override fun getValue(material: MtlMaterialElement?): Color? =
            material?.let(elementGetter)?.color

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, value as? Color)
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlColorElement, value: Color) {
            val newColorNode = MtlElementFactory.createColorElement(material.project, value).node
            newColorNode.replaceChild(newColorNode.firstChildNode, element.node.firstChildNode)
            element.node.replaceAllChildrenToChildrenOf(newColorNode)
        }

        override fun addElement(material: MtlMaterialElement, value: Color) {
            val newColorNode = MtlElementFactory.createColorElement(material.project, propertyKeyword, value).node
            material.node.addChild(MtlElementFactory.createCRLF(material.project).node)
            material.node.addChild(newColorNode)
        }
    }

    data class MaterialIlluminationValue(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlIlluminationValueElement?
    ) : MaterialProperty<MtlIlluminationValueElement, MtlIlluminationValueElement.Illumination>() {

        override fun getValue(material: MtlMaterialElement?): MtlIlluminationValueElement.Illumination? =
            material?.let(elementGetter)?.value

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, value as? MtlIlluminationValueElement.Illumination)
        }

        override fun updateElement(
            material: MtlMaterialElement,
            element: MtlIlluminationValueElement,
            value: MtlIlluminationValueElement.Illumination
        ) {
            val newIlluminationNode = MtlElementFactory.createIlluminationValueElement(material.project, value).node
            newIlluminationNode.replaceChild(newIlluminationNode.firstChildNode, element.node.firstChildNode)
            element.node.replaceAllChildrenToChildrenOf(newIlluminationNode)
        }

        override fun addElement(material: MtlMaterialElement, value: MtlIlluminationValueElement.Illumination) {
            val newIlluminationNode = MtlElementFactory.createIlluminationValueElement(material.project, value).node
            material.node.addChild(MtlElementFactory.createCRLF(material.project).node)
            material.node.addChild(newIlluminationNode)
        }
    }

    data class MaterialFloatValue(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlFloatValueElement?
    ) : MaterialProperty<MtlFloatValueElement, Float>() {

        override fun getValue(material: MtlMaterialElement?): Float? =
            material?.let(elementGetter)?.value

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, (value as? String)?.toFloatOrNull())
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlFloatValueElement, value: Float) {
            val newFloatValueNode = MtlElementFactory.createFloatValueElement(material.project, value).node
            newFloatValueNode.replaceChild(newFloatValueNode.firstChildNode, element.node.firstChildNode)
            element.node.replaceAllChildrenToChildrenOf(newFloatValueNode)
        }

        override fun addElement(material: MtlMaterialElement, value: Float) {
            val newFloatValueNode =
                MtlElementFactory.createFloatValueElement(material.project, propertyKeyword, value).node
            material.node.addChild(MtlElementFactory.createCRLF(material.project).node)
            material.node.addChild(newFloatValueNode)
        }
    }

    data class MaterialTexture(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlTextureElement?
    ) : MaterialProperty<MtlTextureElement, String>() {

        override fun getValue(material: MtlMaterialElement?): String? =
            material?.let(elementGetter)?.textureFilename

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, (value as? String)?.takeUnless { it.isBlank() })
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlTextureElement, value: String) {
            val newTextureNode = MtlElementFactory.createTextureElement(material.project, value).node
            element.node.replaceChild(element.node.lastChildNode, newTextureNode.lastChildNode)
        }

        override fun addElement(material: MtlMaterialElement, value: String) {
            val newTextureNode = MtlElementFactory.createTextureElement(material.project, propertyKeyword, value).node
            material.node.addChild(MtlElementFactory.createCRLF(material.project).node)
            material.node.addChild(newTextureNode)
        }
    }

    data class MaterialTextureValueModifier(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        val parentElementGetter: (MtlMaterialElement) -> MtlTextureElement?,
        override val elementGetter: (MtlMaterialElement) -> MtlValueModifierOption?,
        val valueIndex: Int
    ) : MaterialProperty<MtlValueModifierOption, Float>() {

        override fun getValue(material: MtlMaterialElement?): Float? =
            material?.let(elementGetter)?.values?.get(valueIndex)

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, (value as? String)?.toFloatOrNull())
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlValueModifierOption, value: Float) {
            val newValueModifierNode = createNewValueModifierNode(material, value, element)
            element.node.replaceAllChildrenToChildrenOf(newValueModifierNode)
        }

        private fun createNewValueModifierNode(
            material: MtlMaterialElement,
            value: Float,
            element: MtlValueModifierOption?
        ): ASTNode = when (valueIndex) {
            VALUE_INDEX_BASE -> MtlElementFactory.createValueModifierOption(
                material.project,
                base = value,
                gain = element?.gain ?: 1f
            )
            VALUE_INDEX_GAIN -> MtlElementFactory.createValueModifierOption(
                material.project,
                base = element?.base ?: 0f,
                gain = value
            )
            else -> error("Invalid value index: $valueIndex")
        }.node

        override fun addElement(material: MtlMaterialElement, value: Float) {
            val textureElement = parentElementGetter(material)
            val textureNode = textureElement?.node
            if (textureNode != null) {
                val newValueModifierNode = createNewValueModifierNode(material, value, element = null)
                textureNode.addChild(newValueModifierNode, textureNode.lastChildNode)
            }
        }

        companion object {
            const val VALUE_INDEX_BASE = 0
            const val VALUE_INDEX_GAIN = 1
        }
    }
}
