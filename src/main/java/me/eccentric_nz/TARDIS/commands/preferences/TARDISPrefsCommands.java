/*
 * Copyright (C) 2018 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.sonic.TARDISSonicMenuInventory;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

/**
 * Command /tardisprefs [arguments].
 * <p>
 * Children begin instruction at the Time Lord Academy, at the age of 8, in a special ceremony. The Gallifreyans are
 * forced to look into the Untempered Schism, which shows the entirety of the Time Vortex and the power that the Time
 * Lords have.
 *
 * @author eccentric_nz
 */
public class TARDISPrefsCommands implements CommandExecutor {

    private final TARDIS plugin;
    private final List<String> firstArgs = new ArrayList<>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("auto");
        firstArgs.add("auto_powerup");
        firstArgs.add("auto_rescue");
        firstArgs.add("auto_siege");
        firstArgs.add("beacon");
        firstArgs.add("build");
        firstArgs.add("ctm");
        firstArgs.add("difficulty");
        firstArgs.add("dnd");
        firstArgs.add("eps");
        firstArgs.add("eps_message");
        firstArgs.add("farm");
        firstArgs.add("flight");
        firstArgs.add("floor");
        firstArgs.add("hads");
        firstArgs.add("hads_type");
        firstArgs.add("hum");
        firstArgs.add("isomorphic");
        firstArgs.add("key");
        firstArgs.add("key_menu");
        firstArgs.add("junk");
        firstArgs.add("lamp");
        firstArgs.add("language");
        firstArgs.add("lanterns");
        firstArgs.add("minecart");
        firstArgs.add("policebox_textures");
        firstArgs.add("quotes");
        firstArgs.add("renderer");
        firstArgs.add("sfx");
        firstArgs.add("siege_floor");
        firstArgs.add("siege_wall");
        firstArgs.add("sign");
        firstArgs.add("sonic");
        firstArgs.add("submarine");
        firstArgs.add("telepathy");
        firstArgs.add("travelbar");
        firstArgs.add("wall");
        firstArgs.add("wool_lights");
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
                new TARDISCommandHelper(plugin).getCommand("tardisprefs", sender);
                return true;
            }
            if (player == null) {
                TARDISMessage.send(sender, "CMD_PLAYER");
                return true;
            }
            String pref = args[0].toLowerCase(Locale.ENGLISH);
            if (firstArgs.contains(pref)) {
                if (player.hasPermission("tardis.timetravel")) {
                    if (pref.equals("sonic")) {
                        // open sonic prefs menu
                        ItemStack[] sonics = new TARDISSonicMenuInventory().getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "Sonic Prefs Menu");
                        sim.setContents(sonics);
                        player.openInventory(sim);
                        return true;
                    }
                    if (pref.equals("key_menu")) {
                        // open sonic prefs menu
                        ItemStack[] keys = new TARDISKeyMenuInventory().getMenu();
                        Inventory sim = plugin.getServer().createInventory(player, 27, ChatColor.DARK_RED + "TARDIS Key Prefs Menu");
                        sim.setContents(keys);
                        player.openInventory(sim);
                        return true;
                    }
                    // get the players preferences
                    HashMap<String, Object> wherepp = new HashMap<>();
                    wherepp.put("uuid", player.getUniqueId().toString());
                    ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
                    QueryFactory qf = new QueryFactory(plugin);
                    HashMap<String, Object> set = new HashMap<>();
                    // if no prefs record found, make one
                    if (!rsp.resultSet()) {
                        set.put("uuid", player.getUniqueId().toString());
                        set.put("lamp", plugin.getConfig().getString("police_box.tardis_lamp"));
                        qf.doInsert("player_prefs", set);
                    }
                    switch (pref) {
                        case "difficulty":
                            return new TARDISSetDifficultyCommand(plugin).setDiff(player, args, qf);
                        case "eps_message":
                            return new TARDISEPSMessageCommand().setMessage(player, args, qf);
                        case "flight":
                            return new TARDISSetFlightCommand().setMode(player, args, qf);
                        case "hads_type":
                            return new TARDISHadsTypeCommand().setHadsPref(player, args, qf);
                        case "hum":
                            return new TARDISHumCommand().setHumPref(player, args, qf);
                        case "isomorphic":
                            return new TARDISIsomorphicCommand(plugin).toggleIsomorphicControls(player, qf);
                        case "key":
                            return new TARDISSetKeyCommand(plugin).setKeyPref(player, args, qf);
                        case "lamp":
                            return new TARDISSetLampCommand(plugin).setLampPref(player, args, qf);
                        case "language":
                            return new TARDISSetLanguageCommand().setLanguagePref(player, args, qf);
                        case "wall":
                        case "floor":
                        case "siege_wall":
                        case "siege_floor":
                            return new TARDISFloorCommand().setFloorOrWallBlock(player, args, qf);
                        default:
                            if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                                TARDISMessage.send(player, "PREF_ON_OFF", pref);
                                return false;
                            }
                            switch (pref) {
                                case "build":
                                    return new TARDISBuildCommand(plugin).toggleCompanionBuilding(player, args);
                                case "junk":
                                    return new TARDISJunkPreference(plugin).toggle(player, args[1], qf);
                                default:
                                    return new TARDISToggleOnOffCommand(plugin).toggle(player, args, qf);
                            }
                    }
                } else {
                    TARDISMessage.send(player, "NO_PERMS");
                    return false;
                }
            } else {
                TARDISMessage.send(player, "PREF_NOT_VALID");
            }
        }
        return false;
    }

    public static String ucfirst(String str) {
        return str.substring(0, 1).toUpperCase(Locale.ENGLISH) + str.substring(1).toLowerCase(Locale.ENGLISH);
    }
}
