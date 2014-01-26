/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Vehicle;
import org.bukkit.entity.minecart.StorageMinecart;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMinecartListener implements Listener {

    private final TARDIS plugin;
    private final List<Integer> rails = new ArrayList<Integer>();
    private final List<BlockFace> faces = new ArrayList<BlockFace>();

    public TARDISMinecartListener(TARDIS plugin) {
        this.plugin = plugin;
        this.rails.add(27);
        this.rails.add(28);
        this.rails.add(66);
        this.rails.add(157);
        this.faces.add(BlockFace.NORTH);
        this.faces.add(BlockFace.SOUTH);
        this.faces.add(BlockFace.EAST);
        this.faces.add(BlockFace.WEST);
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        if (event.getVehicle() instanceof StorageMinecart) {
            Block b = event.getBlock();
            Material mat = b.getType();
            if (mat.equals(Material.IRON_DOOR_BLOCK) || mat.equals(Material.FENCE)) {
                Vehicle minecart = event.getVehicle();
                ItemStack[] inv = ((StorageMinecart) minecart).getInventory().getContents();
                String[] data = null;
                String p = "";
                int id = 0;
                COMPASS d = COMPASS.SOUTH;
                Location block_loc = b.getLocation();
                String bw = block_loc.getWorld().getName();
                int bx = block_loc.getBlockX();
                int by = block_loc.getBlockY();
                int bz = block_loc.getBlockZ();
                String db_loc = bw + ":" + bx + ":" + by + ":" + bz;
                switch (mat) {
                    case IRON_DOOR_BLOCK: // is it a TARDIS door?
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("door_location", db_loc);
                        where.put("door_type", 0);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                        if (rsd.resultSet()) {
                            if (rsd.isLocked()) {
                                return;
                            }
                            // get RAIL room location
                            id = rsd.getTardis_id();
                            HashMap<String, Object> whereid = new HashMap<String, Object>();
                            whereid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false);
                            if (rs.resultSet() && !plugin.trackMinecart.contains(Integer.valueOf(id))) {
                                data = rs.getRail().split(":");
                                p = rs.getOwner();
                                plugin.trackMinecart.add(Integer.valueOf(id));
                            }
                        }
                        break;
                    case FENCE: // is it a RAIL room fence?
                        // get police box location
                        HashMap<String, Object> wherep = new HashMap<String, Object>();
                        wherep.put("rail", db_loc);
                        ResultSetTardis rsp = new ResultSetTardis(plugin, wherep, "", false);
                        if (rsp.resultSet()) {
                            p = rsp.getOwner();
                            id = rsp.getTardis_id();
                            HashMap<String, Object> whereinner = new HashMap<String, Object>();
                            whereinner.put("tardis_id", id);
                            whereinner.put("door_type", 1);
                            ResultSetDoors rsdinner = new ResultSetDoors(plugin, whereinner, false);
                            if (rsdinner.resultSet() && rsdinner.isLocked()) {
                                return;
                            }
                            HashMap<String, Object> whered = new HashMap<String, Object>();
                            whered.put("tardis_id", id);
                            whered.put("door_type", 0);
                            ResultSetDoors rspb = new ResultSetDoors(plugin, whered, false);
                            if (rspb.resultSet()) {
                                data = rspb.getDoor_location().split(":");
                                d = switchDirection(rspb.getDoor_direction());
                                plugin.trackMinecart.remove(Integer.valueOf(id));
                            }
                        }
                        break;
                    default:
                        break;
                }
                if (data != null) {
                    if (plugin.pm.isPluginEnabled("Multiverse-Inventories")) {
                        if (!plugin.tmic.checkMVI(bw, data[0])) {
                            if (!p.isEmpty() && plugin.getServer().getPlayer(p).isOnline()) {
                                plugin.getServer().getPlayer(p).sendMessage(plugin.pluginName + "You cannot use minecarts from " + bw + " to " + data[0] + ".");
                            }
                            plugin.trackMinecart.remove(Integer.valueOf(id));
                            return;
                        }
                    }
                    World w = plugin.getServer().getWorld(data[0]);
                    int x = plugin.utils.parseInt(data[1]);
                    int y = plugin.utils.parseInt(data[2]);
                    int z = plugin.utils.parseInt(data[3]);
                    Location in_out = new Location(w, x, y, z);
                    if (mat.equals(Material.IRON_DOOR_BLOCK)) {
                        d = getDirection(in_out);
                    }
                    teleportMinecart(minecart, in_out, d, inv);
                }
            }
        }
    }

    private void teleportMinecart(Vehicle minecart, Location targetLocation, COMPASS d, ItemStack[] inv) {
        // search for minecart tracks around the target waypoint
        Location trackLocation = findTrack(targetLocation);
        if (trackLocation == null) {
            return;
        }
        // get minecart's speed
        double speed = minecart.getVelocity().length();
        // simulate teleport minecart...
        Chunk thisChunk = trackLocation.getChunk();
        while (!thisChunk.isLoaded()) {
            thisChunk.load();
        }
        minecart.remove();
        //trackLocation.setY(trackLocation.getY() - 1);
        Entity e = trackLocation.getWorld().spawnEntity(trackLocation, EntityType.MINECART_CHEST);
        StorageMinecart smc = (StorageMinecart) e;
        smc.getInventory().setContents(inv);
        // calculate new velocity
        switch (d) {
            case NORTH:
                e.setVelocity(new Vector(0, 0, -speed));
                break;
            case SOUTH:
                e.setVelocity(new Vector(0, 0, speed));
                break;
            case WEST:
                e.setVelocity(new Vector(-speed, 0, 0));
                break;
            default:
                e.setVelocity(new Vector(speed, 0, 0));
                break;
        }
    }

    public Location findTrack(Location center) {
        Block centerBlock = center.getBlock();
        Block block;
        for (BlockFace f : faces) {
            block = centerBlock.getRelative(f);
            if (isTrack(block)) {
                return block.getLocation();
            }
        }
        return null;
    }

    @SuppressWarnings("deprecation")
    public boolean isTrack(Block block) {
        return isTrack(block.getTypeId());
    }

    public boolean isTrack(int id) {
        return (rails.contains(Integer.valueOf(id)));
    }

    private COMPASS getDirection(Location l) {
//        COMPASS d = COMPASS.SOUTH;
        Block centerBlock = l.getBlock();
        Block block;
        for (BlockFace f : faces) {
            block = centerBlock.getRelative(f);
            if (isTrack(block)) {
                return COMPASS.valueOf(f.toString());
            }
        }
        return null;
    }

    private COMPASS switchDirection(COMPASS d) {
        switch (d) {
            case NORTH:
                return COMPASS.SOUTH;
            case SOUTH:
                return COMPASS.NORTH;
            case WEST:
                return COMPASS.EAST;
            default:
                return COMPASS.WEST;
        }
    }
}
