/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.*;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.boss.BarFlag;
import org.bukkit.entity.EntityType;
import org.bukkit.loot.LootTable;
import org.bukkit.loot.LootTables;
import org.joml.AxisAngle4f;
import org.joml.Vector3f;

import java.util.*;

/**
 * One of the key features of a TARDIS is that the interior exists in a
 * dimension different from the exterior. The main application of this concept
 * is that they are bigger on the inside than the outside.
 *
 * @author eccentric_nz
 */
public class TARDISConstants {

    /**
     * Item Display entity zero rotation angle for transformations
     */
    public static final AxisAngle4f AXIS_ANGLE_ZERO = new AxisAngle4f(0, 0, 0, 0);

    /**
     * Item Display entity zero offset vector for transformations
     */
    public static final Vector3f VECTOR_ZERO = new Vector3f(0, 0, 0);

    /**
     * Item Display entity 0.25 scale vector for transformations
     */
    public static final Vector3f VECTOR_QUARTER = new Vector3f(0.25f, 0.25f, 0.25f);

    /**
     * Consoles which need to be higher
     */
    public static final List<String> HIGHER = Arrays.asList("redstone", "thirteenth", "factory", "copper");

    /**
     * Modelled console colours
     */
    public static final Set<String> COLOURS = Set.of("RUSTIC", "BROWN", "PINK", "MAGENTA", "PURPLE", "BLUE", "LIGHT_BLUE",
            "CYAN", "GREEN", "LIME", "YELLOW", "ORANGE", "RED", "WHITE", "BLACK", "GRAY", "LIGHT_GRAY");

    /**
     * GUI materials
     */
    public static final List<Material> GUI_IDS = Arrays.asList(
            Material.STONE, Material.GRANITE, Material.DIORITE, Material.ANDESITE, Material.GRASS_BLOCK, Material.DIRT, Material.PODZOL,
            Material.CRIMSON_NYLIUM, Material.WARPED_NYLIUM, Material.COBBLESTONE, Material.OAK_PLANKS, Material.CRIMSON_PLANKS,
            Material.WARPED_PLANKS, Material.BEDROCK, Material.SAND, Material.RED_SAND, Material.GRAVEL, Material.GOLD_ORE, Material.IRON_ORE,
            Material.COAL_ORE, Material.NETHER_GOLD_ORE, Material.ACACIA_LOG, Material.CRIMSON_STEM, Material.WARPED_STEM, Material.SPRUCE_WOOD,
            Material.BIRCH_LEAVES, Material.SPONGE, Material.GLASS, Material.LAPIS_ORE, Material.LAPIS_BLOCK, Material.DISPENSER, Material.SANDSTONE,
            Material.NOTE_BLOCK, Material.STICKY_PISTON, Material.COBWEB, Material.GOLD_BLOCK, Material.BRICKS, Material.TNT, Material.BOOKSHELF,
            Material.MOSSY_COBBLESTONE, Material.OBSIDIAN, Material.PURPUR_BLOCK, Material.SPAWNER, Material.CHEST, Material.DIAMOND_ORE,
            Material.DIAMOND_BLOCK, Material.CRAFTING_TABLE, Material.FURNACE, Material.REDSTONE_ORE, Material.ICE, Material.SNOW_BLOCK, Material.CACTUS,
            Material.CLAY, Material.JUKEBOX, Material.PUMPKIN, Material.NETHERRACK, Material.SOUL_SAND, Material.SOUL_SOIL, Material.BASALT,
            Material.GLOWSTONE, Material.JACK_O_LANTERN, Material.STONE_BRICKS, Material.CHISELED_STONE_BRICKS, Material.BROWN_MUSHROOM_BLOCK,
            Material.RED_MUSHROOM_BLOCK, Material.MUSHROOM_STEM, Material.MELON, Material.MYCELIUM, Material.NETHER_BRICKS,
            Material.ENCHANTING_TABLE, Material.END_PORTAL_FRAME, Material.END_STONE, Material.END_STONE_BRICKS, Material.REDSTONE_LAMP,
            Material.EMERALD_ORE, Material.ENDER_CHEST, Material.EMERALD_BLOCK, Material.COMMAND_BLOCK, Material.BEACON, Material.TRAPPED_CHEST,
            Material.REDSTONE_BLOCK, Material.NETHER_QUARTZ_ORE, Material.HOPPER, Material.QUARTZ_BLOCK, Material.QUARTZ_BRICKS, Material.DROPPER,
            Material.PINK_TERRACOTTA, Material.CYAN_TERRACOTTA, Material.RED_TERRACOTTA, Material.HAY_BLOCK, Material.TERRACOTTA, Material.COAL_BLOCK,
            Material.SLIME_BLOCK, Material.YELLOW_STAINED_GLASS, Material.BLACK_STAINED_GLASS, Material.PRISMARINE, Material.DARK_PRISMARINE,
            Material.SEA_LANTERN, Material.RED_SANDSTONE, Material.MAGMA_BLOCK, Material.NETHER_WART_BLOCK, Material.RED_NETHER_BRICKS,
            Material.BONE_BLOCK, Material.OBSERVER, Material.WHITE_GLAZED_TERRACOTTA, Material.ORANGE_GLAZED_TERRACOTTA,
            Material.BLUE_GLAZED_TERRACOTTA, Material.MAGENTA_CONCRETE, Material.LIGHT_BLUE_CONCRETE, Material.TUBE_CORAL_BLOCK,
            Material.BRAIN_CORAL_BLOCK, Material.BUBBLE_CORAL_BLOCK, Material.FIRE_CORAL_BLOCK, Material.HORN_CORAL_BLOCK, Material.BLUE_ICE
    );

