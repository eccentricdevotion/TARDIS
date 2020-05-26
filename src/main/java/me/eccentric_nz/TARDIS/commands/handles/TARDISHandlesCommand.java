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
package me.eccentric_nz.TARDIS.commands.handles;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.handles.TARDISHandlesWeirdness;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
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
        Player player;
        if (args[0].equals("disk")) {
            player = (Player) sender;
            return new TARDISHandlesDiskCommand(plugin).renameDisk(player, args);
        }
        if (args[0].equals("remove")) {
            player = (Player) sender;
            return new TARDISHandlesRemoveCommand(plugin).purge(player);
        }
        if (args[0].equals("weird")) {
            player = (Player) sender;
            TARDISHandlesWeirdness.say(player);
            return true;
        }
        if (!sender.hasPermission("tardis.admin")) {
            TARDISMessage.send(sender, "CMD_ADMIN");
            return true;
        }
        if (args.length < 2) {
            TARDISMessage.send(sender, "HANDLES_INTERNAL");
            return true;
        }
        if (args[0].equals("tell")) {
            return new TARDISHandlesTellCommand(plugin).message(args);
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            TARDISMessage.send(sender, "HANDLES_INTERNAL");
            return true;
        }
        player = plugin.getServer().getPlayer(uuid);
        switch (args[0]) {
            case "land":
                return new TARDISHandlesLandCommand(plugin).exitVortex(player, TARDISNumberParsers.parseInt(args[2]), args[1]);
            case "lock":
            case "unlock":
                return new TARDISHandlesLockUnlockCommand(plugin).toggleLock(player, TARDISNumberParsers.parseInt(args[2]), Boolean.valueOf(args[3]));
            case "name":
                TARDISMessage.handlesSend(player, "HANDLES_NAME", player.getName());
                return true;
            case "remind":
                return new TARDISHandlesRemindCommand(plugin).doReminder(player, args);
            case "say":
                return new TARDISHandlesSayCommand().say(player, args);
            case "scan":
                return new TARDISHandlesScanCommand(plugin, player, TARDISNumberParsers.parseInt(args[2])).sayScan();
            case "takeoff":
                return new TARDISHandlesTakeoffCommand(plugin).enterVortex(player, args);
            case "time":
                return new TARDISHandlesTimeCommand(plugin).sayTime(player);
        }
        return false;
    }
}
