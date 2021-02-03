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

package it.czerwinski.intellij.wavefront.editor.model

enum class UpVector(
    val x: Float,
    val y: Float,
    val z: Float
) {
    X_UP(x = 1f, y = 0f, z = 0f),
    Y_UP(x = 0f, y = 1f, z = 0f),
    Z_UP(x = 0f, y = 0f, z = 1f);

    companion object {
        val DEFAULT = Z_UP
    }
}
