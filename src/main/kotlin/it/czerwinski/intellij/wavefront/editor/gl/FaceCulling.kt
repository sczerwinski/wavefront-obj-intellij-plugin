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

package it.czerwinski.intellij.wavefront.editor.gl

import graphics.glimpse.FaceCullingMode

val FaceCullingMode.isFrontCulling
    get() = this in listOf(FaceCullingMode.FRONT, FaceCullingMode.FRONT_AND_BACK)

val FaceCullingMode.isBackCulling
    get() = this in listOf(FaceCullingMode.BACK, FaceCullingMode.FRONT_AND_BACK)

@Suppress("FunctionName")
fun FaceCulling(front: Boolean, back: Boolean): FaceCullingMode = when {
    front && back -> FaceCullingMode.FRONT_AND_BACK
    front -> FaceCullingMode.FRONT
    back -> FaceCullingMode.BACK
    else -> FaceCullingMode.DISABLED
}
