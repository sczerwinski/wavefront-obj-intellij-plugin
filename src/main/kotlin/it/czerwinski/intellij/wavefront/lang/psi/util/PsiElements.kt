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

package it.czerwinski.intellij.wavefront.lang.psi.util

import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.refactoring.suggested.startOffset
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile

val PsiElement.containingObjFile: ObjFile? get() = containingFile as? ObjFile

inline fun <reified T : PsiElement> PsiElement.getChildrenOfType(): List<T> =
    PsiTreeUtil.getChildrenOfType(this, T::class.java)?.filterNotNull().orEmpty()

inline fun <reified T : PsiElement> PsiElement.countChildrenOfType(): Int =
    PsiTreeUtil.countChildrenOfType(this, T::class.java)

inline fun <reified T : PsiElement> PsiElement.countChildrenOfTypeBefore(element: PsiElement): Int =
    getChildrenOfType<T>()
        .filter { child -> child.startOffset < element.startOffset }
        .size
