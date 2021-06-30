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

import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.vfs.VirtualFile
import com.jogamp.opengl.GLAnimatorControl
import com.jogamp.opengl.GLProfile
import graphics.glimpse.BlendingFactorFunction
import graphics.glimpse.ClearableBufferType
import graphics.glimpse.DepthTestFunction
import graphics.glimpse.GlimpseAdapter
import graphics.glimpse.GlimpseCallback
import graphics.glimpse.textures.Texture
import graphics.glimpse.types.Vec3
import it.czerwinski.intellij.common.ui.ErrorLog
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.gl.shaders.ProgramExecutorsManager
import it.czerwinski.intellij.wavefront.editor.gl.textures.TexturesManager

abstract class BaseScene(
    private val profile: GLProfile,
    animatorControl: GLAnimatorControl,
    protected val errorLog: ErrorLog
) : GlimpseCallback,
    GLAnimatorControl by animatorControl {

    protected var width: Int = 1
    protected var height: Int = 1
    protected var aspect: Float = 1f

    private val background
        get() = EditorColorsManager.getInstance().globalScheme.defaultBackground

    protected val programExecutorsManager = ProgramExecutorsManager(errorLog)

    private val texturesManager = TexturesManager()

    protected fun prepareTexture(file: VirtualFile) {
        try {
            texturesManager.prepare(profile, file)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.fileTypes.obj.preview.prepareTexture.error", file.name),
                expected
            )
        }
    }

    protected fun VirtualFile.getTexture(gl: GlimpseAdapter): Texture? = try {
        texturesManager[gl, this]
    } catch (expected: Throwable) {
        errorLog.addError(
            WavefrontObjBundle.message("editor.fileTypes.obj.preview.getTexture.error", name),
            expected
        )
        null
    }

    final override fun onCreate(gl: GlimpseAdapter) {
        gl.glClearDepth(depth = 1f)
        gl.glDepthTest(DepthTestFunction.LESS_OR_EQUAL)

        gl.glEnableBlending()
        gl.glBlendingFunction(BlendingFactorFunction.SOURCE_ALPHA, BlendingFactorFunction.ONE_MINUS_SOURCE_ALPHA)

        gl.glEnableLineSmooth()
        gl.glEnableProgramPointSize()

        programExecutorsManager.initialize(gl)

        initialize(gl)
    }

    protected abstract fun initialize(gl: GlimpseAdapter)

    final override fun onResize(gl: GlimpseAdapter, x: Int, y: Int, width: Int, height: Int) {
        try {
            this.width = width
            this.height = height

            if (width == 0 || height == 0) return

            aspect = width.toFloat() / height.toFloat()
            gl.glViewport(width = width, height = height)

            afterResize(gl)
        } catch (expected: Throwable) {
            onResizeError(gl, expected)
        }
    }

    protected abstract fun afterResize(gl: GlimpseAdapter)

    protected abstract fun onResizeError(gl: GlimpseAdapter, error: Throwable)

    protected fun requestRender() {
        if (isStarted && isPaused) resume()
    }

    final override fun onRender(gl: GlimpseAdapter) {
        try {
            gl.glClearColor(Vec3(background))
            gl.glClear(ClearableBufferType.COLOR_BUFFER, ClearableBufferType.DEPTH_BUFFER)
            doRender(gl)
        } catch (expected: Throwable) {
            onRenderError(gl, expected)
        }
    }

    protected abstract fun doRender(gl: GlimpseAdapter)

    protected abstract fun onRenderError(gl: GlimpseAdapter, error: Throwable)

    final override fun onDestroy(gl: GlimpseAdapter) {
        try {
            dispose(gl)
            texturesManager.dispose(gl)
            programExecutorsManager.dispose(gl)
        } catch (expected: Throwable) {
            onDestroyError(gl, expected)
        }
    }

    protected abstract fun dispose(gl: GlimpseAdapter)

    protected abstract fun onDestroyError(gl: GlimpseAdapter, expected: Throwable)
}
