/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.commands.remote;

import com.google.common.collect.ImmutableList;
import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.advanced.TardisCircuitChecker;
import me.eccentric_nz.tardis.api.Parameters;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.commands.TardisCommandHelper;
import me.eccentric_nz.tardis.commands.TardisCompleter;
import me.eccentric_nz.tardis.commands.tardis.TardisHideCommand;
import me.eccentric_nz.tardis.commands.tardis.TardisRebuildCommand;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetAreas;
import me.eccentric_nz.tardis.database.resultset.ResultSetCurrentLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.Difficulty;
import me.eccentric_nz.tardis.enumeration.Flag;
import me.eccentric_nz.tardis.enumeration.Preset;
import me.eccentric_nz.tardis.enumeration.Remote;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.planets.TardisAliasResolver;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import me.eccentric_nz.tardis.utility.TardisNumberParsers;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.*;

/**
 * @author eccentric_nz
 */
public class TardisRemoteCommands extends TardisCompleter implements CommandExecutor, TabCompleter {

    private final TardisPlugin plugin;
    private final ImmutableList<String> rootSubs = ImmutableList.of("travel", "comehere", "hide", "rebuild", "back");
    private final ImmutableList<String> travelSubs = ImmutableList.of("home", "area");
    private final List<String> areaSubs = new ArrayList<>();

    //[player] [travel|comehere|hide|rebuild|back] [home|area|coords]

