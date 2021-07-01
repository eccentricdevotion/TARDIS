/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetDoors;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.utility.TardisMultiverseInventoriesChecker;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import me.eccentric_nz.tardis.utility.TardisPerWorldInventoryChecker;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Minecart;
import org.bukkit.entity.Vehicle;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.vehicle.VehicleBlockCollisionEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisMinecartListener implements Listener {

    private final TardisPlugin plugin;

    public TardisMinecartListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onVehicleBlockCollision(VehicleBlockCollisionEvent event) {
        Vehicle vehicle = event.getVehicle();
        if (vehicle instanceof Minecart && vehicle instanceof InventoryHolder) {
            Block block = event.getBlock();
            Material material = block.getType();
            if (Tag.DOORS.isTagged(material) || Tag.FENCES.isTagged(material)) {
                String[] data = null;
                UUID playerUuid = null;
                int id = 0;
                CardinalDirection d = CardinalDirection.SOUTH;
                Location block_loc = block.getLocation();
                String bw = Objects.requireNonNull(block_loc.getWorld()).getName();
                int bx = block_loc.getBlockX();
                int by = block_loc.getBlockY();
                int bz = block_loc.getBlockZ();
                String db_loc = bw + ":" + bx + ":" + by + ":" + bz;
                switch (material) {
                    case ACACIA_DOOR: // is it a TARDIS door?
                    case BIRCH_DOOR:
                    case CRIMSON_DOOR:
                    case DARK_OAK_DOOR:
                    case IRON_DOOR:
                    case JUNGLE_DOOR:
                    case OAK_DOOR:
                    case SPRUCE_DOOR:
                    case WARPED_DOOR:
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("door_location", db_loc);
                        where.put("door_type", 0);
                        ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
                        if (rsd.resultSet()) {
                            if (rsd.isLocked()) {
                                return;
                            }
                            // get RAIL room location
                            id = rsd.getTardisId();
                            HashMap<String, Object> whereid = new HashMap<>();
                            whereid.put("tardis_id", id);
                            ResultSetTardis rs = new ResultSetTardis(plugin, whereid, "", false, 0);
                            if (rs.resultSet() && !plugin.getTrackerKeeper().getMinecart().contains(id)) {
                                Tardis tardis = rs.getTardis();
                                data = tardis.getRail().split(":");
                                playerUuid = tardis.getUuid();
                                plugin.getTrackerKeeper().getMinecart().add(id);
                            }
                        }
                        break;
                    case ACACIA_FENCE: // is it a RAIL room fence?
                    case BIRCH_FENCE:
                    case DARK_OAK_FENCE:
                    case JUNGLE_FENCE:
                    case OAK_FENCE:
                    case SPRUCE_FENCE:
                        // get police box location
                        HashMap<String, Object> wherep = new HashMap<>();
                        wherep.put("rail", db_loc);
                        ResultSetTardis rsp = new ResultSetTardis(plugin, wherep, "", false, 0);
                        if (rsp.resultSet()) {
                            Tardis tardis = rsp.getTardis();
                            playerUuid = tardis.getUuid();
                            id = tardis.getTardisId();
                            HashMap<String, Object> whereinner = new HashMap<>();
                            whereinner.put("tardis_id", id);
                            whereinner.put("door_type", 1);
                            ResultSetDoors rsdinner = new ResultSetDoors(plugin, whereinner, false);
                            if (rsdinner.resultSet() && rsdinner.isLocked()) {
                                return;
                            }
                            HashMap<String, Object> whered = new HashMap<>();
                            whered.put("tardis_id", id);
                            whered.put("door_type", 0);
                            ResultSetDoors rspb = new ResultSetDoors(plugin, whered, false);
                            if (rspb.resultSet()) {
                                data = rspb.getDoorLocation().split(":");
                                d = switchDirection(rspb.getDoorDirection());
                                plugin.getTrackerKeeper().getMinecart().remove(id);
                            }
                        }
                        break;
                    default:
                        break;
                }
                if (data != null && data.length > 3) {
                    boolean shouldPrevent = switch (plugin.getInventoryManager()) {
                        case MULTIVERSE -> (!TardisMultiverseInventoriesChecker.checkWorldsCanShare(bw, data[0]));
                        case PER_WORLD -> (!TardisPerWorldInventoryChecker.checkWorldsCanShare(bw, data[0]));
                        default -> false;
                    };
                    if (shouldPrevent) {
                        if (playerUuid != null && Objects.requireNonNull(plugin.getServer().getPlayer(playerUuid)).isOnline()) {
                            TardisMessage.send(plugin.getServer().getPlayer(playerUuid), "WORLD_NO_CART", bw, data[0]);
                        }
                        plugin.getTrackerKeeper().getMinecart().remove(id);
                    } else {
                        World w = TardisAliasResolver.getWorldFromAlias(data[0]);
                        int x = TardisNumberParsers.parseInt(data[1]);
                        int y = TardisNumberParsers.parseInt(data[2]);
                        int z = TardisNumberParsers.parseInt(data[3]);
                        Location in_out = new Location(w, x, y, z);
                        if (Tag.DOORS.isTagged(material)) {
                            d = getDirection(in_out);
                            assert w != null;
                            w.getChunkAt(in_out).setForceLoaded(true);
                        } else {
                            assert w != null;
                            w.getChunkAt(in_out).setForceLoaded(false);
                        }
                        Inventory inventory = ((InventoryHolder) vehicle).getInventory();
                        ItemStack[] inv = Arrays.copyOf(inventory.getContents(), inventory.getSize());
                        inventory.clear();
                        teleportMinecart(vehicle, in_out, d, inv, vehicle.getType());
                    }
                }
            }
        }
    }

    private void teleportMinecart(Vehicle minecart, Location targetLocation, CardinalDirection d, ItemStack[] inv, EntityType cart) {
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
        // keep the chunk loaded until the cart has finished unloading
        thisChunk.setForceLoaded(true);
        // determine how long to keep it loaded (at a rate of approx 1 item per 8 ticks)
        long delay = 200L; // add an initial 10 second buffer
        for (ItemStack is : inv) {
            if (is != null) {
                delay += is.getAmount() * 8L;
            }
        }
        // start a delayed task to remove the chunk
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> thisChunk.setForceLoaded(false), delay);
        minecart.remove();
        Entity e = Objects.requireNonNull(trackLocation.getWorld()).spawnEntity(trackLocation, cart);
        InventoryHolder smc = (InventoryHolder) e;
        smc.getInventory().setContents(inv);
        // calculate new velocity
        switch (d) {
            case NORTH -> e.setVelocity(new Vector(0, 0, -speed));
            case SOUTH -> e.setVelocity(new Vector(0, 0, speed));
            case WEST -> e.setVelocity(new Vector(-speed, 0, 0));
            default -> e.setVelocity(new Vector(speed, 0, 0));
        }
    }

    private Location findTrack(Location center) {
        Block centerBlock = center.getBlock();
        Block block;
        for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
            block = centerBlock.getRelative(f);
            if (isTrack(block.getType())) {
                return block.getLocation();
            }
        }
        return null;
    }

    private boolean isTrack(Material mat) {
        return (Tag.RAILS.isTagged(mat));
    }

    private CardinalDirection getDirection(Location l) {
        Block centerBlock = l.getBlock();
        Block block;
        for (BlockFace f : plugin.getGeneralKeeper().getFaces()) {
            block = centerBlock.getRelative(f);
            if (isTrack(block.getType())) {
                return CardinalDirection.valueOf(f.toString());
            }
        }
        return null;
    }

    private CardinalDirection switchDirection(CardinalDirection d) {
        return switch (d) {
            case NORTH -> CardinalDirection.SOUTH;
            case SOUTH -> CardinalDirection.NORTH;
            case WEST -> CardinalDirection.EAST;
            default -> CardinalDirection.WEST;
        };
    }
}
