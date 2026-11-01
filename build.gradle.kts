import net.ornithemc.ploceus.api.PloceusGradleExtensionApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("net.fabricmc.fabric-loom-remap") version "1.17-SNAPSHOT"
    id("ploceus") version "1.17.4" // Ornithe's Loom add-on, which provides Feather mappings and LWJGL fixes for old versions
    id("org.jetbrains.kotlin.jvm") version "2.4.21"
    id("dev.deftu.gradle.bloom") version "0.2.0" // Replaces tokens such as `@MOD_VERSION@` in source files
    id("me.modmuss50.mod-publish-plugin") version "2.2.0" // Publishes to Modrinth
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

// OneConfig on Ornithe runs on modern Java, even for 1.8.9
val javaVersion = 25

val ploceus = extensions.getByType<PloceusGradleExtensionApi>().apply {
    setIntermediaryGeneration(2)
}

// Ornithe supplies its own LWJGL, so drop the one Minecraft depends on
configurations.configureEach {
    exclude(group = "org.lwjgl.lwjgl")
}

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

    // OneConfig needs STB natives for every platform, but Mojang's library repository only has the ones vanilla ships
    exclusiveContent {
        forRepository { mavenCentral() }
        filter { includeModule("org.lwjgl", "lwjgl-stb") }
    }
}

dependencies {
    minecraft("com.mojang:minecraft:$mcversion")
    mappings(ploceus.layeredMappings {
        mappings("net.ornithemc:feather-gen2:$mcversion+build.${property("feather_build")}:v2") {
            containsUnpick()
        }
    })

    modImplementation("net.fabricmc:fabric-loader:$loaderversion")
    modImplementation("org.polyfrost.oneconfig:$mcversion-ornithe:$oneconfigversion")
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

kotlin {
    compilerOptions.jvmTarget = JvmTarget.fromTarget(javaVersion.toString())
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

// Set `publish.modrinth.token` in your user Gradle properties (~/.gradle/gradle.properties)
// CurseForge doesn't list Ornithe as a loader, so only Modrinth is set up
val modrinthId = findProperty("publish.modrinth")?.toString()?.takeIf { it.isNotBlank() }
val modrinthToken = findProperty("publish.modrinth.token")?.toString()?.takeIf { it.isNotBlank() }

publishMods {
    file = tasks.remapJar.flatMap { it.archiveFile }

    displayName = modversion
    version = "v$modversion"
    changelog = rootProject.file("CHANGELOG.md").takeIf { it.exists() }?.readText() ?: "No changelog provided."
    type = STABLE

    modLoaders.add("ornithe")

    dryRun = modrinthId != null && modrinthToken == null

    if (modrinthId != null) {
        modrinth {
            projectId = modrinthId
            accessToken = modrinthToken.orEmpty()
            minecraftVersions.add(mcversion)

            requires("oneconfig", "fabric-language-kotlin")
        }
    }
}
