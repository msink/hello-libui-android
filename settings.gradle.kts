pluginManagement {
    resolutionStrategy {
        eachPlugin {
            if (requested.id.id == "kotlin-multiplatform") {
                useModule("org.jetbrains.kotlin:kotlin-gradle-plugin:${requested.version}")
            }
        }
    }

    repositories {
        mavenCentral()
        maven { url = uri("https://plugins.gradle.org/m2/") }
    }
}

enableFeaturePreview("GRADLE_METADATA")

include(":app")
include(":lib-hello")