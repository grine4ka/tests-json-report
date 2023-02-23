package com.hellofresh.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.gradle.configurationcache.extensions.capitalized
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import javax.inject.Inject

private const val PRETTY_INDENTATION = 4

private const val JSON_TESTSUITE_FIELD_NAME = "testsuite"
private const val JSON_TESTSUITES_FIELD_NAME = "testsuites"

private const val XML_FILE_EXTENSION = "xml"

private const val JSON_REPORT_FILE_NAME = "json-report.json"

abstract class JsonReportTask @Inject constructor(
    private val variant: String,
    private val jsonReportExtension: JsonReportExtension
) : DefaultTask() {

    init {
        group = JavaBasePlugin.VERIFICATION_GROUP
        description = "Creates JSON report for your $variant unit tests"
    }

    /**
     * The report files.
     *
     * Gradle currently fails to pick up the producer tasks of nested provider properties
     * (see https://github.com/gradle/gradle/issues/6619).
     * As a workaround I use @Internal here
     */
    @get:Internal
    abstract val testReportsDirectory: DirectoryProperty

    @TaskAction
    fun run() {
        val testSuitesArray = JSONArray()
        testReportsDirectory.asFile.get().walk()
            .filter { it.isFile && it.extension == XML_FILE_EXTENSION }
            .forEach {
                val testSuiteJson = XML.toJSONObject(it.reader())
                testSuitesArray.put(testSuiteJson.get(JSON_TESTSUITE_FIELD_NAME))
            }
        val testSuitesJsonObject = JSONObject()
        testSuitesJsonObject.put(JSON_TESTSUITES_FIELD_NAME, testSuitesArray)
        val jsonString = testSuitesJsonObject.toString(PRETTY_INDENTATION)
        if (jsonReportExtension.fileOutput.enabled) {
            val output = project.layout.buildDirectory
                .dir("reports/tests/test${variant.capitalized()}UnitTest")
                .get().asFile

            if (!output.exists()) {
                output.mkdirs()
            }
            output.resolve(JSON_REPORT_FILE_NAME).writeText(jsonString)
        }
        if (jsonReportExtension.systemOutput.enabled) {
            println(jsonString)
        }
    }
}