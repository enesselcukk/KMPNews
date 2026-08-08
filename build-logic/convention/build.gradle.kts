plugins {
    `kotlin-dsl`
}

group = "com.example.kmpnews.buildlogic"

java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}

kotlin {
    jvmToolchain(11)
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
    compileOnly(libs.composeCompiler.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("kmpLibrary") {
            id = "kmpnews.library.kmp"
            implementationClass = "KmpLibraryConventionPlugin"
        }
        register("kmpCompose") {
            id = "kmpnews.library.compose"
            implementationClass = "KmpComposeConventionPlugin"
        }
        register("androidApplication") {
            id = "kmpnews.application"
            implementationClass = "AndroidApplicationConventionPlugin"
        }
    }
}
