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
package me.eccentric_nz.TARDIS.commands.sudo;

import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCompleter;
import me.eccentric_nz.TARDIS.commands.preferences.TARDISIsomorphicCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteBackCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteComehereCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteHideCommand;
import me.eccentric_nz.TARDIS.commands.remote.TARDISRemoteRebuildCommand;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetARS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisConsole;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.Updateable;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.*;
import org.bukkit.entity.Player;

import java.util.*;

public class TARDISSudoCommand extends TARDISCompleter implements CommandExecutor, TabCompleter {

    private final TARDIS plugin;
    private final List<String> SUDOS = Arrays.asList("ars", "assemble", "back", "chameleon", "clean", "comehere", "deadlock", "desiege", "handbrake", "hide", "isomorphic", "rebuild", "repair", "travel", "update");
    private final List<String> CHAM_SUBS = new ArrayList<>();
    private final ImmutableList<String> TRAVEL_SUBS = ImmutableList.of("home", "area", "back");
    private final List<String> AREA_SUBS = new ArrayList<>();
    private final List<String> UPD_SUBS = new ArrayList<>();

    public TARDISSudoCommand(TARDIS plugin) {
        this.plugin = plugin;
        for (ChameleonPreset p : ChameleonPreset.values()) {
            CHAM_SUBS.add(p.toString());
        }
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            AREA_SUBS.addAll(rsa.getNames());
        }
        for (Updateable u : Updateable.values()) {
            UPD_SUBS.add(u.getName());
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardissudo")) {
            if (sender instanceof ConsoleCommandSender || sender.hasPermission("tardis.admin")) {
                if (args.length < 2) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "TOO_FEW_ARGS");
                    return true;
                }
                // must be a player name
                OfflinePlayer offlinePlayer = Bukkit.getOfflinePlayer(args[0]);
                if (offlinePlayer.getName() == null) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "COULD_NOT_FIND_NAME");
                    return true;
                }
                UUID uuid = offlinePlayer.getUniqueId();
                ResultSetTardisID rs = new ResultSetTardisID(plugin);
                if (!rs.fromUUID(uuid.toString())) {
                    plugin.getMessenger().send(sender, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
                    return true;
                }
                String which = args[1].toLowerCase(Locale.ROOT);
                if (SUDOS.contains(which)) {
                    switch (which) {
                        case "ars" -> {
                            if (sender instanceof ConsoleCommandSender) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                return true;
                            }
                            // does the player have an ARS record yet?
                            HashMap<String, Object> wherer = new HashMap<>();
                            wherer.put("tardis_id", rs.getTardisId());
                            ResultSetARS rsa = new ResultSetARS(plugin, wherer);
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
                                // get TARDIS console size
                                ResultSetTardisConsole rstc = new ResultSetTardisConsole(plugin);
                                if (rstc.fromUUID(uuid.toString())) {
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
                                    } else if (schm.getPermission().equals("bigger") || schm.getPermission().equals("division") || schm.getPermission().equals("redstone") || schm.getPermission().equals("twelfth") || schm.getPermission().equals("thirteenth")) {
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
                            return new SudoARS(plugin).showARS((Player) sender, uuid);
                        }
                        case "assemble" -> {
                            return new SudoAssemble(plugin).restore(sender, uuid, offlinePlayer.getName());
                        }
                        case "back" -> {
                            return new TARDISRemoteBackCommand(plugin).sendBack(sender, rs.getTardisId(), offlinePlayer);
                        }
                        case "chameleon" -> {
                            return new SudoChameleon(plugin).setPreset(sender, rs.getTardisId(), args, offlinePlayer);
                        }
                        case "clean" -> {
                            return new SudoRepair(plugin, uuid, true).repair();
                        }
                        case "comehere" -> {
                            if (sender instanceof ConsoleCommandSender) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                return true;
                            }
                            return new TARDISRemoteComehereCommand(plugin).doRemoteComeHere((Player) sender, uuid);
                        }
                        case "deadlock" -> {
                            // toggle door deadlocks
                            return new SudoDeadlock(plugin).toggleDeadlock(uuid, sender);
                        }
                        case "desiege" -> {
                            if (offlinePlayer.isOnline()) {
                                return new SudoDesiege(plugin).restore(sender, uuid, rs.getTardisId());
                            } else {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "NOT_ONLINE");
                                return true;
                            }
                        }
                        case "handbrake" -> {
                            return new SudoHandbrake(plugin).toggle(sender, args, uuid);
                        }
                        case "hide" -> {
                            return new TARDISRemoteHideCommand(plugin).doRemoteHide(sender, rs.getTardisId());
                        }
                        case "isomorphic" -> {
                            // toggle isomorphic
                            return new TARDISIsomorphicCommand(plugin).toggleIsomorphicControls(uuid, sender);
                        }
                        case "rebuild" -> {
                            return new TARDISRemoteRebuildCommand(plugin).doRemoteRebuild(sender, rs.getTardisId(), offlinePlayer, true);
                        }
                        case "repair" -> {
                            return new SudoRepair(plugin, uuid, false).repair();
                        }
                        case "travel" -> {
                            // get arguments
                            return plugin.getServer().dispatchCommand(plugin.getConsole(), "tardisremote " + offlinePlayer.getName() + " travel " + String.join(" ", Arrays.copyOfRange(args, 2, args.length)));
                        }
                        default -> { // update
                            if (sender instanceof ConsoleCommandSender) {
                                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_NO_CONSOLE");
                                return true;
                            }
                            return new SudoUpdate(plugin).initiate((Player) sender, args, rs.getTardisId(), uuid);
                        }
                    }
                }
            } else {
                plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_ADMIN");
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1) {
            return null;
        }
        if (args.length == 2) {
            return partial(args[1], SUDOS);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("chameleon")) {
            return partial(args[2], CHAM_SUBS);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("travel")) {
            return partial(args[2], TRAVEL_SUBS);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("update")) {
            return partial(args[2], UPD_SUBS);
        } else if (args.length == 4 && args[2].equalsIgnoreCase("area")) {
            return partial(args[3], AREA_SUBS);
        }
        return ImmutableList.of();
    }
}
