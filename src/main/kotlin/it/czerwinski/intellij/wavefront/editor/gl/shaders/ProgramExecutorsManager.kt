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

package it.czerwinski.intellij.wavefront.editor.gl.shaders

import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.ProgramExecutor
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.ShaderQuality
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

    private lateinit var pbrProgram: Program
    private lateinit var pbrShaderProgramExecutor: PBRShaderProgramExecutor

    private lateinit var environmentProgram: Program
    private lateinit var environmentShaderProgramExecutor: EnvironmentShaderProgramExecutor

    private lateinit var textProgram: Program
    private lateinit var textShaderProgramExecutor: TextShaderProgramExecutor

    /**
     * Initializes all shaders and program executors.
     */
    fun initialize(gl: GlimpseAdapter, shaderQuality: ShaderQuality) {
        try {
            createPrograms(gl, shaderQuality)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.createShaders.error"),
                expected
            )
        }
    }

    private fun createPrograms(gl: GlimpseAdapter, shaderQuality: ShaderQuality) {
        val shaderFactory = Shader.Factory.newInstance(gl)
        val programBuilder = Program.Builder.newInstance(gl)

        wireframeProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.WIREFRAME, shaderQuality)
        texturedWireframeProgram =
            createNamedProgram(shaderFactory, programBuilder, TEXTURED_WIREFRAME_SHADER, shaderQuality)
        solidProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.SOLID, shaderQuality)
        materialProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.MATERIAL, shaderQuality)
        pbrProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.PBR, shaderQuality)
        environmentProgram = createNamedProgram(shaderFactory, programBuilder, ENVIRONMENT_SHADER, shaderQuality)
        textProgram = createNamedProgram(shaderFactory, programBuilder, TEXT_SHADER, shaderQuality)

        wireframeShaderProgramExecutor = WireframeShaderProgramExecutor(wireframeProgram)
        texturedWireframeShaderProgramExecutor = TexturedWireframeShaderProgramExecutor(texturedWireframeProgram)
        solidShaderProgramExecutor = SolidShaderProgramExecutor(solidProgram)
        materialShaderProgramExecutor = MaterialShaderProgramExecutor(materialProgram)
        pbrShaderProgramExecutor = PBRShaderProgramExecutor(pbrProgram)
        environmentShaderProgramExecutor = EnvironmentShaderProgramExecutor(environmentProgram)
        textShaderProgramExecutor = TextShaderProgramExecutor(textProgram)
    }

    private fun createProgram(
        shaderFactory: Shader.Factory,
        programBuilder: Program.Builder,
        shadingMethod: ShadingMethod,
        shaderQuality: ShaderQuality
    ): Program = programBuilder
        .withVertexShader(shaderFactory.createShader(shadingMethod, ShaderType.VERTEX_SHADER, shaderQuality))
        .withFragmentShader(shaderFactory.createShader(shadingMethod, ShaderType.FRAGMENT_SHADER, shaderQuality))
        .build()

    private fun createNamedProgram(
        shaderFactory: Shader.Factory,
        programBuilder: Program.Builder,
        shaderName: String,
        shaderQuality: ShaderQuality
    ): Program = programBuilder
        .withVertexShader(shaderFactory.createNamedShader(shaderName, ShaderType.VERTEX_SHADER, shaderQuality))
        .withFragmentShader(shaderFactory.createNamedShader(shaderName, ShaderType.FRAGMENT_SHADER, shaderQuality))
        .build()

    private fun Shader.Factory.createShader(
        shadingMethod: ShadingMethod,
        shaderType: ShaderType,
        shaderQuality: ShaderQuality
    ): Shader = createShader(
        type = shaderType,
        source = ShaderResources.getShaderSource(shadingMethod, shaderType, shaderQuality)
    )

    private fun Shader.Factory.createNamedShader(
        name: String,
        shaderType: ShaderType,
        shaderQuality: ShaderQuality
    ): Shader = createShader(
        type = shaderType,
        source = ShaderResources.getNamedShaderSource(name, shaderType, shaderQuality)
    )

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
     * Renders [meshes] using PBR shader with given [params].
     */
    fun renderPBR(gl: GlimpseAdapter, params: PBRShader, vararg meshes: Mesh) {
        if (::pbrShaderProgramExecutor.isInitialized) {
            pbrShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Renders [meshes] using environment shader with given [params].
     */
    fun renderEnvironment(gl: GlimpseAdapter, params: EnvironmentShader, vararg meshes: Mesh) {
        if (::environmentShaderProgramExecutor.isInitialized) {
            environmentShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Renders [meshes] using text shader with given [params].
     */
    fun renderText(gl: GlimpseAdapter, params: TextShader, vararg meshes: Mesh) {
        if (::textShaderProgramExecutor.isInitialized) {
            textShaderProgramExecutor.render(gl, params, meshes)
        }
    }

    /**
     * Disposes all previously created programs.
     */
    fun dispose(gl: GlimpseAdapter) {
        wireframeShaderProgramExecutor.dispose(gl)
        texturedWireframeShaderProgramExecutor.dispose(gl)
        solidShaderProgramExecutor.dispose(gl)
        materialShaderProgramExecutor.dispose(gl)
        pbrShaderProgramExecutor.dispose(gl)
        environmentShaderProgramExecutor.dispose(gl)
        textShaderProgramExecutor.dispose(gl)
    }

    companion object {
        private const val TEXTURED_WIREFRAME_SHADER = "textured_wireframe"
        private const val ENVIRONMENT_SHADER = "environment"
        private const val TEXT_SHADER = "text"
    }
}
