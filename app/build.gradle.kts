plugins {
    kotlin("multiplatform")
    id("com.android.application")
    id("kotlin-android-extensions")
}

repositories {
    google()
    jcenter()
    mavenCentral()
}

android {
    compileSdkVersion(28)
    defaultConfig {
        applicationId = "org.jetbrains.kotlin.mpp_app_android"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 1
        versionName = "1.0"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }
    buildTypes {
        named("release") {
            isMinifyEnabled = false
        }
    }
}

kotlin {
    val commonMain by sourceSets.getting {
        dependencies {
            implementation(kotlin("stdlib-common"))
            implementation(project(":lib-hello"))
        }
    }
    val commonTest by sourceSets.getting {
        dependencies {
            implementation(kotlin("test-common"))
            implementation(kotlin("test-annotations-common"))
        }
    }

    android("android")
    val androidMain by sourceSets.getting {
        dependencies {
            implementation(kotlin("stdlib"))
            implementation("com.android.support:appcompat-v7:28.0.0")
            implementation("com.android.support.constraint:constraint-layout:1.1.3")
        }
    }
    val androidTest by sourceSets.getting {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
            implementation("com.android.support.test:runner:1.0.2")
        }
    }

    val os = org.gradle.internal.os.OperatingSystem.current()!!
    when {
        os.isWindows -> mingwX64("desktop")
        os.isMacOsX -> macosX64("desktop")
        os.isLinux -> linuxX64("desktop")
        else -> throw Error("Unknown host")
    }.binaries.executable {
        entryPoint("sample.main")
        if (os.isWindows) {
            windowsResources("app.rc")
            linkerOpts("-mwindows")
        }
    }
    val desktopMain by sourceSets.getting {
        dependencies {
            implementation("com.github.msink:libui:0.1.2")
        }
    }
}

fun org.jetbrains.kotlin.gradle.plugin.mpp.Executable.windowsResources(rcFileName: String) {
    val taskName = linkTaskName.replaceFirst("link", "windres")
    val inFile = compilation.defaultSourceSet.resources.sourceDirectories.singleFile.resolve(rcFileName)
    val outFile = buildDir.resolve("processedResources/$taskName.res")

    val windresTask = tasks.create<Exec>(taskName) {
        val konanUserDir = System.getenv("KONAN_DATA_DIR") ?: "${System.getProperty("user.home")}/.konan"
        val konanLlvmDir = "$konanUserDir/dependencies/msys2-mingw-w64-x86_64-gcc-7.3.0-clang-llvm-lld-6.0.1/bin"

        inputs.file(inFile)
        outputs.file(outFile)
        commandLine("$konanLlvmDir/windres", inFile, "-D_${buildType.name}", "-O", "coff", "-o", outFile)
        environment("PATH", "$konanLlvmDir;${System.getenv("PATH")}")

        dependsOn(compilation.compileKotlinTask)
    }

    linkTask.dependsOn(windresTask)
    linkerOpts(outFile.toString())
}
