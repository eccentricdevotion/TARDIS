/*
 * Copyright (C) 2014 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
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
    private final List<String> firstArgs = new ArrayList<String>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("auto");
        firstArgs.add("beacon");
        firstArgs.add("build");
        firstArgs.add("dnd");
        firstArgs.add("eps");
        firstArgs.add("eps_message");
        firstArgs.add("floor");
        firstArgs.add("hads");
        firstArgs.add("isomorphic");
        firstArgs.add("key");
        firstArgs.add("lamp");
        firstArgs.add("language");
        firstArgs.add("minecart");
        firstArgs.add("plain");
        firstArgs.add("platform");
        firstArgs.add("quotes");
        firstArgs.add("renderer");
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
                sender.sendMessage(plugin.getPluginName() + MESSAGE.MUST_BE_PLAYER.getText());
                return true;
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
                        set.put("lamp", plugin.getConfig().getInt("police_box.tardis_lamp"));
                        qf.doInsert("player_prefs", set);
                    }
                    if (pref.equals("key")) {
                        return new TARDISSetKeyCommand(plugin).setKeyPref(player, args, qf);
                    }
                    if (pref.equals("lamp")) {
                        return new TARDISSetLampCommand(plugin).setLampPref(player, args, qf);
                    }
                    if (pref.equals("language")) {
                        return new TARDISSetLanguageCommand(plugin).setLanguagePref(player, args, qf);
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
                        TARDISMessage.send(player, plugin.getPluginName() + "You need to specify if " + pref + " should be on or off!");
                        return false;
                    }
                    if (pref.equals("build")) {
                        return new TARDISBuildCommand(plugin).toggleCompanionBuilding(player, args);
                    } else {
                        return new TARDISToggleOnOffCommand(plugin).doAbort(player, args, qf);
                    }
                } else {
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
                    return false;
                }
            }
        }
        return false;
    }

    public static String ucfirst(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1).toLowerCase(Locale.ENGLISH);
    }
}
