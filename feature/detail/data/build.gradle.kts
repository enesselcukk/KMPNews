plugins {
    id("kmpnews.library.kmp")
}

kotlin {
    jvm()

    android {
        namespace = "com.example.kmpnews.feature.detail.data"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":core:model"))
        }
    }
}
