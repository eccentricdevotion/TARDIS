/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.utils;

import com.google.common.collect.ImmutableList;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.GameMode;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TARDISGameModeCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final ImmutableList<String> ROOT_SUBS = ImmutableList.of("SURVIVAL", "s", "CREATIVE", "c", "ADVENTURE", "a", "SPECTATOR", "sp");

    public TARDISGameModeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisgamemode")) {
            if (label.equalsIgnoreCase("tgms")) {
                // set survival
                return setPlayerGameMode(sender, args, GameMode.SURVIVAL);
            }
            if (label.equalsIgnoreCase("tgmc")) {
                // set creative
                return setPlayerGameMode(sender, args, GameMode.CREATIVE);
            }
            if (label.equalsIgnoreCase("tgma")) {
                // set adventure
                return setPlayerGameMode(sender, args, GameMode.ADVENTURE);
            }
            if (label.equalsIgnoreCase("tgmsp")) {
                // set spectator
                return setPlayerGameMode(sender, args, GameMode.SPECTATOR);
            }
            return setPlayerGameMode(sender, args, null);
        }
        return false;
    }

    private boolean setPlayerGameMode(CommandSender sender, String[] args, GameMode gm) {
        if (gm == null && args.length < 1) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        Player player = null;
        boolean thirdperson = false;
        if (sender instanceof ConsoleCommandSender) {
            // must specify a player name
            if ((gm == null && args.length < 2) || ((gm != null && args.length < 1))) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                return true;
            }
            player = plugin.getServer().getPlayer(gm == null ? args[1] : args[0]);
            if (player == null) {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_ONLINE");
                return true;
            }
            thirdperson = true;
        }
        if (sender instanceof Player) {
            if (gm == null && args.length == 2) {
                player = plugin.getServer().getPlayer(args[1]);
                if (player == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_ONLINE");
                    return true;
                }
            } else if (gm != null && args.length == 1) {
                if (ROOT_SUBS.contains(args[0].toUpperCase(Locale.ENGLISH))) {
                    player = (Player) sender;
                } else {
                    player = plugin.getServer().getPlayer(args[0]);
                    thirdperson = true;
                    if (player == null) {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_ONLINE");
                        return true;
                    }
                }
            } else {
                player = (Player) sender;
            }
        }
        if (gm == null) {
            // get GameMode from first argument
            if (args[0].length() <= 2) {
                switch (args[0].toLowerCase(Locale.ENGLISH)) {
                    case "s" -> gm = GameMode.SURVIVAL;
                    case "c" -> gm = GameMode.CREATIVE;
                    case "a" -> gm = GameMode.ADVENTURE;
                    case "sp" -> gm = GameMode.SPECTATOR;
                    default -> {
                        plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_GAMEMODE");
                        return false;
                    }
                }
            } else {
                try {
                    gm = GameMode.valueOf(args[0].toUpperCase(Locale.ENGLISH));
                } catch (IllegalArgumentException e) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_GAMEMODE");
                    return false;
                }
            }
        }
        player.setGameMode(gm);
        if (thirdperson) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_GAMEMODE_CONSOLE", player.getName(), gm.toString());
        }
        plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_GAMEMODE_PLAYER", gm.toString());
        return true;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length <= 1) {
            if (label.equalsIgnoreCase("tgms") || label.equalsIgnoreCase("tgmc") || label.equalsIgnoreCase("tgma") || label.equalsIgnoreCase("tgmsp")) {
                return null;
            } else {
                return partial(args[0], ROOT_SUBS);
            }
        }
        if (args.length == 2) {
            return null;
        }
        return ImmutableList.of();
    }
}
