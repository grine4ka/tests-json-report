package com.hellofresh.plugins

import com.android.build.api.variant.AndroidComponentsExtension
import com.android.build.gradle.tasks.factory.AndroidUnitTest
import com.android.builder.core.ComponentType.Companion.UNIT_TEST_PREFIX
import com.android.builder.core.ComponentType.Companion.UNIT_TEST_SUFFIX
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.configurationcache.extensions.capitalized
import org.gradle.kotlin.dsl.register

class JsonReportPlugin : Plugin<Project> {

    override fun apply(project: Project) {
        val androidComponents = project.extensions.getByType(AndroidComponentsExtension::class.java)
        androidComponents.onVariants { variant ->
            project.registerReportTask(variant.name)
        }
    }

    private fun Project.registerReportTask(variantName: String) {
        val jsonReportTask = tasks.register<JsonReportTask>("test${variantName.capitalized()}JsonReport", variantName)
        tasks.withType(AndroidUnitTest::class.java)
            .matching { it.name == "$UNIT_TEST_PREFIX${variantName.capitalized()}$UNIT_TEST_SUFFIX" }
            .whenTaskAdded {
                val unitTests = this
                jsonReportTask.configure {
                    val reportsDir = unitTests.reports.junitXml.outputLocation
                    testReportsDirectory.set(reportsDir)
                }
                finalizedBy(jsonReportTask)
            }
    }
}