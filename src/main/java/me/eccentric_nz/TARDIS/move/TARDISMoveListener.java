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
package me.eccentric_nz.TARDIS.move;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetDoors;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISDoorLocation;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

/**
 *
 * @author eccentric_nz
 */
public class TARDISMoveListener implements Listener {

    private final TARDIS plugin;

    public TARDISMoveListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveToFromTARDIS(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        if (!plugin.getTrackerKeeper().getTrackMover().contains(p.getUniqueId())) {
            return;
        }
        Location l = event.getTo();
        Location loc = p.getLocation(); // Grab Location

        /**
         * Copyright (c) 2011, The Multiverse Team All rights reserved. Check
         * the Player has actually moved a block to prevent unneeded
         * calculations... This is to prevent huge performance drops on high
         * player count servers.
         */
        TARDISMoveSession tms = plugin.getTrackerKeeper().getTARDISMoveSession(p);
        tms.setStaleLocation(loc);

        // If the location is stale, ie: the player isn't actually moving xyz coords, they're looking around
        if (tms.isStaleLocation()) {
            return;
        }
        // check the block they're on
        MoveResult mvres = checkMoveLocation(l, p);
        if (mvres.getWhich() != 0) {
            HashMap<String, Object> wherepp = new HashMap<String, Object>();
            wherepp.put("uuid", p.getUniqueId().toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            boolean hasPrefs = rsp.resultSet();
            boolean minecart = (hasPrefs) ? rsp.isMinecartOn() : false;
            boolean userQuotes = (hasPrefs) ? rsp.isQuotesOn() : false;
            boolean isExit = (mvres.getWhich() == 2);
            // tp player
            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, mvres.getTo(), isExit, l.getWorld(), userQuotes, 0, minecart);
            //plugin.getTrackerKeeper().getTrackMover().remove(p.getUniqueId());
            TARDISMessage.send(p, plugin.getPluginName() + "Don't forget to close the door!");
        }
    }

    private MoveResult checkMoveLocation(Location l, Player player) {
        MoveResult mr = new MoveResult();
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("world", l.getWorld().getName());
        wherec.put("x", l.getBlockX());
        wherec.put("y", l.getBlockY());
        wherec.put("z", l.getBlockZ());
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (rsc.resultSet()) {
            mr.setWhich(1);
            float yaw = player.getLocation().getYaw();
            float pitch = player.getLocation().getPitch();
            COMPASS pd = COMPASS.valueOf(plugin.getUtils().getPlayersDirection(player, false));
            // get inner TARDIS location
            TARDISDoorLocation idl = plugin.getGeneralKeeper().getDoorListener().getDoor(1, rsc.getTardis_id());
            Location door_loc = idl.getL();
            World cw = idl.getW();
            COMPASS innerD = idl.getD();
            cw.getChunkAt(door_loc).load();
            door_loc.setPitch(pitch);
            // get inner door direction so we can adjust yaw if necessary
            if (!innerD.equals(pd)) {
                yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw(pd, innerD);
            }
            door_loc.setYaw(yaw);
            mr.setTo(door_loc);
            // put player into travellers table
            HashMap<String, Object> set = new HashMap<String, Object>();
            set.put("tardis_id", rsc.getTardis_id());
            set.put("uuid", player.getUniqueId().toString());
            new QueryFactory(plugin).doInsert("travellers", set);
        } else {
            String door_loc = l.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
            HashMap<String, Object> whered = new HashMap<String, Object>();
            whered.put("door_type", 1);
            whered.put("door_location", door_loc);
            ResultSetDoors rsd = new ResultSetDoors(plugin, whered, false);
            if (rsd.resultSet()) {
                int id = rsd.getTardis_id();
                COMPASS dd = rsd.getDoor_direction();
                mr.setWhich(2);
                float yaw = player.getLocation().getYaw();
                float pitch = player.getLocation().getPitch();
                // get outer TARDIS location
                Location exit;
                COMPASS d;
                HashMap<String, Object> wheret = new HashMap<String, Object>();
                wheret.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, wheret, "", false);
                if (rs.resultSet() && rs.isHandbrake_on()) {
                    if (rs.getPreset().hasDoor()) {
                        HashMap<String, Object> other = new HashMap<String, Object>();
                        other.put("tardis_id", id);
                        other.put("door_type", 0);
                        ResultSetDoors rse = new ResultSetDoors(plugin, other, false);
                        rse.resultSet();
                        d = rse.getDoor_direction();
                        exit = plugin.getUtils().getLocationFromDB(rse.getDoor_location(), yaw, pitch);
                        exit.setX(exit.getX() + 0.5);
                        exit.setZ(exit.getZ() + 0.5);
                    } else {
                        HashMap<String, Object> wherecl = new HashMap<String, Object>();
                        wherecl.put("tardis_id", id);
                        ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
                        rscl.resultSet();
                        d = rsc.getDirection();
                        exit = new Location(rsc.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ(), yaw, pitch);
                        switch (d) {
                            case NORTH:
                                exit.setX(exit.getX() + 0.5);
                                exit.setZ(exit.getZ() + 2.5);
                                break;
                            case EAST:
                                exit.setX(exit.getX() - 1.5);
                                exit.setZ(exit.getZ() + 0.5);
                                break;
                            case SOUTH:
                                exit.setX(exit.getX() + 0.5);
                                exit.setZ(exit.getZ() - 1.5);
                                break;
                            case WEST:
                                exit.setX(exit.getX() + 2.5);
                                exit.setZ(exit.getZ() + 0.5);
                                break;
                        }
                    }
                    if (!dd.equals(d)) {
                        yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw(dd, d);
                    }
                    exit.setYaw(yaw);
                    mr.setTo(exit);
                    // remove player from tarvellers table
                    plugin.getGeneralKeeper().getDoorListener().removeTraveller(player.getUniqueId());
                } else {
                    // send message handbrake must be on?
                    TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.LOST_IN_VORTEX.getText());
                    mr.setWhich(0);
                }
            } else {
                mr.setWhich(0);
            }
        }
        return mr;
    }

    public class MoveResult {

        private int which;
        private Location to;

        public int getWhich() {
            return which;
        }

        public void setWhich(int which) {
            this.which = which;
        }

        public Location getTo() {
            return to;
        }

        public void setTo(Location to) {
            this.to = to;
        }
    }
}
