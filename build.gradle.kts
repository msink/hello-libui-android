buildscript {
    repositories {
        google()
        jcenter()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.2.1")
    }
}

plugins {
    id("org.jetbrains.kotlin.multiplatform") version "1.3.20" apply false
}

task<Delete>("clean") {
    delete(rootProject.buildDir)
}
