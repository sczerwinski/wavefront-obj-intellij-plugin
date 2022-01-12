/*
 * Copyright 2020-2021 Slawomir Czerwinski
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

@file:Suppress("TooManyFunctions")

package it.czerwinski.intellij.wavefront.lang.structure

import com.intellij.icons.AllIcons
import com.intellij.ide.projectView.PresentationData
import com.intellij.navigation.ItemPresentation
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.ColorIcon
import it.czerwinski.intellij.wavefront.lang.MTL_FILE_ICON
import it.czerwinski.intellij.wavefront.lang.MTL_MATERIAL_ICON
import it.czerwinski.intellij.wavefront.lang.MTL_OPTION_ICON
import it.czerwinski.intellij.wavefront.lang.MTL_PROPERTY_ICON
import it.czerwinski.intellij.wavefront.lang.MTL_TEXTURE_ICON
import it.czerwinski.intellij.wavefront.lang.psi.MtlAmbientColor
import it.czerwinski.intellij.wavefront.lang.psi.MtlAmbientColorMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlBlendUOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlBlendVOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlBumpMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlBumpMultiplierOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlClampOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorCorrectionOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlColorElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlDiffuseColor
import it.czerwinski.intellij.wavefront.lang.psi.MtlDiffuseColorMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlDisplacementMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlDissolve
import it.czerwinski.intellij.wavefront.lang.psi.MtlDissolveMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlEmissionColor
import it.czerwinski.intellij.wavefront.lang.psi.MtlEmissionColorMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlFile
import it.czerwinski.intellij.wavefront.lang.psi.MtlIllumination
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.MtlMetalness
import it.czerwinski.intellij.wavefront.lang.psi.MtlMetalnessMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlNormalMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlOffsetOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlOpticalDensity
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlReflectionTypeOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlResolutionOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlRoughness
import it.czerwinski.intellij.wavefront.lang.psi.MtlRoughnessMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlScalarChannelOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlScaleOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlSharpness
import it.czerwinski.intellij.wavefront.lang.psi.MtlSpecularColor
import it.czerwinski.intellij.wavefront.lang.psi.MtlSpecularColorMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlSpecularExponent
import it.czerwinski.intellij.wavefront.lang.psi.MtlSpecularExponentMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlStencilDecalMap
import it.czerwinski.intellij.wavefront.lang.psi.MtlTextureElement
import it.czerwinski.intellij.wavefront.lang.psi.MtlTransmissionFilter
import it.czerwinski.intellij.wavefront.lang.psi.MtlTurbulenceOption
import it.czerwinski.intellij.wavefront.lang.psi.MtlValueModifierOption
import it.czerwinski.intellij.wavefront.lang.psi.valuesString
import javax.swing.Icon

object MtlItemPresentationFactory {

    @Suppress("ComplexMethod")
    fun createPresentation(element: PsiElement): ItemPresentation = when (element) {
        is MtlFile -> createPresentation(element)

        is MtlMaterial -> createPresentation(element)

        is MtlAmbientColor -> createPresentation(element)
        is MtlDiffuseColor -> createPresentation(element)
        is MtlSpecularColor -> createPresentation(element)
        is MtlTransmissionFilter -> createPresentation(element)
        is MtlEmissionColor -> createPresentation(element)

        is MtlIllumination -> createPresentation(element)
        is MtlDissolve -> createPresentation(element)
        is MtlSpecularExponent -> createPresentation(element)
        is MtlSharpness -> createPresentation(element)
        is MtlOpticalDensity -> createPresentation(element)
        is MtlRoughness -> createPresentation(element)
        is MtlMetalness -> createPresentation(element)

        is MtlAmbientColorMap -> createPresentation(element)
        is MtlDiffuseColorMap -> createPresentation(element)
        is MtlSpecularColorMap -> createPresentation(element)
        is MtlEmissionColorMap -> createPresentation(element)
        is MtlSpecularExponentMap -> createPresentation(element)
        is MtlDissolveMap -> createPresentation(element)
        is MtlDisplacementMap -> createPresentation(element)
        is MtlStencilDecalMap -> createPresentation(element)
        is MtlRoughnessMap -> createPresentation(element)
        is MtlMetalnessMap -> createPresentation(element)
        is MtlNormalMap -> createPresentation(element)
        is MtlBumpMap -> createPresentation(element)
        is MtlReflectionMap -> createPresentation(element)

        is MtlBlendUOption -> createPresentation(element)
        is MtlBlendVOption -> createPresentation(element)
        is MtlColorCorrectionOption -> createPresentation(element)
        is MtlClampOption -> createPresentation(element)
        is MtlScalarChannelOption -> createPresentation(element)
        is MtlValueModifierOption -> createPresentation(element)
        is MtlOffsetOption -> createPresentation(element)
        is MtlScaleOption -> createPresentation(element)
        is MtlTurbulenceOption -> createPresentation(element)
        is MtlResolutionOption -> createPresentation(element)
        is MtlBumpMultiplierOption -> createPresentation(element)
        is MtlReflectionTypeOption -> createPresentation(element)

        else -> createErrorPresentation(
            errorMessage = WavefrontObjBundle.message(
                "fileTypes.mtl.structure.presentation.error.unknownElement"
            ),
            elementText = element.text
        )
    }

    private fun createPresentation(file: MtlFile): ItemPresentation = createPresentation(
        presentableText = file.name,
        icon = MTL_FILE_ICON
    )

    private fun createPresentation(material: MtlMaterial): ItemPresentation = createPresentation(
        presentableText = material.getName() ?: WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.material"
        ),
        icon = MTL_MATERIAL_ICON
    )

    private fun createPresentation(color: MtlAmbientColor): ItemPresentation = createColorPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.ambientColor"
        ),
        color = color
    )

    private fun createColorPresentation(presentableText: String, color: MtlColorElement) =
        createPresentation(
            presentableText = presentableText,
            locationString = color.colorString.orEmpty(),
            icon = color.color?.let(::ColorIcon) ?: AllIcons.General.Error
        )

    private fun createPresentation(color: MtlDiffuseColor): ItemPresentation = createColorPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.diffuseColor"
        ),
        color = color
    )

    private fun createPresentation(color: MtlSpecularColor): ItemPresentation = createColorPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.specularColor"
        ),
        color = color
    )

    private fun createPresentation(color: MtlTransmissionFilter): ItemPresentation = createColorPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.transmissionFilter"
        ),
        color = color
    )

    private fun createPresentation(color: MtlEmissionColor): ItemPresentation = createColorPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.emissionColor"
        ),
        color = color
    )

    private fun createPresentation(illumination: MtlIllumination): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.illumination"
        ),
        locationString = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.illumination.${illumination.value}"
        )
    )

    private fun createPropertyPresentation(presentableText: String, locationString: String) =
        createPresentation(
            presentableText = presentableText,
            locationString = locationString,
            icon = MTL_PROPERTY_ICON
        )

    private fun createPresentation(dissolve: MtlDissolve): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.dissolve"
        ),
        locationString = dissolve.value?.toString().orEmpty()
    )

    private fun createPresentation(specularExponent: MtlSpecularExponent): ItemPresentation =
        createPropertyPresentation(
            presentableText = WavefrontObjBundle.message(
                "fileTypes.mtl.structure.presentation.specularExponent"
            ),
            locationString = specularExponent.value?.toString().orEmpty()
        )

    private fun createPresentation(sharpness: MtlSharpness): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.sharpness"
        ),
        locationString = sharpness.value?.toString().orEmpty()
    )

    private fun createPresentation(opticalDensity: MtlOpticalDensity): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.opticalDensity"
        ),
        locationString = opticalDensity.value?.toString().orEmpty()
    )

    private fun createPresentation(roughness: MtlRoughness): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.roughness"
        ),
        locationString = roughness.value?.toString().orEmpty()
    )

    private fun createPresentation(metalness: MtlMetalness): ItemPresentation = createPropertyPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.metalness"
        ),
        locationString = metalness.value?.toString().orEmpty()
    )

    private fun createPresentation(map: MtlAmbientColorMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.ambientColorMap"
        ),
        texture = map
    )

    private fun createTexturePresentation(presentableText: String, texture: MtlTextureElement) =
        createPresentation(
            presentableText = presentableText,
            locationString = texture.textureFilename.orEmpty(),
            icon = MTL_TEXTURE_ICON
        )

    private fun createPresentation(map: MtlDiffuseColorMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.diffuseColorMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlSpecularColorMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.specularColorMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlEmissionColorMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.emissionColorMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlSpecularExponentMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.specularExponentMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlDissolveMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.dissolveMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlDisplacementMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.displacementMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlStencilDecalMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.stencilDecalMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlRoughnessMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.roughnessMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlMetalnessMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.metalnessMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlNormalMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.normalmap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlBumpMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.bumpMap"
        ),
        texture = map
    )

    private fun createPresentation(map: MtlReflectionMap): ItemPresentation = createTexturePresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.reflectionMap"
        ),
        texture = map
    )

    private fun createPresentation(option: MtlBlendUOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.blendU"
        ),
        locationString = option.value?.let(::flagValueText)
    )

    private fun flagValueText(flag: Boolean) = WavefrontObjBundle.message(
        if (flag) "fileTypes.mtl.structure.presentation.flag.true"
        else "fileTypes.mtl.structure.presentation.flag.false"
    )

    private fun createOptionPresentation(presentableText: String, locationString: String?) =
        createPresentation(
            presentableText = presentableText,
            locationString = locationString.orEmpty(),
            icon = MTL_OPTION_ICON
        )

    private fun createPresentation(option: MtlBlendVOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.blendV"
        ),
        locationString = option.value?.let(::flagValueText)
    )

    private fun createPresentation(option: MtlColorCorrectionOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.colorCorrection"
        ),
        locationString = option.value?.let(::flagValueText)
    )

    private fun createPresentation(option: MtlClampOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.clamp"
        ),
        locationString = option.value?.let(::flagValueText)
    )

    private fun createPresentation(option: MtlScalarChannelOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.scalarChannel"
        ),
        locationString = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.scalarChannel.${option.value}"
        )
    )

    private fun createPresentation(option: MtlValueModifierOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.valueModifier"
        ),
        locationString = option.valuesString
    )

    private fun createPresentation(option: MtlOffsetOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.offset"
        ),
        locationString = option.valuesString
    )

    private fun createPresentation(option: MtlScaleOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.scale"
        ),
        locationString = option.valuesString
    )

    private fun createPresentation(option: MtlTurbulenceOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.turbulence"
        ),
        locationString = option.valuesString
    )

    private fun createPresentation(option: MtlResolutionOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.resolution"
        ),
        locationString = option.value?.toString()
    )

    private fun createPresentation(option: MtlBumpMultiplierOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.bumpMultiplier"
        ),
        locationString = option.value?.toString()
    )

    private fun createPresentation(option: MtlReflectionTypeOption): ItemPresentation = createOptionPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.reflectionType"
        ),
        locationString = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.reflectionType.${option.value}"
        )
    )

    private fun createErrorPresentation(
        errorMessage: String,
        elementText: String
    ): ItemPresentation = createPresentation(
        presentableText = WavefrontObjBundle.message(
            "fileTypes.mtl.structure.presentation.error",
            errorMessage
        ),
        locationString = elementText,
        icon = AllIcons.General.Error
    )

    private fun createPresentation(
        presentableText: String,
        locationString: String = "",
        icon: Icon
    ): ItemPresentation =
        PresentationData(presentableText, locationString, icon, null)
}
