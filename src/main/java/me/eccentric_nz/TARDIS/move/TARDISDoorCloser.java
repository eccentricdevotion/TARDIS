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
package me.eccentric_nz.TARDIS.move;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoorBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISDoorCloser {

    private final TARDIS plugin;
    private final UUID uuid;
    private final int id;

    public TARDISDoorCloser(TARDIS plugin, UUID uuid, int id) {
        this.plugin = plugin;
        this.uuid = uuid;
        this.id = id;
    }

    public void closeDoors() {
        // get door locations
        // outer
        final ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultset()) {
            close(rs.getOuterBlock(), true, rs.getOuterDirection());
            // inner
            if (!rs.getInnerBlock().getChunk().isLoaded()) {
                rs.getInnerBlock().getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    close(rs.getInnerBlock(), false, rs.getInnerDirection());
                }
            }, 5L);
        }
    }

    /**
     * Close the door.
     *
     * @param block the bottom door block
     * @param remove whether to clear the
     * @param dd the door direction
     */
    @SuppressWarnings("deprecation")
    private void close(Block block, boolean remove, COMPASS dd) {
        if (block.getType().equals(Material.IRON_DOOR_BLOCK) || block.getType().equals(Material.WOODEN_DOOR)) {
            byte door_data = block.getData();
            switch (dd) {
                case NORTH:
                    if (door_data == 7) {
                        block.setData((byte) 3, false);
                    }
                    break;
                case WEST:
                    if (door_data == 6) {
                        block.setData((byte) 2, false);
                    }
                    break;
                case SOUTH:
                    if (door_data == 5) {
                        block.setData((byte) 1, false);
                    }
                    break;
                default:
                    if (door_data == 4) {
                        block.setData((byte) 0, false);
                    }
                    break;
            }
            if (remove) {
                // get all companion UUIDs
                List<UUID> uuids = new ArrayList<UUID>();
                uuids.add(uuid);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    String[] companions = rs.getCompanions().split(":");
                    for (String c : companions) {
                        if (!c.isEmpty()) {
                            uuids.add(UUID.fromString(c));
                        }
                    }
                }
                // get locations
                // exterior portal (from current location)
                HashMap<String, Object> where_exportal = new HashMap<String, Object>();
                where_exportal.put("tardis_id", id);
                ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where_exportal);
                rsc.resultSet();
                Location exportal = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
                // interior portal
                TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, id);
                Location inportal = idl.getL();
                // unset trackers
                // players
                for (UUID u : uuids) {
                    if (plugin.getTrackerKeeper().getTrackMover().contains(u)) {
                        plugin.getTrackerKeeper().getTrackMover().remove(u);
                    }
                }
                // locations
                plugin.getTrackerKeeper().getTrackPortals().remove(exportal);
                plugin.getTrackerKeeper().getTrackPortals().remove(inportal);
            }
        }
    }
}
