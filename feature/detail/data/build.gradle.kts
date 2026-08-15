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
            implementation(project(":core:data"))
            implementation(project(":core:domain"))
            implementation(project(":core:model"))
            implementation(project(":feature:detail:domain"))
            implementation(project(":feature:home:domain"))
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.koin.core)
        }
    }
}
