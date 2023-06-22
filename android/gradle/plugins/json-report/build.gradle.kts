plugins {
    `kotlin-dsl`
}

dependencies {
    compileOnly("com.android.tools.build:gradle:7.3.1")
    compileOnly("com.android.tools.build:gradle-api:7.3.1")
    implementation("org.json:json:20230227")
}

gradlePlugin {
    plugins {
        create("JsonReport") {
            id = "com.bykov.json-report"
            implementationClass = "com.bykov.plugins.JsonReportPlugin"
        }
    }
}
