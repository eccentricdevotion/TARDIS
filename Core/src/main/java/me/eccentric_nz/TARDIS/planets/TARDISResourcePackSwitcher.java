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
package me.eccentric_nz.TARDIS.planets;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author eccentric_nz
 */
public class TARDISResourcePackSwitcher implements Listener {

    private final TARDIS plugin;

    public TARDISResourcePackSwitcher(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onResourcePackWorldChange(PlayerChangedWorldEvent event) {
        Player player = event.getPlayer();
        String world = player.getWorld().getName();
        String from = plugin.getPlanetsConfig().getString("planets." + event.getFrom().getName() + ".resource_pack");
        if (from == null) {
            from = "default";
        }
        String path = plugin.getPlanetsConfig().getString("planets." + world + ".resource_pack");
        if (path == null) {
            path = "default";
        }
        // check to see whether the resource pack URL is actually different
        if (!from.equals(path)) {
            setResourcePack(player, path);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerJoin(PlayerJoinEvent event) {
        if (plugin.getPlanetsConfig().getBoolean("set_pack_on_join")) {
            Player player = event.getPlayer();
            String world = player.getWorld().getName();
            String path = plugin.getPlanetsConfig().getString("planets." + world + ".resource_pack");
            setResourcePack(player, path);
        }
    }

    private void setResourcePack(Player player, String path) {
        if (path == null || path.equalsIgnoreCase("default")) {
            player.removeResourcePacks();
        }
        if (player.isOnline()) {
            player.setResourcePack(path);
        }
    }
}
