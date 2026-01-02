/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.info;

import java.util.TreeMap;

/**
 * Articles in the TARDIS Information System were accessed by typing in an initialism for the subject. Entering IF, for
 * example, brought up the database's index file.
 *
 * @author bootthanoo, eccentric_nz
 */
public enum TARDISInfoMenu {

    TIS("TARDIS Information System", "", "TARDIS"),
    ITEMS("TIS|Items", "TIS", "I"),
    KEY("TIS|Items|TARDIS Key", "ITEMS", "K"),
    KEY_INFO("TIS|Items|TARDIS Key|Info", "KEY", "I"),
    KEY_RECIPE("TIS|Items|TARDIS Key|Recipe", "KEY", "R"),
    SONIC_SCREWDRIVER("TIS|Items|Sonic Screwdriver", "ITEMS", "S"),
    SONIC_SCREWDRIVER_INFO("TIS|Items|Sonic Screwdriver|Info", "SONIC_SCREWDRIVER", "I"),
    SONIC_SCREWDRIVER_RECIPE("TIS|Items|Sonic Screwdriver|Recipe", "SONIC_SCREWDRIVER", "R"),
    SONIC_TYPES("TIS|Items|Sonic Screwdriver|Types", "SONIC_SCREWDRIVER", "T"),
    SONIC_STANDARD("TIS|Items|Sonic Screwdriver|Types|Standard Sonic", "SONIC_TYPES", "S"),
    SONIC_STANDARD_INFO("TIS|Items|Sonic Screwdriver|Types|Standard Sonic|Info", "SONIC_STANDARD", "I"),
    SONIC_BIO("TIS|Items|Sonic Screwdriver|Types|Bio-scanner Sonic", "SONIC_TYPES", "i"),
    SONIC_BIO_INFO("TIS|Items|Sonic Screwdriver|Types|Bio-scanner Sonic|Info", "SONIC_BIO", "I"),
    SONIC_REDSTONE("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic", "SONIC_TYPES", "R"),
    SONIC_REDSTONE_INFO("TIS|Items|Sonic Screwdriver|Types|Redstone Sonic|Info", "SONIC_REDSTONE", "I"),
    SONIC_DIAMOND("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic", "SONIC_TYPES", "D"),
    SONIC_DIAMOND_INFO("TIS|Items|Sonic Screwdriver|Types|Diamond Sonic|Info", "SONIC_DIAMOND", "I"),
    SONIC_EMERALD("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic", "SONIC_TYPES", "m"),
    SONIC_EMERALD_INFO("TIS|Items|Sonic Screwdriver|Types|Emerald Sonic|Info", "SONIC_EMERALD", "I"),
    SONIC_ADMIN("TIS|Items|Sonic Screwdriver|Types|Admin Sonic", "SONIC_TYPES", "A"),
    SONIC_ADMIN_INFO("TIS|Items|Sonic Screwdriver|Types|Admin Sonic|Info", "SONIC_ADMIN", "I"),
    SONIC_PAINTER("TIS|Items|Sonic Screwdriver|Types|Painter Sonic", "SONIC_TYPES", "P"),
    SONIC_PAINTER_INFO("TIS|Items|Sonic Screwdriver|Types|Painter Sonic|Info", "SONIC_PAINTER", "I"),
    SONIC_BRUSH("TIS|Items|Sonic Screwdriver|Types|Brush Sonic", "SONIC_TYPES", "B"),
    SONIC_BRUSH_INFO("TIS|Items|Sonic Screwdriver|Types|Brush Sonic|Info", "SONIC_BRUSH", "I"),
    SONIC_IGNITE("TIS|Items|Sonic Screwdriver|Types|Ignite Sonic", "SONIC_TYPES", "g"),
    SONIC_IGNITE_INFO("TIS|Items|Sonic Screwdriver|Types|Ignite Sonic|Info", "SONIC_IGNITE", "I"),
    SONIC_KNOCKBACK("TIS|Items|Sonic Screwdriver|Types|Knockback Sonic", "SONIC_TYPES", "K"),
    SONIC_KNOCKBACK_INFO("TIS|Items|Sonic Screwdriver|Types|Knockback Sonic|Info", "SONIC_KNOCKBACK", "I"),
    SONIC_PICKUP_ARROWS("TIS|Items|Sonic Screwdriver|Types|Pickup Arrows Sonic", "SONIC_TYPES", "c"),
    SONIC_PICKUP_ARROWS_INFO("TIS|Items|Sonic Screwdriver|Types|Pickup Arrows Sonic|Info", "SONIC_PICKUP_ARROWS", "I"),
    SONIC_CONVERSION("TIS|Items|Sonic Screwdriver|Types|Conversion Sonic", "SONIC_TYPES", "o"),
    SONIC_CONVERSION_INFO("TIS|Items|Sonic Screwdriver|Types|Conversion Sonic|Info", "SONIC_CONVERSION", "I"),
    LOCATOR("TIS|Items|TARDIS Locator", "ITEMS", "L"),
    LOCATOR_INFO("TIS|Items|TARDIS Locator|Info", "LOCATOR", "I"),
    LOCATOR_RECIPE("TIS|Items|TARDIS Locator|Recipe", "LOCATOR", "R"),
    STATTENHEIM_REMOTE("TIS|Items|Stattenheim Remote", "ITEMS", "R"),
    STATTENHEIM_REMOTE_INFO("TIS|Items|Stattenheim Remote|Info", "STATTENHEIM_REMOTE", "I"),
    STATTENHEIM_REMOTE_RECIPE("TIS|Items|Stattenheim Remote|Recipe", "STATTENHEIM_REMOTE", "R"),
    BIOME_READER("TIS|Items|TARDIS Biome Reader", "ITEMS", "B"),
    BIOME_READER_INFO("TIS|Items|TARDIS Biome Reader|Info", "BIOME_READER", "I"),
    BIOME_READER_RECIPE("TIS|Items|TARDIS Biome Reader|Recipe", "BIOME_READER", "R"),
    REMOTE_KEY("TIS|Items|TARDIS Remote Key", "ITEMS", "m"),
    REMOTE_KEY_INFO("TIS|Items|TARDIS Remote Key|Info", "REMOTE_KEY", "I"),
    REMOTE_KEY_RECIPE("TIS|Items|TARDIS Remote Key|Recipe", "REMOTE_KEY", "R"),
    ARTRON_CAPACITOR("TIS|Items|Artron Capacitor", "ITEMS", "C"),
    ARTRON_CAPACITOR_INFO("TIS|Items|Artron Capacitor|Info", "ARTRON_CAPACITOR", "I"),
    ARTRON_CAPACITOR_RECIPE("TIS|Items|Artron Capacitor|Recipe", "ARTRON_CAPACITOR", "R"),
    ARTRON_CAPACITOR_STORAGE("TIS|Items|Artron Capacitor", "ITEMS", "n"),
    ARTRON_CAPACITOR_STORAGE_INFO("TIS|Items|Artron Capacitor Storage|Info", "ARTRON_CAPACITOR_STORAGE", "I"),
    ARTRON_CAPACITOR_STORAGE_RECIPE("TIS|Items|Artron Capacitor Storage|Recipe", "ARTRON_CAPACITOR_STORAGE", "R"),
    TARDIS_TELEVISION("TIS|Items|TARDIS Television", "ITEMS", "T"),
    TARDIS_TELEVISION_INFO("TIS|Items|TARDIS Television|Info", "TARDIS_TELEVISION", "I"),
    TARDIS_TELEVISION_RECIPE("TIS|Items|TARDIS Television|Recipe", "TARDIS_TELEVISION", "R"),
    ARTRON_STORAGE_CELL("TIS|Items|Artron Storage Cell", "ITEMS", "A"),
    ARTRON_STORAGE_CELL_INFO("TIS|Items|Artron Storage Cell|Info", "ARTRON_STORAGE_CELL", "I"),
    ARTRON_STORAGE_CELL_RECIPE("TIS|Items|Artron Storage Cell|Recipe", "ARTRON_STORAGE_CELL", "R"),
    ARTRON_FURNACE("TIS|Items|Artron Furnace", "ITEMS", "u"),
    ARTRON_FURNACE_INFO("TIS|Items|Artron Furnace|Info", "ARTRON_FURNACE", "I"),
    ARTRON_FURNACE_RECIPE("TIS|Items|Artron Furnace|Recipe", "ARTRON_FURNACE", "R"),
    PERCEPTION_FILTER("TIS|Items|Perception Filter", "ITEMS", "F"),
    PERCEPTION_FILTER_INFO("TIS|Items|Perception Filter|Info", "PERCEPTION_FILTER", "I"),
    PERCEPTION_FILTER_RECIPE("TIS|Items|Perception Filter|Recipe", "PERCEPTION_FILTER", "R"),
    SONIC_GENERATOR("TIS|Items|Sonic Generator", "ITEMS", "G"),
    SONIC_GENERATOR_INFO("TIS|Items|Sonic Generator|Info", "SONIC_GENERATOR", "I"),
    SONIC_GENERATOR_RECIPE("TIS|Items|Sonic Generator|Recipe", "SONIC_GENERATOR", "R"),
    HANDLES("TIS|Items|Handles", "ITEMS", "H"),
    HANDLES_INFO("TIS|Items|Handles|Info", "HANDLES", "I"),
    HANDLES_RECIPE("TIS|Items|Handles|Recipe", "HANDLES", "R"),
    VORTEX_MANIPULATOR("TIS|Items|Vortex Manipulator", "ITEMS", "V"),
    VORTEX_MANIPULATOR_INFO("TIS|Items|Vortex Manipulator|Info", "VORTEX_MANIPULATOR", "I"),
    VORTEX_MANIPULATOR_RECIPE("TIS|Items|Vortex Manipulator|Recipe", "VORTEX_MANIPULATOR", "R"),
    SONIC_BLASTER("TIS|Items|Sonic Blaster", "ITEMS", "o"),
    SONIC_BLASTER_INFO("TIS|Items|Sonic Blaster|Info", "SONIC_BLASTER", "I"),
    SONIC_BLASTER_RECIPE("TIS|Items|Sonic Blaster|Recipe", "SONIC_BLASTER", "R"),
    COMPONENTS("TIS|Components", "TIS", "C"),
    ARS_CIRCUIT("TIS|Components|Architectural Reconfiguration System (ARS) Circuit", "COMPONENTS", "h"),
    ARS_CIRCUIT_INFO("TIS|Components|Architectural Reconfiguration System (ARS) Circuit|Info", "ARS_CIRCUIT", "I"),
    ARS_CIRCUIT_RECIPE("TIS|Components|Architectural Reconfiguration System (ARS) Circuit|Recipe", "ARS_CIRCUIT", "R"),
    CHAMELEON_CIRCUIT("TIS|Components|Chameleon Circuit", "COMPONENTS", "C"),
    CHAMELEON_CIRCUIT_INFO("TIS|Components|Chameleon Circuit|Info", "CHAMELEON_CIRCUIT", "I"),
    CHAMELEON_CIRCUIT_RECIPE("TIS|Components|Chameleon Circuit|Recipe", "CHAMELEON_CIRCUIT", "R"),
    INPUT_CIRCUIT("TIS|Components|Input Circuit", "COMPONENTS", "I"),
    INPUT_CIRCUIT_INFO("TIS|Components|Input Circuit|Info", "INPUT_CIRCUIT", "I"),
    INPUT_CIRCUIT_RECIPE("TIS|Components|Input Circuit|Recipe", "INPUT_CIRCUIT", "R"),
    INVISIBILITY_CIRCUIT("TIS|Components|Invisibility Circuit", "COMPONENTS", "v"),
    INVISIBILITY_CIRCUIT_INFO("TIS|Components|Invisibility Circuit|Info", "INVISIBILITY_CIRCUIT", "I"),
    INVISIBILITY_CIRCUIT_RECIPE("TIS|Components|Invisibility Circuit|Recipe", "INVISIBILITY_CIRCUIT", "R"),
    LOCATOR_CIRCUIT("TIS|Components|Locator Circuit", "COMPONENTS", "L"),
    LOCATOR_CIRCUIT_INFO("TIS|Components|Locator Circuit|Info", "LOCATOR_CIRCUIT", "I"),
    LOCATOR_CIRCUIT_RECIPE("TIS|Components|Locator Circuit|Recipe", "LOCATOR_CIRCUIT", "R"),
    MATERIALISATION_CIRCUIT("TIS|Components|Materialisation Circuit", "COMPONENTS", "M"),
    MATERIALISATION_CIRCUIT_INFO("TIS|Components|Materialisation Circuit|Info", "MATERIALISATION_CIRCUIT", "I"),
    MATERIALISATION_CIRCUIT_RECIPE("TIS|Components|Materialisation Circuit|Recipe", "MATERIALISATION_CIRCUIT", "R"),
    MEMORY_CIRCUIT("TIS|Components|Memory Circuit", "COMPONENTS", "y"),
    MEMORY_CIRCUIT_INFO("TIS|Components|Memory Circuit|Info", "MEMORY_CIRCUIT", "I"),
    MEMORY_CIRCUIT_RECIPE("TIS|Components|Memory Circuit|Recipe", "MEMORY_CIRCUIT", "R"),
    RANDOMISER_CIRCUIT("TIS|Components|Randomiser Circuit", "COMPONENTS", "o"),
    RANDOMISER_CIRCUIT_INFO("TIS|Components|Randomiser Circuit|Info", "RANDOMISER_CIRCUIT", "I"),
    RANDOMISER_CIRCUIT_RECIPE("TIS|Components|Randomiser Circuit|Recipe", "RANDOMISER_CIRCUIT", "R"),
    SCANNER_CIRCUIT("TIS|Components|Scanner Circuit", "COMPONENTS", "n"),
    SCANNER_CIRCUIT_INFO("TIS|Components|Scanner Circuit|Info", "SCANNER_CIRCUIT", "I"),
    SCANNER_CIRCUIT_RECIPE("TIS|Components|Scanner Circuit|Recipe", "SCANNER_CIRCUIT", "R"),
    STATTENHEIM_CIRCUIT("TIS|Components|Stattenheim Circuit", "COMPONENTS", "S"),
    STATTENHEIM_CIRCUIT_INFO("TIS|Components|Stattenheim Circuit|Info", "STATTENHEIM_CIRCUIT", "I"),
    STATTENHEIM_CIRCUIT_RECIPE("TIS|Components|Stattenheim Circuit|Recipe", "STATTENHEIM_CIRCUIT", "R"),
    TEMPORAL_CIRCUIT("TIS|Components|Temporal Circuit", "COMPONENTS", "T"),
    TEMPORAL_CIRCUIT_INFO("TIS|Components|Temporal Circuit|Info", "TEMPORAL_CIRCUIT", "I"),
    TEMPORAL_CIRCUIT_RECIPE("TIS|Components|Temporal Circuit|Recipe", "TEMPORAL_CIRCUIT", "R"),
    // Sonic components
    SONIC_COMPONENTS("TIS|Sonic Components", "TIS", "S"),
    BIO_SCANNER_CIRCUIT("TIS|Sonic Components|Bio-scanner Circuit", "SONIC_COMPONENTS", "B"),
    BIO_SCANNER_CIRCUIT_INFO("TIS|Sonic Components|Bio-scanner Circuit|Info", "BIO_SCANNER_CIRCUIT", "I"),
    BIO_SCANNER_CIRCUIT_RECIPE("TIS|Sonic Components|Bio-scanner Circuit|Recipe", "BIO_SCANNER_CIRCUIT", "R"),
    BRUSH_CIRCUIT("TIS|Sonic Components|Brush Circuit", "SONIC_COMPONENTS", "u"),
    BRUSH_CIRCUIT_INFO("TIS|Sonic Components|Brush Circuit|Info", "BRUSH_CIRCUIT", "I"),
    BRUSH_CIRCUIT_RECIPE("TIS|Sonic Components|Brush Circuit|Recipe", "BRUSH_CIRCUIT", "R"),
    CONVERSION_CIRCUIT("TIS|Sonic Components|Conversion Circuit", "SONIC_COMPONENTS", "n"),
    CONVERSION_CIRCUIT_INFO("TIS|Sonic Components|Conversion Circuit|Info", "CONVERSION_CIRCUIT", "I"),
    CONVERSION_CIRCUIT_RECIPE("TIS|Sonic Components|Conversion Circuit|Recipe", "CONVERSION_CIRCUIT", "R"),
    DIAMOND_DISRUPTOR_CIRCUIT("TIS|Sonic Components|Diamond Disruptor Circuit", "SONIC_COMPONENTS", "D"),
    DIAMOND_DISRUPTOR_CIRCUIT_INFO("TIS|Sonic Components|Diamond Disruptor Circuit|Info", "DIAMOND_DISRUPTOR_CIRCUIT", "I"),
    DIAMOND_DISRUPTOR_CIRCUIT_RECIPE("TIS|Sonic Components|Diamond Disruptor Circuit|Recipe", "DIAMOND_DISRUPTOR_CIRCUIT", "R"),
    EMERALD_ENVIRONMENT_CIRCUIT("TIS|Sonic Components|Emerald Environment Circuit", "SONIC_COMPONENTS", "m"),
    EMERALD_ENVIRONMENT_CIRCUIT_INFO("TIS|Sonic Components|Emerald Environment Circuit|Info", "EMERALD_ENVIRONMENT_CIRCUIT", "I"),
    EMERALD_ENVIRONMENT_CIRCUIT_RECIPE("TIS|Sonic Components|Emerald Environment Circuit|Recipe", "EMERALD_ENVIRONMENT_CIRCUIT", "R"),
    IGNITE_CIRCUIT("TIS|Sonic Components|Ignite Circuit", "SONIC_COMPONENTS", "I"),
    IGNITE_CIRCUIT_INFO("TIS|Sonic Components|Ignite Circuit|Info", "IGNITE_CIRCUIT", "I"),
    IGNITE_CIRCUIT_RECIPE("TIS|Sonic Components|Ignite Circuit|Recipe", "IGNITE_CIRCUIT", "R"),
    KNOCKBACK_CIRCUIT("TIS|Sonic Components|Knockback Circuit", "SONIC_COMPONENTS", "K"),
    KNOCKBACK_CIRCUIT_INFO("TIS|Sonic Components|Knockback Circuit|Info", "KNOCKBACK_CIRCUIT", "I"),
    KNOCKBACK_CIRCUIT_RECIPE("TIS|Sonic Components|Knockback Circuit|Recipe", "KNOCKBACK_CIRCUIT", "R"),
    PAINTER_CIRCUIT("TIS|Sonic Components|Painter Circuit", "SONIC_COMPONENTS", "P"),
    PAINTER_CIRCUIT_INFO("TIS|Sonic Components|Painter Circuit|Info", "PAINTER_CIRCUIT", "I"),
    PAINTER_CIRCUIT_RECIPE("TIS|Sonic Components|Painter Circuit|Recipe", "PAINTER_CIRCUIT", "R"),
    PERCEPTION_CIRCUIT("TIS|Components|Perception Circuit", "COMPONENTS", "P"),
    PERCEPTION_CIRCUIT_INFO("TIS|Components|Perception Circuit|Info", "PERCEPTION_CIRCUIT", "I"),
    PERCEPTION_CIRCUIT_RECIPE("TIS|Components|Perception Circuit|Recipe", "PERCEPTION_CIRCUIT", "R"),
    PICKUP_ARROWS_CIRCUIT("TIS|Sonic Components|Pickup Arrows Circuit", "SONIC_COMPONENTS", "c"),
    PICKUP_ARROWS_CIRCUIT_INFO("TIS|Sonic Components|Pickup Arrows Circuit|Info", "PICKUP_ARROWS_CIRCUIT", "I"),
    PICKUP_ARROWS_CIRCUIT_RECIPE("TIS|Sonic Components|Pickup Arrows Circuit|Recipe", "PICKUP_ARROWS_CIRCUIT", "R"),
    REDSTONE_ACTIVATOR_CIRCUIT("TIS|Sonic Components|Redstone Activator Circuit", "COMPONENTS", "R"),
    REDSTONE_ACTIVATOR_CIRCUIT_INFO("TIS|Sonic Components|Redstone Activator Circuit|Info", "REDSTONE_ACTIVATOR_CIRCUIT", "I"),
    REDSTONE_ACTIVATOR_CIRCUIT_RECIPE("TIS|Sonic Components|Redstone Activator Circuit|Recipe", "REDSTONE_ACTIVATOR_CIRCUIT", "R"),
    SERVER_ADMIN_CIRCUIT("TIS|Sonic Components|Admin Circuit", "SONIC_COMPONENTS", "A"),
    SERVER_ADMIN_CIRCUIT_INFO("TIS|Sonic Components|Admin Circuit|Info", "SERVER_ADMIN_CIRCUIT", "I"),
    SERVER_ADMIN_CIRCUIT_RECIPE("TIS|Sonic Components|Admin Circuit|Recipe", "SERVER_ADMIN_CIRCUIT", "R"),
    SONIC_OSCILLATOR("TIS|Sonic Components|Oscillator Circuit", "SONIC_COMPONENTS", "O"),
    SONIC_OSCILLATOR_INFO("TIS|Sonic Components|Oscillator Circuit|Info", "SONIC_OSCILLATOR", "I"),
    SONIC_OSCILLATOR_RECIPE("TIS|Sonic Components|Oscillator Circuit|Recipe", "SONIC_OSCILLATOR", "R"),
    DISKS("TIS|TARDIS Disks", "TIS", "D"),
    AREA_DISK("TIS|TARDIS Disks|Area Storage Disk", "DISKS", "A"),
    BLANK_STORAGE_DISK("TIS|TARDIS Disks|Blank Storage Disk", "DISKS", "B"),
    BLANK_STORAGE_DISK_INFO("TIS|TARDIS Disks|Blank Storage Disk|Info", "BLANK_STORAGE_DISK", "I"),
    BLANK_STORAGE_DISK_RECIPE("TIS|TARDIS Disks|Blank Storage Disk|Recipe", "BLANK_STORAGE_DISK", "R"),
    BIOME_STORAGE_DISK("TIS|TARDIS Disks|Biome Storage Disk", "DISKS", "i"),
    BIOME_STORAGE_DISK_INFO("TIS|TARDIS Disks|Biome Storage Disk|Info", "BIOME_STORAGE_DISK", "I"),
    BIOME_STORAGE_DISK_RECIPE("TIS|TARDIS Disks|Biome Storage Disk|Recipe", "BIOME_STORAGE_DISK", "R"),
    AUTHORISED_CONTROL_DISK("TIS|TARDIS Disks|Authorised Control Disk", "DISKS", "C"),
    AUTHORISED_CONTROL_DISK_INFO("TIS|TARDIS Disks|Authorised Control Disk|Info", "AUTHORISED_CONTROL_DISK", "I"),
    AUTHORISED_CONTROL_DISK_RECIPE("TIS|TARDIS Disks|Authorised Control Disk|Recipe", "AUTHORISED_CONTROL_DISK", "R"),
    PLAYER_STORAGE_DISK("TIS|TARDIS Disks|Player Storage Disk", "DISKS", "P"),
    PLAYER_STORAGE_DISK_INFO("TIS|TARDIS Disks|Player Storage Disk|Info", "PLAYER_STORAGE_DISK", "I"),
    PLAYER_STORAGE_DISK_RECIPE("TIS|TARDIS Disks|Player Storage Disk|Recipe", "PLAYER_STORAGE_DISK", "R"),
    PRESET_STORAGE_DISK("TIS|TARDIS Disks|Preset Storage Disk", "DISKS", "r"),
    PRESET_STORAGE_DISK_INFO("TIS|TARDIS Disks|Preset Storage Disk|Info", "PRESET_STORAGE_DISK", "I"),
    PRESET_STORAGE_DISK_RECIPE("TIS|TARDIS Disks|Preset Storage Disk|Recipe", "PRESET_STORAGE_DISK", "R"),
    SAVE_STORAGE_DISK("TIS|TARDIS Disks|Save Storage Disk", "DISKS", "S"),
    SAVE_STORAGE_DISK_INFO("TIS|TARDIS Disks|Save Storage Disk|Info", "SAVE_STORAGE_DISK", "I"),
    SAVE_STORAGE_DISK_RECIPE("TIS|TARDIS Disks|Save Storage Disk|Recipe", "SAVE_STORAGE_DISK", "R"),
    FLYING("TIS|Manual|Time Travel|Flying", "TIME_TRAVEL", "F"),
    CAMERA("TIS|Manual|Time Travel|Camera", "TIME_TRAVEL", "C"),
    ALT_CONTROLS("TIS|Manual|Time Travel|Alternative Controls", "TIME_TRAVEL", "A"),
    MALFUNCTIONS("TIS|Manual|Time Travel|Malfunctions", "TIME_TRAVEL", "M"),
    // consoles
    TYPES("TIS|TARDIS Types", "TIS", "T"),
    ARS("TIS|TARDIS Types|ARS", "TYPES", "A"),
    BIGGER("TIS|TARDIS Types|Bigger", "TYPES", "i"),
    BONE("TIS|TARDIS Types|Bone!", "TYPES", "!"),
    BUDGET("TIS|TARDIS Types|Budget", "TYPES", "B"),
    CAVE("TIS|TARDIS Types|Cave", "TYPES", "v"),
    COPPER_11TH("TIS|TARDIS Types|Copper 11th", "TYPES", "1"),
    CORAL("TIS|TARDIS Types|Coral", "TYPES", "o"),
    CURSED("TIS|TARDIS Types|Cursed+", "TYPES", "+"),
    DELTA("TIS|TARDIS Types|Delta=", "TYPES", "="),
    DELUXE("TIS|TARDIS Types|Deluxe", "TYPES", "D"),
    DIVISION("TIS|TARDIS Types|Division/", "TYPES", "/"),
    EIGHTH("TIS|TARDIS Types|8th", "TYPES", "8"),
    ELEVENTH("TIS|TARDIS Types|Eleventh", "TYPES", "l"),
    ENDER("TIS|TARDIS Types|Ender", "TYPES", "d"),
    FACTORY("TIS|TARDIS Types|Factory", "TYPES", "y"),
    FIFTEENTH("TIS|TARDIS Types|Fifteenth*", "TYPES", "*"),
    FUGITIVE("TIS|TARDIS Types|Fugitive", "TYPES", "u"),
    HOSPITAL("TIS|TARDIS Types|Hospital-", "TYPES", "-"),
    MASTER("TIS|TARDIS Types|Master", "TYPES", "M"),
    MECHANICAL("TIS|TARDIS Types|Mechanical^", "TYPES", "^"),
    PLANK("TIS|TARDIS Types|Plank", "TYPES", "P"),
    PYRAMID("TIS|TARDIS Types|Pyramid", "TYPES", "y"),
    REDSTONE("TIS|TARDIS Types|Redstone", "TYPES", "R"),
    ROTOR("TIS|TARDIS Types|Rotor^", "TYPES", "^"),
    RUSTIC("TIS|TARDIS Types|Ru$tic", "TYPES", "$"),
    SIDRAT("TIS|TARDIS Types|SIDR@T", "TYPES", "@"),
    STEAMPUNK("TIS|TARDIS Types|Steampunk", "TYPES", "S"),
    THIRTEENTH("TIS|TARDIS Types|Thirteenth", "TYPES", "n"),
    TOM("TIS|TARDIS Types|Tom", "TYPES", "T"),
    TWELFTH("TIS|TARDIS Types|Twelfth", "TYPES", "f"),
    WAR("TIS|TARDIS Types|War", "TYPES", "W"),
    WEATHERED("TIS|TARDIS Types|Weathered", "TYPES", "h"),
    ORIGINAL("TIS|TARDIS Types|Original", "TYPES", "g"),
    ANCIENT("TIS|TARDIS Types|Ancient~", "TYPES", "~"),
    CUSTOM("TIS|TARDIS Types|Custom", "TYPES", "C"),
    // rooms
    ROOMS("TIS|Rooms", "TIS", "R"),
    ROOMS_2("TIS|Rooms 2", "TIS", "2"),
    ALLAY("TIS|Rooms|Allay^", "ROOMS", "^"),
    ANTIGRAVITY("TIS|Rooms|Anti-gravity", "ROOMS", "A"),
    APIARY("TIS|Rooms|Apiary", "ROOMS", "ia"),
    AQUARIUM("TIS|Rooms|Aquarium", "ROOMS", "q"),
    ARBORETUM("TIS|Rooms|Arboretum", "ROOMS", "u"),
    BAKER("TIS|Rooms|Baker", "ROOMS", "B"),
    BAMBOO("TIS|Rooms|Bamboo", "ROOMS", "oo"),
    BEDROOM("TIS|Rooms|Bedroom", "ROOMS", "d"),
    BIRDCAGE("TIS|Rooms|Bird Cage", "ROOMS", "c"),
    CHEMISTRY("TIS|Rooms|Chemistry", "ROOMS", "he"),
    EMPTY("TIS|Rooms|Empty", "ROOMS", "y"),
    EYE("TIS|Rooms|Eye*", "ROOMS", "*"),
    FARM("TIS|Rooms|Farm", "ROOMS", "F"),
    GARDEN("TIS|Rooms|Garden", "ROOMS", "de"),
    GEODE("TIS|Rooms|Geode", "ROOMS", "eo"),
    GRAVITY("TIS|Rooms|Gravity", "ROOMS", "G"),
    GREENHOUSE("TIS|Rooms|Greenhouse", "ROOMS", "n"),
    HAPPY("TIS|Rooms|Happy", "ROOMS", "y"),
    HARMONY("TIS|Rooms|Harmony", "ROOMS", "H"),
    HUTCH("TIS|Rooms|Hutch", "ROOMS", "ut"),
    IGLOO("TIS|Rooms|Igloo", "ROOMS", "gl"),
    IISTUBIL("TIS|Rooms|Iistabul", "ROOMS", "Ii"),
    KITCHEN("TIS|Rooms|Kitchen", "ROOMS", "K"),
    LAVA("TIS|Rooms|Lava", "ROOMS", "av"),
    LAZARUS("TIS|Rooms 2|Lazarus", "ROOMS_2", "za"),
    LIBRARY("TIS|Rooms 2|Library", "ROOMS_2", "L"),
    MANGROVE("TIS|Rooms 2|Mangrove", "ROOMS_2", "ng"),
    MAZE("TIS|Rooms 2|Maze", "ROOMS_2", "ze"),
    MUSHROOM("TIS|Rooms 2|Mushroom", "ROOMS_2", "M"),
    NAUTILUS("TIS|Rooms 2|Nautilus", "ROOMS_2", "u"),
    NETHER("TIS|Rooms 2|Nether", "ROOMS_2", "th"),
    OBSERVATORY("TIS|Rooms 2|Observatory", "ROOMS_2", "y"),
    PASSAGE("TIS|Rooms 2|Passage", "ROOMS_2", "P"),
    PEN("TIS|Rooms 2|Pen", "ROOMS_2", "Pe"),
    POOL("TIS|Rooms 2|Pool", "ROOMS_2", "o"),
    RAIL("TIS|Rooms 2|Rail", "ROOMS_2", "R"),
    RENDERER("TIS|Rooms 2|External Renderer", "ROOMS_2", "x"),
    SHELL("TIS|Rooms 2|Shell", "ROOMS_2", "Sh"),
    SMELTER("TIS|Rooms 2|Smelter", "ROOMS_2", "lt"),
    STABLE("TIS|Rooms 2|Stable", "ROOMS_2", "S"),
    STALL("TIS|Rooms 2|Llama Stall", "ROOMS_2", "Ll"),
    SURGERY("TIS|Rooms 2|Surgery", "ROOMS_2", "rg"),
    TRENZALORE("TIS|Rooms 2|Trenzalore", "ROOMS_2", "T"),
    VAULT("TIS|Rooms 2|Vault", "ROOMS_2", "V"),
    VILLAGE("TIS|Rooms 2|Village", "ROOMS_2", "i"),
    WOOD("TIS|Rooms 2|Wood", "ROOMS_2", "W"),
    WORKSHOP("TIS|Rooms 2|Workshop", "ROOMS_2", "h"),
    ZERO("TIS|Rooms 2|Zero", "ROOMS_2", "er"),
    MANUAL("TIS|Manual", "TIS", "M"),
    COMMANDS("TIS|Commands", "TIS", "o"),
    TARDIS("TIS|Commands|TARDIS Commands", "COMMANDS", "T"),
    TARDIS_ABORT("TIS|Commands|TARDIS Commands|abort", "TARDIS", "ab"),
    TARDIS_ADD("TIS|Commands|TARDIS Commands|add", "TARDIS", "a"),
    TARDIS_CHAMELEON("TIS|Commands|TARDIS Commands|chameleon", "TARDIS", "c"),
    TARDIS_COMEHERE("TIS|Commands|TARDIS Commands|comehere", "TARDIS", "com"),
    TARDIS_DIRECTION("TIS|Commands|TARDIS Commands|direction", "TARDIS", "d"),
    TARDIS_EXTERMINATE("TIS|Commands|TARDIS Commands|exterminate", "TARDIS", "x"),
    TARDIS_FIND("TIS|Commands|TARDIS Commands|find", "TARDIS", "f"),
    TARDIS_HIDE("TIS|Commands|TARDIS Commands|hide", "TARDIS", "h"),
    TARDIS_HOME("TIS|Commands|TARDIS Commands|home", "TARDIS", "m"),
    TARDIS_INSIDE("TIS|Commands|TARDIS Commands|inside", "TARDIS", "i"),
    TARDIS_JETTISON("TIS|Commands|TARDIS Commands|jettison", "TARDIS", "j"),
    TARDIS_LAMPS("TIS|Commands|TARDIS Commands|lamps", "TARDIS", "la"),
    TARDIS_LIST("TIS|Commands|TARDIS Commands|list", "TARDIS", "l"),
    TARDIS_NAMEKEY("TIS|Commands|TARDIS Commands|namekey", "TARDIS", "k"),
    TARDIS_OCCUPY("TIS|Commands|TARDIS Commands|occupy", "TARDIS", "o"),
    TARDIS_REBUILD("TIS|Commands|TARDIS Commands|rebuild", "TARDIS", "b"),
    TARDIS_REMOVE("TIS|Commands|TARDIS Commands|remove", "TARDIS", "r"),
    TARDIS_REMOVESAVE("TIS|Commands|TARDIS Commands|removesave", "TARDIS", "rem"),
    TARDIS_RESCUE("TIS|Commands|TARDIS Commands|rescue", "TARDIS", "u"),
    TARDIS_ROOM("TIS|Commands|TARDIS Commands|room", "TARDIS", "roo"),
    TARDIS_SAVE("TIS|Commands|TARDIS Commands|save", "TARDIS", "s"),
    TARDIS_SECONDARY("TIS|Commands|TARDIS Commands|secondary", "TARDIS", "n"),
    TARDIS_SETDEST("TIS|Commands|TARDIS Commands|setdest", "TARDIS", "t"),
    TARDIS_UPDATE("TIS|Commands|TARDIS Commands|update", "TARDIS", "p"),
    TARDIS_VERSION("TIS|Commands|TARDIS Commands|version", "TARDIS", "v"),
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
    CONSOLE_BLOCKS_3("TIS|Manual|Console Blocks 3", "MANUAL", "k"),
    CONSOLE_ARS("TIS|Manual|Console Blocks|ARS", "CONSOLE_BLOCKS", "A"),
    ADVANCED("TIS|Manual|Console Blocks|Advanced Console", "CONSOLE_BLOCKS", "v"),
    STORAGE("TIS|Manual|Console Blocks|Disk Storage", "CONSOLE_BLOCKS", "S"),
    ARTRON("TIS|Manual|Console Blocks|Artron Energy Capacitor", "CONSOLE_BLOCKS", "r"),
    BACKDOOR("TIS|Manual|Console Blocks|Backdoor", "CONSOLE_BLOCKS", "B"),
    BUTTON("TIS|Manual|Console Blocks|Button", "CONSOLE_BLOCKS", "u"),
    CHAMELEON("TIS|Manual|Console Blocks|Chameleon", "CONSOLE_BLOCKS", "C"),
    CONDENSER("TIS|Manual|Console Blocks|Condenser", "CONSOLE_BLOCKS", "o"),
    DOOR("TIS|Manual|Console Blocks|Door", "CONSOLE_BLOCKS", "D"),
    HANDBRAKE("TIS|Manual|Console Blocks|Handbrake", "CONSOLE_BLOCKS", "k"),
    INFO("TIS|Manual|Console Blocks|Information System", "CONSOLE_BLOCKS_2", "I"),
    KEYBOARD("TIS|Manual|Console Blocks|Keyboard", "CONSOLE_BLOCKS_2", "K"),
    TOGGLE("TIS|Manual|Console Blocks 2|Wool toggle", "CONSOLE_BLOCKS_2", "o"),
    LIGHT("TIS|Manual|Console Blocks 2|Light", "CONSOLE_BLOCKS_2", "L"),
    SAVE_SIGN("TIS|Manual|Console Blocks 2|Save Sign", "CONSOLE_BLOCKS_2", "S"),
    SCANNER("TIS|Manual|Console Blocks 2|Scanner", "CONSOLE_BLOCKS_2", "c"),
    UPDATEABLE_BLOCKS("TIS|Manual|Updateable Blocks", "MANUAL", "U"),
    EPS("TIS|Manual|Updateable Blocks|EPS", "UPDATEABLE_BLOCKS", "P"),
    CREEPER("TIS|Manual|Updateable Blocks|Creeper", "UPDATEABLE_BLOCKS", "C"),
    UPDATEABLE_FARM("TIS|Manual|Updateable Blocks|Farm", "UPDATEABLE_BLOCKS", "F"),
    UPDATEABLE_RAIL("TIS|Manual|Updateable Blocks|Rails", "UPDATEABLE_BLOCKS", "R"),
    UPDATEABLE_STABLE("TIS|Manual|Updateable Blocks|Stable", "UPDATEABLE_BLOCKS", "l"),
    UPDATEABLE_STALL("TIS|Manual|Updateable Blocks|Stall", "UPDATEABLE_BLOCKS", "a"),
    UPDATEABLE_ALLAY("TIS|Manual|Updateable Blocks|Ally", "UPDATEABLE_BLOCKS", "y"),
    UPDATEABLE_BAMBOO("TIS|Manual|Updateable Blocks|Bamboo", "UPDATEABLE_BLOCKS", "B"),
    UPDATEABLE_BIRDCAGE("TIS|Manual|Updateable Blocks|Birdcage", "UPDATEABLE_BLOCKS", "g"),
    UPDATEABLE_FUEL("TIS|Manual|Updateable Blocks|Fuel", "UPDATEABLE_BLOCKS", "u"),
    UPDATEABLE_HUTCH("TIS|Manual|Updateable Blocks|Hutch", "UPDATEABLE_BLOCKS", "H"),
    UPDATEABLE_IGLOO("TIS|Manual|Updateable Blocks|Igloo", "UPDATEABLE_BLOCKS", "I"),
    UPDATEABLE_IISTUBIL("TIS|Manual|Updateable Blocks|Iistabul", "UPDATEABLE_BLOCKS", "Ii"),
    UPDATEABLE_LAVA("TIS|Manual|Updateable Blocks|Lava", "UPDATEABLE_BLOCKS", "v"),
    UPDATEABLE_PEN("TIS|Manual|Updateable Blocks|Pen", "UPDATEABLE_BLOCKS", "n"),
    UPDATEABLE_SMELT("TIS|Manual|Updateable Blocks|Smelt", "UPDATEABLE_BLOCKS", "S"),
    UPDATEABLE_VAULT("TIS|Manual|Updateable Blocks|Vault", "UPDATEABLE_BLOCKS", "Va"),
    UPDATEABLE_VILLAGE("TIS|Manual|Updateable Blocks|Village", "UPDATEABLE_BLOCKS", "Vi"),
    TERMINAL("TIS|Manual|Console Blocks 2|Terminal", "CONSOLE_BLOCKS_2", "T"),
    TEMPORAL("TIS|Manual|Console Blocks 2|Temporal Locator", "CONSOLE_BLOCKS_2", "m"),
    WORLD_REPEATER("TIS|Manual|Console Blocks 2|World Repeater", "CONSOLE_BLOCKS_2", "W"),
    X_REPEATER("TIS|Manual|Console Blocks 2|X Repeater", "CONSOLE_BLOCKS_2", "X"),
    Y_REPEATER("TIS|Manual|Console Blocks 2|Y Repeater", "CONSOLE_BLOCKS_2", "Y"),
    Z_REPEATER("TIS|Manual|Console Blocks 2|Z Repeater", "CONSOLE_BLOCKS_2", "Z"),
    EXTERIOR_LAMP_LEVEL_SWITCH("TIS|Manual|Console Blocks 3|Exterior Lamp Level Switch", "CONSOLE_BLOCKS_3", "x"),
    EXTERIOR_LAMP_LEVEL_SWITCH_INFO("TIS|Manual|Console Blocks 3|Exterior Lamp Level Switch|Info", "EXTERIOR_LAMP_LEVEL_SWITCH", "I"),
    EXTERIOR_LAMP_LEVEL_SWITCH_RECIPE("TIS|Manual|Console Blocks 3|Exterior Lamp Level Switch|Recipe", "EXTERIOR_LAMP_LEVEL_SWITCH", "R"),
    INTERIOR_LIGHT_LEVEL_SWITCH("TIS|Manual|Console Blocks 3|Interior Light Level Switch", "CONSOLE_BLOCKS_3", "I"),
    INTERIOR_LIGHT_LEVEL_SWITCH_INFO("TIS|Manual|Console Blocks 3|Interior Light Level Switch|Info", "INTERIOR_LIGHT_LEVEL_SWITCH", "I"),
    INTERIOR_LIGHT_LEVEL_SWITCH_RECIPE("TIS|Manual|Console Blocks 3|Interior Light Level Switch|Recipe", "INTERIOR_LIGHT_LEVEL_SWITCH", "R"),
    CHARGING_SENSOR("TIS|Manual|Console Blocks 3|Charging Sensor", "CONSOLE_BLOCKS_3", "C"),
    FLIGHT_SENSOR("TIS|Manual|Console Blocks 3|Flight Sensor", "CONSOLE_BLOCKS_3", "l"),
    HANDBRAKE_SENSOR("TIS|Manual|Console Blocks 3|Handbrake Sensor", "CONSOLE_BLOCKS_3", "H"),
    MALFUNCTION_SENSOR("TIS|Manual|Console Blocks 3|Malfunction Sensor", "CONSOLE_BLOCKS_3", "M"),
    POWER_SENSOR("TIS|Manual|Console Blocks 3|Power Sensor", "CONSOLE_BLOCKS_3", "P"),
    TARDIS_MONITOR("TIS|Manual|Console Blocks 3|Monitor", "CONSOLE_BLOCKS_3", "o"),
    TARDIS_MONITOR_INFO("TIS|Manual|Console Blocks 3|Monitor|Info", "TARDIS_MONITOR", "I"),
    TARDIS_MONITOR_RECIPE("TIS|Manual|Console Blocks 3|Monitor|Recipe", "TARDIS_MONITOR", "R"),
    MONITOR_FRAME("TIS|Manual|Console Blocks 3|Monitor Frame", "CONSOLE_BLOCKS_3", "F"),
    MONITOR_FRAME_INFO("TIS|Manual|Console Blocks 3|Monitor Frame|Info", "MONITOR_FRAME", "I"),
    MONITOR_FRAME_RECIPE("TIS|Manual|Console Blocks 3|Monitor Frame|Recipe", "MONITOR_FRAME", "R"),
    RELATIVITY_DIFFERENTIATOR("TIS|Manual|Console Blocks 3|Relativity Differentiator", "CONSOLE_BLOCKS_3", "R"),
    TARDIS_CONTROLS("TIS|Manual|TARDIS Controls", "MANUAL", "S"),
    FOOD_ACCESSORIES("TIS|Food & Accessories", "TIS", "F"),
    CUSTARD("TIS|Food & Accessories|Custard", "FOOD_ACCESSORIES", "C"),
    CUSTARD_INFO("TIS|Food & Accessories|Custard|Info", "CUSTARD", "I"),
    CUSTARD_RECIPE("TIS|Food & Accessories|Custard|Recipe", "CUSTARD", "R"),
    CUSTARD_CREAM("TIS|Food & Accessories|Custard Cream", "FOOD_ACCESSORIES", "m"),
    CUSTARD_CREAM_INFO("TIS|Food & Accessories|Custard Cream|Info", "CUSTARD_CREAM", "I"),
    CUSTARD_CREAM_RECIPE("TIS|Food & Accessories|Custard Cream|Recipe", "CUSTARD_CREAM", "R"),
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
    THREE_D_GLASSES("TIS|Food & Accessories|3-D Glasses", "FOOD_ACCESSORIES", "G"),
    THREE_D_GLASSES_INFO("TIS|Food & Accessories|3-D Glasses|Info", "THREE_D_GLASSES", "I"),
    THREE_D_GLASSES_RECIPE("TIS|Food & Accessories|3-D Glasses|Recipe", "THREE_D_GLASSES", "R"),
    FOB_WATCH("TIS|Food & Accessories|Chameleon Arch Fob Watch", "FOOD_ACCESSORIES", "W"),
    FOB_WATCH_INFO("TIS|Food & Accessories|Chameleon Arch Fob Watch|Info", "FOB_WATCH", "I"),
    FOB_WATCH_RECIPE("TIS|Food & Accessories|Chameleon Arch Fob Watch|Recipe", "FOB_WATCH", "R"),

