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
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class InnerDisplayDoorExtra {

    private final TARDIS plugin;

    public InnerDisplayDoorExtra(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void deactivate(Block block, int id, UUID uuid) {
        // deactivate portals / movers
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                    for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                        plugin.getTrackerKeeper().getMovers().remove(p.getUniqueId());
                    }
                } else {
                    String[] companions = tardis.getCompanions().split(":");
                    for (String c : companions) {
                        if (!c.isEmpty()) {
                            plugin.getTrackerKeeper().getMovers().remove(UUID.fromString(c));
                        }
                    }
                    plugin.getTrackerKeeper().getMovers().remove(uuid);
                }
            }
        }
        // get locations
        // interior portal
        Location inportal = block.getLocation();
        // exterior portal (from current location)
        Current current = TARDISCache.CURRENT.get(id);
        if (current != null) {
            // locations
            plugin.getTrackerKeeper().getPortals().remove(current.location());
        }
        plugin.getTrackerKeeper().getPortals().remove(inportal);
    }
}
