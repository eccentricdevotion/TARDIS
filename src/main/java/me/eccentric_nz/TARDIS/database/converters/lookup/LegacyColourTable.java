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
package me.eccentric_nz.TARDIS.database.converters.lookup;

import java.util.HashMap;

public class LegacyColourTable {

    public static final HashMap<Integer, String> LOOKUP = new HashMap<>();

    static {
        LOOKUP.put(0, "WHITE");
        LOOKUP.put(1, "ORANGE");
        LOOKUP.put(2, "MAGENTA");
        LOOKUP.put(3, "LIGHT_BLUE");
        LOOKUP.put(4, "YELLOW");
        LOOKUP.put(5, "LIME");
        LOOKUP.put(6, "PINK");
        LOOKUP.put(7, "GRAY");
        LOOKUP.put(8, "LIGHT_GRAY");
        LOOKUP.put(9, "CYAN");
        LOOKUP.put(10, "PURPLE");
        LOOKUP.put(11, "BLUE");
        LOOKUP.put(12, "BROWN");
        LOOKUP.put(13, "GREEN");
        LOOKUP.put(14, "RED");
        LOOKUP.put(15, "BLACK");
    }
}
