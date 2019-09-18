/*
 * Copyright (C) 2019 eccentric_nz
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
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISSetDestinationCommand {

    private final TARDIS plugin;

    TARDISSetDestinationCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doSetDestination(Player player, String[] args) {
        if (player.hasPermission("tardis.save")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NO_TARDIS");
                return false;
            }
            if (args.length < 2) {
                TARDISMessage.send(player, "TOO_FEW_ARGS");
                return false;
            }
            if (!args[1].matches("[A-Za-z0-9_]{2,16}")) {
                TARDISMessage.send(player, "DEST_NAME_NOT_VALID");
                return false;
            } else if (args[1].equalsIgnoreCase("hide") || args[1].equalsIgnoreCase("rebuild") || args[1].equalsIgnoreCase("home")) {
                TARDISMessage.send(player, "SAVE_RESERVED");
                return false;
            } else {
                int id = rs.getTardis_id();
                TARDISCircuitChecker tcc = null;
                if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, true)) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasMemory()) {
                    TARDISMessage.send(player, "NO_MEM_CIRCUIT");
                    return true;
                }
                // check they are not in the tardis
                HashMap<String, Object> wherettrav = new HashMap<>();
                wherettrav.put("uuid", player.getUniqueId().toString());
                wherettrav.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                if (rst.resultSet()) {
                    TARDISMessage.send(player, "NO_PB_IN_TARDIS");
                    return true;
                }
                // get location player is looking at
                Block b = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50);
                Location l = b.getLocation();
                if (!plugin.getTardisArea().areaCheckInExisting(l)) {
                    TARDISMessage.send(player, "AREA_NO_SETDEST", ChatColor.AQUA + "/tardistravel area [area name]");
                    return true;
                }
                String world = l.getWorld().getName();
                if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && world.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                    return true;
                }
                // check the world is not excluded
                if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                    TARDISMessage.send(player, "NO_PB_IN_WORLD");
                    return true;
                }
                if (!plugin.getPluginRespect().getRespect(l, new Parameters(player, FLAG.getDefaultFlags()))) {
                    return true;
                }
                if (player.hasPermission("tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
                    String areaPerm = plugin.getTardisArea().getExileArea(player);
                    if (plugin.getTardisArea().areaCheckInExile(areaPerm, l)) {
                        TARDISMessage.send(player, "EXILE_NO_TRAVEL");
                        return false;
                    }
                }
                String dw = l.getWorld().getName();
                int dx = l.getBlockX();
                int dy = l.getBlockY() + 1;
                int dz = l.getBlockZ();
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("dest_name", args[1]);
                set.put("world", dw);
                set.put("x", dx);
                set.put("y", dy);
                set.put("z", dz);
                if (plugin.getQueryFactory().doSyncInsert("destinations", set) < 0) {
                    return false;
                } else {
                    TARDISMessage.send(player, "DEST_SAVED", args[1]);
                    return true;
                }
            }
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }
}
