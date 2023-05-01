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
import graphics.glimpse.types.toVec3
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.MtlPreviewMeshesManager
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.PBRShader
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.MaterialPreviewMesh
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterialElement
import it.czerwinski.intellij.wavefront.lang.psi.toInt
import java.awt.Color

/**
 * 3D preview scene for a material.
 */
@Suppress("UseJBColor")
class MtlPreviewScene(
    profile: GLProfile,
    parent: Disposable,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : PreviewScene(profile, parent, animatorControl, errorLog) {

    var material: MtlMaterialElement? = null
        set(value) {
            field = value
            if (value != null) {
                notifyLoading(loading = true)
                BackgroundTaskUtil.executeOnPooledThread(parent) {
                    materialTexturesProvider = MaterialTexturesProvider(value)
                    materialTexturesProvider.prepare()
                    requestRender()
                    notifyLoading(loading = false)
                }
            }
        }

    override val modelSize: Float = MODEL_SIZE

    var cameraModel: GLCameraModel? = null
        set(value) {
            field = value
            value?.let { recalculateCamera(it) }
        }

    var previewMesh: MaterialPreviewMesh = MaterialPreviewMesh.DEFAULT
        set(value) {
            field = value
            requestRender()
        }

    var shadingMethod: ShadingMethod = ShadingMethod.MTL_DEFAULT
        set(value) {
            field = value
            requestRender()
        }

    var cropTextures: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    override val upVector: UpVector = cameraModel?.upVector ?: UpVector.DEFAULT

    override val showEnvironment: Boolean get() = shadingMethod == ShadingMethod.PBR

    private val meshesManager = MtlPreviewMeshesManager()

    private val mesh: Mesh? get() = meshesManager.getMesh(previewMesh)

    private lateinit var materialTexturesProvider: MaterialTexturesProvider

    override var materialEnvironmentTexture: Texture? = null
        private set

    override var materialIrradianceTexture: Texture? = null
        private set

    override var materialReflectionTextures: List<Texture> = emptyList()
        private set

    init {
        try {
            meshesManager.prepare()
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.mtl.material.preview.loadMeshes.error"),
                expected
            )
        }
    }

    private fun recalculateCamera(newCameraModel: GLCameraModel) {
        with(newCameraModel) {
            camera = TargetCamera(upVector.modelMatrix.toMat3() * Vec3(x, y, z), Vec3.nullVector(), Vec3.unitZ())
            lens = PerspectiveLens(fovY(aspect), aspect, near, far)
        }
        requestRender()
    }

    override fun initialize(gl: GlimpseAdapter) {
        super.initialize(gl)
        createMeshes(gl)
    }

    private fun createMeshes(gl: GlimpseAdapter) {
        try {
            meshesManager.initialize(gl)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.mtl.material.preview.createMeshes.error"),
                expected
            )
        }
    }

    override fun afterResize(gl: GlimpseAdapter) {
        cameraModel?.let { recalculateCamera(it) }
    }

    override fun onResizeError(gl: GlimpseAdapter, error: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.mtl.material.preview.onResize.error"),
            error
        )
    }

    override fun renderModel(gl: GlimpseAdapter) {
        val facesMesh = mesh ?: return
        when (shadingMethod) {
            ShadingMethod.MATERIAL -> renderFacesMaterial(gl, facesMesh)
            ShadingMethod.PBR -> renderFacesPBR(gl, facesMesh)
            else -> error("Material preview requires MATERIAL or PBR shading method")
        }
    }

    @Suppress("ComplexMethod")
    private fun renderFacesMaterial(gl: GlimpseAdapter, facesMesh: Mesh) {
        val fallbackEmissionColor = (if (materialTexturesProvider.hasEmission) Color.WHITE else Color.BLACK).toVec3()
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor
        val fallbackColor = Color.WHITE.toVec3()
        val alpha = 1f - (material?.transparency ?: 0f)

        programExecutorsManager.renderMaterial(
            gl,
            MaterialShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                ambientColor = material?.ambientColorVector ?: material?.diffuseColorVector ?: fallbackColor,
                diffuseColor = (material?.diffuseColorVector ?: fallbackColor).toVec4(alpha),
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
    private fun renderFacesPBR(gl: GlimpseAdapter, facesMesh: Mesh) {
        val fallbackEmissionColor = (if (materialTexturesProvider.hasEmission) Color.WHITE else Color.BLACK).toVec3()
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor
        val alpha = 1f - (material?.transparency ?: 0f)

        programExecutorsManager.renderPBR(
            gl,
            PBRShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                diffuseColor = (material?.diffuseColorVector ?: Color.WHITE.toVec3()).toVec4(alpha),
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

    override fun prepareEnvironment(gl: GlimpseAdapter) {
        materialEnvironmentTexture = materialTexturesProvider.environmentTexture(gl)
        materialIrradianceTexture = materialTexturesProvider.irradianceTexture(gl)
        materialReflectionTextures = listOf(materialEnvironmentTexture ?: environmentTexture) +
            materialTexturesProvider.reflectionTextures(gl)
    }

    override fun onRenderError(gl: GlimpseAdapter, error: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.mtl.material.preview.onRender.error"),
            error
        )
    }

    override fun dispose(gl: GlimpseAdapter) {
        super.dispose(gl)
        meshesManager.dispose(gl)
    }

    override fun onDestroyError(gl: GlimpseAdapter, expected: Throwable) = Unit

    companion object {
        const val MODEL_SIZE = 1f
    }
}
