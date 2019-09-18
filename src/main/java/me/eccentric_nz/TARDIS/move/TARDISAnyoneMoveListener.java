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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPet;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISAnyoneMoveListener implements Listener {

    private final TARDIS plugin;

    public TARDISAnyoneMoveListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveToFromTARDIS(PlayerMoveEvent event) {
        Player p = event.getPlayer();
        Location l = new Location(event.getTo().getWorld(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ(), 0.0f, 0.0f);
        Location loc = p.getLocation(); // Grab Location

        /*
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
        if (plugin.getTrackerKeeper().getPortals().containsKey(l)) {
            TARDISTeleportLocation tpl = plugin.getTrackerKeeper().getPortals().get(l);
            UUID uuid = p.getUniqueId();
            int id = tpl.getTardisId();
            Location to = tpl.getLocation();
            boolean exit;
            if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && plugin.getServer().getPlayer(uuid).hasPermission("tardis.create_world")) {
                exit = !(to.getWorld().getName().contains("TARDIS"));
            } else if (plugin.getConfig().getBoolean("creation.default_world")) {
                // check default world name
                exit = !(to.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name")));
            } else {
                exit = !(to.getWorld().getName().contains("TARDIS"));
            }
            // adjust player yaw for to
            float yaw = (exit) ? p.getLocation().getYaw() + 180.0f : p.getLocation().getYaw();
            COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(p, false));
            if (!tpl.getDirection().equals(d)) {
                yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw(d, tpl.getDirection());
            }
            to.setYaw(yaw);
            HashMap<String, Object> wherepp = new HashMap<>();
            wherepp.put("uuid", uuid.toString());
            ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, wherepp);
            boolean hasPrefs = rsp.resultSet();
            boolean minecart = (hasPrefs) && rsp.isMinecartOn();
            boolean userQuotes = (hasPrefs) && rsp.isQuotesOn();
            boolean willFarm = (hasPrefs) && rsp.isFarmOn();
            // check for entities near the police box
            List<TARDISPet> pets = null;
            if (plugin.getConfig().getBoolean("allow.mob_farming") && p.hasPermission("tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(uuid) && willFarm) {
                plugin.getTrackerKeeper().getFarming().add(uuid);
                TARDISFarmer tf = new TARDISFarmer(plugin);
                pets = tf.farmAnimals(l, d, id, p, tpl.getLocation().getWorld().getName(), l.getWorld().getName());
            }
            // set travelling status
            if (exit) {
                // unoccupied
                plugin.getGeneralKeeper().getDoorListener().removeTraveller(uuid);
            } else {
                // occupied
                plugin.getGeneralKeeper().getDoorListener().removeTraveller(uuid);
                HashMap<String, Object> set = new HashMap<>();
                set.put("tardis_id", id);
                set.put("uuid", uuid.toString());
                plugin.getQueryFactory().doSyncInsert("travellers", set);
                // if WorldGuard is on the server check for TARDIS region protection and add player as member
                if (plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                    // get owner of TARDIS
                    HashMap<String, Object> whereo = new HashMap<>();
                    whereo.put("tardis_id", id);
                    ResultSetTardis rs = new ResultSetTardis(plugin, whereo, "", false, 2);
                    if (rs.resultSet()) {
                        plugin.getWorldGuardUtils().addMemberToRegion(to.getWorld(), rs.getTardis().getOwner(), p.getName());
                    }
                }
            }
            // tp player
            plugin.getGeneralKeeper().getDoorListener().movePlayer(p, to, exit, l.getWorld(), userQuotes, 0, minecart);
            if (pets != null && pets.size() > 0) {
                plugin.getGeneralKeeper().getDoorListener().movePets(pets, tpl.getLocation(), p, d, true);
            }
            if (userQuotes) {
                TARDISMessage.send(p, "DOOR_REMIND");
            }
            // if WorldGuard is on the server check for TARDIS region protection and remove player as member
            if (exit && plugin.isWorldGuardOnServer() && plugin.getConfig().getBoolean("preferences.use_worldguard")) {
                // get owner of TARDIS
                HashMap<String, Object> whereo = new HashMap<>();
                whereo.put("tardis_id", id);
                ResultSetTardis rs = new ResultSetTardis(plugin, whereo, "", false, 2);
                if (rs.resultSet()) {
                    plugin.getWorldGuardUtils().removeMemberFromRegion(l.getWorld(), rs.getTardis().getOwner(), p.getName());
                }
            }
        }
    }
}
