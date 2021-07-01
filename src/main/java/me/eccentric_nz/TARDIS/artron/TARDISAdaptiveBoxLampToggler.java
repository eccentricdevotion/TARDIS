/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISAdaptiveBoxLampToggler {

    private final TARDIS plugin;

    public TARDISAdaptiveBoxLampToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleLamp(int id, boolean on, PRESET preset) {
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetCurrentLocation rs = new ResultSetCurrentLocation(plugin, where);
        if (rs.resultSet()) {
            Location location = new Location(rs.getWorld(), rs.getX(), rs.getY(), rs.getZ());
            Block light = location.getBlock().getRelative(BlockFace.UP, 2);
            if (preset.usesItemFrame()) {
                if (on) {
                    light.setBlockData(TARDISConstants.LIGHT);
                } else {
                    light.setBlockData(TARDISConstants.AIR);
                }
            } else {
                Block lamp = location.getBlock().getRelative(BlockFace.UP, 3);
                while (!lamp.getChunk().isLoaded()) {
                    lamp.getChunk().load();
                }
                if (lamp.getType().equals(Material.REDSTONE_LAMP)) {
                    if (on) {
                        // turn on
                        light.setBlockData(TARDISConstants.POWER);
                    } else {
                        // turn off
                        light.setBlockData(Material.BLUE_WOOL.createBlockData());
                    }
                }
            }
        }
    }
}
