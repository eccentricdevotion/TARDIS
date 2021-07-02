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
package me.eccentric_nz.TARDIS.utility;

import com.google.common.collect.Sets;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.entity.EntityType;

import java.io.Serial;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

/**
 * @author eccentric_nz
 */
public class TARDISMaterials {

    public static final List<Material> glass = Arrays.asList(Material.BLACK_STAINED_GLASS, Material.BLACK_STAINED_GLASS_PANE, Material.BLUE_STAINED_GLASS, Material.BLUE_STAINED_GLASS_PANE, Material.BROWN_STAINED_GLASS, Material.BROWN_STAINED_GLASS_PANE, Material.CYAN_STAINED_GLASS, Material.CYAN_STAINED_GLASS_PANE, Material.GLASS, Material.GLASS_PANE, Material.GRAY_STAINED_GLASS, Material.GRAY_STAINED_GLASS_PANE, Material.GREEN_STAINED_GLASS, Material.GREEN_STAINED_GLASS_PANE, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS_PANE, Material.LIGHT_GRAY_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS_PANE, Material.LIME_STAINED_GLASS, Material.LIME_STAINED_GLASS_PANE, Material.MAGENTA_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS_PANE, Material.ORANGE_STAINED_GLASS, Material.ORANGE_STAINED_GLASS_PANE, Material.PINK_STAINED_GLASS, Material.PINK_STAINED_GLASS_PANE, Material.PURPLE_STAINED_GLASS, Material.PURPLE_STAINED_GLASS_PANE, Material.RED_STAINED_GLASS, Material.RED_STAINED_GLASS_PANE, Material.WHITE_STAINED_GLASS, Material.WHITE_STAINED_GLASS_PANE, Material.YELLOW_STAINED_GLASS, Material.YELLOW_STAINED_GLASS_PANE);

    public static final List<Material> stained_glass = Arrays.asList(Material.BLACK_STAINED_GLASS, Material.BLUE_STAINED_GLASS, Material.BROWN_STAINED_GLASS, Material.CYAN_STAINED_GLASS, Material.GRAY_STAINED_GLASS, Material.GREEN_STAINED_GLASS, Material.LIGHT_BLUE_STAINED_GLASS, Material.LIGHT_GRAY_STAINED_GLASS, Material.LIME_STAINED_GLASS, Material.MAGENTA_STAINED_GLASS, Material.ORANGE_STAINED_GLASS, Material.PINK_STAINED_GLASS, Material.PURPLE_STAINED_GLASS, Material.RED_STAINED_GLASS, Material.WHITE_STAINED_GLASS, Material.YELLOW_STAINED_GLASS);

    public static final List<Material> dyes = Arrays.asList(Material.WHITE_DYE, Material.GREEN_DYE, Material.BROWN_DYE, Material.CYAN_DYE, Material.YELLOW_DYE, Material.GRAY_DYE, Material.BLACK_DYE, Material.BLUE_DYE, Material.LIGHT_BLUE_DYE, Material.LIGHT_GRAY_DYE, Material.LIME_DYE, Material.MAGENTA_DYE, Material.ORANGE_DYE, Material.PINK_DYE, Material.PURPLE_DYE, Material.RED_DYE);

    public static final Set<Material> doors = Sets.union(Tag.DOORS.getValues(), Tag.TRAPDOORS.getValues());

    public static final List<Material> plants = Arrays.asList(Material.ALLIUM, Material.AZURE_BLUET, Material.BLUE_ORCHID, Material.CACTUS, Material.DANDELION, Material.DEAD_BUSH, Material.FERN, Material.GRASS, Material.LARGE_FERN, Material.LILAC, Material.ORANGE_TULIP, Material.OXEYE_DAISY, Material.PEONY, Material.PINK_TULIP, Material.POPPY, Material.RED_TULIP, Material.ROSE_BUSH, Material.SUNFLOWER, Material.TALL_GRASS, Material.WHITE_TULIP, Material.RED_MUSHROOM, Material.BROWN_MUSHROOM);

    public static final List<Material> carpet = Arrays.asList(Material.WHITE_CARPET, Material.ORANGE_CARPET, Material.MAGENTA_CARPET, Material.LIGHT_BLUE_CARPET, Material.YELLOW_CARPET, Material.LIME_CARPET, Material.PINK_CARPET, Material.GRAY_CARPET, Material.LIGHT_GRAY_CARPET, Material.CYAN_CARPET, Material.PURPLE_CARPET, Material.BLUE_CARPET, Material.BROWN_CARPET, Material.GREEN_CARPET, Material.RED_CARPET, Material.BLACK_CARPET);

