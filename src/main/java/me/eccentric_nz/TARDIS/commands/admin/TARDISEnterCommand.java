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
package me.eccentric_nz.TARDIS.commands.admin;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISEnterCommand {

    private final TARDIS plugin;

    TARDISEnterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean enterTARDIS(CommandSender sender, String[] args) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            TARDISMessage.send(sender, "CMD_PLAYER");
            return true;
        }
        if (!player.hasPermission("tardis.skeletonkey")) {
            TARDISMessage.send(player, "NO_PERMS");
            return true;
        }
        int tmp = -1;
        try {
            tmp = Integer.parseInt(args[1]);
        } catch (NumberFormatException nfe) {
            // do nothing
        }
        HashMap<String, Object> where = new HashMap<>();
        if (tmp == -1) {
            // Look up this player's UUID
            UUID uuid = plugin.getServer().getOfflinePlayer(args[1]).getUniqueId();
            where.put("uuid", uuid.toString());
            where.put("abandoned", 0);
        } else {
            where.put("tardis_id", tmp);
        }
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardis_id();
            String owner = tardis.getOwner();
            HashMap<String, Object> wherei = new HashMap<>();
            wherei.put("door_type", 1);
            wherei.put("tardis_id", id);
            ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
            if (rsi.resultSet()) {
                COMPASS innerD = rsi.getDoor_direction();
                String doorLocStr = rsi.getDoor_location();
                String[] split = doorLocStr.split(":");
                String w = (split[0].equals("TARDIS_TimeVortex") ? "tardis_time_vortex" : split[0].toLowerCase(Locale.ENGLISH));
                World cw = plugin.getServer().getWorld(w);
                int cx = TARDISNumberParsers.parseInt(split[1]);
                int cy = TARDISNumberParsers.parseInt(split[2]);
                int cz = TARDISNumberParsers.parseInt(split[3]);
                Location tardis_loc = cw.getBlockAt(cx, cy, cz).getLocation();
                int getx = tardis_loc.getBlockX();
                int getz = tardis_loc.getBlockZ();
                switch (innerD) {
                    case NORTH:
                        // z -ve
                        tardis_loc.setX(getx + 0.5);
                        tardis_loc.setZ(getz - 0.5);
                        break;
                    case EAST:
                        // x +ve
                        tardis_loc.setX(getx + 1.5);
                        tardis_loc.setZ(getz + 0.5);
                        break;
                    case SOUTH:
                        // z +ve
                        tardis_loc.setX(getx + 0.5);
                        tardis_loc.setZ(getz + 1.5);
                        break;
                    case WEST:
                        // x -ve
                        tardis_loc.setX(getx - 0.5);
                        tardis_loc.setZ(getz + 0.5);
                        break;
                }
                // if WorldGuard is on the server check for TARDIS region protection and add admin as member
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    plugin.getWorldGuardUtils().addMemberToRegion(cw, owner, player.getName());
                }
                // enter TARDIS!
                cw.getChunkAt(tardis_loc).load();
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                tardis_loc.setPitch(pitch);
                // get players direction so we can adjust yaw if necessary
                COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                if (!innerD.equals(d)) {
                    switch (d) {
                        case NORTH:
                            yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[0][innerD.ordinal()];
                            break;
                        case WEST:
                            yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[1][innerD.ordinal()];
                            break;
                        case SOUTH:
                            yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[2][innerD.ordinal()];
                            break;
                        case EAST:
                            yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[3][innerD.ordinal()];
                            break;
                    }
                }
                tardis_loc.setYaw(yaw);
                World playerWorld = player.getLocation().getWorld();
                plugin.getGeneralKeeper().getDoorListener().movePlayer(player, tardis_loc, false, playerWorld, false, 3, true);
                // put player into travellers table
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("uuid", player.getUniqueId().toString());
                qf.doInsert("travellers", set);
                return true;
            }
        } else {
            String message = (tmp == -1) ? "PLAYER_NO_TARDIS" : "ABANDONED_NOT_FOUND";
            TARDISMessage.send(player, message);
        }
        return true;
    }
}
