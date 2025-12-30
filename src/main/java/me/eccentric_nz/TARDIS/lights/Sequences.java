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
package me.eccentric_nz.TARDIS.lights;

import org.bukkit.Material;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class Sequences {

    public static final List<Material> LIGHT_EMITTING = List.of(
            Material.OCHRE_FROGLIGHT,
            Material.PEARLESCENT_FROGLIGHT,
            Material.VERDANT_FROGLIGHT,
            Material.JACK_O_LANTERN,
            Material.REDSTONE_LAMP,
            Material.SEA_LANTERN,
            Material.SHROOMLIGHT,
            Material.COPPER_BULB,
            Material.EXPOSED_COPPER_BULB,
            Material.WEATHERED_COPPER_BULB,
            Material.OXIDIZED_COPPER_BULB
    );
    public static final List<List<Material>> PRESETS = List.of(
            List.of(Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL),
            List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL),
            List.of(Material.RED_WOOL, Material.ORANGE_WOOL, Material.YELLOW_WOOL, Material.GREEN_WOOL, Material.BLUE_WOOL, Material.PURPLE_WOOL, Material.PINK_WOOL, Material.BLACK_WOOL, Material.BLACK_WOOL)
    );
    public static final List<List<Integer>> DELAYS = List.of(
            List.of(20, 20, 20, 20, 20, 20, 20, 20, 20),
            List.of(40, 40, 40, 40, 40, 40, 40, 40, 40),
            List.of(30, 20, 30, 20, 30, 20, 30, 20, 30)
    );

    public static final List<List<Integer>> LEVELS = List.of(
            List.of(5, 5, 5, 15, 15, 15, 10, 10, 10),
            List.of(15, 15, 15, 15, 15, 15, 15, 15, 15),
            List.of(15, 15, 15, 15, 15, 15, 15, 15, 15)
    );

    public static final HashMap<UUID, String> CONVERTERS = new HashMap<>();
}