    public static final List<Material> saplings = Arrays.asList(Material.ACACIA_SAPLING, Material.BIRCH_SAPLING, Material.DARK_OAK_SAPLING, Material.JUNGLE_SAPLING, Material.OAK_SAPLING, Material.SPRUCE_SAPLING);

    public static final List<Material> infested = Arrays.asList(Material.INFESTED_CHISELED_STONE_BRICKS, Material.INFESTED_COBBLESTONE, Material.INFESTED_CRACKED_STONE_BRICKS, Material.INFESTED_MOSSY_STONE_BRICKS, Material.INFESTED_STONE, Material.INFESTED_STONE_BRICKS);

    public static final List<Material> not_glass = Arrays.asList(Material.AIR, Material.GLASS, Material.TORCH, Material.WALL_TORCH, Material.OAK_SIGN, Material.DARK_OAK_SIGN, Material.SPRUCE_SIGN, Material.ACACIA_SIGN, Material.JUNGLE_SIGN, Material.BIRCH_SIGN, Material.OAK_DOOR, Material.BIRCH_DOOR, Material.SPRUCE_DOOR, Material.JUNGLE_DOOR, Material.ACACIA_DOOR, Material.DARK_OAK_DOOR, Material.CRIMSON_DOOR, Material.WARPED_DOOR, Material.OAK_WALL_SIGN, Material.DARK_OAK_WALL_SIGN, Material.CRIMSON_WALL_SIGN, Material.WARPED_WALL_SIGN, Material.SPRUCE_WALL_SIGN, Material.ACACIA_WALL_SIGN, Material.JUNGLE_WALL_SIGN, Material.BIRCH_WALL_SIGN, Material.IRON_DOOR, Material.SOUL_TORCH, Material.SOUL_WALL_TORCH, Material.REDSTONE_TORCH, Material.REDSTONE_WALL_TORCH, Material.OAK_TRAPDOOR, Material.BIRCH_TRAPDOOR, Material.SPRUCE_TRAPDOOR, Material.JUNGLE_TRAPDOOR, Material.ACACIA_TRAPDOOR, Material.DARK_OAK_TRAPDOOR, Material.CRIMSON_TRAPDOOR, Material.WARPED_TRAPDOOR, Material.VINE, Material.REDSTONE_LAMP);

    public static final List<Material> precious = Arrays.asList(Material.BARRIER, Material.BEDROCK, Material.COAL_ORE, Material.COPPER_ORE, Material.DEEPSLATE_COPPER_ORE, Material.DEEPSLATE_DIAMOND_ORE, Material.DEEPSLATE_EMERALD_ORE, Material.DEEPSLATE_GOLD_ORE, Material.DEEPSLATE_IRON_ORE, Material.DEEPSLATE_LAPIS_ORE, Material.DIAMOND_BLOCK, Material.DIAMOND_ORE, Material.EMERALD_BLOCK, Material.EMERALD_ORE, Material.GOLD_BLOCK, Material.GOLD_ORE, Material.IRON_BLOCK, Material.IRON_ORE, Material.LAPIS_BLOCK, Material.LAPIS_ORE, Material.LIGHT, Material.NETHER_QUARTZ_ORE, Material.OBSIDIAN, Material.REDSTONE_BLOCK, Material.REDSTONE_ORE);

    public static final List<Material> crops = Arrays.asList(Material.SUGAR_CANE, Material.WHEAT, Material.CARROTS, Material.BEETROOTS, Material.MELON_STEM, Material.PUMPKIN_STEM, Material.NETHER_WART, Material.POTATOES, Material.COCOA, Material.CACTUS, Material.SWEET_BERRY_BUSH);

    public static final List<Material> fish_buckets = Arrays.asList(Material.TROPICAL_FISH_BUCKET, Material.COD_BUCKET, Material.PUFFERFISH_BUCKET, Material.SALMON_BUCKET);

    public static final List<Material> submarine_blocks = Arrays.asList(Material.BLUE_ICE, Material.FROSTED_ICE, Material.ICE, Material.KELP_PLANT, Material.PACKED_ICE, Material.SEA_PICKLE, Material.SEAGRASS, Material.TALL_SEAGRASS, Material.WATER);

    public static final HashMap<Material, EntityType> fishMap = new HashMap<>() {

        @Serial
        private static final long serialVersionUID = 3109256773218160485L;

        {
            put(Material.TROPICAL_FISH_BUCKET, EntityType.TROPICAL_FISH);
            put(Material.COD_BUCKET, EntityType.COD);
            put(Material.PUFFERFISH_BUCKET, EntityType.PUFFERFISH);
            put(Material.SALMON_BUCKET, EntityType.SALMON);
        }
    };
}
