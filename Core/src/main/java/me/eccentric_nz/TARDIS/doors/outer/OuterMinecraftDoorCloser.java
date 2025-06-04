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
package me.eccentric_nz.TARDIS.doors.outer;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISCache;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.doors.DoorUtility;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class OuterMinecraftDoorCloser {

    private final TARDIS plugin;

    public OuterMinecraftDoorCloser(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void close(Block block, int id, UUID uuid) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable closeable = (Openable) block.getBlockData();
            closeable.setOpen(false);
            block.setBlockData(closeable, true);
        }
        // hide the interior if it is being cast
        if (plugin.getConfig().getBoolean("police_box.view_interior")) {
            plugin.getTrackerKeeper().getCasters().remove(uuid);
            // remove fake blocks
            if (plugin.getTrackerKeeper().getCastRestore().containsKey(uuid)) {
                for (Block b : plugin.getTrackerKeeper().getCastRestore().get(uuid)) {
                    b.getState().update();
                }
                plugin.getTrackerKeeper().getCastRestore().remove(uuid);
                // remove fake item frame if necessary
                if (plugin.getTrackerKeeper().getRotorRestore().containsKey(uuid)) {
                    int rotorId = plugin.getTrackerKeeper().getRotorRestore().get(uuid);
                    plugin.getTardisHelper().removeFakeItemFrame(rotorId, Bukkit.getPlayer(uuid));
                    plugin.getTrackerKeeper().getRotorRestore().remove(uuid);
                }
            }
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            // get exterior portal (from current location)
            Current current = TARDISCache.CURRENT.get(id);
            Location portal = current.location();
            if (tardis.getPreset().equals(ChameleonPreset.SWAMP)) {
                portal.add(0.0d, 1.0d, 0.0d);
            }
            // remove exterior portal
            TARDISTeleportLocation removed = plugin.getTrackerKeeper().getPortals().remove(portal);
            if (removed == null) {
                DoorUtility.debugPortal(portal.toString());
            }
            // remove movers
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
    }
}
