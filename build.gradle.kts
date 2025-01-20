plugins {
    id("io.papermc.paperweight.userdev") version "2.0.0-beta.14"
    id("com.gradleup.shadow") version "9.0.0-beta4"
    id("java")
}

group = "me.eccentric_nz"
version = "6.1.0"

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
        url = uri("https://repo.onarandombox.com/multiverse-releases/")
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
        name = "dynmap"
        url = uri("https://repo.mikeprimm.com/")
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
    paperweight.paperDevBundle("1.21.4-R0.1-SNAPSHOT")
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.10-SNAPSHOT")
    compileOnly("net.citizensnpcs:citizensapi:2.0.33-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("com.palmergames.bukkit.towny:towny:0.101.1.0") {
        isTransitive = false
    }
    compileOnly("com.github.Brettflan:WorldBorder:44f388f3ba") {
        isTransitive = false
    }
    compileOnly("com.onarandombox.multiversecore:multiverse-core:4.3.13") {
        isTransitive = false
    }
    compileOnly("com.onarandombox.multiverseinventories:Multiverse-Inventories:4.2.3") {
        isTransitive = false
    }
    compileOnly("dev.kitteh:factionsuuid:0.7.0") {
        isTransitive = false
    }
    compileOnly("com.github.GriefPrevention:GriefPrevention:17.0.0") {
        isTransitive = false
    }
    compileOnly("nl.rutgerkok:blocklocker:1.13") {
        isTransitive = false
    }
    compileOnly("com.griefcraft:lwc:2.4.0") {
        isTransitive = false
    }
    compileOnly("com.comphenix.protocol:ProtocolLib:5.3.0") {
        isTransitive = false
    }
    compileOnly("LibsDisguises:LibsDisguises:10.0.44") {
        isTransitive = false
    }
    compileOnly("net.essentialsx:EssentialsX:2.21.0-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("me.clip:placeholderapi:2.11.6") {
        isTransitive = false
    }
    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Core:8.1.1") {
        isTransitive = false
    }
    compileOnly("io.github.fabiozumbi12.RedProtect:RedProtect-Spigot:8.1.1") {
        isTransitive = false
    }
    compileOnly("net.coreprotect:coreprotect:22.4") {
        isTransitive = false
    }
    compileOnly("us.dynmap:dynmap-api:3.6") {
        isTransitive = false
    }
    compileOnly("us.dynmap:DynmapCoreAPI:3.7-SNAPSHOT") {
        isTransitive = false
    }
    compileOnly("de.bluecolored.bluemap:BlueMapAPI:2.7.1")
    compileOnly("xyz.jpenilla:squaremap-api:1.2.1") {
        isTransitive = false
    }
    compileOnly("com.github.MilkBowl:VaultAPI:1.7.1") {
        isTransitive = false
    }
    compileOnly("org.geysermc.floodgate:api:2.2.3-SNAPSHOT")
    compileOnly("org.jsoup:jsoup:1.17.2") {
        isTransitive = false
    }
    compileOnly("com.formdev:flatlaf:3.4.1") {
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
