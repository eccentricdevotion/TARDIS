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
package me.eccentric_nz.TARDIS.commands.remote;

import java.util.HashMap;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.ResultSetTravellers;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.MESSAGE;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Biome;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoteComehereCommand {

    private final TARDIS plugin;

    public TARDISRemoteComehereCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    public boolean doRemoteComeHere(Player player, UUID uuid) {
        Location eyeLocation = player.getTargetBlock(plugin.getGeneralKeeper().getTransparent(), 50).getLocation();
        if (!plugin.getConfig().getBoolean("travel.include_default_world") && plugin.getConfig().getBoolean("creation.default_world") && eyeLocation.getWorld().getName().equals(plugin.getConfig().getString("creation.default_world_name"))) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_WORLD_TRAVEL.getText());
            return true;
        }
        if (!plugin.getPluginRespect().getRespect(player, eyeLocation, true)) {
            return true;
        }
        if (!plugin.getTardisArea().areaCheckInExisting(eyeLocation)) {
            TARDISMessage.send(player, plugin.getPluginName() + "You cannot use /tardis comehere to bring the Police Box to a TARDIS area! Please use " + ChatColor.AQUA + "/tardisremote [player] travel area [area name]");
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
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PB_IN_WORLD.getText());
            return true;
        }
        // check the remote player is a Time Lord
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            TARDISMessage.send(player, plugin.getPluginName() + "The specified player does not have a TARDIS!");
            return true;
        }
        final int id = rs.getTardis_id();
        // check they are not in the tardis
        HashMap<String, Object> wherettrav = new HashMap<String, Object>();
        wherettrav.put("uuid", player.getUniqueId().toString());
        wherettrav.put("tardis_id", id);
        ResultSetTravellers rst = new ResultSetTravellers(plugin, wherettrav, false);
        if (rst.resultSet()) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NO_PB_IN_TARDIS.getText());
            return true;
        }
        if (plugin.getTrackerKeeper().getInVortex().contains(id)) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.NOT_WHILE_MAT.getText());
            return true;
        }
        boolean chamtmp = false;
        if (plugin.getConfig().getBoolean("travel.chameleon")) {
            chamtmp = rs.isChamele_on();
        }
        boolean hidden = rs.isHidden();
        // get current police box location
        HashMap<String, Object> wherecl = new HashMap<String, Object>();
        wherecl.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherecl);
        if (!rsc.resultSet()) {
            hidden = true;
        }
        COMPASS d = rsc.getDirection();
        COMPASS player_d = COMPASS.valueOf(plugin.getUtils().getPlayersDirection(player, false));
        Biome biome = rsc.getBiome();
        TARDISTimeTravel tt = new TARDISTimeTravel(plugin);
        int count;
        boolean sub = false;
        Block b = eyeLocation.getBlock();
        if (b.getRelative(BlockFace.UP).getTypeId() == 8 || b.getRelative(BlockFace.UP).getTypeId() == 9) {
            count = (tt.isSafeSubmarine(eyeLocation, player_d)) ? 0 : 1;
            if (count == 0) {
                sub = true;
            }
        } else {
            int[] start_loc = tt.getStartLocation(eyeLocation, player_d);
            // safeLocation(int startx, int starty, int startz, int resetx, int resetz, World w, COMPASS player_d)
            count = tt.safeLocation(start_loc[0], eyeLocation.getBlockY(), start_loc[2], start_loc[1], start_loc[3], eyeLocation.getWorld(), player_d);
        }
        if (count > 0) {
            TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.WOULD_GRIEF_BLOCKS.getText());
            return true;
        }
        boolean cham = chamtmp;
        final QueryFactory qf = new QueryFactory(plugin);
        Location oldSave = null;
        HashMap<String, Object> bid = new HashMap<String, Object>();
        bid.put("tardis_id", id);
        HashMap<String, Object> bset = new HashMap<String, Object>();
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
            // set fast return location
            bset.put("world", eyeLocation.getWorld().getName());
            bset.put("x", eyeLocation.getX());
            bset.put("y", eyeLocation.getY());
            bset.put("z", eyeLocation.getZ());
            bset.put("submarine", (sub) ? 1 : 0);
        }
        qf.doUpdate("back", bset, bid);
        HashMap<String, Object> tid = new HashMap<String, Object>();
        tid.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<String, Object>();
        set.put("world", eyeLocation.getWorld().getName());
        set.put("x", eyeLocation.getBlockX());
        set.put("y", eyeLocation.getBlockY());
        set.put("z", eyeLocation.getBlockZ());
        set.put("direction", player_d.toString());
        set.put("submarine", (sub) ? 1 : 0);
        if (hidden) {
            HashMap<String, Object> sett = new HashMap<String, Object>();
            sett.put("hidden", 0);
            HashMap<String, Object> ttid = new HashMap<String, Object>();
            ttid.put("tardis_id", id);
            qf.doUpdate("tardis", sett, ttid);
        }
        qf.doUpdate("current", set, tid);
        TARDISMessage.send(player, plugin.getPluginName() + MESSAGE.TARDIS_COMING.getText());
        boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
        long delay = (mat) ? 1L : 180L;
        plugin.getTrackerKeeper().getInVortex().add(id);
        final boolean hid = hidden;
        final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
        pdd.setChameleon(cham);
        pdd.setDirection(d);
        pdd.setLocation(oldSave);
        pdd.setDematerialise(mat);
        pdd.setPlayer(player);
        pdd.setHide(false);
        pdd.setOutside(true);
        pdd.setSubmarine(rsc.isSubmarine());
        pdd.setTardisID(id);
        pdd.setBiome(biome);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                if (!hid) {
                    plugin.getTrackerKeeper().getDematerialising().add(id);
                    plugin.getPresetDestroyer().destroyPreset(pdd);
                } else {
                    plugin.getPresetDestroyer().removeBlockProtection(id, qf);
                }
            }
        }, delay);
        final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
        pbd.setChameleon(cham);
        pbd.setDirection(player_d);
        pbd.setLocation(eyeLocation);
        pbd.setMalfunction(false);
        pbd.setOutside(true);
        pbd.setPlayer(player);
        pbd.setRebuild(false);
        pbd.setSubmarine(sub);
        pbd.setTardisID(id);
        Bukkit.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
            @Override
            public void run() {
                plugin.getPresetBuilder().buildPreset(pbd);
            }
        }, delay * 2);
        plugin.getTrackerKeeper().getHasDestination().remove(id);
        if (plugin.getTrackerKeeper().getRescue().containsKey(id)) {
            plugin.getTrackerKeeper().getRescue().remove(id);
        }
        return true;
    }
}
