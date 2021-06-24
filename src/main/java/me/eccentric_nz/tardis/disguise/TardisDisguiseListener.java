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
package me.eccentric_nz.tardis.disguise;

import me.eccentric_nz.tardis.TardisPlugin;
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

public class TardisDisguiseListener implements Listener {

    private final TardisPlugin plugin;

    public TardisDisguiseListener(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerWorldChange(PlayerChangedWorldEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            Player player = event.getPlayer();
            World world = player.getWorld();
            Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                // redisguise disguised players
                if (TardisDisguiseTracker.DISGUISED_AS_MOB.containsKey(player.getUniqueId())) {
                    TardisDisguiser.redisguise(player, world);
                } else {
                    // show other disguises to player
                    TardisDisguiser.disguiseToPlayer(player, world);
                }
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            // show disguise to newly joined players
            disguiseToPlayer(event.getPlayer(), event.getPlayer().getWorld());
            TardisPacketListener.injectPlayer(event.getPlayer());
        }
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
                TardisDisguiser.disguiseToPlayer(player, world);
                TardisEpsDisguiser.disguiseToPlayer(player, world);
                TardisArmorStandDisguiser.disguiseToPlayer(player, world);
            }, 5L);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerQuit(PlayerQuitEvent event) {
        if (!plugin.getServer().getPluginManager().isPluginEnabled("LibsDisguises")) {
            UUID uuid = event.getPlayer().getUniqueId();
            // stop tracking the disguise
            TardisDisguiseTracker.DISGUISED_AS_MOB.remove(uuid);
            TardisDisguiseTracker.DISGUISED_AS_PLAYER.remove(uuid);
            TardisDisguiseTracker.ARCHED.remove(uuid);
            TardisPacketListener.removePlayer(event.getPlayer());
        }
    }
}
