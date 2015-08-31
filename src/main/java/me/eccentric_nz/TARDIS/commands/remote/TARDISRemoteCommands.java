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
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.commands.TARDISCommandHelper;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISHideCommand;
import me.eccentric_nz.TARDIS.commands.tardis.TARDISRebuildCommand;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetAreas;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.enumeration.REMOTE;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
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
            if (args.length < 2) {
                new TARDISCommandHelper(plugin).getCommand("tardisremote", sender);
                return true;
            }
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
                    // not in siege mode
                    if (plugin.getTrackerKeeper().getInSiegeMode().contains(rs.getTardis_id())) {
                        TARDISMessage.send(sender, "SIEGE_NO_CMD");
                        return true;
                    }
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
                            TARDISMessage.send(sender, "NOT_A_TIMELORD");
                            return true;
                        }
                        int tardis_id = rst.getTardis_id();
                        if (tardis_id != id) {
                            TARDISMessage.send(sender, "CMD_ONLY_TL_REMOTE");
                            return true;
                        }
                        if (plugin.getConfig().getBoolean("allow.power_down") && !rst.isPowered_on()) {
                            TARDISMessage.send(sender, "POWER_DOWN");
                            return true;
                        }
                        // must have circuits
                        TARDISCircuitChecker tcc = null;
                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                            tcc = new TARDISCircuitChecker(plugin, id);
                            tcc.getCircuits();
                        }
                        if (tcc != null && !tcc.hasMaterialisation()) {
                            TARDISMessage.send(sender, "NO_MAT_CIRCUIT");
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
                                String onoff = (chameleon) ? plugin.getLanguage().getString("SET_OFF") : plugin.getLanguage().getString("SET_ON");
                                HashMap<String, Object> setc = new HashMap<String, Object>();
                                setc.put("chamele_on", cham);
                                HashMap<String, Object> wherec = new HashMap<String, Object>();
                                wherec.put("tardis_id", id);
                                new QueryFactory(plugin).doUpdate("tardis", setc, wherec);
                                TARDISMessage.send(sender, "CHAM_SET_ON_OFF", onoff);
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
                                    TARDISMessage.send(sender, "NO_PERMS");
                                    return true;
                                }
                            case BACK:
                                // NOT non-admin players or command blocks
                                if ((sender instanceof Player && sender.hasPermission("tardis.admin")) || sender instanceof ConsoleCommandSender) {
                                    if (!handbrake) {
                                        TARDISMessage.send(sender, "NOT_WHILE_TRAVELLING");
                                        return true;
                                    }
                                    return new TARDISRemoteBackCommand(plugin).sendBack(sender, id, p);
                                } else {
                                    TARDISMessage.send(sender, "NO_PERMS");
                                    return true;
                                }
                            default: // TRAVEL
                                if (args.length < 3) {
                                    TARDISMessage.send(sender, "ARG_REMOTE");
                                    return false;
                                }
                                // already travelling
                                if (!handbrake) {
                                    TARDISMessage.send(sender, "NOT_WHILE_TRAVELLING");
                                    return true;
                                }
                                // check artron energy if not admin
                                if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                    int travel = plugin.getArtronConfig().getInt("travel");
                                    if (level < travel) {
                                        TARDISMessage.send(sender, "NOT_ENOUGH_ENERGY");
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
                                        TARDISMessage.send(sender, "HOME_NOT_FOUND");
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
                                        TARDISMessage.send(sender, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        // must use advanced console if difficulty hard
                                        if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                                            TARDISMessage.send(sender, "ADV_AREA");
                                            return true;
                                        }
                                        // check permission
                                        String perm = "tardis.area." + args[3];
                                        if ((!p.getPlayer().hasPermission(perm) && !p.getPlayer().hasPermission("tardis.area.*"))) {
                                            TARDISMessage.send(sender, "TRAVEL_NO_AREA_PERM", args[3]);
                                            return true;
                                        }
                                    }
                                    // get a landing spot
                                    Location l = plugin.getTardisArea().getNextSpot(rsa.getAreaName());
                                    // returns null if full!
                                    if (l == null) {
                                        TARDISMessage.send(sender, "NO_MORE_SPOTS");
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
                                        TARDISMessage.send(sender, "ARG_COORDS");
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!p.getPlayer().hasPermission("tardis.timetravel.location")) {
                                            TARDISMessage.send(sender, "NO_PERMS");
                                            return true;
                                        }
                                    }
                                    int x, y, z;
                                    World w = plugin.getServer().getWorld(args[2]);
                                    if (w == null) {
                                        TARDISMessage.send(sender, "WORLD_NOT_FOUND");
                                        return true;
                                    }
                                    if (!plugin.getConfig().getBoolean("worlds." + w.getName())) {
                                        TARDISMessage.send(sender, "NO_WORLD_TRAVEL");
                                        return true;
                                    }
                                    if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[2].equals(plugin.getConfig().getString("creation.default_world_name"))) {
                                        TARDISMessage.send(sender, "NO_WORLD_TRAVEL");
                                        return true;
                                    }
                                    x = TARDISNumberParsers.parseInt(args[args.length - 3]);
                                    y = TARDISNumberParsers.parseInt(args[args.length - 2]);
                                    if (y == 0) {
                                        TARDISMessage.send(sender, "Y_NOT_VALID");
                                        return true;
                                    }
                                    z = TARDISNumberParsers.parseInt(args[args.length - 1]);
                                    Location location = new Location(w, x, y, z);
                                    // check location
                                    if (!plugin.getTardisArea().areaCheckInExisting(location)) {
                                        TARDISMessage.send(sender, "TRAVEL_IN_AREA", ChatColor.AQUA + "/tardisremote [player] travel area [area name]");
                                        return true;
                                    }
                                    // check respect if not admin
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!plugin.getPluginRespect().getRespect(location, new Parameters(p.getPlayer(), FLAG.getDefaultFlags()))) {
                                            return true;
                                        }
                                    }
                                    HashMap<String, Object> wherecl = new HashMap<String, Object>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        TARDISMessage.send(sender, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                    // check location
                                    TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
                                    int[] start_loc = tt.getStartLocation(location, rsc.getDirection());
                                    int count = tt.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
                                    if (count > 0) {
                                        TARDISMessage.send(sender, "NOT_SAFE");
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
                                        String success = (new TARDISRemoteTravelCommand(plugin).doTravel(id, player, sender)) ? plugin.getLanguage().getString("SUCCESS_Y") : plugin.getLanguage().getString("SUCCESS_N");
                                        TARDISMessage.send(sender, "REMOTE_SUCCESS", success);
                                    }
                                }, 5L);
                                return true;
                        }
                    } catch (IllegalArgumentException e) {
                        TARDISMessage.send(sender, "CMD_NOT_VALID");
                        return false;
                    }
                }
            } else {
                TARDISMessage.send(sender, "UUID");
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
