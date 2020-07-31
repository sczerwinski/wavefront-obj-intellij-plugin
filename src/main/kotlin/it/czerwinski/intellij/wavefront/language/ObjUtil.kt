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

@file:JvmName("ObjUtil")

package it.czerwinski.intellij.wavefront.language

import com.intellij.psi.PsiFile
import com.intellij.psi.util.PsiTreeUtil
import it.czerwinski.intellij.wavefront.language.psi.ObjGroup
import it.czerwinski.intellij.wavefront.language.psi.ObjObject
import it.czerwinski.intellij.wavefront.language.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.language.psi.ObjVertex
import it.czerwinski.intellij.wavefront.language.psi.ObjVertexNormal

fun findAllVertices(file: PsiFile): List<ObjVertex> =
    PsiTreeUtil.getChildrenOfAnyType(file, ObjObject::class.java, ObjGroup::class.java)
        .let { listOf(file) + it.toList() }
        .flatMap { parent ->
            PsiTreeUtil.getChildrenOfType(parent, ObjVertex::class.java)?.toList().orEmpty()
        }
        .filterNotNull()

fun findVertex(file: PsiFile, index: Int): ObjVertex? =
    findAllVertices(file).getOrNull(index - 1)

fun findAllTextureCoordinates(file: PsiFile): List<ObjTextureCoordinates> =
    PsiTreeUtil.getChildrenOfAnyType(file, ObjObject::class.java, ObjGroup::class.java)
        .let { listOf(file) + it.toList() }
        .flatMap { parent ->
            PsiTreeUtil.getChildrenOfType(parent, ObjTextureCoordinates::class.java)?.toList().orEmpty()
        }
        .filterNotNull()

fun findTextureCoordinates(file: PsiFile, index: Int): ObjTextureCoordinates? =
    findAllTextureCoordinates(file).getOrNull(index - 1)

fun findAllVertexNormals(file: PsiFile): List<ObjVertexNormal> =
    PsiTreeUtil.getChildrenOfAnyType(file, ObjObject::class.java, ObjGroup::class.java)
        .let { listOf(file) + it.toList() }
        .flatMap { parent ->
            PsiTreeUtil.getChildrenOfType(parent, ObjVertexNormal::class.java)?.toList().orEmpty()
        }
        .filterNotNull()

fun findVertexNormal(file: PsiFile, index: Int): ObjVertexNormal? =
    findAllVertexNormals(file).getOrNull(index - 1)
