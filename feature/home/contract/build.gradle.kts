plugins {
    id("kmpnews.library.kmp")
}

kotlin {
    jvm()

    android {
        namespace = "com.example.kmpnews.feature.home.contract"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
    }

    sourceSets {
        commonMain.dependencies {
            api(project(":core:model"))
        }
    }
}
