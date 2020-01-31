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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.PRESET;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It is stored in the Secondary Control Room.
 * All TARDISes have one of these devices, which can be used to remotely control a TARDIS by broadcasting Stattenheim
 * signals that travel along the time contours in the Space/Time Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISBindCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<>();
    private final List<String> type_1;
    private final List<String> chameleon;

    public TARDISBindCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("save"); // type 0
        firstArgs.add("cmd"); // type 1
        firstArgs.add("player"); // type 2
        firstArgs.add("area"); // type 3
        firstArgs.add("biome"); // type 4
        firstArgs.add("chameleon"); // type 5
        firstArgs.add("transmat"); // type 6
        firstArgs.add("remove");
        firstArgs.add("update");
        type_1 = Arrays.asList("hide", "rebuild", "home", "cave", "make_her_blue", "occupy");
        chameleon = Arrays.asList("off", "adapt", "invisible");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisbind")) {
            if (!sender.hasPermission("tardis.update")) {
                TARDISMessage.send(sender, "NO_PERMS");
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return false;
            }
            if (args.length < 1) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                new TARDISCommandHelper(plugin).getCommand("tardisbind", sender);
                return false;
            }
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                TARDISMessage.send(player, "BIND_NOT_VALID");
                return false;
            }
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            int id = rs.getTardis_id();
            HashMap<String, Object> wheret = new HashMap<>();
            wheret.put("uuid", player.getUniqueId().toString());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                TARDISMessage.send(player, "NOT_IN_TARDIS");
                return false;
            }
            if (args[0].equalsIgnoreCase("update")) {
                for (String s : type_1) {
                    HashMap<String, Object> whereu = new HashMap<>();
                    whereu.put("tardis_id", id);
                    whereu.put("dest_name", s);
                    HashMap<String, Object> setu = new HashMap<>();
                    setu.put("type", 1);
                    plugin.getQueryFactory().doUpdate("destinations", setu, whereu);
                }
                TARDISMessage.send(player, "BIND_SET");
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length < 3) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                String dest = args[1].toLowerCase(Locale.ENGLISH);
                if (dest.equals("area") || dest.equals("cmd")) {
                    dest = args[2].toLowerCase(Locale.ENGLISH);
                }
                HashMap<String, Object> whered = new HashMap<>();
                if (args.length == 4) {
                    try {
                        id = Integer.parseInt(args[3]);
                    } catch (NumberFormatException ignored) {
                    }
                }
                whered.put("tardis_id", id);
                whered.put("dest_name", dest);
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                if (!rsd.resultSet()) {
                    TARDISMessage.send(player, "SAVE_NOT_FOUND", ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET);
                    return true;
                }
                if (rsd.getBind().isEmpty()) {
                    TARDISMessage.send(player, "BIND_NO_SAVE");
                    return true;
                }
                int dtype = rsd.getType();
                int did = rsd.getDest_id();
                if (rsd.getDest_name().equals("transmat")) {
                    HashMap<String, Object> wheremat = new HashMap<>();
                    wheremat.put("tardis_id", id);
                    wheremat.put("dest_name", dest);
                    wheremat.put("preset", args[2]);
                    ResultSetDestinations rsmat = new ResultSetDestinations(plugin, wheremat, true);
                    if (!rsmat.resultSet()) {
                        TARDISMessage.send(player, "TRANSMAT_NOT_FOUND");
                        return true;
                    }
                    if (rsmat.getData().size() > 1) {
                        // must click transmat sign
                        plugin.getTrackerKeeper().getTransmatRemoval().put(player.getUniqueId(), args[2]);
                        TARDISMessage.send(player, "TRANSMAT_CLICK_BLOCK");
                        return true;
                    }
                    did = rsmat.getDest_id();
                }
                HashMap<String, Object> whereb = new HashMap<>();
                whereb.put("dest_id", did);
                if (dtype > 0) {
                    // delete the record
                    plugin.getQueryFactory().doDelete("destinations", whereb);
                } else {
                    // just remove the bind location
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("bind", "");
                    plugin.getQueryFactory().doUpdate("destinations", set, whereb);
                }
                TARDISMessage.send(player, "BIND_REMOVED", firstArgs.get(dtype));
                return true;
            } else if (args[0].equalsIgnoreCase("transmat")) { // type 6
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                if (args.length > 1) {
                    set.put("preset", args[1]);
                } else {
                    set.put("preset", "console");
                }
                set.put("dest_name", "transmat");
                set.put("type", 6);
                int did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                if (did != 0) {
                    plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), did);
                    TARDISMessage.send(player, "BIND_CLICK");
                    return true;
                }
            } else {
                // all the rest require at least 2 arguments
                if (args.length < 2) {
                    TARDISMessage.send(player, "TOO_FEW_ARGS");
                    return false;
                }
                int did = 0;
                HashMap<String, Object> set = new HashMap<>();
                if (args[0].equalsIgnoreCase("save")) { // type 0
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    whered.put("dest_name", args[1]);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                    if (!rsd.resultSet()) {
                        TARDISMessage.send(player, "SAVE_NOT_FOUND", ChatColor.GREEN + "/TARDIS list saves" + ChatColor.RESET);
                        return true;
                    } else {
                        did = rsd.getDest_id();
                    }
                }
                if (args[0].equalsIgnoreCase("cmd")) { // type 1
                    if (!type_1.contains(args[1])) {
                        TARDISMessage.send(player, "BIND_CMD_NOT_VALID");
                        return true;
                    }
                    set.put("tardis_id", id);
                    set.put("dest_name", args[1].toLowerCase(Locale.ENGLISH));
                    set.put("type", 1);
                    did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("player")) { // type 2
                    // get player online or offline
                    Player p = plugin.getServer().getPlayer(args[1]);
                    if (p == null) {
                        OfflinePlayer offp = plugin.getServer().getOfflinePlayer(args[1]);
                        if (offp == null) {
                            TARDISMessage.send(player, "COULD_NOT_FIND_NAME");
                            return true;
                        }
                    }
                    set.put("tardis_id", id);
                    set.put("dest_name", args[1]);
                    set.put("type", 2);
                    did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("area")) { // type 3
                    HashMap<String, Object> wherea = new HashMap<>();
                    wherea.put("area_name", args[1]);
                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                    if (!rsa.resultSet()) {
                        TARDISMessage.send(player, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                        return true;
                    }
                    if (!player.hasPermission("tardis.area." + args[1]) || !player.isPermissionSet("tardis.area." + args[1])) {
                        TARDISMessage.send(player, "BIND_NO_AREA_PERM", args[1]);
                        return true;
                    }
                    set.put("tardis_id", id);
                    set.put("dest_name", args[1].toLowerCase(Locale.ENGLISH));
                    set.put("type", 3);
                    did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("biome")) { // type 4
                    // check valid biome
                    try {
                        String upper = args[1].toUpperCase(Locale.ENGLISH);
                        if (!upper.equals("HELL") && !upper.equals("SKY")) {
                            set.put("tardis_id", id);
                            set.put("dest_name", upper);
                            set.put("type", 4);
                            did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                        }
                    } catch (IllegalArgumentException iae) {
                        TARDISMessage.send(player, "BIOME_NOT_VALID");
                        return true;
                    }
                }
                if (args[0].equalsIgnoreCase("chameleon")) { // type 5
                    String which = args[1].toLowerCase(Locale.ENGLISH);
                    if (!chameleon.contains(which)) {
                        try {
                            which = args[1].toUpperCase(Locale.ENGLISH);
                            PRESET.valueOf(which);
                        } catch (IllegalArgumentException e) {
                            // abort
                            TARDISMessage.send(player, "ARG_PRESET");
                            return true;
                        }
                    }
                    set.put("tardis_id", id);
                    set.put("dest_name", which);
                    set.put("type", 5);
                    set.put("preset", which);
                    did = plugin.getQueryFactory().doSyncInsert("destinations", set);
                }
                if (did != 0) {
                    plugin.getTrackerKeeper().getBinder().put(player.getUniqueId(), did);
                    TARDISMessage.send(player, "BIND_CLICK");
                    return true;
                }
            }
        }
        return false;
    }
}
