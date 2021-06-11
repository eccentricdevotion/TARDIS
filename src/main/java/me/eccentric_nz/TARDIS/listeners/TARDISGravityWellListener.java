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

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetGravity;
import me.eccentric_nz.tardis.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisID;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import me.eccentric_nz.tardis.rooms.TARDISGravityWellRunnable;
import me.eccentric_nz.tardis.utility.TARDISVoidFall;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.EquipmentSlot;

import java.util.HashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * Air corridors projected by TARDISes had the option to use anti-gravity, allowing the occupant of the corridor to
 * float through the corridor instead of walk.
 *
 * @author eccentric_nz
 */
public class TARDISGravityWellListener implements Listener {

    private final TARDISPlugin plugin;
    private final HashMap<Double, Material> woolType = new HashMap<>();
    private final HashMap<Double, String> woolColour = new HashMap<>();

    public TARDISGravityWellListener(TARDISPlugin plugin) {
        this.plugin = plugin;
        woolType.put(0D, Material.PINK_WOOL);
        woolType.put(1D, Material.LIME_WOOL);
        woolType.put(2D, Material.BLACK_WOOL);
        woolType.put(3D, Material.PURPLE_WOOL);
        woolType.put(4D, Material.RED_WOOL);
        woolType.put(5D, Material.YELLOW_WOOL);
        woolColour.put(0D, "PINK");
        woolColour.put(1D, "LIME");
        woolColour.put(2D, "BLACK");
        woolColour.put(3D, "PURPLE");
        woolColour.put(4D, "RED");
        woolColour.put(5D, "YELLOW");
    }

