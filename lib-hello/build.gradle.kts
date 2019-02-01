plugins {
    kotlin("multiplatform")
}

repositories {
    mavenCentral()
}

// workaround for https://youtrack.jetbrains.com/issue/KT-27170
configurations.create("compileClasspath")

val os = org.gradle.internal.os.OperatingSystem.current()!!

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(kotlin("stdlib-common"))
        }
    }
    val commonTest by sourceSets.getting {
        dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }

    jvm("android")
    val androidMain by sourceSets.getting {
        dependencies {
            implementation(kotlin("stdlib-jdk8"))
        }
    }
    val androidTest by sourceSets.getting {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
        }
    }

    when {
        os.isWindows -> mingwX64("desktop")
        os.isMacOsX -> macosX64("desktop")
        os.isLinux -> linuxX64("desktop")
        else -> throw Error("Unknown host")
    }.binaries {
        sharedLib()
    }
}