    COMMUNICATOR("TIS|Food & Accessories|TARDIS Communicator", "FOOD_ACCESSORIES", "u"),
    COMMUNICATOR_INFO("TIS|Food & Accessories|TARDIS Communicator|Info", "COMMUNICATOR", "I"),
    COMMUNICATOR_RECIPE("TIS|Food & Accessories|TARDIS Communicator|Recipe", "COMMUNICATOR", "R"),
    SPACE_HELMET("TIS|Food & Accessories|TARDIS Space Helmet", "FOOD_ACCESSORIES", "H"),
    SPACE_HELMET_INFO("TIS|Food & Accessories|TARDIS Space Helmet|Info", "SPACE_HELMET", "I"),
    SPACE_HELMET_RECIPE("TIS|Food & Accessories|TARDIS Space Helmet|Recipe", "SPACE_HELMET", "R"),
    PLANETS("TIS|Planets", "TIS", "P"),
    SKARO("TIS|Planets|Skaro", "PLANETS", "S"),
    SKARO_INFO("TIS|Planets|Skaro|Info", "SKARO", "I"),
    SKARO_MONSTERS("TIS|Planets|Skaro|Monsters", "SKARO", "M"),
    SKARO_ITEMS("TIS|Planets|Skaro|Items", "", "t"),
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
    ACID_BATTERY_RECIPE("TIS|Planets|Skaro|Items|Acid Battery|Recipe", "ACID_BATTERY", "R"),
    MONSTERS("TIS|Monsters", "MONSTERS", "n"),
    CYBERMAN("TIS|Monsters|Cyberman", "MONSTERS", "C"),
    DALEK("TIS|Monsters|Dalek", "MONSTERS", "D"),
    DALEK_SEC("TIS|Monsters|Dalek Sec", "MONSTERS", "a"),
    DAVROS("TIS|Monsters|Davros", "MONSTERS", "av"),
    EMPTY_CHILD("TIS|Monsters|Empty Child", "MONSTERS", "p"),
    HATH("TIS|Monsters|Hath", "MONSTERS", "H"),
    HEADLESS_MONK("TIS|Monsters|Headless Monk", "MONSTERS", "k"),
    ICE_WARRIOR("TIS|Monsters|Ice Warrior", "MONSTERS", "I"),
    JUDOON("TIS|Monsters|Judoon", "MONSTERS", "J"),
    K9("TIS|Monsters|K9", "MONSTERS", "K"),
    K9_INFO("TIS|Monsters|K9|Info", "K9", "I"),
    K9_RECIPE("TIS|Monsters|K9|Recipe", "K9", "R"),
    MIRE("TIS|Monsters|Mire", "MONSTERS", "M"),
    OOD("TIS|Monsters|Ood", "MONSTERS", "O"),
    RACNOSS("TIS|Monsters|Racnoss", "MONSTERS", "R"),
    SEA_DEVIL("TIS|Monsters|Sea Devil", "MONSTERS", "S"),
    SILENT("TIS|Monsters|Silent", "MONSTERS", "l"),
    SILURIAN("TIS|Monsters|Silurian", "MONSTERS", "n"),
    SLITHEEN("TIS|Monsters|Slitheen", "MONSTERS", "li"),
    SONTARAN("TIS|Monsters|Sontaran", "MONSTERS", "on"),
    STRAX("TIS|Monsters|Strax", "MONSTERS", "x"),
    TOCLAFANE("TIS|Monsters|Toclafane", "MONSTERS", "f"),
    VASHTA_NERADA("TIS|Monsters|Vashta Nerada", "MONSTERS", "V"),
    WEEPING_ANGEL("TIS|Monsters|Weeping Angel", "MONSTERS", "W"),
    ZYGON("TIS|Monsters|Zygon", "MONSTERS", "Z");

