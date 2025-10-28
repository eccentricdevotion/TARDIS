import io.papermc.hangarpublishplugin.model.Platforms
import org.apache.tools.ant.filters.ReplaceTokens
import java.io.ByteArrayOutputStream

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
    id("com.gradleup.shadow") version "9.2.2"
    id("java")
    id("io.papermc.hangar-publish-plugin") version "0.1.3"
}

group = "me.eccentric_nz"
val buildNumber = "-b${System.getenv("BUILD_NUMBER") ?: (System.getenv("SHORT_SHA") ?: ".local")}"
version = "6.2.4${buildNumber}"

repositories {
    mavenCentral()
    maven {
        name = "papermc-repo"
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }
    maven {
        name = "jitpack.io"
        url = uri("https://jitpack.io")
    }
    maven {
        name = "floodgate"
        url = uri("https://repo.opencollab.dev/maven-snapshots/")
    }
    maven {
        name = "geyser"
        url = uri("https://repo.opencollab.dev/maven-releases/")
    }
    maven {
        name = "worldguard"
        url = uri("https://maven.enginehub.org/repo/")
    }
    maven {
        name = "multiverse"
        url = uri("https://repo.onarandombox.com/content/groups/public/")
    }
    maven {
        name = "libsdisguises"
        url = uri("https://repo.md-5.net/content/repositories/public/")
    }
    maven {
        name = "placeholderapi"
        url = uri("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    }
    maven {
        name = "essentialsx"
        url = uri("https://repo.essentialsx.net/snapshots/")
    }
    maven {
        name = "blocklocker"
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }
    maven {
        name = "lwcx"
        url = uri("https://ci.ender.zone/plugin/repository/everything/")
    }
    maven {
        name = "coreprotect"
        url = uri("https://maven.playpro.com")
    }
    maven {
        name = "citizens"
        url = uri("https://repo.citizensnpcs.co/")
    }
    maven {
        name = "towny"
        url = uri("https://repo.glaremasters.me/repository/towny/")
    }
    maven {
        name = "factions"
        url = uri("https://dependency.download/releases/")
    }
    maven {
        name = "bluemap"
        url = uri("https://repo.bluecolored.de/releases/")
    }
    maven {
        name = "sonatype"
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }
}

dependencies {
    paperweight.paperDevBundle("1.21.10-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.1.0-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizensapi:2.0.39-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.palmergames.bukkit.towny:towny:0.101.2.4") {
        isTransitive = false
    }
    compileOnly("org.mvplugins.multiverse.core:multiverse-core:5.2.1") {
        isTransitive = false
    }
    compileOnly("org.mvplugins.multiverse.inventories:multiverse-inventories:5.2.0") {
        isTransitive = false
    }
    compileOnly("dev.kitteh:factionsuuid:0.7.0") {
        isTransitive = false
    }
    compileOnly(files("libs/GriefPrevention.jar"))
    compileOnly("nl.rutgerkok:blocklocker:1.13") {
        isTransitive = false
    }
    compileOnly("com.griefcraft:lwc:2.4.1") {
        isTransitive = false
    }
    compileOnly("com.github.retrooper:packetevents-api:2.9.5") {
        isTransitive = false
    }
    compileOnly("me.libraryaddict.disguises:libsdisguises:11.0.9") {
        isTransitive = false
    }
    compileOnly("net.essentialsx:EssentialsX:2.22.0-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("me.clip:placeholderapi:2.11.6") {
        isTransitive = false
    }
    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Core:8.1.2") {
        isTransitive = false
    }
    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Spigot:8.1.2") {
        isTransitive = false
    }
    compileOnly("net.coreprotect:coreprotect:23.0") {
        isTransitive = false
    }
    compileOnly(files("libs/dynmap-api-3.7-beta-10.jar"))
    compileOnly(files("libs/DynmapCoreAPI-3.7-beta-10.jar"))
    compileOnly("de.bluecolored:bluemap-api:2.7.5")
    compileOnly("xyz.jpenilla:squaremap-api:1.3.8") {
        isTransitive = false
    }
    compileOnly(files("libs/VaultAPI.jar"))
    compileOnly("org.geysermc.floodgate:api:2.2.4-SNAPSHOT")
    compileOnly("org.geysermc.geyser:api:2.8.3-SNAPSHOT")
    compileOnly("org.jsoup:jsoup:1.21.2") {
        isTransitive = false
    }
    compileOnly("org.popcraft:chunky-common:1.4.51")
    compileOnly("org.popcraft:chunky-bukkit:1.4.51")
    compileOnly("org.popcraft:chunkyborder-common:1.2.33")
    compileOnly("net.luckperms:api:5.5")
    compileOnly("org.apache.commons:commons-lang3:3.19.0")
    compileOnly(files("libs/TerraformGenerator.jar"))
    compileOnly("com.formdev:flatlaf:3.6.1") {
        isTransitive = false
    }
    compileOnly("org.swinglabs:swing-layout:1.0.3") {
        isTransitive = false
    }
    testImplementation(platform("org.junit:junit-bom:5.13.4"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

allprojects {
    plugins.apply("com.gradleup.shadow")
    plugins.apply("java")
}

tasks {
    compileJava {
        // Set the release flag
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        filter<ReplaceTokens>("tokens" to mapOf("buildNumber" to buildNumber))
    }
}

tasks.jar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

// if you have shadowJar configured
tasks.shadowJar {
    manifest {
        attributes["paperweight-mappings-namespace"] = "mojang"
    }
}

paperweight.reobfArtifactConfiguration = io.papermc.paperweight.userdev.ReobfArtifactConfiguration.MOJANG_PRODUCTION

interface InjectedExecOps {
    @get:Inject val execOps: ExecOperations
}

// Helper methods
fun executeGitCommand(vararg command: String): String {
    val injected = project.objects.newInstance<InjectedExecOps>()
    val byteOut = ByteArrayOutputStream()
    injected.execOps.exec {
        commandLine = listOf("git", *command)
        standardOutput = byteOut
    }
    return byteOut.toString(Charsets.UTF_8.name()).trim()
}

fun latestCommitMessage(): String {
    return executeGitCommand("log", "-1", "--pretty=%B")
}

// Use the commit description for the changelog
val changelogContent: String = latestCommitMessage()

hangarPublish {
    publications.register("plugin") {
        version.set(project.version as String)
        channel.set("Snapshot")
        id.set("TARDIS")
        apiKey.set(System.getenv("HANGAR_API_TOKEN"))
        platforms {
            register(Platforms.PAPER) {
                // Set the JAR file to upload
                jar.set(tasks.shadowJar.flatMap { it.archiveFile })
                // Set platform versions from gradle.properties file
                val versions: List<String> = (property("paperVersion") as String)
                    .split(",")
                    .map { it.trim() }
                platformVersions.set(versions)
                changelog.set(changelogContent)
            }
        }
    }
}
