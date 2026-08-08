plugins {
    id("kmpnews.library.compose")
}

kotlin {
    jvm()

    android {
        namespace = "com.example.kmpnews.feature.news"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "FeatureNews"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":app:ui-components"))
            implementation(project(":core:model"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
    }
}
