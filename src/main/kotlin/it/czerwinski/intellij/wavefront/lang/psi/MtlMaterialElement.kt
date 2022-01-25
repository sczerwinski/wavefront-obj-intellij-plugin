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
import com.intellij.psi.PsiFile
import graphics.glimpse.types.Vec3

interface MtlMaterialElement : PsiElement {

    val ambientColorVector: Vec3?
    val diffuseColorVector: Vec3?
    val specularColorVector: Vec3?
    val transmissionFilterVector: Vec3?
    val emissionColorVector: Vec3?

    val illumination: MtlIlluminationValueElement.Illumination?
    val dissolve: Float?
    val specularExponent: Float?
    val sharpness: Float?
    val opticalDensity: Float?
    val roughness: Float?
    val metalness: Float?

    val ambientColorMap: VirtualFile?
    val diffuseColorMap: VirtualFile?
    val specularColorMap: VirtualFile?
    val emissionColorMap: VirtualFile?
    val specularExponentMap: VirtualFile?
    val specularExponentBase: Float?
    val specularExponentGain: Float?
    val dissolveMap: VirtualFile?
    val displacementMap: VirtualFile?
    val displacementGain: Float?
    val stencilDecalMap: VirtualFile?
    val roughnessMap: VirtualFile?
    val metalnessMap: VirtualFile?
    val normalMap: VirtualFile?
    val bumpMap: VirtualFile?
    val bumpMapMultiplier: Float?
    val reflectionMap: VirtualFile?

    val texturePsiFiles: List<PsiFile>

    fun getName(): String?
}
