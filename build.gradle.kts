plugins {
    id("net.fabricmc.fabric-loom") version "1.17-SNAPSHOT"
    id("dev.deftu.gradle.bloom") version "0.2.0" // Replaces tokens such as `@MOD_VERSION@` in source files
    id("me.modmuss50.mod-publish-plugin") version "2.2.0" // Publishes to Modrinth and CurseForge
}

val modid = property("mod.id").toString()
val modname = property("mod.name").toString()
val modversion = property("mod.version").toString()
val mcversion = property("minecraft_version").toString()
val versionrange = property("minecraft_version_range").toString()
val loaderversion = property("fabric_loader_version").toString()
val oneconfigversion = property("oneconfig_version").toString()

group = property("mod.group").toString()
version = "$modversion+$mcversion"
base.archivesName = modid

val javaVersion = 25

repositories {
    fun strictMaven(url: String, alias: String, vararg groups: String) = exclusiveContent {
        forRepository { maven(url) { name = alias } }
        filter { groups.forEach(::includeGroup) }
    }

    mavenLocal()
    mavenCentral()
    google()
    maven("https://repo.polyfrost.org/releases") { name = "Polyfrost Releases" }
    maven("https://repo.polyfrost.org/snapshots") { name = "Polyfrost Snapshots" }
    maven("https://central.sonatype.com/repository/maven-snapshots") {
        name = "Sonatype Snapshots"
        content { includeGroup("net.kyori") }
    }
    maven("https://maven.cloverclient.com/releases") {
        content { includeGroup("pl.tomgirl") }
    }
    strictMaven("https://maven.deftu.dev/releases", "Deftu", "dev.deftu")
    strictMaven("https://maven.terraformersmc.com/", "TerraformersMC", "com.terraformersmc")
    strictMaven("https://maven.fabricmc.net/", "FabricMC", "net.fabricmc")
    strictMaven("https://api.modrinth.com/maven", "Modrinth", "maven.modrinth")
}

// Minecraft 26.1+ is no longer obfuscated, so no mappings are needed and mods are plain `implementation` dependencies
dependencies {
    minecraft("com.mojang:minecraft:$mcversion")

    implementation("net.fabricmc:fabric-loader:$loaderversion")
    implementation("net.fabricmc.fabric-api:fabric-api:${property("fabric_api_version")}")
    implementation("org.polyfrost.oneconfig:$mcversion-fabric:$oneconfigversion")
    for (module in arrayOf("commands", "config", "config-impl", "events", "internal", "ui", "utils", "hud")) {
        implementation("org.polyfrost.oneconfig:$module:$oneconfigversion")
    }
}

loom {
    runConfigs.all {
        ideConfigGenerated(true)
        runDir = "run"
        vmArg("-Dmixin.debug.export=true")
    }

    // Client-side mod, so there's no need for a server run config
    runConfigs.remove(runConfigs["server"])
}

java {
    withSourcesJar()

    toolchain {
        vendor = JvmVendorSpec.ADOPTIUM
        languageVersion = JavaLanguageVersion.of(javaVersion)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = javaVersion
}

bloom {
    replacement("@MOD_ID@", modid)
    replacement("@MOD_NAME@", modname)
    replacement("@MOD_VERSION@", modversion)
}

tasks {
    processResources {
        val props = mapOf(
            "mod_id" to modid,
            "mod_name" to modname,
            "mod_version" to modversion,
            "minecraft_version_range" to versionrange,
            "loader_version" to loaderversion
        )

        inputs.properties(props)

        filesMatching("fabric.mod.json") { expand(props) }
    }

    jar {
        inputs.property("archivesName", base.archivesName)

        from(rootProject.file("LICENSE")) {
            rename { "${it}_${inputs.properties["archivesName"]}" }
        }
    }
}

// Set `publish.modrinth.token` and `publish.curseforge.token` in your user Gradle properties (~/.gradle/gradle.properties)
val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }
val modrinthToken = findProperty("publish.modrinth.token")?.toString()?.takeIf { it.isNotBlank() }
val curseforgeId = findProperty("publish.curseforge")?.toString()?.takeIf { it.isNotBlank() }
val curseforgeToken = findProperty("publish.curseforge.token")?.toString()?.takeIf { it.isNotBlank() }

publishMods {
    file = tasks.jar.flatMap { it.archiveFile }

    displayName = modversion
    version = "v$modversion"
    changelog = rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = STABLE

    modLoaders.add("fabric")

    dryRun = (modrinthId != null && modrinthToken == null) || (curseforgeId != null && curseforgeToken == null)

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = modrinthToken.orEmpty()
            minecraftVersions.add(mcversion)

            requires("oneconfig", "fabric-api")
        }
    }

    if (curseforgeId != null) {
        curseforge {
            projectId = curseforgeId
            accessToken = curseforgeToken.orEmpty()
            minecraftVersions.add(mcversion)
            client = true

            requires("oneconfig", "fabric-api")
        }
    }
}
