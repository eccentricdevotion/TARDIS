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
package me.eccentric_nz.TARDIS.doors.inner;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InnerMinecraftDoorCloser {

    private final TARDIS plugin;

    public InnerMinecraftDoorCloser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void close(Block block, int id, UUID uuid) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable closeable = (Openable) block.getBlockData();
            closeable.setOpen(false);
            block.setBlockData(closeable, true);
        }
        if (plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                // remove portal
                TARDISTeleportLocation removed = plugin.getTrackerKeeper().getPortals().remove(block.getLocation());
                if (removed == null) {
                    DoorUtility.debugPortal(block.getLocation().toString());
                }
                // remove movers
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (rs.getTardis().getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            plugin.getTrackerKeeper().getMovers().remove(p.getUniqueId());
                        }
                    } else {
                        String[] companions = rs.getTardis().getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                plugin.getTrackerKeeper().getMovers().remove(UUID.fromString(c));
                            }
                        }
                        plugin.getTrackerKeeper().getMovers().remove(uuid);
                    }
                }
            }
        }
    }
}
