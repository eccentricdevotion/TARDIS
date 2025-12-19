/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.custommodels.keys.ConsolePart;
import me.eccentric_nz.TARDIS.custommodels.keys.ConsoleVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;

import java.util.HashMap;
import java.util.TreeMap;

public class ColourType {

    public static final HashMap<Material, NamespacedKey> LOOKUP = new HashMap<>();
    public static final HashMap<NamespacedKey, String> COLOURS = new HashMap<>();
    public static TreeMap<Material, NamespacedKey> BY_MATERIAL = new TreeMap<>();

    static {
        LOOKUP.put(Material.LIGHT_GRAY_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIGHT_GRAY.getKey());
        LOOKUP.put(Material.GRAY_CONCRETE_POWDER, ConsoleVariant.CONSOLE_GRAY.getKey());
        LOOKUP.put(Material.BLACK_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BLACK.getKey());
        LOOKUP.put(Material.WHITE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_WHITE.getKey());
        LOOKUP.put(Material.RED_CONCRETE_POWDER, ConsoleVariant.CONSOLE_RED.getKey());
        LOOKUP.put(Material.ORANGE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_ORANGE.getKey());
        LOOKUP.put(Material.YELLOW_CONCRETE_POWDER, ConsoleVariant.CONSOLE_YELLOW.getKey());
        LOOKUP.put(Material.LIME_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIME.getKey());
        LOOKUP.put(Material.GREEN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_GREEN.getKey());
        LOOKUP.put(Material.CYAN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_CYAN.getKey());
        LOOKUP.put(Material.LIGHT_BLUE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_LIGHT_BLUE.getKey());
        LOOKUP.put(Material.BLUE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BLUE.getKey());
        LOOKUP.put(Material.PURPLE_CONCRETE_POWDER, ConsoleVariant.CONSOLE_PURPLE.getKey());
        LOOKUP.put(Material.MAGENTA_CONCRETE_POWDER, ConsoleVariant.CONSOLE_MAGENTA.getKey());
        LOOKUP.put(Material.PINK_CONCRETE_POWDER, ConsoleVariant.CONSOLE_PINK.getKey());
        LOOKUP.put(Material.BROWN_CONCRETE_POWDER, ConsoleVariant.CONSOLE_BROWN.getKey());
        BY_MATERIAL.put(Material.LIGHT_GRAY_CONCRETE, ConsoleVariant.CONSOLE_LIGHT_GRAY.getKey());
        BY_MATERIAL.put(Material.GRAY_CONCRETE, ConsoleVariant.CONSOLE_GRAY.getKey());
        BY_MATERIAL.put(Material.BLACK_CONCRETE, ConsoleVariant.CONSOLE_BLACK.getKey());
        BY_MATERIAL.put(Material.WHITE_CONCRETE, ConsoleVariant.CONSOLE_WHITE.getKey());
        BY_MATERIAL.put(Material.RED_CONCRETE, ConsoleVariant.CONSOLE_RED.getKey());
        BY_MATERIAL.put(Material.ORANGE_CONCRETE, ConsoleVariant.CONSOLE_ORANGE.getKey());
        BY_MATERIAL.put(Material.YELLOW_CONCRETE, ConsoleVariant.CONSOLE_YELLOW.getKey());
        BY_MATERIAL.put(Material.LIME_CONCRETE, ConsoleVariant.CONSOLE_LIME.getKey());
        BY_MATERIAL.put(Material.GREEN_CONCRETE, ConsoleVariant.CONSOLE_GREEN.getKey());
        BY_MATERIAL.put(Material.CYAN_CONCRETE, ConsoleVariant.CONSOLE_CYAN.getKey());
        BY_MATERIAL.put(Material.LIGHT_BLUE_CONCRETE, ConsoleVariant.CONSOLE_LIGHT_BLUE.getKey());
        BY_MATERIAL.put(Material.BLUE_CONCRETE, ConsoleVariant.CONSOLE_BLUE.getKey());
        BY_MATERIAL.put(Material.PURPLE_CONCRETE, ConsoleVariant.CONSOLE_PURPLE.getKey());
        BY_MATERIAL.put(Material.MAGENTA_CONCRETE, ConsoleVariant.CONSOLE_MAGENTA.getKey());
        BY_MATERIAL.put(Material.PINK_CONCRETE, ConsoleVariant.CONSOLE_PINK.getKey());
        BY_MATERIAL.put(Material.BROWN_CONCRETE, ConsoleVariant.CONSOLE_BROWN.getKey());
        BY_MATERIAL.put(Material.WAXED_OXIDIZED_COPPER, ConsoleVariant.CONSOLE_RUSTIC.getKey());
        COLOURS.put(ConsolePart.CONSOLE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_RUSTIC.getKey(), "RUSTIC");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_DIVISION_RUSTIC.getKey(), "RUSTIC");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIGHT_GRAY.getKey(), "LIGHT_GRAY");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_GRAY.getKey(), "GRAY");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BLACK.getKey(), "BLACK");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_WHITE.getKey(), "WHITE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_RED.getKey(), "RED");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_ORANGE.getKey(), "ORANGE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_YELLOW.getKey(), "YELLOW");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIME.getKey(), "LIME");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_GREEN.getKey(), "GREEN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_CYAN.getKey(), "CYAN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_LIGHT_BLUE.getKey(), "LIGHT_BLUE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BLUE.getKey(), "BLUE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_PURPLE.getKey(), "PURPLE");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_MAGENTA.getKey(), "MAGENTA");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_PINK.getKey(), "PINK");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_BROWN.getKey(), "BROWN");
        COLOURS.put(ConsolePart.CONSOLE_CENTRE_RUSTIC.getKey(), "RUSTIC");
    }
}
