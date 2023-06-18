/*
 * Copyright (C) 2023 eccentric_nz
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

import java.util.HashMap;
import java.util.Locale;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardisID;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.ChameleonPreset;
import me.eccentric_nz.TARDIS.enumeration.Difficulty;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 * @author eccentric_nz
 */
class TARDISHomeCommand {

    private final TARDIS plugin;

    TARDISHomeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean setHome(Player player, String[] args) {
        if (TARDISPermission.hasPermission(player, "tardis.timetravel")) {
            ResultSetTardisID rs = new ResultSetTardisID(plugin);
            if (!rs.fromUUID(player.getUniqueId().toString())) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_A_TIMELORD");
                return false;
            }
            int id = rs.getTardis_id();
            if (args.length == 3 && args[1].equalsIgnoreCase("set")) {
                // set the PRESET for the home location
                String which = "";
                if (plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false).contains(args[2])) {
                    which = args[2];
                } else {
                    try {
                        which = args[2].toUpperCase(Locale.ENGLISH);
                        ChameleonPreset.valueOf(which);
                    } catch (IllegalArgumentException e) {
                        // abort
                        plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_PRESET");
                        return true;
                    }
                }
                if (!which.isEmpty()) {
                    // update home record
                    HashMap<String, Object> whereh = new HashMap<>();
                    whereh.put("tardis_id", id);
                    HashMap<String, Object> seth = new HashMap<>();
                    seth.put("preset", which);
                    plugin.getQueryFactory().doUpdate("homes", seth, whereh);
                    plugin.getMessenger().sendInsertedColour(player, "CHAM_SET", which, plugin);
                }
            } else {
                Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
                String world = eyeLocation.getWorld().getName();
                COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && world.equals(plugin.getConfig().getString("creation.default_world_name"))) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                    return true;
                }
                if (plugin.getTardisArea().isInExistingArea(eyeLocation)) {
                    plugin.getMessenger().sendColouredCommand(player, "AREA_NO_HOME", "/tardistravel area [area name]", plugin);
                    return true;
                }
                if (!plugin.getPluginRespect().getRespect(eyeLocation, new Parameters(player, Flag.getDefaultFlags()))) {
                    return true;
                }
                Material m = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getType();
                if (m != Material.SNOW) {
                    int yplusone = eyeLocation.getBlockY();
                    eyeLocation.setY(yplusone + 1);
                }
                // check the world is not excluded
                if (!plugin.getPlanetsConfig().getBoolean("planets." + world + ".time_travel")) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
                    return true;
                }
                TARDISCircuitChecker tcc = null;
                if (!plugin.getDifficulty().equals(Difficulty.EASY) && !plugin.getUtils().inGracePeriod(player, false)) {
                    tcc = new TARDISCircuitChecker(plugin, id);
                    tcc.getCircuits();
                }
                if (tcc != null && !tcc.hasMemory()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_MEM_CIRCUIT");
                    return true;
                }
                // check they are not in the tardis
                HashMap<String, Object> wherettrav = new HashMap<>();
                wherettrav.put("uuid", player.getUniqueId().toString());
                wherettrav.put("tardis_id", id);
                ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
                if (rst.resultSet()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_NO_INSIDE");
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
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "TARDIS_NO_HOME");
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
                plugin.getMessenger().send(player, TardisModule.TARDIS, "HOME_SET");
            }
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return false;
        }
    }

    private boolean isSub(Location l) {
        return l.getBlock().getType().equals(Material.WATER);
    }
}
