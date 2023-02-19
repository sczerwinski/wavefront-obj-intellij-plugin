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

package it.czerwinski.intellij.wavefront.tools.model

import com.intellij.lang.ASTNode
import com.intellij.openapi.command.UndoConfirmationPolicy
import com.intellij.openapi.command.WriteCommandAction
import com.intellij.psi.PsiElement
import com.intellij.refactoring.RefactoringFactory
import com.intellij.ui.components.JBLabel
import com.intellij.util.ui.UIUtil
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlElementFactory
import it.czerwinski.intellij.wavefront.lang.psi.MtlFloatValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialIdentifierElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionType
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionTypeOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarChannel
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarChannelOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlValueModifierOption
import java.awt.Color
import java.awt.Font
import org.jetbrains.annotations.Nls

sealed class MaterialProperty<E : PsiElement, T> {

    @get:Nls abstract val label: String
    @get:Nls(capitalization = Nls.Capitalization.Title) abstract val actionName: String
    abstract val propertyKeyword: String
    abstract val elementGetter: (MtlMaterialElement) -> E?
    abstract val shadingMethods: Set<ShadingMethod>

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

    open fun applyStyle(component: JBLabel) = Unit

    data class MaterialName(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlMaterialIdentifierElement?
    ) : MaterialProperty<MtlMaterialIdentifierElement, String>() {

        override val shadingMethods: Set<ShadingMethod> = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)

        override fun getValue(material: MtlMaterialElement?): String? =
            material?.let(elementGetter)?.name

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            material?.let(elementGetter)?.let { element ->
                RefactoringFactory.getInstance(element.project)
                    ?.createRename(element, value as? String)
                    ?.run()
            }
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlMaterialIdentifierElement, value: String) {
            throw UnsupportedOperationException("Cannot update material name element")
        }

        override fun addElement(material: MtlMaterialElement, value: String) {
            throw UnsupportedOperationException("Cannot add material name element")
        }

        override fun applyStyle(component: JBLabel) {
            component.font = component.font.deriveFont(Font.BOLD)
        }
    }

    data class MaterialColor(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        override val elementGetter: (MtlMaterialElement) -> MtlColorElement?,
        override val shadingMethods: Set<ShadingMethod> = emptySet()
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

        override val shadingMethods: Set<ShadingMethod> = emptySet()

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
        override val elementGetter: (MtlMaterialElement) -> MtlFloatValueElement?,
        override val shadingMethods: Set<ShadingMethod> = emptySet()
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
        override val elementGetter: (MtlMaterialElement) -> MtlTextureElement?,
        override val shadingMethods: Set<ShadingMethod> = emptySet()
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
        override val shadingMethods: Set<ShadingMethod> = emptySet(),
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

        override fun applyStyle(component: JBLabel) {
            component.fontColor = UIUtil.FontColor.BRIGHTER
        }

        companion object {
            const val VALUE_INDEX_BASE = 0
            const val VALUE_INDEX_GAIN = 1
        }
    }

    data class MaterialTextureScalarChannel(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        val parentElementGetter: (MtlMaterialElement) -> MtlScalarTextureElement?,
        override val elementGetter: (MtlMaterialElement) -> MtlScalarChannelOption?,
        override val shadingMethods: Set<ShadingMethod> = emptySet()
    ) : MaterialProperty<MtlScalarChannelOption, MtlScalarChannel>() {

        override fun getValue(material: MtlMaterialElement?): MtlScalarChannel? =
            MtlScalarChannel.fromValue(material?.let(elementGetter)?.value)

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, value as? MtlScalarChannel)
        }

        override fun updateElement(
            material: MtlMaterialElement,
            element: MtlScalarChannelOption,
            value: MtlScalarChannel
        ) {
            val newScalarChannelOption = MtlElementFactory.createScalarChannelOption(material.project, value)
            element.node.replaceAllChildrenToChildrenOf(newScalarChannelOption.node)
        }

        override fun addElement(material: MtlMaterialElement, value: MtlScalarChannel) {
            val textureElement = parentElementGetter(material)
            val textureNode = textureElement?.node
            if (textureNode != null) {
                val newScalarChannelOption = MtlElementFactory.createScalarChannelOption(material.project, value)
                textureNode.addChild(newScalarChannelOption.node, textureNode.lastChildNode)
            }
        }

        override fun applyStyle(component: JBLabel) {
            component.fontColor = UIUtil.FontColor.BRIGHTER
        }
    }

    data class MaterialReflectionTexture(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String
    ) : MaterialProperty<MtlTextureElement, String>() {

        override val propertyKeyword: String = "refl"

        override val elementGetter: (MtlMaterialElement) -> MtlTextureElement? =
            MtlMaterialElement::reflectionMapElement

        override val shadingMethods: Set<ShadingMethod> = setOf(ShadingMethod.PBR)

        override fun getValue(material: MtlMaterialElement?): String? =
            material?.let(elementGetter)?.textureFilename

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, (value as? String)?.takeUnless { it.isBlank() })
        }

        override fun updateElement(material: MtlMaterialElement, element: MtlTextureElement, value: String) {
            val newTextureNode = MtlElementFactory.createReflectionTextureElement(
                project = material.project,
                filename = value
            ).node
            element.node.replaceChild(element.node.lastChildNode, newTextureNode.lastChildNode)
        }

        override fun addElement(material: MtlMaterialElement, value: String) {
            val newTextureNode = MtlElementFactory.createReflectionTextureElement(
                project = material.project,
                filename = value
            ).node
            material.node.addChild(MtlElementFactory.createCRLF(material.project).node)
            material.node.addChild(newTextureNode)
        }
    }

    data class MaterialReflectionTextureType(
        @Nls override val label: String,
        @Nls(capitalization = Nls.Capitalization.Title) override val actionName: String,
        override val propertyKeyword: String,
        val parentElementGetter: (MtlMaterialElement) -> MtlReflectionTextureElement?,
        override val elementGetter: (MtlMaterialElement) -> MtlReflectionTypeOption?
    ) : MaterialProperty<MtlReflectionTypeOption, MtlReflectionType>() {

        override val shadingMethods: Set<ShadingMethod> = setOf(ShadingMethod.PBR)

        override fun getValue(material: MtlMaterialElement?): MtlReflectionType? =
            MtlReflectionType.fromValue(material?.let(elementGetter)?.value)

        override fun setValue(material: MtlMaterialElement?, value: Any?) {
            setValueWriteCommandAction(material, value as? MtlReflectionType)
        }

        override fun updateElement(
            material: MtlMaterialElement,
            element: MtlReflectionTypeOption,
            value: MtlReflectionType
        ) {
            val newReflectionTypeOption = MtlElementFactory.createReflectionTypeOption(material.project, value)
            element.node.replaceAllChildrenToChildrenOf(newReflectionTypeOption.node)
        }

        override fun addElement(material: MtlMaterialElement, value: MtlReflectionType) {
            val textureElement = parentElementGetter(material)
            val textureNode = textureElement?.node
            if (textureNode != null) {
                val newReflectionTypeOption = MtlElementFactory.createReflectionTypeOption(material.project, value)
                textureNode.addChild(newReflectionTypeOption.node, textureNode.lastChildNode)
            }
        }

        override fun applyStyle(component: JBLabel) {
            component.fontColor = UIUtil.FontColor.BRIGHTER
        }
    }
}
