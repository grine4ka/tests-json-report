package com.hellofresh.plugins

import org.gradle.api.Project
import javax.inject.Inject

open class JsonReportExtension @Inject constructor(
    val fileOutput: JsonReportOutput.JsonReportFileOutput = JsonReportOutput.JsonReportFileOutput,
    val systemOutput: JsonReportOutput.JsonReportSystemOutput = JsonReportOutput.JsonReportSystemOutput
) {

    companion object {
        fun create(project: Project): JsonReportExtension {
            return project.extensions.create("jsonReport", JsonReportExtension::class.java)
        }
    }
}

sealed class JsonReportOutput(var enabled: Boolean) {

    object JsonReportFileOutput: JsonReportOutput(false)

    object JsonReportSystemOutput: JsonReportOutput(true)

}
