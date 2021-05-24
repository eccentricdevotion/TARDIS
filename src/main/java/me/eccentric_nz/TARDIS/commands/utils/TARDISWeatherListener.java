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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.tardis.commands.utils;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.control.TARDISAtmosphericExcitation;
import me.eccentric_nz.tardis.database.data.TARDIS;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTravellers;
import me.eccentric_nz.tardis.listeners.TARDISMenuListener;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class TARDISWeatherListener extends TARDISMenuListener implements Listener {

	private final TARDISPlugin plugin;

	public TARDISWeatherListener(TARDISPlugin plugin) {
		super(plugin);
		this.plugin = plugin;
	}

	@EventHandler(ignoreCancelled = true)
	public void onWeatherMenuInteract(InventoryClickEvent event) {
		InventoryView view = event.getView();
		String name = view.getTitle();
		if (name.equals(ChatColor.DARK_RED + "tardis Weather Menu")) {
			event.setCancelled(true);
			int slot = event.getRawSlot();
			Player player = (Player) event.getWhoClicked();
			if (slot >= 0 && slot < 9) {
				ItemStack is = view.getItem(slot);
				if (is != null) {
					// get the tardis the player is in
					HashMap<String, Object> wheres = new HashMap<>();
					wheres.put("uuid", player.getUniqueId().toString());
					ResultSetTravellers rst = new ResultSetTravellers(plugin, wheres, false);
					if (rst.resultSet()) {
						int id = rst.getTardisId();
						HashMap<String, Object> where = new HashMap<>();
						where.put("tardis_id", id);
						ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
						if (rs.resultSet()) {
							TARDIS tardis = rs.getTardis();
							// check they initialised
							if (!tardis.isTardisInit()) {
								TARDISMessage.send(player, "ENERGY_NO_INIT");
								return;
							}
							if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPowered() && slot != 6) {
								TARDISMessage.send(player, "POWER_DOWN");
								return;
							}
							if (!tardis.isHandbrakeOn()) {
								TARDISMessage.send(player, "NOT_WHILE_TRAVELLING");
								return;
							}
							// get current location
							HashMap<String, Object> wherec = new HashMap<>();
							wherec.put("tardis_id", tardis.getTardisId());
							ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
							if (!rsc.resultSet()) {
								TARDISMessage.send(player, "CURRENT_NOT_FOUND");
								close(player);
							}
							switch (slot) {
								case 0:
									// clear / sun
									if (TARDISPermission.hasPermission(player, "tardis.weather.clear")) {
										TARDISWeather.setClear(rsc.getWorld());
										TARDISMessage.send(player, "WEATHER_SET", "clear");
									} else {
										TARDISMessage.send(player, "NO_PERMS");
									}
									close(player);
									break;
								case 1:
									// rain
									if (TARDISPermission.hasPermission(player, "tardis.weather.rain")) {
										TARDISWeather.setRain(rsc.getWorld());
										TARDISMessage.send(player, "WEATHER_SET", "rain");
									} else {
										TARDISMessage.send(player, "NO_PERMS");
									}
									close(player);
									break;
								case 2:
									// thunderstorm
									if (TARDISPermission.hasPermission(player, "tardis.weather.thunder")) {
										TARDISWeather.setThunder(rsc.getWorld());
										TARDISMessage.send(player, "WEATHER_SET", "thunder");
									} else {
										TARDISMessage.send(player, "NO_PERMS");
									}
									close(player);
									break;
								case 5:
									// atmospheric excitation
									if (plugin.getTrackerKeeper().getExcitation().contains(player.getUniqueId())) {
										TARDISMessage.send(player, "CMD_EXCITE");
										return;
									}
									new TARDISAtmosphericExcitation(plugin).excite(tardis.getTardisId(), player);
									plugin.getTrackerKeeper().getExcitation().add(player.getUniqueId());
									close(player);
									break;
								case 8:
									// close
									close(player);
									break;
								default:
									break;
							}
						}
					}
				}
			}
		}
	}
}
