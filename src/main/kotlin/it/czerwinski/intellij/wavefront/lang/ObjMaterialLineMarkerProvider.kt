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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile
import javax.swing.Icon

class ObjMaterialLineMarkerProvider : RelatedItemLineMarkerProvider() {

    private val myName: String by lazy {
        WavefrontObjBundle.message("fileTypes.obj.marker.material.name")
    }

    private val myTooltip: String by lazy {
        WavefrontObjBundle.message("fileTypes.obj.marker.material.tooltip")
    }

    override fun getName(): String = myName

    override fun getIcon(): Icon = OBJ_MATERIAL_ICON

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        if (element is ObjMaterialReference) {
            val markedElement = element.node.findChildByType(ObjTypes.MATERIAL_NAME)?.psi
            val files = element.containingObjFile?.referencedMtlFiles.orEmpty()
            val materials = files.flatMap { file -> file.materialIdentifiers }
                .filter { material -> material.name == element.materialName }
            if (markedElement != null && materials.isNotEmpty()) {
                val marker = NavigationGutterIconBuilder.create(icon)
                    .setTargets(materials)
                    .setTooltipText(myTooltip)
                    .createLineMarkerInfo(markedElement)
                result.add(marker)
            }
        }
    }
}
