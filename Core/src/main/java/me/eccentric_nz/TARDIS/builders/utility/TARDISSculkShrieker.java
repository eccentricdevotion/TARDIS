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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetShrieker;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.tardischunkgenerator.helpers.Shrieker;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.SculkShrieker;

public class TARDISSculkShrieker {

    public static void setRotor(int id) {
        ResultSetShrieker rs = new ResultSetShrieker(TARDIS.plugin, id);
        if (rs.resultSet()) {
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            Block block = location.getBlock();
            if (block.getType() == Material.SCULK_SHRIEKER) {
                SculkShrieker shrieker = (SculkShrieker) block.getBlockData();
                int task = TARDIS.plugin.getServer().getScheduler().scheduleSyncRepeatingTask(TARDIS.plugin, () -> {
                    Shrieker.shriek(block);
                }, 2L, 90L);
                TARDIS.plugin.getTrackerKeeper().getShriekers().put(id, task);
            }
        }
    }

    public static void stopRotor(int id) {
        int task = TARDIS.plugin.getTrackerKeeper().getShriekers().getOrDefault(id, -1);
        if (task != -1) {
            TARDIS.plugin.getServer().getScheduler().cancelTask(task);
            ResultSetShrieker rs = new ResultSetShrieker(TARDIS.plugin, id);
            if (rs.resultSet()) {
                Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
                Block block = location.getBlock();
                if (block.getType() == Material.SCULK_SHRIEKER) {
                    SculkShrieker shrieker = (SculkShrieker) block.getBlockData();
                    shrieker.setShrieking(false);
                    block.setBlockData(shrieker);
                }
            }
        }
    }
}
