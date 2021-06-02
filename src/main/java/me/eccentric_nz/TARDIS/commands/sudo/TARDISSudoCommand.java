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
package me.eccentric_nz.TARDIS.commands.sudo;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISIsomorphicCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteBackCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteComehereCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteHideCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteRebuildCommand;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class TARDISSudoCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> SUDOS = Arrays.asList("ars", "assemble", "back", "chameleon", "clean", "comehere", "deadlock", "desiege", "handbrake", "hide", "isomorphic", "rebuild", "repair", "travel", "update");
    private final List<String> CHAM_SUBS = new ArrayList<>();
    private final ImmutableList<String> TRAVEL_SUBS = ImmutableList.of("home", "area", "back");
    private final List<String> AREA_SUBS = new ArrayList<>();

    public TARDISSudoCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (PRESET p : PRESET.values()) {
            CHAM_SUBS.add(p.toString());
        }
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            AREA_SUBS.addAll(rsa.getNames());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissudo")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length < 2) {
                    TARDISMessage.send(sender, "TOO_FEW_ARGS");
                    return true;
                }
                // must be a player name
                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(args[0]);
                if (offlinePlayer == null) {
                    TARDISMessage.send(sender, "COULD_NOT_FIND_NAME");
                    return true;
                }
                UUID uuid = offlinePlayer.getUniqueId();
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(uuid.toString())) {
                    TARDISMessage.send(sender, "PLAYER_NO_TARDIS");
                    return true;
                }
                String which = args[1].toLowerCase();
                if (SUDOS.contains(which)) {
                    switch (which) {
                        case "ars":
                            if (sender instanceof ConsoleCommandSender) {
                                TARDISMessage.send(sender, "CMD_NO_CONSOLE");
                                return true;
                            }
                            return new SudoARS(plugin).showARS();
                        case "assemble":
                            return new SudoAssemble(plugin).restore(sender, uuid, offlinePlayer.getName());
                        case "back":
                            return new TARDISRemoteBackCommand(plugin).sendBack(sender, rs.getTardis_id(), offlinePlayer);
                        case "chameleon":
                            return new SudoChameleon(plugin).setPreset();
                        case "clean":
                            return new SudoRepair(plugin, uuid, true).repair();
                        case "comehere":
                            return new TARDISRemoteComehereCommand(plugin).doRemoteComeHere((Player) sender, uuid);
                        case "deadlock":
                            // toggle door deadlocks
                            return new SudoDeadlock(plugin).toggleDeadlock(uuid, sender);
                        case "desiege":
                            if (offlinePlayer.isOnline()) {
                                return new SudoDesiege(plugin).restore(sender, uuid, rs.getTardis_id());
                            } else {
                                TARDISMessage.send(sender, "NOT_ONLINE");
                                return true;
                            }
                        case "handbrake":
                            return new SudoHandbrake(plugin).toggle(sender, args, uuid);
                        case "hide":
                            return new TARDISRemoteHideCommand(plugin).doRemoteHide(sender, rs.getTardis_id());
                        case "isomorphic":
                            // toggle isomorphic
                            return new TARDISIsomorphicCommand(plugin).toggleIsomorphicControls(uuid, sender);
                        case "rebuild":
                            return new TARDISRemoteRebuildCommand(plugin).doRemoteRebuild(sender, rs.getTardis_id(), offlinePlayer, true);
                        case "repair":
                            return new SudoRepair(plugin, uuid, false).repair();
                        case "travel":
                            // get arguments
                            StringBuilder sb = new StringBuilder();
                            for (int i = 2; i < args.length; i++) {
                                sb.append(" ").append(args[i]);
                            }
                            return plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + offlinePlayer.getName() + " travel" + sb);
                        default: // update
                            return new SudoUpdate(plugin).initiate();
                    }
                }
            } else {
                TARDISMessage.send(sender, "CMD_ADMIN");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return null;
        }
        if (args.length == 2) {
            return partial(args[1], SUDOS);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("chameleon")) {
            return partial(args[2], CHAM_SUBS);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("travel")) {
            return partial(args[2], TRAVEL_SUBS);
        } else if (args.length == 4 && args[2].equalsIgnoreCase("area")) {
            return partial(args[3], AREA_SUBS);
        }
        return ImmutableList.of();
    }
}
