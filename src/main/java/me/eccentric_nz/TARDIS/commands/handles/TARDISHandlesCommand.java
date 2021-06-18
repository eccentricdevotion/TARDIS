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
package me.eccentric_nz.tardis.commands.handles;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.handles.TardisHandlesWeirdness;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TardisHandlesCommand implements CommandExecutor {

    private final TardisPlugin plugin;

    public TardisHandlesCommand(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (args.length < 1) {
            return false;
        }
        Player player;
        if (args[0].equals("disk")) {
            player = (Player) sender;
            return new TardisHandlesDiskCommand(plugin).renameDisk(player, args);
        }
        if (args[0].equals("remove")) {
            player = (Player) sender;
            return new TardisHandlesRemoveCommand(plugin).purge(player);
        }
        if (args[0].equals("weird")) {
            player = (Player) sender;
            TardisHandlesWeirdness.say(player);
            return true;
        }
        if (!sender.hasPermission("tardis.admin")) {
            TardisMessage.send(sender, "CMD_ADMIN");
            return true;
        }
        if (args.length < 2) {
            TardisMessage.send(sender, "HANDLES_INTERNAL");
            return true;
        }
        if (args[0].equals("tell")) {
            return new TardisHandlesTellCommand(plugin).message(args);
        }
        UUID uuid;
        try {
            uuid = UUID.fromString(args[1]);
        } catch (IllegalArgumentException e) {
            TardisMessage.send(sender, "HANDLES_INTERNAL");
            return true;
        }
        player = plugin.getServer().getPlayer(uuid);
        switch (args[0]) {
            case "land":
                return new TardisHandlesLandCommand(plugin).exitVortex(player, TardisNumberParsers.parseInt(args[2]), args[1]);
            case "lock":
            case "unlock":
                return new TardisHandlesLockUnlockCommand(plugin).toggleLock(player, TardisNumberParsers.parseInt(args[2]), Boolean.parseBoolean(args[3]));
            case "name":
                assert player != null;
                TardisMessage.handlesSend(player, "HANDLES_NAME", player.getName());
                return true;
            case "remind":
                return new TardisHandlesRemindCommand(plugin).doReminder(player, args);
            case "say":
                return new TardisHandlesSayCommand().say(player, args);
            case "scan":
                return new TardisHandlesScanCommand(plugin, player, TardisNumberParsers.parseInt(args[2])).sayScan();
            case "takeoff":
                return new TardisHandlesTakeoffCommand(plugin).enterVortex(player, args);
            case "time":
                assert player != null;
                return new TardisHandlesTimeCommand(plugin).sayTime(player);
        }
        return false;
    }
}
