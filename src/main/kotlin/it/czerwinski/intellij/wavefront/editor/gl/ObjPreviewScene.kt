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

import com.intellij.openapi.Disposable
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.textures.Texture
import graphics.glimpse.types.Vec3
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.ModelMeshesManager
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.PBRShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.SolidShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.TexturedWireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import it.czerwinski.intellij.wavefront.lang.psi.toInt
import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 3D preview scene for an OBJ file.
 */
@Suppress("UseJBColor")
class ObjPreviewScene(
    profile: GLProfile,
    parent: Disposable,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : PreviewScene(profile, parent, animatorControl, errorLog) {

    var model: GLModel? = null
        set(value) {
            field = value
            modelChanged.set(true)
            notifyLoading(loading = true)
            BackgroundTaskUtil.executeOnPooledThread(parent) {
                materialTexturesProviders = model?.materials
                    ?.mapNotNull { material ->
                        val name = material?.getName().orEmpty()
                        val provider = material?.let(::MaterialTexturesProvider)
                        if (provider == null) null else name to provider
                    }
                    ?.toMap()
                    .orEmpty()
                for ((_, provider) in materialTexturesProviders) {
                    provider.prepare()
                }
                requestRender()
                notifyLoading(loading = false)
            }
        }

    private val modelChanged: AtomicBoolean = AtomicBoolean(false)

    override val modelSize: Float? get() = model?.size

    var cameraModel: GLCameraModel? = null
        set(value) {
            field = value
            value?.let { recalculateCamera(it) }
        }

    var shadingMethod: ShadingMethod = ShadingMethod.DEFAULT
        set(value) {
            field = value
            modelChanged.set(true)
            requestRender()
        }

    var cropTextures: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    override val upVector: UpVector get() = cameraModel?.upVector ?: UpVector.DEFAULT

    override val showEnvironment: Boolean get() = shadingMethod == ShadingMethod.PBR

    private val modelMeshesManager = ModelMeshesManager()

    private lateinit var materialTexturesProviders: Map<String, MaterialTexturesProvider>

    override var materialEnvironmentTexture: Texture? = null
        private set

    override var materialIrradianceTexture: Texture? = null
        private set

    override var materialReflectionTextures: List<Texture> = emptyList()
        private set

    private fun recalculateCamera(newCameraModel: GLCameraModel) {
        with(newCameraModel) {
            camera = TargetCamera(upVector.modelMatrix.toMat3() * Vec3(x, y, z), Vec3.nullVector, Vec3.unitZ)
            lens = PerspectiveLens(fovY(aspect), aspect, near, far)
        }
        requestRender()
    }

    override fun initialize(gl: GlimpseAdapter) {
        super.initialize(gl)
        createModelMeshes(gl)
    }

    private fun createModelMeshes(gl: GlimpseAdapter) {
        try {
            modelMeshesManager.initialize(gl, model ?: return, shadingMethod)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createModelMeshes.error"),
                expected
            )
        }
    }

    override fun afterResize(gl: GlimpseAdapter) {
        cameraModel?.let { recalculateCamera(it) }
    }

    override fun onResizeError(gl: GlimpseAdapter, error: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.obj.preview.onResize.error"),
            error
        )
    }

    override fun renderModel(gl: GlimpseAdapter) {
        if (modelChanged.getAndSet(false)) {
            createModelMeshes(gl)
        }
        modelMeshesManager.facesMeshes.forEachIndexed { index, mesh -> renderFaces(gl, mesh, shadingMethod, index) }
        modelMeshesManager.linesMeshes.forEachIndexed { index, mesh -> renderLines(gl, mesh, index) }
        modelMeshesManager.pointsMeshes.forEach { renderPoints(gl, it) }
    }

    private fun renderFaces(gl: GlimpseAdapter, facesMesh: Mesh, shadingMethod: ShadingMethod, index: Int) {
        when (shadingMethod) {
            ShadingMethod.WIREFRAME -> renderFacesWireframe(gl, facesMesh)
            ShadingMethod.SOLID -> renderFacesSolid(gl, facesMesh)
            ShadingMethod.MATERIAL -> renderFacesMaterial(gl, facesMesh, index)
            ShadingMethod.PBR -> renderFacesPBR(gl, facesMesh, index)
        }
    }

    private fun renderFacesWireframe(gl: GlimpseAdapter, facesMesh: Mesh) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * upVector.modelMatrix,
                color = Vec4(PreviewColors.COLOR_FACE)
            ),
            facesMesh
        )
    }

    private fun renderFacesSolid(gl: GlimpseAdapter, facesMesh: Mesh) {
        programExecutorsManager.renderSolid(
            gl,
            SolidShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                color = Vec3(PreviewColors.COLOR_FACE)
            ),
            facesMesh
        )
    }

    @Suppress("ComplexMethod")
    private fun renderFacesMaterial(gl: GlimpseAdapter, facesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)
        val materialName = material?.getName().orEmpty()
        val materialTexturesProvider = materialTexturesProviders[materialName] ?: MaterialTexturesProvider()

        val fallbackEmissionColor = Vec3(if (materialTexturesProvider.hasEmission) Color.WHITE else Color.BLACK)
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor
        val fallbackColor = Vec3(color = Color.WHITE)

        programExecutorsManager.renderMaterial(
            gl,
            MaterialShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                ambientColor = material?.ambientColorVector ?: material?.diffuseColorVector ?: fallbackColor,
                diffuseColor = material?.diffuseColorVector ?: fallbackColor,
                specularColor = material?.specularColorVector ?: fallbackColor,
                emissionColor = emissionColor,
                specularExponent = material?.specularExponent ?: 1f,
                ambientTexture = materialTexturesProvider.ambientTexture(gl),
                diffuseTexture = materialTexturesProvider.diffuseTexture(gl),
                specularTexture = materialTexturesProvider.specularTexture(gl),
                emissionTexture = materialTexturesProvider.emissionTexture(gl),
                specularExponentTexture = materialTexturesProvider.specularExponentTexture(gl),
                specularExponentBase = material?.specularExponentBase ?: 0f,
                specularExponentGain = material?.specularExponentGain ?: 1f,
                specularExponentChannel = material?.specularExponentChannel.toInt(),
                normalmapTexture = materialTexturesProvider.normalmapTexture(gl),
                normalmapMultiplier = material?.bumpMapMultiplier ?: 1f,
                displacementTexture = materialTexturesProvider.displacementTexture(gl),
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                displacementChannel = material?.displacementChannel.toInt(),
                cropTexture = cropTextures
            ),
            facesMesh
        )
    }

    @Suppress("ComplexMethod")
    private fun renderFacesPBR(gl: GlimpseAdapter, facesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)
        val materialName = material?.getName().orEmpty()
        val materialTexturesProvider = materialTexturesProviders[materialName] ?: MaterialTexturesProvider()

        val fallbackEmissionColor = Vec3(if (materialTexturesProvider.hasEmission) Color.WHITE else Color.BLACK)
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor

        programExecutorsManager.renderPBR(
            gl,
            PBRShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                diffuseColor = material?.diffuseColorVector ?: Vec3(color = Color.WHITE),
                emissionColor = emissionColor,
                roughness = material?.roughness ?: 1f,
                metalness = material?.metalness ?: 1f,
                diffuseTexture = materialTexturesProvider.diffuseTexture(gl),
                emissionTexture = materialTexturesProvider.emissionTexture(gl),
                roughnessTexture = materialTexturesProvider.roughnessTexture(gl),
                roughnessChannel = material?.roughnessChannel.toInt(),
                metalnessTexture = materialTexturesProvider.metalnessTexture(gl),
                metalnessChannel = material?.metalnessChannel.toInt(),
                normalmapTexture = materialTexturesProvider.normalmapTexture(gl),
                displacementTexture = materialTexturesProvider.displacementTexture(gl),
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                displacementChannel = material?.displacementChannel.toInt(),
                irradianceTexture = materialIrradianceTexture ?: irradianceTexture,
                reflectionTextures = materialReflectionTextures.takeUnless { it.isEmpty() } ?: reflectionTextureLevels,
                brdfTexture = brdfTexture,
                cropTexture = cropTextures
            ),
            facesMesh
        )
    }

    private fun renderLines(gl: GlimpseAdapter, linesMesh: Mesh, index: Int) {
        when (shadingMethod) {
            ShadingMethod.WIREFRAME,
            ShadingMethod.SOLID -> renderLinesWireframe(gl, linesMesh)

            ShadingMethod.MATERIAL,
            ShadingMethod.PBR -> renderLinesTextured(gl, linesMesh, index)
        }
    }

    private fun renderLinesWireframe(gl: GlimpseAdapter, linesMesh: Mesh) {
        gl.glLineWidth(config.lineWidth)
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * upVector.modelMatrix,
                color = Vec4(PreviewColors.COLOR_LINE)
            ),
            linesMesh
        )
    }

    private fun renderLinesTextured(gl: GlimpseAdapter, linesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)
        val materialName = material?.getName().orEmpty()
        val materialTexturesProvider = materialTexturesProviders[materialName] ?: MaterialTexturesProvider()

        val color = material?.diffuseColorVector ?: material?.ambientColorVector ?: Vec3(color = Color.WHITE)

        gl.glLineWidth(config.lineWidth)
        programExecutorsManager.renderTexturedWireframe(
            gl,
            TexturedWireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * upVector.modelMatrix,
                color = color.toVec4(w = 1f),
                texture = materialTexturesProvider.diffuseTexture(gl)
            ),
            linesMesh
        )
    }

    private fun renderPoints(gl: GlimpseAdapter, pointsMesh: Mesh) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * upVector.modelMatrix,
                pointSize = config.pointSize,
                color = Vec4(PreviewColors.COLOR_POINT)
            ),
            pointsMesh
        )
    }

    override fun prepareEnvironment(gl: GlimpseAdapter) {
        materialEnvironmentTexture = materialTexturesProviders.values.asSequence()
            .filter { materialTexturesProvider -> materialTexturesProvider.hasEnvironment }
            .map { materialTexturesProvider -> materialTexturesProvider.environmentTexture(gl) }
            .firstOrNull()
        materialIrradianceTexture = materialTexturesProviders.values.asSequence()
            .filter { materialTexturesProvider -> materialTexturesProvider.hasEnvironment }
            .map { materialTexturesProvider -> materialTexturesProvider.irradianceTexture(gl) }
            .firstOrNull()
        materialReflectionTextures = materialTexturesProviders.values.asSequence()
            .filter { materialTexturesProvider -> materialTexturesProvider.hasEnvironment }
            .map { materialTexturesProvider ->
                listOf(materialEnvironmentTexture ?: environmentTexture) +
                    materialTexturesProvider.reflectionTextures(gl)
            }
            .firstOrNull()
            .orEmpty()
    }

    override fun onRenderError(gl: GlimpseAdapter, error: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.obj.preview.onRender.error"),
            error
        )
    }

    override fun dispose(gl: GlimpseAdapter) {
        super.dispose(gl)
        modelMeshesManager.dispose(gl)
    }

    override fun onDestroyError(gl: GlimpseAdapter, expected: Throwable) = Unit
}
