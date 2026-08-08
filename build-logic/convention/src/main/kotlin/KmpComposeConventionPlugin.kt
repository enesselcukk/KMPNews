import org.gradle.api.Plugin
import org.gradle.api.Project

class KmpComposeConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) = with(target) {
        with(pluginManager) {
            apply("kmpnews.library.kmp")
            apply("org.jetbrains.compose")
            apply("org.jetbrains.kotlin.plugin.compose")
        }
    }
}
