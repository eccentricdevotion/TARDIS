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
package me.eccentric_nz.TARDIS.move;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.control.TARDISPowerButton;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCompanions;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetPlayerPrefs;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetVoid;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFarmer;
import me.eccentric_nz.TARDIS.mobfarming.TARDISFollowerSpawner;
import me.eccentric_nz.TARDIS.mobfarming.TARDISPetsAndFollowers;
import me.eccentric_nz.TARDIS.portal.Capture;
import me.eccentric_nz.TARDIS.portal.Cast;
import me.eccentric_nz.TARDIS.portal.CastData;
import me.eccentric_nz.TARDIS.portal.MatrixUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.TARDISVoidUpdate;
import org.bukkit.Location;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * Listen for a player moving, if they move over a TARDIS portal, teleport them to the TARDIS interior or exterior.
 */
public class TARDISMoveListener implements Listener {

    private final TARDIS plugin;

    public TARDISMoveListener(TARDIS plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onPlayerMoveToFromTARDIS(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!plugin.getTrackerKeeper().getMovers().contains(uuid)) {
            return;
        }
        Location l = new Location(event.getTo().getWorld(), event.getTo().getBlockX(), event.getTo().getBlockY(), event.getTo().getBlockZ(), 0.0f, 0.0f);
        Location loc = player.getLocation(); // Grab Location

        /*
         * Copyright (C) 2023, The Multiverse Team All rights reserved. Check
         * the Player has actually moved a block to prevent unneeded
         * calculations... This is to prevent huge performance drops on high
         * player count servers.
         */
        TARDISMoveSession tms = plugin.getTrackerKeeper().getTARDISMoveSession(player);
        tms.setStaleLocation(loc);

        // If the location is stale, ie: the player isn't actually moving xyz coords, they're looking around
        if (tms.isStaleLocation()) {
            return;
        }
        // are they casting?
        if (plugin.getConfig().getBoolean("police_box.view_interior") && plugin.getTrackerKeeper().getCasters().containsKey(uuid)) {
            CastData data = plugin.getTrackerKeeper().getCasters().get(uuid);
            Location exterior = data.exterior();
            if (exterior.getWorld() == loc.getWorld()) {
                Location interior = data.interior();
                // get distance from exterior
                // only in the direction of the door!
                double distance = l.distanceSquared(exterior);
                double vx, vz;
                switch (data.direction()) {
                    case EAST -> {
                        vx = exterior.getX() - 3;
                        vz = exterior.getZ();
                    }
                    case WEST -> {
                        vx = exterior.getX() + 3;
                        vz = exterior.getZ();
                    }
                    case NORTH -> {
                        vx = exterior.getX();
                        vz = exterior.getZ() + 3;
                    }
                    default -> { // SOUTH
                        vx = exterior.getX();
                        vz = exterior.getZ() - 3;
                    }
                }
                double angle = MatrixUtils.getPlayerAngle(l.getX(), l.getZ(), vx, vz, exterior.getX(), exterior.getZ());
                if (angle >= 360) {
                    angle -= 360;
                }
                Cast cast = new Cast(plugin, exterior);
                if (distance <= 9 && angle < 26 && angle > -26) {
                    Capture capture = new Capture();
                    BlockData[][][] dataArr = capture.captureInterior(interior, (int) distance, data.rotor(), data.consoleSize());
                    cast.castInterior(uuid, dataArr);
                    if (capture.getRotorData().frame() != null) {
                        // get vector of rotor
                        cast.castRotor(capture.getRotorData().frame(), player, capture.getRotorData().offset(), data.direction());
                    }
                } else if (plugin.getTrackerKeeper().getCastRestore().containsKey(uuid)) {
                    cast.restoreExterior(uuid);
                    plugin.getTrackerKeeper().getCastRestore().remove(uuid);
                    // remove fake item frame if necessary
                    if (plugin.getTrackerKeeper().getRotorRestore().containsKey(uuid)) {
                        int rotorId = plugin.getTrackerKeeper().getRotorRestore().get(uuid);
                        plugin.getTardisHelper().removeFakeItemFrame(rotorId, player);
                        plugin.getTrackerKeeper().getRotorRestore().remove(uuid);
                    }
                }
            }
        }
        // check the block they're on
        if (plugin.getTrackerKeeper().getPortals().containsKey(l)) {
            TARDISTeleportLocation tpl = plugin.getTrackerKeeper().getPortals().get(l);
            int id = tpl.getTardisId();
            // are they a companion of this TARDIS?
            List<UUID> companions = new ResultSetCompanions(plugin, id).getCompanions();
            if (tpl.isAbandoned() || companions.contains(uuid)) {
                Location to = tpl.getLocation();
                boolean exit;
                if (plugin.getConfig().getBoolean("creation.create_worlds_with_perms") && TARDISPermission.hasPermission(player, "tardis.create_world")) {
                    exit = !(to.getWorld().getName().contains("TARDIS"));
                } else if (plugin.getConfig().getBoolean("creation.default_world")) {
                    // check default world name
                    exit = !(to.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name")));
                } else {
                    exit = !(to.getWorld().getName().contains("TARDIS"));
                }
                // adjust player yaw for to
                float yaw = (exit) ? loc.getYaw() + 180.0f : loc.getYaw();
                switch (tpl.getDirection()) {
                    case NORTH_WEST -> yaw += 90;
                    case SOUTH_WEST -> yaw -= 90;
                    default -> { }
                }
                COMPASS d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
                if (!tpl.getDirection().equals(d)) {
                    yaw += plugin.getGeneralKeeper().getDoorListener().adjustYaw(d, tpl.getDirection());
                }
                to.setYaw(yaw);
                ResultSetPlayerPrefs rsp = new ResultSetPlayerPrefs(plugin, uuid.toString());
                boolean hasPrefs = rsp.resultSet();
                boolean minecart = (hasPrefs) && rsp.isMinecartOn();
                boolean userQuotes = (hasPrefs) && rsp.isQuotesOn();
                boolean willFarm = (hasPrefs) && rsp.isFarmOn();
                boolean canPowerUp = (hasPrefs) && (rsp.isAutoPowerUp() && !tpl.isAbandoned());
                // check for entities near the police box
                TARDISPetsAndFollowers petsAndFollowers = null;
                if (plugin.getConfig().getBoolean("allow.mob_farming") && TARDISPermission.hasPermission(player, "tardis.farm") && !plugin.getTrackerKeeper().getFarming().contains(uuid) && willFarm) {
                    plugin.getTrackerKeeper().getFarming().add(uuid);
                    TARDISFarmer tf = new TARDISFarmer(plugin);
                    petsAndFollowers = tf.farmAnimals(l, d, id, player, tpl.getLocation().getWorld().getName(), l.getWorld().getName());
                }
                // set travelling status
                plugin.getGeneralKeeper().getDoorListener().removeTraveller(uuid);
                if (!exit) {
                    // occupied
                    HashMap<String, Object> set = new HashMap<>();
                    set.put("tardis_id", id);
                    set.put("uuid", uuid.toString());
                    plugin.getQueryFactory().doSyncInsert("travellers", set);
                    // check to see whether the TARDIS has been updated to VOID biome
                    if (!new ResultSetVoid(plugin, id).hasUpdatedToVOID()) {
                        new TARDISVoidUpdate(plugin, id).updateBiome();
                        // add tardis id to void table
                        plugin.getQueryFactory().addToVoid(id);
                    }
                }
                // tp player
                plugin.getGeneralKeeper().getDoorListener().movePlayer(player, to, exit, l.getWorld(), userQuotes, 0, minecart, false);
                if (petsAndFollowers != null) {
                    if (!petsAndFollowers.getPets().isEmpty()) {
                        plugin.getGeneralKeeper().getDoorListener().movePets(petsAndFollowers.getPets(), tpl.getLocation(), player, d, true);
                    }
                    if (!petsAndFollowers.getFollowers().isEmpty()) {
                        new TARDISFollowerSpawner(plugin).spawn(petsAndFollowers.getFollowers(), tpl.getLocation(), player, d, true);
                    }
                }
                if (canPowerUp && !exit) {
                    // power up the TARDIS
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        HashMap<String, Object> where = new HashMap<>();
                        where.put("tardis_id", id);
                        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
                        if (rs.resultSet()) {
                            Tardis tardis = rs.getTardis();
                            if (!tardis.isPoweredOn()) {
                                new TARDISPowerButton(plugin, id, player, tardis.getPreset(), false, tardis.isHidden(), tardis.isLightsOn(), loc, tardis.getArtronLevel(), tardis.getSchematic().getLights()).clickButton();
                            }
                        }
                    }, 20L);
                }
                if (userQuotes) {
                    plugin.getMessenger().sendStatus(player, "DOOR_REMIND");
                }
            }
        }
    }
}
