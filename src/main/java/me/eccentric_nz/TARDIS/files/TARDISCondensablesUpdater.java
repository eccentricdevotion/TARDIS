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

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
            list.put("ACACIA_BARK", null);
            list.put("ACACIA_WOOD", 2);
            list.put("BANNER", null);
            list.put("BED", null);
            list.put("BIRCH_BARK", null);
            list.put("BIRCH_STAIRS", 11);
            list.put("BIRCH_WOOD", 2);
            list.put("BIRCH_WOOD_STAIRS", null);
            list.put("CHEST_MINECART", 30);
            list.put("CHORUS_FRUIT_POPPED", null);
            list.put("CLOWNFISH", null);
            list.put("DARK_OAK_BARK", null);
            list.put("DARK_OAK_WOOD", 2);
            list.put("END_BRICKS", null);
            list.put("END_STONE_BRICKS", 21);
            list.put("GLISTERING_MELON_SLICE", 22);
            list.put("GUNPOWDER", 20);
            list.put("JUNGLE_BARK", null);
            list.put("JUNGLE_STAIRS", 11);
            list.put("JUNGLE_WOOD", 2);
            list.put("JUNGLE_WOOD_STAIRS", null);
            list.put("MUSHROOM_SOUP", null);
            list.put("NETHER_STALK", null);
            list.put("OAK_BARK", null);
            list.put("OAK_WOOD", 2);
            list.put("POPPED_CHORUS_FRUIT", 8);
            list.put("POPPY", 2);
            list.put("RED_ROSE", null);
            list.put("SEA_GRASS", null);
            list.put("SEAGRASS", 2);
            list.put("SPECKLED_MELON", null);
            list.put("SPRUCE_STAIRS", 11);
            list.put("SPRUCE_WOOD_STAIRS", null);
            list.put("STONE_PLATE", null);
            list.put("STORAGE_MINECART", null);
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
            list.put("SULPHUR", null);
            list.put("TROPICAL_FISH", 5);
            // 1.14
            list.put("ACACIA_SIGN", 2);
            list.put("ACACIA_WALL_SIGN", 2);
            list.put("ANDESITE_SLAB", 2);
            list.put("ANDESITE_STAIRS", 2);
            list.put("ANDESITE_WALL", 2);
            list.put("BAMBOO", 2);
            list.put("BARREL", 16);
            list.put("BELL", 100);
            list.put("BIRCH_SIGN", 2);
            list.put("BIRCH_WALL_SIGN", 2);
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
            list.put("DARK_OAK_WALL_SIGN", 2);
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
            list.put("JUNGLE_SIGN", 2);
            list.put("JUNGLE_WALL_SIGN", 2);
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
            list.put("OAK_WALL_SIGN", 2);
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
            list.put("SPRUCE_WALL_SIGN", 2);
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
            list.put("NETHER_GOLD_ORE", 105);
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

            for (Map.Entry<String, Object> entry : list.entrySet()) {
                plugin.getCondensablesConfig().set(entry.getKey(), entry.getValue());
            }
            try {
                String condensablesPath = plugin.getDataFolder() + File.separator + "condensables.yml";
                plugin.getCondensablesConfig().save(new File(condensablesPath));
                plugin.getConsole().sendMessage(plugin.getPluginName() + "Updated condensables.yml");
            } catch (IOException io) {
                plugin.debug("Could not save condensables.yml, " + io.getMessage());
            }
        }
    }
}
