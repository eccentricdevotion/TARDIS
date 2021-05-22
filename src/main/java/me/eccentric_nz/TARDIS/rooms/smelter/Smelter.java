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
package me.eccentric_nz.tardis.rooms.smelter;

import org.bukkit.Material;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.List;

/**
 * @author eccentric_nz
 */
public class Smelter {

	private static final List<Vector> FUEL_VECTORS = new ArrayList<>();
	private static final List<Vector> ORE_VECTORS = new ArrayList<>();

	static {
		FUEL_VECTORS.add(new Vector(-6.0, 3.0, 4.0));
		FUEL_VECTORS.add(new Vector(-6.0, 3.0, 3.0));
		FUEL_VECTORS.add(new Vector(-6.0, 3.0, -3.0));
		FUEL_VECTORS.add(new Vector(-6.0, 3.0, -4.0));
		FUEL_VECTORS.add(new Vector(-4.0, 3.0, 6.0));
		FUEL_VECTORS.add(new Vector(-4.0, 3.0, -6.0));
		FUEL_VECTORS.add(new Vector(-3.0, 3.0, 6.0));
		FUEL_VECTORS.add(new Vector(-3.0, 3.0, -6.0));
		FUEL_VECTORS.add(new Vector(3.0, 3.0, 6.0));
		FUEL_VECTORS.add(new Vector(3.0, 3.0, -6.0));
		FUEL_VECTORS.add(new Vector(4.0, 3.0, 6.0));
		FUEL_VECTORS.add(new Vector(4.0, 3.0, -6.0));
		FUEL_VECTORS.add(new Vector(6.0, 3.0, 4.0));
		FUEL_VECTORS.add(new Vector(6.0, 3.0, 3.0));
		FUEL_VECTORS.add(new Vector(6.0, 3.0, -3.0));
		FUEL_VECTORS.add(new Vector(6.0, 3.0, -4.0));
		ORE_VECTORS.add(new Vector(-5.0, 4.0, 4.0));
		ORE_VECTORS.add(new Vector(-5.0, 4.0, 3.0));
		ORE_VECTORS.add(new Vector(-5.0, 4.0, -3.0));
		ORE_VECTORS.add(new Vector(-5.0, 4.0, -4.0));
		ORE_VECTORS.add(new Vector(-4.0, 4.0, 5.0));
		ORE_VECTORS.add(new Vector(-4.0, 4.0, -5.0));
		ORE_VECTORS.add(new Vector(-3.0, 4.0, 5.0));
		ORE_VECTORS.add(new Vector(-3.0, 4.0, -5.0));
		ORE_VECTORS.add(new Vector(3.0, 4.0, 5.0));
		ORE_VECTORS.add(new Vector(3.0, 4.0, -5.0));
		ORE_VECTORS.add(new Vector(4.0, 4.0, 5.0));
		ORE_VECTORS.add(new Vector(4.0, 4.0, -5.0));
		ORE_VECTORS.add(new Vector(5.0, 4.0, 4.0));
		ORE_VECTORS.add(new Vector(5.0, 4.0, 3.0));
		ORE_VECTORS.add(new Vector(5.0, 4.0, -3.0));
		ORE_VECTORS.add(new Vector(5.0, 4.0, -4.0));
	}

	public static boolean isSmeltable(Material material) {
		return switch (material) {
			case ACACIA_WOOD, ACACIA_LOG, BEEF, BIRCH_WOOD, BIRCH_LOG, BLACK_TERRACOTTA, BLUE_TERRACOTTA, BROWN_TERRACOTTA, CACTUS, CHICKEN, CHORUS_FRUIT, CLAY, CLAY_BALL, COBBLESTONE, COD, CYAN_TERRACOTTA, DARK_OAK_WOOD, DARK_OAK_LOG, GOLD_ORE, GRAY_TERRACOTTA, GREEN_TERRACOTTA, IRON_ORE, JUNGLE_WOOD, JUNGLE_LOG, KELP, LIGHT_BLUE_TERRACOTTA, LIGHT_GRAY_TERRACOTTA, LIME_TERRACOTTA, MAGENTA_TERRACOTTA, MUTTON, NETHERRACK, OAK_WOOD, OAK_LOG, ORANGE_TERRACOTTA, PINK_TERRACOTTA, PORKCHOP, POTATO, PURPLE_TERRACOTTA, QUARTZ_BLOCK, RABBIT, RED_SANDSTONE, RED_TERRACOTTA, SALMON, SAND, SANDSTONE, SEA_PICKLE, SPONGE, SPRUCE_WOOD, SPRUCE_LOG, STONE, STONE_BRICKS, STRIPPED_ACACIA_LOG, STRIPPED_ACACIA_WOOD, STRIPPED_BIRCH_LOG, STRIPPED_BIRCH_WOOD, STRIPPED_DARK_OAK_LOG, STRIPPED_DARK_OAK_WOOD, STRIPPED_JUNGLE_LOG, STRIPPED_JUNGLE_WOOD, STRIPPED_OAK_LOG, STRIPPED_OAK_WOOD, STRIPPED_SPRUCE_LOG, STRIPPED_SPRUCE_WOOD, WET_SPONGE, WHITE_TERRACOTTA, YELLOW_TERRACOTTA -> true;
			default -> false;
		};
	}

	public static List<Vector> getFuelVectors() {
		return FUEL_VECTORS;
	}

	public static List<Vector> getOreVectors() {
		return ORE_VECTORS;
	}
}
