/*
 * Copyright (C) 2021 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.info;

import java.util.TreeMap;

/**
 * Articles in the tardis Information System were accessed by typing in an initialism for the subject. Entering IF, for
 * example, brought up the database's index file.
 *
 * @author bootthanoo, eccentric_nz
 */
public enum TardisInfoMenu {

    TIS("tardis Information System", "TIS|Commands|tardis Commands|add", "tardis"),
    ITEMS("TIS|Items", "TIS", "I"),
    KEY("TIS|Items|tardis Key", "ITEMS", "K"),
    KEY_INFO("TIS|Items|tardis Key|Info", "KEY", "I"),
    KEY_RECIPE("TIS|Items|tardis Key|Recipe", "KEY", "R"),
    SONIC("TIS|Items|Sonic Screwdriver", "ITEMS", "S"),
    SONIC_INFO("TIS|Items|Sonic Screwdriver|Info", "SONIC", "I"),
    SONIC_TYPES("TIS|Items|Sonic Screwdriver|Types", "SONIC", "T"),
    SONIC_Q("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic", "SONIC_TYPES", "Q"),
    SONIC_Q_INFO("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic|Info", "SONIC_Q", "I"),
    SONIC_RECIPE("TIS|Items|Sonic Screwdriver|Types|Quartz Sonic Recipe", "SONIC_Q", "R"),
    SONIC_R("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic", "SONIC_TYPES", "R"),
    SONIC_R_INFO("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic|Info", "SONIC_R", "I"),
    SONIC_R_RECIPE("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic Recipe", "SONIC_R", "R"),
    SONIC_D("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic", "SONIC_TYPES", "D"),
    SONIC_D_INFO("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic|Info", "SONIC_D", "I"),
    SONIC_D_RECIPE("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic Recipe", "SONIC_D", "R"),
    SONIC_E("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic", "SONIC_TYPES", "m"),
    SONIC_E_INFO("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic|Info", "SONIC_E", "I"),
    SONIC_E_RECIPE("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic Recipe", "SONIC_E", "R"),
    SONIC_A("TIS|Items|Sonic Screwdriver|Types|Admin Sonic", "SONIC_TYPES", "A"),
    SONIC_A_INFO("TIS|Items|Sonic Screwdriver|Types|Admin Sonic|Info", "SONIC_A", "I"),
    SONIC_A_RECIPE("TIS|Items|Sonic Screwdriver|Types|Admin Sonic|Recipe", "SONIC_A", "R"),
    LOCATOR("TIS|Items|tardis Locator", "ITEMS", "L"),
    LOCATOR_INFO("TIS|Items|tardis Locator|Info", "LOCATOR", "I"),
    LOCATOR_RECIPE("TIS|Items|tardis Locator|Recipe", "LOCATOR", "R"),
    REMOTE("TIS|Items|Stattenheim Remote", "ITEMS", "R"),
    REMOTE_INFO("TIS|Items|Stattenheim Remote|Info", "REMOTE", "I"),
    REMOTE_RECIPE("TIS|Items|Stattenheim Remote|Recipe", "REMOTE", "R"),
    READER("TIS|Items|tardis Biome Reader", "ITEMS", "B"),
    READER_INFO("TIS|Items|tardis Biome Reader|Info", "READER", "I"),
    READER_RECIPE("TIS|Items|tardis Biome Reader|Recipe", "READER", "R"),
    R_KEY("TIS|Items|tardis Remote Key", "ITEMS", "m"),
    R_KEY_INFO("TIS|Items|tardis Remote Key|Info", "R_KEY", "I"),
    R_KEY_RECIPE("TIS|Items|tardis Remote Key|Recipe", "R_KEY", "R"),
    COMPONENTS("TIS|Components", "TIS", "C"),
    SONIC_COMPONENTS("TIS|Sonic Components", "TIS", "S"),
    L_CIRCUIT("TIS|Components|Locator Circuit", "COMPONENTS", "L"),
    L_CIRCUIT_INFO("TIS|Components|Locator Circuit|Info", "L_CIRCUIT", "I"),
    L_CIRCUIT_RECIPE("TIS|Components|Locator Circuit|Recipe", "L_CIRCUIT", "R"),
    M_CIRCUIT("TIS|Components|Materialisation Circuit", "COMPONENTS", "M"),
    M_CIRCUIT_INFO("TIS|Components|Materialisation Circuit|Info", "M_CIRCUIT", "I"),
    M_CIRCUIT_RECIPE("TIS|Components|Materialisation Circuit|Recipe", "M_CIRCUIT", "R"),
    S_CIRCUIT("TIS|Components|Stattenheim Circuit", "COMPONENTS", "S"),
    S_CIRCUIT_INFO("TIS|Components|Stattenheim Circuit|Info", "S_CIRCUIT", "I"),
    S_CIRCUIT_RECIPE("TIS|Components|Stattenheim Circuit|Recipe", "S_CIRCUIT", "R"),
    A_CIRCUIT("TIS|Sonic Components|Admin Circuit", "SONIC_COMPONENTS", "A"),
    A_CIRCUIT_INFO("TIS|Sonic Components|ars Circuit|Info", "A_CIRCUIT", "I"),
    A_CIRCUIT_RECIPE("TIS|Sonic Components|ars Circuit|Recipe", "A_CIRCUIT", "R"),
    BIO_CIRCUIT("TIS|Sonic Components|Bio-scanner Circuit", "SONIC_COMPONENTS", "B"),
    BIO_CIRCUIT_INFO("TIS|Sonic Components|Bio-scanner Circuit|Info", "BIO_CIRCUIT", "I"),
    BIO_CIRCUIT_RECIPE("TIS|Sonic Components|Bio-scanner Circuit|Recipe", "BIO_CIRCUIT", "R"),
    C_CIRCUIT("TIS|Components|Chameleon Circuit", "COMPONENTS", "C"),
    C_CIRCUIT_INFO("TIS|Components|Chameleon Circuit|Info", "C_CIRCUIT", "I"),
    C_CIRCUIT_RECIPE("TIS|Components|Chameleon Circuit|Recipe", "C_CIRCUIT", "R"),
    D_CIRCUIT("TIS|Sonic Components|Diamond Disruptor Circuit", "SONIC_COMPONENTS", "D"),
    D_CIRCUIT_INFO("TIS|Sonic Components|Diamond Disruptor Circuit|Info", "D_CIRCUIT", "I"),
    D_CIRCUIT_RECIPE("TIS|Sonic Components|Diamond Disruptor Circuit|Recipe", "D_CIRCUIT", "R"),
    E_CIRCUIT("TIS|Sonic Components|Emerald Environment Circuit", "SONIC_COMPONENTS", "m"),
    E_CIRCUIT_INFO("TIS|Sonic Components|Emerald Environment Circuit|Info", "E_CIRCUIT", "I"),
    E_CIRCUIT_RECIPE("TIS|Sonic Components|Emerald Environment Circuit|Recipe", "E_CIRCUIT", "R"),
    ARS_CIRCUIT("TIS|Components|Architectural Reconfiguration System (ars) Circuit", "COMPONENTS", "h"),
    ARS_CIRCUIT_INFO("TIS|Components|Emerald Environment Circuit|Info", "ARS_CIRCUIT", "I"),
    ARS_CIRCUIT_RECIPE("TIS|Components|Emerald Environment Circuit|Recipe", "ARS_CIRCUIT", "R"),
    CELL("TIS|Items|Artron Storage Cell", "ITEMS", "A"),
    CELL_INFO("TIS|Items|Artron Storage Cell|Info", "CELL", "I"),
    CELL_RECIPE("TIS|Items|Artron Storage Cell|Recipe", "CELL", "R"),
    FILTER("TIS|Items|Perception Filter", "ITEMS", "F"),
    FILTER_INFO("TIS|Items|Perception Filter|Info", "FILTER", "I"),
    FILTER_RECIPE("TIS|Items|Perception Filter|Recipe", "FILTER", "R"),
    GENERATOR("TIS|Items|Sonic Generator", "ITEMS", "G"),
    GENERATOR_INFO("TIS|Items|Sonic Generator|Info", "GENERATOR", "I"),
    GENERATOR_RECIPE("TIS|Items|Sonic Generator|Recipe", "GENERATOR", "R"),
    I_CIRCUIT("TIS|Components|Input Circuit", "COMPONENTS", "I"),
    I_CIRCUIT_INFO("TIS|Components|Input Circuit|Info", "I_CIRCUIT", "I"),
    I_CIRCUIT_RECIPE("TIS|Components|Input Circuit|Recipe", "I_CIRCUIT", "R"),
    IGNITE_CIRCUIT("TIS|Sonic Components|Ignite Circuit", "SONIC_COMPONENTS", "I"),
    IGNITE_CIRCUIT_INFO("TIS|Sonic Components|Ignite Circuit|Info", "IGNITE_CIRCUIT", "I"),
    IGNITE_CIRCUIT_RECIPE("TIS|Sonic Components|Ignite Circuit|Recipe", "IGNITE_CIRCUIT", "R"),
    KNOCKBACK_CIRCUIT("TIS|Sonic Components|Knockback Circuit", "SONIC_COMPONENTS", "K"),
    KNOCKBACK_CIRCUIT_INFO("TIS|Sonic Components|Knockback Circuit|Info", "KNOCKBACK_CIRCUIT", "I"),
    KNOCKBACK_CIRCUIT_RECIPE("TIS|Sonic Components|Knockback Circuit|Recipe", "KNOCKBACK_CIRCUIT", "R"),
    ARROW_CIRCUIT("TIS|Sonic Components|Pickup Arrows Circuit", "SONIC_COMPONENTS", "c"),
    ARROW_CIRCUIT_INFO("TIS|Sonic Components|Pickup Arrows Circuit|Info", "ARROW_CIRCUIT", "I"),
    ARROW_CIRCUIT_RECIPE("TIS|Sonic Components|Pickup Arrows Circuit|Recipe", "ARROW_CIRCUIT", "R"),
    INVISIBLE_CIRCUIT("TIS|Components|Invisibility Circuit", "COMPONENTS", "v"),
    INVISIBLE_INFO("TIS|Components|Invisibility Circuit|Info", "INVISIBLE_CIRCUIT", "I"),
    INVISIBLE_RECIPE("TIS|Components|Invisibility Circuit|Recipe", "INVISIBLE_CIRCUIT", "R"),
    OSCILLATOR_CIRCUIT("TIS|Sonic Components|Oscillator Circuit", "SONIC_COMPONENTS", "O"),
    OSCILLATOR_INFO("TIS|Sonic Components|Oscillator Circuit|Info", "OSCILLATOR_CIRCUIT", "I"),
    OSCILLATOR_RECIPE("TIS|Sonic Components|Oscillator Circuit|Recipe", "OSCILLATOR_CIRCUIT", "R"),
    P_CIRCUIT("TIS|Components|Perception Circuit", "COMPONENTS", "P"),
    P_CIRCUIT_INFO("TIS|Components|Perception Circuit|Info", "P_CIRCUIT", "I"),
    P_CIRCUIT_RECIPE("TIS|Components|Perception Circuit|Recipe", "P_CIRCUIT", "R"),
    PAINTER_CIRCUIT("TIS|Sonic Components|Painter Circuit", "SONIC_COMPONENTS", "P"),
    PAINTER_INFO("TIS|Sonic Components|Painter Circuit|Info", "PAINTER_CIRCUIT", "I"),
    PAINTER_RECIPE("TIS|Sonic Components|Painter Circuit|Recipe", "PAINTER_CIRCUIT", "R"),
    R_CIRCUIT("TIS|Components|Redstone Activator Circuit", "COMPONENTS", "R"),
    R_CIRCUIT_INFO("TIS|Components|Redstone Activator Circuit|Info", "R_CIRCUIT", "I"),
    R_CIRCUIT_RECIPE("TIS|Components|Redstone Activator Circuit|Recipe", "R_CIRCUIT", "R"),
    RANDOMISER_CIRCUIT("TIS|Components|Randomiser Circuit", "COMPONENTS", "o"),
    RANDOMISER_CIRCUIT_INFO("TIS|Components|Randomiser Circuit|Info", "RANDOMISER_CIRCUIT", "I"),
    RANDOMISER_CIRCUIT_RECIPE("TIS|Components|Randomiser Circuit|Recipe", "RANDOMISER_CIRCUIT", "R"),
    T_CIRCUIT("TIS|Components|Temporal Circuit", "COMPONENTS", "T"),
    T_CIRCUIT_INFO("TIS|Components|Temporal Circuit|Info", "T_CIRCUIT", "I"),
    T_CIRCUIT_RECIPE("TIS|Components|Temporal Circuit|Recipe", "T_CIRCUIT", "R"),
    MEMORY_CIRCUIT("TIS|Components|Memory Circuit", "COMPONENTS", "y"),
    MEMORY_CIRCUIT_INFO("TIS|Components|Memory Circuit|Info", "MEMORY_CIRCUIT", "I"),
    MEMORY_CIRCUIT_RECIPE("TIS|Components|Memory Circuit|Recipe", "MEMORY_CIRCUIT", "R"),
    SCANNER_CIRCUIT("TIS|Components|Scanner Circuit", "COMPONENTS", "n"),
    SCANNER_CIRCUIT_INFO("TIS|Components|Scanner Circuit|Info", "SCANNER_CIRCUIT", "I"),
    SCANNER_CIRCUIT_RECIPE("TIS|Components|Scanner Circuit|Recipe", "SCANNER_CIRCUIT", "R"),
    DISKS("TIS|tardis Disks", "TIS", "D"),
    AREA_DISK("TIS|tardis Disks|Area Storage Disk", "DISKS", "A"),
    BLANK("TIS|tardis Disks|Blank Storage Disk", "DISKS", "B"),
    BLANK_INFO("TIS|tardis Disks|Blank Storage Disk|Info", "BLANK", "I"),
    BLANK_RECIPE("TIS|tardis Disks|Blank Storage Disk|Recipe", "BLANK", "R"),
    BIOME_DISK("TIS|tardis Disks|Biome Storage Disk", "DISKS", "i"),
    BIOME_DISK_INFO("TIS|tardis Disks|Biome Storage Disk|Info", "BIOME_DISK", "I"),
    BIOME_DISK_RECIPE("TIS|tardis Disks|Biome Storage Disk|Recipe", "BIOME_DISK", "R"),
    CONTROL_DISK("TIS|tardis Disks|Authorised Control Disk", "DISKS", "C"),
    CONTROL_DISK_INFO("TIS|tardis Disks|Authorised Control Disk|Info", "CONTROL_DISK", "I"),
    CONTROL_DISK_RECIPE("TIS|tardis Disks|Authorised Control Disk|Recipe", "CONTROL_DISK", "R"),
    PLAYER_DISK("TIS|tardis Disks|Player Storage Disk", "DISKS", "P"),
    PLAYER_DISK_INFO("TIS|tardis Disks|Player Storage Disk|Info", "PLAYER_DISK", "I"),
    PLAYER_DISK_RECIPE("TIS|tardis Disks|Player Storage Disk|Recipe", "PLAYER_DISK", "R"),
    PRESET_DISK("TIS|tardis Disks|Preset Storage Disk", "DISKS", "r"),
    PRESET_DISK_INFO("TIS|tardis Disks|Preset Storage Disk|Info", "PRESET_DISK", "I"),
    PRESET_DISK_RECIPE("TIS|tardis Disks|Preset Storage Disk|Recipe", "PRESET_DISK", "R"),
    SAVE_DISK("TIS|tardis Disks|Save Storage Disk", "DISKS", "S"),
    SAVE_DISK_INFO("TIS|tardis Disks|Save Storage Disk|Info", "SAVE_DISK", "I"),
    SAVE_DISK_RECIPE("TIS|tardis Disks|Save Storage Disk|Recipe", "SAVE_DISK", "R"),
    TYPES("TIS|tardis Types", "TIS", "T"),
    BUDGET("TIS|tardis Types|Budget", "TYPES", "B"),
    BIGGER("TIS|tardis Types|Bigger", "TYPES", "i"),
    DELUXE("TIS|tardis Types|Deluxe", "TYPES", "D"),
    ELEVENTH("TIS|tardis Types|Eleventh", "TYPES", "l"),
    TWELFTH("TIS|tardis Types|Twelfth", "TYPES", "f"),
    THIRTEENTH("TIS|tardis Types|Thirteenth", "TYPES", "n"),
    FACTORY("TIS|tardis Types|Factory", "TYPES", "y"),
    REDSTONE("TIS|tardis Types|Redstone", "TYPES", "R"),
    STEAMPUNK("TIS|tardis Types|Steampunk", "TYPES", "S"),
    PLANK("TIS|tardis Types|Plank", "TYPES", "P"),
    TOM("TIS|tardis Types|Tom", "TYPES", "T"),
    ARS("TIS|tardis Types|ars", "TYPES", "A"),
    WAR("TIS|tardis Types|War", "TYPES", "W"),
    PYRAMID("TIS|tardis Types|Pyramid", "TYPES", "y"),
    MASTER("TIS|tardis Types|Master", "TYPES", "M"),
    ENDER("TIS|tardis Types|Ender", "TYPES", "d"),
    CORAL("TIS|tardis Types|Coral", "TYPES", "o"),
    COPPER_11TH("TIS|tardis Types|Copper", "TYPES", "1"),
    CUSTOM("TIS|tardis Types|Custom", "TYPES", "C"),
    ROOMS("TIS|Rooms", "TIS", "R"),
    ANTIGRAVITY("TIS|Rooms|Anti-gravity", "ROOMS", "A"),
    AQUARIUM("TIS|Rooms|Aquarium", "ROOMS", "q"),
    ARBORETUM("TIS|Rooms|Arboretum", "ROOMS", "u"),
    BAKER("TIS|Rooms|Baker", "ROOMS", "B"),
    BEDROOM("TIS|Rooms|Bedroom", "ROOMS", "d"),
    BIRDCAGE("TIS|Rooms|Bird Cage", "ROOMS", "c"),
    EMPTY("TIS|Rooms|Empty", "ROOMS", "y"),
    FARM("TIS|Rooms|Farm", "ROOMS", "F"),
    GRAVITY("TIS|Rooms|Gravity", "ROOMS", "G"),
    GREENHOUSE("TIS|Rooms|Greenhouse", "ROOMS", "n"),
    HARMONY("TIS|Rooms|Harmony", "ROOMS", "H"),
    KITCHEN("TIS|Rooms|Kitchen", "ROOMS", "K"),
    LIBRARY("TIS|Rooms|Library", "ROOMS", "L"),
    MUSHROOM("TIS|Rooms|Mushroom", "ROOMS", "M"),
    PASSAGE("TIS|Rooms|Passage", "ROOMS", "P"),
    POOL("TIS|Rooms|Pool", "ROOMS", "o"),
    RAIL("TIS|Rooms|Rail", "ROOMS", "R"),
    RENDERER("TIS|Rooms|External Renderer", "ROOMS", "x"),
    SHELL("TIS|Rooms|Shell", "ROOMS", "Sh"),
    STABLE("TIS|Rooms|Stable", "ROOMS", "S"),
    STALL("TIS|Rooms|Llama Stall", "ROOMS", "Ll"),
    TRENZALORE("TIS|Rooms|Trenzalore", "ROOMS", "T"),
    VAULT("TIS|Rooms|Vault", "ROOMS", "V"),
    VILLAGE("TIS|Rooms|Village", "ROOMS", "i"),
    WOOD("TIS|Rooms|Wood", "ROOMS", "W"),
    WORKSHOP("TIS|Rooms|Workshop", "ROOMS", "h"),
    MANUAL("TIS|Manual", "TIS", "M"),
    COMMANDS("TIS|Commands", "TIS", "o"),
    TARDIS("TIS|Commands|tardis Commands", "COMMANDS", "T"),
    TARDIS_ABORT("TIS|Commands|tardis Commands|abort", "tardis", "ab"),
    TARDIS_ADD("TIS|Commands|tardis Commands|add", "tardis", "a"),
    TARDIS_CHAMELEON("TIS|Commands|tardis Commands|chameleon", "tardis", "c"),
    TARDIS_COMEHERE("TIS|Commands|tardis Commands|comehere", "tardis", "com"),
    TARDIS_DIRECTION("TIS|Commands|tardis Commands|direction", "tardis", "d"),
    TARDIS_EXTERMINATE("TIS|Commands|tardis Commands|exterminate", "tardis", "x"),
    TARDIS_FIND("TIS|Commands|tardis Commands|find", "tardis", "f"),
    TARDIS_HIDE("TIS|Commands|tardis Commands|hide", "tardis", "h"),
    TARDIS_HOME("TIS|Commands|tardis Commands|home", "tardis", "m"),
    TARDIS_INSIDE("TIS|Commands|tardis Commands|inside", "tardis", "i"),
    TARDIS_JETTISON("TIS|Commands|tardis Commands|jettison", "tardis", "j"),
    TARDIS_LAMPS("TIS|Commands|tardis Commands|lamps", "tardis", "la"),
    TARDIS_LIST("TIS|Commands|tardis Commands|list", "tardis", "l"),
    TARDIS_NAMEKEY("TIS|Commands|tardis Commands|namekey", "tardis", "k"),
    TARDIS_OCCUPY("TIS|Commands|tardis Commands|occupy", "tardis", "o"),
    TARDIS_REBUILD("TIS|Commands|tardis Commands|rebuild", "tardis", "b"),
    TARDIS_REMOVE("TIS|Commands|tardis Commands|remove", "tardis", "r"),
    TARDIS_REMOVESAVE("TIS|Commands|tardis Commands|removesave", "tardis", "rem"),
    TARDIS_RESCUE("TIS|Commands|tardis Commands|rescue", "tardis", "u"),
    TARDIS_ROOM("TIS|Commands|tardis Commands|room", "tardis", "roo"),
    TARDIS_SAVE("TIS|Commands|tardis Commands|save", "tardis", "s"),
    TARDIS_SECONDARY("TIS|Commands|tardis Commands|secondary", "tardis", "n"),
    TARDIS_SETDEST("TIS|Commands|tardis Commands|setdest", "tardis", "t"),
    TARDIS_UPDATE("TIS|Commands|tardis Commands|update", "tardis", "p"),
    TARDIS_VERSION("TIS|Commands|tardis Commands|version", "tardis", "v"),
    TARDISADMIN("TIS|Commands|Admin Commands", "COMMANDS", "A"),
    TARDISAREA("TIS|Commands|Area Commands", "COMMANDS", "C"),
    TARDISAREA_START("TIS|Commands|Area Commands|start", "TARDISAREA", "s"),
    TARDISAREA_END("TIS|Commands|Area Commands|end", "TARDISAREA", "n"),
    TARDISAREA_SHOW("TIS|Commands|Area Commands|show", "TARDISAREA", "h"),
    TARDISAREA_REMOVE("TIS|Commands|Area Commands|remove", "TARDISAREA", "r"),
    TARDISBIND("TIS|Commands|Bind Commands", "COMMANDS", "B"),
    TARDISBIND_SAVE("TIS|Commands|Bind Commands|save", "TARDISBIND", "s"),
    TARDISBIND_CMD("TIS|Commands|Bind Commands|cmd", "TARDISBIND", "c"),
    TARDISBIND_PLAYER("TIS|Commands|Bind Commands|player", "TARDISBIND", "p"),
    TARDISBIND_COORDS("TIS|Commands|Bind Commands|coordinates", "TARDISBIND", "o"),
    TARDISBIND_AREA("TIS|Commands|Bind Commands|area", "TARDISBIND", "a"),
    TARDISBIND_BIOME("TIS|Commands|Bind Commands|biome", "TARDISBIND", "b"),
    TARDISBIND_REMOVE("TIS|Commands|Bind Commands|remove", "TARDISBIND", "r"),
    TARDISBOOK("TIS|Commands|Book Commands", "COMMANDS", "k"),
    TARDISGRAVITY("TIS|Commands|Gravity Commands", "COMMANDS", "G"),
    TARDISPREFS("TIS|Commands|Player Preference Commands", "COMMANDS", "P"),
    TARDISPREFS_AUTO("TIS|Commands|Player Preference Commands|auto", "TARDISPREFS", "a"),
    TARDISPREFS_EPS("TIS|Commands|Player Preference Commands|eps", "TARDISPREFS", "p"),
    TARDISPREFS_FLOOR("TIS|Commands|Player Preference Commands|floor", "TARDISPREFS", "f"),
    TARDISPREFS_HADS("TIS|Commands|Player Preference Commands|hads", "TARDISPREFS", "h"),
    TARDISPREFS_ISOMORPHIC("TIS|Commands|Player Preference Commands|isomorphic", "TARDISPREFS", "i"),
    TARDISPREFS_KEY("TIS|Commands|Player Preference Commands|key", "TARDISPREFS", "k"),
    TARDISPREFS_MESSAGE("TIS|Commands|Player Preference Commands|eps_message", "TARDISPREFS", "m"),
    TARDISPREFS_QUOTES("TIS|Commands|Player Preference Commands|quotes", "TARDISPREFS", "q"),
    TARDISPREFS_SFX("TIS|Commands|Player Preference Commands|sfx", "TARDISPREFS", "s"),
    TARDISPREFS_SUBMARINE("TIS|Commands|Player Preference Commands|submarine", "TARDISPREFS", "u"),
    TARDISPREFS_WALL("TIS|Commands|Player Preference Commands|wall", "TARDISPREFS", "w"),
    TARDISRECIPE("TIS|Commands|Recipe Commands", "COMMANDS", "R"),
    TARDISROOM("TIS|Commands|Room Commands", "COMMANDS", "o"),
    TARDISROOM_ADD("TIS|Commands|Room Commands|add", "TARDISROOM", "a"),
    TARDISROOM_SEED("TIS|Commands|Room Commands|seed", "TARDISROOM", "s"),
    TARDISROOM_COST("TIS|Commands|Room Commands|cost", "TARDISROOM", "c"),
    TARDISROOM_OFFSET("TIS|Commands|Room Commands|offset", "TARDISROOM", "o"),
    TARDISROOM_ENABLED("TIS|Commands|Room Commands|enabled", "TARDISROOM", "n"),
    TARDISTEXTURE("TIS|Commands|Texture Commands", "COMMANDS", "x"),
    TARDISTEXTURE_ON("TIS|Commands|Texture Commands|on", "TARDISTEXTURE", "o"),
    TARDISTEXTURE_OFF("TIS|Commands|Texture Commands|off", "TARDISTEXTURE", "f"),
    TARDISTEXTURE_IN("TIS|Commands|Texture Commands|in", "TARDISTEXTURE", "i"),
    TARDISTEXTURE_OUT("TIS|Commands|Texture Commands|out", "TARDISTEXTURE", "u"),
    TARDISTRAVEL("TIS|Commands|Travel Commands", "COMMANDS", "v"),
    TARDISTRAVEL_HOME("TIS|Commands|Travel Commands|home", "TARDISTRAVEL", "h"),
    TARDISTRAVEL_PLAYER("TIS|Commands|Travel Commands|player", "TARDISTRAVEL", "p"),
    TARDISTRAVEL_COORDS("TIS|Commands|Travel Commands|coordinates", "TARDISTRAVEL", "c"),
    TARDISTRAVEL_DEST("TIS|Commands|Travel Commands|dest", "TARDISTRAVEL", "d"),
    TARDISTRAVEL_AREA("TIS|Commands|Travel Commands|area", "TARDISTRAVEL", "a"),
    TARDISTRAVEL_BIOME("TIS|Commands|Travel Commands|biome", "TARDISTRAVEL", "b"),
    TIME_TRAVEL("TIS|Manual|Time Travel", "MANUAL", "T"),
    CONSOLE_BLOCKS("TIS|Manual|Console Blocks", "MANUAL", "C"),
    CONSOLE_BLOCKS_2("TIS|Manual|Console Blocks 2", "MANUAL", "o"),
    CONSOLE_ARS("TIS|Manual|Console Blocks|ars", "CONSOLE_BLOCKS", "A"), //A
    ADVANCED("TIS|Manual|Console Blocks|Advanced Console", "CONSOLE_BLOCKS", "v"), //v
    STORAGE("TIS|Manual|Console Blocks|Disk Storage", "CONSOLE_BLOCKS", "S"), //S
    ARTRON("TIS|Manual|Console Blocks|Artron Energy Capacitor", "CONSOLE_BLOCKS", "r"), //r
    BACKDOOR("TIS|Manual|Console Blocks|Backdoor", "CONSOLE_BLOCKS", "B"), //B
    BUTTON("TIS|Manual|Console Blocks|Button", "CONSOLE_BLOCKS", "u"), //u
    CHAMELEON("TIS|Manual|Console Blocks|Chameleon", "CONSOLE_BLOCKS", "C"), //C
    CONDENSER("TIS|Manual|Console Blocks|Condenser", "CONSOLE_BLOCKS", "o"), //o
    CREEPER("TIS|Manual|Console Blocks|Creeper", "CONSOLE_BLOCKS", "p"), //p
    DOOR("TIS|Manual|Console Blocks|Door", "CONSOLE_BLOCKS", "D"), //D
    EPS("TIS|Manual|Console Blocks|EPS", "CONSOLE_BLOCKS", "P"), //P
    CONSOLE_FARM("TIS|Manual|Console Blocks|Farm", "CONSOLE_BLOCKS", "m"), //m
    HANDBRAKE("TIS|Manual|Console Blocks|Handbrake", "CONSOLE_BLOCKS", "k"), //k
    INFO("TIS|Manual|Console Blocks|Information System", "CONSOLE_BLOCKS_2", "I"), //I
    KEYBOARD("TIS|Manual|Console Blocks|Keyboard", "CONSOLE_BLOCKS_2", "K"), //K
    TOGGLE("TIS|Manual|Console Blocks 2|Wool toggle", "CONSOLE_BLOCKS_2", "o"), //o
    LIGHT("TIS|Manual|Console Blocks 2|Light", "CONSOLE_BLOCKS_2", "L"), //L
    CONSOLE_RAIL("TIS|Manual|Console Blocks 2|Rails", "CONSOLE_BLOCKS_2", "R"), //R
    SAVE_SIGN("TIS|Manual|Console Blocks 2|Save Sign", "CONSOLE_BLOCKS_2", "S"), //S
    SCANNER("TIS|Manual|Console Blocks 2|Scanner", "CONSOLE_BLOCKS_2", "c"), //c
    CONSOLE_STABLE("TIS|Manual|Console Blocks 2|Stable", "CONSOLE_BLOCKS_2", "b"), //t
    CONSOLE_STALL("TIS|Manual|Console Blocks 2|Stall", "CONSOLE_BLOCKS_2", "a"), //a
    TERMINAL("TIS|Manual|Console Blocks 2|Terminal", "CONSOLE_BLOCKS_2", "T"), //T
    TEMPORAL("TIS|Manual|Console Blocks 2|Temporal Locator", "CONSOLE_BLOCKS_2", "m"), //m
    WORLD_REPEATER("TIS|Manual|Console Blocks 2|World Repeater", "CONSOLE_BLOCKS_2", "W"), //W
    X_REPEATER("TIS|Manual|Console Blocks 2|X Repeater", "CONSOLE_BLOCKS_2", "X"), //X
    Y_REPEATER("TIS|Manual|Console Blocks 2|Y Repeater", "CONSOLE_BLOCKS_2", "Y"), //Y
    Z_REPEATER("TIS|Manual|Console Blocks 2|Z Repeater", "CONSOLE_BLOCKS_2", "Z"), //Z
    TARDIS_CONTROLS("TIS|Manual|tardis Controls", "MANUAL", "S"),
    MALFUNCTIONS("TIS|Manual|tardis Controls|Malfunctions", "TARDIS_CONTROLS", "M"),
    ALT_CONTROLS("TIS|Manual|tardis Controls|Alternative Controls", "TARDIS_CONTROLS", "l"),
    FOOD_ACCESSORIES("TIS|Food & Accessories", "TIS", "F"),
    CUSTARD("TIS|Food & Accessories|Custard", "FOOD_ACCESSORIES", "C"),
    CUSTARD_INFO("TIS|Food & Accessories|Custard|Info", "CUSTARD", "I"),
    CUSTARD_RECIPE("TIS|Food & Accessories|Custard|Recipe", "CUSTARD", "R"),
    FISH_FINGER("TIS|Food & Accessories|Fish Finger", "FOOD_ACCESSORIES", "F"),
    FISH_FINGER_INFO("TIS|Food & Accessories|Fish Finger|Info", "FISH_FINGER", "I"),
    FISH_FINGER_RECIPE("TIS|Food & Accessories|Fish Finger|Recipe", "FISH_FINGER", "R"),
    JAMMY_DODGER("TIS|Food & Accessories|Jammy Dodger", "FOOD_ACCESSORIES", "D"),
    JAMMY_DODGER_INFO("TIS|Food & Accessories|Jammy Dodger|Info", "JAMMY_DODGER", "I"),
    JAMMY_DODGER_RECIPE("TIS|Food & Accessories|Jammy Dodger|Recipe", "JAMMY_DODGER", "R"),
    JELLY_BABY("TIS|Food & Accessories|Jelly Baby", "FOOD_ACCESSORIES", "J"),
    JELLY_BABY_INFO("TIS|Food & Accessories|Jelly Baby|Info", "JELLY_BABY", "I"),
    JELLY_BABY_RECIPE("TIS|Food & Accessories|Jelly Baby|Recipe", "JELLY_BABY", "R"),
    BOW_TIE("TIS|Food & Accessories|Bow Tie", "FOOD_ACCESSORIES", "B"),
    BOW_TIE_INFO("TIS|Food & Accessories|Bow Tie|Info", "BOW_TIE", "I"),
    BOW_TIE_RECIPE("TIS|Food & Accessories|Bow Tie|Recipe", "BOW_TIE", "R"),
    GLASSES("TIS|Food & Accessories|3-D Glasses", "FOOD_ACCESSORIES", "G"),
    GLASSES_INFO("TIS|Food & Accessories|3-D Glasses|Info", "GLASSES", "I"),
    GLASSES_RECIPE("TIS|Food & Accessories|3-D Glasses|Recipe", "GLASSES", "R"),
    WATCH("TIS|Food & Accessories|Chameleon Arch Fob Watch", "FOOD_ACCESSORIES", "W"),
    WATCH_INFO("TIS|Food & Accessories|Chameleon Arch Fob Watch|Info", "WATCH", "I"),
    WATCH_RECIPE("TIS|Food & Accessories|Chameleon Arch Fob Watch|Recipe", "WATCH", "R"),
    PLANETS("TIS|Planets", "TIS", "P"),
    SKARO("TIS|Planets|Skaro", "PLANETS", "S"),
    SKARO_INFO("TIS|Planets|Skaro|Info", "SKARO", "I"),
    SKARO_MONSTERS("TIS|Planets|Skaro|Monsters", "SKARO", "M"),
    SKARO_ITEMS("TIS|Planets|Skaro|Items", "SKARO", "t"),
    SILURIA("TIS|Planets|Siluria", "PLANETS", "i"),
    SILURIA_INFO("TIS|Planets|Siluria|Info", "SILURIA", "I"),
    SILURIA_MONSTERS("TIS|Planets|Siluria|Monsters", "SILURIA", "M"),
    GALLIFREY("TIS|Planets|Gallifrey", "PLANETS", "G"),
    GALLIFREY_INFO("TIS|Planets|Gallifrey|Info", "GALLIFREY", "I"),
    GALLIFREY_MONSTERS("TIS|Planets|Gallifrey|Monsters", "GALLIFREY", "M"),
    RIFT_CIRCUIT("TIS|Planets|Skaro|Items|Rift Circuit", "SKARO_ITEMS", "R"),
    RIFT_CIRCUIT_INFO("TIS|Planets|Skaro|Items|Rift Circuit|Info", "RIFT_CIRCUIT", "I"),
    RIFT_CIRCUIT_RECIPE("TIS|Planets|Skaro|Items|Rift Circuit|Recipe", "RIFT_CIRCUIT", "R"),
    RIFT_MANIPULATOR("TIS|Planets|Skaro|Items|Rift Manipulator", "SKARO_ITEMS", "M"),
    RIFT_MANIPULATOR_INFO("TIS|Planets|Skaro|Items|Rift Manipulator|Info", "RIFT_MANIPULATOR", "I"),
    RIFT_MANIPULATOR_RECIPE("TIS|Planets|Skaro|Items|Rift Manipulator|Recipe", "RIFT_MANIPULATOR", "R"),
    RUST_BUCKET("TIS|Planets|Skaro|Items|Rust Bucket", "SKARO_ITEMS", "u"),
    RUST_BUCKET_INFO("TIS|Planets|Skaro|Items|Rust Bucket|Info", "RUST_BUCKET", "I"),
    RUST_BUCKET_RECIPE("TIS|Planets|Skaro|Items|Rust Bucket|Recipe", "RUST_BUCKET", "R"),
    RUST_PLAGUE_SWORD("TIS|Planets|Skaro|Items|Rust Plague Sword", "SKARO_ITEMS", "P"),
    RUST_PLAGUE_SWORD_INFO("TIS|Planets|Skaro|Items|Rust Plague Sword|Info", "RUST_PLAGUE_SWORD", "I"),
    RUST_PLAGUE_SWORD_RECIPE("TIS|Planets|Skaro|Items|Rust Plague Sword|Recipe", "RUST_PLAGUE_SWORD", "R"),
    ACID_BUCKET("TIS|Planets|Skaro|Items|Acid Bucket", "SKARO_ITEMS", "A"),
    ACID_BUCKET_INFO("TIS|Planets|Skaro|Items|Acid Bucket|Info", "ACID_BUCKET", "I"),
    ACID_BUCKET_RECIPE("TIS|Planets|Skaro|Items|Acid Bucket|Recipe", "ACID_BUCKET", "R"),
    ACID_BATTERY("TIS|Planets|Skaro|Items|Acid Battery", "SKARO_ITEMS", "c"),
    ACID_BATTERY_INFO("TIS|Planets|Skaro|Items|Acid Battery|Info", "ACID_BATTERY", "I"),
    ACID_BATTERY_RECIPE("TIS|Planets|Skaro|Items|Acid Battery|Recipe", "ACID_BATTERY", "R");

