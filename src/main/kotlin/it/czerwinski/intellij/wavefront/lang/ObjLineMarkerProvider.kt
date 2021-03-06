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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.codeInsight.daemon.RelatedItemLineMarkerInfo
import com.intellij.codeInsight.daemon.RelatedItemLineMarkerProvider
import com.intellij.codeInsight.navigation.NavigationGutterIconBuilder
import com.intellij.psi.PsiElement
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes
import it.czerwinski.intellij.wavefront.lang.psi.util.containingObjFile

class ObjLineMarkerProvider : RelatedItemLineMarkerProvider() {

    override fun collectNavigationMarkers(
        element: PsiElement,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        when (element) {
            is ObjMaterialFileReference -> collectMaterialFileMarkers(element, result)
            is ObjMaterialReference -> collectMaterialMarkers(element, result)
        }
    }

    private fun collectMaterialFileMarkers(
        element: ObjMaterialFileReference,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val markedElement = element.node.findChildByType(ObjTypes.MATERIAL_FILE_NAME)?.psi
        val file = element.mtlFile
        if (markedElement != null && file != null) {
            val marker = NavigationGutterIconBuilder.create(OBJ_MATERIAL_FILE_ICON)
                .setTargets(file)
                .setTooltipText(WavefrontObjBundle.message("fileTypes.obj.marker.mtlFile"))
                .createLineMarkerInfo(markedElement)
            result.add(marker)
        }
    }

    private fun collectMaterialMarkers(
        element: ObjMaterialReference,
        result: MutableCollection<in RelatedItemLineMarkerInfo<*>>
    ) {
        val markedElement = element.node.findChildByType(ObjTypes.MATERIAL_NAME)?.psi
        val files = element.containingObjFile?.referencedMtlFiles.orEmpty()
        val materials = files.flatMap { file -> file.materialIdentifiers }
            .filter { material -> material.name == element.materialName }
        if (markedElement != null && materials.isNotEmpty()) {
            val marker = NavigationGutterIconBuilder.create(OBJ_MATERIAL_ICON)
                .setTargets(materials)
                .setTooltipText(WavefrontObjBundle.message("fileTypes.obj.marker.materials"))
                .createLineMarkerInfo(markedElement)
            result.add(marker)
        }
    }
}
