/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * One of the key features of a TARDIS is that the interior exists in a
 * dimension different from the exterior. The main application of this concept
 * is that they are bigger on the inside than the outside.
 *
 * @author eccentric_nz
 */
public class TARDISConstants {

    // chameleon blocks
    public static final List<Integer> CHAMELEON_BLOCKS_VALID = Arrays.asList(new Integer[]{1, 3, 4, 5, 7, 14, 15, 16, 17, 18, 19, 20, 21, 22, 24, 25, 35, 41, 42, 43, 45, 46, 47, 48, 49, 56, 57, 58, 73, 74, 79, 80, 82, 84, 86, 87, 88, 89, 91, 98, 99, 100, 103, 110, 112, 121, 123, 124, 129, 133, 155, 159, 161, 162, 170, 172, 173, 174});
    public static final List<Integer> CHAMELEON_BLOCKS_BAD = Arrays.asList(new Integer[]{6, 8, 9, 10, 11, 23, 26, 29, 33, 34, 50, 51, 52, 54, 55, 59, 61, 62, 63, 64, 65, 68, 70, 71, 72, 75, 76, 77, 83, 85, 90, 92, 93, 94, 95, 101, 107, 111, 115, 116, 117, 118, 119, 122, 127, 130, 131, 132});
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_ARR = {2, 12, 13, 44, 53, 60, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156, 163, 164};
    private static final Integer[] CHAMELEON_BLOCKS_CHANGE_TO_ARR = {3, 24, 1, 43, 5, 3, 4, 80, 24, 5, 1, 20, 103, 86, 18, 45, 1, 112, 112, 121, 5, 5, 24, 5, 5, 5, 155, 5, 5};
    public static final List<Integer> CHAMELEON_BLOCKS_CHANGE = Arrays.asList(new Integer[]{2, 12, 13, 44, 53, 67, 78, 81, 96, 97, 101, 104, 105, 106, 108, 109, 113, 114, 120, 125, 126, 128, 134, 135, 136, 156, 163, 164});
    public static final HashMap<Integer, Integer> CHAMELEON_BLOCKS_CHANGE_HASH = toMap(CHAMELEON_BLOCKS_CHANGE_ARR, CHAMELEON_BLOCKS_CHANGE_TO_ARR);
    public static final List<Integer> CHAMELEON_BLOCKS_NEXT = Arrays.asList(new Integer[]{0, 27, 28, 30, 31, 32, 37, 38, 39, 40, 66, 151});
    public static final List<Integer> CHAMELEON_BLOCKS_PRECIOUS = Arrays.asList(new Integer[]{41, 42, 46, 57, 133, 152, 173});
    // messages
    public static final String NO_PERMS_MESSAGE = "You do not have permission to do that!";
    public static final String NOT_OWNER = "You are not the Timelord or Companion for this TARDIS!";
    public static final String NO_TARDIS = "You have not created a TARDIS yet!";
    public static final String TIMELORD_OFFLINE = "The Timelord who owns this TARDIS is offline!";
    public static final String TIMELORD_NOT_IN = "The Timelord who owns this TARDIS is not inside!";
    public static final List<String> vowels = Arrays.asList(new String[]{"A", "E", "I", "O", "U"});

    /**
     * Creates a HashMap from two arrays. The resulting map is used by the
     * chameleon circuit to change unsuitable blocks into more aesthetically
     * pleasing or robust ones i.e. GRASS -> DIRT, SAND -> SANDSTONE
     *
     * @param keys an array of block types to change
     * @param values an array of block types to change to
     * @return the combined arrays as a HashMap
     */
    public static HashMap toMap(Object[] keys, Object[] values) {
        int keysSize = (keys != null) ? keys.length : 0;
        int valuesSize = (values != null) ? values.length : 0;
        if (keysSize == 0 && valuesSize == 0) {
            return new HashMap();
        }
        if (keysSize != valuesSize) {
            throw new IllegalArgumentException("The number of keys doesn't match the number of values.");
        }
        HashMap map = new HashMap();
        for (int i = 0; i < keysSize; i++) {
            map.put(keys[i], values[i]);
        }
        return map;
    }
}
