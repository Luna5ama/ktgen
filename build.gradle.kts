allprojects {
    group = "dev.luna5ama"
    version = "1.0.0"
}

plugins {
    kotlin("jvm")
    `java-gradle-plugin`
    id("dev.fastmc.maven-repo").version("1.0.0").apply(false)
}

allprojects {
    apply {
        plugin("java")
        plugin("kotlin")
        plugin("dev.fastmc.maven-repo")
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        val kotlinVersion: String by project
        implementation("org.jetbrains.kotlin:kotlin-gradle-plugin:$kotlinVersion")
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(8))
        }
    }
}

subprojects {
    base {
        archivesName.set("${rootProject.name}-${project.name}")
    }
}

java {
    withSourcesJar()
}

gradlePlugin {
    plugins {
        create("ktgen") {
            id = "dev.luna5ama.ktgen"
            displayName = "ktgen"
            implementationClass = "dev.luna5ama.ktgen.KtgenPlugin"
        }
    }
}

tasks {
    processResources {
        expand("version" to project.version)
    }
}