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
package me.eccentric_nz.tardischunkgenerator.worldgen.utils;

import org.bukkit.util.BlockVector;

import java.util.ArrayList;
import java.util.List;

public class GallifeyStructureUtility {

    public static final List<BlockVector> vectorLeft = new ArrayList<>();
    public static final List<BlockVector> vectorUp = new ArrayList<>();
    public static final List<BlockVector> vectorRight = new ArrayList<>();
    public static final List<BlockVector> vectorDown = new ArrayList<>();
    public static final List<String> structures = List.of("large", "farm", "house", "water", "temple");

    static {
        // left
        vectorLeft.add(new BlockVector(-13, -4, -14)); // large
        vectorLeft.add(new BlockVector(23, 0, -11)); // farm
        vectorLeft.add(new BlockVector(20, 0, 20)); // house
        vectorLeft.add(new BlockVector(8, 0, 25)); // water feature
        vectorLeft.add(new BlockVector(-15, 0, 21)); // temple
        // up
        vectorUp.add(new BlockVector(-3, -4, -13)); // large
        vectorUp.add(new BlockVector(19, 0, 23)); // farm
        vectorUp.add(new BlockVector(-13, 0, 20)); // house
        vectorUp.add(new BlockVector(-14, 0, 8)); // water feature
        vectorUp.add(new BlockVector(-15, 0, -15)); // temple
        // right
        vectorRight.add(new BlockVector(-4, -4, -3)); // large
        vectorRight.add(new BlockVector(-15, 0, 19)); // farm
        vectorRight.add(new BlockVector(-13, 0, -13)); // house
        vectorRight.add(new BlockVector(3, 0, -14)); // water feature
        vectorRight.add(new BlockVector(21, 0, -15)); // temple
        // down
        vectorDown.add(new BlockVector(-14, -4, -4)); // large
        vectorDown.add(new BlockVector(-11, 0, -15)); // farm
        vectorDown.add(new BlockVector(20, 0, -13)); // house
        vectorDown.add(new BlockVector(25, 0, 3)); // water feature
        vectorDown.add(new BlockVector(21, 0, 21)); // temple
    }
}
