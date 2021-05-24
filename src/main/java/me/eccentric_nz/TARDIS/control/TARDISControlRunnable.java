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
package me.eccentric_nz.tardis.control;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.database.resultset.ResultSetConsole;
import me.eccentric_nz.tardis.database.resultset.ResultSetOccupied;
import me.eccentric_nz.tardis.enumeration.WorldManager;
import me.eccentric_nz.tardis.planets.TARDISAliasResolver;
import org.bukkit.ChatColor;
import org.bukkit.Tag;
import org.bukkit.block.Sign;

/**
 * @author eccentric_nz
 */
public class TARDISControlRunnable implements Runnable {

	private final TARDISPlugin plugin;
	private int modulo = 0;

	public TARDISControlRunnable(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public void run() {
		// get occupied TARDISes
		ResultSetOccupied rso = new ResultSetOccupied(plugin);
		rso.resultSet();
		for (int id : rso.getData()) {
			if (!plugin.getTrackerKeeper().getKeyboard().contains(id)) {
				ResultSetConsole rsc = new ResultSetConsole(plugin, id);
				if (modulo % 2 == 0) {// get the location data
					if (rsc.locationData()) {
						if (Tag.SIGNS.isTagged(rsc.getSign().getType())) {
							// get the sign
							Sign sign = (Sign) rsc.getSign().getState();
							// update the data
							if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
								sign.setLine(0, ChatColor.DARK_PURPLE + "Drifting in the");
								sign.setLine(1, ChatColor.DARK_PURPLE + "time vortex...");
								sign.setLine(2, "");
							} else {
								String worldname = (rsc.getWorld() != null) ? TARDISAliasResolver.getWorldAlias(rsc.getWorld()) : "";
								if (plugin.getWorldManager().equals(WorldManager.MULTIVERSE) && !worldname.equals("")) {
									worldname = plugin.getMVHelper().getAlias(worldname);
								}
								sign.setLine(0, ChatColor.DARK_PURPLE + worldname);
								sign.setLine(1, ChatColor.BLACK + rsc.getLocation());
								sign.setLine(2, ChatColor.BLACK + rsc.getBiome());
							}
							sign.setLine(3, ChatColor.BLUE + rsc.getPreset());
							sign.update();
						}
					}
				} else {// get the artron data
					if (rsc.artronData()) {
						int fc = plugin.getArtronConfig().getInt("full_charge");
						int current_level = rsc.getArtronLevel();
						int percent = Math.round((current_level * 100.0f) / fc);
						if (Tag.SIGNS.isTagged(rsc.getSign().getType())) {
							// get the sign
							Sign sign = (Sign) rsc.getSign().getState();
							// update the data
							sign.setLine(0, ChatColor.BLACK + plugin.getLanguage().getString("ARTRON_DISPLAY"));
							sign.setLine(1, ChatColor.AQUA + plugin.getLanguage().getString("ARTRON_MAX") + ":" + plugin.getArtronConfig().getInt("full_charge"));
							sign.setLine(2, ChatColor.GREEN + plugin.getLanguage().getString("ARTRON_REMAINING") + ":" + current_level);
							sign.setLine(3, ChatColor.LIGHT_PURPLE + plugin.getLanguage().getString("ARTRON_PERCENT") + ":" + percent + "%");
							sign.update();
						}
					}
				}
			}
		}
		modulo++;
		if (modulo == 2) {
			modulo = 0;
		}
	}
}
