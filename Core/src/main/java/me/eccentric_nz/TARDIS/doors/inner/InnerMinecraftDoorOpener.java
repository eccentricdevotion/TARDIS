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
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetOuterPortal;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
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

public class InnerMinecraftDoorOpener {

    private final TARDIS plugin;

    public InnerMinecraftDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Block block, int id) {
        // open door
        if (Tag.DOORS.isTagged(block.getType())) {
            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(true);
            block.setBlockData(openable, true);
        }
        if (!plugin.getTrackerKeeper().getWoolToggles().contains(id)) {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                ChameleonPreset preset = tardis.getPreset();
                // get portal location
                Location portal = block.getLocation();
                // get tp location
                ResultSetOuterPortal resultSetPortal = new ResultSetOuterPortal(plugin, id);
                if (resultSetPortal.resultSet()) {
                    Location teleport = resultSetPortal.getLocation().clone();
                    // adjust for teleport
                    if (preset.usesArmourStand() || preset == ChameleonPreset.INVISIBLE) {
                        switch (resultSetPortal.getDirection()) {
                            case NORTH_EAST -> teleport.add(0, 0, 1);
                            case NORTH -> teleport.add(0.5d, 0.0d, 1.0d);
                            case NORTH_WEST -> teleport.add(1, 0, 1);
                            case WEST -> teleport.add(1.0d, 0.0d, 0.5d);
                            case SOUTH_WEST -> teleport.add(1, 0, -0.5);
                            case SOUTH -> teleport.add(0.5d, 0.0d, -1.0d);
                            case SOUTH_EAST -> teleport.add(-0.5, 0, 0);
                            default -> teleport.add(-1.0d, 0.0d, 0.5d);
                        }
                    } else {
                        teleport.setX(teleport.getX() + 0.5);
                        teleport.setZ(teleport.getZ() + 0.5);
                    }
                    TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                    tp_out.setLocation(teleport);
                    tp_out.setTardisId(id);
                    tp_out.setDirection(resultSetPortal.getDirection());
                    tp_out.setAbandoned(tardis.isAbandoned());
                    // create portal
                    plugin.getTrackerKeeper().getPortals().put(portal, tp_out);
                    // add movers
                    if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                        // always add the time lord of this TARDIS - as a companion may be opening the door
                        plugin.getTrackerKeeper().getMovers().add(tardis.getUuid());
                        // others
                        if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                            // online players
                            for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                                plugin.getTrackerKeeper().getMovers().add(p.getUniqueId());
                            }
                        } else {
                            //  companion UUIDs
                            String[] companions = tardis.getCompanions().split(":");
                            for (String c : companions) {
                                if (!c.isEmpty()) {
                                    plugin.getTrackerKeeper().getMovers().add(UUID.fromString(c));
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
