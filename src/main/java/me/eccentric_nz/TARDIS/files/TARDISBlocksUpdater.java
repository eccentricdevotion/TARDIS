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
package me.eccentric_nz.TARDIS.files;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISBlocksUpdater {

    private final TARDIS plugin;
    private final FileConfiguration blocks_config;

    public TARDISBlocksUpdater(TARDIS plugin, FileConfiguration blocks_config) {
        this.plugin = plugin;
        this.blocks_config = blocks_config;
    }

    public void checkBlocksConfig() {
        int i = 0;
        if (!blocks_config.contains("version") || blocks_config.getInt("version") < 6) {
            List<String> MIDDLE_BLOCKS = Arrays.asList("ACACIA_LOG", "ACACIA_PLANKS", "ANDESITE", "BIRCH_LOG", "BIRCH_PLANKS", "BLACK_CONCRETE", "BLACK_GLAZED_TERRACOTTA", "BLACK_TERRACOTTA", "BLACK_WOOL", "BLUE_CONCRETE", "BLUE_GLAZED_TERRACOTTA", "BLUE_TERRACOTTA", "BLUE_WOOL", "BONE_BLOCK", "BRICKS", "BROWN_CONCRETE", "BROWN_GLAZED_TERRACOTTA", "BROWN_MUSHROOM_BLOCK", "BROWN_TERRACOTTA", "BROWN_WOOL", "CHISELED_QUARTZ_BLOCK", "CHISELED_RED_SANDSTONE", "CHISELED_SANDSTONE", "CHISELED_STONE_BRICKS", "CLAY", "COARSE_DIRT", "COBBLESTONE", "CRACKED_STONE_BRICKS", "CYAN_CONCRETE", "CYAN_GLAZED_TERRACOTTA", "CYAN_TERRACOTTA", "CYAN_WOOL", "DARK_OAK_LOG", "DARK_OAK_PLANKS", "DARK_PRISMARINE", "DIORITE", "DIRT", "END_STONE", "END_STONE_BRICKS", "GRANITE", "GRAY_CONCRETE", "GRAY_GLAZED_TERRACOTTA", "GRAY_TERRACOTTA", "GRAY_WOOL", "GREEN_CONCRETE", "GREEN_GLAZED_TERRACOTTA", "GREEN_TERRACOTTA", "GREEN_WOOL", "HAY_BLOCK", "JUNGLE_LOG", "JUNGLE_PLANKS", "LIGHT_BLUE_CONCRETE", "LIGHT_BLUE_GLAZED_TERRACOTTA", "LIGHT_BLUE_TERRACOTTA", "LIGHT_BLUE_WOOL", "LIGHT_GRAY_CONCRETE", "LIGHT_GRAY_GLAZED_TERRACOTTA", "LIGHT_GRAY_TERRACOTTA", "LIGHT_GRAY_WOOL", "LIME_CONCRETE", "LIME_GLAZED_TERRACOTTA", "LIME_TERRACOTTA", "LIME_WOOL", "MAGENTA_CONCRETE", "MAGENTA_GLAZED_TERRACOTTA", "MAGENTA_TERRACOTTA", "MAGENTA_WOOL", "MOSSY_COBBLESTONE", "MOSSY_STONE_BRICKS", "MUSHROOM_STEM", "NETHER_BRICKS", "NETHER_WART_BLOCK", "NETHERRACK", "OAK_LOG", "OAK_PLANKS", "ORANGE_CONCRETE", "ORANGE_GLAZED_TERRACOTTA", "ORANGE_TERRACOTTA", "ORANGE_WOOL", "PACKED_ICE", "PINK_CONCRETE", "PINK_GLAZED_TERRACOTTA", "PINK_TERRACOTTA", "PINK_WOOL", "PODZOL", "POLISHED_ANDESITE", "POLISHED_DIORITE", "POLISHED_GRANITE", "PRISMARINE", "PRISMARINE_BRICKS", "PURPLE_CONCRETE", "PURPLE_GLAZED_TERRACOTTA", "PURPLE_TERRACOTTA", "PURPLE_WOOL", "PURPUR_BLOCK", "PURPUR_PILLAR", "QUARTZ_BLOCK", "QUARTZ_PILLAR", "RED_CONCRETE", "RED_GLAZED_TERRACOTTA", "RED_MUSHROOM_BLOCK", "RED_NETHER_BRICKS", "RED_SANDSTONE", "RED_TERRACOTTA", "RED_WOOL", "SANDSTONE", "SMOOTH_QUARTZ", "SMOOTH_RED_SANDSTONE", "SMOOTH_SANDSTONE", "SOUL_SAND", "SPRUCE_LOG", "SPRUCE_PLANKS", "STONE", "STONE_BRICKS", "TERRACOTTA", "WHITE_CONCRETE", "WHITE_GLAZED_TERRACOTTA", "WHITE_TERRACOTTA", "WHITE_WOOL", "YELLOW_CONCRETE", "YELLOW_GLAZED_TERRACOTTA", "YELLOW_TERRACOTTA", "YELLOW_WOOL");
            blocks_config.set("tardis_blocks", MIDDLE_BLOCKS);
            List<String> LAMP_BLOCKS = Arrays.asList("TORCH", "REDSTONE_TORCH", "GLOWSTONE", "JACK_O_LANTERN", "REDSTONE_LAMP", "SEA_LANTERN");
            blocks_config.set("lamp_blocks", LAMP_BLOCKS);
            blocks_config.set("version", 6);
            i += 4;
        }
        if (!blocks_config.contains("version") || blocks_config.getInt("version") < 7) {
            List<String> CHAM_BLOCKS = Arrays.asList("STONE", "DIRT", "COARSE_DIRT", "PODZOL", "COBBLESTONE", "OAK_PLANKS", "BIRCH_PLANKS", "SPRUCE_PLANKS", "JUNGLE_PLANKS", "ACACIA_PLANKS", "DARK_OAK_PLANKS", "GOLD_ORE", "IRON_ORE", "COAL_ORE", "OAK_LOG", "BIRCH_LOG", "SPRUCE_LOG", "JUNGLE_LOG", "ACACIA_LOG", "DARK_OAK_LOG", "STRIPPED_OAK_LOG", "STRIPPED_BIRCH_LOG", "STRIPPED_SPRUCE_LOG", "STRIPPED_JUNGLE_LOG", "STRIPPED_ACACIA_LOG", "STRIPPED_DARK_OAK_LOG", "OAK_LEAVES", "BIRCH_LEAVES", "SPRUCE_LEAVES", "JUNGLE_LEAVES", "ACACIA_LEAVES", "DARK_OAK_LEAVES", "SPONGE", "GLASS", "LAPIS_ORE", "LAPIS_BLOCK", "SANDSTONE", "NOTE_BLOCK", "WHITE_WOOL", "ORANGE_WOOL", "MAGENTA_WOOL", "LIGHT_BLUE_WOOL", "YELLOW_WOOL", "LIME_WOOL", "PINK_WOOL", "GRAY_WOOL", "LIGHT_GRAY_WOOL", "CYAN_WOOL", "PURPLE_WOOL", "BLUE_WOOL", "BROWN_WOOL", "GREEN_WOOL", "RED_WOOL", "BLACK_WOOL", "GOLD_BLOCK", "IRON_BLOCK", "BRICKS", "TNT", "BOOKSHELF", "MOSSY_COBBLESTONE", "OBSIDIAN", "DIAMOND_ORE", "DIAMOND_BLOCK", "CRAFTING_TABLE", "REDSTONE_ORE", "ICE", "SNOW_BLOCK", "CLAY", "JUKEBOX", "PUMPKIN", "NETHERRACK", "SOUL_SAND", "GLOWSTONE", "JACK_O_LANTERN", "WHITE_STAINED_GLASS", "ORANGE_STAINED_GLASS", "MAGENTA_STAINED_GLASS", "LIGHT_BLUE_STAINED_GLASS", "YELLOW_STAINED_GLASS", "LIME_STAINED_GLASS", "PINK_STAINED_GLASS", "GRAY_STAINED_GLASS", "LIGHT_GRAY_STAINED_GLASS", "CYAN_STAINED_GLASS", "PURPLE_STAINED_GLASS", "BLUE_STAINED_GLASS", "BROWN_STAINED_GLASS", "GREEN_STAINED_GLASS", "RED_STAINED_GLASS", "BLACK_STAINED_GLASS", "STONE_BRICKS", "BROWN_MUSHROOM_BLOCK", "RED_MUSHROOM_BLOCK", "MELON", "MUSHROOM_STEM", "MYCELIUM", "NETHER_BRICKS", "END_STONE", "REDSTONE_LAMP", "EMERALD_ORE", "EMERALD_BLOCK", "QUARTZ_BLOCK", "WHITE_TERRACOTTA", "ORANGE_TERRACOTTA", "MAGENTA_TERRACOTTA", "LIGHT_BLUE_TERRACOTTA", "YELLOW_TERRACOTTA", "LIME_TERRACOTTA", "PINK_TERRACOTTA", "GRAY_TERRACOTTA", "LIGHT_GRAY_TERRACOTTA", "CYAN_TERRACOTTA", "PURPLE_TERRACOTTA", "BLUE_TERRACOTTA", "BROWN_TERRACOTTA", "GREEN_TERRACOTTA", "RED_TERRACOTTA", "BLACK_TERRACOTTA", "SLIME_BLOCK", "BARRIER", "PRISMARINE", "HAY_BLOCK", "WHITE_CARPET", "ORANGE_CARPET", "MAGENTA_CARPET", "LIGHT_BLUE_CARPET", "YELLOW_CARPET", "LIME_CARPET", "PINK_CARPET", "GRAY_CARPET", "LIGHT_GRAY_CARPET", "CYAN_CARPET", "PURPLE_CARPET", "BLUE_CARPET", "BROWN_CARPET", "GREEN_CARPET", "RED_CARPET", "BLACK_CARPET", "TERRACOTTA", "COAL_BLOCK", "PACKED_ICE", "BLUE_ICE", "RED_SANDSTONE", "PURPUR_BLOCK", "PURPUR_PILLAR", "END_STONE_BRICKS", "WHITE_GLAZED_TERRACOTTA", "ORANGE_GLAZED_TERRACOTTA", "MAGENTA_GLAZED_TERRACOTTA", "LIGHT_BLUE_GLAZED_TERRACOTTA", "YELLOW_GLAZED_TERRACOTTA", "LIME_GLAZED_TERRACOTTA", "PINK_GLAZED_TERRACOTTA", "GRAY_GLAZED_TERRACOTTA", "LIGHT_GRAY_GLAZED_TERRACOTTA", "CYAN_GLAZED_TERRACOTTA", "PURPLE_GLAZED_TERRACOTTA", "BLUE_GLAZED_TERRACOTTA", "BROWN_GLAZED_TERRACOTTA", "GREEN_GLAZED_TERRACOTTA", "RED_GLAZED_TERRACOTTA", "BLACK_GLAZED_TERRACOTTA", "WHITE_CONCRETE", "ORANGE_CONCRETE", "MAGENTA_CONCRETE", "LIGHT_BLUE_CONCRETE", "YELLOW_CONCRETE", "LIME_CONCRETE", "PINK_CONCRETE", "GRAY_CONCRETE", "LIGHT_GRAY_CONCRETE", "CYAN_CONCRETE", "PURPLE_CONCRETE", "BLUE_CONCRETE", "BROWN_CONCRETE", "GREEN_CONCRETE", "RED_CONCRETE", "BLACK_CONCRETE");
            blocks_config.set("chameleon_blocks", CHAM_BLOCKS);
            blocks_config.set("version", 7);
            i++;
        }
        if (!blocks_config.contains("version") || blocks_config.getInt("version") < 8) {
            List<String> UNDER_BLOCKS = Arrays.asList("ACACIA_DOOR", "ACACIA_FENCE_GATE", "ACACIA_FENCE", "ACACIA_LEAVES", "ACACIA_PRESSURE_PLATE", "ACACIA_SAPLING", "ACACIA_SIGN", "ACACIA_SLAB", "ACACIA_STAIRS", "ACACIA_TRAPDOOR", "ACACIA_WALL_SIGN", "AIR", "ALLIUM", "AZURE_BLUET", "BEETROOTS", "BIRCH_DOOR", "BIRCH_FENCE_GATE", "BIRCH_FENCE", "BIRCH_LEAVES", "BIRCH_PRESSURE_PLATE", "BIRCH_SAPLING", "BIRCH_SIGN", "BIRCH_SLAB", "BIRCH_STAIRS", "BIRCH_TRAPDOOR", "BIRCH_WALL_SIGN", "BLACK_BANNER", "BLACK_BED", "BLACK_CARPET", "BLACK_SHULKER_BOX", "BLACK_WALL_BANNER", "BLUE_BANNER", "BLUE_BED", "BLUE_CARPET", "BLUE_ORCHID", "BLUE_SHULKER_BOX", "BLUE_WALL_BANNER", "BREWING_STAND", "BRICK_STAIRS", "BROWN_BANNER", "BROWN_BED", "BROWN_CARPET", "BROWN_MUSHROOM", "BROWN_SHULKER_BOX", "BROWN_WALL_BANNER", "CACTUS", "CAKE", "CAULDRON", "CAVE_AIR", "CHEST", "CHORUS_FLOWER", "CHORUS_PLANT", "COBBLESTONE_STAIRS", "COBWEB", "CYAN_BANNER", "CYAN_BED", "CYAN_CARPET", "CYAN_SHULKER_BOX", "CYAN_WALL_BANNER", "DARK_OAK_DOOR", "DARK_OAK_FENCE_GATE", "DARK_OAK_FENCE", "DARK_OAK_LEAVES", "DARK_OAK_PRESSURE_PLATE", "DARK_OAK_SAPLING", "DARK_OAK_SIGN", "DARK_OAK_SLAB", "DARK_OAK_STAIRS", "DARK_OAK_TRAPDOOR", "DARK_OAK_WALL_SIGN", "DAYLIGHT_DETECTOR", "DEAD_BUSH", "DETECTOR_RAIL", "DIRT_PATH", "DRAGON_EGG", "ENCHANTING_TABLE", "END_PORTAL_FRAME", "END_PORTAL", "END_ROD", "ENDER_CHEST", "FARMLAND", "FERN", "FIRE", "FROSTED_ICE", "FURNACE", "GLASS_PANE", "GLASS", "GLOWSTONE", "SHORT_GRASS", "GRAY_BANNER", "GRAY_BED", "GRAY_CARPET", "GRAY_SHULKER_BOX", "GRAY_WALL_BANNER", "GREEN_BANNER", "GREEN_BED", "GREEN_CARPET", "GREEN_SHULKER_BOX", "GREEN_WALL_BANNER", "HEAVY_WEIGHTED_PRESSURE_PLATE", "ICE", "IRON_BARS", "IRON_DOOR", "IRON_TRAPDOOR", "JUNGLE_DOOR", "JUNGLE_FENCE_GATE", "JUNGLE_FENCE", "JUNGLE_LEAVES", "JUNGLE_PRESSURE_PLATE", "JUNGLE_SAPLING", "JUNGLE_SIGN", "JUNGLE_SLAB", "JUNGLE_STAIRS", "JUNGLE_TRAPDOOR", "JUNGLE_WALL_SIGN", "LADDER", "LARGE_FERN", "LAVA_CAULDRON", "LAVA", "LEVER", "LIGHT_BLUE_BANNER", "LIGHT_BLUE_BED", "LIGHT_BLUE_CARPET", "LIGHT_BLUE_SHULKER_BOX", "LIGHT_BLUE_WALL_BANNER", "LIGHT_GRAY_BANNER", "LIGHT_GRAY_BED", "LIGHT_GRAY_CARPET", "LIGHT_GRAY_SHULKER_BOX", "LIGHT_GRAY_WALL_BANNER", "LIGHT_WEIGHTED_PRESSURE_PLATE", "LILAC", "LILY_PAD", "LIME_BANNER", "LIME_BED", "LIME_CARPET", "LIME_SHULKER_BOX", "LIME_WALL_BANNER", "MAGENTA_BANNER", "MAGENTA_BED", "MAGENTA_CARPET", "MAGENTA_SHULKER_BOX", "MAGENTA_WALL_BANNER", "MELON_STEM", "NETHER_BRICK_FENCE", "NETHER_BRICK_STAIRS", "NETHER_WART", "OAK_DOOR", "OAK_FENCE_GATE", "OAK_FENCE", "OAK_LEAVES", "OAK_PRESSURE_PLATE", "OAK_SAPLING", "OAK_SIGN", "OAK_SLAB", "OAK_STAIRS", "OAK_TRAPDOOR", "OAK_WALL_SIGN", "ORANGE_BANNER", "ORANGE_BED", "ORANGE_CARPET", "ORANGE_SHULKER_BOX", "ORANGE_TULIP", "ORANGE_WALL_BANNER", "OXEYE_DAISY", "PEONY", "PINK_BANNER", "PINK_BED", "PINK_CARPET", "PINK_SHULKER_BOX", "PINK_TULIP", "PINK_WALL_BANNER", "PISTON_HEAD", "PISTON", "POPPY", "POWDER_SNOW_CAULDRON", "POWDER_SNOW", "POWERED_RAIL", "PUMPKIN_STEM", "PURPLE_BANNER", "PURPLE_BED", "PURPLE_CARPET", "PURPLE_SHULKER_BOX", "PURPLE_WALL_BANNER", "PURPUR_SLAB", "PURPUR_STAIRS", "RAIL", "RED_BANNER", "RED_BED", "RED_CARPET", "RED_MUSHROOM", "RED_SANDSTONE_STAIRS", "RED_SHULKER_BOX", "RED_TULIP", "RED_WALL_BANNER", "REDSTONE_TORCH", "REDSTONE_WIRE", "REPEATER", "ROSE_BUSH", "SANDSTONE_STAIRS", "SNOW", "SPRUCE_DOOR", "SPRUCE_FENCE_GATE", "SPRUCE_FENCE", "SPRUCE_LEAVES", "SPRUCE_PRESSURE_PLATE", "SPRUCE_SAPLING", "SPRUCE_SIGN", "SPRUCE_SLAB", "SPRUCE_STAIRS", "SPRUCE_TRAPDOOR", "SPRUCE_WALL_SIGN", "STICKY_PISTON", "STONE_BRICK_STAIRS", "STONE_BUTTON", "STONE_PRESSURE_PLATE", "STRUCTURE_VOID", "SUGAR_CANE", "SUNFLOWER", "TALL_GRASS", "TNT", "TORCH", "TRIPWIRE_HOOK", "TRIPWIRE", "VINE", "VOID_AIR", "WATER_CAULDRON", "WATER", "WHITE_BANNER", "WHITE_BED", "WHITE_CARPET", "WHITE_SHULKER_BOX", "WHITE_TULIP", "WHITE_WALL_BANNER", "YELLOW_BANNER", "YELLOW_BED", "YELLOW_CARPET", "YELLOW_SHULKER_BOX", "YELLOW_WALL_BANNER");
            blocks_config.set("under_door_blocks", UNDER_BLOCKS);
            blocks_config.set("version", 8);
            i++;
        }
        List<String> tbs = blocks_config.getStringList("tardis_blocks");
        if (!tbs.contains("SMOOTH_QUARTZ")) {
            tbs.add("SMOOTH_QUARTZ");
            blocks_config.set("tardis_blocks", tbs);
            i++;
        }
        if (!tbs.contains("BASALT")) {
            List<String> blocks = new ArrayList<>();
            blocks.add("BASALT");
            blocks.add("BLACKSTONE");
            blocks.add("CHISELED_NETHER_BRICKS");
            blocks.add("CHISELED_POLISHED_BLACKSTONE");
            blocks.add("CRACKED_NETHER_BRICKS");
            blocks.add("CRACKED_POLISHED_BLACKSTONE_BRICKS");
            blocks.add("CRIMSON_HYPHAE");
            blocks.add("CRIMSON_NYLIUM");
            blocks.add("CRIMSON_PLANKS");
            blocks.add("CRIMSON_STEM");
            blocks.add("POLISHED_BASALT");
            blocks.add("POLISHED_BLACKSTONE");
            blocks.add("POLISHED_BLACKSTONE_BRICKS");
            blocks.add("QUARTZ_BRICKS");
            blocks.add("SOUL_SOIL");
            blocks.add("STRIPPED_CRIMSON_HYPHAE");
            blocks.add("STRIPPED_CRIMSON_STEM");
            blocks.add("STRIPPED_WARPED_HYPHAE");
            blocks.add("STRIPPED_WARPED_STEM");
            blocks.add("WARPED_HYPHAE");
            blocks.add("WARPED_NYLIUM");
            blocks.add("WARPED_PLANKS");
            blocks.add("WARPED_STEM");
            blocks.add("WARPED_WART_BLOCK");
            // tardis blocks
            tbs.addAll(blocks);
            tbs.sort(Comparator.naturalOrder());
            blocks_config.set("tardis_blocks", tbs);
            // chameleon blocks
            List<String> chameleon = blocks_config.getStringList("chameleon_blocks");
            chameleon.addAll(blocks);
            chameleon.sort(Comparator.naturalOrder());
            blocks_config.set("chameleon_blocks", chameleon);
            // lamp blocks
            List<String> lamps = blocks_config.getStringList("lamp_blocks");
            lamps.add("SHROOMLIGHT");
            blocks_config.set("lamp_blocks", lamps);
            // under door blocks
            List<String> under = blocks_config.getStringList("under_door_blocks");
            under.add("ACACIA_BUTTON");
            under.add("ANDESITE_WALL");
            under.add("BIRCH_BUTTON");
            under.add("BLACKSTONE_SLAB");
            under.add("BLACKSTONE_STAIRS");
            under.add("BLACKSTONE_WALL");
            under.add("BRICK_WALL");
            under.add("CAMPFIRE");
            under.add("CHAIN");
            under.add("CRIMSON_BUTTON");
            under.add("CRIMSON_DOOR");
            under.add("CRIMSON_FENCE");
            under.add("CRIMSON_FENCE_GATE");
            under.add("CRIMSON_FUNGUS");
            under.add("CRIMSON_PRESSURE_PLATE");
            under.add("CRIMSON_ROOTS");
            under.add("CRIMSON_SIGN");
            under.add("CRIMSON_SLAB");
            under.add("CRIMSON_STAIRS");
            under.add("CRIMSON_TRAPDOOR");
            under.add("DARK_OAK_BUTTON");
            under.add("DIORITE_WALL");
            under.add("END_STONE_BRICK_WALL");
            under.add("GRANITE_WALL");
            under.add("JUNGLE_BUTTON");
            under.add("LANTERN");
            under.add("NETHER_BRICK_WALL");
            under.add("NETHER_SPROUTS");
            under.add("OAK_BUTTON");
            under.add("POLISHED_BLACKSTONE_BRICK_SLAB");
            under.add("POLISHED_BLACKSTONE_BRICK_STAIRS");
            under.add("POLISHED_BLACKSTONE_BRICK_WALL");
            under.add("POLISHED_BLACKSTONE_BUTTON");
            under.add("POLISHED_BLACKSTONE_PRESSURE_PLATE");
            under.add("POLISHED_BLACKSTONE_SLAB");
            under.add("POLISHED_BLACKSTONE_STAIRS");
            under.add("POLISHED_BLACKSTONE_WALL");
            under.add("PRISMARINE_WALL");
            under.add("RED_NETHER_BRICK_WALL");
            under.add("RED_SANDSTONE_WALL");
            under.add("SANDSTONE_WALL");
            under.add("SOUL_CAMPFIRE");
            under.add("SOUL_LANTERN");
            under.add("SOUL_TORCH");
            under.add("SPRUCE_BUTTON");
            under.add("TWISTING_VINES");
            under.add("SOUL_WALL_TORCH");
            under.add("WALL_TORCH");
            under.add("WARPED_BUTTON");
            under.add("WARPED_DOOR");
            under.add("WARPED_FENCE");
            under.add("WARPED_FENCE_GATE");
            under.add("WARPED_FUNGUS");
            under.add("WARPED_PRESSURE_PLATE");
            under.add("WARPED_ROOTS");
            under.add("WARPED_SIGN");
            under.add("WARPED_SLAB");
            under.add("WARPED_STAIRS");
            under.add("WARPED_TRAPDOOR");
            under.add("WARPED_WALL_SIGN");
            under.add("WEEPING_VINES");
            under.sort(Comparator.naturalOrder());
            blocks_config.set("under_door_blocks", under);
            blocks_config.set("version", 9);
            i += 4;
        }
        if (!tbs.contains("AMETHYST_BLOCK")) {
            List<String> blocks = new ArrayList<>();
            blocks.add("AMETHYST_BLOCK");
            blocks.add("CALCITE");
            blocks.add("CHISELED_DEEPSLATE");
            blocks.add("COBBLED_DEEPSLATE");
            blocks.add("COPPER_BLOCK");
            blocks.add("CRACKED_DEEPSLATE_BRICKS");
            blocks.add("CRACKED_DEEPSLATE_TILES");
            blocks.add("CUT_COPPER");
            blocks.add("DEEPSLATE");
            blocks.add("DEEPSLATE_BRICKS");
            blocks.add("DEEPSLATE_TILES");
            blocks.add("DRIPSTONE_BLOCK");
            blocks.add("EXPOSED_COPPER");
            blocks.add("EXPOSED_CUT_COPPER");
            blocks.add("MOSS_BLOCK");
            blocks.add("OXIDIZED_COPPER");
            blocks.add("OXIDIZED_CUT_COPPER");
            blocks.add("POLISHED_DEEPSLATE");
            blocks.add("ROOTED_DIRT");
            blocks.add("SMOOTH_BASALT");
            blocks.add("TUFF");
            blocks.add("WAXED_COPPER_BLOCK");
            blocks.add("WAXED_CUT_COPPER");
            blocks.add("WAXED_EXPOSED_COPPER");
            blocks.add("WAXED_EXPOSED_CUT_COPPER");
            blocks.add("WAXED_OXIDIZED_COPPER");
            blocks.add("WAXED_OXIDIZED_CUT_COPPER");
            blocks.add("WAXED_WEATHERED_COPPER");
            blocks.add("WAXED_WEATHERED_CUT_COPPER");
            blocks.add("WEATHERED_COPPER");
            blocks.add("WEATHERED_CUT_COPPER");
            // tardis blocks
            tbs.addAll(blocks);
            tbs.sort(Comparator.naturalOrder());
            blocks_config.set("tardis_blocks", tbs);
            // chameleon blocks
            List<String> chameleon = blocks_config.getStringList("chameleon_blocks");
            chameleon.addAll(blocks);
            chameleon.sort(Comparator.naturalOrder());
            blocks_config.set("chameleon_blocks", chameleon);
            // lamp blocks
            List<String> lamps = blocks_config.getStringList("lamp_blocks");
            lamps.add("LANTERN");
            lamps.add("LIGHT");
            lamps.add("SOUL_LANTERN");
            blocks_config.set("lamp_blocks", lamps);
            // under door blocks
            List<String> under = blocks_config.getStringList("under_door_blocks");
            under.add("AMETHYST_CLUSTER");
            under.add("AZALEA");
            under.add("AZALEA_LEAVES");
            under.add("BIG_DRIPLEAF");
            under.add("BLACK_CANDLE");
            under.add("BLUE_CANDLE");
            under.add("BROWN_CANDLE");
            under.add("CANDLE");
            under.add("CAVE_VINES");
            under.add("COBBLED_DEEPSLATE_SLAB");
            under.add("COBBLED_DEEPSLATE_STAIRS");
            under.add("COBBLED_DEEPSLATE_WALL");
            under.add("CUT_COPPER_SLAB");
            under.add("CUT_COPPER_STAIRS");
            under.add("CYAN_CANDLE");
            under.add("DEEPSLATE_BRICK_SLAB");
            under.add("DEEPSLATE_BRICK_STAIRS");
            under.add("DEEPSLATE_BRICK_WALL");
            under.add("DEEPSLATE_TILE_SLAB");
            under.add("DEEPSLATE_TILE_STAIRS");
            under.add("DEEPSLATE_TILE_WALL");
            under.add("DIRT_PATH");
            under.add("EXPOSED_CUT_COPPER_SLAB");
            under.add("EXPOSED_CUT_COPPER_STAIRS");
            under.add("FLOWERING_AZALEA");
            under.add("FLOWERING_AZALEA_LEAVES");
            under.add("GLOW_ITEM_FRAME");
            under.add("GLOW_LICHEN");
            under.add("GRAY_CANDLE");
            under.add("GREEN_CANDLE");
            under.add("HANGING_ROOTS");
            under.add("LIGHT_BLUE_CANDLE");
            under.add("LIGHT_GRAY_CANDLE");
            under.add("LIGHTNING_ROD");
            under.add("LIME_CANDLE");
            under.add("MAGENTA_CANDLE");
            under.add("MOSS_CARPET");
            under.add("ORANGE_CANDLE");
            under.add("OXIDIZED_CUT_COPPER_SLAB");
            under.add("OXIDIZED_CUT_COPPER_STAIRS");
            under.add("PINK_CANDLE");
            under.add("POINTED_DRIPSTONE");
            under.add("POLISHED_DEEPSLATE_SLAB");
            under.add("POLISHED_DEEPSLATE_STAIRS");
            under.add("POLISHED_DEEPSLATE_WALL");
            under.add("PURPLE_CANDLE");
            under.add("RED_CANDLE");
            under.add("SMALL_DRIPLEAF");
            under.add("SPORE_BLOSSOM");
            under.add("TINTED_GLASS");
            under.add("WAXED_CUT_COPPER_SLAB");
            under.add("WAXED_CUT_COPPER_STAIRS");
            under.add("WAXED_EXPOSED_CUT_COPPER_SLAB");
            under.add("WAXED_EXPOSED_CUT_COPPER_STAIRS");
            under.add("WAXED_OXIDIZED_CUT_COPPER_SLAB");
            under.add("WAXED_OXIDIZED_CUT_COPPER_STAIRS");
            under.add("WAXED_WEATHERED_CUT_COPPER_SLAB");
            under.add("WAXED_WEATHERED_CUT_COPPER_STAIRS");
            under.add("WEATHERED_CUT_COPPER_SLAB");
            under.add("WEATHERED_CUT_COPPER_STAIRS");
            under.add("WHITE_CANDLE");
            under.add("YELLOW_CANDLE");
            // replaced with DIRT_PATH
            under.remove("GRASS_PATH");
            under.sort(Comparator.naturalOrder());
            blocks_config.set("under_door_blocks", under);
            blocks_config.set("version", 10);
            i += 3;
        }
        if (tbs.contains("REINFORCED_DEEPSLAT")) {
            tbs.remove("REINFORCED_DEEPSLAT");
            blocks_config.set("tardis_blocks", tbs);
        }
        if (!tbs.contains("MANGROVE_LOG")) {
            List<String> blocks = new ArrayList<>();
            blocks.add("MANGROVE_LOG");
            blocks.add("MANGROVE_PLANKS");
            blocks.add("MANGROVE_ROOTS");
            blocks.add("MANGROVE_WOOD");
            blocks.add("MUD");
            blocks.add("MUDDY_MANGROVE_ROOTS");
            blocks.add("MUD_BRICKS");
            blocks.add("PACKED_MUD");
            blocks.add("SCULK");
            blocks.add("STRIPPED_MANGROVE_LOG");
            blocks.add("STRIPPED_MANGROVE_WOOD");
            // tardis blocks
            tbs.addAll(blocks);
            tbs.sort(Comparator.naturalOrder());
            blocks_config.set("tardis_blocks", tbs);
            // chameleon blocks
            List<String> chameleon = blocks_config.getStringList("chameleon_blocks");
            chameleon.addAll(blocks);
            chameleon.sort(Comparator.naturalOrder());
            blocks_config.set("chameleon_blocks", chameleon);
            // lamp blocks
            List<String> lamps = blocks_config.getStringList("lamp_blocks");
            lamps.add("OCHRE_FROGLIGHT");
            lamps.add("PEARLESCENT_FROGLIGHT");
            lamps.add("VERDANT_FROGLIGHT");
            blocks_config.set("lamp_blocks", lamps);
            // under door blocks
            List<String> under = blocks_config.getStringList("under_door_blocks");
            under.add("MANGROVE_BUTTON");
            under.add("MANGROVE_DOOR");
            under.add("MANGROVE_FENCE");
            under.add("MANGROVE_FENCE_GATE");
            under.add("MANGROVE_LEAVES");
            under.add("MANGROVE_PRESSURE_PLATE");
            under.add("MANGROVE_SIGN");
            under.add("MANGROVE_SLAB");
            under.add("MANGROVE_STAIRS");
            under.add("MANGROVE_TRAPDOOR");
            under.add("MANGROVE_WALL_SIGN");
            under.add("MANGROVE_PROPAGULE");
            under.add("MANGROVE_ROOTS");
            under.add("MUD_BRICK_SLAB");
            under.add("MUD_BRICK_STAIRS");
            under.add("MUD_BRICK_WALL");
            under.add("SCULK_SENSOR");
            under.add("SCULK_SHRIEKER");
            under.add("SCULK_VEIN");
            blocks_config.set("under_door_blocks", under);
            blocks_config.set("version", 11);
            i += 4;
        }
        List<String> underDoorBlocks = blocks_config.getStringList("under_door_blocks");
        if (underDoorBlocks.contains("GRASS")) {
            underDoorBlocks.remove("GRASS");
            underDoorBlocks.add("SHORT_GRASS");
            blocks_config.set("under_door_blocks", underDoorBlocks);
            i++;
        }
        if (!underDoorBlocks.contains("WATER_CAULDRON")) {
            underDoorBlocks.add("WATER_CAULDRON");
            underDoorBlocks.add("LAVA_CAULDRON");
            underDoorBlocks.add("POWDER_SNOW_CAULDRON");
            underDoorBlocks.add("POWDER_SNOW");
            underDoorBlocks.add("BIG_DRIPLEAF");
            underDoorBlocks.add("BIG_DRIPLEAF_STEM");
            underDoorBlocks.add("SMALL_DRIPLEAF");
            blocks_config.set("under_door_blocks", underDoorBlocks);
            blocks_config.set("version", 11);
            i++;
        }
        // fix SOUL_WALL_TORCH in under door blocks
        if (underDoorBlocks.contains("WALL_SOUL_TORCH")) {
            underDoorBlocks.remove("WALL_SOUL_TORCH");
            underDoorBlocks.add("SOUL_WALL_TORCH");
            blocks_config.set("under_door_blocks", underDoorBlocks);
            i++;
        }
        List<String> dripLeafBlocks = blocks_config.getStringList("tardis_blocks");
        if (dripLeafBlocks.contains("BIG_DRIPLEAF")) {
            dripLeafBlocks.remove("BIG_DRIPLEAF");
            dripLeafBlocks.remove("BIG_DRIPLEAF_STEM");
            dripLeafBlocks.remove("SMALL_DRIPLEAF");
            blocks_config.set("tardis_blocks", dripLeafBlocks);
            i++;
        }
        if (!tbs.contains("BAMBOO_BLOCK")) {
            List<String> blocks = new ArrayList<>();
            blocks.add("BAMBOO_BLOCK");
            blocks.add("BAMBOO_MOSAIC");
            blocks.add("BAMBOO_PLANKS");
            blocks.add("CHISELED_BOOKSHELF");
            blocks.add("STRIPPED_BAMBOO_BLOCK");
            // tardis blocks
            tbs.addAll(blocks);
            tbs.sort(Comparator.naturalOrder());
            blocks_config.set("tardis_blocks", tbs);
            // chameleon blocks
            List<String> chameleon = blocks_config.getStringList("chameleon_blocks");
            chameleon.addAll(blocks);
            chameleon.sort(Comparator.naturalOrder());
            blocks_config.set("chameleon_blocks", chameleon);
            // under door blocks
            List<String> under = blocks_config.getStringList("under_door_blocks");
            under.add("ACACIA_HANGING_SIGN");
            under.add("BAMBOO_BUTTON");
            under.add("BAMBOO_DOOR");
            under.add("BAMBOO_FENCE");
            under.add("BAMBOO_FENCE_GATE");
            under.add("BAMBOO_HANGING_SIGN");
            under.add("BAMBOO_MOSAIC_SLAB");
            under.add("BAMBOO_MOSAIC_STAIRS");
            under.add("BAMBOO_PRESSURE_PLATE");
            under.add("BAMBOO_SIGN");
            under.add("BAMBOO_SLAB");
            under.add("BAMBOO_STAIRS");
            under.add("BAMBOO_TRAPDOOR");
            under.add("BAMBOO_WALL_SIGN");
            under.add("BIRCH_HANGING_SIGN");
            under.add("CRIMSON_HANGING_SIGN");
            under.add("DARK_OAK_HANGING_SIGN");
            under.add("JUNGLE_HANGING_SIGN");
            under.add("MANGROVE_HANGING_SIGN");
            under.add("OAK_HANGING_SIGN");
            under.add("PIGLIN_HEAD");
            under.add("SPRUCE_HANGING_SIGN");
            under.add("WARPED_HANGING_SIGN");
            blocks_config.set("under_door_blocks", under);
            blocks_config.set("version", 12);
            i += 3;
        }
        if (!tbs.contains("CHERRY_LOG")) {
            List<String> blocks = new ArrayList<>();
            blocks.add("CHERRY_LOG");
            blocks.add("CHERRY_PLANKS");
            blocks.add("STRIPPED_CHERRY_LOG");
            // tardis blocks
            tbs.addAll(blocks);
            tbs.sort(Comparator.naturalOrder());
            blocks_config.set("tardis_blocks", tbs);
            // chameleon blocks
            List<String> chameleon = blocks_config.getStringList("chameleon_blocks");
            blocks.add("CHERRY_LEAVES");
            chameleon.addAll(blocks);
            chameleon.sort(Comparator.naturalOrder());
            blocks_config.set("chameleon_blocks", chameleon);
            // under door blocks
            List<String> under = blocks_config.getStringList("under_door_blocks");
            under.add("ACACIA_WALL_HANGING_SIGN");
            under.add("BAMBOO_WALL_HANGING_SIGN");
            under.add("BIRCH_WALL_HANGING_SIGN");
            under.add("CHERRY_BUTTON");
            under.add("CHERRY_DOOR");
            under.add("CHERRY_FENCE");
            under.add("CHERRY_FENCE_GATE");
            under.add("CHERRY_HANGING_SIGN");
            under.add("CHERRY_LEAVES");
            under.add("CHERRY_PRESSURE_PLATE");
            under.add("CHERRY_SAPLING");
            under.add("CHERRY_SIGN");
            under.add("CHERRY_SLAB");
            under.add("CHERRY_STAIRS");
            under.add("CHERRY_TRAPDOOR");
            under.add("CHERRY_WALL_HANGING_SIGN");
            under.add("CHERRY_WALL_SIGN");
            under.add("CRIMSON_WALL_HANGING_SIGN");
            under.add("DARK_OAK_WALL_HANGING_SIGN");
            under.add("JUNGLE_WALL_HANGING_SIGN");
            under.add("MANGROVE_WALL_HANGING_SIGN");
            under.add("OAK_WALL_HANGING_SIGN");
            under.add("SPRUCE_WALL_HANGING_SIGN");
            under.add("TORCHFLOWER");
            under.add("TORCHFLOWER_CROP");
            under.add("WARPED_WALL_HANGING_SIGN");
            under.sort(Comparator.naturalOrder());
            blocks_config.set("under_door_blocks", under);
            blocks_config.set("version", 13);
            i += 3;
        }
        try {
            blocks_config.save(new File(plugin.getDataFolder(), "blocks.yml"));
            if (i > 0) {
                plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added " + i + " new items to blocks.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save blocks.yml, " + io.getMessage());
        }
    }
}
