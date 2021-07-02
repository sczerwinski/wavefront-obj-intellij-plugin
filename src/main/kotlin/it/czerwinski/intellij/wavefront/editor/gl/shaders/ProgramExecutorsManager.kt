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

package it.czerwinski.intellij.wavefront.editor.gl.shaders

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.ProgramExecutor
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod

/**
 * GL program executors manager.
 */
class ProgramExecutorsManager(private val errorLog: ErrorLog) {

    private lateinit var wireframeProgram: Program
    private lateinit var wireframeShaderProgramExecutor: WireframeShaderProgramExecutor

    private lateinit var texturedWireframeProgram: Program
    private lateinit var texturedWireframeShaderProgramExecutor: TexturedWireframeShaderProgramExecutor

    private lateinit var solidProgram: Program
    private lateinit var solidShaderProgramExecutor: SolidShaderProgramExecutor

    private lateinit var materialProgram: Program
    private lateinit var materialShaderProgramExecutor: MaterialShaderProgramExecutor

    /**
     * Initializes all shaders and program executors.
     */
    fun initialize(gl: GlimpseAdapter) {
        try {
            createPrograms(gl)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createShaders.error"),
                expected
            )
        }
    }

    private fun createPrograms(gl: GlimpseAdapter) {
        val shaderFactory = Shader.Factory.newInstance(gl)
        val programBuilder = Program.Builder.newInstance(gl)

        wireframeProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.WIREFRAME)
        texturedWireframeProgram = programBuilder
            .withVertexShader(shaderFactory.createTexturedWireframeShader(ShaderType.VERTEX_SHADER))
            .withFragmentShader(shaderFactory.createTexturedWireframeShader(ShaderType.FRAGMENT_SHADER))
            .build()
        solidProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.SOLID)
        materialProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.MATERIAL)

        wireframeShaderProgramExecutor = WireframeShaderProgramExecutor(wireframeProgram)
        texturedWireframeShaderProgramExecutor = TexturedWireframeShaderProgramExecutor(texturedWireframeProgram)
        solidShaderProgramExecutor = SolidShaderProgramExecutor(solidProgram)
        materialShaderProgramExecutor = MaterialShaderProgramExecutor(materialProgram)
    }

    private fun createProgram(
        shaderFactory: Shader.Factory,
        programBuilder: Program.Builder,
        shadingMethod: ShadingMethod
    ): Program = programBuilder
        .withVertexShader(shaderFactory.createShader(shadingMethod, ShaderType.VERTEX_SHADER))
        .withFragmentShader(shaderFactory.createShader(shadingMethod, ShaderType.FRAGMENT_SHADER))
        .build()

    private fun Shader.Factory.createShader(shadingMethod: ShadingMethod, shaderType: ShaderType): Shader =
        createShader(type = shaderType, source = ShaderResources.getShaderSource(shadingMethod, shaderType))

    private fun Shader.Factory.createTexturedWireframeShader(shaderType: ShaderType): Shader =
        createShader(type = shaderType, source = ShaderResources.getTexturedWireframeShaderSource(shaderType))

    /**
     * Renders [meshes] using wireframe shader with given [params].
     */
    fun renderWireframe(gl: GlimpseAdapter, params: WireframeShader, vararg meshes: Mesh) {
        if (::wireframeShaderProgramExecutor.isInitialized) {
            wireframeShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    private fun <T> ProgramExecutor<T>.render(gl: GlimpseAdapter, params: T, meshes: Array<out Mesh>) {
        useProgram(gl)
        applyParams(gl, params)
        for (mesh in meshes) {
            drawMesh(gl, mesh)
        }
    }

    /**
     * Renders [meshes] using textured wireframe shader with given [params].
     */
    fun renderTexturedWireframe(gl: GlimpseAdapter, params: TexturedWireframeShader, vararg meshes: Mesh) {
        if (::texturedWireframeShaderProgramExecutor.isInitialized) {
            texturedWireframeShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Renders [meshes] using solid shader with given [params].
     */
    fun renderSolid(gl: GlimpseAdapter, params: SolidShader, vararg meshes: Mesh) {
        if (::solidShaderProgramExecutor.isInitialized) {
            solidShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Renders [meshes] using material shader with given [params].
     */
    fun renderMaterial(gl: GlimpseAdapter, params: MaterialShader, vararg meshes: Mesh) {
        if (::materialShaderProgramExecutor.isInitialized) {
            materialShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Disposes all previously created programs.
     */
    fun dispose(gl: GlimpseAdapter) {
        wireframeShaderProgramExecutor.dispose()
        solidShaderProgramExecutor.dispose()
        materialShaderProgramExecutor.dispose()
        wireframeProgram.dispose(gl)
        solidProgram.dispose(gl)
        materialProgram.dispose(gl)
    }
}
