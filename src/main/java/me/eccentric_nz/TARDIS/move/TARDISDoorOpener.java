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
import me.eccentric_nz.TARDIS.database.ResultSetPortals;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;

/**
 *
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
        final ResultSetDoorBlocks rs = new ResultSetDoorBlocks(plugin, id);
        if (rs.resultSet()) {
            open(rs.getInnerBlock(), rs.getOuterBlock(), true, rs.getInnerDirection());
            // outer
            if (!rs.getOuterBlock().getChunk().isLoaded()) {
                rs.getOuterBlock().getChunk().load();
            }
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    open(rs.getOuterBlock(), rs.getInnerBlock(), false, rs.getOuterDirection());
                }
            }, 5L);
        }
    }

    /**
     * Open the door.
     *
     */
    @SuppressWarnings("deprecation")
    private void open(Block block, Block other, boolean add, COMPASS dd) {
        if (plugin.getGeneralKeeper().getDoors().contains(block.getType())) {
            byte door_data = block.getData();
            switch (dd) {
                case NORTH:
                    if (door_data == 3) {
                        block.setData((byte) 7, false);
                    }
                    break;
                case WEST:
                    if (door_data == 2) {
                        block.setData((byte) 6, false);
                    }
                    break;
                case SOUTH:
                    if (door_data == 1) {
                        block.setData((byte) 5, true);
                    }
                    break;
                default:
                    if (door_data == 0) {
                        block.setData((byte) 4, false);
                    }
                    break;
            }
            if (add && plugin.getConfig().getBoolean("preferences.walk_in_tardis")) {
                // get all companion UUIDs
                List<UUID> uuids = new ArrayList<UUID>();
                uuids.add(uuid);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                PRESET preset = null;
                if (rs.resultSet()) {
                    preset = rs.getPreset();
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
                if (preset != null && preset.equals(PRESET.SWAMP)) {
                    exportal.add(0.0d, 1.0d, 0.0d);
                }
                // interior teleport location
                Location indoor = null;
                COMPASS indirection = COMPASS.SOUTH;
                // exterior teleport location
                Location exdoor = null;
                COMPASS exdirection = COMPASS.SOUTH;
                // interior portal
                Location inportal = null;
                ResultSetPortals rsp = new ResultSetPortals(plugin, id);
                rsp.resultSet();
                for (HashMap<String, String> map : rsp.getData()) {
                    Location tmp_loc = TARDISLocationGetters.getLocationFromDB(map.get("door_location"), 0.0f, 0.0f);
                    COMPASS tmp_direction = COMPASS.valueOf(map.get("door_direction"));
                    if (map.get("door_type").equals("1")) {
                        // clone it because we're going to change it!
                        inportal = tmp_loc.clone();
                        indirection = tmp_direction;
                        // adjust for teleport
                        int getx = tmp_loc.getBlockX();
                        int getz = tmp_loc.getBlockZ();
                        switch (indirection) {
                            case NORTH:
                                // z -ve
                                tmp_loc.setX(getx + 0.5);
                                tmp_loc.setZ(getz - 0.5);
                                break;
                            case EAST:
                                // x +ve
                                tmp_loc.setX(getx + 1.5);
                                tmp_loc.setZ(getz + 0.5);
                                break;
                            case SOUTH:
                                // z +ve
                                tmp_loc.setX(getx + 0.5);
                                tmp_loc.setZ(getz + 1.5);
                                break;
                            case WEST:
                                // x -ve
                                tmp_loc.setX(getx - 0.5);
                                tmp_loc.setZ(getz + 0.5);
                                break;
                        }
                        indoor = tmp_loc;
                    } else {
                        exdoor = tmp_loc.clone();
                        exdirection = COMPASS.valueOf(map.get("door_direction"));
                        // adjust for teleport
                        exdoor.setX(exdoor.getX() + 0.5);
                        exdoor.setZ(exdoor.getZ() + 0.5);
                    }
                }
                if (!checkForSpace(block, indirection)) {
                    // set trackers
                    TARDISTeleportLocation tp_in = new TARDISTeleportLocation();
                    tp_in.setLocation(indoor);
                    tp_in.setTardisId(id);
                    tp_in.setDirection(indirection);
                    TARDISTeleportLocation tp_out = new TARDISTeleportLocation();
                    tp_out.setLocation(exdoor);
                    tp_out.setTardisId(id);
                    tp_out.setDirection(exdirection);
                    // players
                    for (UUID u : uuids) {
                        // only add them if they're not there already!
                        if (!plugin.getTrackerKeeper().getMover().contains(u)) {
                            plugin.getTrackerKeeper().getMover().add(u);
                        }
                    }
                    // locations
                    if (preset != null && preset.hasPortal()) {
                        plugin.getTrackerKeeper().getPortals().put(exportal, tp_in);
                        if (preset.equals(PRESET.INVISIBLE) && plugin.getConfig().getBoolean("allow.3d_doors")) {
                            // remember door location
                            plugin.getTrackerKeeper().getInvisibleDoors().put(rs.getUuid(), other);
                        }
                    }
                    plugin.getTrackerKeeper().getPortals().put(inportal, tp_out);
                }
            }
        }
    }

    private boolean checkForSpace(Block b, COMPASS d) {
        BlockFace face = getOppositeFace(d);
        return (b.getRelative(face).getType().equals(Material.AIR)
                && b.getRelative(face).getRelative(BlockFace.UP).getType().equals(Material.AIR));
    }

    private BlockFace getOppositeFace(COMPASS d) {
        switch (d) {
            case SOUTH:
                return BlockFace.NORTH;
            case WEST:
                return BlockFace.EAST;
            case NORTH:
                return BlockFace.SOUTH;
            default:
                return BlockFace.WEST;
        }
    }
}
