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
import it.czerwinski.intellij.wavefront.editor.gl.glClearColor
import it.czerwinski.intellij.wavefront.editor.gl.glFaces
import it.czerwinski.intellij.wavefront.editor.gl.glLines
import it.czerwinski.intellij.wavefront.editor.gl.glPoints
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.settings.ObjPreviewFileEditorSettingsState
import java.awt.Color

class GL2Presenter(
    animator: GLAnimatorControl,
    private val errorCallback: (Throwable) -> Unit
) : GLPresenter<GL2>,
    GLAnimatorControl by animator,
    GLContext<GL2> {

    private var aspect: Float = 1f

    private var model: GLModel? = null

    private var cameraModel: GLCameraModel? = null

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

        glHint(GL2.GL_PERSPECTIVE_CORRECTION_HINT, GL2.GL_NICEST)

        glEnable(GL.GL_CULL_FACE)
        glCullFace(GL.GL_BACK)

        glShadeModel(GL2.GL_SMOOTH)

        glLightfv(GL2.GL_LIGHT0, GL2.GL_POSITION, LIGHT_POSITION, 0)
        glEnable(GL2.GL_LIGHTING)
        glEnable(GL2.GL_LIGHT0)

        glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, getColorComponents(COLOR_FACE, AMBIENT_FACTOR), 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, getColorComponents(COLOR_FACE, DIFFUSE_FACTOR), 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, getColorComponents(COLOR_FACE, SPECULAR_FACTOR), 0)
        glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, SHININESS)
    }

    private fun getColorComponents(colorKey: ColorKey, factor: Float = 1f): FloatArray {
        val output = FloatArray(size = COLOR_COMPONENTS_SIZE)
        val color = EditorColorsManager.getInstance().globalScheme.getColor(colorKey) ?: Color.GRAY
        color.getRGBComponents(output)
        for (index in output.indices) {
            output[index] = output[index] * factor
        }
        return output
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
        updateProjectionMatrix()
        updateModelViewMatrix()
        model?.let { model ->
            glFaces(model)
            glDisable(GL2.GL_LIGHTING)
            glColor4fv(getColorComponents(COLOR_LINE), 0)
            glLines(model)
            glColor4fv(getColorComponents(COLOR_POINT), 0)
            glPoints(model)
            glEnable(GL2.GL_LIGHTING)
        }
        pause()
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

        private const val COLOR_COMPONENTS_SIZE = 4

        private const val AMBIENT_FACTOR = .7f
        private const val DIFFUSE_FACTOR = .9f
        private const val SPECULAR_FACTOR = 0f
        private const val SHININESS = 128f

        private val LIGHT_POSITION =
            floatArrayOf(-10f, 10f, 10f)
    }
}
