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
    sourceSets["commonMain"].apply {
        dependencies {
            implementation(kotlin("stdlib-common"))
        }
    }
    sourceSets["commonTest"].apply {
        dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }
    sourceSets.create("androidMain") {
        dependencies {
            implementation(kotlin("stdlib-jdk8"))
        }
    }
    sourceSets.create("androidTest") {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
        }
    }

    jvm("android")

    val desktopTarget = when {
        os.isWindows -> mingwX64("desktop")
        os.isMacOsX -> macosX64("desktop")
        os.isLinux -> linuxX64("desktop")
        else -> throw Error("Unknown host")
    }
    configure(listOf(desktopTarget)) {
        binaries {
            sharedLib(listOf(DEBUG))
        }
    }
}
