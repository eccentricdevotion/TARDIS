/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.enumeration.WorldManager;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;

/**
 * @author eccentric_nz
 */
public class TARDISGameModeSwitcher implements Listener {

	private final TARDIS plugin;

	public TARDISGameModeSwitcher(TARDIS plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
	public void onGameModeWorldChange(PlayerChangedWorldEvent event) {
		Player player = event.getPlayer();
		String world = player.getWorld().getName();
		if (plugin.getWorldManager().equals(WorldManager.NONE) && !TARDISPermission.hasPermission(player, "tardis.gamemode.bypass")) {
			// TARDIS is managing worlds so switch player GameMode if necessary
			try {
				GameMode gm = GameMode.valueOf(plugin.getPlanetsConfig().getString("planets." + world + ".gamemode"));
				player.setGameMode(gm);
			} catch (IllegalArgumentException e) {
				plugin.debug("Could not get GameMode for world change: '" + world + "'");
			}
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void onGameModeJoin(PlayerJoinEvent event) {
		Player player = event.getPlayer();
		String world = player.getWorld().getName();
		if (plugin.getWorldManager().equals(WorldManager.NONE) && !TARDISPermission.hasPermission(player, "tardis.gamemode.bypass")) {
			try {
				GameMode gm = GameMode.valueOf(plugin.getPlanetsConfig().getString("planets." + world + ".gamemode"));
				player.setGameMode(gm);
			} catch (IllegalArgumentException e) {
				plugin.debug("Could not get GameMode for world join: '" + world + "'");
			}
		}
	}
}