    private final String name;
    private final String parent;
    private final String key;

    TardisInfoMenu(String name, String parent, String key) {
        this.name = name;
        this.parent = parent;
        this.key = key;
    }

    /**
     * Attempts to get the children of the parent menu.
     *
     * @param parent the parent menu TARDISInfoMenu.toString();
     * @return a HashMap&lt;String, String&gt; of child menu items, and their (ALT)key
     */
    public static TreeMap<String, String> getChildren(String parent) {
        TreeMap<String, String> children = new TreeMap<>();
        for (TardisInfoMenu tim : values()) {
            if (tim.getParent().equals(parent)) {
                String[] crumbs = tim.getName().split("[|]");
                children.put(crumbs[crumbs.length - 1], tim.getKey());
            }
        }
        return children;
    }

    /**
     * Gets the menu name of this TARDISInfoMenu
     *
     * @return name of this TARDISInfoMenu
     */
    public String getName() {
        return name;
    }

    /**
     * Gets the parent menu of this TARDISInfoMenu
     *
     * @return parent of this TARDISInfoMenu
     */
    private String getParent() {
        return parent;
    }

    /**
     * Gets the menu key of this TARDISInfoMenu
     *
     * @return key of this TARDISInfoMenu
     */
    private String getKey() {
        return key;
    }
}
