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
package me.eccentric_nz.TARDIS.particles;

import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;

import java.util.List;

public class ParticleColour {

    public static final List<NamedTextColor> colours = List.of(NamedTextColor.WHITE, NamedTextColor.AQUA, NamedTextColor.BLACK, NamedTextColor.BLUE, NamedTextColor.DARK_AQUA, NamedTextColor.DARK_BLUE, NamedTextColor.DARK_GRAY, NamedTextColor.DARK_GREEN, NamedTextColor.DARK_PURPLE, NamedTextColor.DARK_RED, NamedTextColor.GOLD, NamedTextColor.GRAY, NamedTextColor.GREEN, NamedTextColor.LIGHT_PURPLE, NamedTextColor.RED, NamedTextColor.YELLOW);

    public static NamedTextColor fromColor(Color color) {
        if (color == Color.WHITE) { return NamedTextColor.WHITE; }
        if (color == Color.AQUA) { return NamedTextColor.AQUA; }
        if (color == Color.BLACK) { return NamedTextColor.BLACK; }
        if (color == Color.BLUE) { return NamedTextColor.BLUE; }
        if (color == Color.TEAL) { return NamedTextColor.DARK_AQUA; }
        if (color == Color.NAVY) { return NamedTextColor.DARK_BLUE; }
        if (color == Color.GRAY) { return NamedTextColor.DARK_GRAY; }
        if (color == Color.GREEN) { return NamedTextColor.DARK_GREEN; }
        if (color == Color.PURPLE) { return NamedTextColor.DARK_PURPLE; }
        if (color == Color.MAROON) { return NamedTextColor.DARK_RED;}
        if (color == Color.ORANGE) { return NamedTextColor.GOLD; }
        if (color == Color.SILVER) { return NamedTextColor.GRAY; }
        if (color == Color.LIME) { return NamedTextColor.GREEN; }
        if (color == Color.FUCHSIA) { return NamedTextColor.LIGHT_PURPLE; }
        if (color == Color.RED) { return NamedTextColor.RED; }
        if (color == Color.YELLOW) { return NamedTextColor.YELLOW; }
        return NamedTextColor.WHITE;
    }

    public static Color fromDatabase(String colour) {
        switch (colour) {
            case "White" -> { return Color.WHITE; }
            case "Aqua" -> { return Color.AQUA; }
            case "Black" -> { return Color.BLACK; }
            case "Blue" -> { return Color.BLUE; }
            case "Dark Aqua" -> { return Color.TEAL; }
            case "Dark Blue" -> { return Color.NAVY; }
            case "Dark Gray" -> { return Color.GRAY; }
            case "Dark Green" -> { return Color.GREEN; }
            case "Dark Purple" -> { return Color.PURPLE; }
            case "Dark Red" -> { return Color.MAROON;}
            case "Gold" -> { return Color.ORANGE; }
            case "Gray" -> { return Color.SILVER; }
            case "Green" -> { return Color.LIME; }
            case "Light Purple" -> { return Color.FUCHSIA; }
            case "Red" -> { return Color.RED; }
            case "Yellow" -> { return Color.YELLOW; }
            default -> { return Color.OLIVE; }
        }
    }

    public static NamedTextColor fromComponent(Component str) {
        String clean = ComponentUtils.stripColour(str);
        switch (clean) {
            case "White" -> { return NamedTextColor.WHITE; }
            case "Aqua" -> { return NamedTextColor.AQUA; }
            case "Black" -> { return NamedTextColor.BLACK; }
            case "Blue" -> { return NamedTextColor.BLUE; }
            case "Dark Aqua" -> { return NamedTextColor.DARK_AQUA; }
            case "Dark Blue" -> { return NamedTextColor.DARK_BLUE; }
            case "Dark Gray" -> { return NamedTextColor.DARK_GRAY; }
            case "Dark Green" -> { return NamedTextColor.DARK_GREEN; }
            case "Dark Purple" -> { return NamedTextColor.DARK_PURPLE; }
            case "Dark Red" -> { return NamedTextColor.DARK_RED;}
            case "Gold" -> { return NamedTextColor.GOLD; }
            case "Gray" -> { return NamedTextColor.GRAY; }
            case "Green" -> { return NamedTextColor.GREEN; }
            case "Light Purple" -> { return NamedTextColor.LIGHT_PURPLE; }
            case "Red" -> { return NamedTextColor.RED; }
            case "Yellow" -> { return NamedTextColor.YELLOW; }
            default -> { return NamedTextColor.WHITE; }
        }
    }

    public static String toString(NamedTextColor colour) {
        return TARDISStringUtils.capitalise(colour.toString());
    }
}

