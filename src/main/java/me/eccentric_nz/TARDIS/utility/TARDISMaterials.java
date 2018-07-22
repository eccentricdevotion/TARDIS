/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class TARDISMaterials {

    public static final List<Material> glass = Arrays.asList(Material.BLACK_STAINED_GLASS, Material.BLACK_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS, Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS, Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS, Material.CYAN_STAINED_GLASS_PANE, Material.GLASS, Material.GLASS_PANE, Material.GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS, Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS, Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS, Material.PINK_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS, Material.WHITE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS, Material.YELLOW_STAINED_GLASS_PANE);

    public static final List<Material> stained_glass = Arrays.asList(Material.BLACK_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.WHITE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS);

    public static final List<Material> dyes = Arrays.asList(Material.BONE_MEAL, Material.CACTUS_GREEN, Material.COCOA_BEANS, Material.CYAN_DYE, Material.DANDELION_YELLOW, Material.GRAY_DYE, Material.INK_SAC, Material.LAPIS_LAZULI, Material.LIGHT_BLUE_DYE, Material.LIGHT_GRAY_DYE, Material.LIME_DYE, Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.PINK_DYE, Material.PURPLE_DYE, Material.ROSE_RED);

    public static final List<Material> doors = Arrays.asList(Material.IRON_DOOR, Material.IRON_TRAPDOOR, Material.OAK_DOOR, Material.BIRCH_DOOR, Material.JUNGLE_DOOR, Material.SPRUCE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR);

    public static final List<Material> trapdoors = Arrays.asList(Material.IRON_TRAPDOOR, Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR);

    public static List<Material> fences = Arrays.asList(Material.OAK_FENCE, Material.BIRCH_FENCE, Material.JUNGLE_FENCE, Material.SPRUCE_FENCE, Material.ACACIA_FENCE, Material.DARK_OAK_FENCE);

    public static final List<Material> pressure_plates = Arrays.asList(Material.OAK_PRESSURE_PLATE, Material.JUNGLE_PRESSURE_PLATE, Material.BIRCH_PRESSURE_PLATE, Material.SPRUCE_PRESSURE_PLATE, Material.ACACIA_PRESSURE_PLATE, Material.DARK_OAK_PRESSURE_PLATE, Material.STONE_PRESSURE_PLATE);

    public static final List<Material> plants = Arrays.asList(Material.ALLIUM, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.CACTUS, Material.DANDELION, Material.DEAD_BUSH, Material.FERN, Material.GRASS, Material.LARGE_FERN, Material.LILAC, Material.ORANGE_TULIP, Material.OXEYE_DAISY, Material.PEONY, Material.PINK_TULIP, Material.POPPY, Material.RED_TULIP, Material.ROSE_BUSH, Material.SUNFLOWER, Material.TALL_GRASS, Material.WHITE_TULIP, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);

    public static final List<Material> carpet = Arrays.asList(Material.WHITE_CARPET, Material.ORANGE_CARPET, Material.MAGENTA_CARPET, Material.LIGHT_BLUE_CARPET, Material.YELLOW_CARPET, Material.LIME_CARPET, Material.PINK_CARPET, Material.GRAY_CARPET, Material.LIGHT_GRAY_CARPET, Material.CYAN_CARPET, Material.PURPLE_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.GREEN_CARPET, Material.RED_CARPET, Material.BLACK_CARPET);

    public static final List<Material> saplings = Arrays.asList(Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.DARK_OAK_SAPLING, Material.JUNGLE_SAPLING, Material.OAK_SAPLING, Material.SPRUCE_SAPLING);

    public static final List<Material> has_colour = Arrays.asList(Material.BLACK_CARPET, Material.BLACK_CONCRETE_POWDER, Material.BLACK_CONCRETE, Material.BLACK_GLAZED_TERRACOTTA, Material.BLACK_SHULKER_BOX, Material.BLACK_STAINED_GLASS_PANE, Material.BLACK_STAINED_GLASS, Material.BLACK_TERRACOTTA, Material.BLACK_WOOL, Material.BLUE_CARPET, Material.BLUE_CONCRETE_POWDER, Material.BLUE_CONCRETE, Material.BLUE_GLAZED_TERRACOTTA, Material.BLUE_SHULKER_BOX, Material.BLUE_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS, Material.BLUE_TERRACOTTA, Material.BLUE_WOOL, Material.BROWN_CARPET, Material.BROWN_CONCRETE_POWDER, Material.BROWN_CONCRETE, Material.BROWN_GLAZED_TERRACOTTA, Material.BROWN_SHULKER_BOX, Material.BROWN_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS, Material.BROWN_TERRACOTTA, Material.BROWN_WOOL, Material.CYAN_CARPET, Material.CYAN_CONCRETE_POWDER, Material.CYAN_CONCRETE, Material.CYAN_GLAZED_TERRACOTTA, Material.CYAN_SHULKER_BOX, Material.CYAN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS, Material.CYAN_TERRACOTTA, Material.CYAN_WOOL, Material.GRAY_CARPET, Material.GRAY_CONCRETE_POWDER, Material.GRAY_CONCRETE, Material.GRAY_GLAZED_TERRACOTTA, Material.GRAY_SHULKER_BOX, Material.GRAY_STAINED_GLASS_PANE, Material.GRAY_STAINED_GLASS, Material.GRAY_TERRACOTTA, Material.GRAY_WOOL, Material.GREEN_CARPET, Material.GREEN_CONCRETE_POWDER, Material.GREEN_CONCRETE, Material.GREEN_GLAZED_TERRACOTTA, Material.GREEN_SHULKER_BOX, Material.GREEN_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS, Material.GREEN_TERRACOTTA, Material.GREEN_WOOL, Material.LIGHT_BLUE_CARPET, Material.LIGHT_BLUE_CONCRETE_POWDER, Material.LIGHT_BLUE_CONCRETE, Material.LIGHT_BLUE_GLAZED_TERRACOTTA, Material.LIGHT_BLUE_SHULKER_BOX, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_BLUE_TERRACOTTA, Material.LIGHT_BLUE_WOOL, Material.LIGHT_GRAY_CARPET, Material.LIGHT_GRAY_CONCRETE_POWDER, Material.LIGHT_GRAY_CONCRETE, Material.LIGHT_GRAY_GLAZED_TERRACOTTA, Material.LIGHT_GRAY_SHULKER_BOX, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS, Material.LIGHT_GRAY_TERRACOTTA, Material.LIGHT_GRAY_WOOL, Material.LIME_CARPET, Material.LIME_CONCRETE_POWDER, Material.LIME_CONCRETE, Material.LIME_GLAZED_TERRACOTTA, Material.LIME_SHULKER_BOX, Material.LIME_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS, Material.LIME_TERRACOTTA, Material.LIME_WOOL, Material.MAGENTA_CARPET, Material.MAGENTA_CONCRETE_POWDER, Material.MAGENTA_CONCRETE, Material.MAGENTA_GLAZED_TERRACOTTA, Material.MAGENTA_SHULKER_BOX, Material.MAGENTA_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS, Material.MAGENTA_TERRACOTTA, Material.MAGENTA_WOOL, Material.ORANGE_CARPET, Material.ORANGE_CONCRETE_POWDER, Material.ORANGE_CONCRETE, Material.ORANGE_GLAZED_TERRACOTTA, Material.ORANGE_SHULKER_BOX, Material.ORANGE_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS, Material.ORANGE_TERRACOTTA, Material.ORANGE_WOOL, Material.PINK_CARPET, Material.PINK_CONCRETE_POWDER, Material.PINK_CONCRETE, Material.PINK_GLAZED_TERRACOTTA, Material.PINK_SHULKER_BOX, Material.PINK_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS, Material.PINK_TERRACOTTA, Material.PINK_WOOL, Material.PURPLE_CARPET, Material.PURPLE_CONCRETE_POWDER, Material.PURPLE_CONCRETE, Material.PURPLE_GLAZED_TERRACOTTA, Material.PURPLE_SHULKER_BOX, Material.PURPLE_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS, Material.PURPLE_TERRACOTTA, Material.PURPLE_WOOL, Material.RED_CARPET, Material.RED_CONCRETE_POWDER, Material.RED_CONCRETE, Material.RED_GLAZED_TERRACOTTA, Material.RED_SHULKER_BOX, Material.RED_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS, Material.RED_TERRACOTTA, Material.RED_WOOL, Material.WHITE_CARPET, Material.WHITE_CONCRETE_POWDER, Material.WHITE_CONCRETE, Material.WHITE_GLAZED_TERRACOTTA, Material.WHITE_SHULKER_BOX, Material.WHITE_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS, Material.WHITE_TERRACOTTA, Material.WHITE_WOOL, Material.YELLOW_CARPET, Material.YELLOW_CONCRETE_POWDER, Material.YELLOW_CONCRETE, Material.YELLOW_GLAZED_TERRACOTTA, Material.YELLOW_SHULKER_BOX, Material.YELLOW_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS, Material.YELLOW_TERRACOTTA, Material.YELLOW_WOOL);

    public static final List<Material> infested = Arrays.asList(Material.INFESTED_CHISELED_STONE_BRICKS, Material.INFESTED_COBBLESTONE, Material.INFESTED_CRACKED_STONE_BRICKS, Material.INFESTED_MOSSY_STONE_BRICKS, Material.INFESTED_STONE, Material.INFESTED_STONE_BRICKS);

    public static final List<Material> not_glass = Arrays.asList(Material.AIR, Material.GLASS, Material.TORCH, Material.SIGN, Material.OAK_DOOR, Material.BIRCH_DOOR, Material.SPRUCE_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.WALL_SIGN, Material.IRON_DOOR, Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH, Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.VINE, Material.REDSTONE_LAMP);

    public static final List<Material> precious = Arrays.asList(Material.BEDROCK, Material.COAL_ORE, Material.DIAMOND_BLOCK, Material.DIAMOND_ORE, Material.EMERALD_BLOCK, Material.EMERALD_ORE, Material.NETHER_QUARTZ_ORE, Material.GOLD_BLOCK, Material.GOLD_ORE, Material.IRON_BLOCK, Material.IRON_ORE, Material.LAPIS_BLOCK, Material.LAPIS_BLOCK, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE);

    public static final List<Material> crops = Arrays.asList(Material.SUGAR_CANE, Material.WHEAT, Material.CARROTS, Material.BEETROOTS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.NETHER_WART, Material.POTATOES, Material.COCOA);

    public static final List<Material> fish_buckets = Arrays.asList(Material.TROPICAL_FISH_BUCKET, Material.COD_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET);

    public static final HashMap<Material, EntityType> fishMap = new HashMap<Material, EntityType>() {
        private static final long serialVersionUID = 3109256773218160485L;

        {
            put(Material.TROPICAL_FISH_BUCKET, EntityType.TROPICAL_FISH);
            put(Material.COD_BUCKET, EntityType.COD);
            put(Material.PUFFERFISH_BUCKET, EntityType.PUFFERFISH);
            put(Material.SALMON_BUCKET, EntityType.SALMON);
        }
    };
}
