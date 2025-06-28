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

import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import it.czerwinski.intellij.wavefront.actions.gl.BaseMapRenderer
import it.czerwinski.intellij.wavefront.actions.gl.DiffuseIrradianceRenderer
import java.awt.image.BufferedImage

class DiffuseIrradianceBufferedImageProvider(
    project: Project,
    environmentTextureFilename: String,
    relativeTo: VirtualFile? = null
) : BaseBufferedImageProvider(project, environmentTextureFilename, relativeTo) {

    override fun createRenderer(inputImage: BufferedImage, outputWidth: Int, outputHeight: Int): BaseMapRenderer<*> =
        DiffuseIrradianceRenderer(inputImage, SAMPLES, outputWidth, outputHeight)

    companion object {
        private const val SAMPLES = 100
    }
}
