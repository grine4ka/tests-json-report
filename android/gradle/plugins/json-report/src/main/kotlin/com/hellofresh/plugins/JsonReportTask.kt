package com.hellofresh.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction
import javax.inject.Inject

abstract class JsonReportTask @Inject constructor(variant: String) : DefaultTask() {

    init {
        group = JavaBasePlugin.VERIFICATION_GROUP
        description = "Creates JSON report for your $variant unit tests"
    }

    /**
     * The report files.
     *
     * Gradle currently fails to pick up the producer tasks of nested provider properties resulting in TS-31797
     * (see https://github.com/gradle/gradle/issues/6619).
     * As a workaround we use @Internal here
     */
    @get:Internal
    abstract val testReportsDirectory: DirectoryProperty

    @TaskAction
    fun run() {
        println("Task $name is run")
        println("Directory name is ${testReportsDirectory.asFile.orNull?.path}")
        // TODO parse xml reports from the dir to JSON
    }
}