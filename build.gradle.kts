plugins {
    kotlin("jvm") version "2.1.20"
    // Removed shadow plugin
}

group = "org.oreo"
version = "1.0"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    // Add repository for glowingentities if needed
    maven("https://repo.skytasul.fr/public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.7-R0.1-SNAPSHOT")
    compileOnly("fr.skytasul:glowingentities:1.4.3")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // If nodes.jar is a local dependency not available in any repo
    implementation(files("src/libs/nodes.jar"))
}

tasks {
    processResources {
        val props = mapOf(
            "version" to project.version,
            "description" to "Your plugin description",
            "libraries" to listOf(
                "fr.skytasul:glowingentities:1.4.3",
                // Add other libraries here
            )
        )
        inputs.properties(props)
        filesMatching("plugin.yml") {
            expand(props)
        }
    }
}