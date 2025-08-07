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
package me.eccentric_nz.TARDIS.builders.utility;

import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.util.Vector;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FractalFence {

    public static void grow(Block start, int i) {
        Material material = start.getType();
        for (Location location : getPoints(start.getLocation(), i)) {
            Block block = location.getBlock();
            if (block.getType().isAir()) {
                block.setType(material, true);
            }
        }
    }

    private static Set<Location> getPoints(Location location, int i) {
        List<Vector> points = new ArrayList<>();
        points.add(location.toVector());
        Set<Location> adjacent = new HashSet<>();
        World world = location.getWorld();
        int minx = location.getBlockX();
        int miny = location.getBlockY() + 1;
        int minz = location.getBlockZ();
        switch (i) {
            case 1 -> {
                minx -= 4;
                minz -= 4;
            }
            case 2 -> {
                minx -= 4;
                minz -= 1;
            }
            case 3 -> {
                minx -= 1;
                minz -= 4;
            }
            default -> { // 0
                minx -= 1;
                minz -= 1;
            }
        }
        int n = 0;
        while (n < 60) {
            int rx = minx + TARDISConstants.RANDOM.nextInt(6);
            int ry = miny + TARDISConstants.RANDOM.nextInt(5);
            int rz = minz + TARDISConstants.RANDOM.nextInt(6);
            points.add(new Vector(rx, ry, rz));
            n++;
        }
        for (int j = 0; j < points.size() - 1; j++) {
            for (int k = 1; k < points.size(); k++) {
                Vector a = points.get(j);
                Vector b = points.get(k);
                int diff = Math.abs(a.getBlockX() - b.getBlockX()) + Math.abs(a.getBlockY() - b.getBlockY()) + Math.abs(a.getBlockZ() - b.getBlockZ());
                if (diff == 1) {
                    adjacent.add(new Location(world, a.getBlockX(), a.getBlockY(), a.getBlockZ()));
                }
            }
        }
        return adjacent;
    }
}
