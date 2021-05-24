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
package me.eccentric_nz.tardis.commands.tardis;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.builders.TARDISInteriorPostioning;
import me.eccentric_nz.tardis.builders.TARDISTIPSData;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.desktop.TARDISThemeInventory;
import me.eccentric_nz.tardis.desktop.TARDISUpgradeData;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISUpgradeCommand {

	private final TARDISPlugin plugin;

	TARDISUpgradeCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	boolean openUpgradeGUI(Player player) {
		if (!TARDISPermission.hasPermission(player, "tardis.upgrade")) {
			TARDISMessage.send(player, "NO_PERM_UPGRADE");
			return true;
		}
		// they must have an existing tardis
		HashMap<String, Object> where = new HashMap<>();
		where.put("uuid", player.getUniqueId().toString());
		ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
		if (!rs.resultSet()) {
			TARDISMessage.send(player, "NO_TARDIS");
			return false;
		}
		TARDIS tardis = rs.getTardis();
		// console must in a tardis world
		if (!plugin.getUtils().canGrowRooms(tardis.getChunk())) {
			TARDISMessage.send(player, "UPGRADE_ABORT_WORLD");
			return true;
		}
		// it must be their own tardis
		boolean own;
		Location pl = player.getLocation();
		String current_world = pl.getWorld().getName();
		String[] split = tardis.getChunk().split(":");
		if (plugin.getConfig().getBoolean("creation.default_world")) {
			if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && TARDISPermission.hasPermission(player, "tardis.create_world")) {
				own = (current_world.equals(split[0]));
			} else {
				// get if player is in TIPS area for their tardis
				TARDISInteriorPostioning tintpos = new TARDISInteriorPostioning(plugin);
				TARDISTIPSData pos = tintpos.getTIPSData(tardis.getTIPS());
				own = (pl.getBlockX() > pos.getMinX() && pl.getBlockZ() > pos.getMinZ() && pl.getBlockX() < pos.getMaxX() && pl.getBlockZ() < pos.getMaxZ());
			}
		} else {
			own = (current_world.equals(split[0]));
		}
		if (!own) {
			TARDISMessage.send(player, "NOT_OWNER");
			return true;
		}
		// get player's current console
		Schematic current_console = tardis.getSchematic();
		int level = tardis.getArtronLevel();
		TARDISUpgradeData tud = new TARDISUpgradeData();
		tud.setPrevious(current_console);
		tud.setLevel(level);
		plugin.getTrackerKeeper().getUpgrades().put(player.getUniqueId(), tud);
		// open the upgrade menu
		ItemStack[] consoles = new TARDISThemeInventory(plugin, player, current_console.getPermission(), level).getMenu();
		Inventory upg = plugin.getServer().createInventory(player, 54, ChatColor.DARK_RED + "tardis Upgrade Menu");
		upg.setContents(consoles);
		player.openInventory(upg);
		return true;
	}
}
