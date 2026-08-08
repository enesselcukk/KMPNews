plugins {
    id("kmpnews.library.compose")
}

kotlin {
    jvm()

    android {
        namespace = "com.example.kmpnews.core.designsystem"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    listOf(
        iosArm64(),
        iosSimulatorArm64(),
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "CoreDesignSystem"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.compose.runtime)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material3)
            implementation(libs.compose.ui)
            implementation(libs.compose.uiToolingPreview)
        }
        androidMain.dependencies {
            implementation(libs.compose.uiTooling)
        }
    }
}

dependencies {
    androidRuntimeClasspath(libs.compose.uiTooling)
}
