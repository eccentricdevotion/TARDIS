/*
 * CraftBook Copyright (C) EngineHub and Contributors <https://enginehub.org/>
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the GNU General Public
 * License as published by the Free
 * Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If not,
 * see <http://www.gnu.org/licenses/>.
 */

package me.eccentric_nz.TARDIS.utility;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Levelled;
import org.bukkit.block.data.Waterlogged;

public class TARDISSponge {

    private static final int radius = 5;

    public static void removeWater(Block block) {
        for (int cx = -radius; cx <= radius; cx++) {
            for (int cy = -radius; cy <= radius; cy++) {
                for (int cz = -radius; cz <= radius; cz++) {
                    Block water = block.getRelative(cx, cy, cz);
                    if (!isWithinSphericalRadius(water, block, radius)) {
                        continue;
                    }
                    if (isRemovableWater(water.getType())) {
                        water.setType(Material.AIR);
                    } else {
                        BlockData data = water.getBlockData();
                        if (data instanceof Waterlogged waterlogged) {
                            waterlogged.setWaterlogged(false);
                            water.setBlockData(waterlogged);
                        }
                    }
                }
            }
        }
    }

    public static void addWater(Block block) {
        // The negative x edge
        int cx = -radius - 1;
        for (int cy = -radius - 1; cy <= radius + 1; cy++) {
            for (int cz = -radius - 1; cz <= radius + 1; cz++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(1, 0, 0), water);
                }
            }
        }
        // The positive x edge
        cx = radius + 1;
        for (int cy = -radius - 1; cy <= radius + 1; cy++) {
            for (int cz = -radius - 1; cz <= radius + 1; cz++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(-1, 0, 0), water);
                }
            }
        }
        // The negative y edge
        int cy = -radius - 1;
        for (cx = -radius - 1; cx <= radius + 1; cx++) {
            for (int cz = -radius - 1; cz <= radius + 1; cz++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(0, 1, 0), water);
                }
            }
        }
        // The positive y edge
        cy = radius + 1;
        for (cx = -radius - 1; cx <= radius + 1; cx++) {
            for (int cz = -radius - 1; cz <= radius + 1; cz++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(0, -1, 0), water);
                }
            }
        }
        // The negative z edge
        int cz = -radius - 1;
        for (cx = -radius - 1; cx <= radius + 1; cx++) {
            for (cy = -radius - 1; cy <= radius + 1; cy++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(0, 0, 1), water);
                }
            }
        }
        // The positive z edge
        cz = radius + 1;
        for (cx = -radius - 1; cx <= radius + 1; cx++) {
            for (cy = -radius - 1; cy <= radius + 1; cy++) {
                Block water = block.getRelative(cx, cy, cz);
                if (isWithinSphericalRadius(water, block, radius + 1)) {
                    continue;
                }
                if (isRemovableWater(water.getType())) {
                    setBlockToWater(water.getRelative(0, 0, -1), water);
                }
            }
        }
    }

    private static boolean isRemovableWater(Material material) {
        return material == Material.WATER || material == Material.SEAGRASS || material == Material.TALL_SEAGRASS || material == Material.KELP_PLANT || material == Material.KELP;
    }

    private static boolean isWithinSphericalRadius(Location base, Location checked, double radius) {
        return base.getWorld() == checked.getWorld() && base.distanceSquared(checked) <= radius * radius;
    }

    private static boolean isWithinSphericalRadius(Block base, Block checked, double radius) {
        return isWithinSphericalRadius(base.getLocation(), checked.getLocation(), radius + 0.5);
    }

    private static void setBlockToWater(Block block, Block source) {
        if (block.getType().isAir()) {
            BlockData sourceData = source.getBlockData();
            int level = 0;
            if (sourceData instanceof Levelled levelled) {
                int sourceLevel = levelled.getLevel();
                if (sourceLevel != 0) {
                    level = Math.max(levelled.getLevel() + 1, 7);
                }
            }
            Levelled data = (Levelled) Bukkit.createBlockData(Material.WATER);
            data.setLevel(level);
            block.setBlockData(data);
        }
    }
}
