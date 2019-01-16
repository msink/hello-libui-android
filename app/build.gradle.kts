plugins {
    id("kotlin-multiplatform")
    id("com.android.application")
    id("kotlin-android-extensions")
}

repositories {
    google()
    jcenter()
    maven { url = uri("https://dl.bintray.com/kotlin/kotlin-eap") }
    maven { url = uri("https://dl.bintray.com/msink/kotlin-dev") }
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

dependencies {
    implementation(project(":lib-hello"))
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    androidTestImplementation("com.android.support.test:runner:1.0.2")
}

val os = org.gradle.internal.os.OperatingSystem.current()!!

val resourcesDir = "$projectDir/src/desktopMain/resources"
val windowsResources = "$buildDir/resources/app.res"

val compileWindowsResources by tasks.registering(Exec::class) {
    onlyIf { os.isWindows }

    val konanUserDir = System.getenv("KONAN_DATA_DIR") ?: "${System.getProperty("user.home")}/.konan"
    val konanLlvmDir = "$konanUserDir/dependencies/msys2-mingw-w64-x86_64-gcc-7.3.0-clang-llvm-lld-6.0.1/bin"
    val rcFile = file("$resourcesDir/app.rc")

    inputs.file(rcFile)
    outputs.file(file(windowsResources))
    commandLine("cmd", "/c", "windres", rcFile, "-O", "coff", "-o", windowsResources)
    environment("PATH", "c:/msys64/mingw64/bin;$konanLlvmDir;${System.getenv("PATH")}")
}

kotlin {
    // common
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

    // android
    sourceSets.create("androidMain") {
        dependencies {
            implementation(kotlin("stdlib"))
        }
    }
    sourceSets.create("androidTest") {
        dependencies {
            implementation(kotlin("test"))
            implementation(kotlin("test-junit"))
        }
    }
    android("android")

    // desktop
    sourceSets.create("desktopMain") {
        dependencies {
            implementation(project(":lib-hello"))
            implementation("com.github.msink:libui:0.2.0-dev")
        }
    }
    val desktopTarget = when {
        os.isWindows -> mingwX64("desktop")
        os.isMacOsX -> macosX64("desktop")
        os.isLinux -> linuxX64("desktop")
        else -> throw Error("Unknown host")
    }
    configure(listOf(desktopTarget)) {
        binaries {
            executable(listOf(DEBUG)) {
                entryPoint("sample.main")
                if (os.isWindows) {
                    tasks.named("compileKotlinDesktop") { dependsOn(compileWindowsResources) }
                    linkerOpts(windowsResources, "-mwindows")
                }
            }
        }
    }
}
