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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;

/**
 * One of the key features of a TARDIS is that the interior exists in a
 * dimension different from the exterior. The main application of this concept
 * is that they are bigger on the inside than the outside.
 *
 * @author eccentric_nz
 */
public class TARDISConstants {

    // GUI materials
    public static final List<Material> GUI_IDS = Arrays.asList(Material.STONE, Material.GRASS, Material.WOOD, Material.BEDROCK, Material.SAND, Material.GRAVEL, Material.GOLD_ORE, Material.IRON_ORE, Material.COAL_ORE, Material.LOG, Material.LEAVES, Material.SPONGE, Material.GLASS, Material.LAPIS_ORE, Material.LAPIS_BLOCK, Material.DISPENSER, Material.SANDSTONE, Material.PISTON_STICKY_BASE, Material.WOOL, Material.GOLD_BLOCK, Material.IRON_BLOCK, Material.BRICK, Material.TNT, Material.BOOKSHELF, Material.MOSSY_COBBLESTONE, Material.OBSIDIAN, Material.MOB_SPAWNER, Material.DIAMOND_ORE, Material.DIAMOND_BLOCK, Material.WORKBENCH, Material.FURNACE, Material.REDSTONE_ORE, Material.ICE, Material.SNOW_BLOCK, Material.CACTUS, Material.CLAY, Material.JUKEBOX, Material.PUMPKIN, Material.NETHERRACK, Material.SOUL_SAND, Material.GLOWSTONE, Material.SMOOTH_BRICK, Material.HUGE_MUSHROOM_1, Material.HUGE_MUSHROOM_2, Material.MELON, Material.MYCEL, Material.NETHER_BRICK, Material.CAULDRON, Material.ENDER_STONE, Material.REDSTONE_LAMP_OFF, Material.EMERALD_ORE, Material.EMERALD_BLOCK, Material.QUARTZ_ORE, Material.QUARTZ_BLOCK);
    // chameleon blocks
    public static final List<Integer> CHAMELEON_BLOCKS_VALID = Arrays.asList(1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 35, 41, 42, 43, 45, 46, 47, 48, 49, 56, 57, 58, 73, 74, 79, 80, 82, 84, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 124, 129, 133, 155, 159, 161, 162, 170, 172, 173, 174);
    public static final List<Integer> CHAMELEON_BLOCKS_BAD = Arrays.asList(6, 8, 9, 10, 11, 23, 26, 29, 33, 34, 50, 51, 52, 54, 55, 59, 61, 62, 63, 64, 65, 68, 70, 71, 72, 75, 76, 77, 83, 85, 90, 92, 93, 94, 101, 107, 111, 115, 116, 117, 118, 119, 122, 127, 130, 131, 132);
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_ARR = {2, 12, 13, 44, 53, 60, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156, 163, 164};
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_TO_ARR = {3, 24, 1, 43, 5, 3, 4, 80, 24, 5, 1, 20, 103, 86, 18, 45, 1, 112, 112, 121, 5, 5, 24, 5, 5, 5, 155, 5, 5};
    public static final List<Integer> CHAMELEON_BLOCKS_CHANGE = Arrays.asList(2, 12, 13, 44, 53, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156, 163, 164);
    public static final HashMap<Integer, Integer> CHAMELEON_BLOCKS_CHANGE_HASH = toMap(CHAMELEON_BLOCKS_CHANGE_ARR, CHAMELEON_BLOCKS_CHANGE_TO_ARR);
    public static final List<Integer> CHAMELEON_BLOCKS_NEXT = Arrays.asList(0, 27, 28, 30, 31, 32, 37, 38, 39, 40, 66, 151);
    public static final List<Integer> CHAMELEON_BLOCKS_PRECIOUS = Arrays.asList(41, 42, 46, 57, 133, 152, 173);
    public static final List<String> VOWELS = Arrays.asList("A", "E", "I", "O", "U");
    public static final List<EntityType> ENTITY_TYPES = Arrays.asList(EntityType.BAT, EntityType.BLAZE, EntityType.CAVE_SPIDER, EntityType.CHICKEN, EntityType.COW, EntityType.CREEPER, EntityType.ENDER_DRAGON, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.GIANT, EntityType.GHAST, EntityType.GUARDIAN, EntityType.HORSE, EntityType.IRON_GOLEM, EntityType.MAGMA_CUBE, EntityType.MUSHROOM_COW, EntityType.OCELOT, EntityType.PIG, EntityType.PIG_ZOMBIE, EntityType.PLAYER, EntityType.RABBIT, EntityType.SHEEP, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SNOWMAN, EntityType.SPIDER, EntityType.SQUID, EntityType.VILLAGER, EntityType.WITCH, EntityType.WITHER, EntityType.WOLF, EntityType.ZOMBIE);
    public static final List<EntityType> MONSTER_TYPES = Arrays.asList(EntityType.CAVE_SPIDER, EntityType.CREEPER, EntityType.ENDERMAN, EntityType.ENDERMITE, EntityType.SHULKER, EntityType.SILVERFISH, EntityType.SKELETON, EntityType.SLIME, EntityType.SPIDER, EntityType.SQUID, EntityType.WITCH, EntityType.ZOMBIE);
    public static final List<Biome> NO_RAIN = Arrays.asList(Biome.DESERT, Biome.DESERT_HILLS, Biome.MUTATED_DESERT, Biome.HELL, Biome.SAVANNA, Biome.SAVANNA_ROCK, Biome.MESA, Biome.MESA_ROCK, Biome.MESA_CLEAR_ROCK, Biome.MUTATED_SAVANNA, Biome.MUTATED_SAVANNA_ROCK, Biome.MESA, Biome.MUTATED_MESA, Biome.MUTATED_MESA_ROCK, Biome.MUTATED_MESA_CLEAR_ROCK, Biome.FROZEN_OCEAN, Biome.FROZEN_RIVER, Biome.ICE_FLATS, Biome.ICE_MOUNTAINS, Biome.MUTATED_ICE_FLATS, Biome.COLD_BEACH, Biome.TAIGA_COLD, Biome.TAIGA_COLD_HILLS, Biome.MUTATED_TAIGA_COLD);
    public static final List<Material> GOOD_MATERIALS = Arrays.asList(Material.AIR, Material.BROWN_MUSHROOM, Material.DEAD_BUSH, Material.DOUBLE_PLANT, Material.LONG_GRASS, Material.NETHER_WARTS, Material.RED_MUSHROOM, Material.RED_ROSE, Material.SAPLING, Material.SNOW, Material.YELLOW_FLOWER);
    public static final List<Material> GOOD_WATER = Arrays.asList(Material.AIR, Material.WATER, Material.STATIONARY_WATER);

    /**
     * Creates a HashMap from two arrays. The resulting map is used by the
     * chameleon circuit to change unsuitable blocks into more aesthetically
     * pleasing or robust ones i.e. GRASS -> DIRT, SAND -> SANDSTONE
     *
     * @param keys an array of block types to change
     * @param values an array of block types to change to
     * @return the combined arrays as a HashMap
     */
    public static HashMap<Integer, Integer> toMap(Integer[] keys, Integer[] values) {
        int keysSize = (keys != null) ? keys.length : 0;
        int valuesSize = (values != null) ? values.length : 0;
        if (keysSize == 0 && valuesSize == 0) {
            return new HashMap<Integer, Integer>();
        }
        if (keysSize != valuesSize) {
            throw new IllegalArgumentException("The number of keys doesn't match the number of values.");
        }
        HashMap<Integer, Integer> map = new HashMap<Integer, Integer>();
        for (int i = 0; i < keysSize; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }
}
