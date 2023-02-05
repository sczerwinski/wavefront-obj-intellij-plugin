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

package it.czerwinski.intellij.wavefront.settings.ui

import com.intellij.ui.SimpleListCellRenderer
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.PBREnvironment

@Suppress("FunctionName")
fun PBREnvironmentListCellRenderer(): SimpleListCellRenderer<PBREnvironment> =
    SimpleListCellRenderer.create("") { value ->
        when (value) {
            PBREnvironment.INTERIOR ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.interior")
            PBREnvironment.OFFICE ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.office")
            PBREnvironment.GARAGE ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.garage")
            PBREnvironment.GARDEN ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.garden")
            PBREnvironment.WOODS ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.woods")
            PBREnvironment.PARK ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.park")
            PBREnvironment.BEACH ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.beach")
            PBREnvironment.NIGHT ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.night")
            PBREnvironment.CITY_SQUARE ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.city_square")
            PBREnvironment.ALLEY ->
                WavefrontObjBundle.message("settings.editor.fileTypes.obj.preview.pbrEnvironment.alley")
            else ->
                null
        }
    }
