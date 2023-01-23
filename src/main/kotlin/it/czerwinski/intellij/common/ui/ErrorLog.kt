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

import com.jetbrains.rd.util.string.printToString

/**
 * A common interface for error log.
 */
interface ErrorLog {

    /**
     * Adds an [error] with a given [message] to the log.
     */
    fun addError(message: String, error: Throwable) {
        addError(Entry(message, error))
    }

    /**
     * Adds an error [entry] to the log.
     */
    fun addError(entry: Entry)

    /**
     * Clears all errors in the log
     */
    fun clearErrors()

    /**
     * Error log entry.
     */
    data class Entry(
        /**
         * Descriptive error message.
         */
        val message: String,

        /**
         * The error.
         */
        val error: Throwable
    ) {

        /**
         * Entry headline.
         */
        val headline: String get() = "$message: ${error.message}"

        /**
         * Error stack trace.
         */
        val stackTrace: String get() = error.printToString()
    }
}
