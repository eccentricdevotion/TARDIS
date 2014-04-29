/*
 * Copyright (C) 2014 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBlackWoolToggler {

    private final TARDIS plugin;

    public TARDISBlackWoolToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleBlocks(int id) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        where.put("door_type", 1);
        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
        if (rsd.resultSet()) {
            Block b = plugin.getUtils().getLocationFromDB(rsd.getDoor_location(), 0.0f, 0.0f).getBlock().getRelative(BlockFace.NORTH);
            Material mat;
            byte data;
            if (isAir(b)) {
                mat = Material.WOOL;
                data = (byte) 15;
            } else {
                mat = Material.AIR;
                data = (byte) 0;
            }
            b.setType(mat);
            b.setData(data);
            b.getRelative(BlockFace.UP).setType(mat);
            b.getRelative(BlockFace.UP).setData(data);
        }
    }

    private boolean isAir(Block b) {
        return b.getType().equals(Material.AIR);
    }
}
