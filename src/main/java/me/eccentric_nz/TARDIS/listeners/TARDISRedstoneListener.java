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
import me.eccentric_nz.tardis.sonic.actions.TARDISSonicRedstone;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockRedstoneEvent;

/**
 * @author eccentric_nz
 */
public class TARDISRedstoneListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISRedstoneListener(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onRedstoneChange(BlockRedstoneEvent event) {
		String block = event.getBlock().getLocation().toString();
		if (plugin.getGeneralKeeper().getSonicWires().contains(block) || plugin.getGeneralKeeper().getSonicLamps().contains(block) || plugin.getGeneralKeeper().getSonicRails().contains(block)) {
			event.setNewCurrent(event.getOldCurrent());
		}
	}

	@EventHandler(ignoreCancelled = true)
	public void onPistonRetract(BlockPistonRetractEvent event) {
		String block = event.getBlock().getLocation().toString();
		if (plugin.getGeneralKeeper().getSonicPistons().contains(block)) {
			event.setCancelled(true);
			TARDISSonicRedstone.setExtension(plugin, event.getBlock());
		}
	}
}
