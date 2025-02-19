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

package it.czerwinski.intellij.common.ui

import com.intellij.openapi.actionSystem.ActionPlaces
import com.intellij.openapi.actionSystem.ActionToolbar
import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBScrollPane
import java.awt.BorderLayout
import javax.swing.JComponent
import javax.swing.JPanel

/**
 * A splitter displaying error log tree below its [component].
 */
open class ErrorLogSplitter :
    JBSplitter(
        true,
        DEFAULT_SPLIT_PROPORTION,
        MIN_SPLIT_PROPORTION,
        MAX_SPLIT_PROPORTION
    ),
    ErrorLogContainer,
    ErrorLog {

    private val model: ErrorLogTreeModel = ErrorLogTreeModel()

    private val previewPanel = JPanel(BorderLayout())

    /**
     * The component displayed above the error log.
     */
    var component: JComponent? = null
        set(value) {
            if (field != null) {
                previewPanel.remove(field)
            }
            if (value != null) {
                previewPanel.add(value, BorderLayout.CENTER)
            }
            field = value
            invalidate()
        }

    private var isErrorLogVisible: Boolean = true

    private val myFloatingActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(FLOATING_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(this)
            .build()

    private val myActionToolbar: ActionToolbar =
        ActionToolbarBuilder()
            .setActionGroupId(TOOLBAR_ACTIONS_GROUP_ID)
            .setPlace(ActionPlaces.EDITOR_TOOLBAR)
            .setTargetComponent(this)
            .build()

    override val hasErrors: Boolean
        get() = model.hasErrors

    override val errorsCount: Int
        get() = model.errorsCount

    init {
        dividerWidth = DIVIDER_WIDTH

        previewPanel.add(myFloatingActionToolbar.component, BorderLayout.AFTER_LAST_LINE)
        firstComponent = previewPanel

        val errorLogPanel = JPanel(BorderLayout())
        errorLogPanel.add(
            EditorToolbarHeader(rightActionToolbar = myActionToolbar),
            BorderLayout.BEFORE_FIRST_LINE
        )
        errorLogPanel.add(
            JBScrollPane(ErrorLogTree(model)),
            BorderLayout.CENTER
        )

        super.setSecondComponent(errorLogPanel)
    }

    override fun setSecondComponent(component: JComponent?) {
        throw UnsupportedOperationException("Cannot set second component")
    }

    override fun setErrorLogVisibility(visible: Boolean) {
        isErrorLogVisible = visible
        updateLogTreeVisibility()
        myFloatingActionToolbar.updateActionsAsync()
        myActionToolbar.updateActionsAsync()
    }

    private fun updateLogTreeVisibility() {
        secondComponent.isVisible = hasErrors && isErrorLogVisible
        myFloatingActionToolbar.component.isVisible = hasErrors && !isErrorLogVisible
        invalidate()
    }

    override fun addError(entry: ErrorLog.Entry) {
        model.addError(entry)
        updateLogTreeVisibility()
    }

    override fun clearErrors() {
        model.clearErrors()
        updateLogTreeVisibility()
    }

    companion object {
        private const val FLOATING_ACTIONS_GROUP_ID = "PreviewComponent.ErrorLog.Floating"
        private const val TOOLBAR_ACTIONS_GROUP_ID = "PreviewComponent.ErrorLog.Toolbar"

        private const val DEFAULT_SPLIT_PROPORTION = 0.7f
        private const val MIN_SPLIT_PROPORTION = 0.15f
        private const val MAX_SPLIT_PROPORTION = 0.85f
        private const val DIVIDER_WIDTH = 3
    }
}
