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

package it.czerwinski.intellij.wavefront.lang.psi.impl

import it.czerwinski.intellij.wavefront.lang.psi.ObjFace
import it.czerwinski.intellij.wavefront.lang.psi.ObjFaceType
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialFileReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjMaterialReference
import it.czerwinski.intellij.wavefront.lang.psi.ObjTypes

object ObjPsiImplUtil {

    @JvmStatic
    fun getType(element: ObjFace): ObjFaceType? =
        ObjFaceType.fromVerticesCount(element.faceVertexList.size)

    @JvmStatic
    fun getFilename(element: ObjMaterialFileReference): String? =
        element.node.findChildByType(ObjTypes.REFERENCE)?.text

    @JvmStatic
    fun getMaterialName(element: ObjMaterialReference): String? =
        element.node.findChildByType(ObjTypes.REFERENCE)?.text
}
