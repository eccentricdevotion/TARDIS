/*
 * Copyright (C) 2013 eccentric_nz
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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetDestinations;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * A Time Control Unit is a golden sphere about the size of a Cricket ball. It
 * is stored in the Secondary Control Room. All TARDISes have one of these
 * devices, which can be used to remotely control a TARDIS by broadcasting
 * Stattenheim signals that travel along the time contours in the Space/Time
 * Vortex.
 *
 * @author eccentric_nz
 */
public class TARDISBindCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();
    private final List<String> type_1;

    public TARDISBindCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("save"); // type 0
        firstArgs.add("cmd"); // type 1
        firstArgs.add("player"); // type 2
        firstArgs.add("area"); // type 3
        firstArgs.add("biome"); // type 4
        firstArgs.add("remove");
        firstArgs.add("update");
        type_1 = Arrays.asList(new String[]{"hide", "rebuild", "home", "cave"});
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("tardisbind")) {
            if (!sender.hasPermission("tardis.update")) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NO_PERMS.getText());
                return false;
            }
            Player player = null;
            if (sender instanceof Player) {
                player = (Player) sender;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + MESSAGE.MUST_BE_PLAYER.getText());
                return false;
            }
            if (args.length < 1) {
                sender.sendMessage(plugin.pluginName + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            if (!firstArgs.contains(args[0].toLowerCase(Locale.ENGLISH))) {
                sender.sendMessage(plugin.pluginName + "That is not a valid 'bind' type! Try one of: save|cmd|player|area|remove");
                return false;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NOT_A_TIMELORD.getText());
                return false;
            }
            int id = rs.getTardis_id();
            HashMap<String, Object> wheret = new HashMap<String, Object>();
            wheret.put("player", player.getName());
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
            if (!rst.resultSet()) {
                sender.sendMessage(plugin.pluginName + MESSAGE.NOT_IN_TARDIS.getText());
                return false;
            }
            if (args[0].equalsIgnoreCase("update")) {
                for (String s : type_1) {
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> whereu = new HashMap<String, Object>();
                    whereu.put("tardis_id", id);
                    whereu.put("dest_name", s);
                    HashMap<String, Object> setu = new HashMap<String, Object>();
                    setu.put("type", 1);
                    qf.doUpdate("destinations", setu, whereu);
                }
                sender.sendMessage(plugin.pluginName + "Binds updated!");
                return true;
            }
            if (args.length < 2) {
                sender.sendMessage(plugin.pluginName + MESSAGE.TOO_FEW_ARGS.getText());
                return false;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                HashMap<String, Object> whered = new HashMap<String, Object>();
                whered.put("tardis_id", id);
                whered.put("dest_name", args[1].toLowerCase(Locale.ENGLISH));
                ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                if (!rsd.resultSet()) {
                    sender.sendMessage(plugin.pluginName + "Could not find a save with that name! Try using " + ChatColor.AQUA + "/tardis list saves" + ChatColor.RESET + " first.");
                    return true;
                }
                if (rsd.getBind().isEmpty()) {
                    sender.sendMessage(plugin.pluginName + "There is no button bound to that save name!");
                    return true;
                }
                int did = rsd.getDest_id();
                int dtype = rsd.getType();
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> whereb = new HashMap<String, Object>();
                whereb.put("dest_id", did);
                if (dtype > 0) {
                    // delete the record
                    qf.doDelete("destinations", whereb);
                } else {
                    // just remove the bind location
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    set.put("bind", "");
                    qf.doUpdate("destinations", set, whereb);
                }
                player.sendMessage(plugin.pluginName + "The " + firstArgs.get(dtype) + " was unbound. You can safely delete the block.");
                return true;
            } else {
                int did = 0;
                if (args[0].equalsIgnoreCase("save")) { // type 0
                    HashMap<String, Object> whered = new HashMap<String, Object>();
                    whered.put("tardis_id", id);
                    whered.put("dest_name", args[1]);
                    ResultSetDestinations rsd = new ResultSetDestinations(plugin, whered, false);
                    if (!rsd.resultSet()) {
                        sender.sendMessage(plugin.pluginName + "Could not find a save with that name! Try using " + ChatColor.AQUA + "/tardis list saves" + ChatColor.RESET + " first.");
                        return true;
                    } else {
                        did = rsd.getDest_id();
                    }
                }
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", id);
                if (args[0].equalsIgnoreCase("cmd")) { // type 1
                    if (!type_1.contains(args[1])) {
                        sender.sendMessage(plugin.pluginName + "You can only bind the hide, cave, rebuild and home commands!");
                        return true;
                    }
                    set.put("dest_name", args[1].toLowerCase(Locale.ENGLISH));
                    set.put("type", 1);
                    did = qf.doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("player")) { // type 2
                    // get player online or offline
                    Player p = plugin.getServer().getPlayer(args[1]);
                    if (p == null) {
                        OfflinePlayer offp = plugin.getServer().getOfflinePlayer(args[1]);
                        if (offp == null) {
                            sender.sendMessage(plugin.pluginName + "Could not find a player with that name on this server!");
                            return true;
                        }
                    }
                    set.put("dest_name", args[1]);
                    set.put("type", 2);
                    did = qf.doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("area")) { // type 3
                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                    wherea.put("area_name", args[1]);
                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                    if (!rsa.resultSet()) {
                        sender.sendMessage(plugin.pluginName + "Could not find an area with that name! try using " + ChatColor.AQUA + "/tardis list areas" + ChatColor.RESET + " first.");
                        return true;
                    }
                    if (!player.hasPermission("tardis.area." + args[1]) || !player.isPermissionSet("tardis.area." + args[1])) {
                        sender.sendMessage(plugin.pluginName + "You do not have permission [tardis.area." + args[1] + "] to bind to this location!");
                        return true;
                    }
                    set.put("dest_name", args[1].toLowerCase(Locale.ENGLISH));
                    set.put("type", 3);
                    did = qf.doSyncInsert("destinations", set);
                }
                if (args[0].equalsIgnoreCase("biome")) { // type 4
                    // check valid biome
                    try {
                        String upper = args[1].toUpperCase(Locale.ENGLISH);
//                        Biome biome = Biome.valueOf(upper);
                        if (!upper.equals("HELL") && !upper.equals("SKY")) {
                            set.put("dest_name", upper);
                            set.put("type", 4);
                            did = qf.doSyncInsert("destinations", set);
                        }
                    } catch (IllegalArgumentException iae) {
                        player.sendMessage(plugin.pluginName + "Biome type not valid!");
                        return true;
                    }
                }
                if (did != 0) {
                    plugin.trackBinder.put(player.getName(), did);
                    player.sendMessage(plugin.pluginName + "Click the block you want to bind to this save location.");
                    return true;
                }
            }
        }
        return false;
    }
}
