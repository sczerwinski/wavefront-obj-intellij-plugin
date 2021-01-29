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

package it.czerwinski.intellij.wavefront.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.OBJ_FILE_ICON
import it.czerwinski.intellij.wavefront.lang.ObjSyntaxHighlighter
import javax.swing.Icon

class ObjColorSettingsPage : ColorSettingsPage {

    override fun getIcon(): Icon? =
        OBJ_FILE_ICON

    override fun getHighlighter(): SyntaxHighlighter =
        ObjSyntaxHighlighter()

    override fun getDemoText(): String =
        """
            # OBJ File: 'cube.obj'
            mtllib cube.mtl
            o Cube
            v 1.000000 1.000000 -1.000000
            v 1.000000 -1.000000 -1.000000
            v 1.000000 1.000000 1.000000
            v 1.000000 -1.000000 1.000000
            v -1.000000 1.000000 -1.000000
            v -1.000000 -1.000000 -1.000000
            v -1.000000 1.000000 1.000000
            v -1.000000 -1.000000 1.000000
            vt 0.625000 0.500000
            vt 0.875000 0.500000
            vt 0.875000 0.750000
            vt 0.625000 0.750000
            vt 0.375000 0.750000
            vt 0.625000 1.000000
            vt 0.375000 1.000000
            vt 0.375000 0.000000
            vt 0.625000 0.000000
            vt 0.625000 0.250000
            vt 0.375000 0.250000
            vt 0.125000 0.500000
            vt 0.375000 0.500000
            vt 0.125000 0.750000
            vn 0.0000 1.0000 0.0000
            vn 0.0000 0.0000 1.0000
            vn -1.0000 0.0000 0.0000
            vn 0.0000 -1.0000 0.0000
            vn 1.0000 0.0000 0.0000
            vn 0.0000 0.0000 -1.0000
            usemtl White
            s off
            f 1/1/1 5/2/1 7/3/1 3/4/1
            f 4/5/2 3/4/2 7/6/2 8/7/2
            f 8/8/3 7/9/3 5/10/3 6/11/3
            f 6/12/4 2/13/4 4/5/4 8/14/4
            f 2/13/5 1/1/5 3/4/5 4/5/5
            f 6/11/6 5/10/6 1/1/6 2/13/6
        """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap(): MutableMap<String, TextAttributesKey>? = null

    override fun getAttributeDescriptors(): Array<AttributesDescriptor> = arrayOf(
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.keyword"),
            ObjSyntaxHighlighter.ATTR_KEYWORD
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.operator"),
            ObjSyntaxHighlighter.ATTR_OPERATOR
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.string"),
            ObjSyntaxHighlighter.ATTR_STRING
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.number"),
            ObjSyntaxHighlighter.ATTR_NUMBER
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.reference"),
            ObjSyntaxHighlighter.ATTR_REFERENCE
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.comment"),
            ObjSyntaxHighlighter.ATTR_COMMENT
        ),
        AttributesDescriptor(
            WavefrontObjBundle.message("settings.fileTypes.obj.coloring.attribute.badCharacter"),
            ObjSyntaxHighlighter.ATTR_BAD_CHARACTER
        )
    )

    override fun getColorDescriptors(): Array<ColorDescriptor> = ColorDescriptor.EMPTY_ARRAY

    override fun getDisplayName(): String =
        WavefrontObjBundle.message("settings.fileTypes.obj.name")
}
