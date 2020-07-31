/*
 * Copyright 2020 Slawomir Czerwinski
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

package it.czerwinski.intellij.wavefront.language.psi

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider
import it.czerwinski.intellij.wavefront.language.ObjFileType
import it.czerwinski.intellij.wavefront.language.ObjLanguage

class ObjFile(
    viewProvider: FileViewProvider
) : PsiFileBase(viewProvider, ObjLanguage) {

    override fun getFileType(): FileType = ObjFileType

    override fun toString(): String = "Wavefront OBJ File"
}
