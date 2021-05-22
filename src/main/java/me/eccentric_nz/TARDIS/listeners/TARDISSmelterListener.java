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
package me.eccentric_nz.tardis.listeners;

import me.eccentric_nz.tardis.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetSmelter;
import me.eccentric_nz.tardis.rooms.smelter.TARDISSmelterDrop;
import me.eccentric_nz.tardis.rooms.smelter.TARDISSmelterFuel;
import me.eccentric_nz.tardis.rooms.smelter.TARDISSmelterOre;
import me.eccentric_nz.tardis.sonic.TARDISSonicSorterListener;
import org.bukkit.block.Chest;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.List;

public class TARDISSmelterListener implements Listener {

	private final TARDIS plugin;

	public TARDISSmelterListener(TARDIS plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL)
	public void onSmelterDropChestClose(InventoryCloseEvent event) {
		Inventory inv = event.getInventory();
		InventoryHolder holder = inv.getHolder();
		if (holder instanceof Chest chest) {
			String loc = chest.getLocation().toString();
			// check is drop chest
			ResultSetSmelter rs = new ResultSetSmelter(plugin, loc);
			if (!rs.resultSet()) {
				return;
			}
			// sort contents
			TARDISSonicSorterListener.sortInventory(inv);
			// get fuel chests
			List<Chest> fuelChests = rs.getFuelChests();
			List<Chest> oreChests = rs.getOreChests();
			// process chest contents
			switch (rs.getType()) {
				case FUEL -> new TARDISSmelterFuel().processItems(inv, fuelChests);
				case SMELT -> new TARDISSmelterOre().processItems(inv, oreChests);
				default -> // DROP
						new TARDISSmelterDrop().processItems(inv, fuelChests, oreChests);
			}
		}
	}
}
