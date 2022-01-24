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

package it.czerwinski.intellij.wavefront.editor.gl

import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.FaceCullingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.textures.Texture
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import graphics.glimpse.types.normalize
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.ModelMeshesManager
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.PBRShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.SolidShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.TexturedWireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.textures.TextureResources
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

/**
 * 3D preview scene for an OBJ file.
 */
class ObjPreviewScene(
    profile: GLProfile,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : PreviewScene(profile, animatorControl, errorLog) {

    var model: GLModel? = null
        set(value) {
            field = value
            modelChanged.set(true)
            value?.materials?.forEach { material ->
                material?.ambientColorMap?.let(::prepareTexture)
                material?.diffuseColorMap?.let(::prepareTexture)
                material?.specularColorMap?.let(::prepareTexture)
                material?.emissionColorMap?.let(::prepareTexture)
                material?.specularExponentMap?.let(::prepareTexture)
                material?.roughnessMap?.let(::prepareTexture)
                material?.metalnessMap?.let(::prepareTexture)
                material?.normalMap?.let(::prepareTexture)
                material?.bumpMap?.let(::prepareTexture)
                material?.displacementMap?.let(::prepareTexture)
            }
            requestRender()
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

    private lateinit var fallbackTexture: Texture
    private lateinit var fallbackNormalmap: Texture

    private val modelMeshesManager = ModelMeshesManager()

    private fun recalculateCamera(newCameraModel: GLCameraModel) {
        with(newCameraModel) {
            camera = TargetCamera(upVector.modelMatrix.toMat3() * Vec3(x, y, z), Vec3.nullVector, Vec3.unitZ)
            lens = PerspectiveLens(fovY(aspect), aspect, near, far)
        }
        requestRender()
    }

    override fun initialize(gl: GlimpseAdapter) {
        super.initialize(gl)
        createFallbackTextures(gl)
        createModelMeshes(gl)
    }

    private fun createFallbackTextures(gl: GlimpseAdapter) {
        try {
            val textures = Texture.Builder.getInstance(gl)
                .addTexture(TextureResources.fallbackTextureImageSource)
                .addTexture(TextureResources.fallbackNormalmapImageSource)
                .generateMipmaps()
                .build()
            fallbackTexture = textures.first()
            fallbackNormalmap = textures.last()
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createFallbackTextures.error"),
                expected
            )
        }
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
        gl.glCullFace(FaceCullingMode.BACK)
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
                color = PreviewColors.asVec4(PreviewColors.COLOR_FACE)
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
                color = PreviewColors.asVec3(PreviewColors.COLOR_FACE)
            ),
            facesMesh
        )
    }

    @Suppress("ComplexMethod")
    private fun renderFacesMaterial(gl: GlimpseAdapter, facesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)
        val ambientTexture = material?.ambientColorMap?.getTexture(gl)
        val diffuseTexture = material?.diffuseColorMap?.getTexture(gl)
        val emissionTexture = material?.emissionColorMap?.getTexture(gl)

        val roughnessTexture = material?.roughnessMap?.getTexture(gl)
        val metalnessTexture = material?.metalnessMap?.getTexture(gl)
        val normalTexture = material?.normalMap?.getTexture(gl)
        val specularColorTexture = material?.specularColorMap?.getTexture(gl)
        val specularExponentTexture = material?.specularExponentMap?.getTexture(gl)
        val bumpTexture = material?.bumpMap?.getTexture(gl)

        val emissionColor = material?.emissionColor ?: if (emissionTexture != null) Color.WHITE else Color.BLACK

        programExecutorsManager.renderMaterial(
            gl,
            MaterialShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = upVector.modelMatrix,
                normalMatrix = upVector.normalMatrix.toMat3(),
                cameraPosition = camera.eye,
                ambientColor = Vec3(color = material?.let { it.ambientColor ?: it.diffuseColor } ?: Color.WHITE),
                diffuseColor = Vec3(color = material?.diffuseColor ?: Color.WHITE),
                specularColor = Vec3(color = material?.specularColor ?: Color.WHITE),
                emissionColor = Vec3(color = emissionColor),
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
                displacementTexture = material?.displacementMap?.getTexture(gl) ?: fallbackTexture,
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                cropTexture = cropTextures
            ),
            facesMesh
        )
    }

    @Suppress("ComplexMethod")
    private fun renderFacesPBR(gl: GlimpseAdapter, facesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)

        val diffuseTexture = material?.diffuseColorMap?.getTexture(gl)
        val emissionTexture = material?.emissionColorMap?.getTexture(gl)
        val roughnessTexture = material?.roughnessMap?.getTexture(gl)
        val metalnessTexture = material?.metalnessMap?.getTexture(gl)
        val normalTexture = material?.normalMap?.getTexture(gl)
        val specularColorTexture = material?.specularColorMap?.getTexture(gl)
        val specularExponentTexture = material?.specularExponentMap?.getTexture(gl)
        val bumpTexture = material?.bumpMap?.getTexture(gl)

        val emissionColor = material?.emissionColor ?: if (emissionTexture != null) Color.WHITE else Color.BLACK

        val cameraDirection = normalize(camera.eye)
        val cameraUpVector = normalize(vector = Vec3.unitZ - cameraDirection * cameraDirection.z)
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
                diffuseColor = Vec3(color = material?.diffuseColor ?: Color.WHITE),
                emissionColor = Vec3(color = emissionColor),
                roughness = material?.roughness ?: 1f,
                metalness = material?.metalness ?: 1f,
                diffuseTexture = diffuseTexture ?: fallbackTexture,
                emissionTexture = emissionTexture ?: fallbackTexture,
                roughnessTexture = roughnessTexture ?: specularExponentTexture ?: fallbackTexture,
                metalnessTexture = metalnessTexture ?: specularColorTexture ?: fallbackTexture,
                normalmapTexture = normalTexture ?: bumpTexture ?: fallbackNormalmap,
                displacementTexture = material?.displacementMap?.getTexture(gl) ?: fallbackTexture,
                displacementGain = material?.displacementGain ?: 1f,
                displacementQuality = config.displacementQuality,
                reflectionTexture = environmentTexture,
                radianceTexture = radianceTexture,
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
                color = PreviewColors.asVec4(PreviewColors.COLOR_LINE)
            ),
            linesMesh
        )
    }

    private fun renderLinesTextured(gl: GlimpseAdapter, linesMesh: Mesh, index: Int) {
        val material: MtlMaterial? = model?.materials?.getOrNull(index)
        val ambientTexture = material?.ambientColorMap?.getTexture(gl)
        val diffuseTexture = material?.diffuseColorMap?.getTexture(gl)

        gl.glLineWidth(config.lineWidth)
        programExecutorsManager.renderTexturedWireframe(
            gl,
            TexturedWireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * upVector.modelMatrix,
                color = Vec4(color = material?.diffuseColor ?: material?.ambientColor ?: Color.WHITE),
                texture = diffuseTexture ?: ambientTexture ?: fallbackTexture
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
                color = PreviewColors.asVec4(PreviewColors.COLOR_POINT)
            ),
            pointsMesh
        )
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
        fallbackTexture.dispose(gl)
        fallbackNormalmap.dispose(gl)
    }

    override fun onDestroyError(gl: GlimpseAdapter, expected: Throwable) = Unit

    companion object {
        private const val CAMERA_DISTANCE = 10f
    }
}
