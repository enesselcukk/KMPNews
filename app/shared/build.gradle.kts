import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("kmpnews.library.compose")
}

kotlin {
    jvm()

    android {
        namespace = "com.example.kmpnews.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        compilerOptions {
            jvmTarget = JvmTarget.JVM_11
        }
        androidResources {
            enable = true
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.compose.uiTooling)
            implementation(libs.koin.android)
        }
        commonMain.dependencies {
            implementation(project(":app:ui-components"))
            implementation(project(":core:model"))
            implementation(project(":core:domain"))
            implementation(project(":core:navigation"))
            implementation(project(":core:network"))
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":feature:home:data"))
            implementation(project(":feature:home:domain"))
            implementation(project(":feature:home:presentation"))
            implementation(project(":feature:detail:domain"))
            implementation(project(":feature:detail:presentation"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.ktor.client.core)
            implementation(libs.koin.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
