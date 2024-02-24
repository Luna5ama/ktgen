package dev.luna5ama.ktgen

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.tasks.Jar

class KtgenPlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val sourceSetContainer = project.extensions.getByType(SourceSetContainer::class.java)
        val srcDir = project.layout.buildDirectory.dir("generated/sources/ktgen/main/kotlin")

        val ktgenInput = project.configurations.create("ktgenInput")
        val ktgenImpl = project.configurations.create("ktgen")

        project.dependencies.add("ktgen", "dev.luna5ama:ktgen-api:$version")
        project.dependencies.add("ktgen", "dev.luna5ama:ktgen-runtime:$version")

        sourceSetContainer.named("main").configure { main ->
            main.java.srcDir(srcDir)
            project.tasks.named(main.getCompileTaskName("kotlin")).configure {
                it.dependsOn("ktgen")
            }
        }

        val ktgenTask = project.tasks.register("ktgen", KtgenTask::class.java) { task ->
            task.inputFiles.set(project.files(ktgenInput.elements.map { set ->
                set.map { it.asFile }.map { if (it.isDirectory) it else project.zipTree(it) }
            }))
            task.runtimeClasspath.set(ktgenImpl)
            task.outputDir.set(srcDir)
        }

        project.tasks.named("sourcesJar", Jar::class.java) { task ->
            task.dependsOn(ktgenTask)
        }
    }

    companion object {
        val version = KtgenPlugin::class.java.getResource("/ktgen-plugin.version")!!.readText()
    }
}