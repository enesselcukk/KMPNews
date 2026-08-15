import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.register
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kmpnews.library.kmp")
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }

        val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
        extensions.configure<KotlinMultiplatformExtension> {
            sourceSets.named("commonMain") {
                dependencies {
                    implementation(libs.findLibrary("compose-components-resources").get())
                }
            }
        }

        val cleanPreparedComposeResources = tasks.register<CleanDuplicateComposeResourcesTask>(
            "cleanDuplicatePreparedComposeResources",
        ) {
            resourceDirectory.set(
                layout.buildDirectory.dir(
                    "generated/compose/resourceGenerator/preparedResources/commonMain/composeResources",
                ),
            )
        }

        val cleanAndroidComposeResources = tasks.register<CleanDuplicateComposeResourcesTask>(
            "cleanDuplicateAndroidComposeResources",
        ) {
            resourceDirectory.set(
                layout.buildDirectory.dir(
                    "generated/assets/copyAndroidMainComposeResourcesToAndroidAssets/composeResources",
                ),
            )
        }

        tasks.matching { it.name == "prepareComposeResourcesTaskForCommonMain" }.configureEach {
            finalizedBy(cleanPreparedComposeResources)
        }

        tasks.matching { it.name == "copyAndroidMainComposeResourcesToAndroidAssets" }.configureEach {
            finalizedBy(cleanPreparedComposeResources, cleanAndroidComposeResources)
        }
    }
}
