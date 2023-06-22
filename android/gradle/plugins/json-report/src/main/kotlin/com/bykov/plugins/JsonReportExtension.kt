package com.bykov.plugins

import org.gradle.api.Project
import javax.inject.Inject

/**
 * Extension for configuring the JsonReportPlugin.
 *
 * @property fileOutput configure file output
 * @property systemOutput configure system output
*/
open class JsonReportExtension @Inject constructor(
    val fileOutput: JsonReportOutput.JsonReportFileOutput = JsonReportOutput.JsonReportFileOutput,
    val systemOutput: JsonReportOutput.JsonReportSystemOutput = JsonReportOutput.JsonReportSystemOutput
) {

    companion object {
        /**
         * Creates a new JsonReportExtension for the given project.
         *
         * @param project the project to create the extension for
         * @return the new JsonReportExtension
         */
        fun create(project: Project): JsonReportExtension {
            return project.extensions.create("jsonReport", JsonReportExtension::class.java)
        }
    }
}

sealed class JsonReportOutput(
    /**
     * Enables writing output to a specific sources.
     */
    var enabled: Boolean
) {

    /**
     * Writes json report to a file located in <code>build/reports/tests</code> folder.
     * Disabled by default.
     */
    object JsonReportFileOutput: JsonReportOutput(false)

    /**
     * Writes json report to system output.
     * Enabled by default.
     */
    object JsonReportSystemOutput: JsonReportOutput(true)

}
