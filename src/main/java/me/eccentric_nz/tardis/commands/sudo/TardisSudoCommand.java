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
package me.eccentric_nz.tardis.commands.sudo;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.commands.TardisCompleter;
import me.eccentric_nz.tardis.commands.preferences.TardisIsomorphicCommand;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteBackCommand;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteComeHereCommand;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteHideCommand;
import me.eccentric_nz.tardis.commands.remote.TardisRemoteRebuildCommand;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.database.resultset.ResultSetArs;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisConsole;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardisId;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.Schematic;
import me.eccentric_nz.tardis.enumeration.Updateable;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.utility.TardisStaticUtils;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class TardisSudoCommand extends TardisCompleter implements CommandExecutor, TabCompleter {

    private final TardisPlugin plugin;
    private final List<String> sudos = Arrays.asList("ars", "assemble", "back", "chameleon", "clean", "comehere", "deadlock", "desiege", "handbrake", "hide", "isomorphic", "rebuild", "repair", "travel", "update");
    private final List<String> chamSubs = new ArrayList<>();
    private final ImmutableList<String> travelSubs = ImmutableList.of("home", "area", "back");
    private final List<String> areaSubs = new ArrayList<>();
    private final List<String> updSubs = new ArrayList<>();

    public TardisSudoCommand(TardisPlugin plugin) {
        this.plugin = plugin;
        for (Preset p : Preset.values()) {
            chamSubs.add(p.toString());
        }
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            areaSubs.addAll(rsa.getNames());
        }
        for (Updateable u : Updateable.values()) {
            updSubs.add(u.getName());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissudo")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length < 2) {
                    TardisMessage.send(sender, "TOO_FEW_ARGS");
                    return true;
                }
                // must be a player name
                OfflinePlayer offlinePlayer = TardisStaticUtils.getOfflinePlayer(args[0]);
                if (offlinePlayer == null) {
                    TardisMessage.send(sender, "COULD_NOT_FIND_NAME");
                    return true;
                }
                UUID uuid = offlinePlayer.getUniqueId();
                ResultSetTardisId rs = new ResultSetTardisId(plugin);
                if (!rs.fromUuid(uuid.toString())) {
                    TardisMessage.send(sender, "PLAYER_NO_Tardis");
                    return true;
                }
                String which = args[1].toLowerCase();
                if (sudos.contains(which)) {
                    switch (which) {
                        case "ars":
                            if (sender instanceof ConsoleCommandSender) {
                                TardisMessage.send(sender, "CMD_NO_CONSOLE");
                                return true;
                            }
                            // does the player have an ARS record yet?
                            HashMap<String, Object> wherer = new HashMap<>();
                            wherer.put("tardis_id", rs.getTardisId());
                            ResultSetArs rsa = new ResultSetArs(plugin, wherer);
                            if (!rsa.resultSet()) {
                                // create default json
                                String[][][] empty = new String[3][9][9];
                                for (int y = 0; y < 3; y++) {
                                    for (int x = 0; x < 9; x++) {
                                        for (int z = 0; z < 9; z++) {
                                            empty[y][x][z] = "STONE";
                                        }
                                    }
                                }
                                // get Tardis console size
                                ResultSetTardisConsole rstc = new ResultSetTardisConsole(plugin);
                                if (rstc.fromUuid(uuid.toString())) {
                                    Schematic schm = rstc.getSchematic();
                                    String controlBlock = schm.getSeedMaterial().toString();
                                    if (schm.getPermission().equals("coral") || schm.getPermission().equals("deluxe") || schm.getPermission().equals("eleventh") || schm.getPermission().equals("master")) {
                                        empty[0][4][4] = controlBlock;
                                        empty[0][4][5] = controlBlock;
                                        empty[0][5][4] = controlBlock;
                                        empty[0][5][5] = controlBlock;
                                        empty[1][4][5] = controlBlock;
                                        empty[1][5][4] = controlBlock;
                                        empty[1][5][5] = controlBlock;
                                    } else if (schm.getPermission().equals("bigger") || schm.getPermission().equals("redstone") || schm.getPermission().equals("twelfth") || schm.getPermission().equals("thirteenth")) {
                                        empty[1][4][5] = controlBlock;
                                        empty[1][5][4] = controlBlock;
                                        empty[1][5][5] = controlBlock;
                                    }
                                    empty[1][4][4] = controlBlock;
                                    Gson gson = new GsonBuilder().disableHtmlEscaping().create();
                                    JsonArray json = JsonParser.parseString(gson.toJson(empty)).getAsJsonArray();
                                    HashMap<String, Object> seta = new HashMap<>();
                                    seta.put("tardis_id", rs.getTardisId());
                                    seta.put("uuid", uuid);
                                    seta.put("json", json.toString());
                                    plugin.getQueryFactory().doInsert("ars", seta);
                                }
                            }
                            return new SudoArs(plugin).showArs((Player) sender, uuid);
                        case "assemble":
                            return new SudoAssemble(plugin).restore(sender, uuid, offlinePlayer.getName());
                        case "back":
                            return new TardisRemoteBackCommand(plugin).sendBack(sender, rs.getTardisId(), offlinePlayer);
                        case "chameleon":
                            return new SudoChameleon(plugin).setPreset(sender, rs.getTardisId(), args, offlinePlayer);
                        case "clean":
                            return new SudoRepair(plugin, uuid, true).repair();
                        case "comehere":
                            if (sender instanceof ConsoleCommandSender) {
                                TardisMessage.send(sender, "CMD_NO_CONSOLE");
                                return true;
                            }
                            return new TardisRemoteComeHereCommand(plugin).doRemoteComeHere((Player) sender, uuid);
                        case "deadlock":
                            // toggle door deadlocks
                            return new SudoDeadlock(plugin).toggleDeadlock(uuid, sender);
                        case "desiege":
                            if (offlinePlayer.isOnline()) {
                                return new SudoDesiege(plugin).restore(sender, uuid, rs.getTardisId());
                            } else {
                                TardisMessage.send(sender, "NOT_ONLINE");
                                return true;
                            }
                        case "handbrake":
                            return new SudoHandbrake(plugin).toggle(sender, args, uuid);
                        case "hide":
                            return new TardisRemoteHideCommand(plugin).doRemoteHide(sender, rs.getTardisId());
                        case "isomorphic":
                            // toggle isomorphic
                            return new TardisIsomorphicCommand(plugin).toggleIsomorphicControls(uuid, sender);
                        case "rebuild":
                            return new TardisRemoteRebuildCommand(plugin).doRemoteRebuild(sender, rs.getTardisId(), offlinePlayer, true);
                        case "repair":
                            return new SudoRepair(plugin, uuid, false).repair();
                        case "travel":
                            // get arguments
                            return plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + offlinePlayer.getName() + " travel " + String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                        default: // update
                            if (sender instanceof ConsoleCommandSender) {
                                TardisMessage.send(sender, "CMD_NO_CONSOLE");
                                return true;
                            }
                            return new SudoUpdate(plugin).initiate((Player) sender, args, rs.getTardisId(), uuid);
                    }
                }
            } else {
                TardisMessage.send(sender, "CMD_ADMIN");
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
            return partial(args[1], sudos);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("chameleon")) {
            return partial(args[2], chamSubs);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("travel")) {
            return partial(args[2], travelSubs);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("update")) {
            return partial(args[2], updSubs);
        } else if (args.length == 4 && args[2].equalsIgnoreCase("area")) {
            return partial(args[3], areaSubs);
        }
        return ImmutableList.of();
    }
}
