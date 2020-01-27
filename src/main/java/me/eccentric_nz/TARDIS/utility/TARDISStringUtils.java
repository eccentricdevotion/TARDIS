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
package me.eccentric_nz.TARDIS.utility;

import java.util.Arrays;
import java.util.List;

public class TARDISStringUtils {

    private static final List<String> numbers = Arrays.asList("ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE");

    public static String capitalise(String s) {
        String[] split = s.split("_");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
        }
        return builder.toString().trim();
    }

    public static String sentenceCase(String s) {
        String replaced = s.replace("_", " ");
        return uppercaseFirst(replaced);
    }

    public static String uppercaseFirst(String s) {
        if (s.equalsIgnoreCase("ii") || s.equalsIgnoreCase("iii") || s.equalsIgnoreCase("iv")) {
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }

    public static String fromInt(int i) {
        if (i > 9) {
            return "A big number";
        }
        return numbers.get(i);
    }

    public static String toNumber(String s) {
        return String.format("%s", numbers.indexOf(s));
        // "-1" if not found
    }
}
