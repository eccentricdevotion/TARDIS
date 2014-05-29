/*
 * Copyright (C) Error: on line 4, column 33 in Templates/Licenses/license-gpl30.txt
 The string doesn't match the expected date/time format. The string to parse was: "29/05/2014". The expected format was: "MMM d, yyyy". eccentric_nz
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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
 * @author eccentric_nz
 */
public class TARDISPoliceBoxLampToggler {

    private final TARDIS plugin;

    public TARDISPoliceBoxLampToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleLamp(int id, boolean on) {
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (rs.resultSet()) {
            Block lamp = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ()).getBlock().getRelative(BlockFace.UP, 3);
            Block redstone = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ()).getBlock().getRelative(BlockFace.UP, 2);
            if (lamp.getType().equals(Material.REDSTONE_LAMP_ON) && !on) {
                // turn off
                redstone.setType(Material.WOOL);
                redstone.setData((byte) 11);
                return;
            }
            if (lamp.getType().equals(Material.REDSTONE_LAMP_OFF) && on) {
                // turn on
                redstone.setType(Material.REDSTONE_BLOCK);
            }
        }
    }

}
