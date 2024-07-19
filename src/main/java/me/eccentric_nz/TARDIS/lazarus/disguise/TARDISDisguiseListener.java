/*
 * Copyright (C) 2024 eccentric_nz
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (location your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.lazarus.disguise;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.tardischunkgenerator.helpers.TARDISPacketListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;

import java.util.UUID;

public class TARDISDisguiseListener implements Listener {

    private final TARDIS plugin;

    public TARDISDisguiseListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // redisguise disguised players
                if (TARDISDisguiseTracker.DISGUISED_AS_MOB.containsKey(player.getUniqueId())) {
                    TARDISDisguiser.redisguise(player, world);
                } else {
                    // show other disguises to player
                    TARDISDisguiser.disguiseToPlayer(player, world);
                }
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            // show disguise to newly joined players
            disguiseToPlayer(event.getPlayer(), event.getPlayer().getWorld());
        }
        TARDISPacketListener.injectPlayer(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerRespawn(PlayerRespawnEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            // show disguise to newly joined players
            disguiseToPlayer(event.getPlayer(), event.getPlayer().getWorld());
        }
    }

    private void disguiseToPlayer(Player player, World world) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                TARDISDisguiser.disguiseToPlayer(player, world);
//                TARDISEPSDisguiser.disguiseToPlayer(player, world);
                TARDISArmourStandDisguiser.disguiseToPlayer(player, world);
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        UUID uuid = event.getPlayer().getUniqueId();
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            // stop tracking the disguise
            TARDISDisguiseTracker.DISGUISED_AS_MOB.remove(uuid);
            TARDISDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
            TARDISDisguiseTracker.ARCHED.remove(uuid);
        }
        TARDISPacketListener.removePlayer(event.getPlayer());
    }
}
