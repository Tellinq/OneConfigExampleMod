pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.fabricmc.net/")
        maven("https://maven.deftu.dev/releases")
        maven("https://maven.deftu.dev/snapshots")
        maven("https://repo.polyfrost.org/releases")
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

// Configures the root project Gradle name based on the value in `gradle.properties`
rootProject.name = providers.gradleProperty("mod.name").get()
