package dev.luna5ama.ktgen

import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.FileCollection
import org.gradle.api.provider.Property
import org.gradle.api.tasks.InputFiles
import org.gradle.api.tasks.JavaExec
import org.gradle.api.tasks.OutputDirectory
import java.io.File
import java.util.jar.Attributes
import java.util.jar.JarOutputStream
import java.util.jar.Manifest

abstract class KtgenTask : JavaExec() {
    @get:InputFiles
    abstract val inputFiles: Property<FileCollection>

    @get:OutputDirectory
    abstract val outputDir: DirectoryProperty

    @get:InputFiles
    abstract val runtimeClasspath: Property<FileCollection>

    init {
        mainClass.set("dev.luna5ama.ktgen.KtgenRuntime")
    }

    override fun exec() {
        val manifestJar = createManifestJar(runtimeClasspath.get())
        try {
            classpath = project.files(manifestJar)
            environment("INPUT_PATHS", inputFiles.get().asPath)
            args = listOf(outputDir.asFile.get().absolutePath)
            super.exec()
        } finally {
            manifestJar.delete()
        }
    }

    private fun createManifestJar(classpath: FileCollection): File {
        val manifestJar = File.createTempFile("ktgen-classpath", ".jar")

        // Build the Class-Path manifest attribute as space-separated URIs
        val classPathValue = classpath.files.joinToString(" ") { file ->
            file.toURI().toString()
        }

        val manifest = Manifest()
        manifest.mainAttributes[Attributes.Name.MANIFEST_VERSION] = "1.0"
        manifest.mainAttributes[Attributes.Name.CLASS_PATH] = classPathValue

        JarOutputStream(manifestJar.outputStream(), manifest).close()

        return manifestJar
    }
}