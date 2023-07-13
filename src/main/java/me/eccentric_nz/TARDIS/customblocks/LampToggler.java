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
package me.eccentric_nz.TARDIS.customblocks;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Waterlogged;
import org.bukkit.block.data.type.Light;
import org.bukkit.entity.ItemDisplay;

public class LampToggler {

    public static void setLightlevel(Block block, int level) {
        Light light = (Light) Material.LIGHT.createBlockData();
        light.setLevel(level);
        block.setBlockData(light);
    }

    public static void deleteLight(Block lamp) {
        Block block = findLightBlock(lamp);
        if (block != null) {
            if (block.getBlockData() instanceof Waterlogged waterlogged && waterlogged.isWaterlogged()) {
                block.setType(Material.WATER);
            } else {
                block.setBlockData(TARDISConstants.AIR);
            }
        }
    }

    private static Block findLightBlock(Block source) {
        Block block = null;
        for (BlockFace face : TARDIS.plugin.getGeneralKeeper().getBlockFaces()) {
            block = source.getRelative(face);
            // check it is not another item display lamp
            ItemDisplay display = TARDISDisplayItemUtils.get(block);
            if (display == null && block.getType().equals(Material.LIGHT)) {
                break;
            }
        }
        return block;
    }
}
