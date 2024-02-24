dependencies {
    val kotlinPoetVersion: String by project
    implementation(project(":api"))
    runtimeOnly("com.squareup:kotlinpoet:$kotlinPoetVersion")
}

publishing {
    publications {
        create<MavenPublication>("ktgen-runtime") {
            from(components["java"])
            artifactId = "ktgen-runtime"
        }
    }
}