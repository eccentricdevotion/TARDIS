/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Weather;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Locale;

public class TARDISWeatherCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("clear", "c", "rain", "r", "thunder", "t", "sun", "s");

    public TARDISWeatherCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisweather")) {
            if (args.length < 1) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return true;
            }
            if (sender instanceof Player player) {
                if (!plugin.getConfig().getBoolean("allow.weather_set")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_DISABLED");
                    return true;
                }
                Location location = player.getLocation();
                World world = location.getWorld();
                if (plugin.getUtils().inTARDISWorld(player)) {
                    // get TARDIS player is in
                    int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
                    // get current TARDIS location
                    ResultSetCurrentFromId rs = new ResultSetCurrentFromId(plugin, id);
                    if (rs.resultSet()) {
                        world = rs.getCurrent().location().getWorld();
                    } else {
                        // can't change weather in TARDIS world
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_TARDIS");
                        return true;
                    }
                }
                Weather weather = Weather.fromString(args[0]);
                String perm = weather.toString().toLowerCase(Locale.ROOT);
                if (!TARDISPermission.hasPermission(player, "tardis.weather." + perm)) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                    return true;
                }
                TARDISWeather.setWeather(world, weather);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "WEATHER_SET", perm);
                return true;
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length <= 1) {
            return partial(args[0], ROOT_SUBS);
        }
        return ImmutableList.of();
    }
}
