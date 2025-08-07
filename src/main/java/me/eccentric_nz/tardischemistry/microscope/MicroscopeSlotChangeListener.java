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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;

public class MicroscopeSlotChangeListener implements Listener {

    private final TARDIS plugin;

    MicroscopeSlotChangeListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onSwapHand(PlayerSwapHandItemsEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onDropItem(PlayerDropItemEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onInventoryChange(PlayerItemHeldEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            event.setCancelled(true);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
            MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onGameModeChange(PlayerGameModeChangeEvent event) {
        Player player = event.getPlayer();
        if (MicroscopeUtils.STORED_STACKS.containsKey(player.getUniqueId())) {
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                player.getInventory().setItemInMainHand(MicroscopeUtils.STORED_STACKS.get(player.getUniqueId()));
                MicroscopeUtils.STORED_STACKS.remove(player.getUniqueId());
            }, 1L);
        }
    }
}
