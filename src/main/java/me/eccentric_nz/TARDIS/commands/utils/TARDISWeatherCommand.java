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

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.commands.TARDISCompleter;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.enumeration.Weather;
import me.eccentric_nz.tardis.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.List;

public class TARDISWeatherCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

	private final TARDISPlugin plugin;
	private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("clear", "c", "rain", "r", "thunder", "t", "sun", "s");

	public TARDISWeatherCommand(TARDISPlugin plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(@NotNull CommandSender sender, Command cmd, @NotNull String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("tardisweather")) {
			if (args.length < 1) {
				TARDISMessage.send(sender, "TOO_FEW_ARGS");
				return true;
			}
			Player player;
			if (sender instanceof Player) {
				player = (Player) sender;
				Location location = player.getLocation();
				World world = location.getWorld();
				if (plugin.getUtils().inTARDISWorld(player)) {
					// get TARDIS player is in
					int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
					// get current TARDIS location
					HashMap<String, Object> where = new HashMap<>();
					where.put("tardis_id", id);
					ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, where);
					if (rsc.resultSet()) {
						world = rsc.getWorld();
					} else {
						// can't change weather in TARDIS world
						TARDISMessage.send(player, "WEATHER_TARDIS");
						return true;
					}
				}
				Weather weather = Weather.fromString(args[0]);
				String perm = weather.toString().toLowerCase();
				if (!TARDISPermission.hasPermission(player, "tardis.weather." + perm)) {
					TARDISMessage.send(sender, "NO_PERMS");
					return true;
				}
				TARDISWeather.setWeather(world, weather);
				TARDISMessage.send(player, "WEATHER_SET", perm);
			} else {
				TARDISMessage.send(sender, "CMD_PLAYER");
			}
			return true;
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
		if (args.length == 1) {
			return partial(args[0], ROOT_SUBS);
		}
		return ImmutableList.of();
	}
}
