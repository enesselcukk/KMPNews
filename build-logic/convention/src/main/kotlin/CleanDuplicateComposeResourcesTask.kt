import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.TaskAction

abstract class CleanDuplicateComposeResourcesTask : DefaultTask() {

    @get:Internal
    abstract val resourceDirectory: DirectoryProperty

    @TaskAction
    fun clean() {
        val root = resourceDirectory.orNull?.asFile ?: return
        if (!root.exists()) return

        root.walkTopDown()
            .filter { file -> file.isFile && DUPLICATE_COMPOSE_RESOURCE_FILE.matches(file.name) }
            .forEach { file -> file.delete() }
    }
}

private val DUPLICATE_COMPOSE_RESOURCE_FILE =
    Regex("""\.commonMain \d+\.cvr$""")
