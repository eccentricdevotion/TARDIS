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
package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import org.bukkit.util.BlockVector;

import java.util.HashMap;
import java.util.List;

public class SiluriaStructureUtility {

    public static final HashMap<BlockVector, String> vectorZero = new HashMap<>();
    public static final HashMap<BlockVector, String> vectorOne = new HashMap<>();
    public static final HashMap<BlockVector, String> vectorTwo = new HashMap<>();
    public static final HashMap<BlockVector, String> vectorThree = new HashMap<>();
    public static final HashMap<BlockVector, String> vectorFour = new HashMap<>();
    public static final HashMap<BlockVector, String> vectorFive = new HashMap<>();
    public static final HashMap<BlockVector, HashMap<BlockVector, String>> centres = new HashMap<>();
    public static final List<String> structures = List.of("cross", "east_west", "farm", "large", "lift", "north_south", "small", "temple");

    static {
        // zero
        vectorZero.put(new BlockVector(0, 0, 0), "lift");
        vectorZero.put(new BlockVector(0, 0, -16), "north_south");
        vectorZero.put(new BlockVector(0, 0, 16), "north_south");
        vectorZero.put(new BlockVector(-16, 0, 0), "east_west");
        vectorZero.put(new BlockVector(16, 0, 0), "east_west");
        // one
        vectorOne.put(new BlockVector(0, 0, -16), "large");
        vectorOne.put(new BlockVector(0, 0, 16), "north_south");
        vectorOne.put(new BlockVector(0, 0, 0), "cross");
        vectorOne.put(new BlockVector(-16, 0, 0), "east_west");
        vectorOne.put(new BlockVector(16, 0, 0), "east_west");
        // two
        vectorTwo.put(new BlockVector(0, 0, 0), "temple");
        // three
        vectorThree.put(new BlockVector(0, 0, 0), "north_south");
        vectorThree.put(new BlockVector(0, 0, -16), "cross");
        vectorThree.put(new BlockVector(16, 0, -16), "large");
        vectorThree.put(new BlockVector(0, 0, 16), "small");
        // four
        vectorFour.put(new BlockVector(0, 0, 0), "cross");
        vectorFour.put(new BlockVector(-16, 0, 0), "east_west");
        vectorFour.put(new BlockVector(16, 0, 0), "east_west");
        // five
        vectorFive.put(new BlockVector(0, 0, 0), "farm");
        // centres
        centres.put(new BlockVector(3, 0, 0), vectorOne); // one
        centres.put(new BlockVector(5, 0, 0), vectorTwo); // two
        centres.put(new BlockVector(0, 0, 3), vectorThree); // three
        centres.put(new BlockVector(3, 0, 2), vectorFour); // four
        centres.put(new BlockVector(5, 0, 2), vectorFive); // five
    }
}
