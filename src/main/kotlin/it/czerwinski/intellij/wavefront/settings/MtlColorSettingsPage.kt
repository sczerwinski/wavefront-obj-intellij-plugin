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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.MTL_FILE_ICON
import it.czerwinski.intellij.wavefront.lang.MtlSyntaxHighlighter
import javax.swing.Icon

class MtlColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon =
        MTL_FILE_ICON

    override fun getHighlighter(): SyntaxHighlighter =
        MtlSyntaxHighlighter()

    override fun getDemoText(): String =
        """
            # MTL File: 'cube.mtl'
            newmtl Cube_Material
                Ka 0.0435 0.0435 0.0435
                Kd 0.1086 0.1086 0.1086
                Ks 0.0000 0.0000 0.0000
                Tf 0.9885 0.9885 0.9885
                illum 6
                d 0.6600
                Ns 10.0000
                sharpness 60
                Ni 1.19713
    
                map_Ka -blendu on -blendv on -s 1 1 1 -o 0 0 0 -mm 0 1 chrome.mpc
                map_Kd -s 1 1 1 -o 0 0 0 -mm 0 1 chrome.mpc
                map_Ks -s 1 1 1 -o 0 0 0 -mm 0 1 chrome.mpc
                map_Ns -s 1 1 1 -o 0 0 0 -mm 0 1 wisp.mps
                map_d -s 1 1 1 -o 0 0 0 -mm 0 1 wisp.mps
                disp -s 1 1 0.5 wisp.mps
                decal -s 1 1 1 -o 0 0 0 -mm 0 1 sand.mps
                bump -s 1 1 1 -o 0 0 0 -bm 1 sand.mpb
    
                refl -type sphere -mm 0 1 clouds.mpc
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.keyword"),
            MtlSyntaxHighlighter.ATTR_KEYWORD
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.optionName"),
            MtlSyntaxHighlighter.ATTR_OPTION_NAME
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.declaration"),
            MtlSyntaxHighlighter.ATTR_DECLARATION
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.number"),
            MtlSyntaxHighlighter.ATTR_NUMBER
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.constant"),
            MtlSyntaxHighlighter.ATTR_CONSTANT
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.reference"),
            MtlSyntaxHighlighter.ATTR_REFERENCE
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.comment"),
            MtlSyntaxHighlighter.ATTR_COMMENT
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.mtl.coloring.attribute.badCharacter"),
            MtlSyntaxHighlighter.ATTR_BAD_CHARACTER
        )
    )

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String =
        WavefrontObjBundle.message("settings.fileTypes.mtl.name")
}
