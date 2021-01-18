/*
 * Copyright (C) 2020 eccentric_nz
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
import org.bukkit.ChatColor;
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
            List<String> UNDER_BLOCKS = Arrays.asList("AIR", "CAVE_AIR", "VOID_AIR", "OAK_LEAVES", "BIRCH_LEAVES", "SPRUCE_LEAVES", "JUNGLE_LEAVES", "ACACIA_LEAVES", "DARK_OAK_LEAVES", "WATER", "LAVA", "OAK_SAPLING", "BIRCH_SAPLING", "SPRUCE_SAPLING", "JUNGLE_SAPLING", "ACACIA_SAPLING", "DARK_OAK_SAPLING", "GLASS", "WHITE_BED", "ORANGE_BED", "MAGENTA_BED", "LIGHT_BLUE_BED", "YELLOW_BED", "LIME_BED", "PINK_BED", "GRAY_BED", "LIGHT_GRAY_BED", "CYAN_BED", "PURPLE_BED", "BLUE_BED", "BROWN_BED", "GREEN_BED", "RED_BED", "BLACK_BED", "POWERED_RAIL", "DETECTOR_RAIL", "COBWEB", "GRASS", "DEAD_BUSH", "PISTON", "STICKY_PISTON", "PISTON_HEAD", "ALLIUM", "AZURE_BLUET", "BLUE_ORCHID", "FERN", "LARGE_FERN", "LILAC", "ORANGE_TULIP", "OXEYE_DAISY", "PEONY", "PINK_TULIP", "POPPY", "RED_TULIP", "ROSE_BUSH", "SUNFLOWER", "TALL_GRASS", "WHITE_TULIP", "RED_MUSHROOM", "BROWN_MUSHROOM", "TNT", "TORCH", "FIRE", "OAK_STAIRS", "BIRCH_STAIRS", "SPRUCE_STAIRS", "JUNGLE_STAIRS", "ACACIA_STAIRS", "DARK_OAK_STAIRS", "CHEST", "REDSTONE_WIRE", "FARMLAND", "FURNACE", "OAK_SIGN", "DARK_OAK_SIGN", "SPRUCE_SIGN", "JUNGLE_SIGN", "BIRCH_SIGN", "ACACIA_SIGN", "OAK_DOOR", "BIRCH_DOOR", "SPRUCE_DOOR", "JUNGLE_DOOR", "ACACIA_DOOR", "DARK_OAK_DOOR", "LADDER", "RAIL", "COBBLESTONE_STAIRS", "OAK_WALL_SIGN", "DARK_OAK_WALL_SIGN", "SPRUCE_WALL_SIGN", "BIRCH_WALL_SIGN", "JUNGLE_WALL_SIGN", "ACACIA_WALL_SIGN", "LEVER", "STONE_PRESSURE_PLATE", "HEAVY_WEIGHTED_PRESSURE_PLATE", "LIGHT_WEIGHTED_PRESSURE_PLATE", "IRON_DOOR", "OAK_PRESSURE_PLATE", "BIRCH_PRESSURE_PLATE", "SPRUCE_PRESSURE_PLATE", "JUNGLE_PRESSURE_PLATE", "ACACIA_PRESSURE_PLATE", "DARK_OAK_PRESSURE_PLATE", "REDSTONE_TORCH", "STONE_BUTTON", "SNOW", "ICE", "CACTUS", "SUGAR_CANE", "OAK_FENCE", "BIRCH_FENCE", "SPRUCE_FENCE", "JUNGLE_FENCE", "ACACIA_FENCE", "DARK_OAK_FENCE", "GLOWSTONE", "CAKE", "REPEATER", "OAK_TRAPDOOR", "BIRCH_TRAPDOOR", "SPRUCE_TRAPDOOR", "JUNGLE_TRAPDOOR", "ACACIA_TRAPDOOR", "DARK_OAK_TRAPDOOR", "IRON_BARS", "GLASS_PANE", "PUMPKIN_STEM", "MELON_STEM", "VINE", "OAK_FENCE_GATE", "BIRCH_FENCE_GATE", "SPRUCE_FENCE_GATE", "JUNGLE_FENCE_GATE", "ACACIA_FENCE_GATE", "DARK_OAK_FENCE_GATE", "BRICK_STAIRS", "STONE_BRICK_STAIRS", "LILY_PAD", "NETHER_BRICK_FENCE", "NETHER_BRICK_STAIRS", "NETHER_WART", "ENCHANTING_TABLE", "BREWING_STAND", "CAULDRON", "END_PORTAL", "END_PORTAL_FRAME", "DRAGON_EGG", "OAK_SLAB", "BIRCH_SLAB", "SPRUCE_SLAB", "JUNGLE_SLAB", "ACACIA_SLAB", "DARK_OAK_SLAB", "SANDSTONE_STAIRS", "ENDER_CHEST", "TRIPWIRE_HOOK", "TRIPWIRE", "IRON_TRAPDOOR", "WHITE_CARPET", "ORANGE_CARPET", "MAGENTA_CARPET", "LIGHT_BLUE_CARPET", "YELLOW_CARPET", "LIME_CARPET", "PINK_CARPET", "GRAY_CARPET", "LIGHT_GRAY_CARPET", "CYAN_CARPET", "PURPLE_CARPET", "BLUE_CARPET", "BROWN_CARPET", "GREEN_CARPET", "RED_CARPET", "BLACK_CARPET", "WHITE_BANNER", "ORANGE_BANNER", "MAGENTA_BANNER", "LIGHT_BLUE_BANNER", "YELLOW_BANNER", "LIME_BANNER", "PINK_BANNER", "GRAY_BANNER", "LIGHT_GRAY_BANNER", "CYAN_BANNER", "PURPLE_BANNER", "BLUE_BANNER", "BROWN_BANNER", "GREEN_BANNER", "RED_BANNER", "BLACK_BANNER", "WHITE_WALL_BANNER", "ORANGE_WALL_BANNER", "MAGENTA_WALL_BANNER", "LIGHT_BLUE_WALL_BANNER", "YELLOW_WALL_BANNER", "LIME_WALL_BANNER", "PINK_WALL_BANNER", "GRAY_WALL_BANNER", "LIGHT_GRAY_WALL_BANNER", "CYAN_WALL_BANNER", "PURPLE_WALL_BANNER", "BLUE_WALL_BANNER", "BROWN_WALL_BANNER", "GREEN_WALL_BANNER", "RED_WALL_BANNER", "BLACK_WALL_BANNER", "DAYLIGHT_DETECTOR", "RED_SANDSTONE_STAIRS", "END_ROD", "CHORUS_PLANT", "CHORUS_FLOWER", "PURPUR_STAIRS", "PURPUR_SLAB", "BEETROOTS", "GRASS_PATH", "FROSTED_ICE", "STRUCTURE_VOID", "WHITE_SHULKER_BOX", "ORANGE_SHULKER_BOX", "MAGENTA_SHULKER_BOX", "LIGHT_BLUE_SHULKER_BOX", "YELLOW_SHULKER_BOX", "LIME_SHULKER_BOX", "PINK_SHULKER_BOX", "GRAY_SHULKER_BOX", "LIGHT_GRAY_SHULKER_BOX", "CYAN_SHULKER_BOX", "PURPLE_SHULKER_BOX", "BLUE_SHULKER_BOX", "BROWN_SHULKER_BOX", "GREEN_SHULKER_BOX", "RED_SHULKER_BOX", "BLACK_SHULKER_BOX");
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
            under.add("WALL_SOUL_TORCH");
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
        try {
            blocks_config.save(new File(plugin.getDataFolder(), "blocks.yml"));
            if (i > 0) {
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Added " + ChatColor.AQUA + i + ChatColor.RESET + " new items to blocks.yml");
            }
        } catch (IOException io) {
            plugin.debug("Could not save blocks.yml, " + io.getMessage());
        }
    }
}
