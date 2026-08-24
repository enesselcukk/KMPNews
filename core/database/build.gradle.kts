import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    id("kmpnews.library.kmp")
    alias(libs.plugins.ksp)
    alias(libs.plugins.room3)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        useEsModules()
    }

    jvm()

    android {
        namespace = "com.example.kmpnews.core.database"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources {
            enable = true
        }
        withHostTest {}
    }

    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreDatabase"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:model"))
            api(libs.room3.runtime)
            implementation(libs.sqlite)
            implementation(libs.kotlinx.coroutines.core)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        androidMain.dependencies {
            implementation(libs.sqlite.bundled)
        }
        jvmMain.dependencies {
            implementation(libs.sqlite.bundled)
        }
        iosMain.dependencies {
            implementation(libs.sqlite.bundled)
        }
        wasmJsMain.dependencies {
            implementation(libs.sqlite.web)
            implementation(libs.kotlinx.browser)
            implementation(
                npm("sqlite-wasm-worker", layout.projectDirectory.dir("sqlite-wasm-worker").asFile),
            )
        }
        getByName("androidHostTest") {
            dependencies {
                implementation(libs.sqlite.bundled.jvm)
            }
        }
    }
}

room3 {
    schemaDirectory(layout.projectDirectory.dir("schemas").asFile.path)
}

dependencies {
    add("kspAndroid", libs.room3.compiler)
    add("kspJvm", libs.room3.compiler)
    add("kspIosArm64", libs.room3.compiler)
    add("kspIosSimulatorArm64", libs.room3.compiler)
    add("kspWasmJs", libs.room3.compiler)
}
