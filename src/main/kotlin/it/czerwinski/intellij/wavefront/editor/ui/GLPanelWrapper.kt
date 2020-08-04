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

import com.intellij.openapi.Disposable
import com.intellij.openapi.application.ModalityState
import com.intellij.openapi.application.invokeLater
import com.intellij.openapi.application.runReadAction
import com.intellij.openapi.util.Disposer
import com.jogamp.opengl.awt.GLJPanel
import com.jogamp.opengl.util.FPSAnimator
import it.czerwinski.intellij.wavefront.WavefrontObjBundle
import it.czerwinski.intellij.wavefront.editor.model.GLModel
import it.czerwinski.intellij.wavefront.editor.model.GLModelFactory
import it.czerwinski.intellij.wavefront.lang.psi.ObjFile
import java.awt.BorderLayout
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.SwingConstants

class GLPanelWrapper : JPanel(BorderLayout()), Disposable {

    private val placeholder = JLabel(
        WavefrontObjBundle.message("editor.fileTypes.obj.preview.placeholder"),
        SwingConstants.CENTER
    )

    private val modalityState = ModalityState.stateForComponent(this)

    private lateinit var presenter: GLPresenter<*>

    private var model: GLModel? = null
        set(value) {
            field = value
            invokeLater(modalityState) {
                if (::presenter.isInitialized) {
                    presenter.updateModel(value)
                }
            }
        }

    init {
        invokeLater(modalityState, ::attachPlaceholder)
    }

    private fun attachPlaceholder() {
        removeAll()
        add(placeholder, BorderLayout.CENTER)
        invalidate()

        invokeLater(modalityState, ::attachJPanel)
    }

    private fun attachJPanel() {
        val canvas = GLJPanel()
        val animator = FPSAnimator(canvas, DEFAULT_FPS_LIMIT)
        presenter = GL2Presenter(animator)
        presenter.updateModel(model)
        canvas.addGLEventListener(presenter)

        add(canvas, BorderLayout.CENTER)
        remove(placeholder)
        invalidate()

        presenter.start()
    }

    fun updateObjFile(objFile: ObjFile?) {
        runReadAction { createModel(objFile) }
    }

    private fun createModel(objFile: ObjFile?) {
        model = objFile?.let(GLModelFactory::create)
        if (::presenter.isInitialized) {
            invokeLater(modalityState) {
                presenter.updateModel(model)
            }
        }
    }

    override fun dispose() {
        Disposer.dispose(presenter)
    }

    companion object {
        private const val DEFAULT_FPS_LIMIT = 10
    }
}
