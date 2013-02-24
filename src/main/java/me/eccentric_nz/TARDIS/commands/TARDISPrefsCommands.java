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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Command /tardisprefs [arguments].
 *
 * Children begin instruction at the Time Lord Academy, at the age of 8, in a
 * special ceremony. The Gallifreyans are forced to look into the Untempered
 * Schism, which shows the entirety of the Time Vortex and the power that the
 * Time Lords have.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsCommands implements CommandExecutor {

    private final TARDIS plugin;
    private List<String> firstArgs = new ArrayList<String>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("sfx");
        firstArgs.add("platform");
        firstArgs.add("quotes");
        firstArgs.add("wall");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        // If the player typed /tardisprefs then do the following...
        // check there is the right number of arguments
        if (cmd.getName().equalsIgnoreCase("tardisprefs")) {
            if (args.length == 0) {
                return false;
            }
            if (player == null) {
                sender.sendMessage(plugin.pluginName + ChatColor.RED + " This command can only be run by a player");
                return false;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (firstArgs.contains(pref)) {
                if (player.hasPermission("tardis.timetravel")) {
                    if (args[0].equalsIgnoreCase("wall")) {
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "You need to specify a wall material!");
                            return false;
                        }
                        String wall_mat;
                        if (args.length > 2) {
                            int count = args.length;
                            StringBuilder buf = new StringBuilder();
                            for (int i = 1; i < count; i++) {
                                buf.append(args[i]).append("_");
                            }
                            String tmp = buf.toString();
                            String t = tmp.substring(0, tmp.length() - 1);
                            wall_mat = t.toUpperCase(Locale.ENGLISH);
                        } else {
                            wall_mat = args[1].toUpperCase(Locale.ENGLISH);
                        }
                        TARDISWalls tw = new TARDISWalls();
                        if (!tw.blocks.containsKey(wall_mat)) {
                            String message = (wall_mat.equals("HELP")) ? "Here is a list of valid wall materials:" : "That is not a valid wall material! Try:";
                            sender.sendMessage(plugin.pluginName + message);
                            for (String w : tw.blocks.keySet()) {
                                sender.sendMessage(w);
                            }
                            return true;
                        }
                        QueryFactory qf = new QueryFactory(plugin);
                        HashMap<String, Object> set = new HashMap<String, Object>();
                        set.put("wall", wall_mat);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", player.getName());
                        qf.doUpdate("player_prefs", set, where);
                        sender.sendMessage(plugin.pluginName + "Wall material saved.");
                        return true;
                    }
                    if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                        sender.sendMessage(plugin.pluginName + "You need to specify if " + pref + " should be on or off!");
                        return false;
                    }
                    // get the players preferences
                    HashMap<String, Object> where = new HashMap<String, Object>();
                    where.put("player", player.getName());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, where);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    if (!rsp.resultSet()) {
                        set.put("player", player.getName());
                        qf.doInsert("player_prefs", set);
                    }
                    HashMap<String, Object> setp = new HashMap<String, Object>();
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("player", player.getName());
                    if (args[1].equalsIgnoreCase("on")) {
                        setp.put(pref + "_on", 1);
                        sender.sendMessage(plugin.pluginName + pref + " were turned ON!");
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        setp.put(pref + "_on", 0);
                        sender.sendMessage(plugin.pluginName + pref + " were turned OFF.");
                    }
                    qf.doUpdate("player_prefs", setp, wherep);
                    return true;
                } else {
                    sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                    return false;
                }
            }
        }
        return false;
    }
}
