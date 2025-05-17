/*
 * Copyright (C) 2025 eccentric_nz
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

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.builders.interior.TARDISBuildData;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;

import java.util.*;

/**
 * Keeps track of various building related lookups. these include: Room block
 * counts, Room seed blocks and Stained Glass block colour equivalents for
 * regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private static final HashMap<String, String> BLOCK_CONVERSION = initBlockConversion();
    private static final Set<String> IGNORE_BLOCKS = Sets.newHashSet("AIR", "CAVE_AIR", "VOID_AIR", "BEDROCK", "CAKE", "COMMAND_BLOCK", "REPEATING_COMMAND_BLOCK", "CHAIN_COMMAND_BLOCK", "GOLD_ORE", "MUSHROOM_STEM", "ICE", "LAVA", "SPAWNER", "INFESTED_CHISELED_STONE_BRICKS", "INFESTED_COBBLESTONE", "INFESTED_DEEPSLATE", "INFESTED_CRACKED_STONE_BRICKS", "INFESTED_MOSSY_STONE_BRICKS", "INFESTED_STONE", "INFESTED_STONE_BRICKS", "PISTON_HEAD", "SPONGE", "WATER", "JUKEBOX", "NOTE_BLOCK");
    private static final Set<Material> PRECIOUS = new HashSet<>();
    private static final Set<Integer> TIPS_SLOTS = new HashSet<>();

    static {
        // precious blocks
        PRECIOUS.add(Material.BEACON);
        PRECIOUS.add(Material.BEDROCK);
        PRECIOUS.add(Material.BUDDING_AMETHYST);
        PRECIOUS.add(Material.CONDUIT);
        PRECIOUS.add(Material.DIAMOND_BLOCK);
        PRECIOUS.add(Material.EMERALD_BLOCK);
        PRECIOUS.add(Material.GOLD_BLOCK);
        PRECIOUS.add(Material.IRON_BLOCK);
//        PRECIOUS.add(Material.COPPER_BLOCK);
        PRECIOUS.add(Material.RAW_GOLD_BLOCK);
        PRECIOUS.add(Material.RAW_IRON_BLOCK);
//        PRECIOUS.add(Material.RAW_COPPER_BLOCK);
        PRECIOUS.add(Material.LIGHT);
        PRECIOUS.add(Material.NETHERITE_BLOCK);
        PRECIOUS.add(Material.REDSTONE_BLOCK);
    }

    private final HashMap<Location, TARDISBuildData> trackTARDISSeed = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<>();
    private final HashMap<UUID, Integer> roomProgress = new HashMap<>();
    private HashMap<Material, String> seeds;

    /**
     * Gets a list of precious blocks to protect
     *
     * @return a set of precious blocks
     */
    public static Set<Material> getPrecious() {
        return PRECIOUS;
    }

    /**
     * Gets a list of used TIPS slots
     *
     * @return a set of slots
     */
    public static Set<Integer> getTipsSlots() {
        return TIPS_SLOTS;
    }

    private static HashMap<String, String> initBlockConversion() {
        HashMap<String, String> conversions = new HashMap<>();
        conversions.put("AMETHYST_CLUSTER", "AMETHYST_SHARD");
        conversions.put("BARRIER", "STONE");
        conversions.put("BEETROOTS", "BEETROOT");
        conversions.put("BIG_DRIPLEAF_STEM", "BIG_DRIPLEAF");
        conversions.put("BLACK_WALL_BANNER", "BLACK_BANNER");
        conversions.put("BLUE_WALL_BANNER", "BLUE_BANNER");
        conversions.put("BROWN_MUSHROOM_BLOCK", "BROWN_MUSHROOM");
        conversions.put("BROWN_WALL_BANNER", "BROWN_BANNER");
        conversions.put("BUDDING_AMETHYST", "AMETHYST_BLOCK");
        conversions.put("CAKE", "LEVER");
        conversions.put("CARROTS", "CARROT");
        conversions.put("CAVE_VINES", "GLOW_BERRIES");
        conversions.put("CAVE_VINES_PLANT", "GLOW_BERRIES");
        conversions.put("COBWEB", "STRING");
        conversions.put("COCOA", "COCOA_BEANS");
        conversions.put("CYAN_WALL_BANNER", "CYAN_BANNER");
        conversions.put("DIRT_PATH", "DIRT");
        conversions.put("FARMLAND", "DIRT");
        conversions.put("SHORT_GRASS", "WHEAT_SEEDS");
        conversions.put("GRASS_BLOCK", "DIRT");
        conversions.put("GRAY_WALL_BANNER", "GRAY_BANNER");
        conversions.put("GREEN_WALL_BANNER", "GREEN_BANNER");
        conversions.put("LARGE_AMETHYST_BUD", "AMETHYST_SHARD");
        conversions.put("LAVA_CAULDRON", "CAULDRON");
        conversions.put("LIGHT", "TORCH");
        conversions.put("LIGHT_BLUE_WALL_BANNER", "LIGHT_BLUE_BANNER");
        conversions.put("LIGHT_GRAY_WALL_BANNER", "LIGHT_GRAY_BANNER");
        conversions.put("LIME_WALL_BANNER", "LIME_BANNER");
        conversions.put("MAGENTA_WALL_BANNER", "MAGENTA_BANNER");
        conversions.put("MEDIUM_AMETHYST_BUD", "AMETHYST_SHARD");
        conversions.put("MELON_STEM", "MELON_SEEDS");
        conversions.put("MYCELIUM", "DIRT");
        conversions.put("ORANGE_WALL_BANNER", "ORANGE_BANNER");
        conversions.put("PINK_WALL_BANNER", "PINK_BANNER");
        conversions.put("PITCHER_CROP", "PITCHER_PLANT");
        conversions.put("POTATOES", "POTATO");
        conversions.put("POWDER_SNOW", "SNOW_BLOCK");
        conversions.put("POWDER_SNOW_CAULDRON", "CAULDRON");
        conversions.put("PUMPKIN_STEM", "PUMPKIN_SEEDS");
        conversions.put("PURPLE_WALL_BANNER", "PURPLE_BANNER");
        conversions.put("REDSTONE_WALL_TORCH", "REDSTONE_TORCH");
        conversions.put("REDSTONE_WIRE", "REDSTONE");
        conversions.put("RED_MUSHROOM_BLOCK", "RED_MUSHROOM");
        conversions.put("RED_WALL_BANNER", "RED_BANNER");
        conversions.put("SCULK_VEIN", "SCULK");
        conversions.put("SMALL_AMETHYST_BUD", "AMETHYST_SHARD");
        conversions.put("SNOW", "SNOWBALL");
        conversions.put("STONE", "COBBLESTONE");
        conversions.put("TALL_GRASS", "WHEAT_SEEDS");
        conversions.put("TALL_SEAGRASS", "SEAGRASS");
        conversions.put("TORCHFLOWER_CROP", "TORCHFLOWER");
        conversions.put("TRIPWIRE", "STRING");
        conversions.put("TWISTING_VINES_PLANT", "TWISTING_VINES");
        conversions.put("WALL_TORCH", "TORCH");
        conversions.put("WATER_CAULDRON", "CAULDRON");
        conversions.put("WEEPING_VINES_PLANT", "WEEPING_VINES");
        conversions.put("WHEAT", "WHEAT_SEEDS");
        conversions.put("WHITE_WALL_BANNER", "WHITE_BANNER");
        conversions.put("YELLOW_WALL_BANNER", "YELLOW_BANNER");
        // add all potted plants
        for (Material pot : Tag.FLOWER_POTS.getValues()) {
            conversions.put(pot.toString(), pot.toString().replace("POTTED_", ""));
        }
        // all wall signs
        for (Material sign : Tag.WALL_SIGNS.getValues()) {
            conversions.put(sign.toString(), sign.toString().replace("_WALL", ""));
        }
        for (Material sign : Tag.WALL_HANGING_SIGNS.getValues()) {
            conversions.put(sign.toString(), sign.toString().replace("_WALL", ""));
        }
        return conversions;
    }

    /**
     * Gets a map of locations and TARDIS seed data
     *
     * @return a map of locations and TARDIS seed data
     */
    public HashMap<Location, TARDISBuildData> getTrackTARDISSeed() {
        return trackTARDISSeed;
    }

    /**
     * Gets a map of room names and block counts
     *
     * @return a map of room names and block counts
     */
    public HashMap<String, HashMap<String, Integer>> getRoomBlockCounts() {
        return roomBlockCounts;
    }

    /**
     * Gets a map of Material and TARDIS seed names
     *
     * @return a map of Material and TARDIS seed names
     */
    public HashMap<Material, String> getSeeds() {
        return seeds;
    }

    /**
     * Sets a map of Material and TARDIS seed names
     *
     * @param t_seeds a Map of Material keys and console name values
     */
    public void setSeeds(HashMap<Material, String> t_seeds) {
        seeds = t_seeds;
    }

    /**
     * Gets a map of Material names where the key is a block that needs to be
     * changed to the item in the map value. This is needed when condensing
     * blocks for rooms.
     *
     * @return a map of Material names for conversion
     */
    public Map<String, String> getBlockConversion() {
        return BLOCK_CONVERSION;
    }

    /**
     * Get a list of blocks that will be ignored by the rooms_require_blocks
     * option
     *
     * @return a set of block names
     */
    public Set<String> getIgnoreBlocks() {
        return IGNORE_BLOCKS;
    }

    /**
     * Gets a map of player UUIDs and the amount of room growing progress.
     *
     * @return a map
     */
    public HashMap<UUID, Integer> getRoomProgress() {
        return roomProgress;
    }
}
