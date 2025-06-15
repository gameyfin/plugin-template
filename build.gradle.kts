import java.util.jar.Manifest

plugins {
    kotlin("jvm") version "2.1.21"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1" // match with your Kotlin version
}

group = "org.gameyfin.plugins"

// Read the version from the MANIFEST.MF file in resources
val manifestFile = file("src/main/resources/MANIFEST.MF")
val manifestVersion: String? = if (manifestFile.exists()) {
    Manifest(manifestFile.inputStream()).mainAttributes.getValue("Plugin-Version")
} else null
version = manifestVersion ?: "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    ksp("care.better.pf4j:pf4j-kotlin-symbol-processing:2.1.21-1.0.2") // match with your Kotlin version
    compileOnly("org.gameyfin:plugin-api:2.0.0.beta1") // match with your Gameyfin version
}

tasks.jar {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    isZip64 = true
    archiveBaseName.set(project.name)

    manifest {
        from("./src/main/resources/MANIFEST.MF")
    }

    from(configurations.runtimeClasspath.get().map { project.zipTree(it) }) {
        exclude("META-INF/*.SF")
        exclude("META-INF/*.DSA")
        exclude("META-INF/*.RSA")
    }

    from(sourceSets["main"].output.classesDirs)
    from(sourceSets["main"].resources)

    // Include logo file under META-INF/resources
    from("src/main/resources") {
        include("logo.*")
        into("META-INF/resources")
    }
}

tasks.register<Copy>("copyDependencyClasses") {
    dependsOn(tasks.jar)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    from(configurations.runtimeClasspath.get().map { project.zipTree(it) }) {
        include("**/*.class")
    }
    into(layout.buildDirectory.get().asFile.resolve("classes/kotlin/main"))
}

tasks.build {
    dependsOn("copyDependencyClasses")
}

kotlin {
    jvmToolchain(21)
}