/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Time;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class TARDISTimeCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("day", "morning", "noon", "night", "midnight", "1AM", "2AM", "3AM", "4AM", "5AM", "6AM", "7AM", "8AM", "9AM", "10AM", "11AM", "12AM", "1PM", "2PM", "3PM", "4PM", "5PM", "6PM", "7PM", "8PM", "9PM", "10PM", "11PM", "12PM");

    public TARDISTimeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardistime")) {
            if (args.length < 1) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                return true;
            }
            if (sender instanceof Player player) {
                if (!player.hasPermission("tardis.admin")) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "NO_PERMS");
                    return true;
                }
                Location location = player.getLocation();
                World world = location.getWorld();
                if (plugin.getUtils().inTARDISWorld(player)) {
                    // get TARDIS player is in
                    int id = plugin.getTardisAPI().getIdOfTARDISPlayerIsIn(player);
                    // get current TARDIS location
                    ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                    if (rsc.resultSet()) {
                        world = rsc.getWorld();
                    } else {
                        // can't change weather in TARDIS world
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_TARDIS");
                        return true;
                    }
                }
                long ticks;
                Time time = Time.getByName().get(args[0].toUpperCase());
                if (time != null) {
                    ticks = time.getTicks();
                } else {
                    try {
                        ticks = Long.parseLong(args[0]);
                    } catch (NumberFormatException nfe) {
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_FORMAT");
                        return true;
                    }
                }
                world.setTime(ticks);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TIME_SET", String.format("%s", ticks), world.getName());
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
