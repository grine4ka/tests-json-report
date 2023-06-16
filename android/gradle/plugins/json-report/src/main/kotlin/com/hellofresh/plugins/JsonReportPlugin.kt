package com.hellofresh.plugins

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.tasks.factory.AndroidUnitTest
import com.android.builder.core.ComponentType.Companion.UNIT_TEST_PREFIX
import com.android.builder.core.ComponentType.Companion.UNIT_TEST_SUFFIX
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.register

/**
 * A Gradle plugin that converts JUnit XML results of Android unit test tasks to JSON format.
 * This plugin creates a new task, test<variantName>JsonReport, for each Android unit test task,
 * where variantName is the name of the variant associated with the task.
 *
 * The plugin can be configured through the `jsonReport` extension, which can be added to the `build.gradle` file.
 *
 * <pre>{@code
 * plugins {
 *     id "com.hellofresh.json-report"
 * }
 *
 * jsonReport {
 *     // Enable writing output to a file. Disabled by default.
 *     fileOutput.enabled = true
 *     // Disable writing output to system output. Enabled by default.
 *     systemOutput.enabled = false
 * }
 * }</pre>
 *
 * For example, if you configure the plugin to write output to a file, and you have the following JUnit XML files:
 *
 * <pre>{@code
 * $buildDir/test-results/testDebugUnitTest/TEST-com.example.MyUnitTest.xml
 * $buildDir/test-results/testDebugUnitTest/TEST-com.example.MyUnitTest2.xml
 * }</pre>
 *
 * The plugin will generate one JSON file:
 *
 * <pre>{@code
 * build/reports/tests/testDebugUnitTest/json-report.json
 * }</pre>
 *
 */
class JsonReportPlugin : Plugin<Project> {

    /**
     * Configures the plugin with the given project.
     *
     * @param project the project to configure
     */
    override fun apply(project: Project): Unit = project.run {
        val extension = JsonReportExtension.create(this)
        val androidComponents = extensions.getByType(AndroidComponentsExtension::class.java)
        // For each variant in the Android components extension, register a new report task
        androidComponents.onVariants { variant ->
            registerReportTask(variant.name, extension)
        }
    }

    /**
     * Registers a new task for generating a JSON report for the given variant.
     * The task is associated with the Android unit test task for the variant.
     *
     * @param variantName the name of the variant to generate a report for
     * @param extension the JsonReportExtension to use for the task
     */
    private fun Project.registerReportTask(variantName: String, extension: JsonReportExtension) {
        // Register a new task with the project for generating a JSON report for the given variant
        val jsonReportTask = tasks.register<JsonReportTask>("test${variantName.capitalized()}JsonReport", variantName, extension)
        // Find the Android unit test task for the variant
        tasks.withType(AndroidUnitTest::class.java)
            .matching { it.name == "$UNIT_TEST_PREFIX${variantName.capitalized()}$UNIT_TEST_SUFFIX" }
            .whenTaskAdded {
                val unitTests = this
                // Configure the JSON report task to use the output directory of the Android unit test task
                jsonReportTask.configure {
                    val reportsDir = unitTests.reports.junitXml.outputLocation
                    testReportsDirectory.set(reportsDir)
                }
                // Make the JSON report task finalize the Android unit test task
                finalizedBy(jsonReportTask)
            }
    }
}