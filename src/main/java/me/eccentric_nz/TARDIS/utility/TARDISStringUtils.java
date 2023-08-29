/*
 * Copyright (C) 2023 eccentric_nz
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
            return s.toUpperCase();
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase().replace("tardis", "TARDIS");
    }

    public static String titleCase(String s) {
        String[] split = s.split(" ");
        StringBuilder builder = new StringBuilder();
        for (String str : split) {
            builder.append(uppercaseFirst(str)).append(" ");
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
        return s.toLowerCase().replace("_", "-");
    }

    public static String toLowercaseDashed(String s) {
        return s.toLowerCase().replace(" ", "-");
    }

    public static String toScoredUppercase(String s) {
        return s.toUpperCase().replace("-", "_");
    }

    public static String toUnderscoredUppercase(String s) {
        return s.toUpperCase().replace(" ", "_");
    }

    public static String toEnumUppercase(String s) {
        return s.replace(" ", "_").replace("-", "_").replace("3", "THREE").toUpperCase(Locale.ENGLISH);
    }

    /**
     * Determines the Material type of the block. Values are calculated by
     * converting the string values stored in a TARDIS Seed block.
     *
     * @param str the lore stored in the TARDIS Seed block's Item Meta
     * @return an String representing the Material
     */
    public static String getValuesFromWallString(String str) {
        String[] split = str.split(": ");
        return split[1];
    }

    /**
     * Replace all occurrences of Strings within another String.
     *
     * @param text text to search and replace in, no-op if null
     * @param searchList the Strings to search for, no-op if null
     * @param replacementList the Strings to replace them with, no-op if null
     * @return the text with any replacements processed, {@code null} if null
     * String input
     * @throws IllegalArgumentException if the lengths of the arrays are not the
     * same (null is ok, and/or size 0)
     */
    public static String replaceEach(String text, String[] searchList, String[] replacementList) {
        if (isEmpty(text) || isEmptyArray(searchList) || isEmptyArray(replacementList)) {
            return text;
        }
        int searchLength = searchList.length;
        int replacementLength = replacementList.length;
        // make sure lengths are ok, these need to be equal
        if (searchLength != replacementLength) {
            throw new IllegalArgumentException("Search and Replace array lengths don't match: "
                    + searchLength
                    + " vs "
                    + replacementLength);
        }
        // keep track of which still have matches
        boolean[] noMoreMatchesForReplIndex = new boolean[searchLength];
        // index on index that the match was found
        int textIndex = -1;
        int replaceIndex = -1;
        int tempIndex;
        // index of replace array that will replace the search string found
        for (int i = 0; i < searchLength; i++) {
            if (noMoreMatchesForReplIndex[i] || isEmpty(searchList[i]) || replacementList[i] == null) {
                continue;
            }
            tempIndex = text.indexOf(searchList[i]);
            // see if we need to keep searching for this
            if (tempIndex == -1) {
                noMoreMatchesForReplIndex[i] = true;
            } else if (textIndex == -1 || tempIndex < textIndex) {
                textIndex = tempIndex;
                replaceIndex = i;
            }
        }
        // no search strings found, we are done
        if (textIndex == -1) {
            return text;
        }
        int start = 0;
        // get a good guess on the size of the result buffer so it doesn't have to double if it goes over a bit
        int increase = 0;
        // count the replacement text elements that are larger than their corresponding text being replaced
        for (int i = 0; i < searchList.length; i++) {
            if (searchList[i] == null || replacementList[i] == null) {
                continue;
            }
            int greater = replacementList[i].length() - searchList[i].length();
            if (greater > 0) {
                increase += 3 * greater; // assume 3 matches
            }
        }
        // have upper-bound at 20% increase, then let Java take over
        increase = Math.min(increase, text.length() / 5);
        StringBuilder buf = new StringBuilder(text.length() + increase);
        while (textIndex != -1) {
            for (int i = start; i < textIndex; i++) {
                buf.append(text.charAt(i));
            }
            buf.append(replacementList[replaceIndex]);
            start = textIndex + searchList[replaceIndex].length();
            textIndex = -1;
            replaceIndex = -1;
            // find the next earliest match
            for (int i = 0; i < searchLength; i++) {
                if (noMoreMatchesForReplIndex[i] || searchList[i] == null
                        || searchList[i].isEmpty() || replacementList[i] == null) {
                    continue;
                }
                tempIndex = text.indexOf(searchList[i], start);
                // see if we need to keep searching for this
                if (tempIndex == -1) {
                    noMoreMatchesForReplIndex[i] = true;
                } else if (textIndex == -1 || tempIndex < textIndex) {
                    textIndex = tempIndex;
                    replaceIndex = i;
                }
            }
        }
        int textLength = text.length();
        for (int i = start; i < textLength; i++) {
            buf.append(text.charAt(i));
        }
        return buf.toString();
    }

    public static boolean isEmpty(CharSequence cs) {
        return cs == null || cs.length() == 0;
    }

    /**
     * Checks if an array of primitive chars is empty or {@code null}.
     *
     * @param array the array to test
     * @return {@code true} if the array is empty or {@code null}
     */
    private static boolean isEmptyArray(String[] array) {
        return getLength(array) == 0;
    }

    /**
     * Returns the length of the specified array.
     *
     * @param array the array to retrieve the length from, may be null
     * @return The length of the array, or {@code 0} if the array is
     * {@code null}
     * @throws IllegalArgumentException if the object argument is not an array.
     */
    private static int getLength(Object array) {
        if (array == null) {
            return 0;
        }
        return Array.getLength(array);
    }

    public static String normalizeSpace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int size = str.length();
        char[] newChars = new char[size];
        int count = 0;
        int whitespacesCount = 0;
        boolean startWhitespaces = true;
        for (int i = 0; i < size; i++) {
            char actualChar = str.charAt(i);
            boolean isWhitespace = Character.isWhitespace(actualChar);
            if (isWhitespace) {
                if (whitespacesCount == 0 && !startWhitespaces) {
                    newChars[count++] = ' ';
                }
                whitespacesCount++;
            } else {
                startWhitespaces = false;
                newChars[count++] = (actualChar == 160 ? 32 : actualChar);
                whitespacesCount = 0;
            }
        }
        if (startWhitespaces) {
            return "";
        }
        return new String(newChars, 0, count - (whitespacesCount > 0 ? 1 : 0)).trim();
    }
}
