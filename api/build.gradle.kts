java {
    withSourcesJar()
}

dependencies {
    val kotlinPoetVersion: String by project
    api("com.squareup:kotlinpoet:$kotlinPoetVersion")
}

publishing {
    publications {
        create<MavenPublication>("ktgen-api") {
            from(components["java"])
            artifactId = "ktgen-api"
        }
    }
}