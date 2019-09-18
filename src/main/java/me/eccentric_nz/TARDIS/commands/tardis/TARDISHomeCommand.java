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
import me.eccentric_nz.TARDIS.database.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.DIFFICULTY;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
class TARDISHomeCommand {

    private final TARDIS plugin;

    TARDISHomeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setHome(Player player, String[] args) {
        if (player.hasPermission("tardis.timetravel")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                TARDISMessage.send(player, "NOT_A_TIMELORD");
                return false;
            }
            int id = rs.getTardis_id();
            Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
            COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
            if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                return true;
            }
            if (!plugin.getTardisArea().areaCheckInExisting(eyeLocation)) {
                TARDISMessage.send(player, "AREA_NO_HOME", ChatColor.AQUA + "/tardistravel area [area name]");
                return true;
            }
            if (!plugin.getPluginRespect().getRespect(eyeLocation, new Parameters(player, FLAG.getDefaultFlags()))) {
                return true;
            }
            Material m = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getType();
            if (m != Material.SNOW) {
                int yplusone = eyeLocation.getBlockY();
                eyeLocation.setY(yplusone + 1);
            }
            // check the world is not excluded
            String world = eyeLocation.getWorld().getName();
            if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                TARDISMessage.send(player, "NO_WORLD_TRAVEL");
                return true;
            }
            TARDISCircuitChecker tcc = null;
            if (!plugin.getDifficulty().equals(DIFFICULTY.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
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
                TARDISMessage.send(player, "TARDIS_NO_INSIDE");
                return true;
            }
            // check it is not another Time Lords home location
            HashMap<String, Object> where = new HashMap<>();
            where.put("world", eyeLocation.getWorld().getName());
            where.put("x", eyeLocation.getBlockX());
            where.put("y", eyeLocation.getBlockY());
            where.put("z", eyeLocation.getBlockZ());
            ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, where);
            if (rsh.resultSet()) {
                TARDISMessage.send(player, "TARDIS_NO_HOME");
                return true;
            }
            HashMap<String, Object> tid = new HashMap<>();
            HashMap<String, Object> set = new HashMap<>();
            tid.put("tardis_id", id);
            set.put("world", eyeLocation.getWorld().getName());
            set.put("x", eyeLocation.getBlockX());
            set.put("y", eyeLocation.getBlockY());
            set.put("z", eyeLocation.getBlockZ());
            set.put("direction", player_d.toString());
            set.put("submarine", isSub(eyeLocation) ? 1 : 0);
            plugin.getQueryFactory().doUpdate("homes", set, tid);
            TARDISMessage.send(player, "HOME_SET");
            return true;
        } else {
            TARDISMessage.send(player, "NO_PERMS");
            return false;
        }
    }

    private boolean isSub(Location l) {
        return l.getBlock().getType().equals(Material.WATER);
    }
}
