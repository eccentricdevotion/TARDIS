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
package me.eccentric_nz.TARDIS.doors;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Tag;
import org.bukkit.block.Block;
import org.bukkit.block.data.Openable;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class DoorCloserAction {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public DoorCloserAction(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void closeDoors() {
        // get door locations
        ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultSet()) {
            close(rs.getOuterBlock(), rs.getInnerBlock().getLocation());
            // inner
            if (!rs.getInnerBlock().getChunk().isLoaded()) {
                rs.getInnerBlock().getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> close(rs.getInnerBlock(), null), 5L);
        }
    }

    /**
     * Close the door.
     *
     * @param block    the bottom door block
     * @param inportal the location of the interior door portal
     */
    private void close(Block block, Location inportal) {
        if (block != null && Tag.DOORS.isTagged(block.getType())) {
            Openable closeable = (Openable) block.getBlockData();
            closeable.setOpen(false);
            block.setBlockData(closeable, true);
        }
        if (inportal != null && plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
            ChameleonPreset preset = null;
            // get all companion UUIDs
            List<UUID> uuids = new ArrayList<>();
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
            if (rs.resultSet()) {
                preset = rs.getTardis().getPreset();
                if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                    if (rs.getTardis().getCompanions().equalsIgnoreCase("everyone")) {
                        for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                            uuids.add(p.getUniqueId());
                        }
                    } else {
                        String[] companions = rs.getTardis().getCompanions().split(":");
                        for (String c : companions) {
                            if (!c.isEmpty()) {
                                uuids.add(UUID.fromString(c));
                            }
                        }
                        uuids.add(uuid);
                    }
                }
            }
            // get locations
            // exterior portal (from current location)
            ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
            rsc.resultSet();
            Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            if (rs.getTardis().getPreset().equals(ChameleonPreset.SWAMP)) {
                exportal.add(0.0d, 1.0d, 0.0d);
            }
            // unset trackers
            if (!plugin.getConfig().getBoolean("preferences.open_door_policy")) {
                // players
                uuids.forEach((u) -> plugin.getTrackerKeeper().getMover().remove(u));
            }
            // locations
            plugin.getTrackerKeeper().getPortals().remove(exportal);
            plugin.getTrackerKeeper().getPortals().remove(inportal);
            if (plugin.getConfig().getBoolean("police_box.view_interior") && (preset != null && !preset.usesArmourStand())) {
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
        }
    }
}
