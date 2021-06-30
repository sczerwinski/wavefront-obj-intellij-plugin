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

import com.intellij.openapi.editor.colors.ColorKey
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.vfs.VirtualFile
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.BlendingFactorFunction
import graphics.glimpse.ClearableBufferType
import graphics.glimpse.DepthTestFunction
import graphics.glimpse.FaceCullingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.GlimpseCallback
import graphics.glimpse.buffers.Buffer
import graphics.glimpse.buffers.toFloatBufferData
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureMagFilter
import graphics.glimpse.textures.TextureMinFilter
import graphics.glimpse.textures.TextureType
import graphics.glimpse.textures.TextureWrap
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat3
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.scale
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.AxisMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.GridMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.LinesMesh
import it.czerwinski.intellij.wavefront.editor.gl.meshes.PointsMesh
import it.czerwinski.intellij.wavefront.editor.gl.meshes.SolidFacesMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.WireframeFacesMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.ProgramExecutorsManager
import it.czerwinski.intellij.wavefront.editor.gl.shaders.SolidShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.textures.TextureResources
import it.czerwinski.intellij.wavefront.editor.gl.textures.TexturesManager
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.lang.psi.MtlMaterial
import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

class ObjPreviewScene(
    private val profile: GLProfile,
    animatorControl: GLAnimatorControl,
    private val errorLog: ErrorLog
) : GlimpseCallback,
    GLAnimatorControl by animatorControl {

    private var width: Int = 1
    private var height: Int = 1
    private var aspect: Float = 1f

    private var model: GLModel? = null
    private val modelChanged: AtomicBoolean = AtomicBoolean(false)

    private var cameraModel: GLCameraModel? = null
    private var camera: TargetCamera = TargetCamera(eye = Vec3.unitX, target = Vec3.nullVector)
    private var lens: PerspectiveLens = PerspectiveLens(Angle.rightAngle, aspect = 1f, near = 1f, far = 2f)

    private var shadingMethod: ShadingMethod = ShadingMethod.DEFAULT

    private val upVector: UpVector
        get() = cameraModel?.upVector ?: UpVector.DEFAULT

    private var showAxes: Boolean = false
    private var showGrid: Boolean = false
    private var config: PreviewSceneConfig = PreviewSceneConfig()

    private val background
        get() = EditorColorsManager.getInstance().globalScheme.defaultBackground

    private val programExecutorsManager = ProgramExecutorsManager(errorLog)

    private val texturesManager = TexturesManager()

    private lateinit var fallbackTexture: Texture
    private lateinit var fallbackNormalmap: Texture

    private val facesMeshes = mutableListOf<Mesh>()
    private val linesMeshes = mutableListOf<Mesh>()
    private val pointsMeshes = mutableListOf<Mesh>()

    private lateinit var gridMesh: Mesh
    private lateinit var fineGridMesh: Mesh
    private lateinit var axisMesh: Mesh
    private lateinit var axisConeMesh: Mesh

    fun updateModel(newModel: GLModel?) {
        model = newModel
        modelChanged.set(true)
        model?.materials?.forEach { material ->
            material?.ambientColorMap?.let(::prepareTexture)
            material?.diffuseColorMap?.let(::prepareTexture)
            material?.specularColorMap?.let(::prepareTexture)
            material?.specularExponentMap?.let(::prepareTexture)
            material?.bumpMap?.let(::prepareTexture)
        }
        requestRender()
    }

    private fun prepareTexture(file: VirtualFile) {
        try {
            texturesManager.prepare(profile, file)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.prepareTexture.error", file.name),
                expected
            )
        }
    }

    fun updateCameraModel(newCameraModel: GLCameraModel) {
        cameraModel = newCameraModel
        recalculateCamera(newCameraModel)
    }

    private fun recalculateCamera(newCameraModel: GLCameraModel) {
        with(newCameraModel) {
            camera = TargetCamera(Vec3(x, y, z), Vec3.nullVector, upVector.vector)
            lens = PerspectiveLens(fovY(aspect), aspect, near, far)
        }
        requestRender()
    }

    fun updateShadingMethod(newShadingMethod: ShadingMethod) {
        shadingMethod = newShadingMethod
        modelChanged.set(true)
        requestRender()
    }

    fun updateAxes(newShowAxes: Boolean) {
        showAxes = newShowAxes
        requestRender()
    }

    fun updateGrid(newShowGrid: Boolean) {
        showGrid = newShowGrid
        requestRender()
    }

    fun updateConfig(newConfig: PreviewSceneConfig) {
        config = newConfig
        requestRender()
    }

    private fun requestRender() {
        if (isStarted && isPaused) resume()
    }

    override fun onCreate(gl: GlimpseAdapter) {
        gl.glClearDepth(depth = 1f)
        gl.glDepthTest(DepthTestFunction.LESS_OR_EQUAL)

        gl.glEnableBlending()
        gl.glBlendingFunction(BlendingFactorFunction.SOURCE_ALPHA, BlendingFactorFunction.ONE_MINUS_SOURCE_ALPHA)

        gl.glEnableLineSmooth()
        gl.glEnableProgramPointSize()

        programExecutorsManager.initialize(gl)
        createFallbackTextures(gl)
        createMeshes(gl)
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

            gl.glTexParameterWrap(TextureType.TEXTURE_2D, TextureWrap.REPEAT, TextureWrap.REPEAT)
            gl.glTexParameterFilter(
                TextureType.TEXTURE_2D,
                TextureMinFilter.LINEAR_MIPMAP_LINEAR,
                TextureMagFilter.LINEAR
            )
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createFallbackTextures.error"),
                expected
            )
        }
    }

    private fun createMeshes(gl: GlimpseAdapter) {
        try {
            gridMesh = GridMeshFactory.createGrid(gl)
            fineGridMesh = GridMeshFactory.createFineGrid(gl)
            axisMesh = AxisMeshFactory.createAxis(gl)
            axisConeMesh = AxisMeshFactory.createAxisCone(gl)
            if (model != null) {
                recreateModelMeshes(gl)
            }
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createMeshes.error"),
                expected
            )
        }
    }

    override fun onResize(gl: GlimpseAdapter, x: Int, y: Int, width: Int, height: Int) {
        try {
            this.width = width
            this.height = height

            if (width == 0 || height == 0) return

            aspect = width.toFloat() / height.toFloat()
            gl.glViewport(width = width, height = height)

            cameraModel?.let { recalculateCamera(it) }
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.onResize.error"),
                expected
            )
        }
    }

    override fun onRender(gl: GlimpseAdapter) {
        try {
            gl.glClearColor(Vec3(background))
            gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)
            gl.glCullFace(FaceCullingMode.BACK)
            renderModel(gl)
            if (showAxes) renderAxes(gl)
            if (showGrid) renderGrid(gl)
            if (isStarted) pause()
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.onRender.error"),
                expected
            )
        }
    }

    private fun renderModel(gl: GlimpseAdapter) {
        if (modelChanged.getAndSet(false)) recreateModelMeshes(gl)
        facesMeshes.forEachIndexed { index, mesh -> renderFaces(gl, mesh, shadingMethod, index) }
        linesMeshes.forEach { renderLines(gl, it) }
        pointsMeshes.forEach { renderPoints(gl, it) }
    }

    private fun recreateModelMeshes(gl: GlimpseAdapter) {
        (facesMeshes + linesMeshes + pointsMeshes).forEach { mesh -> mesh.dispose(gl) }
        facesMeshes.clear()
        linesMeshes.clear()
        pointsMeshes.clear()
        val model = this.model ?: return
        createFacesMeshes(model, gl)
        createLinesMeshes(model, gl)
        createPointsMeshes(model, gl)
    }

    private fun createFacesMeshes(model: GLModel, gl: GlimpseAdapter) {
        val facesMeshFactory = when (shadingMethod) {
            ShadingMethod.WIREFRAME -> WireframeFacesMeshFactory
            ShadingMethod.SOLID -> SolidFacesMeshFactory
            ShadingMethod.MATERIAL -> SolidFacesMeshFactory
        }
        facesMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    facesMeshFactory.create(gl, model, part)
                }
            }
        )
    }

    private fun createLinesMeshes(model: GLModel, gl: GlimpseAdapter) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        linesMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    val linesPositionsData = part.lines.flatMap { line ->
                        line.vertexIndexList.zipWithNext().flatMap { (index1, index2) ->
                            model.vertices[(index1.value ?: 1) - 1].coordinates.map { it ?: 0f } +
                                model.vertices[(index2.value ?: 1) - 1].coordinates.map { it ?: 0f }
                        }
                    }.toFloatBufferData()
                    LinesMesh(
                        vertexCount = part.lines.sumBy { line -> (line.vertexIndexList.size - 1) * 2 },
                        buffers = bufferFactory.createArrayBuffers(linesPositionsData)
                    )
                }
            }
        )
    }

    private fun createPointsMeshes(model: GLModel, gl: GlimpseAdapter) {
        val bufferFactory = Buffer.Factory.newInstance(gl)
        pointsMeshes.addAll(
            model.groupingElements.flatMap { element ->
                element.materialParts.map { part ->
                    val pointsPositionsData = part.points.flatMap { point ->
                        model.vertices[(point.vertexIndex.value ?: 1) - 1].coordinates.map { it ?: 0f }
                    }.toFloatBufferData()
                    PointsMesh(
                        vertexCount = part.points.size,
                        buffers = bufferFactory.createArrayBuffers(pointsPositionsData)
                    )
                }
            }
        )
    }

    private fun renderFaces(gl: GlimpseAdapter, facesMesh: Mesh, shadingMethod: ShadingMethod, index: Int) {
        when (shadingMethod) {
            ShadingMethod.WIREFRAME -> renderFacesWireframe(gl, facesMesh)
            ShadingMethod.SOLID -> renderFacesSolid(gl, facesMesh)
            ShadingMethod.MATERIAL -> renderFacesMaterial(gl, facesMesh, index)
        }
    }

    private fun renderFacesWireframe(gl: GlimpseAdapter, facesMesh: Mesh) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
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
                modelMatrix = Mat4.identity,
                normalMatrix = Mat3.identity,
                cameraPosition = camera.eye,
                upVector = upVector.vector,
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

        programExecutorsManager.renderMaterial(
            gl,
            MaterialShader(
                projectionMatrix = lens.projectionMatrix,
                viewMatrix = camera.viewMatrix,
                modelMatrix = Mat4.identity,
                normalMatrix = Mat3.identity,
                cameraPosition = camera.eye,
                upVector = upVector.vector,
                ambientColor = Vec3(color = material?.let { it.ambientColor ?: it.diffuseColor } ?: Color.WHITE),
                diffuseColor = Vec3(color = material?.diffuseColor ?: Color.WHITE),
                specularColor = Vec3(color = material?.specularColor ?: Color.WHITE),
                specularExponent = material?.specularExponent ?: 1f,
                ambientTexture = ambientTexture ?: diffuseTexture ?: fallbackTexture,
                diffuseTexture = diffuseTexture ?: fallbackTexture,
                specularTexture = material?.specularColorMap?.getTexture(gl) ?: fallbackTexture,
                specularExponentTexture = material?.specularExponentMap?.getTexture(gl) ?: fallbackTexture,
                specularExponentBase = material?.specularExponentBase ?: 0f,
                specularExponentGain = material?.specularExponentGain ?: 1f,
                normalmapTexture = material?.bumpMap?.getTexture(gl) ?: fallbackNormalmap,
                normalmapMultiplier = material?.bumpMapMultiplier ?: 1f
            ),
            facesMesh
        )
    }

    private fun VirtualFile.getTexture(gl: GlimpseAdapter): Texture? = try {
        texturesManager[gl, this]
    } catch (expected: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.obj.preview.getTexture.error", name),
            expected
        )
        null
    }

    private fun renderLines(gl: GlimpseAdapter, linesMesh: Mesh) {
        gl.glLineWidth(config.lineWidth)
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
                color = PreviewColors.asVec4(PreviewColors.COLOR_LINE)
            ),
            linesMesh
        )
    }

    private fun renderPoints(gl: GlimpseAdapter, pointsMesh: Mesh) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
                pointSize = config.pointSize,
                color = PreviewColors.asVec4(PreviewColors.COLOR_POINT)
            ),
            pointsMesh
        )
    }

    private fun renderAxes(gl: GlimpseAdapter) {
        gl.glCullFace(FaceCullingMode.DISABLED)
        gl.glLineWidth(config.axisLineWidth)

        renderAxis(gl, AxisMeshFactory.xAxisModelMatrix, PreviewColors.COLOR_AXIS_X)
        renderAxis(gl, AxisMeshFactory.yAxisModelMatrix, PreviewColors.COLOR_AXIS_Y)
        renderAxis(gl, AxisMeshFactory.zAxisModelMatrix, PreviewColors.COLOR_AXIS_Z)
    }

    private fun renderAxis(gl: GlimpseAdapter, modelMatrix: Mat4, colorKey: ColorKey) {
        val scale = (model?.size ?: 1f) * AXIS_LENGTH_FACTOR
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * modelMatrix,
                color = PreviewColors.asVec4(colorKey)
            ),
            axisMesh,
            axisConeMesh
        )
    }

    private fun renderGrid(gl: GlimpseAdapter) {
        gl.glLineWidth(config.gridLineWidth)
        val scale = GridMeshFactory.calculateGridScale(modelSize = model?.size ?: 1f)
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.gridModelMatrix,
                color = PreviewColors.asVec4(PreviewColors.COLOR_GRID, GRID_ALPHA)
            ),
            gridMesh
        )
        if (config.showFineGrid) renderFineGrid(gl, scale)
    }

    private fun renderFineGrid(gl: GlimpseAdapter, scale: Float) {
        programExecutorsManager.renderWireframe(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.gridModelMatrix,
                color = PreviewColors.asVec4(PreviewColors.COLOR_GRID, FINE_GRID_ALPHA)
            ),
            fineGridMesh
        )
    }

    override fun onDestroy(gl: GlimpseAdapter) {
        try {
            (facesMeshes + linesMeshes + pointsMeshes).forEach { mesh -> mesh.dispose(gl) }
            gridMesh.dispose(gl)
            fineGridMesh.dispose(gl)
            axisMesh.dispose(gl)
            axisConeMesh.dispose(gl)
            texturesManager.dispose(gl)
            fallbackTexture.dispose(gl)
            fallbackNormalmap.dispose(gl)
            programExecutorsManager.dispose(gl)
        } catch (ignored: Throwable) {
        }
    }

    companion object {
        private const val GRID_ALPHA = 0.3f
        private const val FINE_GRID_ALPHA = 0.1f

        private const val AXIS_LENGTH_FACTOR = 2f
    }

}
