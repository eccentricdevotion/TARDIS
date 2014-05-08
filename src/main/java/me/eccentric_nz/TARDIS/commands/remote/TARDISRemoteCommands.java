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
package me.eccentric_nz.TARDIS.commands.remote;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.enumeration.REMOTE;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoteCommands implements CommandExecutor {

    private final TARDIS plugin;

    public TARDISRemoteCommands(TARDIS plugin) {
        this.plugin = plugin;
    }

    @Override
    @SuppressWarnings("deprecation")
    public boolean onCommand(final CommandSender sender, Command cmd, String label, String[] args) {
        // If the player/console typed /tardisremote then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisremote") && sender.hasPermission("tardis.remote")) {
            UUID oluuid = plugin.getServer().getOfflinePlayer(args[0]).getUniqueId();
            if (oluuid == null) {
                oluuid = plugin.getGeneralKeeper().getUUIDCache().getIdOptimistic(args[0]);
                plugin.getGeneralKeeper().getUUIDCache().getId(args[0]);
            }
            if (oluuid != null) {
                final UUID uuid = oluuid;
                // check the player has a TARDIS
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("uuid", uuid.toString());
                ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                if (rs.resultSet()) {
                    // we're good to go
                    final int id = rs.getTardis_id();
                    boolean chameleon = rs.isChamele_on();
                    boolean hidden = rs.isHidden();
                    boolean handbrake = rs.isHandbrake_on();
                    int level = rs.getArtron_level();
                    if (sender instanceof Player && !sender.hasPermission("tardis.admin")) {
                        HashMap<String, Object> wheret = new HashMap<String, Object>();
                        wheret.put("uuid", ((Player) sender).getUniqueId().toString());
                        ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false);
                        if (!rst.resultSet()) {
                            sendMessage(sender, plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                            return true;
                        }
                        int tardis_id = rst.getTardis_id();
                        if (tardis_id != id) {
                            sendMessage(sender, plugin.getPluginName() + "You can only run this command if you are the Time Lord of the " + ChatColor.LIGHT_PURPLE + "remote" + ChatColor.RESET + " TARDIS!");
                            return true;
                        }
                        // must have circuits
                        TARDISCircuitChecker tcc = null;
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            sendMessage(sender, plugin.getPluginName() + MESSAGE.NO_MAT_CIRCUIT.getText());
                            return true;
                        }
                    }
                    // what are we going to do?
                    try {
                        REMOTE remote = REMOTE.valueOf(args[1].toUpperCase());
                        OfflinePlayer p = plugin.getServer().getOfflinePlayer(uuid);
                        // we can't get permissions for offline players!
                        if (sender instanceof BlockCommandSender && p.getPlayer() == null) {
                            return true;
                        }
                        switch (remote) {
                            case CHAMELEON:
                                // toggle the chameleon circuit on/off
                                int cham = (chameleon) ? 0 : 1;
                                String onoff = (chameleon) ? "OFF" : "ON";
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                setc.put("chamele_on", cham);
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                wherec.put("tardis_id", id);
                                new QueryFactory(plugin).doUpdate("tardis", setc, wherec);
                                sendMessage(sender, plugin.getPluginName() + "Chameleon circuit set to: " + onoff);
                                return true;
                            case HIDE:
                                // if it's a non-admin player or command block running the command
                                // check the usual requirements (circuits/energy) - else just do it
                                if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                    return new TARDISHideCommand(plugin).hide(p);
                                } else {
                                    return new TARDISRemoteHideCommand(plugin).doRemoteHide(sender, id);
                                }
                            case REBUILD:
                                // if it's a non-admin player or command block running the command
                                // check the usual requirements (circuits/energy) - else just do it
                                if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                    return new TARDISRebuildCommand(plugin).rebuildPreset(p);
                                } else {
                                    return new TARDISRemoteRebuildCommand(plugin).doRemoteRebuild(sender, id, p, chameleon, hidden);
                                }
                            case COMEHERE:
                                // NOT non-admin players, command blocks or the console
                                if (sender instanceof Player && sender.hasPermission("tardis.admin")) {
                                    return new TARDISRemoteComehereCommand(plugin).doRemoteComeHere((Player) sender, uuid);
                                } else {
                                    sendMessage(sender, plugin.getPluginName() + "You must be player with the tardis.admin permission!");
                                    return true;
                                }
                            default: // TRAVEL
                                // already travelling
                                if (!handbrake) {
                                    sendMessage(sender, plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_WHILE_TRAVELLING.getText());
                                    return true;
                                }
                                // check artron energy if not admin
                                if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                    int travel = plugin.getArtronConfig().getInt("travel");
                                    if (level < travel) {
                                        sendMessage(sender, plugin.getPluginName() + ChatColor.RED + MESSAGE.NOT_ENOUGH_ENERGY.getText());
                                        return true;
                                    }
                                }
                                // home, area or coords?
                                HashMap<String, Object> set = new HashMap<String, Object>();
                                if (args[2].toLowerCase().equals("home")) {
                                    // get home location
                                    HashMap<String, Object> wherehl = new HashMap<String, Object>();
                                    wherehl.put("tardis_id", id);
                                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                    if (!rsh.resultSet()) {
                                        sendMessage(sender, plugin.getPluginName() + "Could not get the TARDIS 'home' location!");
                                        return true;
                                    }
                                    set.put("world", rsh.getWorld().getName());
                                    set.put("x", rsh.getX());
                                    set.put("y", rsh.getY());
                                    set.put("z", rsh.getZ());
                                    set.put("direction", rsh.getDirection().toString());
                                    set.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
                                } else if (args[2].toLowerCase().equals("area")) {
                                    // check area name
                                    HashMap<String, Object> wherea = new HashMap<String, Object>();
                                    wherea.put("area_name", args[3]);
                                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false);
                                    if (!rsa.resultSet()) {
                                        sendMessage(sender, plugin.getPluginName() + "Could not find an area with that name! try using " + ChatColor.GREEN + " /tardis list areas " + ChatColor.RESET + " first.");
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        // must use advanced console if difficulty hard
                                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                                            sendMessage(sender, plugin.getPluginName() + "The TARDIS difficulty level on this server requires you to use the Advanced Console! See the " + ChatColor.AQUA + "TARDIS Information System" + ChatColor.RESET + " for help with using Area Disks.");
                                            return true;
                                        }
                                        // check permission
                                        String perm = "tardis.area." + args[3];
                                        if ((!p.getPlayer().hasPermission(perm) && !p.getPlayer().hasPermission("tardis.area.*"))) {
                                            sendMessage(sender, plugin.getPluginName() + "You do not have permission [tardis.area." + args[3] + "] to send the TARDIS to this location!");
                                            return true;
                                        }
                                    }
                                    // get a landing spot
                                    Location l = plugin.getTardisArea().getNextSpot(rsa.getArea_name());
                                    // returns null if full!
                                    if (l == null) {
                                        sendMessage(sender, plugin.getPluginName() + MESSAGE.NO_MORE_SPOTS.getText());
                                        return true;
                                    }
                                    set.put("world", l.getWorld().getName());
                                    set.put("x", l.getBlockX());
                                    set.put("y", l.getBlockY());
                                    set.put("z", l.getBlockZ());
                                    set.put("submarine", 0);
                                } else {
                                    // coords
                                    if (args.length < 6) {
                                        sendMessage(sender, plugin.getPluginName() + "Too few command arguments for co-ordinates travel!");
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!p.getPlayer().hasPermission("tardis.timetravel.location")) {
                                            sendMessage(sender, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
                                            return true;
                                        }
                                    }
                                    int x, y, z;
                                    World w = plugin.getServer().getWorld(args[2]);
                                    if (w == null) {
                                        sendMessage(sender, plugin.getPluginName() + "Cannot find the specified world! Make sure you typed it correctly.");
                                        return true;
                                    }
                                    if (!plugin.getConfig().getBoolean("worlds." + w.getName())) {
                                        sendMessage(sender, plugin.getPluginName() + "The server does not allow time travel to this world!");
                                        return true;
                                    }
                                    if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[2].equals(plugin.getConfig().getString("creation.default_world_name"))) {
                                        sendMessage(sender, plugin.getPluginName() + "The server does not allow time travel to this world!");
                                        return true;
                                    }
                                    x = plugin.getUtils().parseInt(args[args.length - 3]);
                                    y = plugin.getUtils().parseInt(args[args.length - 2]);
                                    if (y == 0) {
                                        sendMessage(sender, plugin.getPluginName() + "Y coordinate must be > 0!");
                                        return true;
                                    }
                                    z = plugin.getUtils().parseInt(args[args.length - 1]);
                                    Location location = new Location(w, x, y, z);
                                    // check location
                                    if (!plugin.getTardisArea().areaCheckInExisting(location)) {
                                        sendMessage(sender, plugin.getPluginName() + "The location is in a TARDIS area! Please use " + ChatColor.AQUA + "/tardisremote [player] travel area [area name]");
                                        return true;
                                    }
                                    // check respect if not admin
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!plugin.getPluginRespect().getRespect(p.getPlayer(), location, true)) {
                                            return true;
                                        }
                                    }
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        sendMessage(sender, plugin.getPluginName() + MESSAGE.NO_CURRENT.getText());
                                        return true;
                                    }
                                    // check location
                                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                                    int[] start_loc = tt.getStartLocation(location, rsc.getDirection());
                                    int count = tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
                                    if (count > 0) {
                                        sendMessage(sender, plugin.getPluginName() + "The specified location would not be safe! Please try another.");
                                        return true;
                                    } else {
                                        set.put("world", location.getWorld().getName());
                                        set.put("x", location.getBlockX());
                                        set.put("y", location.getBlockY());
                                        set.put("z", location.getBlockZ());
                                        set.put("submarine", 0);
                                    }
                                }
                                HashMap<String, Object> wheret = new HashMap<String, Object>();
                                wheret.put("tardis_id", id);
                                new QueryFactory(plugin).doUpdate("next", set, wheret);
                                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                                    @Override
                                    public void run() {
                                        OfflinePlayer player = plugin.getServer().getOfflinePlayer(uuid);
                                        String success = (new TARDISRemoteTravelCommand(plugin).doTravel(id, player, sender)) ? "successful." : "unsuccessful!";
                                        sendMessage(sender, plugin.getPluginName() + "The remote TARDIS travel command was " + success);
                                    }
                                }, 5L);
                                return true;
                        }
                    } catch (IllegalArgumentException e) {
                        sendMessage(sender, plugin.getPluginName() + MESSAGE.NOT_VALID_ARG.getText());
                        return false;
                    }
                }
            } else {
                sendMessage(sender, plugin.getPluginName() + "Could not get specified player's UUID. If the player is offline we need to lookup the UUID from the Mojang Authentication server. Try the command again shortly.");
                return true;
            }
        }
        return false;
    }

    private void sendMessage(CommandSender sender, String message) {
        if (sender instanceof BlockCommandSender) {
            return;
        }
        sender.sendMessage(message);
    }
}
