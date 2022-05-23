/*
 * Copyright (C) 2022 eccentric_nz
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
package me.eccentric_nz.TARDIS.chemistry.product;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Waterlogged;

public class LampToggler {

    private static final BlockData LIGHT = Bukkit.createBlockData("minecraft:light[level=15,waterlogged=false]");
    private static final BlockData LIGHT_WATERLOGGED = Bukkit.createBlockData("minecraft:light[level=15,waterlogged=true]");

    public static void createLight(Block mushroom) {
        Block block = findAirOrWaterBlock(mushroom);
        if (block != null) {
            if (block.getType().isAir()) {
                block.setBlockData(LIGHT);
            } else {
                block.setBlockData(LIGHT_WATERLOGGED);
            }
        }
    }

    public static void deleteLight(Block mushroom) {
        Block block = findLightBlock(mushroom);
        if (block != null) {
            Waterlogged waterlogged = (Waterlogged) block.getBlockData();
            if (waterlogged.isWaterlogged()) {
                block.setType(Material.WATER);
            } else {
                block.setBlockData(TARDISConstants.AIR);
            }
        }
    }

    private static Block findAirOrWaterBlock(Block source) {
        Block block = null;
        for (BlockFace face : TARDIS.plugin.getGeneralKeeper().getBlockFaces()) {
            block = source.getRelative(face);
            if (block.getType().isAir() || block.getType().equals(Material.WATER)) {
                break;
            }
        }
        return block;
    }

    private static Block findLightBlock(Block source) {
        Block block = null;
        for (BlockFace face : TARDIS.plugin.getGeneralKeeper().getBlockFaces()) {
            block = source.getRelative(face);
            if (block.getType().equals(Material.LIGHT)) {
                break;
            }
        }
        return block;
    }
}
