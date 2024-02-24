package dev.luna5ama.ktgen

import java.nio.file.Path

interface KtgenProcessor {
    fun process(inputs: List<Path>, outputDir: Path)
}