/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class EnterCommand {

    private final TARDIS plugin;

    public EnterCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean enterTARDIS(CommandSender sender, Player timelord) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
            return true;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return true;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", timelord.getUniqueId().toString());
        where.put("abandoned", 0);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            int id = tardis.getTardisId();
            String owner = tardis.getOwner();
            process(player, owner, id);
            return true;
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
        }
        return true;
    }

    public void enterTARDIS(CommandSender sender, int id) {
        Player player = null;
        if (sender instanceof Player) {
            player = (Player) sender;
        }
        if (player == null) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "CMD_PLAYER");
            return;
        }
        if (!TARDISPermission.hasPermission(player, "tardis.skeletonkey")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PERMS");
            return;
        }
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            String owner = tardis.getOwner();
            process(player, owner, id);
        } else {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ABANDONED_NOT_FOUND");
        }
    }

    private void process(Player player, String owner, int id) {
        HashMap<String, Object> wherei = new HashMap<>();
        wherei.put("door_type", 1);
        wherei.put("tardis_id", id);
        ResultSetDoors rsi = new ResultSetDoors(plugin, wherei, false);
        if (rsi.resultSet()) {
            COMPASS innerD = rsi.getDoor_direction();
            String doorLocStr = rsi.getDoor_location();
            World cw = TARDISStaticLocationGetters.getWorldFromSplitString(doorLocStr);
            Location tardis_loc = TARDISStaticLocationGetters.getLocationFromDB(doorLocStr);
            int getx = tardis_loc.getBlockX();
            int getz = tardis_loc.getBlockZ();
            switch (innerD) {
                case NORTH -> {
                    // z -ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz - 0.5);
                }
                case EAST -> {
                    // x +ve
                    tardis_loc.setX(getx + 1.5);
                    tardis_loc.setZ(getz + 0.5);
                }
                case SOUTH -> {
                    // z +ve
                    tardis_loc.setX(getx + 0.5);
                    tardis_loc.setZ(getz + 1.5);
                }
                // WEST
                default -> {
                    // x -ve
                    tardis_loc.setX(getx - 0.5);
                    tardis_loc.setZ(getz + 0.5);
                }
            }
            // if WorldGuard is on the server check for TARDIS region protection and add admin as member
            if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                plugin.getWorldGuardUtils().addMemberToRegion(cw, owner, player.getUniqueId());
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
                    case NORTH -> yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[0][innerD.ordinal() / 2];
                    case WEST -> yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[1][innerD.ordinal() / 2];
                    case SOUTH -> yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[2][innerD.ordinal() / 2];
                    // EAST
                    default -> yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw[3][innerD.ordinal() / 2];
                }
            }
            tardis_loc.setYaw(yaw);
            World playerWorld = player.getLocation().getWorld();
            plugin.getGeneralKeeper().getDoorListener().movePlayer(player, tardis_loc, false, playerWorld, false, 3, true, false);
            // put player into travellers table
            HashMap<String, Object> set = new HashMap<>();
            set.put("tardis_id", id);
            set.put("uuid", player.getUniqueId().toString());
            plugin.getQueryFactory().doInsert("travellers", set);
        }
    }
}
