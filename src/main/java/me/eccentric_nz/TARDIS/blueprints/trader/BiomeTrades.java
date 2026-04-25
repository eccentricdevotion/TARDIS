/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.blueprints.trader;

import me.eccentric_nz.TARDIS.enumeration.Room;
import org.bukkit.block.Biome;

import java.util.HashMap;
import java.util.List;

public class BiomeTrades {

    /**
     * Associate biomes with room types.
     */
    public static HashMap<Biome, List<Room>> CHOICES = new HashMap<>() {{
        put(Biome.BAMBOO_JUNGLE, List.of(Room.BAMBOO, Room.BAKER, Room.SURGERY));
        put(Biome.BEACH, List.of(Room.NAUTILUS, Room.KITCHEN));
        put(Biome.CHERRY_GROVE, List.of(Room.GREENHOUSE, Room.MAZE));
        put(Biome.DARK_FOREST, List.of(Room.EYE));
        put(Biome.DESERT, List.of(Room.IISTUBIL, Room.GALLERY, Room.WORKSHOP));
        put(Biome.DRIPSTONE_CAVES, List.of(Room.GEODE, Room.SMELTER));
        put(Biome.FLOWER_FOREST, List.of(Room.GARDEN, Room.LAZARUS));
        put(Biome.FOREST, List.of(Room.ARBORETUM, Room.GOLEM));
        put(Biome.JUNGLE, List.of(Room.BIRDCAGE, Room.ARCHITECTURAL, Room.STAIRCASE));
        put(Biome.MANGROVE_SWAMP, List.of(Room.MANGROVE, Room.HARMONY));
        put(Biome.MEADOW, List.of(Room.PEN, Room.PASSAGE));
        put(Biome.MUSHROOM_FIELDS, List.of(Room.MUSHROOM, Room.LAUNDRY));
        put(Biome.NETHER_WASTES, List.of(Room.NETHER, Room.LAVA));
        put(Biome.PALE_GARDEN, List.of(Room.VILLAGE, Room.RAIL));
        put(Biome.PLAINS, List.of(Room.FARM, Room.LIBRARY));
        put(Biome.RIVER, List.of(Room.AQUARIUM, Room.POOL, Room.SHELL));
        put(Biome.SAVANNA, List.of(Room.STABLE, Room.BEDROOM, Room.TRENZALORE));
        put(Biome.SNOWY_PLAINS, List.of(Room.IGLOO, Room.EMPTY, Room.WOOD));
        put(Biome.SOUL_SAND_VALLEY, List.of(Room.HAPPY, Room.OBSERVATORY));
        put(Biome.STONY_PEAKS, List.of(Room.ALLAY, Room.RENDERER));
        put(Biome.SUNFLOWER_PLAINS, List.of(Room.APIARY, Room.CLOISTER, Room.WARDROBE));
        put(Biome.TAIGA, List.of(Room.HUTCH, Room.GAMES));
        put(Biome.THE_END, List.of(Room.ANTIGRAVITY, Room.GRAVITY));
        put(Biome.WINDSWEPT_SAVANNA, List.of(Room.STALL, Room.CHEMISTRY, Room.VAULT));
    }};
}
