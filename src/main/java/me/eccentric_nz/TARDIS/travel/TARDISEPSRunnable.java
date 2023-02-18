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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.floodgate.TARDISFloodgate;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandException;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Emergency Programme One was a feature of the Doctor's TARDIS designed to return a companion to a designated place in
 * case of extreme emergency.
 *
 * @author eccentric_nz
 */
public class TARDISEPSRunnable implements Runnable {

    private final TARDIS plugin;
    private final String message;
    private final Player tl;
    private final List<UUID> players;
    private final int id;
    private final String eps;
    private final String creeper;

    public TARDISEPSRunnable(TARDIS plugin, String message, Player tl, List<UUID> players, int id, String eps, String creeper) {
        this.plugin = plugin;
        this.message = message;
        this.tl = tl;
        this.players = players;
        this.id = id;
        this.eps = eps;
        this.creeper = creeper;
    }

    /**
     * Determines the angle of a straight line drawn between point one and two. The number returned, which is a double
     * in degrees, tells us how much we have to rotate a horizontal line clockwise for it to match the line between the
     * two points.
     *
     * @param px1 the x coordinate of the first point
     * @param pz1 the z coordinate of the first point
     * @param px2 the x coordinate of the second point
     * @param pz2 the z coordinate of the second point
     * @return the head angle of EP1
     */
    private static float getCorrectYaw(double px1, double pz1, double px2, double pz2) {
        double xDiff = px2 - px1;
        double zDiff = pz2 - pz1;
        return (float) Math.toDegrees(Math.atan2(zDiff, xDiff)) + 90.0f;
    }

    @Override
    public void run() {
        Location l = getSpawnLocation(id);
        if (l != null) {
            try {
                TARDISSounds.playTARDISSound(l, "tardis_takeoff");
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> TARDISSounds.playTARDISSound(l, "tardis_land"), 490L);
                plugin.setTardisSpawn(true);
                // set yaw if npc spawn location has been changed
                if (!eps.isEmpty()) {
                    String[] creep = creeper.split(":");
                    double cx = TARDISNumberParsers.parseDouble(creep[1]);
                    double cz = TARDISNumberParsers.parseDouble(creep[3]);
                    float yaw = getCorrectYaw(cx, cz, l.getX(), l.getZ());
                    l.setYaw(yaw);
                }
                // create NPC
                int npcID = plugin.getTardisHelper().spawnEmergencyProgrammeOne(tl, l);
                players.forEach((p) -> {
                    Player pp = plugin.getServer().getPlayer(p);
                    if (pp != null) {
                        TARDISMessage.message(pp, ChatColor.RED + "[Emergency Programme One] " + ChatColor.RESET + message);
                    }
                });
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    players.forEach((p) -> {
                        Player pp = plugin.getServer().getPlayer(p);
                        if (pp != null) {
                            TARDISMessage.message(pp, ChatColor.RED + "[Emergency Programme One] " + ChatColor.RESET + plugin.getLanguage().getString("EP1_BYE"));
                        }
                    });
                    plugin.getTardisHelper().removeNPC(npcID, l.getWorld());
                }, 1000L);
            } catch (CommandException e) {
                plugin.debug(e.getMessage());
            }
        }
    }

    /**
     * @param id the TARDIS to look up
     * @return the EP1 spawn location
     */
    private Location getSpawnLocation(int id) {
        if (!eps.isEmpty()) {
            return TARDISStaticLocationGetters.getLocationFromDB(eps);
        } else if (plugin.getConfig().getBoolean("creation.create_worlds")) {
            // get world spawn location
            if (TARDISFloodgate.shouldReplacePrefix(tl.getName())) {
                return plugin.getServer().getWorld(TARDISFloodgate.getPlayerWorldName(tl.getName())).getSpawnLocation();
            } else {
                return plugin.getServer().getWorld("TARDIS_WORLD_" + tl.getName()).getSpawnLocation();
            }
        } else {
            HashMap<String, Object> where = new HashMap<>();
            where.put("tardis_id", id);
            where.put("door_type", 1);
            ResultSetDoors rsd = new ResultSetDoors(plugin, where, false);
            if (rsd.resultSet()) {
                double x;
                double z;
                Location location = TARDISStaticLocationGetters.getLocationFromDB(rsd.getDoor_location());
                switch (rsd.getDoor_direction()) {
                    case NORTH -> {
                        x = 0.5;
                        z = -1.5;
                    }
                    case EAST -> {
                        x = 1.5;
                        z = 0.5;
                    }
                    case WEST -> {
                        x = -1.5;
                        z = 0.5;
                    }
                    default -> { // SOUTH
                        x = 0.5;
                        z = 1.5;
                    }
                }
                return location.add(x, 0, z);
            } else {
                return null;
            }
        }
    }
}
