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
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLException
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
import graphics.glimpse.shaders.Program
import graphics.glimpse.shaders.Shader
import graphics.glimpse.shaders.ShaderType
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat3
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.Vec4
import graphics.glimpse.types.scale
import it.czerwinski.intellij.wavefront.editor.gl.meshes.AxisMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.GridMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.LinesMesh
import it.czerwinski.intellij.wavefront.editor.gl.meshes.PointsMesh
import it.czerwinski.intellij.wavefront.editor.gl.meshes.SolidFacesMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.WireframeFacesMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.MaterialShaderProgramExecutor
import it.czerwinski.intellij.wavefront.editor.gl.shaders.ShaderResources
import it.czerwinski.intellij.wavefront.editor.gl.shaders.SolidShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.SolidShaderProgramExecutor
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShaderProgramExecutor
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.ShadingMethod
import it.czerwinski.intellij.wavefront.editor.model.UpVector
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import java.awt.Color
import java.util.concurrent.atomic.AtomicBoolean

class PreviewScene(
    animatorControl: GLAnimatorControl
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
    private var settings: ObjPreviewFileEditorSettingsState = ObjPreviewFileEditorSettingsState()

    private val background
        get() = EditorColorsManager.getInstance().globalScheme.defaultBackground

    private lateinit var wireframeProgram: Program
    private lateinit var wireframeShaderProgramExecutor: WireframeShaderProgramExecutor

    private lateinit var solidProgram: Program
    private lateinit var solidShaderProgramExecutor: SolidShaderProgramExecutor

    private lateinit var materialProgram: Program
    private lateinit var materialShaderProgramExecutor: MaterialShaderProgramExecutor

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
        requestRender()
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

    fun updateSettings(newSettings: ObjPreviewFileEditorSettingsState) {
        settings = newSettings
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

        createShaders(gl)
        createMeshes(gl)
    }

    private fun createShaders(gl: GlimpseAdapter) {
        val shaderFactory = Shader.Factory.newInstance(gl)
        val programBuilder = Program.Builder.newInstance(gl)

        wireframeProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.WIREFRAME)
        solidProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.SOLID)
        materialProgram = createProgram(shaderFactory, programBuilder, ShadingMethod.MATERIAL)

        wireframeShaderProgramExecutor = WireframeShaderProgramExecutor(wireframeProgram)
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

    private fun createMeshes(gl: GlimpseAdapter) {
        gridMesh = GridMeshFactory.createGrid(gl)
        fineGridMesh = GridMeshFactory.createFineGrid(gl)
        axisMesh = AxisMeshFactory.createAxis(gl)
        axisConeMesh = AxisMeshFactory.createAxisCone(gl)
        if (model != null) {
            recreateModelMeshes(gl)
        }
    }

    override fun onResize(gl: GlimpseAdapter, x: Int, y: Int, width: Int, height: Int) {
        this.width = width
        this.height = height

        if (width == 0 || height == 0) return

        aspect = width.toFloat() / height.toFloat()
        gl.glViewport(width = width, height = height)

        cameraModel?.let { recalculateCamera(it) }
    }

    override fun onRender(gl: GlimpseAdapter) {
        gl.glClearColor(Vec3(background))
        gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)
        gl.glCullFace(FaceCullingMode.BACK)
        renderModel(gl)
        if (showAxes) renderAxes(gl)
        if (showGrid) renderGrid(gl)
        if (isStarted) pause()
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
            ShadingMethod.WIREFRAME -> {
                wireframeProgram.use(gl)
                wireframeShaderProgramExecutor.applyParams(
                    gl,
                    WireframeShader(
                        mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
                        color = Colors.asVec4(Colors.COLOR_FACE)
                    )
                )
                wireframeShaderProgramExecutor.drawMesh(gl, facesMesh)
            }
            ShadingMethod.SOLID -> {
                solidProgram.use(gl)
                solidShaderProgramExecutor.applyParams(
                    gl,
                    SolidShader(
                        projectionMatrix = lens.projectionMatrix,
                        viewMatrix = camera.viewMatrix,
                        modelMatrix = Mat4.identity,
                        normalMatrix = Mat3.identity,
                        cameraPosition = camera.eye,
                        upVector = upVector.vector,
                        color = Colors.asVec3(Colors.COLOR_FACE)
                    )
                )
                solidShaderProgramExecutor.drawMesh(gl, facesMesh)
            }
            ShadingMethod.MATERIAL -> {
                materialProgram.use(gl)
                materialShaderProgramExecutor.applyParams(
                    gl,
                    MaterialShader(
                        projectionMatrix = lens.projectionMatrix,
                        viewMatrix = camera.viewMatrix,
                        modelMatrix = Mat4.identity,
                        normalMatrix = Mat3.identity,
                        cameraPosition = camera.eye,
                        upVector = upVector.vector,
                        ambientColor = Vec3(
                            color = model?.materials?.getOrNull(index)?.let { it.ambientColor ?: it.diffuseColor }
                                ?: Color.WHITE
                        ),
                        diffuseColor = Vec3(model?.materials?.getOrNull(index)?.diffuseColor ?: Color.WHITE),
                        specularColor = Vec3(model?.materials?.getOrNull(index)?.specularColor ?: Color.BLACK),
                        specularExponent = model?.materials?.getOrNull(index)?.specularExponent ?: 1f
                    )
                )
                materialShaderProgramExecutor.drawMesh(gl, facesMesh)
            }
        }
    }

    private fun renderLines(gl: GlimpseAdapter, linesMesh: Mesh) {
        gl.glLineWidth(settings.lineWidth)
        wireframeProgram.use(gl)
        wireframeShaderProgramExecutor.applyParams(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
                color = Colors.asVec4(Colors.COLOR_LINE)
            )
        )
        wireframeShaderProgramExecutor.drawMesh(gl, linesMesh)
    }

    private fun renderPoints(gl: GlimpseAdapter, pointsMesh: Mesh) {
        wireframeProgram.use(gl)
        wireframeShaderProgramExecutor.applyParams(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix,
                pointSize = settings.pointSize,
                color = Colors.asVec4(Colors.COLOR_POINT)
            )
        )
        wireframeShaderProgramExecutor.drawMesh(gl, pointsMesh)
    }

    private fun renderAxes(gl: GlimpseAdapter) {
        gl.glCullFace(FaceCullingMode.DISABLED)
        gl.glLineWidth(settings.axisLineWidth)

        wireframeProgram.use(gl)
        renderAxis(gl, AxisMeshFactory.xAxisModelMatrix, Colors.COLOR_AXIS_X)
        renderAxis(gl, AxisMeshFactory.yAxisModelMatrix, Colors.COLOR_AXIS_Y)
        renderAxis(gl, AxisMeshFactory.zAxisModelMatrix, Colors.COLOR_AXIS_Z)
    }

    private fun renderAxis(gl: GlimpseAdapter, modelMatrix: Mat4, colorKey: ColorKey) {
        val scale = (model?.size ?: 1f) * AXIS_LENGTH_FACTOR
        wireframeShaderProgramExecutor.applyParams(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * modelMatrix,
                color = Colors.asVec4(colorKey)
            )
        )
        wireframeShaderProgramExecutor.drawMesh(gl, axisMesh)
        wireframeShaderProgramExecutor.drawMesh(gl, axisConeMesh)
    }

    private fun renderGrid(gl: GlimpseAdapter) {
        gl.glLineWidth(settings.gridLineWidth)
        val scale = GridMeshFactory.calculateGridScale(modelSize = model?.size ?: 1f)
        wireframeProgram.use(gl)
        wireframeShaderProgramExecutor.applyParams(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.gridModelMatrix,
                color = Colors.asVec4(Colors.COLOR_GRID, GRID_ALPHA)
            )
        )
        wireframeShaderProgramExecutor.drawMesh(gl, gridMesh)
        if (settings.showFineGrid) renderFineGrid(gl, scale)
    }

    private fun renderFineGrid(gl: GlimpseAdapter, scale: Float) {
        wireframeShaderProgramExecutor.applyParams(
            gl,
            WireframeShader(
                mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * upVector.gridModelMatrix,
                color = Colors.asVec4(Colors.COLOR_GRID, FINE_GRID_ALPHA)
            )
        )
        wireframeShaderProgramExecutor.drawMesh(gl, fineGridMesh)
    }

    override fun onDestroy(gl: GlimpseAdapter) {
        try {
            (facesMeshes + linesMeshes + pointsMeshes).forEach { mesh -> mesh.dispose(gl) }
            gridMesh.dispose(gl)
            fineGridMesh.dispose(gl)
            axisMesh.dispose(gl)
            axisConeMesh.dispose(gl)
            wireframeShaderProgramExecutor.dispose()
            solidShaderProgramExecutor.dispose()
            materialShaderProgramExecutor.dispose()
            wireframeProgram.dispose(gl)
            solidProgram.dispose(gl)
            materialProgram.dispose(gl)
        } catch (ignored: GLException) {
        }
    }

    companion object {
        private const val GRID_ALPHA = 0.3f
        private const val FINE_GRID_ALPHA = 0.1f

        private const val AXIS_LENGTH_FACTOR = 2f
    }

    internal object Colors {

        internal val COLOR_FACE: ColorKey = ColorKey.createColorKey("OBJ_3D_FACE", Color.LIGHT_GRAY)
        internal val COLOR_LINE: ColorKey = ColorKey.createColorKey("OBJ_3D_LINE", Color.GRAY)
        internal val COLOR_POINT: ColorKey = ColorKey.createColorKey("OBJ_3D_POINT", Color.GRAY)

        internal val COLOR_AXIS_X: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_X", Color.RED)
        internal val COLOR_AXIS_Y: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Y", Color.GREEN)
        internal val COLOR_AXIS_Z: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Z", Color.BLUE)
        internal val COLOR_GRID: ColorKey = ColorKey.createColorKey("OBJ_3D_GRID", Color.GRAY)

        internal fun asVec3(colorKey: ColorKey): Vec3 =
            Vec3(color = EditorColorsManager.getInstance().globalScheme.getColor(colorKey) ?: Color.GRAY)

        internal fun asVec4(colorKey: ColorKey, alpha: Float = 1f): Vec4 =
            asVec3(colorKey).toVec4(w = alpha)
    }
}
