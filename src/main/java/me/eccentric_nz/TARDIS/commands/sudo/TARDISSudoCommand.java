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
 * along with plugin program. If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class TARDISSudoCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;

    public TARDISSudoCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissudo")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length < 1) {
                    TARDISMessage.send(sender, "TOO_FEW_ARGS");
                    return true;
                }
                Player player;
                UUID uuid;
                if (sender instanceof Player) {
                    player = (Player) sender;
                    uuid = player.getUniqueId();
                } else {
                    uuid = TARDISSudoTracker.CONSOLE_UUID;
                }
                String first = args[0];
                if (first.equalsIgnoreCase("off")) {
                    TARDISSudoTracker.SUDOERS.remove(uuid);
                    TARDISMessage.send(sender, "SUDO_OFF");
                } else {
                    // must be a player name
                    OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[0]);
                    if (offlinePlayer == null) {
                        TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                        return true;
                    }
                    ResultSetTardisID rs = new ResultSetTardisID(plugin);
                    if (!rs.fromUUID(offlinePlayer.getUniqueId().toString())) {
                        TARDISMessage.send(sender, "PLAYER_NO_TARDIS");
                        return true;
                    }
                    TARDISSudoTracker.SUDOERS.put(uuid, offlinePlayer.getUniqueId());
                    TARDISMessage.send(sender, "SUDO_ON", offlinePlayer.getName());
                }
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            List<String> SUBS = new ArrayList<>();
            SUBS.add("off");
            for (Player p : plugin.getServer().getOnlinePlayers()) {
                SUBS.add(p.getName());
            }
            return partial(args[0], SUBS);
        }
        return null;
    }
}
