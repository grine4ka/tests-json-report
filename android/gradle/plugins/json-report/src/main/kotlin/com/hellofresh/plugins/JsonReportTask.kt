package com.hellofresh.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import org.json.JSONArray
import org.json.JSONObject
import org.json.XML
import javax.inject.Inject

private const val PRETTY_INDENTATION = 4

private const val JSON_TESTSUITE_FIELD_NAME = "testsuite"
private const val JSON_TESTSUITES_FIELD_NAME = "testsuites"

abstract class JsonReportTask @Inject constructor(variant: String) : DefaultTask() {

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
            .filter { it.isFile && it.extension == "xml"}
            .forEach {
                val testSuiteJson = XML.toJSONObject(it.reader())
                testSuitesArray.put(testSuiteJson.get(JSON_TESTSUITE_FIELD_NAME))
            }
        val testSuitesJsonObject = JSONObject()
        testSuitesJsonObject.put(JSON_TESTSUITES_FIELD_NAME, testSuitesArray)
        // TODO add plugin extension to configure output: either file or systemoutput
        println(testSuitesJsonObject.toString(PRETTY_INDENTATION))
    }
}