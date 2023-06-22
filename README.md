# Android Unit Tests JSON Report

My motivation was to demonstrate my skills in developing Gradle Plugins.

But **the main task** was to create _a tool that can create json reports for the tests_.

## Possible solutions

The task can be done in several ways that I thought about. Here are some of them:
- Standalone CLI tool
  - Python variant
  - Kotlin variant using [Clikt](https://ajalt.github.io/clikt/)
- Custom Android Test Runner
  - Like [AllureAndroidJUnit4](https://github.com/allure-framework/allure-kotlin/blob/master/allure-kotlin-android/src/main/kotlin/io/qameta/allure/android/runners/AllureAndroidJUnitRunners.kt)
- Using Gradle
  - Gradle task
  - Gradle Plugin with task
  - Using AbstractTestTask methods: [addTestListener](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.AbstractTestTask.html#org.gradle.api.tasks.testing.AbstractTestTask:addTestListener(org.gradle.api.tasks.testing.TestListener)) or [afterSuite](https://docs.gradle.org/current/dsl/org.gradle.api.tasks.testing.AbstractTestTask.html#org.gradle.api.tasks.testing.AbstractTestTask:afterSuite(groovy.lang.Closure)) closure. (This method I found too late)

I stopped on implementing Gradle Plugin with task by reasons:

- Gradle plugin is a good place to hide implementation details.
- Gradle plugin allows to create extension where user can configure some details.
- Gradle plugin can be modified easily, for example if we need to add some new functionality to a task.
- standalone CLI tool is hard to develop in short period of time and I think it is too complex solution for such a simple task. 
- custom android test runner is also a very complex solution.

## Implementation

Implemented Gradle plugin is located in [gradle/plugins/json-report](./gradle/plugins/json-report/src/main/kotlin/com/bykov/plugins/JsonReportPlugin.kt) directory.

Everything that is located in [gradle/plugins](./gradle/plugins) folder is connected to the project using `includedBuild` method in `pluginManagement` block in [settings.gradle](settings.gradle) file.  
As of this now we can develop plugins in such an `includedBuild` project separately from the main app.

The implementation itself is very straightforward.
Using AndroidComponentsExtension it registers a new Gradle [JsonReportTask](./gradle/plugins/json-report/src/main/kotlin/com/bykov/plugins/JsonReportTask.kt) task for every variant-aware `AndroidUnitTest` Gradle task.
Then it configure it to use `junitXml` report as an input and make `AndroidUnitTest` task be finalized by newly registered task.

The [JsonReportTask](./gradle/plugins/json-report/src/main/kotlin/com/bykov/plugins/JsonReportTask.kt) is also straightforward. 
It gets all files from input directory, filters them by `xml` extension.  
Then converts its XML contents to JSON using [JSON-java library](https://github.com/stleary/JSON-java) and outputs either to file or to system output.

The way task outputs the result can be configured using [JsonReportExtension](./gradle/plugins/json-report/src/main/kotlin/com/bykov/plugins/JsonReportExtension.kt)

## Configuration

The plugin can be configured through the `jsonReport` extension, which can be added to the `build.gradle` file.

```groovy
plugins {
    id "com.bykov.json-report"
}

jsonReport {
    // Enable writing output to a file. Disabled by default.
    fileOutput.enabled = true
    // Disable writing output to system output. Enabled by default.
    systemOutput.enabled = false
}
```

## How to use it

The best thing of this implementation is that you have to do nothing after applying and configuring Gradle plugin.

Android Unit Test tasks are finalized by `JsonReportTask`.  
This means that after executing for example `:app:testDebugUnitTest` task either you'll see json output in your console or you'll find `json-report.json` file inside `build/reports/tests/testDebugUnitTest` directory.

## Points of growth

This solution has cons and thus points-of-growth.

1. It is not crossplatform. I mean it is applicable only for Android, not iOS.
2. Output directory path and output file name can be made configurable.
3. Filter fields for JSON report. We don't need all info about test suites that can be found in junit xml reports.