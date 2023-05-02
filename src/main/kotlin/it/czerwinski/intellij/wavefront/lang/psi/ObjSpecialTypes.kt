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

object ObjSpecialTypes {
    @JvmField val UNKNOWN_KEYWORD = ObjTokenType(debugName = "UNKNOWN_KEYWORD")
    @JvmField val UNKNOWN_SMOOTHING_GROUP_NUMBER = ObjTokenType(debugName = "UNKNOWN_SMOOTHING_GROUP_NUMBER")
    @JvmField val UNKNOWN_FREE_FORM_TYPE = ObjTokenType(debugName = "UNKNOWN_FREE_FORM_TYPE")
    @JvmField val UNKNOWN_FREE_FORM_DIRECTION = ObjTokenType(debugName = "UNKNOWN_FREE_FORM_DIRECTION")
}
