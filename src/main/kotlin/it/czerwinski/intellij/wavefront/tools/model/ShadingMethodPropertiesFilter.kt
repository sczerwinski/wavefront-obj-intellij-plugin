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

import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

enum class ShadingMethodPropertiesFilter(
    private val predicate: (Set<ShadingMethod>) -> Boolean
) {
    Unsupported(predicate = { shadingMethods -> shadingMethods.isEmpty() }),
    Material(predicate = { shadingMethods -> ShadingMethod.MATERIAL in shadingMethods }),
    PBR(predicate = { shadingMethods -> ShadingMethod.PBR in shadingMethods });

    fun matches(shadingMethods: Set<ShadingMethod>): Boolean = predicate(shadingMethods)

    companion object {
        val DEFAULT_SET = setOf(Material, PBR)
    }
}
