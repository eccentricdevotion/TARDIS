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
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetInnerDoorLocations;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.move.TARDISTeleportLocation;
import me.eccentric_nz.TARDIS.portal.Capture;
import me.eccentric_nz.TARDIS.portal.Cast;
import me.eccentric_nz.TARDIS.portal.CastData;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

public class OuterMinecraftDoorOpener {

    private final TARDIS plugin;

    public OuterMinecraftDoorOpener(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void open(Block block, int id, Player player) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable openable = (Openable) block.getBlockData();
            openable.setOpen(true);
            block.setBlockData(openable, true);
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            ChameleonPreset preset = tardis.getPreset();
            // get locations
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Current current = rsc.getCurrent();
            Location portal = current.location();
            if (preset != null && preset.equals(ChameleonPreset.SWAMP)) {
                portal.add(0.0d, 1.0d, 0.0d);
            }
            // get interior teleport location
            ResultSetInnerDoorLocations resultSetPortal = new ResultSetInnerDoorLocations(plugin, id);
            if (resultSetPortal.resultSet()) {
                Location teleport = resultSetPortal.getTeleportLocation();
                TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                tp_in.setLocation(teleport);
                tp_in.setTardisId(id);
                tp_in.setDirection(resultSetPortal.getDirection());
                tp_in.setAbandoned(tardis.isAbandoned());
                // create portal
                plugin.getTrackerKeeper().getPortals().put(portal, tp_in);
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
                if (preset != null && preset.equals(ChameleonPreset.INVISIBLE) && plugin.getConfig().getBoolean("allow.3d_doors")) {
                    // remember door location
                    plugin.getTrackerKeeper().getInvisibleDoors().put(tardis.getUuid(), block);
                }
                if (plugin.getConfig().getBoolean("police_box.view_interior") && preset != null && !preset.usesArmourStand()) {
                    UUID uuid = player.getUniqueId();
                    ConsoleSize consoleSize = tardis.getSchematic().getConsoleSize();
                    plugin.getTrackerKeeper().getCasters().put(uuid, new CastData(resultSetPortal.getDoorLocation(), portal, current.direction(), tardis.getRotor(), consoleSize));
                    // get distance from door
                    Location location = player.getLocation();
                    double distance = (location.getWorld() == portal.getWorld()) ? location.distanceSquared(portal) : 1; // or exdoor?
                    if (distance <= 9) {
                        // start casting
                        Capture capture = new Capture();
                        BlockData[][][] data = capture.captureInterior(resultSetPortal.getDoorLocation(), (int) distance, tardis.getRotor(), consoleSize);
                        Cast cast = new Cast(plugin, portal);
                        cast.castInterior(uuid, data);
                        if (capture.getRotorData().frame() != null) {
                            // get vector of rotor
                            cast.castRotor(capture.getRotorData().frame(), player, capture.getRotorData().offset(), current.direction());
                        }
                    }
                }
            }
        }
    }
}
