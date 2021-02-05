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

import com.intellij.navigation.ChooseByNameContributor
import com.intellij.navigation.NavigationItem
import com.intellij.openapi.project.Project
import it.czerwinski.intellij.wavefront.lang.psi.util.findAllMtlFiles
import it.czerwinski.intellij.wavefront.lang.psi.util.findMaterialIdentifiers
import it.czerwinski.intellij.wavefront.lang.psi.util.findMaterials

class MtlChooseByNameContributor : ChooseByNameContributor {

    override fun getNames(project: Project?, includeNonProjectItems: Boolean): Array<String> =
        project?.let(::findAllMtlFiles)
            ?.flatMap { file -> file.findMaterials() }
            ?.mapNotNull { material -> material.getName()?.takeIf(String::isNotBlank) }
            .orEmpty()
            .toTypedArray()

    override fun getItemsByName(
        name: String?,
        pattern: String?,
        project: Project?,
        includeNonProjectItems: Boolean
    ): Array<NavigationItem> =
        project?.let(::findAllMtlFiles)
            ?.flatMap { file -> file.findMaterialIdentifiers() }
            ?.filter { material -> material.name == name }
            ?.map { it as NavigationItem }
            .orEmpty()
            .toTypedArray()
}
