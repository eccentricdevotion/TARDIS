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
package me.eccentric_nz.TARDIS.enumeration;

import com.google.common.collect.Maps;
import java.util.Map;

/**
 *
 * @author eccentric_nz
 */
public enum BIOME_LOOKUP {

    CACTUS_B("DESERT", "DESERT_HILLS"),
    DIRT_B("EXTREME_HILLS", "EXTREME_HILLS_WITH_TREES"),
    PODZOL_B("REDWOOD_TAIGA", "REDWOOD_TAIGA_HILLS"),
    RED_SAND_B("MESA", "MESA_ROCK"),
    ACACIA_LOG_B("SAVANNA", "MUTATED_SAVANNA"),
    DARK_OAK_LOG_B("ROOFED_FOREST", "MUTATED_ROOFED_FOREST"),
    OAK_LOG_B("FOREST", "FOREST_HILLS"),
    SPRUCE_LOG_B("TAIGA", "TAIGA_HILLS"),
    BIRCH_LOG_B("BIRCH_FOREST", "BIRCH_FOREST_HILLS"),
    JUNGLE_LOG_B("JUNGLE", "JUNGLE_HILLS"),
    SAND_B("BEACHES", "COLD_BEACH"),
    SNOW_BLOCK_B("TAIGA_COLD", "TAIGA_COLD_HILLS"),
    WATER_BUCKET_B("OCEAN", "DEEP_OCEAN"),
    RED_TULIP_B("MUTATED_FOREST", "MUTATED_BIRCH_FOREST_HILLS"),
    SUNFLOWER_B("PLAINS", "MUTATED_PLAINS"),
    ICE_B("ICE_FLATS", "MUTATED_ICE_FLATS"),
    MYCELIUM_B("MUSHROOM_ISLAND", "MUSHROOM_ISLAND_SHORE"),
    LILY_PAD_B("SWAMPLAND", "MUTATED_SWAMPLAND");

    String regular;
    String upper;
    public final static Map<String, BIOME_LOOKUP> BY_REG = Maps.newHashMap();

    private BIOME_LOOKUP(String regular, String upper) {
        this.regular = regular;
        this.upper = upper;
    }

    public String getRegular() {
        return regular;
    }

    public String getUpper() {
        return upper;
    }

    public static BIOME_LOOKUP getBiome(final String data) {
        return BY_REG.get(data);
    }

    static {
        for (BIOME_LOOKUP bm : values()) {
            BY_REG.put(bm.getRegular(), bm);
        }
    }
}