    private final String name;
    private final String parent;
    private final String key;

    TARDISInfoMenu(String name, String parent, String key) {
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
        for (TARDISInfoMenu tim : values()) {
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
    public String getParent() {
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

    public boolean isConsoleBlock() {
        switch (this) {
            case ARS, CONSOLE_ARS, ADVANCED, STORAGE, ARTRON, BACKDOOR, BUTTON, CHAMELEON, CONDENSER,
                    DOOR, HANDBRAKE, INFO, KEYBOARD, TOGGLE, LIGHT, SAVE_SIGN, SCANNER, TERMINAL,
                    TEMPORAL, WORLD_REPEATER, X_REPEATER, Y_REPEATER, Z_REPEATER, EXTERIOR_LAMP_LEVEL_SWITCH,
                    INTERIOR_LIGHT_LEVEL_SWITCH, CHARGING_SENSOR, FLIGHT_SENSOR, HANDBRAKE_SENSOR,
                    MALFUNCTION_SENSOR, POWER_SENSOR, TARDIS_MONITOR, MONITOR_FRAME, RELATIVITY_DIFFERENTIATOR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isUpdateable() {
        switch (this) {
            case CREEPER, EPS, UPDATEABLE_FARM, UPDATEABLE_RAIL, UPDATEABLE_STABLE, UPDATEABLE_STALL,
                    UPDATEABLE_ALLAY, UPDATEABLE_BAMBOO, UPDATEABLE_BIRDCAGE, UPDATEABLE_FUEL, UPDATEABLE_HUTCH,
                    UPDATEABLE_IGLOO, UPDATEABLE_IISTUBIL, UPDATEABLE_LAVA, UPDATEABLE_PEN, UPDATEABLE_SMELT,
                    UPDATEABLE_VAULT, UPDATEABLE_VILLAGE -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isItem() {
        switch (this) {
            case KEY, SONIC_SCREWDRIVER, LOCATOR, STATTENHEIM_REMOTE, BIOME_READER, REMOTE_KEY, ARTRON_CAPACITOR, ARTRON_STORAGE_CELL,
                    ARTRON_FURNACE, PERCEPTION_FILTER, SONIC_GENERATOR, SONIC_BLASTER, VORTEX_MANIPULATOR, HANDLES -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isSonicUpgrade() {
        switch (this) {
            case SONIC_STANDARD, SONIC_BIO, SONIC_REDSTONE, SONIC_DIAMOND, SONIC_EMERALD, SONIC_ADMIN, SONIC_PAINTER,
                    SONIC_BRUSH, SONIC_IGNITE, SONIC_KNOCKBACK, SONIC_PICKUP_ARROWS, SONIC_CONVERSION -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isSonicComponent() {
        switch (this) {
            case BIO_SCANNER_CIRCUIT, BRUSH_CIRCUIT, CONVERSION_CIRCUIT, DIAMOND_DISRUPTOR_CIRCUIT,
                    EMERALD_ENVIRONMENT_CIRCUIT, IGNITE_CIRCUIT, KNOCKBACK_CIRCUIT, PAINTER_CIRCUIT, PERCEPTION_CIRCUIT,
                    PICKUP_ARROWS_CIRCUIT, REDSTONE_ACTIVATOR_CIRCUIT, SERVER_ADMIN_CIRCUIT, SONIC_OSCILLATOR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isComponent() {
        switch (this) {
            case ARS_CIRCUIT, CHAMELEON_CIRCUIT, INPUT_CIRCUIT, INVISIBILITY_CIRCUIT, LOCATOR_CIRCUIT, MATERIALISATION_CIRCUIT,
                    MEMORY_CIRCUIT, RANDOMISER_CIRCUIT, SCANNER_CIRCUIT, STATTENHEIM_CIRCUIT, TEMPORAL_CIRCUIT -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isDisk() {
        switch (this) {
            case AREA_DISK, AUTHORISED_CONTROL_DISK, BLANK_STORAGE_DISK, BIOME_STORAGE_DISK,
                    PLAYER_STORAGE_DISK, PRESET_STORAGE_DISK, SAVE_STORAGE_DISK -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isConsole() {
        switch (this) {
            case ARS, BIGGER, BONE, BUDGET, CAVE, COPPER_11TH, CORAL, CURSED, DELTA, DELUXE, DIVISION, EIGHTH, ELEVENTH,
                 ENDER, FACTORY, FIFTEENTH, FUGITIVE, HOSPITAL, MASTER, MECHANICAL, PLANK, PYRAMID, REDSTONE, ROTOR,
                 RUSTIC, STEAMPUNK, SIDRAT, THIRTEENTH, TOM, TWELFTH, WAR, WEATHERED, ORIGINAL, ANCIENT, CUSTOM -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isRoom() {
        switch (this) {
            case ALLAY, ANTIGRAVITY, APIARY, AQUARIUM, ARBORETUM, BAKER, BAMBOO, BEDROOM, BIRDCAGE, CHEMISTRY, EMPTY,
                 EYE, FARM, GARDEN, GEODE, GRAVITY, GREENHOUSE, HAPPY, HARMONY, HUTCH, IGLOO, IISTUBIL, KITCHEN,
                 LAZARUS, LAVA, LIBRARY, MANGROVE, MAZE, MUSHROOM, NAUTILUS, NETHER, OBSERVATORY, PASSAGE, PEN, POOL,
                 RAIL, RENDERER, SHELL, SMELTER, STABLE, STALL, SURGERY, TRENZALORE, VAULT, VILLAGE, WOOD, WORKSHOP,
                 ZERO -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isAccessory() {
        switch (this) {
            case BOW_TIE, THREE_D_GLASSES, FOB_WATCH, COMMUNICATOR -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isFood() {
        switch (this) {
            case CUSTARD, FISH_FINGER, JAMMY_DODGER, JELLY_BABY, CUSTARD_CREAM -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isPlanet() {
        switch (this) {
            case SKARO, SILURIA, GALLIFREY, RIFT_CIRCUIT, RIFT_MANIPULATOR, RUST_BUCKET,
                    RUST_PLAGUE_SWORD, ACID_BUCKET, ACID_BATTERY -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isTimeTravel() {
        switch (this) {
            case MALFUNCTIONS, ALT_CONTROLS, CAMERA, FLYING -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }

    public boolean isMonster() {
        switch (this) {
            case CYBERMAN, DALEK, DALEK_SEC, DAVROS, EMPTY_CHILD, HATH, HEADLESS_MONK, ICE_WARRIOR,
                    JUDOON, K9, MIRE, OOD, RACNOSS, SEA_DEVIL, SILENT, SILURIAN, SLITHEEN, SONTARAN,
                    STRAX, TOCLAFANE, VASHTA_NERADA, WEEPING_ANGEL, ZYGON -> {
                return true;
            }
            default -> {
                return false;
            }
        }
    }
}
