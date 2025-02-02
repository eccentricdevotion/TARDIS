/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.camera;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardisweepingangels.TARDISWeepingAngels;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDismountEvent;
import org.bukkit.persistence.PersistentDataType;

public class TARDISDismountListener implements Listener {

    private final TARDIS plugin;

    public TARDISDismountListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        EntityType riding = event.getEntity().getType();
        EntityType ridden = event.getDismounted().getType();
        if (plugin.getConfig().getBoolean("modules.weeping_angels") && ridden == EntityType.SKELETON && riding == EntityType.GUARDIAN) {
            if (event.getEntity().getPersistentDataContainer().has(TARDISWeepingAngels.SILENT, PersistentDataType.INTEGER)) {
                event.setCancelled(true);
            }
        }
        if (!(event.getDismounted() instanceof ArmorStand)) {
            return;
        }
        if (!(event.getEntity() instanceof Player player)) {
            return;
        }
        if (TARDISCameraTracker.SPECTATING.containsKey(player.getUniqueId()) || plugin.getTrackerKeeper().getJunkRelog().containsKey(player.getUniqueId())) {
            // get armour stand
            for (Entity e : player.getLocation().getWorld().getNearbyEntities(player.getLocation(), 1, 2, 1, (s) -> s.getType() == EntityType.ARMOR_STAND)) {
                if (e instanceof ArmorStand stand) {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> new TARDISCamera(plugin).stopViewing(player, stand), 2L);
                }
            }
        }
    }
}
