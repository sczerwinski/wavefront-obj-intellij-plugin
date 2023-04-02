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

object MtlUnknownTypes {
    @JvmField val UNKNOWN_KEYWORD = MtlTokenType(debugName = "UNKNOWN_KEYWORD")
    @JvmField val UNKNOWN_OPTION = MtlTokenType(debugName = "UNKNOWN_OPTION")
    @JvmField val UNKNOWN_REFLECTION_TYPE_OPTION = MtlTokenType(debugName = "UNKNOWN_REFLECTION_TYPE_OPTION")
    @JvmField val UNKNOWN_REFLECTION_TYPE = MtlTokenType(debugName = "UNKNOWN_REFLECTION_TYPE")
    @JvmField val UNKNOWN_FLAG = MtlTokenType(debugName = "UNKNOWN_FLAG")
    @JvmField val UNKNOWN_ILLUMINATION_VALUE = MtlTokenType(debugName = "UNKNOWN_ILLUMINATION_VALUE")
    @JvmField val UNKNOWN_SCALAR_CHANNEL = MtlTokenType(debugName = "UNKNOWN_SCALAR_CHANNEL")
}
