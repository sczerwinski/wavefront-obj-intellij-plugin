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

import com.intellij.openapi.Disposable
import com.intellij.openapi.editor.colors.EditorColorsManager
import com.intellij.openapi.progress.util.BackgroundTaskUtil
import com.intellij.openapi.project.Project
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
import it.czerwinski.intellij.wavefront.editor.gl.textures.TexturesManager
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Base 3D scene.
 */
abstract class BaseScene(
    private val profile: GLProfile,
    protected val parent: Disposable,
    animatorControl: GLAnimatorControl,
    protected val errorLog: ErrorLog
) : GlimpseCallback,
    GLAnimatorControl by animatorControl {

    protected var width: Int = 1
    protected var height: Int = 1
    protected var aspect: Float = 1f

    private val background
        get() = EditorColorsManager.getInstance().globalScheme.defaultBackground

    private val texturesManager = TexturesManager()

    protected abstract val mipmapping: Boolean

    private val loadingListeners = mutableListOf<LoadingListener>()

    /**
     * Loads texture image from a file with given [filename] before creating a texture.
     */
    private fun prepareTexture(project: Project, filename: String) {
        try {
            texturesManager.prepare(profile, project, filename)
        } catch (expected: Throwable) {
            errorLog.addError(
                WavefrontObjBundle.message("editor.common.preview.prepareTexture.error", filename),
                expected
            )
        }
    }

    /**
     * Returns texture created from this file.
     */
    private fun getTexture(gl: GlimpseAdapter, filename: String): Texture? = try {
        texturesManager[gl, filename, mipmapping]
    } catch (ignored: Throwable) {
        null
    }

    final override fun onCreate(gl: GlimpseAdapter) {
        gl.glClearDepth(depth = 1f)
        gl.glDepthTest(DepthTestFunction.LESS_OR_EQUAL)

        gl.glEnableBlending()
        gl.glBlendingFunction(BlendingFactorFunction.SOURCE_ALPHA, BlendingFactorFunction.ONE_MINUS_SOURCE_ALPHA)

        gl.glEnableLineSmooth()
        gl.glEnableProgramPointSize()

        initialize(gl)
    }

    /**
     * Initializes the scene.
     *
     * Concrete implementation should create meshes in this method.
     */
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

    /**
     * Performs computations after the viewport has been resized.
     *
     * Concrete implementation should update camera and lens.
     */
    protected abstract fun afterResize(gl: GlimpseAdapter)

    /**
     * Handles viewport resize errors.
     */
    protected abstract fun onResizeError(gl: GlimpseAdapter, error: Throwable)

    /**
     * Resumes scene rendering.
     */
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
        } finally {
            if (isStarted) pause()
        }
    }

    /**
     * Performs actual scene rendering.
     *
     * Buffers have been cleared before this method is called.
     */
    protected abstract fun doRender(gl: GlimpseAdapter)

    /**
     * Handles scene rendering errors.
     */
    protected abstract fun onRenderError(gl: GlimpseAdapter, error: Throwable)

    final override fun onDestroy(gl: GlimpseAdapter) {
        try {
            texturesManager.dispose(gl)
            dispose(gl)
        } catch (expected: Throwable) {
            onDestroyError(gl, expected)
        }
    }

    /**
     * Disposes OpenGL objects.
     *
     * Concrete implementation should dispose meshes created in [initialize].
     */
    protected abstract fun dispose(gl: GlimpseAdapter)

    /**
     * Handles errors that occurred while disposing OpenGL objects.
     */
    protected abstract fun onDestroyError(gl: GlimpseAdapter, expected: Throwable)

    /**
     * Adds given loading status [listener] to this scene.
     */
    fun addLoadingListener(listener: LoadingListener) {
        loadingListeners += listener
    }

    /**
     * Removes given loading status [listener] from this scene.
     */
    fun removeLoadingListener(listener: LoadingListener) {
        loadingListeners -= listener
    }

    protected fun notifyLoading(loading: Boolean) {
        for (listener in loadingListeners) {
            listener.onLoading(scene = this, loading)
        }
    }

    /**
     * Loading status listener.
     */
    fun interface LoadingListener {

        /**
         * Called when [loading] status of given [scene] has changed.
         */
        fun onLoading(scene: BaseScene, loading: Boolean)
    }

    /**
     * Cacheable texture provider.
     */
    protected inner class TextureProvider(
        private val project: Project,
        private val paths: List<String>,
        private val fallbackTexture: () -> Texture
    ) {

        /**
         * Returns `true` if this texture provider always uses fallback texture.
         */
        val isFallback: Boolean get() = paths.isEmpty()

        private val isPreparing = AtomicBoolean(false)

        /**
         * Prepares this texture.
         */
        fun prepare() {
            if (isPreparing.compareAndSet(false, true)) {
                for (path in paths) {
                    prepareTexture(project, path)
                }
                isPreparing.set(false)
            }
        }

        /**
         * Returns this texture if prepared.
         *
         * If the texture is not prepared, initializes its preparation.
         */
        fun get(gl: GlimpseAdapter): Texture {
            val texture = paths.asSequence()
                .mapNotNull { path -> getTexture(gl, path) }
                .firstOrNull()
            if (paths.isNotEmpty() && texture == null) {
                notifyLoading(loading = true)
                BackgroundTaskUtil.executeOnPooledThread(parent) {
                    prepare()
                    requestRender()
                    notifyLoading(loading = false)
                }
            }
            return texture ?: fallbackTexture()
        }
    }
}
