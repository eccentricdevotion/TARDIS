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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetGravity;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.rooms.TARDISGravityWellRunnable;
import org.bukkit.Location;
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

/**
 * Air corridors projected by TARDISes had the option to use anti-gravity,
 * allowing the occupant of the corridor to float through the corridor instead
 * of walk.
 *
 * @author eccentric_nz
 */
public class TARDISGravityWellListener implements Listener {

    private final TARDIS plugin;
    private final HashMap<Double, Byte> woolData = new HashMap<Double, Byte>();
    private final HashMap<Double, String> woolColour = new HashMap<Double, String>();

    public TARDISGravityWellListener(TARDIS plugin) {
        this.plugin = plugin;
        woolData.put(0D, (byte) 6);
        woolData.put(1D, (byte) 5);
        woolData.put(2D, (byte) 15);
        woolData.put(3D, (byte) 10);
        woolData.put(4D, (byte) 14);
        woolData.put(5D, (byte) 4);
        woolColour.put(0D, "PINK");
        woolColour.put(1D, "LIGHT GREEN");
        woolColour.put(2D, "BLACK");
        woolColour.put(3D, "PURPLE");
        woolColour.put(4D, "RED");
        woolColour.put(5D, "YELLOW");
    }

    /**
     * Listens for a player walking over a Gravity Well location. If the block
     * the player is on is contained in the gravityUpList then the player is
     * transported up.
     *
     * @param event a player moving
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        World world = event.getTo().getWorld();
        Location l = new Location(world, event.getTo().getBlockX(), event.getTo().getBlockY() - 1, event.getTo().getBlockZ(), 0.0F, 0.0F);
        String loc = l.toString();
        Double[] values;
        double end;
        double vel;
        if (plugin.gravityUpList.containsKey(loc)) {
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.gravityUpList.get(loc);
            end = l.getY() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 1);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.gravityNorthList.containsKey(loc)) {
            // move player north
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.gravityNorthList.get(loc);
            end = l.getZ() - values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 2);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.gravityWestList.containsKey(loc)) {
            // move player west
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.gravityWestList.get(loc);
            end = l.getX() - values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 3);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.gravitySouthList.containsKey(loc)) {
            // move player south
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.gravitySouthList.get(loc);
            end = l.getZ() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 4);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
            return;
        }
        if (plugin.gravityEastList.containsKey(loc)) {
            // move player east
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            values = plugin.gravityEastList.get(loc);
            end = l.getX() + values[1];
            vel = values[2];
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, vel, end, x, z, 5);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
        }
    }

    /**
     * Listens for a player falling onto a Gravity Well location. If the block
     * the player lands on is contained in the gravityDownList then the player
     * receives no fall damage.
     *
     * @param e an entity taking damage
     */
    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Entity ent = e.getEntity();
            if ((ent instanceof Player)) {
                Location l = ent.getLocation();
                l.setX(l.getBlockX());
                l.setY(l.getBlockY() - 1);
                l.setZ(l.getBlockZ());
                l.setPitch(0.0F);
                l.setYaw(0.0F);
                String loc = l.toString();
                if (plugin.gravityDownList.contains(loc)) {
                    e.setCancelled(true);
                }
            }
        }
    }

    /**
     * Listens for a player clicking a wool block after running the
     * /tardisgravity command. If the block is the right type, it is added or
     * removed from the database.
     *
     * @param event a player clicking a block
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onWoolInteract(PlayerInteractEvent event) {
        final Player player = event.getPlayer();
        String playerNameStr = player.getName();
        if (plugin.trackGravity.containsKey(playerNameStr)) {
            Double[] values = plugin.trackGravity.get(playerNameStr);
            Block b = event.getClickedBlock();
            if (b != null) {
                // get tardis_id
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("owner", player.getName());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (!rs.resultSet()) {
                    player.sendMessage(plugin.pluginName + MESSAGE.NOT_A_TIMELORD.getText());
                    return;
                }
                int id = rs.getTardis_id();
                QueryFactory qf = new QueryFactory(plugin);
                String loc = b.getLocation().toString();
                if (values[0] == 6) {
                    // find record and delete it
                    HashMap<String, Object> whereg = new HashMap<String, Object>();
                    whereg.put("tardis_id", id);
                    whereg.put("location", loc);
                    ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, false);
                    if (rsg.resultSet()) {
                        HashMap<String, Object> whered = new HashMap<String, Object>();
                        whered.put("g_id", rsg.getGravity_id());
                        if (qf.doSyncDelete("gravity_well", whered)) {
                            switch (rsg.getDirection()) {
                                case 1:
                                    plugin.gravityUpList.remove(loc);
                                    break;
                                case 2:
                                    plugin.gravityNorthList.remove(loc);
                                    break;
                                case 3:
                                    plugin.gravityWestList.remove(loc);
                                    break;
                                case 4:
                                    plugin.gravitySouthList.remove(loc);
                                    break;
                                case 5:
                                    plugin.gravityEastList.remove(loc);
                                    break;
                                default:
                                    plugin.gravityDownList.remove(loc);
                                    break;
                            }
                            // set the block to light grey wool
                            b.setTypeId(35);
                            b.setData((byte) 8, true);
                            player.sendMessage(plugin.pluginName + "The gravity block was removed successfully");
                        }
                    } else {
                        player.sendMessage(plugin.pluginName + "Could not find the gravity block in the database!");
                    }
                } else {
                    // check the wool block is the right colour
                    byte bit = woolData.get(values[0]);
                    if (b.getData() != bit) {
                        player.sendMessage(plugin.pluginName + "That wool block is the wrong colour, it should be " + woolColour.get(values[0]) + ".");
                        return;
                    }
                    // add a record
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("tardis_id", id);
                    set.put("location", loc);
                    set.put("direction", values[0].intValue());
                    set.put("distance", values[1].intValue());
                    set.put("velocity", values[2]);
                    qf.doInsert("gravity_well", set);
                    // add it to the block list
                    String dir;
                    switch (values[0].intValue()) {
                        case 1:
                            plugin.gravityUpList.put(loc, values);
                            dir = "UP";
                            break;
                        case 2:
                            plugin.gravityNorthList.put(loc, values);
                            dir = "NORTH";
                            break;
                        case 3:
                            plugin.gravityWestList.put(loc, values);
                            dir = "WEST";
                            break;
                        case 4:
                            plugin.gravitySouthList.put(loc, values);
                            dir = "SOUTH";
                            break;
                        case 5:
                            plugin.gravityEastList.put(loc, values);
                            dir = "EAST";
                            break;
                        default:
                            plugin.gravityDownList.add(loc);
                            dir = "DOWN";
                            break;
                    }
                    player.sendMessage(plugin.pluginName + "The wool was set to a gravity " + dir + " block!");
                }
            }
            plugin.trackGravity.remove(playerNameStr);
        }
    }
}
