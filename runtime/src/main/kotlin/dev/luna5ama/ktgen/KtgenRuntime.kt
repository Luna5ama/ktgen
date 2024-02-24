package dev.luna5ama.ktgen

import org.jetbrains.kotlin.konan.file.File
import java.nio.file.Paths
import java.util.*

object KtgenRuntime {
    @JvmStatic
    fun main(args: Array<String>) {
        val inputs = System.getenv("INPUT_PATHS").split(File.pathSeparator).map { Paths.get(it) }
        val outputDir = Paths.get(args[0])
        with(outputDir.toFile()) {
            deleteRecursively()
            mkdirs()
        }
        val services = ServiceLoader.load(KtgenProcessor::class.java)
        for (service in services) {
            service.process(inputs, outputDir)
        }
    }
}