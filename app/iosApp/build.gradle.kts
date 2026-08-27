plugins {
    alias(libs.plugins.kotlinMultiplatform)
}

kotlin {
    iosArm64()
    iosSimulatorArm64()

    targets.withType<org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget>().configureEach {
        binaries.framework {
            baseName = "IosApp"
            isStatic = true
            export(project(":feature:home:presentation"))
            export(project(":feature:detail:presentation"))
        }
    }

    sourceSets {
        iosMain.dependencies {
            api(project(":feature:home:presentation"))
            api(project(":feature:detail:presentation"))
            implementation(project(":core:model"))
            implementation(project(":core:domain"))
            implementation(project(":core:network"))
            implementation(project(":core:database"))
            implementation(project(":core:datastore"))
            implementation(project(":core:navigation"))
            implementation(project(":core:presentation"))
            implementation(project(":feature:home:data"))
            implementation(project(":feature:home:domain"))
            implementation(project(":feature:home:contract"))
            implementation(project(":feature:detail:data"))
            implementation(project(":feature:detail:domain"))
            implementation(project(":feature:detail:contract"))
            implementation(libs.koin.core)
            implementation(libs.koin.core.viewmodel)
            implementation(libs.ktor.client.darwin)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
        }
    }
}
