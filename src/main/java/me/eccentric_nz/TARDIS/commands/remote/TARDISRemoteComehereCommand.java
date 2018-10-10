/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.remote;

import me.crafter.mc.lockettepro.LocketteProAPI;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.Parameters;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.FLAG;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISStaticUtils;
import nl.rutgerkok.blocklocker.BlockLockerAPI;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.yi.acru.bukkit.Lockette.Lockette;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class TARDISRemoteComehereCommand {

    private final TARDIS plugin;

    TARDISRemoteComehereCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    boolean doRemoteComeHere(Player player, UUID uuid) {
        Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
            TARDISMessage.send(player, "NO_WORLD_TRAVEL");
            return true;
        }
        if (!plugin.getPluginRespect().getRespect(eyeLocation, new Parameters(player, FLAG.getDefaultFlags()))) {
            return true;
        }
        if (!plugin.getTardisArea().areaCheckInExisting(eyeLocation)) {
            TARDISMessage.send(player, "AREA_NO_COMEHERE", ChatColor.AQUA + "/tardisremote [player] travel area [area name]");
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
            TARDISMessage.send(player, "NO_PB_IN_WORLD");
            return true;
        }
        // check the remote player is a Time Lord
        HashMap<String, Object> where = new HashMap<>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, "PLAYER_NO_TARDIS");
            return true;
        }
        Tardis tardis = rs.getTardis();
        int id = tardis.getTardis_id();
        // check they are not in the tardis
        HashMap<String, Object> wherettrav = new HashMap<>();
        wherettrav.put("uuid", player.getUniqueId().toString());
        wherettrav.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
        if (rst.resultSet()) {
            TARDISMessage.send(player, "NO_PB_IN_TARDIS");
            return true;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id) || plugin.getTrackerKeeper().getMaterialising().contains(id) || plugin.getTrackerKeeper().getDematerialising().contains(id)) {
            TARDISMessage.send(player, "NOT_WHILE_MAT");
            return true;
        }
        boolean hidden = tardis.isHidden();
        // get current police box location
        HashMap<String, Object> wherecl = new HashMap<>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            hidden = true;
        }
        COMPASS d = rsc.getDirection();
        COMPASS player_d = COMPASS.valueOf(TARDISStaticUtils.getPlayersDirection(player, false));
        Biome biome = rsc.getBiome();
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
        if (plugin.getPM().isPluginEnabled("Lockette")) {
            if (Lockette.isProtected(eyeLocation.getBlock()) || Lockette.isProtected(under)) {
                count = 1;
            }
        }
        if (plugin.getPM().isPluginEnabled("LockettePro")) {
            if (LocketteProAPI.isProtected(eyeLocation.getBlock()) || LocketteProAPI.isProtected(under) || plugin.getUtils().checkSurrounding(under)) {
                count = 1;
            }
        }
        if (plugin.getPM().isPluginEnabled("BlockLocker")) {
            if (BlockLockerAPI.isProtected(eyeLocation.getBlock()) || BlockLockerAPI.isProtected(under)) {
                count = 1;
            }
        }
        if (count > 0) {
            TARDISMessage.send(player, "WOULD_GRIEF_BLOCKS");
            return true;
        }
        QueryFactory qf = new QueryFactory(plugin);
        Location oldSave = null;
        HashMap<String, Object> bid = new HashMap<>();
        bid.put("tardis_id", id);
        HashMap<String, Object> bset = new HashMap<>();
        if (rsc.getWorld() != null) {
            oldSave = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
            // set fast return location
            bset.put("world", rsc.getWorld().getName());
            bset.put("x", rsc.getX());
            bset.put("y", rsc.getY());
            bset.put("z", rsc.getZ());
            bset.put("direction", d.toString());
            bset.put("submarine", rsc.isSubmarine());
        } else {
            hidden = true;
            // set fast return location
            bset.put("world", eyeLocation.getWorld().getName());
            bset.put("x", eyeLocation.getX());
            bset.put("y", eyeLocation.getY());
            bset.put("z", eyeLocation.getZ());
            bset.put("submarine", (sub) ? 1 : 0);
        }
        qf.doUpdate("back", bset, bid);
        HashMap<String, Object> tid = new HashMap<>();
        tid.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<>();
        set.put("world", eyeLocation.getWorld().getName());
        set.put("x", eyeLocation.getBlockX());
        set.put("y", eyeLocation.getBlockY());
        set.put("z", eyeLocation.getBlockZ());
        set.put("direction", player_d.toString());
        set.put("submarine", (sub) ? 1 : 0);
        if (hidden) {
            HashMap<String, Object> sett = new HashMap<>();
            sett.put("hidden", 0);
            HashMap<String, Object> ttid = new HashMap<>();
            ttid.put("tardis_id", id);
            qf.doUpdate("tardis", sett, ttid);
            // restore biome
            plugin.getUtils().restoreBiome(oldSave, biome);
        }
        qf.doUpdate("current", set, tid);
        TARDISMessage.send(player, "TARDIS_COMING");
        long delay = 1L;
        plugin.getTrackerKeeper().getInVortex().add(id);
        boolean hid = hidden;
        if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            DestroyData dd = new DestroyData(plugin, player.getUniqueId().toString());
            dd.setDirection(d);
            dd.setLocation(oldSave);
            dd.setPlayer(player);
            dd.setHide(false);
            dd.setOutside(true);
            dd.setSubmarine(rsc.isSubmarine());
            dd.setTardisID(id);
            dd.setBiome(biome);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                if (!hid) {
                    plugin.getTrackerKeeper().getDematerialising().add(id);
                    plugin.getPresetDestroyer().destroyPreset(dd);
                } else {
                    plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                }
            }, delay);
        }
        BuildData bd = new BuildData(plugin, player.getUniqueId().toString());
        bd.setDirection(player_d);
        bd.setLocation(eyeLocation);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(player);
        bd.setRebuild(false);
        bd.setSubmarine(sub);
        bd.setTardisID(id);
        plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> plugin.getPresetBuilder().buildPreset(bd), delay * 2);
        plugin.getTrackerKeeper().getHasDestination().remove(id);
        plugin.getTrackerKeeper().getRescue().remove(id);
        return true;
    }
}
