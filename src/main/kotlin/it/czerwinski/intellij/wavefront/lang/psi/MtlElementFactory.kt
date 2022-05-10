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

package it.czerwinski.intellij.wavefront.lang.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiFileFactory
import it.czerwinski.intellij.wavefront.lang.MtlFileType
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

object MtlElementFactory {

    fun createMaterial(project: Project, name: String): MtlMaterial {
        val file = createFile(project, text = "newmtl $name")
        return (file.firstChild as MtlMaterial)
    }

    fun createMaterialIdentifier(project: Project, name: String): MtlMaterialIdentifier =
        createMaterial(project, name).getChildrenOfType<MtlMaterialIdentifier>().single()

    fun createColorElement(project: Project, colorComponents: FloatArray): MtlColorElement {
        val file = createFile(project, text = "newmtl temp\n\tKd ${colorComponents.joinToString(separator = " ")}")
        return (file.firstChild as MtlMaterial).getChildrenOfType<MtlColorElement>().single()
    }

    fun createTextureElement(project: Project, name: String): MtlTextureElement {
        val file = createFile(project, text = "newmtl temp\n\tbump $name")
        return (file.firstChild as MtlMaterial).getChildrenOfType<MtlTextureElement>().single()
    }

    fun createCRLF(project: Project): PsiElement {
        val file = createFile(project, text = "\n")
        return file.firstChild
    }

    fun createTODO(project: Project): PsiElement {
        val file = createFile(project, text = "# TODO")
        return file.firstChild
    }

    private fun createFile(project: Project, text: String): MtlFile {
        val name = "temp.mtl"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, MtlFileType, text) as MtlFile
    }
}
