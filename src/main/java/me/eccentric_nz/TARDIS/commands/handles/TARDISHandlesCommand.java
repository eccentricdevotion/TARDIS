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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesWeirdness;
import me.eccentric_nz.TARDIS.handles.wiki.HandlesWiki;
import me.eccentric_nz.TARDIS.handles.wiki.SearchDialog;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.audience.Audience;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISHandlesCommand implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISHandlesCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        if (sender instanceof Player player) {
            switch (args[0]) {
                case "wiki" -> {
                    if (args.length < 2) {
                        Audience.audience(player).showDialog(new SearchDialog().create());
                        return true;
                    }
                    new HandlesWiki(plugin).getLinks(args[1], player);
                    return true;
                }
                case "disk" -> {
                    return new TARDISHandlesDiskCommand(plugin).renameDisk(player, args);
                }
                case "remove" -> {
                    return new TARDISHandlesRemoveCommand(plugin).purge(player);
                }
                case "weird" -> {
                    TARDISHandlesWeirdness.say(player);
                    return true;
                }
            }
        } else {
            if (args[0].equals("wiki")) {
                plugin.getMessenger().send(sender, TardisModule.HANDLES, "HANDLES_NO_COMMAND");
                return true;
            }
            if (!sender.hasPermission("tardis.admin")) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
                return true;
            }
            if (args.length < 2) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "HANDLES_INTERNAL");
                return true;
            }
            if (args[0].equals("tell")) {
                return new TARDISHandlesTellCommand(plugin).message(args);
            }
            UUID uuid;
            try {
                uuid = (args[0].equals("brake")) ? UUID.fromString(args[2]) : UUID.fromString(args[1]);
            } catch (IllegalArgumentException e) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "HANDLES_INTERNAL");
                return true;
            }
            Player player = plugin.getServer().getPlayer(uuid);
            switch (args[0]) {
                case "land" -> {
                    return new TARDISHandlesLandCommand(plugin).exitVortex(player, TARDISNumberParsers.parseInt(args[2]), args[1]);
                }
                case "lock", "unlock" -> {
                    return new TARDISHandlesLockUnlockCommand(plugin).toggleLock(player, TARDISNumberParsers.parseInt(args[2]), Boolean.parseBoolean(args[3]));
                }
                case "name" -> {
                    plugin.getMessenger().handlesSend(player, "HANDLES_NAME", player.getName());
                    return true;
                }
                case "remind" -> {
                    return new TARDISHandlesRemindCommand(plugin).doReminder(player, args);
                }
                case "say" -> {
                    return new TARDISHandlesSayCommand(plugin).say(player, args);
                }
                case "scan" -> {
                    return new TARDISHandlesScanCommand(plugin, player, TARDISNumberParsers.parseInt(args[2])).sayScan();
                }
                case "takeoff" -> {
                    return new TARDISHandlesTakeoffCommand(plugin).enterVortex(player, args);
                }
                case "time" -> {
                    return new TARDISHandlesTimeCommand(plugin).sayTime(player);
                }
                case "brake" -> {
                    return new TARDISHandlesBrakeCommand(plugin).park(player, args);
                }
            }
        }
        return false;
    }
}
