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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.TARDISGravityWellRunnable;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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

    public TARDISGravityWellListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Listens for a player walking over a Gravity Well location. If the block
     * the player is on is contained in the gravityUpList then the player is
     * transported up.
     */
    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMove(PlayerMoveEvent event) {
        World world = event.getTo().getWorld();
        Location l = new Location(world, event.getTo().getBlockX(), event.getTo().getBlockY() - 1, event.getTo().getBlockZ(), 0.0F, 0.0F);
        String loc = l.toString();
        if (plugin.gravityUpList.contains(loc)) {
            Player player = event.getPlayer();
            int x = l.getBlockX();
            int z = l.getBlockZ();
            double endy = l.getY() + 11;
            TARDISGravityWellRunnable runnable = new TARDISGravityWellRunnable(plugin, player, 0.5D, endy, x, z);
            int task = plugin.getServer().getScheduler().scheduleSyncRepeatingTask(plugin, runnable, 2L, 3L);
            runnable.setTask(task);
        }
    }

    /**
     * Listens for a player falling onto a Gravity Well location. If the block
     * the player lands on is contained in the gravityDownList then the player
     * receives no fall damage.
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
}
