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

import it.czerwinski.intellij.wavefront.WavefrontObjBundle

internal val materialProperties = listOf(
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.ambient"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.ambient.action"),
        propertyKeyword = "Ka",
        elementGetter = { material -> material.ambientColorElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.diffuse"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.diffuse.action"),
        propertyKeyword = "Kd",
        elementGetter = { material -> material.diffuseColorElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.specular"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.specular.action"),
        propertyKeyword = "Ks",
        elementGetter = { material -> material.specularColorElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.transmissionFilter"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.transmissionFilter.action"),
        propertyKeyword = "Tf",
        elementGetter = { material -> material.transmissionFilterElement }
    ),
    MaterialProperty.MaterialColor(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.emission"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.color.emission.action"),
        propertyKeyword = "Ke",
        elementGetter = { material -> material.emissionColorElement }
    ),
    MaterialProperty.MaterialIlluminationValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.illumination"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.illumination.action"),
        propertyKeyword = "illum",
        elementGetter = { material -> material.illuminationElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.dissolve"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.dissolve.action"),
        propertyKeyword = "d",
        elementGetter = { material -> material.dissolveElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.specularExponent"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.specularExponent.action"),
        propertyKeyword = "Ns",
        elementGetter = { material -> material.specularExponentElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.sharpness"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.sharpness.action"),
        propertyKeyword = "sharpness",
        elementGetter = { material -> material.sharpnessElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.opticalDensity"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.opticalDensity.action"),
        propertyKeyword = "Ni",
        elementGetter = { material -> material.opticalDensityElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.roughness"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.roughness.action"),
        propertyKeyword = "Pr",
        elementGetter = { material -> material.roughnessElement }
    ),
    MaterialProperty.MaterialFloatValue(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.metalness"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.value.metalness.action"),
        propertyKeyword = "Pm",
        elementGetter = { material -> material.metalnessElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.ambient"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.ambient.action"),
        propertyKeyword = "map_Ka",
        elementGetter = { material -> material.ambientColorMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.diffuse"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.diffuse.action"),
        propertyKeyword = "map_Kd",
        elementGetter = { material -> material.diffuseColorMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specular"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specular.action"),
        propertyKeyword = "map_Ks",
        elementGetter = { material -> material.specularColorMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.emission"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.emission.action"),
        propertyKeyword = "map_Ke",
        elementGetter = { material -> material.emissionColorMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent.action"),
        propertyKeyword = "map_Ns",
        elementGetter = { material -> material.specularExponentMapElement }
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent.base"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.valueModifier.action"),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.specularExponentMapElement },
        elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_BASE
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.specularExponent.gain"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.valueModifier.action"),
        propertyKeyword = "map_Ns",
        parentElementGetter = { material -> material.specularExponentMapElement },
        elementGetter = { material -> material.specularExponentMapElement?.valueModifierOptionElement },
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.dissolve"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.dissolve.action"),
        propertyKeyword = "map_d",
        elementGetter = { material -> material.dissolveMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.displacement"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.displacement.action"),
        propertyKeyword = "disp",
        elementGetter = { material -> material.displacementMapElement }
    ),
    MaterialProperty.MaterialTextureValueModifier(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.displacement.gain"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.valueModifier.action"),
        propertyKeyword = "disp",
        parentElementGetter = { material -> material.displacementMapElement },
        elementGetter = { material -> material.displacementMapElement?.valueModifierOptionElement },
        valueIndex = MaterialProperty.MaterialTextureValueModifier.VALUE_INDEX_GAIN
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.stencilDecal"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.stencilDecal.action"),
        propertyKeyword = "decal",
        elementGetter = { material -> material.stencilDecalMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.roughness"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.roughness.action"),
        propertyKeyword = "map_Pr",
        elementGetter = { material -> material.roughnessMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.metalness"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.metalness.action"),
        propertyKeyword = "map_Pm",
        elementGetter = { material -> material.metalnessMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.normalmap"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.normalmap.action"),
        propertyKeyword = "norm",
        elementGetter = { material -> material.normalMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.bump"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.bump.action"),
        propertyKeyword = "bump",
        elementGetter = { material -> material.bumpMapElement }
    ),
    MaterialProperty.MaterialTexture(
        label = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.reflection"),
        actionName = WavefrontObjBundle.message("editor.fileTypes.mtl.material.texture.reflection.action"),
        propertyKeyword = "refl",
        elementGetter = { material -> material.reflectionMapElement }
    )
)
