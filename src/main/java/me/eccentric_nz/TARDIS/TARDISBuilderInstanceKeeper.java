/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * Keeps track of various building related lookups. these include: Room block counts, Room seed blocks and Stained Glass
 * block colour equivalents for regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<>();
    private final TARDISStainedGlassLookup stainedGlassLookup = new TARDISStainedGlassLookup();
    private HashMap<Material, String> seeds;
    private static final HashMap<String, String> BLOCK_CONVERSION = new HashMap<>();
    private static final List<String> IGNORE_BLOCKS = Arrays.asList("AIR", "BEDROCK", "CAKE", "COMMAND", "GOLD_ORE", "MUSHROOM_STEM", "ICE", "LAVA", "SPAWNER", "INFESTED_CHISELED_STONE_BRICKS", "INFESTED_COBBLESTONE", "INFESTED_CRACKED_STONE_BRICKS", "INFESTED_MOSSY_STONE_BRICKS", "INFESTED_STONE", "INFESTED_STONE_BRICKS", "PISTON_HEAD", "SPONGE", "WATER");
    private static final List<Material> PRECIOUS = new ArrayList<>();
    private static final HashMap<String, EntityType> TWA_HEADS = new HashMap<>();

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
        BLOCK_CONVERSION.put("GRASS", "SEEDS");
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
        BLOCK_CONVERSION.put("TALL_GRASS", "SEEDS");
        BLOCK_CONVERSION.put("OAK_WALL_SIGN", "OAK_SIGN");
        BLOCK_CONVERSION.put("DARK_OAK_WALL_SIGN", "DARK_OAK_SIGN");
        BLOCK_CONVERSION.put("SPRUCE_WALL_SIGN", "SPRUCE_SIGN");
        BLOCK_CONVERSION.put("BIRCH_WALL_SIGN", "BIRCH_SIGN");
        BLOCK_CONVERSION.put("JUNGLE_WALL_SIGN", "JUNGLE_SIGN");
        BLOCK_CONVERSION.put("ACACIA_WALL_SIGN", "ACACIA_SIGN");
        BLOCK_CONVERSION.put("WHEAT", "WHEAT_SEEDS");
        PRECIOUS.add(Material.BEACON);
        PRECIOUS.add(Material.DIAMOND_BLOCK);
        PRECIOUS.add(Material.EMERALD_BLOCK);
        PRECIOUS.add(Material.GOLD_BLOCK);
        PRECIOUS.add(Material.IRON_BLOCK);
        PRECIOUS.add(Material.REDSTONE_BLOCK);
        PRECIOUS.add(Material.BEDROCK);
        TWA_HEADS.put("Cyberman Head", EntityType.AREA_EFFECT_CLOUD);
        TWA_HEADS.put("Empty Child Head", EntityType.ARMOR_STAND);
        TWA_HEADS.put("Ice Warrior Head", EntityType.ARROW);
        TWA_HEADS.put("Silurian Head", EntityType.BOAT);
        TWA_HEADS.put("Sontaran Head", EntityType.FIREWORK);
        TWA_HEADS.put("Strax Head", EntityType.EGG);
        TWA_HEADS.put("Vashta Nerada Head", EntityType.ENDER_CRYSTAL);
        TWA_HEADS.put("Zygon Head", EntityType.FISHING_HOOK);
    }

    public HashMap<String, HashMap<String, Integer>> getRoomBlockCounts() {
        return roomBlockCounts;
    }

    public TARDISStainedGlassLookup getStainedGlassLookup() {
        return stainedGlassLookup;
    }

    public HashMap<Material, String> getSeeds() {
        return seeds;
    }

    public void setSeeds(HashMap<Material, String> t_seeds) {
        seeds = t_seeds;
    }

    public HashMap<String, String> getBlockConversion() {
        return BLOCK_CONVERSION;
    }

    public List<String> getIgnoreBlocks() {
        return IGNORE_BLOCKS;
    }

    public static List<Material> getPrecious() {
        return PRECIOUS;
    }

    public HashMap<String, EntityType> getTWA_Heads() {
        return TWA_HEADS;
    }
}
