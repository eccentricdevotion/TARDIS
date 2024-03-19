/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPortals;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.ConsoleSize;
import me.eccentric_nz.TARDIS.portal.Capture;
import me.eccentric_nz.TARDIS.portal.Cast;
import me.eccentric_nz.TARDIS.portal.CastData;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.BlockData;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISDoorOpener {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public TARDISDoorOpener(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void openDoors() {
        // get door locations
        // inner
        ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultSet()) {
            open(rs.getInnerBlock(), rs.getOuterBlock(), true);
            // outer
            if (!rs.getOuterBlock().getChunk().isLoaded()) {
                rs.getOuterBlock().getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> open(rs.getOuterBlock(), rs.getInnerBlock(), false), 5L);
        }
    }

    /**
     * Open the door.
     */
    private void open(Block inner, Block outer, boolean add) {
        if (Tag.DOORS.isTagged(inner.getType())) {
            Openable openable = (Openable) inner.getBlockData();
            openable.setOpen(true);
            inner.setBlockData(openable, true);
        }
        if (add && plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            // get all companion UUIDs
            List<UUID> uuids = new ArrayList<>();
            uuids.add(uuid);
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            Tardis tardis = null;
            ChameleonPreset preset = null;
            boolean abandoned = false;
            if (rs.resultSet()) {
                tardis = rs.getTardis();
                preset = tardis.getPreset();
                abandoned = tardis.isAbandoned();
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (tardis.getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            uuids.add(p.getUniqueId());
                        }
                    } else {
                        String[] companions = tardis.getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                uuids.add(UUID.fromString(c));
                            }
                        }
                    }
                }
            }
            // get locations
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            if (preset != null && preset.equals(ChameleonPreset.SWAMP)) {
                exportal.add(0.0d, 1.0d, 0.0d);
            }
            // interior teleport location
            Location indoor = null;
            COMPASS indirection = COMPASS.SOUTH;
            // exterior teleport location
            Location exdoor = null;
            COMPASS exdirection = rsc.getDirection();
            // interior portal
            Location inportal = null;
            ResultSetPortals rsp = new ResultSetPortals(plugin, id);
            rsp.resultSet();
            for (HashMap<String, String> map : rsp.getData()) {
                Location tmp_loc = TARDISStaticLocationGetters.getLocationFromDB(map.get("door_location"));
                COMPASS tmp_direction = COMPASS.valueOf(map.get("door_direction"));
                if (map.get("door_type").equals("1")) {
                    // clone it because we're going to change it!
                    inportal = tmp_loc.clone();
                    indirection = tmp_direction;
                    // adjust for teleport
                    int getx = tmp_loc.getBlockX();
                    int getz = tmp_loc.getBlockZ();
                    switch (indirection) {
                        case NORTH -> {
                            // z -ve
                            tmp_loc.setX(getx + 0.5);
                            tmp_loc.setZ(getz - 0.5);
                        }
                        case EAST -> {
                            // x +ve
                            tmp_loc.setX(getx + 1.5);
                            tmp_loc.setZ(getz + 0.5);
                        }
                        case SOUTH -> {
                            // z +ve
                            tmp_loc.setX(getx + 0.5);
                            tmp_loc.setZ(getz + 1.5);
                        }
                        // WEST
                        default -> {
                            // x -ve
                            tmp_loc.setX(getx - 0.5);
                            tmp_loc.setZ(getz + 0.5);
                        }
                    }
                    indoor = tmp_loc;
                } else {
                    exdoor = tmp_loc.clone();
                    // adjust for teleport
                    if (preset.usesArmourStand()) {
                        switch (exdirection) {
                            case NORTH_EAST -> exdoor.add(0, 0, 1);
                            case NORTH -> exdoor.add(0.5d, 0.0d, 1.0d);
                            case NORTH_WEST -> exdoor.add(1, 0, 1);
                            case WEST -> exdoor.add(1.0d, 0.0d, 0.5d);
                            case SOUTH_WEST -> exdoor.add(1, 0, -0.5);
                            case SOUTH -> exdoor.add(0.5d, 0.0d, -1.0d);
                            case SOUTH_EAST -> exdoor.add(-0.5, 0, 0);
                            default -> exdoor.add(-1.0d, 0.0d, 0.5d);
                        }
                    } else {
                        exdoor.setX(exdoor.getX() + 0.5);
                        exdoor.setZ(exdoor.getZ() + 0.5);
                    }
                }
            }
            if (plugin.getPresetBuilder().hasBlockBehind(inner, indirection)) {
                // set trackers
                TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                tp_in.setLocation(indoor);
                tp_in.setTardisId(id);
                tp_in.setDirection(indirection);
                tp_in.setAbandoned(abandoned);
                TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                tp_out.setLocation(exdoor);
                tp_out.setTardisId(id);
                tp_out.setDirection(exdirection);
                tp_out.setAbandoned(abandoned);
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    // players
                    uuids.forEach((u) -> plugin.getTrackerKeeper().getMover().add(u));
                }
                // locations
                if (tardis != null && preset != null && preset.hasPortal()) {
                    plugin.getTrackerKeeper().getPortals().put(exportal, tp_in);
                    if (preset.equals(ChameleonPreset.INVISIBLE) && plugin.getConfig().getBoolean("allow.3d_doors")) {
                        // remember door location
                        plugin.getTrackerKeeper().getInvisibleDoors().put(tardis.getUuid(), outer);
                    }
                }
                plugin.getTrackerKeeper().getPortals().put(inportal, tp_out);
                if (plugin.getConfig().getBoolean("police_box.view_interior") && !preset.usesArmourStand()) {
                    ConsoleSize consoleSize = (tardis == null) ? ConsoleSize.SMALL : tardis.getSchematic().getConsoleSize();
                    plugin.getTrackerKeeper().getCasters().put(uuid, new CastData(inportal, exportal, exdirection, tardis.getRotor(), consoleSize));
                    // get distance from door
                    Player player = plugin.getServer().getPlayer(uuid);
                    Location location = player.getLocation();
                    double distance = (location.getWorld() == exportal.getWorld()) ? location.distanceSquared(exportal) : 1; // or exdoor?
                    if (distance <= 9) {
                        // start casting
                        Capture capture = new Capture();
                        BlockData[][][] data = capture.captureInterior(inportal, (int) distance, tardis.getRotor(), consoleSize);
                        Cast cast = new Cast(plugin, exportal);
                        cast.castInterior(uuid, data);
                        if (capture.getRotorData().getFrame() != null) {
                            // get vector of rotor
                            cast.castRotor(capture.getRotorData().getFrame(), player, capture.getRotorData().getOffset(), rsc.getDirection());
                        }
                    }
                }
            }
        }
    }
}
