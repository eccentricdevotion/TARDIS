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
package me.eccentric_nz.TARDIS.commands.tardis;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.TARDISCircuitChecker;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISHomeCommand {

    private final TARDIS plugin;

    public TARDISHomeCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean setHome(Player player, String[] args) {
        if (player.hasPermission("tardis.timetravel")) {
            Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
            if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
                TARDISMessage.send(player, plugin.getPluginName() + "The server admin will not allow you to set the TARDIS home in this world!");
                return true;
            }
            if (!plugin.getTardisArea().areaCheckInExisting(eyeLocation)) {
                TARDISMessage.send(player, plugin.getPluginName() + "You cannot use /tardis home in a TARDIS area! Please use " + ChatColor.AQUA + " /tardistravel area [area name]");
                return true;
            }
            if (!plugin.getPluginRespect().getRespect(player, eyeLocation, true)) {
                return true;
            }
            Material m = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getType();
            if (m != Material.SNOW) {
                int yplusone = eyeLocation.getBlockY();
                eyeLocation.setY(yplusone + 1);
            }
            // check the world is not excluded
            String world = eyeLocation.getWorld().getName();
            if (!plugin.getConfig().getBoolean("worlds." + world)) {
                TARDISMessage.send(player, plugin.getPluginName() + "You cannot set the TARDIS home location to this world");
                return true;
            }
            HashMap<String, Object> where = new HashMap<String, Object>();
            where.put("owner", player.getName());
            ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
            if (!rs.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_A_TIMELORD.getText());
                return false;
            }
            int id = rs.getTardis_id();
            TARDISCircuitChecker tcc = null;
            if (plugin.getConfig().getString("preferences.difficulty").equals("hard")) {
                tcc = new TARDISCircuitChecker(plugin, id);
                tcc.getCircuits();
            }
            if (tcc != null && !tcc.hasMemory()) {
                TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_MEM_CIRCUIT.getText());
                return true;
            }
            // check they are not in the tardis
            HashMap<String, Object> wherettrav = new HashMap<String, Object>();
            wherettrav.put("player", player.getName());
            wherettrav.put("tardis_id", id);
            ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
            if (rst.resultSet()) {
                TARDISMessage.send(player, plugin.getPluginName() + "You cannot set the home location here because you are inside a TARDIS!");
                return true;
            }
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> tid = new HashMap<String, Object>();
            HashMap<String, Object> set = new HashMap<String, Object>();
            tid.put("tardis_id", id);
            set.put("world", eyeLocation.getWorld().getName());
            set.put("x", eyeLocation.getBlockX());
            set.put("y", eyeLocation.getBlockY());
            set.put("z", eyeLocation.getBlockZ());
            set.put("submarine", isSub(eyeLocation) ? 1 : 0);
            qf.doUpdate("homes", set, tid);
            TARDISMessage.send(player, plugin.getPluginName() + "The new TARDIS home was set!");
            return true;
        } else {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PERMS.getText());
            return false;
        }
    }

    private boolean isSub(Location l) {
        switch (l.getBlock().getType()) {
            case STATIONARY_WATER:
            case WATER:
                return true;
            default:
                return false;
        }
    }
}
