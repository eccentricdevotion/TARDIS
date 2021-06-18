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
package me.eccentric_nz.TARDIS;

import com.google.common.collect.Sets;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Keeps track of various building related lookups. these include: Room block counts, Room seed blocks and Stained Glass
 * block colour equivalents for regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private static final HashMap<String, String> BLOCK_CONVERSION = new HashMap<>();
    private static final Set<String> IGNORE_BLOCKS = Sets.newHashSet("AIR", "CAVE_AIR", "VOID_AIR", "BEDROCK", "CAKE", "COMMAND_BLOCK", "REPEATING_COMMAND_BLOCK", "CHAIN_COMMAND_BLOCK", "GOLD_ORE", "MUSHROOM_STEM", "ICE", "LAVA", "SPAWNER", "INFESTED_CHISELED_STONE_BRICKS", "INFESTED_COBBLESTONE", "INFESTED_CRACKED_STONE_BRICKS", "INFESTED_MOSSY_STONE_BRICKS", "INFESTED_STONE", "INFESTED_STONE_BRICKS", "PISTON_HEAD", "SPONGE", "WATER");
    private static final Set<Material> PRECIOUS = new HashSet<>();

    static {
        BLOCK_CONVERSION.put("ACACIA_LEAVES", "ACACIA_SAPLING");
        BLOCK_CONVERSION.put("BIRCH_LEAVES", "BIRCH_SAPLING");
        BLOCK_CONVERSION.put("BROWN_MUSHROOM_BLOCK", "BROWN_MUSHROOM");
        BLOCK_CONVERSION.put("CAKE", "LEVER");
        BLOCK_CONVERSION.put("CARROTS", "CARROT");
        BLOCK_CONVERSION.put("COBWEB", "STRING");
        BLOCK_CONVERSION.put("COCOA", "COCOA_BEANS");
        BLOCK_CONVERSION.put("DARK_OAK_LEAVES", "DARK_OAK_SAPLING");
        BLOCK_CONVERSION.put("FARMLAND", "DIRT");
        BLOCK_CONVERSION.put("GRASS_BLOCK", "DIRT");
        BLOCK_CONVERSION.put("GRASS", "WHEAT_SEEDS");
        BLOCK_CONVERSION.put("IRON_DOOR", "IRON_DOOR");
        BLOCK_CONVERSION.put("JUNGLE_LEAVES", "JUNGLE_SAPLING");
        BLOCK_CONVERSION.put("MELON_STEM", "MELON_SEEDS");
        BLOCK_CONVERSION.put("MYCELIUM", "DIRT");
        BLOCK_CONVERSION.put("OAK_LEAVES", "OAK_SAPLING");
        BLOCK_CONVERSION.put("POTATOES", "POTATO");
        BLOCK_CONVERSION.put("PUMPKIN_STEM", "PUMPKIN_SEEDS");
        BLOCK_CONVERSION.put("REDSTONE_WALL_TORCH", "REDSTONE_TORCH");
        BLOCK_CONVERSION.put("REDSTONE_WIRE", "REDSTONE");
        BLOCK_CONVERSION.put("RED_MUSHROOM_BLOCK", "RED_MUSHROOM");
        BLOCK_CONVERSION.put("SNOW", "SNOWBALL");
        BLOCK_CONVERSION.put("SPRUCE_LEAVES", "SPRUCE_SAPLING");
        BLOCK_CONVERSION.put("STONE", "COBBLESTONE");
        BLOCK_CONVERSION.put("TALL_GRASS", "WHEAT_SEEDS");
        BLOCK_CONVERSION.put("OAK_WALL_SIGN", "OAK_SIGN");
        BLOCK_CONVERSION.put("DARK_OAK_WALL_SIGN", "DARK_OAK_SIGN");
        BLOCK_CONVERSION.put("SPRUCE_WALL_SIGN", "SPRUCE_SIGN");
        BLOCK_CONVERSION.put("BIRCH_WALL_SIGN", "BIRCH_SIGN");
        BLOCK_CONVERSION.put("JUNGLE_WALL_SIGN", "JUNGLE_SIGN");
        BLOCK_CONVERSION.put("ACACIA_WALL_SIGN", "ACACIA_SIGN");
        BLOCK_CONVERSION.put("WHEAT", "WHEAT_SEEDS");
        // potted plants
        BLOCK_CONVERSION.put("POTTED_ACACIA_SAPLING", "ACACIA_SAPLING");
        BLOCK_CONVERSION.put("POTTED_ALLIUM", "ALLIUM");
        BLOCK_CONVERSION.put("POTTED_AZURE_BLUET", "AZURE_BLUET");
        BLOCK_CONVERSION.put("POTTED_BAMBOO", "BAMBOO");
        BLOCK_CONVERSION.put("POTTED_BIRCH_SAPLING", "BIRCH_SAPLING");
        BLOCK_CONVERSION.put("POTTED_BLUE_ORCHID", "BLUE_ORCHID");
        BLOCK_CONVERSION.put("POTTED_BROWN_MUSHROOM", "BROWN_MUSHROOM");
        BLOCK_CONVERSION.put("POTTED_CACTUS", "CACTUS");
        BLOCK_CONVERSION.put("POTTED_CORNFLOWER", "CORNFLOWER");
        BLOCK_CONVERSION.put("POTTED_DANDELION", "DANDELION");
        BLOCK_CONVERSION.put("POTTED_DARK_OAK_SAPLING", "DARK_OAK_SAPLING");
        BLOCK_CONVERSION.put("POTTED_DEAD_BUSH", "DEAD_BUSH");
        BLOCK_CONVERSION.put("POTTED_FERN", "FERN");
        BLOCK_CONVERSION.put("POTTED_JUNGLE_SAPLING", "JUNGLE_SAPLING");
        BLOCK_CONVERSION.put("POTTED_LILY_OF_THE_VALLEY", "LILY_OF_THE_VALLEY");
        BLOCK_CONVERSION.put("POTTED_OAK_SAPLING", "OAK_SAPLING");
        BLOCK_CONVERSION.put("POTTED_ORANGE_TULIP", "ORANGE_TULIP");
        BLOCK_CONVERSION.put("POTTED_OXEYE_DAISY", "OXEYE_DAISY");
        BLOCK_CONVERSION.put("POTTED_PINK_TULIP", "PINK_TULIP");
        BLOCK_CONVERSION.put("POTTED_POPPY", "POPPY");
        BLOCK_CONVERSION.put("POTTED_RED_MUSHROOM", "RED_MUSHROOM");
        BLOCK_CONVERSION.put("POTTED_RED_TULIP", "RED_TULIP");
        BLOCK_CONVERSION.put("POTTED_SPRUCE_SAPLING", "SPRUCE_SAPLING");
        BLOCK_CONVERSION.put("POTTED_WHITE_TULIP", "WHITE_TULIP");
        BLOCK_CONVERSION.put("POTTED_WITHER_ROSE", "WITHER_ROSE");
        // precious blocks
        PRECIOUS.add(Material.BEACON);
        PRECIOUS.add(Material.BEDROCK);
        PRECIOUS.add(Material.CONDUIT);
        PRECIOUS.add(Material.DIAMOND_BLOCK);
        PRECIOUS.add(Material.EMERALD_BLOCK);
        PRECIOUS.add(Material.GOLD_BLOCK);
        PRECIOUS.add(Material.IRON_BLOCK);
        PRECIOUS.add(Material.NETHERITE_BLOCK);
        PRECIOUS.add(Material.REDSTONE_BLOCK);
    }

    private final HashMap<Location, TARDISBuildData> trackTARDISSeed = new HashMap<>();
    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<>();
    private final TARDISStainedGlassLookup stainedGlassLookup = new TARDISStainedGlassLookup();
    private final HashMap<UUID, Integer> roomProgress = new HashMap<>();
    private HashMap<Material, String> seeds;

    /**
     * Gets a list of precious blocks to protect
     *
     * @return a list of precious blocks
     */
    public static Set<Material> getPrecious() {
        return PRECIOUS;
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
     * Gets a lookup utility to convert blocks to their stained glass equivalent
     *
     * @return the TARDIS Stained Glass Lookup class
     */
    public TARDISStainedGlassLookup getStainedGlassLookup() {
        return stainedGlassLookup;
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
     */
    public void setSeeds(HashMap<Material, String> t_seeds) {
        seeds = t_seeds;
    }

    /**
     * Gets a map of Material names where the key is a block that needs to be changed to the item in the map value.
     * This is needed when condensing blocks for rooms.
     *
     * @return a map of Material names for conversion
     */
    public HashMap<String, String> getBlockConversion() {
        return BLOCK_CONVERSION;
    }

    /**
     * Get a list of blocks that will be ignored by the rooms_require_blocks option
     *
     * @return a list of block names
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
