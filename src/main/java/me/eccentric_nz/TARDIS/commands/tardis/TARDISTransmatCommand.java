/*
 * Copyright (C) 2020 eccentric_nz
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
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTransmat;
import me.eccentric_nz.TARDIS.database.ResultSetTransmatList;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Transmat;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.HashMap;

class TARDISTransmatCommand {

    private final TARDIS plugin;

    TARDISTransmatCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean teleportOrProcess(Player player, String[] args) {
        if (args.length < 3) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return false;
        }
        if (!player.hasPermission("tardis.transmat")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        // must be a time lord
        ResultSetTardisID rs = new ResultSetTardisID(plugin);
        if (!rs.fromUUID(player.getUniqueId().toString())) {
            TARDISMessage.send(player, "NOT_A_TIMELORD");
            return true;
        }
        // player is in TARDIS
        HashMap<String, Object> wheret = new HashMap<>();
        wheret.put("uuid", player.getUniqueId().toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wheret, false);
        if (!rst.resultSet()) {
            TARDISMessage.send(player, "NOT_IN_TARDIS");
            return false;
        }
        int id = rs.getTardis_id();
        int thisid = rst.getTardis_id();
        if (thisid != id) {
            TARDISMessage.send(player, "CMD_ONLY_TL");
            return false;
        }
        if (args[1].equalsIgnoreCase("tp")) {
            // transmat to specified location
            if (args[2].equalsIgnoreCase("console")) {
                // get internal door location
                plugin.getGeneralKeeper().getRendererListener().transmat(player);
            } else {
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    TARDISMessage.send(player, "TRANSMAT");
                    Location tp_loc = rsm.getLocation();
                    tp_loc.setYaw(rsm.getYaw());
                    tp_loc.setPitch(player.getLocation().getPitch());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        player.playSound(tp_loc, Sound.ENTITY_ENDERMAN_TELEPORT, 1.0f, 1.0f);
                        player.teleport(tp_loc);
                    }, 10L);
                } else {
                    TARDISMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
            }
            return true;
        } else {
            Location location = player.getLocation();
            if (args[1].equalsIgnoreCase("add")) {
                // must be in their TARDIS
                if (!plugin.getUtils().inTARDISWorld(location)) {
                    TARDISMessage.send(player, "CMD_IN_WORLD");
                    return true;
                }
                // get the transmat name
                if (!args[2].matches("[A-Za-z0-9_]{2,16}")) {
                    TARDISMessage.send(player, "SAVE_NAME_NOT_VALID");
                    return true;
                }
                // check if transmat name exists
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    TARDISMessage.send(player, "TRANSMAT_EXISTS");
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
                    TARDISMessage.send(player, "TRANSMAT_SAVED");
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
                    TARDISMessage.send(player, "TRANSMAT_SAVED");
                } else {
                    TARDISMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
                return true;
            } else if (args[1].equalsIgnoreCase("remove")) {
                ResultSetTransmat rsm = new ResultSetTransmat(plugin, id, args[2]);
                if (rsm.resultSet()) {
                    HashMap<String, Object> wherer = new HashMap<>();
                    wherer.put("transmat_id", rsm.getTransmat_id());
                    plugin.getQueryFactory().doDelete("transmats", wherer);
                    TARDISMessage.send(player, "TRANSMAT_REMOVED");
                } else {
                    TARDISMessage.send(player, "TRANSMAT_NOT_FOUND");
                }
            } else if (args[1].equalsIgnoreCase("list")) {
                ResultSetTransmatList rslist = new ResultSetTransmatList(plugin, id);
                if (rslist.resultSet()) {
                    TARDISMessage.send(player, "TRANSMAT_LIST");
                    for (Transmat t : rslist.getData()) {
                        String entry = String.format(" %s, %.2f, %.2f, %.2f, yaw %.2f", t.getWorld(), t.getX(), t.getY(), t.getZ(), t.getYaw());
                        player.sendMessage(ChatColor.GREEN + t.getName() + ChatColor.RESET + entry);
                    }
                } else {
                    TARDISMessage.send(player, "TRANSMAT_NO_LIST");
                }
                return true;
            }
        }
        return false;
    }
}
