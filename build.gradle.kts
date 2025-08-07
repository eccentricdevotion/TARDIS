import org.apache.tools.ant.filters.ReplaceTokens

plugins {
    `java-library`
    id("io.papermc.paperweight.userdev") version "2.0.0-SNAPSHOT"
    id("com.gradleup.shadow") version "9.0.0-rc1"
    id("java")
}

group = "me.eccentric_nz"
val build_number = "-b${System.getenv("BUILD_NUMBER") ?: ".local"}"
version = "6.2.2${build_number}"

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
        name = "protocollib"
        url = uri("https://repo.dmulloy2.net/repository/public/")
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
    paperweight.paperDevBundle("1.21.8-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.14-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizensapi:2.0.39-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.palmergames.bukkit.towny:towny:0.101.2.0") {
        isTransitive = false
    }
    compileOnly("org.mvplugins.multiverse.core:multiverse-core:5.1.1-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("org.mvplugins.multiverse.inventories:multiverse-inventories:5.1.1-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("dev.kitteh:factionsuuid:0.7.0") {
        isTransitive = false
    }
    compileOnly(files("libs/GriefPrevention.jar"))
    compileOnly("nl.rutgerkok:blocklocker:1.13") {
        isTransitive = false
    }
    compileOnly("com.griefcraft:lwc:2.4.0") {
        isTransitive = false
    }
    compileOnly("com.github.retrooper:packetevents-api:2.9.3") {
        isTransitive = false
    }
    compileOnly("me.libraryaddict.disguises:libsdisguises:11.0.7") {
        isTransitive = false
    }
    compileOnly("net.essentialsx:EssentialsX:2.21.2-SNAPSHOT") {
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
    compileOnly("net.coreprotect:coreprotect:22.4") {
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
    compileOnly("org.geysermc.geyser:api:2.8.2-SNAPSHOT")
    compileOnly("org.jsoup:jsoup:1.21.1") {
        isTransitive = false
    }
    compileOnly("org.popcraft:chunky-common:1.4.40")
    compileOnly("org.popcraft:chunky-bukkit:1.4.40")
    compileOnly("org.popcraft:chunkyborder-common:1.2.26")
    compileOnly("net.luckperms:api:5.5")
    compileOnly("org.apache.commons:commons-lang3:3.17.0")
    compileOnly(files("libs/TerraformGenerator.jar"))
    compileOnly("com.formdev:flatlaf:3.6") {
        isTransitive = false
    }
    compileOnly("org.swinglabs:swing-layout:1.0.3") {
        isTransitive = false
    }
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

allprojects {
    plugins.apply("com.gradleup.shadow")
    plugins.apply("java")
}

tasks {
    compileJava {
        // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
        // See https://openjdk.java.net/jeps/247 for more information.
        options.release = 21
    }
    javadoc {
        options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
    }
}

tasks.processResources {
    filesMatching("plugin.yml") {
        filter<ReplaceTokens>("tokens" to mapOf("buildNumber" to build_number))
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
