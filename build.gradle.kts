plugins {
    kotlin("jvm") version "2.1.21"
    id("com.google.devtools.ksp") version "2.1.21-2.0.1"
}

group = "de.grimsi.gameyfinplugins"
version = "1.0.0"

repositories {
    mavenCentral()
    mavenLocal()
}

dependencies {
    // Kotlin Symbol Processing (KSP) for PF4J
    ksp("care.better.pf4j:pf4j-kotlin-symbol-processing:2.1.21-1.0.2")

    // Gameyfin Plugin API
    compileOnly("de.grimsi.gameyfin:plugin-api:2.0.0-SNAPSHOT")
}

kotlin {
    jvmToolchain(21)
}