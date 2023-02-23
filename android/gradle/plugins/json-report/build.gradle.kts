plugins {
    `kotlin-dsl`
}

dependencies {
    implementation("com.android.tools.build:gradle:7.3.1")
    implementation("com.android.tools.build:gradle-api:7.3.1")
    implementation("org.json:json:20220924")
}

gradlePlugin {
    plugins {
        create("JsonReport") {
            id = "com.hellofresh.json-report"
            implementationClass = "com.hellofresh.plugins.JsonReportPlugin"
        }
    }
}
