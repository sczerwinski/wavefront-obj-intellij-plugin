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

import com.intellij.ui.JBSplitter
import com.intellij.ui.components.JBScrollPane
import javax.swing.JComponent

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
    ErrorLog {

    private val model: ErrorLogTreeModel = ErrorLogTreeModel()

    /**
     * The component displayed above the error log.
     */
    var component: JComponent
        get() = firstComponent
        set(value) {
            firstComponent = value
            invalidate()
        }

    init {
        dividerWidth = DIVIDER_WIDTH
        super.setSecondComponent(JBScrollPane(ErrorLogTree(model)))
    }

    override fun setSecondComponent(component: JComponent?) {
        throw UnsupportedOperationException("Cannot set second component")
    }

    override fun addError(entry: ErrorLog.Entry) {
        model.addError(entry)
        secondComponent.isVisible = true
        invalidate()
    }

    override fun clearErrors() {
        model.clearErrors()
        secondComponent.isVisible = false
        invalidate()
    }

    companion object {
        private const val DEFAULT_SPLIT_PROPORTION = 0.7f
        private const val MIN_SPLIT_PROPORTION = 0.15f
        private const val MAX_SPLIT_PROPORTION = 0.85f
        private const val DIVIDER_WIDTH = 3
    }
}
