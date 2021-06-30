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
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.FaceCullingMode
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.cameras.Camera
import graphics.glimpse.cameras.TargetCamera
import graphics.glimpse.lenses.Lens
import graphics.glimpse.lenses.PerspectiveLens
import graphics.glimpse.meshes.Mesh
import graphics.glimpse.types.Angle
import graphics.glimpse.types.Mat4
import graphics.glimpse.types.Vec3
import graphics.glimpse.types.scale
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.meshes.AxisMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.meshes.GridMeshFactory
import it.czerwinski.intellij.wavefront.editor.gl.shaders.WireframeShader
import it.czerwinski.intellij.wavefront.editor.model.PreviewSceneConfig
import it.czerwinski.intellij.wavefront.editor.model.UpVector

abstract class PreviewScene(
    profile: GLProfile,
    animatorControl: GLAnimatorControl,
    errorLog: ErrorLog
) : BaseScene(profile, animatorControl, errorLog) {

    protected abstract val modelSize: Float?

    protected var camera: Camera = TargetCamera(eye = Vec3.unitX, target = Vec3.nullVector)
    protected var lens: Lens = PerspectiveLens(Angle.rightAngle, aspect = 1f, near = 1f, far = 2f)

    protected abstract val upVector: UpVector

    var showAxes: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    var showGrid: Boolean = false
        set(value) {
            field = value
            requestRender()
        }

    var config: PreviewSceneConfig = PreviewSceneConfig()
        set(value) {
            field = value
            requestRender()
        }

    private lateinit var axisMesh: Mesh
    private lateinit var axisConeMesh: Mesh
    private lateinit var gridMesh: Mesh
    private lateinit var fineGridMesh: Mesh

    override fun initialize(gl: GlimpseAdapter) {
        createAxesMeshes(gl)
        createGridMeshes(gl)
    }

    private fun createAxesMeshes(gl: GlimpseAdapter) {
        try {
            axisMesh = AxisMeshFactory.createAxis(gl)
            axisConeMesh = AxisMeshFactory.createAxisCone(gl)
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
        if (isStarted) pause()
    }

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

    override fun dispose(gl: GlimpseAdapter) {
        axisMesh.dispose(gl)
        axisConeMesh.dispose(gl)
        gridMesh.dispose(gl)
        fineGridMesh.dispose(gl)
    }

    companion object {
        private const val GRID_ALPHA = 0.3f
        private const val FINE_GRID_ALPHA = 0.1f

        private const val AXIS_LENGTH_FACTOR = 2f
    }
}
