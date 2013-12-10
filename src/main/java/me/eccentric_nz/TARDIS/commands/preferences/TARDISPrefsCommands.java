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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
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

    public static String ucfirst(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1).toLowerCase(Locale.ENGLISH);
    }
    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("auto");
        firstArgs.add("beacon");
        firstArgs.add("dnd");
        firstArgs.add("eps");
        firstArgs.add("eps_message");
        firstArgs.add("floor");
        firstArgs.add("hads");
        firstArgs.add("isomorphic");
        firstArgs.add("key");
        firstArgs.add("lamp");
        firstArgs.add("plain");
        firstArgs.add("platform");
        firstArgs.add("quotes");
        firstArgs.add("sfx");
        firstArgs.add("submarine");
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
                    // get the players preferences
                    HashMap<String, Object> wherepp = new HashMap<String, Object>();
                    wherepp.put("player", player.getName());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<String, Object>();
                    // if no prefs record found, make one
                    if (!rsp.resultSet()) {
                        set.put("player", player.getName());
                        //int plain = (plugin.getConfig().getBoolean("plain_on")) ? 1 : 0;
                        //set.put("plain_on", plain);
                        set.put("lamp", plugin.getConfig().getInt("tardis_lamp"));
                        qf.doInsert("player_prefs", set);
                    }
                    if (pref.equals("key")) {
                        return new TARDISSetKeyCommand(plugin).setKeyPref(player, args, qf);
                    }
                    if (pref.equals("lamp")) {
                        return new TARDISSetLampCommand(plugin).setLampPref(player, args, qf);
                    }
                    if (pref.equals("isomorphic")) {
                        return new TARDISIsomorphicCommand(plugin).toggleIsomorphicControls(player, args, qf);
                    }
                    if (pref.equals("eps_message")) {
                        return new TARDISEPSMessageCommand(plugin).setMessage(player, args, qf);
                    }
                    if (pref.equals("wall") || pref.equals("floor")) {
                        return new TARDISFloorCommand(plugin).setFloorOrWallBlock(player, args, qf);
                    }
                    if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                        sender.sendMessage(plugin.pluginName + "You need to specify if " + pref + " should be on or off!");
                        return false;
                    }
                    return new TARDISToggleOnOffCommand(plugin).doAbort(player, args, qf);
                } else {
                    sender.sendMessage(plugin.pluginName + TARDISConstants.NO_PERMS_MESSAGE);
                    return false;
                }
            }
        }
        return false;
    }
}
