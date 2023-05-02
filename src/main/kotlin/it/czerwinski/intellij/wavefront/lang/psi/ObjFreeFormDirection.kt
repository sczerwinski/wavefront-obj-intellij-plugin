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

package it.czerwinski.intellij.wavefront.lang.psi

import it.czerwinski.intellij.wavefront.WavefrontObjBundle

enum class ObjFreeFormDirection {
    U, V;

    val description: String =
        WavefrontObjBundle.message("fileTypes.obj.structure.presentation.freeFormDirection.${name.lowercase()}")

    companion object {

        fun fromValue(value: String?): ObjFreeFormDirection? =
            value?.uppercase()?.let(::valueOf)
    }
}