    public TardisRemoteCommands(TardisPlugin plugin) {
        this.plugin = plugin;
        ResultSetAreas rsa = new ResultSetAreas(plugin, null, false, true);
        if (rsa.resultSet()) {
            areaSubs.addAll(rsa.getNames());
        }
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
        // If the player/console typed /tardisremote then do the following...
        if (cmd.getName().equalsIgnoreCase("tardisremote") && TardisPermission.hasPermission(sender, "tardis.remote")) {
            if (args.length < 2) {
                new TardisCommandHelper(plugin).getCommand("tardisremote", sender);
                return true;
            }
            UUID uuid = plugin.getServer().getOfflinePlayer(args[0]).getUniqueId();
            // check the player has a Tardis
            HashMap<String, Object> where = new HashMap<>();
            where.put("uuid", uuid.toString());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
            if (rs.resultSet()) {
                Tardis tardis = rs.getTardis();
                // not in siege mode
                if (plugin.getTrackerKeeper().getInSiegeMode().contains(tardis.getTardisId())) {
                    TardisMessage.send(sender, "SIEGE_NO_CMD");
                    return true;
                }
                // we're good to go
                int id = tardis.getTardisId();
                boolean hidden = tardis.isHidden();
                boolean handbrake = tardis.isHandbrakeOn();
                int level = tardis.getArtronLevel();
                if (sender instanceof Player && !sender.hasPermission("tardis.admin")) {
                    HashMap<String, Object> wheret = new HashMap<>();
                    wheret.put("uuid", ((Player) sender).getUniqueId().toString());
                    ResultSetTardis rst = new ResultSetTardis(plugin, wheret, "", false, 0);
                    if (!rst.resultSet()) {
                        TardisMessage.send(sender, "NOT_A_TIMELORD");
                        return true;
                    }
                    Tardis t = rst.getTardis();
                    int tardis_id = t.getTardisId();
                    if (tardis_id != id) {
                        TardisMessage.send(sender, "CMD_ONLY_TL_REMOTE");
                        return true;
                    }
                    if (plugin.getConfig().getBoolean("allow.power_down") && !t.isPowered()) {
                        TardisMessage.send(sender, "POWER_DOWN");
                        return true;
                    }
                    // must have circuits
                    TardisCircuitChecker tcc = null;
                    if (!plugin.getDifficulty().equals(Difficulty.EASY)) {
                        tcc = new TardisCircuitChecker(plugin, id);
                        tcc.getCircuits();
                    }
                    if (tcc != null && !tcc.hasMaterialisation()) {
                        TardisMessage.send(sender, "NO_MAT_CIRCUIT");
                        return true;
                    }
                }
                // what are we going to do?
                try {
                    Remote remote = Remote.valueOf(args[1].toUpperCase(Locale.ENGLISH));
                    OfflinePlayer p = plugin.getServer().getOfflinePlayer(uuid);
                    // we can't get permissions for offline players!
                    if (sender instanceof BlockCommandSender && p.getPlayer() == null) {
                        return true;
                    }
                    switch (remote) {
                        case HIDE:
                            // if it's a non-admin player or command block running the command
                            // check the usual requirements (circuits/energy) - else just do it
                            if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                return new TardisHideCommand(plugin).hide(p);
                            } else {
                                return new TardisRemoteHideCommand(plugin).doRemoteHide(sender, id);
                            }
                        case REBUILD:
                            // if it's a non-admin player or command block running the command
                            // check the usual requirements (circuits/energy) - else just do it
                            if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                return new TardisRebuildCommand(plugin).rebuildPreset(p);
                            } else {
                                return new TardisRemoteRebuildCommand(plugin).doRemoteRebuild(sender, id, p, hidden);
                            }
                        case COMEHERE:
                            // NOT non-admin players, command blocks or the console
                            if (sender instanceof Player player && sender.hasPermission("tardis.admin")) {
                                return new TardisRemoteComeHereCommand(plugin).doRemoteComeHere(player, uuid);
                            } else {
                                TardisMessage.send(sender, "NO_PERMS");
                                return true;
                            }
                        case BACK:
                            // NOT non-admin players or command blocks
                            if ((sender instanceof Player && sender.hasPermission("tardis.admin")) || sender instanceof ConsoleCommandSender) {
                                if (!handbrake) {
                                    TardisMessage.send(sender, "NOT_WHILE_TRAVELLING");
                                    return true;
                                }
                                return new TardisRemoteBackCommand(plugin).sendBack(sender, id, p);
                            } else {
                                TardisMessage.send(sender, "NO_PERMS");
                                return true;
                            }
                        default: // TRAVEL
                            if (args.length < 3) {
                                TardisMessage.send(sender, "ARG_REMOTE");
                                return false;
                            }
                            // already travelling
                            if (!handbrake) {
                                TardisMessage.send(sender, "NOT_WHILE_TRAVELLING");
                                return true;
                            }
                            // check artron energy if not admin
                            if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                int travel = plugin.getArtronConfig().getInt("travel");
                                if (level < travel) {
                                    TardisMessage.send(sender, "NOT_ENOUGH_ENERGY");
                                    return true;
                                }
                            }
                            // home, area or coords?
                            HashMap<String, Object> set = new HashMap<>();
                            switch (args[2].toLowerCase(Locale.ENGLISH)) {
                                case "home" -> {
                                    // get home location
                                    HashMap<String, Object> wherehl = new HashMap<>();
                                    wherehl.put("tardis_id", id);
                                    ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
                                    if (!rsh.resultSet()) {
                                        TardisMessage.send(sender, "HOME_NOT_FOUND");
                                        return true;
                                    }
                                    set.put("world", rsh.getWorld().getName());
                                    set.put("x", rsh.getX());
                                    set.put("y", rsh.getY());
                                    set.put("z", rsh.getZ());
                                    set.put("direction", rsh.getDirection().toString());
                                    set.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
                                    if (!rsh.getPreset().isEmpty()) {
                                        // set the chameleon preset
                                        HashMap<String, Object> wherep = new HashMap<>();
                                        wherep.put("tardis_id", id);
                                        HashMap<String, Object> setp = new HashMap<>();
                                        setp.put("chameleon_preset", rsh.getPreset());
                                        // set chameleon adaption to OFF
                                        setp.put("adapti_on", 0);
                                        plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
                                    }
                                }
                                case "area" -> {
                                    if (args.length < 4) {
                                        TardisMessage.send(sender, "TOO_FEW_ARGS");
                                        return true;
                                    }
                                    // check area name
                                    HashMap<String, Object> wherea = new HashMap<>();
                                    wherea.put("area_name", args[3]);
                                    ResultSetAreas rsa = new ResultSetAreas(plugin, wherea, false, false);
                                    if (!rsa.resultSet()) {
                                        TardisMessage.send(sender, "AREA_NOT_FOUND", ChatColor.GREEN + "/tardis list areas" + ChatColor.RESET);
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        // must use advanced console if difficulty hard
                                        if (plugin.getDifficulty().equals(Difficulty.HARD)) {
                                            TardisMessage.send(sender, "ADV_AREA");
                                            return true;
                                        }
                                        // check permission
                                        String perm = "tardis.area." + args[3];
                                        if ((!TardisPermission.hasPermission(p, perm) && !TardisPermission.hasPermission(p, "tardis.area.*"))) {
                                            TardisMessage.send(sender, "TRAVEL_NO_AREA_PERM", args[3]);
                                            return true;
                                        }
                                    }
                                    // check whether this is a no invisibility area
                                    String invisibility = rsa.getArea().getInvisibility();
                                    if (invisibility.equals("DENY") && tardis.getPreset().equals(Preset.INVISIBLE)) {
                                        // check preset
                                        TardisMessage.send(sender, "AREA_NO_INVISIBLE");
                                        return true;
                                    } else if (!invisibility.equals("ALLOW")) {
                                        // force preset
                                        TardisMessage.send(sender, "AREA_FORCE_PRESET", invisibility);
                                        HashMap<String, Object> wherei = new HashMap<>();
                                        wherei.put("tardis_id", id);
                                        HashMap<String, Object> seti = new HashMap<>();
                                        seti.put("chameleon_preset", invisibility);
                                        // set chameleon adaption to OFF
                                        seti.put("adapti_on", 0);
                                        plugin.getQueryFactory().doSyncUpdate("tardis", seti, wherei);
                                    }
                                    // get a landing spot
                                    Location l = plugin.getTardisArea().getNextSpot(rsa.getArea().getAreaName());
                                    // returns null if full!
                                    if (l == null) {
                                        TardisMessage.send(sender, "NO_MORE_SPOTS");
                                        return true;
                                    }
                                    set.put("world", Objects.requireNonNull(l.getWorld()).getName());
                                    set.put("x", l.getBlockX());
                                    set.put("y", l.getBlockY());
                                    set.put("z", l.getBlockZ());
                                    set.put("submarine", 0);
                                }
                                default -> {
                                    // coords
                                    if (args.length < 6) {
                                        TardisMessage.send(sender, "ARG_COORDS");
                                        return true;
                                    }
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!TardisPermission.hasPermission(p, "tardis.timetravel.location")) {
                                            TardisMessage.send(sender, "NO_PERMS");
                                            return true;
                                        }
                                    }
                                    int x, y, z;
                                    World w = TardisAliasResolver.getWorldFromAlias(args[2]);
                                    if (w == null) {
                                        TardisMessage.send(sender, "WORLD_NOT_FOUND");
                                        return true;
                                    }
                                    if (!plugin.getPlanetsConfig().getBoolean("planets." + w.getName() + ".time_travel")) {
                                        TardisMessage.send(sender, "NO_WORLD_TRAVEL");
                                        return true;
                                    }
                                    if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && args[2].equals(plugin.getConfig().getString("creation.default_world_name"))) {
                                        TardisMessage.send(sender, "NO_WORLD_TRAVEL");
                                        return true;
                                    }
                                    x = TardisNumberParsers.parseInt(args[args.length - 3]);
                                    y = TardisNumberParsers.parseInt(args[args.length - 2]);
                                    if (y == 0) {
                                        TardisMessage.send(sender, "Y_NOT_VALID");
                                        return true;
                                    }
                                    z = TardisNumberParsers.parseInt(args[args.length - 1]);
                                    Location location = new Location(w, x, y, z);
                                    // check location
                                    if (!plugin.getTardisArea().areaCheckInExisting(location)) {
                                        TardisMessage.send(sender, "TRAVEL_IN_AREA", ChatColor.AQUA + "/tardisremote [player] travel area [area name]");
                                        return true;
                                    }
                                    // check respect if not admin
                                    if ((sender instanceof Player && !sender.hasPermission("tardis.admin")) || sender instanceof BlockCommandSender) {
                                        if (!plugin.getPluginRespect().getRespect(location, new Parameters(p.getPlayer(), Flag.getDefaultFlags()))) {
                                            return true;
                                        }
                                    }
                                    HashMap<String, Object> wherecl = new HashMap<>();
                                    wherecl.put("tardis_id", id);
                                    ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
                                    if (!rsc.resultSet()) {
                                        TardisMessage.send(sender, "CURRENT_NOT_FOUND");
                                        return true;
                                    }
                                    // check location
                                    int[] start_loc = TardisTimeTravel.getStartLocation(location, rsc.getDirection());
                                    int count = TardisTimeTravel.safeLocation(start_loc[0], location.getBlockY(), start_loc[2], start_loc[1], start_loc[3], location.getWorld(), rsc.getDirection());
                                    if (count > 0) {
                                        TardisMessage.send(sender, "NOT_SAFE");
                                        return true;
                                    } else {
                                        set.put("world", Objects.requireNonNull(location.getWorld()).getName());
                                        set.put("x", location.getBlockX());
                                        set.put("y", location.getBlockY());
                                        set.put("z", location.getBlockZ());
                                        set.put("submarine", 0);
                                    }
                                }
                            }
                            HashMap<String, Object> wheret = new HashMap<>();
                            wheret.put("tardis_id", id);
                            plugin.getQueryFactory().doUpdate("next", set, wheret);
                            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                                OfflinePlayer offlinePlayer = plugin.getServer().getOfflinePlayer(uuid);
                                String success = (new TardisRemoteTravelCommand(plugin).doTravel(id, offlinePlayer, sender)) ? plugin.getLanguage().getString("SUCCESS_Y") : plugin.getLanguage().getString("SUCCESS_N");
                                TardisMessage.send(sender, "REMOTE_SUCCESS", success);
                            }, 5L);
                            return true;
                    }
                } catch (IllegalArgumentException e) {
                    TardisMessage.send(sender, "CMD_NOT_VALID");
                    return false;
                }
            }
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        if (args.length == 1) {
            return null;
        } else if (args.length == 2) {
            return partial(args[1], rootSubs);
        } else if (args.length == 3 && args[1].equalsIgnoreCase("travel")) {
            return partial(args[2], travelSubs);
        } else if (args.length == 4 && args[2].equalsIgnoreCase("area")) {
            return partial(args[3], areaSubs);
        }
        return ImmutableList.of();
    }
}
