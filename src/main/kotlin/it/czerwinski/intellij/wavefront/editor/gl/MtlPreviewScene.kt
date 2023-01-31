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

import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.types.normalize
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
import java.awt.Color

/**
 * 3D preview scene for a material.
 */
@Suppress("UseJBColor")
class MtlPreviewScene(
    profile: GLProfile,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : PreviewScene(profile, animatorControl, errorLog) {

    var material: MtlMaterialElement? = null
        set(value) {
            field = value
            value?.ambientColorMap?.let { prepareTexture(value.project, it) }
            value?.diffuseColorMap?.let { prepareTexture(value.project, it) }
            value?.specularColorMap?.let { prepareTexture(value.project, it) }
            value?.emissionColorMap?.let { prepareTexture(value.project, it) }
            value?.specularExponentMap?.let { prepareTexture(value.project, it) }
            value?.roughnessMap?.let { prepareTexture(value.project, it) }
            value?.metalnessMap?.let { prepareTexture(value.project, it) }
            value?.normalMap?.let { prepareTexture(value.project, it) }
            value?.bumpMap?.let { prepareTexture(value.project, it) }
            value?.displacementMap?.let { prepareTexture(value.project, it) }
            requestRender()
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

    override val upVector: UpVector = UpVector.Z_UP

    override val showEnvironment: Boolean get() = shadingMethod == ShadingMethod.PBR

    private val meshesManager = MtlPreviewMeshesManager()

    private val mesh: Mesh? get() = meshesManager.getMesh(previewMesh)

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
            camera = TargetCamera(
                upVector.modelMatrix.toMat3() * graphics.glimpse.types.Vec3(x, y, z),
                graphics.glimpse.types.Vec3.nullVector,
                graphics.glimpse.types.Vec3.unitZ
            )
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
        val ambientTexture = material?.ambientColorMap?.let { getTexture(gl, it) }
        val diffuseTexture = material?.diffuseColorMap?.let { getTexture(gl, it) }
        val emissionTexture = material?.emissionColorMap?.let { getTexture(gl, it) }

        val roughnessTexture = material?.roughnessMap?.let { getTexture(gl, it) }
        val metalnessTexture = material?.metalnessMap?.let { getTexture(gl, it) }
        val normalTexture = material?.normalMap?.let { getTexture(gl, it) }
        val specularColorTexture = material?.specularColorMap?.let { getTexture(gl, it) }
        val specularExponentTexture = material?.specularExponentMap?.let { getTexture(gl, it) }
        val bumpTexture = material?.bumpMap?.let { getTexture(gl, it) }

        val fallbackEmissionColor =
            graphics.glimpse.types.Vec3(if (emissionTexture != null) Color.WHITE else Color.BLACK)
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor
        val fallbackColor = graphics.glimpse.types.Vec3(color = Color.WHITE)

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
                ambientTexture = ambientTexture ?: diffuseTexture ?: fallbackTexture,
                diffuseTexture = diffuseTexture ?: fallbackTexture,
                specularTexture = specularColorTexture ?: metalnessTexture ?: fallbackTexture,
                emissionTexture = emissionTexture ?: fallbackTexture,
                specularExponentTexture = specularExponentTexture ?: roughnessTexture ?: fallbackTexture,
                specularExponentBase = material?.specularExponentBase ?: 0f,
                specularExponentGain = material?.specularExponentGain ?: 1f,
                normalmapTexture = normalTexture ?: bumpTexture ?: fallbackNormalmap,
                normalmapMultiplier = material?.bumpMapMultiplier ?: 1f,
                displacementTexture = material?.displacementMap?.let { getTexture(gl, it) } ?: fallbackTexture,
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                cropTexture = cropTextures
            ),
            facesMesh
        )
    }

    @Suppress("ComplexMethod")
    private fun renderFacesPBR(gl: GlimpseAdapter, facesMesh: Mesh) {
        val diffuseTexture = material?.diffuseColorMap?.let { getTexture(gl, it) }
        val emissionTexture = material?.emissionColorMap?.let { getTexture(gl, it) }
        val roughnessTexture = material?.roughnessMap?.let { getTexture(gl, it) }
        val metalnessTexture = material?.metalnessMap?.let { getTexture(gl, it) }
        val normalTexture = material?.normalMap?.let { getTexture(gl, it) }
        val specularColorTexture = material?.specularColorMap?.let { getTexture(gl, it) }
        val specularExponentTexture = material?.specularExponentMap?.let { getTexture(gl, it) }
        val bumpTexture = material?.bumpMap?.let { getTexture(gl, it) }

        val fallbackEmissionColor =
            graphics.glimpse.types.Vec3(if (emissionTexture != null) Color.WHITE else Color.BLACK)
        val emissionColor = material?.emissionColorVector ?: fallbackEmissionColor

        val cameraDirection = normalize(camera.eye)
        val cameraUpVector = normalize(vector = graphics.glimpse.types.Vec3.unitZ - cameraDirection * cameraDirection.z)
        val cameraLeftVector = normalize(vector = cameraDirection cross cameraUpVector)
        val lightPosition = (cameraDirection + cameraUpVector + cameraLeftVector) * CAMERA_DISTANCE

        programExecutorsManager.renderPBR(
            gl,
            PBRShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                lightPosition = lightPosition,
                diffuseColor = material?.diffuseColorVector ?: graphics.glimpse.types.Vec3(color = Color.WHITE),
                emissionColor = emissionColor,
                roughness = material?.roughness ?: 1f,
                metalness = material?.metalness ?: 1f,
                diffuseTexture = diffuseTexture ?: fallbackTexture,
                emissionTexture = emissionTexture ?: fallbackTexture,
                roughnessTexture = roughnessTexture ?: specularExponentTexture ?: fallbackTexture,
                metalnessTexture = metalnessTexture ?: specularColorTexture ?: fallbackTexture,
                normalmapTexture = normalTexture ?: bumpTexture ?: fallbackNormalmap,
                displacementTexture = material?.displacementMap?.let { getTexture(gl, it) } ?: fallbackTexture,
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                environmentTexture = environmentTexture,
                irradianceTexture = irradianceTexture,
                reflectionTextures = reflectionTextureLevels,
                brdfTexture = brdfTexture,
                cropTexture = cropTextures
            ),
            facesMesh
        )
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
        private const val CAMERA_DISTANCE = 10f
    }
}
