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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISEnterCommand {

    private final TARDIS plugin;

    public TARDISEnterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean enterTARDIS(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            sender.sendMessage(plugin.pluginName + "Only a player can run this command!");
            return true;
        }
        if (!player.hasPermission("tardis.skeletonkey")) {
            sender.sendMessage(plugin.pluginName + "You do not have permission to run this command!");
            return true;
        }
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("owner", args[1]);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            int id = rs.getTardis_id();
            HashMap<String, Object> wherei = new HashMap<String, Object>();
            wherei.put("door_type", 1);
            wherei.put("tardis_id", id);
            ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
            if (rsi.resultSet()) {
                TARDISConstants.COMPASS innerD = rsi.getDoor_direction();
                String doorLocStr = rsi.getDoor_location();
                String[] split = doorLocStr.split(":");
                World cw = plugin.getServer().getWorld(split[0]);
                int cx = 0, cy = 0, cz = 0;
                try {
                    cx = Integer.parseInt(split[1]);
                    cy = Integer.parseInt(split[2]);
                    cz = Integer.parseInt(split[3]);
                } catch (NumberFormatException nfe) {
                    plugin.debug("Could not convert to number!");
                }
                Location tmp_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                int getx = tmp_loc.getBlockX();
                int getz = tmp_loc.getBlockZ();
                switch (innerD) {
                    case NORTH:
                        // z -ve
                        tmp_loc.setX(getx + 0.5);
                        tmp_loc.setZ(getz - 0.5);
                        break;
                    case EAST:
                        // x +ve
                        tmp_loc.setX(getx + 1.5);
                        tmp_loc.setZ(getz + 0.5);
                        break;
                    case SOUTH:
                        // z +ve
                        tmp_loc.setX(getx + 0.5);
                        tmp_loc.setZ(getz + 1.5);
                        break;
                    case WEST:
                        // x -ve
                        tmp_loc.setX(getx - 0.5);
                        tmp_loc.setZ(getz + 0.5);
                        break;
                }
                // enter TARDIS!
                cw.getChunkAt(tmp_loc).load();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                tmp_loc.setPitch(pitch);
                // get players direction so we can adjust yaw if necessary
                TARDISConstants.COMPASS d = TARDISConstants.COMPASS.valueOf(plugin.utils.getPlayersDirection(player, false));
                if (!innerD.equals(d)) {
                    switch (d) {
                        case NORTH:
                            yaw += plugin.doorListener.adjustYaw[0][innerD.ordinal()];
                            break;
                        case WEST:
                            yaw += plugin.doorListener.adjustYaw[1][innerD.ordinal()];
                            break;
                        case SOUTH:
                            yaw += plugin.doorListener.adjustYaw[2][innerD.ordinal()];
                            break;
                        case EAST:
                            yaw += plugin.doorListener.adjustYaw[3][innerD.ordinal()];
                            break;
                    }
                }
                tmp_loc.setYaw(yaw);
                final Location tardis_loc = tmp_loc;
                World playerWorld = player.getLocation().getWorld();
                plugin.doorListener.movePlayer(player, tardis_loc, false, playerWorld, false, 3);
                // put player into travellers table
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("tardis_id", id);
                set.put("player", player.getName());
                qf.doInsert("travellers", set);
                return true;
            }
        }
        sender.sendMessage(plugin.pluginName + args[1] + " has not created a TARDIS yet!");
        return true;
    }
}
