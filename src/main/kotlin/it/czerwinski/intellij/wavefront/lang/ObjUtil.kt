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

package it.czerwinski.intellij.wavefront.lang

import com.intellij.psi.PsiFile
import it.czerwinski.intellij.wavefront.lang.psi.ObjGroup
import it.czerwinski.intellij.wavefront.lang.psi.ObjObject
import it.czerwinski.intellij.wavefront.lang.psi.ObjTextureCoordinates
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertex
import it.czerwinski.intellij.wavefront.lang.psi.ObjVertexNormal
import it.czerwinski.intellij.wavefront.lang.psi.util.countChildrenOfType
import it.czerwinski.intellij.wavefront.lang.psi.util.getChildrenOfType

fun checkVertexExists(file: PsiFile, index: Int): Boolean =
    index in 1..countVertices(file)

private fun countVertices(file: PsiFile) =
    (listOf(file) + file.getChildrenOfType<ObjObject>() + file.getChildrenOfType<ObjGroup>())
        .sumBy { it.countChildrenOfType<ObjVertex>() }

fun checkTextureCoordinatesExist(file: PsiFile, index: Int): Boolean =
    index in 1..countTextureCoordinates(file)

private fun countTextureCoordinates(file: PsiFile) =
    (listOf(file) + file.getChildrenOfType<ObjObject>() + file.getChildrenOfType<ObjGroup>())
        .sumBy { it.countChildrenOfType<ObjTextureCoordinates>() }

fun checkVertexNormalExists(file: PsiFile, index: Int): Boolean =
    index in 1..countVertexNormals(file)

private fun countVertexNormals(file: PsiFile) =
    (listOf(file) + file.getChildrenOfType<ObjObject>() + file.getChildrenOfType<ObjGroup>())
        .sumBy { it.countChildrenOfType<ObjVertexNormal>() }
