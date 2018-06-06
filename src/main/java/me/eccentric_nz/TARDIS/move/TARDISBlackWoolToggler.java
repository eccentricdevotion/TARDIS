/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISBlackWoolToggler {

    private final TARDIS plugin;

    public TARDISBlackWoolToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleBlocks(int id, Player player) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
        if (rsd.resultSet()) {
            Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
            Material mat;
            if (isAir(b)) {
                mat = Material.BLACK_WOOL;
            } else {
                mat = Material.AIR;
            }
            b.setType(mat);
            b.getRelative(BlockFace.UP).setType(mat);
            if (TARDISStaticUtils.isDoorOpen(b.getRelative(BlockFace.SOUTH))) {
                // toggle doors shut
                new TARDISDoorToggler(plugin, b.getRelative(BlockFace.SOUTH), player, false, true, id).toggleDoors();
            }
        }
    }

    private boolean isAir(Block b) {
        return b.getType().equals(Material.AIR);
    }

    public boolean isOpen(int id) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoorBlocks rsd = new ResultSetDoorBlocks(plugin, id);
        if (rsd.resultSet()) {
            Block b = rsd.getInnerBlock().getRelative(BlockFace.NORTH);
            return (isAir(b));
        } else {
            return false;
        }
    }
}
