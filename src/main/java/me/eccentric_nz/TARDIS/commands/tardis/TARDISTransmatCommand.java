/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.tardis;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.TARDISBoundTransmatRemoval;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.database.resultset.*;
import me.eccentric_nz.TARDIS.desktop.PreviewData;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.planets.RoomsWorld;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.regex.Pattern;

class TARDISTransmatCommand {

    private static final Pattern LETTERS_NUMBERS = Pattern.compile("[A-Za-z0-9_]{2,16}");
    private final TARDIS plugin;

    TARDISTransmatCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean teleportOrProcess(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.transmat")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        // must be a time lord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
            return true;
        }
        // player is in TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_IN_TARDIS");
            return false;
        }
        int id = rs.getTardisId();
        int thisid = rst.getTardis_id();
        if (thisid != id) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_ONLY_TL");
            return false;
        }
        if (args[1].equalsIgnoreCase("list")) {
            Transmat transmat = null;
            if (plugin.getPlanetsConfig().getBoolean("planets.rooms.enabled") && plugin.getServer().getWorld("rooms") != null) {
                transmat = new RoomsWorld().getTransmat(plugin);
            }
            ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
            if (rslist.resultSet()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_LIST");
                for (Transmat t : rslist.getData()) {
                    plugin.getMessenger().sendTransmat(player, t);
                }
                if (transmat != null) {
                    plugin.getMessenger().sendTransmat(player, transmat);
                }
            } else {
                if (transmat != null) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_LIST");
                    plugin.getMessenger().sendTransmat(player, transmat);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NO_LIST");
                }
            }
            return true;
        }
        if (args.length < 3) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return false;
        }
        if (args[1].equalsIgnoreCase("tp")) {
            // transmat to specified location
            if (args[2].equalsIgnoreCase("console")) {
                // get internal door location
                plugin.getGeneralKeeper().getRendererListener().transmat(player);
            } else {
                ResultSetTransmat rsm;
                boolean isRoomsWorld;
                if (args[2].startsWith("Rooms") && args.length == 4 && TARDISPermission.hasPermission(player, "tardis.transmat.rooms")) {
                    rsm = new ResultSetTransmat(plugin, -1, "rooms");
                    isRoomsWorld = true;
                } else {
                    rsm = new ResultSetTransmat(plugin, id, args[2]);
                    isRoomsWorld = false;
                }
                if (rsm.resultSet()) {
                    if (isRoomsWorld) {
                        plugin.getTrackerKeeper().getPreviewers().put(player.getUniqueId(), new PreviewData(player.getLocation().clone(), player.getGameMode(), id));
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT");
                    Location tp_loc = rsm.getLocation();
                    tp_loc.setYaw(rsm.getYaw());
                    tp_loc.setPitch(player.getLocation().getPitch());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.teleport(tp_loc);
                        if (isRoomsWorld) {
                            plugin.getMessenger().send(player, TardisModule.TARDIS, "PREVIEW_DONE");
                        }
                    }, 10L);
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NOT_FOUND");
                }
            }
            return true;
        } else {
            Location location = player.getLocation();
            if (args[1].equalsIgnoreCase("add")) {
                // must be in their TARDIS
                if (!plugin.getUtils().inTARDISWorld(location)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "CMD_IN_WORLD");
                    return true;
                }
                // get the transmat name
                if (!LETTERS_NUMBERS.matcher(args[2]).matches()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "SAVE_NAME_NOT_VALID");
                    return true;
                }
                // check if transmat name exists
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_EXISTS");
                    return true;
                } else {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("tardis_id", id);
                    set.put("name", args[2]);
                    set.put("world", location.getWorld().getName());
                    set.put("x", location.getX());
                    set.put("y", location.getY());
                    set.put("z", location.getZ());
                    set.put("yaw", location.getYaw());
                    plugin.getQueryFactory().doInsert("transmats", set);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_SAVED");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("update")) {
                // check if transmat name exists
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("world", location.getWorld().getName());
                    set.put("x", location.getX());
                    set.put("y", location.getY());
                    set.put("z", location.getZ());
                    set.put("yaw", location.getYaw());
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    where.put("name", args[2]);
                    plugin.getQueryFactory().doUpdate("transmats", set, where);
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_SAVED");
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NOT_FOUND");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("transmat_id", rsm.getTransmat_id());
                    plugin.getQueryFactory().doDelete("transmats", wherer);
                    // check for bound transmat
                    HashMap<String, Object> whered = new HashMap<>();
                    whered.put("tardis_id", id);
                    whered.put("name", args[2]);
                    ResultSetBind rsd = new ResultSetBind(plugin, whered);
                    if (rsd.resultSet()) {
                        new TARDISBoundTransmatRemoval(plugin, id, args[2]).unbind();
                    }
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_REMOVED");
                } else {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TRANSMAT_NOT_FOUND");
                }
            }
        }
        return false;
    }
}
