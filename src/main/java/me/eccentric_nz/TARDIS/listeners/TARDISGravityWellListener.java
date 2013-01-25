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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.listeners;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetGravity;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.TARDISDatabase;
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
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISGravityWellListener implements Listener {

    private final TARDIS plugin;
    TARDISDatabase service = TARDISDatabase.getInstance();

    public TARDISGravityWellListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onInteract(PlayerMoveEvent event) {
        final Player player = event.getPlayer();
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("player", player.getName());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
        if (rst.resultSet()) {
            int tid = rst.getTardis_id();
            HashMap<String, Object> whereg = new HashMap<String, Object>();
            whereg.put("tardis_id", tid);
            ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, false);
            if (rsg.resultSet()) {
                World world = event.getTo().getWorld();
                Location loc = new Location(world, event.getTo().getX(), event.getTo().getY() - .2, event.getTo().getZ());
                Block b = loc.getBlock();
                double endy;
                int id = b.getTypeId();
                byte data = b.getData();
                int x = loc.getBlockX();
                int z = loc.getBlockZ();
                if (id == 35 && data == (byte) 5 && (loc.getBlockX() == rsg.getUpx() && loc.getBlockZ() == rsg.getUpz())) {
                    endy = loc.getY() + 10;
                    TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, 0.5D, endy, x, z);
                    int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
                    runnable.setTask(task);
                }
//                if (id == 0) {
//                    if (loc.getBlockX() == rsg.getDownx() && loc.getBlockZ() == rsg.getDownz()) {
//                        endy = loc.getY() - 10;
//                        TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, -0.5D, endy, x, z);
//                        int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
//                        runnable.setTask(task);
//                    }
//                }
            }
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent e) {
        if (e.getCause() == EntityDamageEvent.DamageCause.FALL) {
            Entity ent = e.getEntity();
            if ((ent instanceof Player)) {
                Player player = (Player) ent;
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("player", player.getName());
                ResultSetTravellers rst = new ResultSetTravellers(plugin, where, false);
                if (rst.resultSet()) {
                    int tid = rst.getTardis_id();
                    HashMap<String, Object> whereg = new HashMap<String, Object>();
                    whereg.put("tardis_id", tid);
                    ResultSetGravity rsg = new ResultSetGravity(plugin, whereg, false);
                    if (rsg.resultSet()) {
                        Location loc = ent.getLocation();
                        if (loc.getBlockX() == rsg.getDownx() && loc.getBlockZ() == rsg.getDownz()) {
                            e.setCancelled(true);
                        }
                    }
                }
            }
        }
    }
}