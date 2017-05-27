/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.chameleon.TARDISStainedGlassLookup;
import org.bukkit.Material;

/**
 * Keeps track of various building related lookups. these include: Room block
 * counts, Room seed blocks and Stained Glass block colour equivalents for
 * regular blocks.
 *
 * @author eccentric_nz
 */
public class TARDISBuilderInstanceKeeper {

    private final HashMap<String, HashMap<String, Integer>> roomBlockCounts = new HashMap<>();
    private final TARDISStainedGlassLookup stainedGlassLookup = new TARDISStainedGlassLookup();
    private HashMap<Material, String> seeds;
    private static final HashMap<String, String> BLOCK_CONVERSION = new HashMap<>();
    private static final List<String> IGNORE_BLOCKS = Arrays.asList(new String[]{"AIR", "BEDROCK", "CAKE_BLOCK", "COMMAND", "GOLD_ORE", "HUGE_MUSHROOM_2", "ICE", "LAVA", "MOB_SPAWNER", "MONSTER_EGGS", "PISTON_EXTENSION", "SPONGE", "STATIONARY_LAVA", "STATIONARY_WATER", "WATER"});
    private static final List<Material> PRECIOUS = new ArrayList<>();

    static {
        BLOCK_CONVERSION.put("BED_BLOCK", "BED");
        BLOCK_CONVERSION.put("BREWING_STAND", "BREWING_STAND_ITEM");
        BLOCK_CONVERSION.put("CAKE_BLOCK", "LEVER");
        BLOCK_CONVERSION.put("CARROT", "CARROT_ITEM");
        BLOCK_CONVERSION.put("CAULDRON", "CAULDRON_ITEM");
        BLOCK_CONVERSION.put("COCOA", "INK_SACK");
        BLOCK_CONVERSION.put("CROPS", "SEEDS");
        BLOCK_CONVERSION.put("DIODE_BLOCK_OFF", "DIODE");
        BLOCK_CONVERSION.put("DIODE_BLOCK_ON", "DIODE");
        BLOCK_CONVERSION.put("DOUBLE_STEP", "STEP");
        BLOCK_CONVERSION.put("FLOWER_POT", "FLOWER_POT_ITEM");
        BLOCK_CONVERSION.put("GRASS", "DIRT");
        BLOCK_CONVERSION.put("HUGE_MUSHROOM_1", "BROWN_MUSHROOM");
        BLOCK_CONVERSION.put("HUGE_MUSHROOM_2", "RED_MUSHROOM");
        BLOCK_CONVERSION.put("IRON_DOOR_BLOCK", "IRON_DOOR");
        BLOCK_CONVERSION.put("LEAVES", "SAPLING");
        BLOCK_CONVERSION.put("LEAVES_2", "SAPLING");
        BLOCK_CONVERSION.put("LONG_GRASS", "SEEDS");
        BLOCK_CONVERSION.put("MELON_STEM", "MELON_SEEDS");
        BLOCK_CONVERSION.put("MYCEL", "DIRT");
        BLOCK_CONVERSION.put("NETHER_WARTS", "NETHER_STALK");
        BLOCK_CONVERSION.put("POTATO", "POTATO_ITEM");
        BLOCK_CONVERSION.put("PUMPKIN_STEM", "PUMPKIN_SEEDS");
        BLOCK_CONVERSION.put("REDSTONE_COMPARATOR_OFF", "REDSTONE_COMPARATOR");
        BLOCK_CONVERSION.put("REDSTONE_COMPARATOR_ON", "REDSTONE_COMPARATOR");
        BLOCK_CONVERSION.put("REDSTONE_LAMP_ON", "REDSTONE_LAMP_OFF");
        BLOCK_CONVERSION.put("REDSTONE_TORCH_OFF", "REDSTONE_TORCH_ON");
        BLOCK_CONVERSION.put("REDSTONE_WIRE", "REDSTONE");
        BLOCK_CONVERSION.put("SIGN_POST", "SIGN");
        BLOCK_CONVERSION.put("SNOW", "SNOW_BALL");
        BLOCK_CONVERSION.put("SOIL", "DIRT");
        BLOCK_CONVERSION.put("STONE", "COBBLESTONE");
        BLOCK_CONVERSION.put("SUGAR_CANE_BLOCK", "SUGAR_CANE");
        BLOCK_CONVERSION.put("WALL_SIGN", "SIGN");
        BLOCK_CONVERSION.put("WEB", "STRING");
        BLOCK_CONVERSION.put("WOODEN_DOOR", "WOOD_DOOR");
        PRECIOUS.add(Material.BEACON);
        PRECIOUS.add(Material.DIAMOND_BLOCK);
        PRECIOUS.add(Material.EMERALD_BLOCK);
        PRECIOUS.add(Material.GOLD_BLOCK);
        PRECIOUS.add(Material.IRON_BLOCK);
        PRECIOUS.add(Material.REDSTONE_BLOCK);
        PRECIOUS.add(Material.BEDROCK);
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
}
