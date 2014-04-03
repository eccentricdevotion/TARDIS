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
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISQuitListener implements Listener {

    private final TARDIS plugin;

    public TARDISQuitListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        // forget the players Police Box chunk
        HashMap<String, Object> wherep = new HashMap<String, Object>();
        wherep.put("uuid", event.getPlayer().getUniqueId().toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, wherep, "", false);
        if (rs.resultSet()) {
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", rs.getTardis_id());
            ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
            if (rsc.resultSet()) {
                World w = rsc.getWorld();
                if (w != null) {
                    Chunk chunk = w.getChunkAt(new Location(w, rsc.getX(), rsc.getY(), rsc.getZ()));
                    if (plugin.getGeneralKeeper().getTardisChunkList().contains(chunk)) {
                        plugin.getGeneralKeeper().getTardisChunkList().remove(chunk);
                    }
                }
            }
        }
    }
}
