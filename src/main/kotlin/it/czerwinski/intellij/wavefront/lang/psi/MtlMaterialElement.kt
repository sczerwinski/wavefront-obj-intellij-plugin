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

package it.czerwinski.intellij.wavefront.lang.psi

import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiElement
import java.awt.Color

interface MtlMaterialElement : PsiElement {

    val ambientColor: Color?
    val diffuseColor: Color?
    val specularColor: Color?
    val transmissionFilter: Color?

    val illumination: MtlIlluminationValueElement.Illumination?
    val dissolve: Float?
    val specularExponent: Float?
    val sharpness: Float?
    val opticalDensity: Float?

    val ambientColorMap: VirtualFile?
    val diffuseColorMap: VirtualFile?
    val specularColorMap: VirtualFile?
    val specularExponentMap: VirtualFile?
    val specularExponentBase: Float?
    val specularExponentGain: Float?
    val dissolveMap: VirtualFile?
    val displacementMap: VirtualFile?
    val displacementGain: Float?
    val stencilDecalMap: VirtualFile?
    val bumpMap: VirtualFile?
    val bumpMapMultiplier: Float?
    val reflectionMap: VirtualFile?

    fun getName(): String?
}
