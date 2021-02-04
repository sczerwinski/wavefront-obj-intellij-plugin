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

package it.czerwinski.intellij.wavefront.editor.ui

import com.intellij.openapi.editor.colors.ColorKey
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.glu.GLU
import com.jogamp.opengl.math.FloatUtil
import it.czerwinski.intellij.wavefront.editor.gl.glClearColor
import it.czerwinski.intellij.wavefront.editor.gl.glFaces
import it.czerwinski.intellij.wavefront.editor.gl.glLines
import it.czerwinski.intellij.wavefront.editor.gl.glPoints
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import java.awt.Color
import kotlin.math.PI
import kotlin.math.ceil
import kotlin.math.cos
import kotlin.math.log10
import kotlin.math.sin

class GL2Presenter(
    animator: GLAnimatorControl,
    private val errorCallback: (Throwable) -> Unit
) : GLPresenter<GL2>,
    GLAnimatorControl by animator,
    GLContext<GL2> {

    private var width: Int = 1

    private var height: Int = 1

    private var aspect: Float = 1f

    private var model: GLModel? = null

    private var cameraModel: GLCameraModel? = null

    private var showAxes: Boolean = false

    private var showGrid: Boolean = false

    private var gridRotation: Int = 0

    private var settings: ObjPreviewFileEditorSettingsState = ObjPreviewFileEditorSettingsState()

    private val background get() =
        EditorColorsManager.getInstance().globalScheme.defaultBackground

    private lateinit var glu: GLU

    override fun updateModel(newModel: GLModel?) {
        model = newModel
        if (isStarted && isPaused) resume()
    }

    override fun updateCameraModel(newCameraModel: GLCameraModel) {
        cameraModel = newCameraModel
        if (isStarted && isPaused) resume()
    }

    override fun updateAxes(newShowAxes: Boolean) {
        showAxes = newShowAxes
        if (isStarted && isPaused) resume()
    }

    override fun updateGrid(newShowGrid: Boolean, newGridRotation: Int) {
        showGrid = newShowGrid
        gridRotation = newGridRotation
        if (isStarted && isPaused) resume()
    }

    override fun updateSettings(newSettings: ObjPreviewFileEditorSettingsState) {
        settings = newSettings
        if (isStarted && isPaused) resume()
    }

    override fun init(
        drawable: GLAutoDrawable?
    ) = drawable.runInGLContext {
        glu = GLU()

        glClearDepth(1.0)

        glEnable(GL.GL_DEPTH_TEST)
        glDepthFunc(GL.GL_LEQUAL)

        glEnable(GL.GL_BLEND)
        glBlendFunc(GL2.GL_SRC_ALPHA, GL2.GL_ONE_MINUS_SRC_ALPHA)

        glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST)

        glCullFace(GL.GL_BACK)

        glShadeModel(GL2.GL_SMOOTH)

        glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LIGHT_POSITION, 0)
        glEnable(GL2.GL_LIGHTING)
        glEnable(GL2.GL_LIGHT0)
    }

    override fun GLAutoDrawable?.runInGLContext(block: GL2.() -> Unit) {
        try {
            with(GL2Context) { runInGLContext(block) }
        } catch (expected: Throwable) {
            errorCallback(expected)
        }
    }

    override fun reshape(
        drawable: GLAutoDrawable?,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) = drawable.runInGLContext {
        this@GL2Presenter.width = width
        this@GL2Presenter.height = height

        if (width == 0 || height == 0) return@runInGLContext
        aspect = width.toFloat() / height.toFloat()

        glViewport(0, 0, width, height)

        resume()
    }

    override fun display(drawable: GLAutoDrawable?) = drawable.runInGLContext {
        glClearColor(background)
        glClear(GL2.GL_COLOR_BUFFER_BIT or GL2.GL_DEPTH_BUFFER_BIT)
        glLineWidth(settings.lineWidth)
        glPointSize(settings.pointSize)
        configureMaterial()
        updateProjectionMatrix()
        updateModelViewMatrix()
        model?.let { model ->
            glEnable(GL.GL_CULL_FACE)
            glEnable(GL2.GL_LIGHTING)
            glFaces(model)
            glDisable(GL2.GL_LIGHTING)
            glColor4fv(getColorComponents(COLOR_LINE), 0)
            glLines(model)
            glColor4fv(getColorComponents(COLOR_POINT), 0)
            glPoints(model)
            val gridSize = calculateGridSize(model.size)
            glDisable(GL.GL_CULL_FACE)
            if (showAxes) displayAxes(size = model.size * AXIS_LENGTH_FACTOR)
            if (showGrid) displayGrid(size = gridSize)
        }
        pause()
    }

    private fun GL2.configureMaterial() {
        glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, getColorComponents(COLOR_FACE, AMBIENT_FACTOR), 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, getColorComponents(COLOR_FACE, DIFFUSE_FACTOR), 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, getColorComponents(COLOR_FACE, SPECULAR_FACTOR), 0)
        glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, SHININESS)
    }

    private fun getColorComponents(colorKey: ColorKey, factor: Float = 1f, alpha: Float = 1f): FloatArray {
        val output = FloatArray(size = COLOR_COMPONENTS_SIZE)
        val color = EditorColorsManager.getInstance().globalScheme.getColor(colorKey) ?: Color.GRAY
        color.getRGBComponents(output)
        for (index in output.indices) {
            output[index] = output[index] * factor
        }
        output[output.lastIndex] = alpha
        return output
    }

    private fun GL2.updateProjectionMatrix() {
        glMatrixMode(GL2.GL_PROJECTION)
        glLoadIdentity()
        cameraModel?.run {
            glu.gluPerspective(fovY(aspect), aspect, near, far)
        }
    }

    private fun GL2.updateModelViewMatrix() {
        glMatrixMode(GL2.GL_MODELVIEW)
        glLoadIdentity()
        cameraModel?.run {
            glu.gluLookAt(x, y, z, CENTER_X, CENTER_Y, CENTER_Z, upX, upY, upZ)
        }
    }

    private fun calculateGridSize(size: Float) =
        FloatUtil.pow(GRID_STEPS.toFloat(), ceil(log10(size)))

    private fun GL2.displayAxes(size: Float) {
        glColor4fv(getColorComponents(COLOR_AXIS_X), 0)
        xCone(size)
        glColor4fv(getColorComponents(COLOR_AXIS_Y), 0)
        yCone(size)
        glColor4fv(getColorComponents(COLOR_AXIS_Z), 0)
        zCone(size)
        glLineWidth(settings.axisLineWidth)
        glBegin(GL2.GL_LINES)
        glColor4fv(getColorComponents(COLOR_AXIS_X), 0)
        xGridLine(size)
        glColor4fv(getColorComponents(COLOR_AXIS_Y), 0)
        yGridLine(size)
        glColor4fv(getColorComponents(COLOR_AXIS_Z), 0)
        zGridLine(size)
        glEnd()
    }

    private fun GL2.xCone(size: Float) {
        val radius = size * AXIS_CONE_RADIUS_FACTOR
        val length = size * AXIS_CONE_LENGTH_FACTOR
        glBegin(GL2.GL_TRIANGLE_FAN)
        glVertex3f(size + length, 0f, 0f)
        for (index in 0..AXIS_CONE_VERTEX_COUNT) {
            val angle = (2 * PI * index / AXIS_CONE_VERTEX_COUNT).toFloat()
            glVertex3f(size - length, radius * cos(angle), radius * sin(angle))
        }
        glEnd()
    }

    private fun GL2.yCone(size: Float) {
        val radius = size * AXIS_CONE_RADIUS_FACTOR
        val length = size * AXIS_CONE_LENGTH_FACTOR
        glBegin(GL2.GL_TRIANGLE_FAN)
        glVertex3f(0f, size + length, 0f)
        for (index in 0..AXIS_CONE_VERTEX_COUNT) {
            val angle = (2 * PI * index / AXIS_CONE_VERTEX_COUNT).toFloat()
            glVertex3f(radius * sin(angle), size - length, radius * cos(angle))
        }
        glEnd()
    }

    private fun GL2.zCone(size: Float) {
        val radius = size * AXIS_CONE_RADIUS_FACTOR
        val length = size * AXIS_CONE_LENGTH_FACTOR
        glBegin(GL2.GL_TRIANGLE_FAN)
        glVertex3f(0f, 0f, size + length)
        for (index in 0..AXIS_CONE_VERTEX_COUNT) {
            val angle = (2 * PI * index / AXIS_CONE_VERTEX_COUNT).toFloat()
            glVertex3f(radius * cos(angle), radius * sin(angle), size - length)
        }
        glEnd()
    }

    private fun GL2.xGridLine(size: Float, y: Float = 0f, z: Float = 0f) {
        glVertex3f(-size, y, z)
        glVertex3f(size, y, z)
    }

    private fun GL2.yGridLine(size: Float, x: Float = 0f, z: Float = 0f) {
        glVertex3f(x, -size, z)
        glVertex3f(x, size, z)
    }

    private fun GL2.zGridLine(size: Float, x: Float = 0f, y: Float = 0f) {
        glVertex3f(x, y, -size)
        glVertex3f(x, y, size)
    }

    private fun GL2.displayGrid(size: Float) {
        glMatrixMode(GL2.GL_MODELVIEW)
        glPushMatrix()
        glRotatef(gridRotation * GRID_ROTATION_ANGLE, 1f, 1f, 1f)
        glLineWidth(settings.gridLineWidth)
        val gridStep = size / GRID_STEPS
        val minorGridStep = gridStep / GRID_STEPS
        glBegin(GL2.GL_LINES)
        glColor4fv(getColorComponents(COLOR_GRID, alpha = GRID_ALPHA), 0)
        for (index in -GRID_STEPS..GRID_STEPS) {
            val position = gridStep * index
            xGridLine(size, y = position)
            yGridLine(size, x = position)
        }
        if (settings.showFineGrid) {
            glColor4fv(getColorComponents(COLOR_GRID, alpha = GRID_FINE_ALPHA), 0)
            for (index in -GRID_STEPS until GRID_STEPS) {
                for (minorIndex in 1 until GRID_STEPS) {
                    val position = gridStep * index + minorGridStep * minorIndex
                    xGridLine(size, y = position)
                    yGridLine(size, x = position)
                }
            }
        }
        glEnd()
        glPopMatrix()
    }

    override fun dispose(drawable: GLAutoDrawable?) = Unit

    override fun dispose() {
        if (isAnimating) stop()
    }

    companion object {
        private const val CENTER_X = 0f
        private const val CENTER_Y = 0f
        private const val CENTER_Z = 0f

        internal val COLOR_FACE: ColorKey = ColorKey.createColorKey("OBJ_3D_FACE", Color.LIGHT_GRAY)
        internal val COLOR_LINE: ColorKey = ColorKey.createColorKey("OBJ_3D_LINE", Color.GRAY)
        internal val COLOR_POINT: ColorKey = ColorKey.createColorKey("OBJ_3D_POINT", Color.GRAY)

        internal val COLOR_AXIS_X: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_X", Color.RED)
        internal val COLOR_AXIS_Y: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Y", Color.GREEN)
        internal val COLOR_AXIS_Z: ColorKey = ColorKey.createColorKey("OBJ_3D_AXIS_Z", Color.BLUE)
        internal val COLOR_GRID: ColorKey = ColorKey.createColorKey("OBJ_3D_GRID", Color.GRAY)

        private const val COLOR_COMPONENTS_SIZE = 4

        private const val AMBIENT_FACTOR = .7f
        private const val DIFFUSE_FACTOR = .9f
        private const val SPECULAR_FACTOR = 0f
        private const val SHININESS = 128f

        private const val AXIS_LINE_WIDTH = 3f
        private const val AXIS_LENGTH_FACTOR = 2f
        private const val AXIS_CONE_LENGTH_FACTOR = 0.05f
        private const val AXIS_CONE_RADIUS_FACTOR = 0.02f
        private const val AXIS_CONE_VERTEX_COUNT = 8
        private const val GRID_LINE_WIDTH = 1f
        private const val GRID_STEPS = 10
        private const val GRID_ROTATION_ANGLE = 120f
        private const val GRID_ALPHA = 0.3f
        private const val GRID_FINE_ALPHA = 0.1f

        private val LIGHT_POSITION =
            floatArrayOf(-10f, 10f, 10f)
    }
}
