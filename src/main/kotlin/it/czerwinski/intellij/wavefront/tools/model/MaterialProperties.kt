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

import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

internal val allMaterialProperties = listOf(
    MaterialProperty.MaterialName(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.string.name"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.string.name.action"),
        propertyKeyword = "newmtl",
        elementGetter = { material -> material.identifierElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.ambient"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.ambient.action"),
        propertyKeyword = "Ka",
        elementGetter = { material -> material.ambientColorElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.diffuse"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.diffuse.action"),
        propertyKeyword = "Kd",
        elementGetter = { material -> material.diffuseColorElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.specular"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.specular.action"),
        propertyKeyword = "Ks",
        elementGetter = { material -> material.specularColorElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.transmissionFilter"),
        actionName = WavefrontObjBundle.message(
            "toolwindow.MaterialPropertiesToolWindow.color.transmissionFilter.action"
        ),
        propertyKeyword = "Tf",
        elementGetter = { material -> material.transmissionFilterElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.emission"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.color.emission.action"),
        propertyKeyword = "Ke",
        elementGetter = { material -> material.emissionColorElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialIlluminationValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.illumination"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.illumination.action"),
        propertyKeyword = "illum",
        elementGetter = { material -> material.illuminationElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.dissolve"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.dissolve.action"),
        propertyKeyword = "d",
        elementGetter = { material -> material.dissolveElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.specularExponent"),
        actionName = WavefrontObjBundle.message(
            "toolwindow.MaterialPropertiesToolWindow.value.specularExponent.action"
        ),
        propertyKeyword = "Ns",
        elementGetter = { material -> material.specularExponentElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.sharpness"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.sharpness.action"),
        propertyKeyword = "sharpness",
        elementGetter = { material -> material.sharpnessElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.opticalDensity"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.opticalDensity.action"),
        propertyKeyword = "Ni",
        elementGetter = { material -> material.opticalDensityElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.roughness"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.roughness.action"),
        propertyKeyword = "Pr",
        elementGetter = { material -> material.roughnessElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.metalness"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.value.metalness.action"),
        propertyKeyword = "Pm",
        elementGetter = { material -> material.metalnessElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.ambient"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.ambient.action"),
        propertyKeyword = "map_Ka",
        elementGetter = { material -> material.ambientColorMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.diffuse"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.diffuse.action"),
        propertyKeyword = "map_Kd",
        elementGetter = { material -> material.diffuseColorMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specular"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specular.action"),
        propertyKeyword = "map_Ks",
        elementGetter = { material -> material.specularColorMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.emission"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.emission.action"),
        propertyKeyword = "map_Ke",
        elementGetter = { material -> material.emissionColorMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specularExponent"),
        actionName = WavefrontObjBundle.message(
            "toolwindow.MaterialPropertiesToolWindow.texture.specularExponent.action"
        ),
        propertyKeyword = "map_Ns",
        elementGetter = { material -> material.specularExponentMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specularExponent.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.specularExponentMapElement },
        elementGetter = { material -> material.specularExponentMapElement?.scalarChannelOptionElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL)
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specularExponent.base"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.valueModifier.action"),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.specularExponentMapElement },
        elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL),
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_BASE
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.specularExponent.gain"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.valueModifier.action"),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.specularExponentMapElement },
        elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL),
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.dissolve"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.dissolve.action"),
        propertyKeyword = "map_d",
        elementGetter = { material -> material.dissolveMapElement }
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.dissolve.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "map_d",
        parentElementGetter = { material -> material.dissolveMapElement },
        elementGetter = { material -> material.dissolveMapElement?.scalarChannelOptionElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.displacement"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.displacement.action"),
        propertyKeyword = "disp",
        elementGetter = { material -> material.displacementMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.displacement.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "disp",
        parentElementGetter = { material -> material.displacementMapElement },
        elementGetter = { material -> material.displacementMapElement?.scalarChannelOptionElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.displacement.gain"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.valueModifier.action"),
        propertyKeyword = "disp",
        parentElementGetter = { material -> material.displacementMapElement },
        elementGetter = { material -> material.displacementMapElement?.valueModifierOptionElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR),
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.stencilDecal"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.stencilDecal.action"),
        propertyKeyword = "decal",
        elementGetter = { material -> material.stencilDecalMapElement }
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.stencilDecal.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "decal",
        parentElementGetter = { material -> material.stencilDecalMapElement },
        elementGetter = { material -> material.stencilDecalMapElement?.scalarChannelOptionElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.roughness"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.roughness.action"),
        propertyKeyword = "map_Pr",
        elementGetter = { material -> material.roughnessMapElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.roughness.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "map_Pr",
        parentElementGetter = { material -> material.roughnessMapElement },
        elementGetter = { material -> material.roughnessMapElement?.scalarChannelOptionElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.metalness"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.metalness.action"),
        propertyKeyword = "map_Pm",
        elementGetter = { material -> material.metalnessMapElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTextureScalarChannel(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.metalness.imfchan"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.colorChannel.action"),
        propertyKeyword = "map_Pm",
        parentElementGetter = { material -> material.metalnessMapElement },
        elementGetter = { material -> material.metalnessMapElement?.scalarChannelOptionElement },
        shadingMethods = setOf(ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.normalmap"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.normalmap.action"),
        propertyKeyword = "norm",
        elementGetter = { material -> material.normalMapElement },
        shadingMethods = setOf(ShadingMethod.MATERIAL, ShadingMethod.PBR)
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.bump"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.bump.action"),
        propertyKeyword = "bump",
        elementGetter = { material -> material.bumpMapElement }
    ),
    MaterialProperty.MaterialReflectionTexture(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.reflection"),
        actionName = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.reflection.action")
    ),
    MaterialProperty.MaterialReflectionTextureType(
        label = WavefrontObjBundle.message("toolwindow.MaterialPropertiesToolWindow.texture.reflection.type"),
        actionName = WavefrontObjBundle.message(
            "toolwindow.MaterialPropertiesToolWindow.texture.reflectionType.action"
        ),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.reflectionMapElement },
        elementGetter = { material -> material.reflectionMapElement?.reflectionTypeOptionElement }
    )
)
