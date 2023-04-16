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
package me.eccentric_nz.TARDIS;

import com.google.common.collect.Sets;
import java.util.*;
import me.eccentric_nz.TARDIS.builders.TARDISBuildData;
import me.eccentric_nz.TARDIS.chameleon.utils.TARDISStainedGlassLookup;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Tag;

/**
 * Keeps track of various building related lookups. these include: Room block
 * counts, Room seed blocks and Stained Glass block colour equivalents for
 * regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private static final Map<String, String> BLOCK_CONVERSION;
    private static final Set<String> IGNORE_BLOCKS = Sets.newHashSet("AIR", "CAVE_AIR", "VOID_AIR", "BEDROCK", "CAKE", "COMMAND_BLOCK", "REPEATING_COMMAND_BLOCK", "CHAIN_COMMAND_BLOCK", "GOLD_ORE", "MUSHROOM_STEM", "ICE", "LAVA", "SPAWNER", "INFESTED_CHISELED_STONE_BRICKS", "INFESTED_COBBLESTONE", "INFESTED_DEEPSLATE", "INFESTED_CRACKED_STONE_BRICKS", "INFESTED_MOSSY_STONE_BRICKS", "INFESTED_STONE", "INFESTED_STONE_BRICKS", "PISTON_HEAD", "SPONGE", "WATER", "JUKEBOX", "NOTE_BLOCK");
    private static final Set<Material> PRECIOUS = new HashSet<>();
    private static final Set<Integer> TIPS_SLOTS = new HashSet<>();

    static {
        BLOCK_CONVERSION = Map.ofEntries(
                Map.entry("AMETHYST_CLUSTER", "AMETHYST_SHARD"),
                Map.entry("BARRIER", "STONE"),
                Map.entry("BEETROOTS", "BEETROOT"),
                Map.entry("BIG_DRIPLEAF_STEM", "BIG_DRIPLEAF"),
                Map.entry("BROWN_MUSHROOM_BLOCK", "BROWN_MUSHROOM"),
                Map.entry("CAKE", "LEVER"),
                Map.entry("CARROTS", "CARROT"),
                Map.entry("CAVE_VINES", "GLOW_BERRIES"),
                Map.entry("CAVE_VINES_PLANT", "GLOW_BERRIES"),
                Map.entry("COBWEB", "STRING"),
                Map.entry("COCOA", "COCOA_BEANS"),
                Map.entry("DIRT_PATH", "DIRT"),
                Map.entry("FARMLAND", "DIRT"),
                Map.entry("GRASS", "WHEAT_SEEDS"),
                Map.entry("GRASS_BLOCK", "DIRT"),
                Map.entry("LARGE_AMETHYST_BUD", "AMETHYST_SHARD"),
                Map.entry("LIGHT", "TORCH"),
                Map.entry("MEDIUM_AMETHYST_BUD", "AMETHYST_SHARD"),
                Map.entry("MELON_STEM", "MELON_SEEDS"),
                Map.entry("MYCELIUM", "DIRT"),
                Map.entry("POTATOES", "POTATO"),
                Map.entry("POWDER_SNOW", "SNOW_BLOCK"),
                Map.entry("PUMPKIN_STEM", "PUMPKIN_SEEDS"),
                Map.entry("REDSTONE_WALL_TORCH", "REDSTONE_TORCH"),
                Map.entry("REDSTONE_WIRE", "REDSTONE"),
                Map.entry("RED_MUSHROOM_BLOCK", "RED_MUSHROOM"),
                Map.entry("SCULK_VEIN", "SCULK"),
                Map.entry("SMALL_AMETHYST_BUD", "AMETHYST_SHARD"),
                Map.entry("SNOW", "SNOWBALL"),
                Map.entry("STONE", "COBBLESTONE"),
                Map.entry("TALL_GRASS", "WHEAT_SEEDS"),
                Map.entry("TORCHFLOWER_CROP", "TORCHFLOWER"),
                Map.entry("TWISTING_VINES_PLANT", "TWISTING_VINES"),
                Map.entry("WALL_TORCH", "TORCH"),
                Map.entry("WEEPING_VINES_PLANT", "WEEPING_VINES"),
                Map.entry("WHEAT", "WHEAT_SEEDS")
        );
        // add all potted plants
        for (Material pot : Tag.FLOWER_POTS.getValues()) {
            BLOCK_CONVERSION.put(pot.toString(), pot.toString().replace("POTTED_", ""));
        }
        // add all leaves
        for (Material leaf : Tag.LEAVES.getValues()) {
            BLOCK_CONVERSION.put(leaf.toString(), leaf.toString().replace("_LEAVES", "_SAPLING"));
        }
        BLOCK_CONVERSION.put("MANGROVE_LEAVES", "MANGROVE_PROPAGULE");
        // all wall signs
        for (Material sign : Tag.WALL_SIGNS.getValues()) {
            BLOCK_CONVERSION.put(sign.toString(), sign.toString().replace("_WALL", ""));
        }
        for (Material sign : Tag.WALL_HANGING_SIGNS.getValues()) {
            BLOCK_CONVERSION.put(sign.toString(), sign.toString().replace("_WALL", ""));
        }
        // precious blocks
        PRECIOUS.add(Material.BEACON);
        PRECIOUS.add(Material.BEDROCK);
        PRECIOUS.add(Material.BUDDING_AMETHYST);
        PRECIOUS.add(Material.CONDUIT);
        PRECIOUS.add(Material.DIAMOND_BLOCK);
        PRECIOUS.add(Material.EMERALD_BLOCK);
        PRECIOUS.add(Material.GOLD_BLOCK);
        PRECIOUS.add(Material.IRON_BLOCK);
        PRECIOUS.add(Material.LIGHT);
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
     * Gets a list of used TIPS slots
     *
     * @return a list of slots
     */
    public static Set<Integer> getTipsSlots() {
        return TIPS_SLOTS;
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
