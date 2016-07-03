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

    CACTUS_B0("DESERT", "DESERT_HILLS"),
    DIRT_B0("EXTREME_HILLS", "EXTREME_HILLS_WITH_TREES"),
    DIRT_B2("REDWOOD_TAIGA", "REDWOOD_TAIGA_HILLS"),
    SAND_B1("MESA", "MESA_ROCK"),
    LOG_2_B0("SAVANNA", "MUTATED_SAVANNA"),
    LOG_2_B1("ROOFED_FOREST", "MUTATED_ROOFED_FOREST"),
    LOG_B0("FOREST", "FOREST_HILLS"),
    LOG_B1("TAIGA", "TAIGA_HILLS"),
    LOG_B2("BIRCH_FOREST", "BIRCH_FOREST_HILLS"),
    LOG_B3("JUNGLE", "JUNGLE_HILLS"),
    SAND_B0("BEACHES", "COLD_BEACH"),
    SNOW_BLOCK_B0("TAIGA_COLD", "TAIGA_COLD_HILLS"),
    WATER_BUCKET_B0("OCEAN", "DEEP_OCEAN"),
    RED_ROSE_B4("MUTATED_FOREST", "MUTATED_BIRCH_FOREST_HILLS"),
    DOUBLE_PLANT_B0("PLAINS", "MUTATED_PLAINS"),
    ICE_B0("ICE_FLATS", "MUTATED_ICE_FLATS"),
    MYCEL_B0("MUSHROOM_ISLAND", "MUSHROOM_ISLAND_SHORE"),
    WATER_LILY_B0("SWAMPLAND", "MUTATED_SWAMPLAND");

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
