/*
 * Copyright (C) 2020 eccentric_nz
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
package me.eccentric_nz.tardis.enumeration;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * @author eccentric_nz
 */
public enum BiomeLookup {

	CACTUS_B("DESERT", "DESERT_HILLS"), DIRT_B("MOUNTAINS", "MODIFIED_GRAVELLY_MOUNTAINS"), PODZOL_B("GIANT_TREE_TAIGA", "GIANT_TREE_TAIGA_HILLS"), RED_SAND_B("BADLANDS", "BADLANDS_PLATEAU"), ACACIA_LOG_B("SAVANNA", "SHATTERED_SAVANNA"), DARK_OAK_LOG_B("DARK_FOREST", "DARK_FOREST_HILLS"), OAK_LOG_B("FOREST", "WOODED_HILLS"), SPRUCE_LOG_B("TAIGA", "TAIGA_HILLS"), BIRCH_LOG_B("BIRCH_FOREST", "BIRCH_FOREST_HILLS"), JUNGLE_LOG_B("JUNGLE", "JUNGLE_HILLS"), SAND_B("BEACH", "SNOWY_BEACH"), SNOW_BLOCK_B("SNOWY_TAIGA", "SNOWY_TAIGA_HILLS"), WATER_BUCKET_B("OCEAN", "DEEP_OCEAN"), RED_TULIP_B("FLOWER_FOREST", "TALL_BIRCH_HILLS"), SUNFLOWER_B("PLAINS", "SUNFLOWER_PLAINS"), ICE_B("SNOWY_TUNDRA", "ICE_SPIKES"), MYCELIUM_B("MUSHROOM_FIELDS", "MUSHROOM_FIELD_SHORE"), LILY_PAD_B("SWAMP", "SWAMP_HILLS"), BAMBOO_B("BAMBOO_JUNGLE", "BAMBOO_JUNGLE_HILLS");

	public final static Map<String, BiomeLookup> BY_REG = Maps.newHashMap();

	static {
		for (BiomeLookup bm : values()) {
			BY_REG.put(bm.getRegular(), bm);
		}
	}

	String regular;
	String upper;

	BiomeLookup(String regular, String upper) {
		this.regular = regular;
		this.upper = upper;
	}

	public static BiomeLookup getBiome(String data) {
		return BY_REG.get(data);
	}

	public String getRegular() {
		return regular;
	}

	public String getUpper() {
		return upper;
	}
}
