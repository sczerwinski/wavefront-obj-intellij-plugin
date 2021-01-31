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

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiFileFactory
import it.czerwinski.intellij.wavefront.lang.ObjFileType
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

object ObjElementFactory {

    fun createObjectOrGroupIdentifier(project: Project, name: String): ObjObjectOrGroupIdentifier {
        val file = createFile(project, text = "o $name")
        return (file.firstChild as ObjObject).getChildrenOfType<ObjObjectOrGroupIdentifier>().single()
    }

    fun createMaterialFileReference(project: Project, filename: String): ObjMaterialFileReference {
        val file = createFile(project, text = "mtllib $filename")
        return file.firstChild as ObjMaterialFileReference
    }

    fun createMaterialReference(project: Project, name: String): ObjMaterialReference {
        val file = createFile(project, text = "usemtl $name")
        return file.firstChild as ObjMaterialReference
    }

    private fun createFile(project: Project, text: String): ObjFile {
        val name = "temp.obj"
        return PsiFileFactory.getInstance(project)
            .createFileFromText(name, ObjFileType, text) as ObjFile
    }
}
