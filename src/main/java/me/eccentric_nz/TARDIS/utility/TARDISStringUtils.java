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
package me.eccentric_nz.TARDIS.utility;

import me.eccentric_nz.TARDIS.playerprefs.DefaultFontInfo;

import java.util.List;
import java.util.Locale;

public class TARDISStringUtils {

    private static final List<String> numbers = List.of("ZERO", "ONE", "TWO", "THREE", "FOUR", "FIVE", "SIX", "SEVEN", "EIGHT", "NINE");

    public static String capitalise(String s) {
        String[] split = s.split("_");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
        }
        return builder.toString().trim();
    }

    public static String switchCapitalise(String s) {
        String[] split = s.split("_");
        StringBuilder builder = new StringBuilder();
        builder.append(split[split.length - 1]);
        for (int i = 0; i < split.length - 1; i++) {
            builder.append(" ").append(uppercaseFirst(split[i]));
        }
        return builder.toString();
    }

    public static String sentenceCase(String s) {
        String replaced = s.replace("_", " ");
        return uppercaseFirst(replaced);
    }

    public static String uppercaseFirst(String s) {
        if (s.equalsIgnoreCase("ii") || s.equalsIgnoreCase("iii") || s.equalsIgnoreCase("iv")) {
            return s.toUpperCase(Locale.ROOT);
        }
        return s.substring(0, 1).toUpperCase(Locale.ROOT) + s.substring(1).toLowerCase(Locale.ROOT).replace("tardis", "TARDIS");
    }

    public static String titleCase(String s) {
        String[] split = s.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
        }
        return builder.toString().trim();
    }

    public static String chemistryCase(String s) {
        String[] split = s.split("-");
        StringBuilder builder = new StringBuilder();
        String prefix = "";
        for (String str : split) {
            builder.append(prefix);
            prefix = "_";
            builder.append(uppercaseFirst(str));
        }
        return builder.toString().trim();
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

    public static String toDashedLowercase(String s) {
        return s.toLowerCase(Locale.ROOT).replace("_", "-");
    }

    public static String toLowercaseDashed(String s) {
        return s.toLowerCase(Locale.ROOT).replace(" ", "-");
    }

    public static String toUnderscoredLowercase(String s) {
        return s.toLowerCase(Locale.ROOT).replace(" ", "_");
    }

    public static String toScoredUppercase(String s) {
        return s.toUpperCase(Locale.ROOT).replace("-", "_");
    }

    public static String toUnderscoredUppercase(String s) {
        return s.toUpperCase(Locale.ROOT).replace(" ", "_");
    }

    public static String toEnumUppercase(String s) {
        return s.replace(" ", "_").replace("-", "_").replace("3", "THREE").toUpperCase(Locale.ROOT);
    }

    /**
     * Determines the Material type of the block. Values are calculated by
     * converting the string values stored in a TARDIS Seed block.
     *
     * @param str the lore stored in the TARDIS Seed block's Item Meta
     * @return a String representing the Material
     */
    public static String getValuesFromWallString(String str) {
        String[] split = str.split(": ");
        return split[1];
    }

    public static String rightPad(String message, int length) {
        int size = 0;
        for (char c : message.toCharArray()) {
            DefaultFontInfo info = DefaultFontInfo.getDefaultFontInfo(c);
            size += info.getLength();
            size++;
        }
        int spaceLength = DefaultFontInfo.SPACE.getLength() + 1;
        int compensated = 0;
        StringBuilder sb = new StringBuilder(message);
        while (compensated < length - size) {
            sb.append(" ");
            compensated += spaceLength;
        }
        return sb.toString();
    }
}