    /**
     * Listens for a player walking over a Gravity Well location. If the block the player is on is contained in the
     * gravityUpList then the player is transported up.
     *
     * @param event a player moving
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        World world = Objects.requireNonNull(event.getTo()).getWorld();
        Location l = new Location(world, event.getTo().getBlockX(), event.getTo().getBlockY() - 1, event.getTo().getBlockZ(), 0.0F, 0.0F);
        String loc = l.toString();
        Double[] values;
        double end;
        double vel;
        if (plugin.getGeneralKeeper().getGravityUpList().containsKey(loc)) {
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.getGeneralKeeper().getGravityUpList().get(loc);
            end = l.getY() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 1);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.getGeneralKeeper().getGravityNorthList().containsKey(loc)) {
            // move player north
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.getGeneralKeeper().getGravityNorthList().get(loc);
            end = l.getZ() - values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 2);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.getGeneralKeeper().getGravityWestList().containsKey(loc)) {
            // move player west
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.getGeneralKeeper().getGravityWestList().get(loc);
            end = l.getX() - values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 3);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.getGeneralKeeper().getGravitySouthList().containsKey(loc)) {
            // move player south
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.getGeneralKeeper().getGravitySouthList().get(loc);
            end = l.getZ() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 4);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.getGeneralKeeper().getGravityEastList().containsKey(loc)) {
            // move player east
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.getGeneralKeeper().getGravityEastList().get(loc);
            end = l.getX() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 5);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
        }
    }

    /**
     * Listens for a player falling onto a Gravity Well location. If the block the player lands on is contained in the
     * gravityDownList then the player receives no fall damage.
     *
     * @param e an entity taking damage
     */
    @EventHandler(ignoreCancelled = true)
    public void onEntityDamage(EntityDamageEvent e) {
        Entity ent = e.getEntity();
        if ((ent instanceof Player p)) {
            Location l = ent.getLocation();
            switch (e.getCause()) {
                case FALL:
                    l.setX(l.getBlockX());
                    l.setY(l.getBlockY() - 1);
                    l.setZ(l.getBlockZ());
                    l.setPitch(0.0F);
                    l.setYaw(0.0F);
                    String loc = l.toString();
                    if (plugin.getGeneralKeeper().getGravityDownList().contains(loc)) {
                        e.setCancelled(true);
                    }
                    break;
                case VOID:
                    if (l.getBlockY() < 1 && plugin.getUtils().inTARDISWorld(p)) {
                        if (Objects.equals(plugin.getConfig().getString("preferences.vortex_fall"), "kill")) {
                            p.setHealth(0);
                        } else {
                            e.setCancelled(true);
                            new TARDISVoidFall(plugin).teleport(p);
                        }
                    }
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Listens for a player clicking a wool block after running the /tardisgravity command. If the block is the right
     * type, it is added or removed from the database.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onWoolInteract(PlayerInteractEvent event) {
        if (event.getHand() == null || event.getHand().equals(EquipmentSlot.OFF_HAND)) {
            return;
        }
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (plugin.getTrackerKeeper().getGravity().containsKey(uuid)) {
            Double[] values = plugin.getTrackerKeeper().getGravity().get(uuid);
            Block b = event.getClickedBlock();
            if (b != null) {
                // get tardis_id
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(player.getUniqueId().toString())) {
                    TARDISMessage.send(player, "NOT_A_TIMELORD");
                    return;
                }
                int id = rs.getTardisId();
                String loc = b.getLocation().toString();
                if (values[0] == 6) {
                    // find record and delete it
                    HashMap<String, Object> whereg = new HashMap<>();
                    whereg.put("tardis_id", id);
                    whereg.put("location", loc);
                    ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, false);
                    if (rsg.resultSet()) {
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("g_id", rsg.getGravityId());
                        if (plugin.getQueryFactory().doSyncDelete("gravity_well", whered)) {
                            switch (rsg.getDirection()) {
                                case 1 -> plugin.getGeneralKeeper().getGravityUpList().remove(loc);
                                case 2 -> plugin.getGeneralKeeper().getGravityNorthList().remove(loc);
                                case 3 -> plugin.getGeneralKeeper().getGravityWestList().remove(loc);
                                case 4 -> plugin.getGeneralKeeper().getGravitySouthList().remove(loc);
                                case 5 -> plugin.getGeneralKeeper().getGravityEastList().remove(loc);
                                default -> plugin.getGeneralKeeper().getGravityDownList().remove(loc);
                            }
                            // set the floor block to the player's preferred floor block
                            Material floor = Material.LIGHT_GRAY_WOOL;
                            ResultSetPlayerPrefs rspp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                            if (rspp.resultSet()) {
                                floor = Material.valueOf(rspp.getFloor());
                            }
                            b.setBlockData(floor.createBlockData(), true);
                            TARDISMessage.send(player, "GRAVITY_REMOVED");
                        }
                    } else {
                        TARDISMessage.send(player, "GRAVITY_NOT_FOUND");
                    }
                } else {
                    // check the wool block is the right colour
                    Material bit = woolType.get(values[0]);
                    if (!b.getType().equals(bit)) {
                        TARDISMessage.send(player, "GRAVITY_COLOUR", woolColour.get(values[0]) + ".");
                        return;
                    }
                    // add a record
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("tardis_id", id);
                    set.put("location", loc);
                    set.put("direction", values[0].intValue());
                    set.put("distance", values[1].intValue());
                    set.put("velocity", values[2]);
                    plugin.getQueryFactory().doInsert("gravity_well", set);
                    // add it to the block list
                    String dir;
                    switch (values[0].intValue()) {
                        case 1 -> {
                            plugin.getGeneralKeeper().getGravityUpList().put(loc, values);
                            dir = "UP";
                        }
                        case 2 -> {
                            plugin.getGeneralKeeper().getGravityNorthList().put(loc, values);
                            dir = "NORTH";
                        }
                        case 3 -> {
                            plugin.getGeneralKeeper().getGravityWestList().put(loc, values);
                            dir = "WEST";
                        }
                        case 4 -> {
                            plugin.getGeneralKeeper().getGravitySouthList().put(loc, values);
                            dir = "SOUTH";
                        }
                        case 5 -> {
                            plugin.getGeneralKeeper().getGravityEastList().put(loc, values);
                            dir = "EAST";
                        }
                        default -> {
                            plugin.getGeneralKeeper().getGravityDownList().add(loc);
                            dir = "DOWN";
                        }
                    }
                    TARDISMessage.send(player, "GRAVITY_SET", dir);
                }
            }
            plugin.getTrackerKeeper().getGravity().remove(uuid);
        }
    }
}
