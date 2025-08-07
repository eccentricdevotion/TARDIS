/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.builders.exterior.TARDISEmergencyRelocation;
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.Flag;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.travel.ComehereRequest;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import me.eccentric_nz.TARDIS.utility.protection.TARDISLWCChecker;
import nl.rutgerkok.blocklocker.BlockLockerAPIv2;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
public class TARDISCallRequestCommand {

    private final TARDIS plugin;

    public TARDISCallRequestCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean requestComeHere(Player player, Player requested) {
        UUID uuid = requested.getUniqueId();
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PLAYER_NO_TARDIS");
            return true;
        }
        Tardis tardis = rs.getTardis();
        int id = tardis.getTardisId();
        if (plugin.getConfig().getBoolean("allow.power_down") && !tardis.isPoweredOn()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PLAYER_NOT_POWERED", requested.getName());
            return true;
        }
        int level = tardis.getArtronLevel();
        // get location
        Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_WORLD_TRAVEL");
            return true;
        }
        if (!plugin.getPluginRespect().getRespect(eyeLocation, new Parameters(player, Flag.getDefaultFlags()))) {
            return true;
        }
        if (TARDISPermission.hasPermission(requested, "tardis.exile") && plugin.getConfig().getBoolean("travel.exile")) {
            String areaPerm = plugin.getTardisArea().getExileArea(requested);
            if (plugin.getTardisArea().areaCheckInExile(areaPerm, eyeLocation)) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "EXILE_NO_REQUEST", requested.getName());
                return true;
            }
        }
        if (plugin.getTrackerKeeper().getDispersedTARDII().contains(id)) {
            plugin.getMessenger().send(player.getPlayer(), TardisModule.TARDIS, "NOT_WHILE_DISPERSED_REQUEST", requested.getName());
            return true;
        }
        if (plugin.getTardisArea().isInExistingArea(eyeLocation)) {
            plugin.getMessenger().sendColouredCommand(player, "AREA_NO_COMEHERE", "/tardistravel area [area name]", plugin);
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
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_WORLD");
            return true;
        }
        UUID playerUuid = player.getUniqueId();
        // check the requesting player is NOT in their tardis
        HashMap<String, Object> wherettrav = new HashMap<>();
        wherettrav.put("uuid", playerUuid.toString());
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
        if (rst.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NO_PB_IN_TARDIS");
            return true;
        }
        // check the requested player IS in their tardis
        HashMap<String, Object> wherein = new HashMap<>();
        wherein.put("uuid", uuid.toString());
        ResultSetTravellers rsti = new ResultSetTravellers(plugin, wherein, false);
        if (!rsti.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "PLAYER_NOT_IN_TARDIS", requested.getName());
            return true;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "NOT_WHILE_MAT_REQUEST");
            return true;
        }
        // get current police box location
        ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
        if (!rsc.resultSet()) {
            // emergency TARDIS relocation
            new TARDISEmergencyRelocation(plugin).relocate(id, player);
            return true;
        }
        Current current = rsc.getCurrent();
        COMPASS d = current.direction();
        COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        int count;
        boolean sub = false;
        Block b = eyeLocation.getBlock();
        if (b.getRelative(BlockFace.UP).getType().equals(Material.WATER)) {
            count = (tt.isSafeSubmarine(eyeLocation, player_d)) ? 0 : 1;
            if (count == 0) {
                sub = true;
            }
        } else {
            int[] start_loc = TARDISTimeTravel.getStartLocation(eyeLocation, player_d);
            // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS player_d)
            count = TARDISTimeTravel.safeLocation(start_loc[0], eyeLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], eyeLocation.getWorld(), player_d);
        }
        Block under = eyeLocation.getBlock().getRelative(BlockFace.DOWN);
        if (plugin.getPM().isPluginEnabled("BlockLocker") && (BlockLockerAPIv2.isProtected(eyeLocation.getBlock()) || BlockLockerAPIv2.isProtected(under))) {
            count = 1;
        }
        if (plugin.getPM().isPluginEnabled("LWC") && new TARDISLWCChecker().isBlockProtected(eyeLocation.getBlock(), under, player)) {
            count = 1;
        }
        if (count > 0) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "WOULD_GRIEF_BLOCKS");
            return true;
        }
        // store request data and await response
        ComehereRequest request = new ComehereRequest();
        request.setRequester(playerUuid);
        request.setAccepter(uuid);
        request.setId(id);
        request.setLevel(level);
        request.setCurrent(current.location());
        request.setCurrentDirection(d);
        request.setDestination(eyeLocation);
        request.setDestinationDirection(player_d);
        request.setSubmarine(sub);
        request.setHidden(tardis.isHidden());
        plugin.getTrackerKeeper().getComehereRequests().put(uuid, request);
        // send message with click event
        plugin.getMessenger().send(requested, TardisModule.TARDIS, "REQUEST_COMEHERE", player.getName());
        plugin.getMessenger().sendRequestComehereAccept(requested, "REQUEST_COMEHERE_ACCEPT", "/tardis call accept");
        // message asking player too
        plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_SENT", requested.getName());
        // remove request after 60 seconds
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
            if (plugin.getTrackerKeeper().getComehereRequests().containsKey(playerUuid)) {
                plugin.getTrackerKeeper().getComehereRequests().remove(playerUuid);
                plugin.getMessenger().send(player, TardisModule.TARDIS, "REQUEST_NO_RESPONSE", requested.getName());
            }
        }, 1200L);
        return true;
    }
}
