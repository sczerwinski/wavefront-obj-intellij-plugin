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

package it.czerwinski.intellij.wavefront.editor.ui

import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlFloatValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlIlluminationValueElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlValueModifierOption
import java.awt.Color
import javax.swing.table.AbstractTableModel

class MaterialPropertiesTableModel(
    material: MtlMaterialElement? = null
) : AbstractTableModel() {

    private var myMaterial: MtlMaterialElement? = material

    fun updateMaterial(material: MtlMaterialElement?) {
        myMaterial = material
        fireTableDataChanged()
    }

    override fun getRowCount(): Int = materialProperties.size

    override fun getColumnCount(): Int = COLUMNS_COUNT

    override fun getColumnName(column: Int): String? = when (column) {
        COLUMN_LABEL -> WavefrontObjBundle.message("editor.fileTypes.mtl.material.properties.column.label")
        COLUMN_VALUE -> WavefrontObjBundle.message("editor.fileTypes.mtl.material.properties.column.value")
        else -> null
    }

    override fun getValueAt(rowIndex: Int, columnIndex: Int): Any? {
        val property = materialProperties[rowIndex]
        return when (columnIndex) {
            COLUMN_LABEL -> property.label
            COLUMN_VALUE -> property.getValue(myMaterial)
            else -> null
        }
    }

    private sealed class MaterialProperty<E, T> {

        abstract val label: String
        abstract val propertyKeyword: String
        abstract val elementGetter: (MtlMaterialElement) -> E?
        abstract fun getValue(material: MtlMaterialElement?): T?

        data class MaterialColor(
            override val label: String,
            override val propertyKeyword: String,
            override val elementGetter: (MtlMaterialElement) -> MtlColorElement?
        ) : MaterialProperty<MtlColorElement, Color>() {
            override fun getValue(material: MtlMaterialElement?): Color? =
                material?.let(elementGetter)?.color
        }

        data class MaterialIlluminationValue(
            override val label: String,
            override val propertyKeyword: String,
            override val elementGetter: (MtlMaterialElement) -> MtlIlluminationValueElement?
        ) : MaterialProperty<MtlIlluminationValueElement, MtlIlluminationValueElement.Illumination>() {
            override fun getValue(material: MtlMaterialElement?): MtlIlluminationValueElement.Illumination? =
                material?.let(elementGetter)?.value
        }

        data class MaterialFloatValue(
            override val label: String,
            override val propertyKeyword: String,
            override val elementGetter: (MtlMaterialElement) -> MtlFloatValueElement?
        ) : MaterialProperty<MtlFloatValueElement, Float>() {
            override fun getValue(material: MtlMaterialElement?): Float? =
                material?.let(elementGetter)?.value
        }

        data class MaterialTexture(
            override val label: String,
            override val propertyKeyword: String,
            override val elementGetter: (MtlMaterialElement) -> MtlTextureElement?
        ) : MaterialProperty<MtlTextureElement, String>() {
            override fun getValue(material: MtlMaterialElement?): String? =
                material?.let(elementGetter)?.textureFilename
        }

        data class MaterialTextureValueModifier(
            override val label: String,
            override val propertyKeyword: String,
            override val elementGetter: (MtlMaterialElement) -> MtlValueModifierOption?,
            val valueIndex: Int
        ) : MaterialProperty<MtlValueModifierOption, Float>() {

            override fun getValue(material: MtlMaterialElement?): Float? =
                material?.let(elementGetter)?.values?.get(valueIndex)

            companion object {
                const val VALUE_INDEX_BASE = 0
                const val VALUE_INDEX_GAIN = 1
            }
        }
    }

    companion object {
        private const val COLUMNS_COUNT = 2

        private const val COLUMN_LABEL = 0
        private const val COLUMN_VALUE = 1

        private val materialProperties = listOf(
            MaterialProperty.MaterialColor(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.ambient"),
                propertyKeyword = "Ka",
                elementGetter = { material -> material.ambientColorElement }
            ),
            MaterialProperty.MaterialColor(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.diffuse"),
                propertyKeyword = "Kd",
                elementGetter = { material -> material.diffuseColorElement }
            ),
            MaterialProperty.MaterialColor(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.specular"),
                propertyKeyword = "Ks",
                elementGetter = { material -> material.specularColorElement }
            ),
            MaterialProperty.MaterialColor(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.transmissionFilter"),
                propertyKeyword = "Tf",
                elementGetter = { material -> material.transmissionFilterElement }
            ),
            MaterialProperty.MaterialColor(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.emission"),
                propertyKeyword = "Ke",
                elementGetter = { material -> material.emissionColorElement }
            ),
            MaterialProperty.MaterialIlluminationValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.illumination"),
                propertyKeyword = "illum",
                elementGetter = { material -> material.illuminationElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.dissolve"),
                propertyKeyword = "d",
                elementGetter = { material -> material.dissolveElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.specularExponent"),
                propertyKeyword = "Ns",
                elementGetter = { material -> material.specularExponentElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.sharpness"),
                propertyKeyword = "sharpness",
                elementGetter = { material -> material.sharpnessElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.opticalDensity"),
                propertyKeyword = "Ni",
                elementGetter = { material -> material.opticalDensityElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.roughness"),
                propertyKeyword = "Pr",
                elementGetter = { material -> material.roughnessElement }
            ),
            MaterialProperty.MaterialFloatValue(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.metalness"),
                propertyKeyword = "Pm",
                elementGetter = { material -> material.metalnessElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.ambient"),
                propertyKeyword = "map_Ka",
                elementGetter = { material -> material.ambientColorMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.diffuse"),
                propertyKeyword = "map_Ka",
                elementGetter = { material -> material.diffuseColorMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specular"),
                propertyKeyword = "map_Ka",
                elementGetter = { material -> material.specularColorMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.emission"),
                propertyKeyword = "map_Ka",
                elementGetter = { material -> material.emissionColorMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent"),
                propertyKeyword = "map_Ns",
                elementGetter = { material -> material.specularExponentMapElement }
            ),
            MaterialProperty.MaterialTextureValueModifier(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent.base"),
                propertyKeyword = "map_Ns",
                elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
                valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_BASE
            ),
            MaterialProperty.MaterialTextureValueModifier(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent.gain"),
                propertyKeyword = "map_Ns",
                elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
                valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.dissolve"),
                propertyKeyword = "map_d",
                elementGetter = { material -> material.dissolveMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.displacement"),
                propertyKeyword = "disp",
                elementGetter = { material -> material.displacementMapElement }
            ),
            MaterialProperty.MaterialTextureValueModifier(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.displacement.gain"),
                propertyKeyword = "disp",
                elementGetter = { material -> material.displacementMapElement?.valueModifierOptionElement },
                valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.stencilDecal"),
                propertyKeyword = "decal",
                elementGetter = { material -> material.stencilDecalMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.roughness"),
                propertyKeyword = "map_Pr",
                elementGetter = { material -> material.roughnessMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.metalness"),
                propertyKeyword = "map_Pm",
                elementGetter = { material -> material.metalnessMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.normalmap"),
                propertyKeyword = "norm",
                elementGetter = { material -> material.normalMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.bump"),
                propertyKeyword = "bump",
                elementGetter = { material -> material.bumpMapElement }
            ),
            MaterialProperty.MaterialTexture(
                label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.reflection"),
                propertyKeyword = "refl",
                elementGetter = { material -> material.reflectionMapElement }
            )
        )
    }
}
