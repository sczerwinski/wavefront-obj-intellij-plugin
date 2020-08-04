/*
 * Copyright 2020 Slawomir Czerwinski
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

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.jogamp.opengl.GL
import com.jogamp.opengl.GL2
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLAutoDrawable
import com.jogamp.opengl.glu.GLU
import it.czerwinski.intellij.wavefront.editor.gl.glClearColor
import it.czerwinski.intellij.wavefront.editor.gl.glFaces
import it.czerwinski.intellij.wavefront.editor.model.GLCameraModel
import it.czerwinski.intellij.wavefront.editor.model.GLModel

class GL2Presenter(
    animator: GLAnimatorControl
) : GLPresenter<GL2>,
    GLAnimatorControl by animator,
    GLContext<GL2> by GL2Context {

    private var model: GLModel? = null

    private val cameraModel: GLCameraModel = GLCameraModel(
        distance = CAMERA_DISTANCE,
        angle = CAMERA_ANGLE,
        elevation = CAMERA_ELEVATION,
        fov = FOV
    )

    private val background get() =
        EditorColorsManager.getInstance().globalScheme.defaultBackground

    private lateinit var glu: GLU

    override fun updateModel(newModel: GLModel?) {
        model = newModel
        resume()
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

        glMaterialfv(GL.GL_FRONT, GL2.GL_AMBIENT, AMBIENT_COLOR, 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_DIFFUSE, DIFFUSE_COLOR, 0)
        glMaterialfv(GL.GL_FRONT, GL2.GL_SPECULAR, SPECULAR_COLOR, 0)
        glMaterialf(GL.GL_FRONT, GL2.GL_SHININESS, SHININESS)
    }

    override fun reshape(
        drawable: GLAutoDrawable?,
        x: Int,
        y: Int,
        width: Int,
        height: Int
    ) = drawable.runInGLContext {
        if (width == 0 || height == 0) return@runInGLContext
        val aspect = width.toFloat() / height.toFloat()
        val fovY = cameraModel.fov / aspect.coerceAtMost(1f)

        glViewport(0, 0, width, height)

        glMatrixMode(GL2.GL_PROJECTION)
        glLoadIdentity()
        glu.gluPerspective(fovY, aspect, cameraModel.near, cameraModel.far)

        glMatrixMode(GL2.GL_MODELVIEW)
        glLoadIdentity()

        resume()
    }

    override fun display(drawable: GLAutoDrawable?) = drawable.runInGLContext {
        glClearColor(background)
        glClear(GL2.GL_COLOR_BUFFER_BIT or GL2.GL_DEPTH_BUFFER_BIT)
        glLoadIdentity()
        glu.gluLookAt(
            cameraModel.x, cameraModel.y, cameraModel.z,
            0f, 0f, 0f,
            0f, 0f, 1f
        )
        model?.let { model -> glFaces(model) }
        pause()
    }

    override fun dispose(drawable: GLAutoDrawable?) = Unit

    override fun dispose() {
        if (isAnimating) stop()
    }

    companion object {
        private const val FOV = 50f
        private const val CAMERA_DISTANCE = 5f
        private const val CAMERA_ANGLE = -30f
        private const val CAMERA_ELEVATION = 30f

        private val AMBIENT_COLOR = floatArrayOf(.7f, .7f, .7f, 1f)
        private val DIFFUSE_COLOR = floatArrayOf(.9f, .9f, .9f, 1f)
        private val SPECULAR_COLOR = floatArrayOf(1f, 1f, 1f, 1f)
        private const val SHININESS = 128f

        private val LIGHT_POSITION =
            floatArrayOf(-CAMERA_DISTANCE, CAMERA_DISTANCE, CAMERA_DISTANCE)
    }
}