    /**
     * Valid Chameleon blocks
     */
    public static final Set<Material> CHAMELEON_BLOCKS_VALID = getValidMaterials();
    /**
     * Bad Chameleon blocks that shouldn't be changed to
     */
    public static final Set<Material> CHAMELEON_BLOCKS_BAD = getBadMaterials();
    public static final List<Tag> CHAMELEON_TAGS_CHANGE = Arrays.asList(
            Tag.SLABS, Tag.STAIRS, Tag.TRAPDOORS, Tag.CLIMBABLE, Tag.WALLS, Tag.FLOWER_POTS
    );
    /**
     * Chameleon blocks that need to be changed to a full block
     */
    public static final Set<Material> CHAMELEON_BLOCKS_CHANGE = getChangeBlocks();
    /**
     * Chameleon blocks to check the surrounding blocks for a valid block to
     * change to
     */
    public static final Set<Material> CHAMELEON_BLOCKS_NEXT = makeNextMaterials();
    /**
     * Precious Chameleon blocks
     */
    public static final List<Material> CHAMELEON_BLOCKS_PRECIOUS = Arrays.asList(
            Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.TNT, Material.DIAMOND_BLOCK,
            Material.EMERALD_BLOCK, Material.REDSTONE_BLOCK, Material.COAL_BLOCK, Material.NETHERITE_BLOCK
    );
    /**
     * A list of vowels
     */
    public static final List<String> VOWELS = Arrays.asList("A", "E", "I", "O", "U");
    /**
     * A list of living entity types that can be reported in scanner results
     */
    public static final List<EntityType> ENTITY_TYPES = Arrays.asList(
            EntityType.ALLAY, EntityType.ARMOR_STAND, EntityType.AXOLOTL, EntityType.BAT, EntityType.BEE, EntityType.BLAZE, EntityType.CAMEL, EntityType.CAT,
            EntityType.CAVE_SPIDER, EntityType.CHICKEN, EntityType.COD, EntityType.COW, EntityType.CREEPER, EntityType.DOLPHIN, EntityType.DONKEY,
            EntityType.DROWNED, EntityType.ELDER_GUARDIAN, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.EVOKER, EntityType.FOX, EntityType.FROG,
            EntityType.GHAST, EntityType.GIANT, EntityType.GLOW_SQUID, EntityType.GOAT, EntityType.GUARDIAN, EntityType.HOGLIN, EntityType.HORSE,
            EntityType.HUSK, EntityType.ILLUSIONER, EntityType.IRON_GOLEM, EntityType.LLAMA, EntityType.MAGMA_CUBE, EntityType.MULE,
            EntityType.MOOSHROOM, EntityType.OCELOT, EntityType.PANDA, EntityType.PARROT, EntityType.PHANTOM, EntityType.PIG, EntityType.PIGLIN,
            EntityType.PIGLIN_BRUTE, EntityType.PILLAGER, EntityType.PLAYER, EntityType.POLAR_BEAR, EntityType.PUFFERFISH, EntityType.RABBIT,
            EntityType.RAVAGER, EntityType.SALMON, EntityType.SHEEP, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON,
            EntityType.SKELETON_HORSE, EntityType.SLIME, EntityType.SNIFFER, EntityType.SNOW_GOLEM, EntityType.SPIDER, EntityType.SQUID, EntityType.STRAY,
            EntityType.STRIDER, EntityType.TADPOLE, EntityType.TRADER_LLAMA, EntityType.TROPICAL_FISH, EntityType.TURTLE, EntityType.VEX, EntityType.VILLAGER,
            EntityType.VINDICATOR, EntityType.WANDERING_TRADER, EntityType.WARDEN, EntityType.WITCH, EntityType.WITHER, EntityType.WITHER_SKELETON,
            EntityType.WOLF, EntityType.ZOGLIN, EntityType.ZOMBIE, EntityType.ZOMBIE_HORSE, EntityType.ZOMBIE_VILLAGER, EntityType.ZOMBIFIED_PIGLIN
    );
    /**
     * A list of materials that can be replaced when the TARDIS lands
     */
    public static final Set<Material> GOOD_MATERIALS = getReplaceableMaterials();
    /**
     * A list of water materials types that can be replaced when the TARDIS
     * lands
     */
    public static final List<Material> GOOD_WATER = Arrays.asList(
            Material.AIR, Material.CAVE_AIR, Material.VOID_AIR, Material.WATER,
            Material.SEAGRASS, Material.TALL_SEAGRASS, Material.KELP_PLANT, Material.SEA_PICKLE
    );
    /**
     * A list of titles to display when a TARDIS Seed block is growing
     */
    public static final List<String> GROWTH_STATES = Arrays.asList(
            "Initiating block transfer mathematics", "Distributing cluster algebra", "Determining quantum probability/uncertainty",
            "Multiplying universal base code numerals", "Building artron mainframe module", "Assembling hyper-loom null-zone",
            "Complicating space-time events", "Forming extra-dimensional metastructure framework", "Finding dimensionally transcendental cradle",
            "Formatting organic protyon units", "Engineering warp matrix", "Inducting transducer power cells", "Implanting exitonic circuitry",
            "Kick-starting dynomorphic generator", "Regenerating molecular stabilizers", "Imprinting symbiotic nuclei", "Constructing temporal drive systems",
            "Choosing gender circuits", "Acquiring extra internal dimensions", "Stabilising Eye of Harmony", "Catalyzing huon particles",
            "Crystalising morphic fields", "Sterilizing life-support systems", "Zeroing transdimensional flux relay indicators",
            "Contrafibulating trachoidal crystals", "Overriding Vespian transmogrifier ratchet", "Installing time-warp anchorage unit",
            "Magnetising pseudo-timeline narrow focus coils", "Inserting translevel communications system", "Linking absolute tesseractulator",
            "Polishing telepathic induction circuits", "Preparing TARDIS factory exterior"
    );
    /**
     * BlockData for AIR
     */
    public static final BlockData AIR = Material.AIR.createBlockData();
    /**
     * BlockData for VOID_AIR
     */
    public static final BlockData VOID_AIR = Material.VOID_AIR.createBlockData();
    /**
     * BlockData for FIRE
     */
    public static final BlockData FIRE = Material.FIRE.createBlockData();
    /**
     * BlockData for lit REDSTONE_LAMP
     */
    public static final BlockData LAMP = Bukkit.createBlockData("minecraft:redstone_lamp[lit=true]");
    /**
     * BlockData for SNOW
     */
    public static final BlockData SNOW = Material.SNOW.createBlockData();
    /**
     * BlockData for Snow Block
     */
    public static final BlockData SNOW_BLOCK = Material.SNOW_BLOCK.createBlockData();
    /**
     * BlockData for GLASS
     */
    public static final BlockData GLASS = Material.GLASS.createBlockData();
    /**
     * BlockData for REDSTONE_BLOCK
     */
    public static final BlockData POWER = Material.REDSTONE_BLOCK.createBlockData();
    /**
     * BlockData for SEA_LANTERN
     */
    public static final BlockData LANTERN = Material.SEA_LANTERN.createBlockData();
    /**
     * BlockData for BLACK_WOOL
     */
    public static final BlockData BLACK = Material.BLACK_WOOL.createBlockData();
    /**
     * BlockData for DAYLIGHT_DETECTOR
     */
    public static final BlockData DAYLIGHT = Material.DAYLIGHT_DETECTOR.createBlockData();
    /**
     * BlockData for ICE
     */
    public static final BlockData ICE = Material.ICE.createBlockData();
    /**
     * BlockData for WATER
     */
    public static final BlockData WATER = Material.WATER.createBlockData();
    /**
     * BlockData for LAVA
     */
    public static final BlockData LAVA = Material.LAVA.createBlockData();
    /**
     * BlockData for police box LIGHT
     */
    public static final Levelled LIGHT = (Levelled) Material.LIGHT.createBlockData();
    /**
     * BlockData for Division LIGHT
     */
    public static final Levelled LIGHT_DIV = setLight(15);
    /**
     * BlockData for Item Display barrier blocks
     */
    public static final BlockData BARRIER = Material.BARRIER.createBlockData();
    /**
     * A list of loot tables to populate TARDIS planet chests
     */
    public static final List<LootTable> LOOT = Arrays.asList(
            LootTables.ABANDONED_MINESHAFT.getLootTable(), LootTables.BURIED_TREASURE.getLootTable(), LootTables.DESERT_PYRAMID.getLootTable(),
            LootTables.IGLOO_CHEST.getLootTable(), LootTables.JUNGLE_TEMPLE.getLootTable(), LootTables.SHIPWRECK_TREASURE.getLootTable(),
            LootTables.SIMPLE_DUNGEON.getLootTable(), LootTables.SPAWN_BONUS_CHEST.getLootTable(), LootTables.STRONGHOLD_LIBRARY.getLootTable(),
            LootTables.VILLAGE_ARMORER.getLootTable(), LootTables.VILLAGE_BUTCHER.getLootTable(), LootTables.VILLAGE_CARTOGRAPHER.getLootTable(),
            LootTables.VILLAGE_DESERT_HOUSE.getLootTable(), LootTables.VILLAGE_FISHER.getLootTable(), LootTables.VILLAGE_FLETCHER.getLootTable(),
            LootTables.VILLAGE_MASON.getLootTable(), LootTables.VILLAGE_PLAINS_HOUSE.getLootTable(), LootTables.VILLAGE_SAVANNA_HOUSE.getLootTable(),
            LootTables.VILLAGE_SHEPHERD.getLootTable(), LootTables.VILLAGE_SNOWY_HOUSE.getLootTable(), LootTables.VILLAGE_TAIGA_HOUSE.getLootTable(),
            LootTables.VILLAGE_TANNERY.getLootTable(), LootTables.VILLAGE_TEMPLE.getLootTable(), LootTables.VILLAGE_TOOLSMITH.getLootTable(),
            LootTables.VILLAGE_WEAPONSMITH.getLootTable(), LootTables.WOODLAND_MANSION.getLootTable(), LootTables.PILLAGER_OUTPOST.getLootTable()
    );
    /**
     * A random number generator for reuse
     */
    public static final Random RANDOM = new Random();
    /**
     * A list of particle dust options for use in TARDIS forcefields
     */
    public static final List<Particle.DustOptions> DUSTOPTIONS = Arrays.asList(
            new Particle.DustOptions(Color.fromRGB(0, 102, 255), 1), new Particle.DustOptions(Color.fromRGB(0, 153, 255), 1),
            new Particle.DustOptions(Color.fromRGB(0, 204, 255), 1), new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1),
            new Particle.DustOptions(Color.fromRGB(0, 255, 204), 1), new Particle.DustOptions(Color.fromRGB(0, 255, 153), 1),
            new Particle.DustOptions(Color.fromRGB(0, 255, 102), 1), new Particle.DustOptions(Color.fromRGB(0, 255, 153), 1),
            new Particle.DustOptions(Color.fromRGB(0, 255, 204), 1), new Particle.DustOptions(Color.fromRGB(0, 255, 255), 1),
            new Particle.DustOptions(Color.fromRGB(0, 204, 255), 1), new Particle.DustOptions(Color.fromRGB(0, 153, 255), 1)
    );
    /**
     * An empty BarFlag array for use with TARDIS boss bars
     */
    public static final BarFlag[] EMPTY_ARRAY = new BarFlag[0];
    /**
     * A list of materials used in ItemFrame Chameleon presets
     */
    public static final List<Material> DYES = Arrays.asList(
            Material.CYAN_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS_PANE, Material.WHITE_DYE, Material.ORANGE_DYE, Material.MAGENTA_DYE,
            Material.LIGHT_BLUE_DYE, Material.YELLOW_DYE, Material.LIME_DYE, Material.PINK_DYE, Material.GRAY_DYE, Material.LIGHT_GRAY_DYE,
            Material.CYAN_DYE, Material.PURPLE_DYE, Material.BLUE_DYE, Material.BROWN_DYE, Material.GREEN_DYE, Material.RED_DYE, Material.BLACK_DYE,
            Material.LEATHER_HORSE_ARMOR, Material.ENDER_PEARL
    );
    public static final UUID UUID_ZERO = new UUID(0, 0);

    private static Set<Material> getValidMaterials() {
        Set<Material> set = new HashSet<>();
        set.add(Material.BOOKSHELF);
        set.add(Material.BROWN_MUSHROOM);
        set.add(Material.COAL_BLOCK);
        set.add(Material.COBBLESTONE);
        set.add(Material.CRAFTING_TABLE);
        set.add(Material.DIAMOND_BLOCK);
        set.add(Material.EMERALD_BLOCK);
        set.add(Material.END_STONE);
        set.add(Material.GLOWSTONE);
        set.add(Material.GOLD_BLOCK);
        set.add(Material.IRON_BLOCK);
        set.add(Material.JUKEBOX);
        set.add(Material.LAPIS_BLOCK);
        set.add(Material.MELON);
        set.add(Material.MOSSY_COBBLESTONE);
        set.add(Material.NETHERRACK);
        set.add(Material.NETHER_BRICKS);
        set.add(Material.NOTE_BLOCK);
        set.add(Material.OBSIDIAN);
        set.add(Material.PUMPKIN);
        set.add(Material.QUARTZ_BLOCK);
        set.add(Material.REDSTONE_LAMP);
        set.add(Material.RED_MUSHROOM);
        set.add(Material.TNT);
        set.addAll(Tag.BEACON_BASE_BLOCKS.getValues());
        set.addAll(Tag.COAL_ORES.getValues());
        set.addAll(Tag.DIAMOND_ORES.getValues());
        set.addAll(Tag.EMERALD_ORES.getValues());
        set.addAll(Tag.GOLD_ORES.getValues());
        set.addAll(Tag.ICE.getValues());
        set.addAll(Tag.IMPERMEABLE.getValues());
        set.addAll(Tag.INFINIBURN_END.getValues());
        set.addAll(Tag.LAPIS_ORES.getValues());
        set.addAll(Tag.LEAVES.getValues());
        set.addAll(Tag.LOGS.getValues());
        set.addAll(Tag.MINEABLE_HOE.getValues());
        set.addAll(Tag.MINEABLE_SHOVEL.getValues());
        set.addAll(Tag.NETHER_CARVER_REPLACEABLES.getValues());
        set.addAll(Tag.OVERWORLD_CARVER_REPLACEABLES.getValues());
        set.addAll(Tag.PLANKS.getValues());
        set.addAll(Tag.REDSTONE_ORES.getValues());
        set.addAll(Tag.STONE_BRICKS.getValues());
        set.addAll(Tag.WOOL.getValues());
        set.remove(Material.SNOW);
        set.remove(Material.WATER);
        set.remove(Material.MOSS_CARPET);
        set.remove(Material.SCULK_SENSOR);
        set.remove(Material.SCULK_VEIN);
        set.remove(Material.SCULK_SHRIEKER);
        set.remove(Material.PINK_PETALS);
        set.removeAll(Tag.SAND.getValues());
        return set;
    }

    private static Set<Material> getBadMaterials() {
        Set<Material> set = new HashSet<>();
        set.add(Material.BREWING_STAND);
        set.add(Material.CAKE);
        set.add(Material.COCOA);
        set.add(Material.DISPENSER);
        set.add(Material.DRAGON_EGG);
        set.add(Material.ENCHANTING_TABLE);
        set.add(Material.END_PORTAL);
        set.add(Material.FIRE);
        set.add(Material.FURNACE);
        set.add(Material.IRON_BARS);
        set.add(Material.LAVA);
        set.add(Material.LILY_PAD);
        set.add(Material.NETHER_PORTAL);
        set.add(Material.NETHER_WART);
        set.add(Material.PISTON);
        set.add(Material.PISTON_HEAD);
        set.add(Material.REDSTONE_TORCH);
        set.add(Material.REDSTONE_WIRE);
        set.add(Material.REPEATER);
        set.add(Material.SPAWNER);
        set.add(Material.STICKY_PISTON);
        set.add(Material.SUGAR_CANE);
        set.add(Material.TRIPWIRE);
        set.add(Material.TRIPWIRE_HOOK);
        set.add(Material.WATER);
        set.addAll(Tag.ALL_SIGNS.getValues());
        set.addAll(Tag.BEDS.getValues());
        set.addAll(Tag.BUTTONS.getValues());
        set.addAll(Tag.CANDLE_CAKES.getValues());
        set.addAll(Tag.CAULDRONS.getValues());
        set.addAll(Tag.CLIMBABLE.getValues());
        set.addAll(Tag.CROPS.getValues());
        set.addAll(Tag.DOORS.getValues());
        set.addAll(Tag.FEATURES_CANNOT_REPLACE.getValues());
        set.addAll(Tag.FENCES.getValues());
        set.addAll(Tag.FENCE_GATES.getValues());
        set.addAll(Tag.PIGLIN_REPELLENTS.getValues());
        set.addAll(Tag.SAPLINGS.getValues());
        set.addAll(Tag.WALL_POST_OVERRIDE.getValues());
        return set;
    }

    private static Set<Material> getChangeBlocks() {
        Set<Material> set = new HashSet<>();
        set.add(Material.ATTACHED_MELON_STEM);
        set.add(Material.ATTACHED_PUMPKIN_STEM);
        set.add(Material.BLACK_CONCRETE_POWDER);
        set.add(Material.BLUE_CONCRETE_POWDER);
        set.add(Material.BROWN_CONCRETE_POWDER);
        set.add(Material.CYAN_CONCRETE_POWDER);
        set.add(Material.END_PORTAL_FRAME);
        set.add(Material.FARMLAND);
        set.add(Material.GRASS_BLOCK);
        set.add(Material.GRAVEL);
        set.add(Material.GRAY_CONCRETE_POWDER);
        set.add(Material.GREEN_CONCRETE_POWDER);
        set.add(Material.INFESTED_CHISELED_STONE_BRICKS);
        set.add(Material.INFESTED_COBBLESTONE);
        set.add(Material.INFESTED_CRACKED_STONE_BRICKS);
        set.add(Material.INFESTED_MOSSY_STONE_BRICKS);
        set.add(Material.INFESTED_STONE);
        set.add(Material.INFESTED_STONE_BRICKS);
        set.add(Material.IRON_BARS);
        set.add(Material.LIGHT_BLUE_CONCRETE_POWDER);
        set.add(Material.LIGHT_GRAY_CONCRETE_POWDER);
        set.add(Material.LIME_CONCRETE_POWDER);
        set.add(Material.MAGENTA_CONCRETE_POWDER);
        set.add(Material.MELON_STEM);
        set.add(Material.ORANGE_CONCRETE_POWDER);
        set.add(Material.PINK_CONCRETE_POWDER);
        set.add(Material.PUMPKIN_STEM);
        set.add(Material.PURPLE_CONCRETE_POWDER);
        set.add(Material.RED_CONCRETE_POWDER);
        set.add(Material.RED_SAND);
        set.add(Material.SAND);
        set.add(Material.SNOW);
        set.add(Material.WHITE_CONCRETE_POWDER);
        set.add(Material.YELLOW_CONCRETE_POWDER);
        set.addAll(Tag.CLIMBABLE.getValues());
        set.addAll(Tag.SLABS.getValues());
        set.addAll(Tag.STAIRS.getValues());
        set.addAll(Tag.TRAPDOORS.getValues());
        set.addAll(Tag.WALLS.getValues());
        return set;
    }

    public static Material changeToMaterial(Material material) {
        String original = material.toString();
        String[] split = original.split("_");
        String changed = "";
        switch (split[0]) {
            case "CACTUS" -> changed = "SANDSTONE";
            case "CAVE", "TWISTING", "WEEPING" -> changed = "MOSS_BLOCK";
            case "END" -> changed = "END_STONE";
            case "FARMLAND" -> changed = "DIRT";
            case "GRASS" -> changed = "DIRT";
            case "SHORT_GRASS" -> changed = "DIRT";
            case "GRAVEL" -> changed = "COARSE_DIRT";
            case "IRON" -> changed = "STONE";
            case "LADDER" -> changed = "BIRCH_PLANKS";
            case "PETRIFIED" -> changed = "OAK_PLANKS";
            case "SCAFFOLDING" -> changed = "BAMBOO_BLOCK";
            case "SNOW" -> changed = "SNOW_BLOCK";
            case "VINE" -> changed = "OAK_LEAVES";
            case "INFESTED" -> {
                changed = original.replace("INFESTED_", "");
            }
            default -> {
                String end = split[split.length - 1];
                switch (end) {
                    case "GATE" -> {
                        String tmp = original.replace("_FENCE_GATE", "");
                        if (tmp.endsWith("BRICK")) {
                            changed = tmp + "S";
                        } else if (tmp.endsWith("STONE")) {
                            changed = tmp;
                        } else if (isWood(tmp)) {
                            changed = tmp + "_PLANKS";
                        } else {
                            changed = tmp + "_BLOCK";
                        }
                    }
                    case "FENCE", "SLAB", "STAIRS", "WALL" -> {
                        String tmp = original.replace("_" + end, "");
                        if (tmp.endsWith("BRICK") || tmp.endsWith("TILE")) {
                            changed = tmp + "S";
                        } else if (tmp.startsWith("POLISHED")
                                || tmp.endsWith("ANDESITE")
                                || tmp.endsWith("COPPER")
                                || tmp.endsWith("DEEPSLATE")
                                || tmp.endsWith("DIORITE")
                                || tmp.endsWith("GRANITE")
                                || tmp.endsWith("PRISMARINE")
                                || tmp.endsWith("QUARTZ")
                                || tmp.endsWith("STONE")) {
                            changed = tmp;
                        } else if (isWood(tmp)) {
                            changed = tmp + "_PLANKS";
                        } else {
                            changed = tmp + "_BLOCK";
                        }
                    }
                    case "TRAPDOOR" -> {
                        if (split[0].equals("IRON")) {
                            changed = "STONE";
                        } else {
                            changed = original.replace("TRAPDOOR", "PLANKS");
                        }
                    }
                    case "POWDER" -> changed = original.replace("_POWDER", "");
                    case "SAND" -> changed = original + "STONE";
                    case "STEM" -> changed = (split[0].equals("ATTACHED")) ? split[1] : split[0];
                }
            }
        }
        try {
            return Material.valueOf(changed);
        } catch (IllegalArgumentException e) {
            TARDIS.plugin.getMessenger().message(TARDIS.plugin.getConsole(), TardisModule.WARNING, "changeToMaterial() failed with the string: " + changed + " from " + original);
            return Material.BLUE_WOOL;
        }
    }

    private static boolean isWood(String tmp) {
        for (Material plank : Tag.PLANKS.getValues()) {
            if (plank.toString().startsWith(tmp)) {
                return true;
            }
        }
        return false;
    }

    private static Set<Material> makeNextMaterials() {
        Set<Material> set = new HashSet<>();
        set.add(Material.AIR);
        set.add(Material.CAVE_AIR);
        set.add(Material.COBWEB);
        set.add(Material.DAYLIGHT_DETECTOR);
        set.add(Material.VOID_AIR);
        set.addAll(Tag.CROPS.getValues());
        set.addAll(Tag.FLOWERS.getValues());
        set.addAll(Tag.RAILS.getValues());
        set.addAll(Tag.REPLACEABLE.getValues());
        return set;
    }

    private static Set<Material> getReplaceableMaterials() {
        Set<Material> set = new HashSet<>();
        set.add(Material.AIR);
        set.add(Material.BROWN_MUSHROOM);
        set.add(Material.CAVE_AIR);
        set.add(Material.RED_MUSHROOM);
        set.add(Material.SNOW);
        set.add(Material.VOID_AIR);
        set.addAll(Tag.FLOWERS.getValues());
        set.addAll(Tag.LEAVES.getValues());
        set.addAll(Tag.REPLACEABLE.getValues());
        set.addAll(Tag.SAPLINGS.getValues());
        return set;
    }

    /**
     * Checks whether a world name is a TARDIS planet
     *
     * @param world the world name to check
     * @return true if the world is a TARDIS planet
     */
    public static boolean isTARDISPlanet(String world) {
        String w = world.toLowerCase(Locale.ROOT);
        return w.endsWith("gallifrey") || w.endsWith("siluria") || w.endsWith("skaro");
    }

    /**
     * Checks whether a world name is a TARDIS planet
     *
     * @param world the world name to check
     * @return true if the world is a TARDIS planet
     */
    public static boolean isTARDISPlanetExact(String world) {
        String w = world.toLowerCase(Locale.ROOT);
        return w.equals("gallifrey") || w.equals("siluria") || w.equals("skaro") || w.equals("rooms");
    }

    /**
     * BlockData setter for LIGHT
     */
    private static Levelled setLight(int level) {
        Levelled levelled = (Levelled) Material.LIGHT.createBlockData();
        levelled.setLevel(level);
        return levelled;
    }
}
