/*
 * Copyright (C) 2023 eccentric_nz
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;

/**
 * @author eccentric_nz
 */
public class TARDISCondensablesUpdater {

    private final TARDIS plugin;

    public TARDISCondensablesUpdater(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void checkCondensables() {
        if (!plugin.getConfig().getBoolean("preferences.use_default_condensables")) {
            HashMap<String, Object> list = new HashMap<>();
            // 1.13
            list.put("ACACIA_WOOD", 2);
            list.put("BIRCH_STAIRS", 11);
            list.put("BIRCH_WOOD", 2);
            list.put("CHEST_MINECART", 30);
            list.put("DARK_OAK_WOOD", 2);
            list.put("END_STONE_BRICKS", 21);
            list.put("GLISTERING_MELON_SLICE", 22);
            list.put("GUNPOWDER", 20);
            list.put("JUNGLE_STAIRS", 11);
            list.put("JUNGLE_WOOD", 2);
            list.put("OAK_WOOD", 2);
            list.put("POPPED_CHORUS_FRUIT", 8);
            list.put("POPPY", 2);
            list.put("SEAGRASS", 2);
            list.put("SPRUCE_STAIRS", 11);
            list.put("STRIPPED_ACACIA_LOG", 2);
            list.put("STRIPPED_ACACIA_WOOD", 2);
            list.put("STRIPPED_BIRCH_LOG", 2);
            list.put("STRIPPED_BIRCH_WOOD", 2);
            list.put("STRIPPED_DARK_OAK_LOG", 2);
            list.put("STRIPPED_DARK_OAK_WOOD", 2);
            list.put("STRIPPED_JUNGLE_LOG", 2);
            list.put("STRIPPED_JUNGLE_WOOD", 2);
            list.put("STRIPPED_OAK_LOG", 2);
            list.put("STRIPPED_OAK_WOOD", 2);
            list.put("STRIPPED_SPRUCE_LOG", 2);
            list.put("STRIPPED_SPRUCE_WOOD", 2);
            list.put("TROPICAL_FISH", 5);
            // 1.14
            list.put("ACACIA_SIGN", 2);
            list.put("ANDESITE_SLAB", 2);
            list.put("ANDESITE_STAIRS", 2);
            list.put("ANDESITE_WALL", 2);
            list.put("BAMBOO", 2);
            list.put("BARREL", 16);
            list.put("BELL", 100);
            list.put("BIRCH_SIGN", 2);
            list.put("BLACK_DYE", 10);
            list.put("BLAST_FURNACE", 110);
            list.put("BLUE_DYE", 10);
            list.put("BRICK_WALL", 2);
            list.put("CAMPFIRE", 24);
            list.put("CARTOGRAPHY_TABLE", 28);
            list.put("COMPOSTER", 10);
            list.put("CORNFLOWER", 2);
            list.put("CROSSBOW", 60);
            list.put("CUT_RED_SANDSTONE_SLAB", 3);
            list.put("CUT_SANDSTONE_SLAB", 3);
            list.put("DARK_OAK_SIGN", 2);
            list.put("DIORITE_SLAB", 2);
            list.put("DIORITE_STAIRS", 2);
            list.put("DIORITE_WALL", 2);
            list.put("END_STONE_BRICK_SLAB", 21);
            list.put("END_STONE_BRICK_WALL", 21);
            list.put("FLETCHING_TABLE", 12);
            list.put("GRANITE_SLAB", 2);
            list.put("GRANITE_STAIRS", 2);
            list.put("GRANITE_WALL", 2);
            list.put("GRINDSTONE", 30);
            list.put("HONEYCOMB_BLOCK", 80);
            list.put("JUNGLE_SIGN", 2);
            list.put("LANTERN", 28);
            list.put("LEATHER_HORSE_ARMOR", 100);
            list.put("LECTERN", 148);
            list.put("LILY_OF_THE_VALLEY", 2);
            list.put("LOOM", 14);
            list.put("MOSSY_COBBLESTONE_SLAB", 2);
            list.put("MOSSY_COBBLESTONE_STAIRS", 2);
            list.put("MOSSY_STONE_BRICK_SLAB", 2);
            list.put("MOSSY_STONE_BRICK_STAIRS", 2);
            list.put("MOSSY_STONE_BRICK_WALL", 2);
            list.put("MUSIC_DISC_CAT", 100);
            list.put("NETHER_BRICK_WALL", 2);
            list.put("OAK_SIGN", 2);
            list.put("POLISHED_ANDESITE_SLAB", 1);
            list.put("POLISHED_ANDESITE_STAIRS", 1);
            list.put("POLISHED_DIORITE_SLAB", 1);
            list.put("POLISHED_DIORITE_STAIRS", 1);
            list.put("POLISHED_GRANITE_SLAB", 1);
            list.put("POLISHED_GRANITE_STAIRS", 1);
            list.put("PRISMARINE_STAIRS", 2);
            list.put("PRISMARINE_WALL", 2);
            list.put("RED_NETHER_BRICK_SLAB", 12);
            list.put("RED_NETHER_BRICK_STAIRS", 12);
            list.put("RED_NETHER_BRICK_WALL", 12);
            list.put("RED_SANDSTONE_WALL", 2);
            list.put("SANDSTONE_WALL", 2);
            list.put("SCAFFOLDING", 18);
            list.put("SMITHING_TABLE", 52);
            list.put("SMOKER", 18);
            list.put("SMOOTH_QUARTZ_SLAB", 60);
            list.put("SMOOTH_QUARTZ_STAIRS", 60);
            list.put("SMOOTH_RED_SANDSTONE_SLAB", 4);
            list.put("SMOOTH_RED_SANDSTONE_STAIRS", 4);
            list.put("SMOOTH_SANDSTONE_SLAB", 4);
            list.put("SMOOTH_SANDSTONE_STAIRS", 4);
            list.put("SMOOTH_STONE_SLAB", 4);
            list.put("SPRUCE_SIGN", 2);
            list.put("STONE_BRICK_WALL", 2);
            list.put("STONE_STAIRS", 23);
            list.put("STONECUTTER", 112);
            list.put("SUSPICIOUS_STEW", 8);
            list.put("SWEET_BERRIES", 10);
            list.put("WHITE_DYE", 10);
            list.put("WITHER_ROSE", 2);
            // 1.16
            list.put("ANCIENT_DEBRIS", 100);
            list.put("BASALT", 1);
            list.put("BLACKSTONE", 1);
            list.put("BLACKSTONE_SLAB", 1);
            list.put("BLACKSTONE_STAIRS", 1);
            list.put("BLACKSTONE_WALL", 1);
            list.put("CHAIN", 1);
            list.put("CHISELED_NETHER_BRICKS", 1);
            list.put("CHISELED_POLISHED_BLACKSTONE", 1);
            list.put("CRACKED_NETHER_BRICKS", 1);
            list.put("CRACKED_POLISHED_BLACKSTONE_BRICKS", 2);
            list.put("CRIMSON_BUTTON", 1);
            list.put("CRIMSON_DOOR", 3);
            list.put("CRIMSON_FENCE", 1);
            list.put("CRIMSON_FENCE_GATE", 5);
            list.put("CRIMSON_FUNGUS", 1);
            list.put("CRIMSON_HYPHAE", 1);
            list.put("CRIMSON_NYLIUM", 1);
            list.put("CRIMSON_PLANKS", 1);
            list.put("CRIMSON_PRESSURE_PLATE", 1);
            list.put("CRIMSON_ROOTS", 1);
            list.put("CRIMSON_SIGN", 2);
            list.put("CRIMSON_SLAB", 2);
            list.put("CRIMSON_STAIRS", 1);
            list.put("CRIMSON_STEM", 1);
            list.put("CRIMSON_TRAPDOOR", 6);
            list.put("CRYING_OBSIDIAN", 150);
            list.put("GILDED_BLACKSTONE", 40);
            list.put("LODESTONE", 1252);
            list.put("MUSIC_DISC_PIGSTEP", 100);
            list.put("NETHER_SPROUTS", 1);
            list.put("NETHERITE_AXE", 1870);
            list.put("NETHERITE_BLOCK", 10980);
            list.put("NETHERITE_BOOTS", 2070);
            list.put("NETHERITE_CHESTPLATE", 2970);
            list.put("NETHERITE_HELMET", 2220);
            list.put("NETHERITE_HOE", 1620);
            list.put("NETHERITE_INGOT", 1220);
            list.put("NETHERITE_LEGGINGS", 2720);
            list.put("NETHERITE_PICKAXE", 1870);
            list.put("NETHERITE_SCRAP", 200);
            list.put("NETHERITE_SHOVEL", 1430);
            list.put("NETHERITE_SWORD", 1640);
            list.put("POLISHED_BASALT", 1);
            list.put("POLISHED_BLACKSTONE", 1);
            list.put("POLISHED_BLACKSTONE_BRICK_SLAB", 1);
            list.put("POLISHED_BLACKSTONE_BRICK_STAIRS", 1);
            list.put("POLISHED_BLACKSTONE_BRICK_WALL", 1);
            list.put("POLISHED_BLACKSTONE_BRICKS", 1);
            list.put("POLISHED_BLACKSTONE_BUTTON", 1);
            list.put("POLISHED_BLACKSTONE_PRESSURE_PLATE", 6);
            list.put("POLISHED_BLACKSTONE_SLAB", 1);
            list.put("POLISHED_BLACKSTONE_STAIRS", 1);
            list.put("POLISHED_BLACKSTONE_WALL", 1);
            list.put("QUARTZ_BRICKS", 1);
            list.put("RESPAWN_ANCHOR", 1);
            list.put("SHROOMLIGHT", 20);
            list.put("SOUL_CAMPFIRE", 24);
            list.put("SOUL_LANTERN", 28);
            list.put("SOUL_SOIL", 1);
            list.put("SOUL_TORCH", 6);
            list.put("STRIPPED_CRIMSON_HYPHAE", 2);
            list.put("STRIPPED_CRIMSON_STEM", 2);
            list.put("STRIPPED_WARPED_HYPHAE", 2);
            list.put("STRIPPED_WARPED_STEM", 2);
            list.put("TARGET", 200);
            list.put("TWISTING_VINES", 1);
            list.put("WARPED_BUTTON", 1);
            list.put("WARPED_DOOR", 3);
            list.put("WARPED_FENCE", 1);
            list.put("WARPED_FENCE_GATE", 5);
            list.put("WARPED_FUNGUS", 1);
            list.put("WARPED_FUNGUS_ON_A_STICK", 24);
            list.put("WARPED_HYPHAE", 1);
            list.put("WARPED_NYLIUM", 1);
            list.put("WARPED_PLANKS", 1);
            list.put("WARPED_PRESSURE_PLATE", 1);
            list.put("WARPED_ROOTS", 1);
            list.put("WARPED_SIGN", 2);
            list.put("WARPED_SLAB", 2);
            list.put("WARPED_STAIRS", 1);
            list.put("WARPED_STEM", 1);
            list.put("WARPED_TRAPDOOR", 6);
            list.put("WARPED_WART_BLOCK", 24);
            list.put("WEEPING_VINES", 1);
            // 1.17
            list.put("AMETHYST_BLOCK", 32);
            list.put("AMETHYST_SHARD", 8);
            list.put("AZALEA", 2);
            list.put("AZALEA_LEAVES", 1);
            list.put("BIG_DRIPLEAF", 2);
            list.put("BLACK_CANDLE", 35);
            list.put("BLUE_CANDLE", 35);
            list.put("BROWN_CANDLE", 35);
            list.put("CALCITE", 2);
            list.put("CANDLE", 25);
            list.put("CAVE_VINES", 2);
            list.put("CHISELED_DEEPSLATE", 3);
            list.put("COBBLED_DEEPSLATE", 2);
            list.put("COBBLED_DEEPSLATE_SLAB", 2);
            list.put("COBBLED_DEEPSLATE_STAIRS", 2);
            list.put("COBBLED_DEEPSLATE_WALL", 2);
            list.put("COPPER_BLOCK", 180);
            list.put("COPPER_INGOT", 20);
            list.put("CRACKED_DEEPSLATE_BRICKS", 4);
            list.put("CRACKED_DEEPSLATE_TILES", 2);
            list.put("CUT_COPPER", 180);
            list.put("CUT_COPPER_SLAB", 90);
            list.put("CUT_COPPER_STAIRS", 120);
            list.put("CYAN_CANDLE", 35);
            list.put("DEEPSLATE", 2);
            list.put("DEEPSLATE_BRICK_SLAB", 2);
            list.put("DEEPSLATE_BRICK_STAIRS", 2);
            list.put("DEEPSLATE_BRICK_WALL", 2);
            list.put("DEEPSLATE_BRICKS", 4);
            list.put("DEEPSLATE_TILE_SLAB", 2);
            list.put("DEEPSLATE_TILE_STAIRS", 2);
            list.put("DEEPSLATE_TILE_WALL", 2);
            list.put("DEEPSLATE_TILES", 2);
            list.put("DRIPSTONE_BLOCK", 2);
            list.put("EXPOSED_COPPER", 180);
            list.put("EXPOSED_CUT_COPPER", 180);
            list.put("EXPOSED_CUT_COPPER_SLAB", 90);
            list.put("EXPOSED_CUT_COPPER_STAIRS", 120);
            list.put("FLOWERING_AZALEA", 2);
            list.put("FLOWERING_AZALEA_LEAVES", 2);
            list.put("GLOW_BERRIES", 10);
            list.put("GLOW_INK_SAC", 10);
            list.put("GLOW_ITEM_FRAME", 22);
            list.put("GLOW_LICHEN", 4);
            list.put("GRAY_CANDLE", 35);
            list.put("GREEN_CANDLE", 35);
            list.put("HANGING_ROOTS", 2);
            list.put("LIGHT_BLUE_CANDLE", 35);
            list.put("LIGHT_GRAY_CANDLE", 35);
            list.put("LIGHTNING_ROD", 2);
            list.put("LIME_CANDLE", 35);
            list.put("MAGENTA_CANDLE", 35);
            list.put("MOSS_BLOCK", 16);
            list.put("MOSS_CARPET", 2);
            list.put("ORANGE_CANDLE", 35);
            list.put("OXIDIZED_COPPER", 180);
            list.put("OXIDIZED_CUT_COPPER", 180);
            list.put("OXIDIZED_CUT_COPPER_SLAB", 90);
            list.put("OXIDIZED_CUT_COPPER_STAIRS", 120);
            list.put("PINK_CANDLE", 35);
            list.put("POINTED_DRIPSTONE", 2);
            list.put("POLISHED_DEEPSLATE", 2);
            list.put("POLISHED_DEEPSLATE_SLAB", 2);
            list.put("POLISHED_DEEPSLATE_STAIRS", 2);
            list.put("POLISHED_DEEPSLATE_WALL", 2);
            list.put("PURPLE_CANDLE", 35);
            list.put("RAW_COPPER", 20);
            list.put("RAW_GOLD", 100);
            list.put("RAW_IRON", 20);
            list.put("RED_CANDLE", 35);
            list.put("ROOTED_DIRT", 2);
            list.put("SMALL_DRIPLEAF", 2);
            list.put("SMOOTH_BASALT", 2);
            list.put("SPORE_BLOSSOM", 2);
            list.put("SPYGLASS", 2);
            list.put("TINTED_GLASS", 36);
            list.put("TUFF", 2);
            list.put("WAXED_COPPER_BLOCK", 180);
            list.put("WAXED_CUT_COPPER", 180);
            list.put("WAXED_CUT_COPPER_SLAB", 90);
            list.put("WAXED_CUT_COPPER_STAIRS", 120);
            list.put("WAXED_EXPOSED_COPPER", 180);
            list.put("WAXED_EXPOSED_CUT_COPPER", 180);
            list.put("WAXED_EXPOSED_CUT_COPPER_SLAB", 90);
            list.put("WAXED_EXPOSED_CUT_COPPER_STAIRS", 120);
            list.put("WAXED_OXIDIZED_COPPER", 180);
            list.put("WAXED_OXIDIZED_CUT_COPPER", 180);
            list.put("WAXED_OXIDIZED_CUT_COPPER_SLAB", 90);
            list.put("WAXED_OXIDIZED_CUT_COPPER_STAIRS", 120);
            list.put("WAXED_WEATHERED_COPPER", 180);
            list.put("WAXED_WEATHERED_CUT_COPPER", 180);
            list.put("WAXED_WEATHERED_CUT_COPPER_SLAB", 90);
            list.put("WAXED_WEATHERED_CUT_COPPER_STAIRS", 120);
            list.put("WEATHERED_COPPER", 180);
            list.put("WEATHERED_CUT_COPPER", 180);
            list.put("WEATHERED_CUT_COPPER_SLAB", 90);
            list.put("WEATHERED_CUT_COPPER_STAIRS", 120);
            list.put("WHITE_CANDLE", 35);
            list.put("YELLOW_CANDLE", 35);
            // 1.18
            list.put("MUSIC_DISC_FAR", 100);
            list.put("MUSIC_DISC_OTHERSIDE", 100);
            // 1.19 - Wild Update
            list.put("MANGROVE_BOAT", 3);
            list.put("MANGROVE_BUTTON", 1);
            list.put("MANGROVE_DOOR", 3);
            list.put("MANGROVE_FENCE", 1);
            list.put("MANGROVE_FENCE_GATE", 5);
            list.put("MANGROVE_LEAVES", 1);
            list.put("MANGROVE_LOG", 2);
            list.put("MANGROVE_PLANKS", 2);
            list.put("MANGROVE_PRESSURE_PLATE", 1);
            list.put("MANGROVE_PROPAGULE", 2);
            list.put("MANGROVE_ROOTS", 2);
            list.put("MANGROVE_SIGN", 2);
            list.put("MANGROVE_SLAB", 2);
            list.put("MANGROVE_STAIRS", 1);
            list.put("MANGROVE_TRAPDOOR", 6);
            list.put("MANGROVE_WOOD", 2);
            list.put("STRIPPED_MANGROVE_LOG", 2);
            list.put("STRIPPED_MMANGROVE_WOOD", 2);
            list.put("MUDDY_MANGROVE_ROOTS", 2);
            // ---
            list.put("MUD", 2);
            list.put("MUD_BRICKS", 16);
            list.put("MUD_BRICK_SLAB", 8);
            list.put("MUD_BRICK_STAIRS", 24);
            list.put("MUD_BRICK_WALL", 16);
            list.put("PACKED_MUD", 4);
            // ---
            list.put("OCHRE_FROGLIGHT", 150);
            list.put("PEARLESCENT_FROGLIGHT", 150);
            list.put("VERDANT_FROGLIGHT", 150);
            // ---
            list.put("REINFORCED_DEEPSLATE", 100);
            // ---
            list.put("OAK_CHEST_BOAT", 8);
            list.put("SPRUCE_CHEST_BOAT", 8);
            list.put("BIRCH_CHEST_BOAT", 8);
            list.put("JUNGLE_CHEST_BOAT", 8);
            list.put("ACACIA_CHEST_BOAT", 8);
            list.put("DARK_OAK_CHEST_BOAT", 8);
            list.put("MANGROVE_CHEST_BOAT", 8);
            // --
            list.put("TADPOLE_BUCKET", 40);
            list.put("POWDER_SNOW_BUCKET", 40);
            list.put("PUFFERFISH_BUCKET", 40);
            list.put("SALMON_BUCKET", 40);
            list.put("COD_BUCKET", 40);
            list.put("TROPICAL_FISH_BUCKET", 40);
            list.put("AXOLOTL_BUCKET", 40);
            list.put("RECOVERY_COMPASS", 182);
            list.put("MUSIC_DISC_5", 180);
            list.put("DISC_FRAGMENT_5", 20);
            list.put("GOAT_HORN", 20);
            list.put("ECHO_SHARD", 20);
            // --
            list.put("SCULK", 2);
            list.put("SCULK_CATALYST", 2);
            list.put("SCULK_SENSOR", 2);
            list.put("SCULK_SHRIEKER", 2);
            list.put("SCULK_VEIN", 2);
            // Missing BEE hive and nest
            list.put("BEEHIVE", 100);
            list.put("BEE_NEST", 100);
            // 1.20
            list.put("ACACIA_HANGING_SIGN", 2);
            list.put("COAST_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("DUNE_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("EYE_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("HOST_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("RAISER_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("RIB_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("SHAPER_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("TIDE_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("VEX_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("WARD_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("WAYFINDER_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("WILD_ARMOR_TRIM_SMITHING_TEMPLATE", 1400);
            list.put("BAMBOO_BLOCK", 2);
            list.put("BAMBOO_BUTTON", 1);
            list.put("BAMBOO_CHEST_RAFT", 8);
            list.put("BAMBOO_DOOR", 3);
            list.put("BAMBOO_FENCE", 1);
            list.put("BAMBOO_FENCE_GATE", 5);
            list.put("BAMBOO_HANGING_SIGN", 2);
            list.put("BAMBOO_MOSAIC", 2);
            list.put("BAMBOO_MOSAIC_SLAB", 2);
            list.put("BAMBOO_MOSAIC_STAIRS", 1);
            list.put("BAMBOO_PLANKS", 2);
            list.put("BAMBOO_PRESSURE_PLATE", 1);
            list.put("BAMBOO_RAFT", 3);
            list.put("BAMBOO_SIGN", 2);
            list.put("BAMBOO_SLAB", 2);
            list.put("BAMBOO_STAIRS", 1);
            list.put("BAMBOO_TRAPDOOR", 6);
            list.put("BIRCH_HANGING_SIGN", 2);
            list.put("CALIBRATED_SKULK_SENSOR", 300);
            list.put("CHERRY_BOAT", 3);
            list.put("CHERRY_BUTTON", 1);
            list.put("CHERRY_DOOR", 3);
            list.put("CHERRY_FENCE", 1);
            list.put("CHERRY_FENCE_GATE", 5);
            list.put("CHERRY_HANGING_SIGN", 2);
            list.put("CHERRY_LEAVES", 1);
            list.put("CHERRY_LOG", 2);
            list.put("CHERRY_PLANKS", 2);
            list.put("CHERRY_PRESSURE_PLATE", 1);
            list.put("CHERRY_SAPLING", 2);
            list.put("CHERRY_SIGN", 2);
            list.put("CHERRY_SLAB", 2);
            list.put("CHERRY_STAIRS", 1);
            list.put("CHERRY_TRAPDOOR", 6);
            list.put("CHERRY_WOOD", 2);
            list.put("CHISELED_BOOKSHELF", 20);
            list.put("CRIMSON_HANGING_SIGN", 2);
            list.put("DARK_OAK_HANGING_SIGN", 2);
            list.put("DECORATED_POT", 80);
            list.put("JUNGLE_HANGING_SIGN", 2);
            list.put("MANGROVE_HANGING_SIGN", 2);
            list.put("MUSIC_DISC_RELIC", 120);
            list.put("OAK_HANGING_SIGN", 2);
            list.put("PIGLIN_HEAD", 200);
            list.put("PINK_PETALS", 2);
            list.put("PITCHER_PLANT", 2);
            list.put("PITCHER_POD", 2);
            list.put("ANGLER_POTTERY_SHERD", 20);
            list.put("ARCHER_POTTERY_SHERD", 20);
            list.put("ARMS_UP_POTTERY_SHERD", 20);
            list.put("BLADE_POTTERY_SHERD", 20);
            list.put("BREWER_POTTERY_SHERD", 20);
            list.put("BURN_POTTERY_SHERD", 20);
            list.put("DANGER_POTTERY_SHERD", 20);
            list.put("EXPLORER_POTTERY_SHERD", 20);
            list.put("FRIEND_POTTERY_SHERD", 20);
            list.put("HEART_POTTERY_SHERD", 20);
            list.put("HEARTBREAK_POTTERY_SHERD", 20);
            list.put("HOWL_POTTERY_SHERD", 20);
            list.put("MINER_POTTERY_SHERD", 20);
            list.put("MOURNER_POTTERY_SHERD", 20);
            list.put("PLENTY_POTTERY_SHERD", 20);
            list.put("PRIZE_POTTERY_SHERD", 20);
            list.put("SHEAF_POTTERY_SHERD", 20);
            list.put("SHELTER_POTTERY_SHERD", 20);
            list.put("SKULL_POTTERY_SHERD", 20);
            list.put("SNORT_POTTERY_SHERD", 20);
            list.put("SKULK_SENSOR", 30);
            list.put("SPRUCE_HANGING_SIGN", 2);
            list.put("STRIPPED_BAMBOO_BLOCK", 2);
            list.put("STRIPPED_CHERRY_LOG", 2);
            list.put("STRIPPED_CHERRY_WOOD", 2);
            list.put("TORCHFLOWER", 20);
            list.put("TORCHFLOWER_SEEDS", 20);
            list.put("WARPED_HANGING_SIGN", 2);
            //
            List<String> remove = new ArrayList();
            remove.add("ACACIA_BARK");
            remove.add("BANNER");
            remove.add("BED");
            remove.add("BIRCH_BARK");
            remove.add("BIRCH_WOOD_STAIRS");
            remove.add("CHORUS_FRUIT_POPPED");
            remove.add("CLOWNFISH");
            remove.add("DARK_OAK_BARK");
            remove.add("END_BRICKS");
            remove.add("JUNGLE_BARK");
            remove.add("JUNGLE_WOOD_STAIRS");
            remove.add("MUSHROOM_SOUP");
            remove.add("NETHER_STALK");
            remove.add("OAK_BARK");
            remove.add("RED_ROSE");
            remove.add("SEA_GRASS");
            remove.add("SPECKLED_MELON");
            remove.add("SPRUCE_WOOD_STAIRS");
            remove.add("STONE_PLATE");
            remove.add("STORAGE_MINECART");
            remove.add("SULPHUR");

            int i = 0;
            for (Map.Entry<String, Object> entry : list.entrySet()) {
                if (!plugin.getCondensablesConfig().contains(entry.getKey())) {
                    plugin.getCondensablesConfig().set(entry.getKey(), entry.getValue());
                    i++;
                }
            }
            for (String r : remove) {
                if (plugin.getCondensablesConfig().contains(r)) {
                    plugin.getCondensablesConfig().set(r, null);
                    i++;
                }
            }
            // fix Material
            if (plugin.getCondensablesConfig().contains("Material")) {
                plugin.getCondensablesConfig().set("Material", null);
                i++;
            }
            // fix BRICK_STAIRS
            if (plugin.getCondensablesConfig().getInt("BRICK_STAIRS") == 121) {
                plugin.getCondensablesConfig().set("BRICKS", 20);
                plugin.getCondensablesConfig().set("BRICK_SLAB", 10);
                plugin.getCondensablesConfig().set("BRICK_STAIRS", 30);
                plugin.getCondensablesConfig().set("BRICK_WALL", 20);
                i++;
            }
            if (plugin.getCondensablesConfig().contains("Material.TADPOLE_BUCKET")) {
                // remove
                plugin.getCondensablesConfig().set("Material m = Material.TADPOLE_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.POWDER_SNOW_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.PUFFERFISH_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.SALMON_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.COD_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.TROPICAL_FISH_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.AXOLOTL_BUCKET", null);
                plugin.getCondensablesConfig().set("Material m = Material.RECOVERY_COMPASS", null);
                plugin.getCondensablesConfig().set("Material m = Material.MUSIC_DISC_5", null);
                plugin.getCondensablesConfig().set("Material m = Material.DISC_FRAGMENT_5", null);
                plugin.getCondensablesConfig().set("Material m = Material.GOAT_HORN", null);
                plugin.getCondensablesConfig().set("Material m = Material.ECHO_SHARD", null);
                // correct
                plugin.getCondensablesConfig().set("TADPOLE_BUCKET", 40);
                plugin.getCondensablesConfig().set("POWDER_SNOW_BUCKET", 40);
                plugin.getCondensablesConfig().set("PUFFERFISH_BUCKET", 40);
                plugin.getCondensablesConfig().set("SALMON_BUCKET", 40);
                plugin.getCondensablesConfig().set("COD_BUCKET", 40);
                plugin.getCondensablesConfig().set("TROPICAL_FISH_BUCKET", 40);
                plugin.getCondensablesConfig().set("AXOLOTL_BUCKET", 40);
                plugin.getCondensablesConfig().set("RECOVERY_COMPASS", 182);
                plugin.getCondensablesConfig().set("MUSIC_DISC_5", 180);
                plugin.getCondensablesConfig().set("DISC_FRAGMENT_5", 20);
                plugin.getCondensablesConfig().set("GOAT_HORN", 20);
                plugin.getCondensablesConfig().set("ECHO_SHARD", 20);
                i++;
            }
            // fix sherds
            if (plugin.getCondensablesConfig().contains("POTTERY_SHARD_ARCHER")) {
                plugin.getCondensablesConfig().set("POTTERY_SHARD_ARCHER", null);
                plugin.getCondensablesConfig().set("POTTERY_SHARD_PRIZE", null);
                plugin.getCondensablesConfig().set("POTTERY_SHARD_ARMS_UP", null);
                plugin.getCondensablesConfig().set("POTTERY_SHARD_SKULL", null);
                i++;
            }
            if (plugin.getCondensablesConfig().contains("ARMS_POTTERY_SHERD UP")) {
                plugin.getCondensablesConfig().set("ARMS_POTTERY_SHERD UP", null);
                i++;
            }
            if (i > 0) {
                try {
                    String listPath = plugin.getDataFolder() + File.separator + "condensables.yml";
                    plugin.getCondensablesConfig().save(new File(listPath));
                    plugin.getMessenger().message(plugin.getConsole(), TardisModule.TARDIS, "Added/updated " + i + " new items to condensables.yml");
                } catch (IOException io) {
                    plugin.debug("Could not save condensables.yml, " + io.getMessage());
                }
            }
        }
    }
}
