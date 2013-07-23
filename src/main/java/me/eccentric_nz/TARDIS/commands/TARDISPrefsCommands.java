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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISMaterials;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
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
    private List<String> firstArgs = new ArrayList<String>();

    public TARDISPrefsCommands(TARDIS plugin) {
        this.plugin = plugin;
        firstArgs.add("auto");
        firstArgs.add("beacon");
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
        firstArgs.add("sign");
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
                        int plain = (plugin.getConfig().getBoolean("plain_on")) ? 1 : 0;
                        set.put("plain_on", plain);
                        set.put("lamp", plugin.getConfig().getInt("tardis_lamp"));
                        qf.doInsert("player_prefs", set);
                    }
                    if (pref.equals("key")) {
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "You need to specify a key item!");
                            return false;
                        }
                        String setMaterial = args[1].toUpperCase(Locale.ENGLISH);
                        if (!Arrays.asList(TARDISMaterials.MATERIAL_LIST).contains(setMaterial)) {
                            sender.sendMessage(plugin.pluginName + ChatColor.RED + "That is not a valid Material! Try checking http://jd.bukkit.org/apidocs/org/bukkit/Material.html");
                            return false;
                        } else {
                            HashMap<String, Object> setk = new HashMap<String, Object>();
                            setk.put("key", setMaterial);
                            HashMap<String, Object> where = new HashMap<String, Object>();
                            where.put("player", player.getName());
                            qf.doUpdate("player_prefs", setk, where);
                            sender.sendMessage(plugin.pluginName + "Key preference saved.");
                            return true;
                        }
                    }
                    if (pref.equals("lamp")) {
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "You need to specify a lamp item ID!");
                            return false;
                        }
                        int lamp;
                        try {
                            lamp = Integer.parseInt(args[1]);
                        } catch (NumberFormatException nfe) {
                            sender.sendMessage(plugin.pluginName + "The lamp item ID was not a number!");
                            return true;
                        }
                        // check it's an allowed block
                        List<Integer> allowed_ids = plugin.getBlocksConfig().getIntegerList("lamp_blocks");
                        if (!allowed_ids.contains(lamp)) {
                            sender.sendMessage(plugin.pluginName + "You cannot set the lamp item to that ID!");
                            return true;
                        }
                        HashMap<String, Object> setl = new HashMap<String, Object>();
                        setl.put("lamp", lamp);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", player.getName());
                        qf.doUpdate("player_prefs", setl, where);
                        sender.sendMessage(plugin.pluginName + "Lamp preference saved.");
                        return true;
                    }
                    if (pref.equals("isomorphic")) {
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("owner", player.getName());
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        // does the player have a TARDIS
                        if (rs.resultSet()) {
                            int iso = (rs.isIso_on()) ? 0 : 1;
                            String onoff = (rs.isIso_on()) ? "OFF" : "ON";
                            int id = rs.getTardis_id();
                            HashMap<String, Object> seti = new HashMap<String, Object>();
                            seti.put("iso_on", iso);
                            HashMap<String, Object> wheret = new HashMap<String, Object>();
                            wheret.put("tardis_id", id);
                            qf.doUpdate("tardis", seti, wheret);
                            sender.sendMessage(plugin.pluginName + "Isomorphic controls were turned " + onoff + "!");
                            return true;
                        } else {
                            sender.sendMessage(plugin.pluginName + "You don't have a TARDIS yet!");
                            return true;
                        }
                    }
                    if (pref.equals("eps_message")) {
                        int count = args.length;
                        StringBuilder buf = new StringBuilder();
                        for (int i = 1; i < count; i++) {
                            buf.append(args[i]).append(" ");
                        }
                        String tmp = buf.toString();
                        String message = tmp.substring(0, tmp.length() - 1);
                        HashMap<String, Object> sete = new HashMap<String, Object>();
                        sete.put("eps_message", message);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", player.getName());
                        qf.doUpdate("player_prefs", sete, where);
                        sender.sendMessage(plugin.pluginName + "The Emergency Program System message was set!");
                        return true;
                    }
                    if (pref.equals("wall") || pref.equals("floor")) {
                        if (args.length < 2) {
                            sender.sendMessage(plugin.pluginName + "You need to specify a " + pref + " material!");
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
                            String message = (wall_mat.equals("HELP")) ? "Here is a list of valid " + pref + " materials:" : "That is not a valid " + pref + " material! Try:";
                            sender.sendMessage(plugin.pluginName + message);
                            List<String> sortedKeys = new ArrayList(tw.blocks.keySet());
                            Collections.sort(sortedKeys);
                            for (String w : sortedKeys) {
                                sender.sendMessage(w);
                            }
                            return true;
                        }
                        HashMap<String, Object> setw = new HashMap<String, Object>();
                        setw.put(pref, wall_mat);
                        HashMap<String, Object> where = new HashMap<String, Object>();
                        where.put("player", player.getName());
                        qf.doUpdate("player_prefs", setw, where);
                        sender.sendMessage(plugin.pluginName + ucfirst(pref) + " material saved.");
                        return true;
                    }
                    if (args.length < 2 || (!args[1].equalsIgnoreCase("on") && !args[1].equalsIgnoreCase("off"))) {
                        sender.sendMessage(plugin.pluginName + "You need to specify if " + pref + " should be on or off!");
                        return false;
                    }
                    List<String> was = Arrays.asList(new String[]{"auto", "beacon", "platform", "eps", "hads", "plain", "submarine"});

                    HashMap<String, Object> setp = new HashMap<String, Object>();
                    HashMap<String, Object> wherep = new HashMap<String, Object>();
                    wherep.put("player", player.getName());
                    String grammar = (was.contains(pref)) ? " was" : " were";
                    if (args[1].equalsIgnoreCase("on")) {
                        setp.put(pref + "_on", 1);
                        if (pref.equals("beacon")) {
                            toggleBeacon(player.getName(), true);
                        }
                        sender.sendMessage(plugin.pluginName + pref + grammar + " turned ON!");
                    }
                    if (args[1].equalsIgnoreCase("off")) {
                        setp.put(pref + "_on", 0);
                        if (pref.equals("beacon")) {
                            toggleBeacon(player.getName(), false);
                        }
                        sender.sendMessage(plugin.pluginName + pref + grammar + " turned OFF.");
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

    private void toggleBeacon(String name, boolean on) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("owner", name);
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        if (rs.resultSet()) {
            // toggle beacon
            String beacon = rs.getBeacon();
            String[] beaconData;
            int plusy = 0;
            if (beacon.isEmpty()) {
                // get the location from the TARDIS size and the creeper location
                switch (rs.getSchematic()) {
                    case REDSTONE:
                        plusy = 18;
                        break;
                    case ELEVENTH:
                        plusy = 17;
                        break;
                    case DELUXE:
                        plusy = 16;
                        break;
                    case BIGGER:
                        plusy = 13;
                        break;
                    default: // BUDGET & STEAMPUNK
                        plusy = 12;
                        break;
                }
                String creeper = rs.getCreeper();
                beaconData = creeper.split(":");
            } else {
                beaconData = beacon.split(":");
            }
            World w = plugin.getServer().getWorld(beaconData[0]);
            float bx = 0, by = 0, bz = 0;
            try {
                bx = Float.parseFloat(beaconData[1]);
                by = Float.parseFloat(beaconData[2]) + plusy;
                bz = Float.parseFloat(beaconData[3]);
            } catch (NumberFormatException nfe) {
                plugin.debug("Couldn't convert to a float! " + nfe.getMessage());
            }
            if (beacon.isEmpty()) {
                // update the tardis table so we don't have to do tnis again
                String beacon_loc = beaconData[0] + ":" + beaconData[1] + ":" + by + ":" + beaconData[3];
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("beacon", beacon_loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", rs.getTardis_id());
                new QueryFactory(plugin).doUpdate("tardis", set, where);
            }
            Location bl = new Location(w, bx, by, bz);
            Block b = bl.getBlock();
            b.setTypeId((on) ? 20 : 7);
        }
    }
}
