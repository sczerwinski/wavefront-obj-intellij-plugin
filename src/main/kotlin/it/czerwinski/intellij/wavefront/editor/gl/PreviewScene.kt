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
import com.intellij.util.ui.UIUtil
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.FaceCullingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.Camera
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.Lens
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.textures.Texture
import graphics.glimpse.textures.TextureMagFilter
import graphics.glimpse.textures.TextureMinFilter
import graphics.glimpse.textures.TextureType
import graphics.glimpse.textures.TextureWrap
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec2
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.scale
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.AxisMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.GridMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.TextMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.shaders.TextShader
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.gl.textures.TextureResources
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.UpVector

/**
 * Base 3D preview scene.
 *
 * This class implements rendering axes and grid.
 */
abstract class PreviewScene(
    profile: GLProfile,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : BaseScene(profile, animatorControl, errorLog) {

    /**
     * Should return maximum distance from the origin point that should be rendered.
     */
    protected abstract val modelSize: Float?

    /**
     * Scene camera configuration.
     */
    protected var camera: Camera = TargetCamera(eye = Vec3.unitX, target = Vec3.nullVector)

    /**
     * Scene lens configuration.
     */
    protected var lens: Lens = PerspectiveLens(Angle.rightAngle, aspect = 1f, near = 1f, far = 2f)

    /**
     * Vector pointing in the "up" direction of the scene.
     */
    protected abstract val upVector: UpVector

    /**
     * Determines whether axes should be shown.
     */
    var showAxes: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    /**
     * Determines whether grid should be shown.
     */
    var showGrid: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    /**
     * Advanced scene configuration.
     */
    var config: PreviewSceneConfig = PreviewSceneConfig()
        set(value) {
            field = value
            requestRender()
        }

    private val fontScaling: Float get() = if (UIUtil.isRetina()) 2f else 1f

    private lateinit var fontTexture: Texture
    private lateinit var boldFontTexture: Texture

    private lateinit var axisMesh: Mesh
    private lateinit var axisConeMesh: Mesh
    private lateinit var axisXLabelMesh: Mesh
    private lateinit var axisYLabelMesh: Mesh
    private lateinit var axisZLabelMesh: Mesh

    private lateinit var gridMesh: Mesh
    private lateinit var fineGridMesh: Mesh

    init {
        TextureResources.prepare(profile)
    }

    override fun initialize(gl: GlimpseAdapter) {
        createFontTexture(gl)
        createAxesMeshes(gl)
        createGridMeshes(gl)
    }

    private fun createFontTexture(gl: GlimpseAdapter) {
        try {
            val textures = Texture.Builder.getInstance(gl)
                .addTexture(TextureResources.fontTextureImageSource)
                .addTexture(TextureResources.boldFontTextureImageSource)
                .generateMipmaps()
                .build()
            fontTexture = textures.first()
            boldFontTexture = textures.last()

            gl.glTexParameterWrap(TextureType.TEXTURE_2D, TextureWrap.REPEAT, TextureWrap.REPEAT)
            gl.glTexParameterFilter(
                TextureType.TEXTURE_2D,
                TextureMinFilter.LINEAR_MIPMAP_LINEAR,
                TextureMagFilter.LINEAR
            )
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createFontTexture.error"),
                expected
            )
        }
    }

    private fun createAxesMeshes(gl: GlimpseAdapter) {
        try {
            axisMesh = AxisMeshFactory.createAxis(gl)
            axisConeMesh = AxisMeshFactory.createAxisCone(gl)
            axisXLabelMesh = TextMeshFactory.createText(gl, AXIS_X_LABEL)
            axisYLabelMesh = TextMeshFactory.createText(gl, AXIS_Y_LABEL)
            axisZLabelMesh = TextMeshFactory.createText(gl, AXIS_Z_LABEL)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createAxesMeshes.error"),
                expected
            )
        }
    }

    private fun createGridMeshes(gl: GlimpseAdapter) {
        try {
            gridMesh = GridMeshFactory.createGrid(gl)
            fineGridMesh = GridMeshFactory.createFineGrid(gl)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.createGridMeshes.error"),
                expected
            )
        }
    }

    final override fun doRender(gl: GlimpseAdapter) {
        renderModel(gl)
        if (showAxes) renderAxes(gl)
        if (showGrid) renderGrid(gl)
        if (showAxes && config.showAxesLabels) renderAxesLabels(gl)
        if (isStarted) pause()
    }

    /**
     * Renders the actual model being the subject of this preview.
     */
    protected abstract fun renderModel(gl: GlimpseAdapter)

    private fun renderAxes(gl: GlimpseAdapter) {
        gl.glCullFace(FaceCullingMode.DISABLED)
        gl.glLineWidth(config.axisLineWidth)

        renderAxis(gl, AxisMeshFactory.xAxisModelMatrix, PreviewColors.COLOR_AXIS_X)
        renderAxis(gl, AxisMeshFactory.yAxisModelMatrix, PreviewColors.COLOR_AXIS_Y)
        renderAxis(gl, AxisMeshFactory.zAxisModelMatrix, PreviewColors.COLOR_AXIS_Z)
    }

    private fun renderAxis(gl: GlimpseAdapter, modelMatrix: Mat4, colorKey: ColorKey) {
        val scale = (modelSize ?: 1f) * AXIS_LENGTH_FACTOR
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
        val scale = GridMeshFactory.calculateGridScale(modelSize = modelSize ?: 1f)
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

    private fun renderAxesLabels(gl: GlimpseAdapter) {
        renderAxisLabel(gl, AxisMeshFactory.xAxisModelMatrix, PreviewColors.COLOR_AXIS_X, axisXLabelMesh)
        renderAxisLabel(gl, AxisMeshFactory.yAxisModelMatrix, PreviewColors.COLOR_AXIS_Y, axisYLabelMesh)
        renderAxisLabel(gl, AxisMeshFactory.zAxisModelMatrix, PreviewColors.COLOR_AXIS_Z, axisZLabelMesh)
    }

    private fun renderAxisLabel(gl: GlimpseAdapter, modelMatrix: Mat4, colorKey: ColorKey, labelMesh: Mesh) {
        val textSize = fontScaling * config.axisLabelFontSize
        val scale = (modelSize ?: 1f) * AXIS_LENGTH_FACTOR
        val mvpMatrix = lens.projectionMatrix * camera.viewMatrix * scale(scale) * modelMatrix
        val labelPosition = mvpMatrix * (Vec3.unitZ * AXIS_LABEL_DISTANCE_FACTOR).toVec4(w = 1f)
        programExecutorsManager.renderText(
            gl,
            TextShader(
                position = labelPosition.toVec3() / labelPosition.w,
                scale = Vec2(x = textSize / width, y = textSize / height),
                color = PreviewColors.asVec4(colorKey),
                texture = boldFontTexture
            ),
            labelMesh
        )
    }

    override fun dispose(gl: GlimpseAdapter) {
        axisMesh.dispose(gl)
        axisConeMesh.dispose(gl)
        gridMesh.dispose(gl)
        fineGridMesh.dispose(gl)
    }

    companion object {
        private const val AXIS_LENGTH_FACTOR = 2f
        private const val AXIS_LABEL_DISTANCE_FACTOR = 1.1f
        private const val AXIS_X_LABEL = "x"
        private const val AXIS_Y_LABEL = "y"
        private const val AXIS_Z_LABEL = "z"

        private const val GRID_ALPHA = 0.3f
        private const val FINE_GRID_ALPHA = 0.1f
    }
}
