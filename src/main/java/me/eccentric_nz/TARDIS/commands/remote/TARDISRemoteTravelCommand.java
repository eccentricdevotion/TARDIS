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
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Biome;
import org.bukkit.command.BlockCommandSender;
import org.bukkit.command.CommandSender;

/**
 *
 * @author eccentric_nz
 */
public class TARDISRemoteTravelCommand {

    private final TARDIS plugin;

    public TARDISRemoteTravelCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean doTravel(int id, OfflinePlayer player, CommandSender sender) {
        HashMap<String, Object> wherei = new HashMap<String, Object>();
        wherei.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false);
        if (rs.resultSet()) {
            boolean cham = rs.isChamele_on();
            boolean hidden = rs.isHidden();
            HashMap<String, Object> wherecl = new HashMap<String, Object>();
            wherecl.put("tardis_id", id);
            ResultSetCurrentLocation rscl = new ResultSetCurrentLocation(plugin, wherecl);
            String resetw = "";
            Location l = null;
            if (!rscl.resultSet()) {
                hidden = true;
            } else {
                resetw = rscl.getWorld().getName();
                l = new Location(rscl.getWorld(), rscl.getX(), rscl.getY(), rscl.getZ());
            }
            COMPASS cd = rscl.getDirection();
            Biome biome = rscl.getBiome();
            boolean sub = rscl.isSubmarine();
            HashMap<String, Object> wherenl = new HashMap<String, Object>();
            wherenl.put("tardis_id", id);
            ResultSetNextLocation rsn = new ResultSetNextLocation(plugin, wherenl);
            if (!rsn.resultSet() && !(sender instanceof BlockCommandSender)) {
                sender.sendMessage(plugin.getPluginName() + "Could not load destination!");
                return true;
            }
            boolean is_next_sub = rsn.isSubmarine();
            Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
            COMPASS sd = rsn.getDirection();
            // Removes Blue Box and loads chunk if it unloaded somehow
            if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                exit.getWorld().loadChunk(exit.getChunk());
            }
            boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
            plugin.getTrackerKeeper().getInVortex().add(id);
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setChameleon(cham);
            pdd.setDirection(cd);
            pdd.setLocation(l);
            pdd.setDematerialise(mat);
            pdd.setPlayer(player);
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(sub);
            pdd.setTardisID(id);
            pdd.setBiome(biome);
            HashMap<String, Object> set = new HashMap<String, Object>();
            if (!hidden && !plugin.getTrackerKeeper().getReset().contains(resetw)) {
                plugin.getTrackerKeeper().getDematerialising().add(id);
                plugin.getPresetDestroyer().destroyPreset(pdd);
            } else {
                // set hidden false!
                set.put("hidden", 0);
                plugin.getPresetDestroyer().removeBlockProtection(id, new QueryFactory(plugin));
            }
            long delay = (mat) ? 500L : 1L;
            final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
            pbd.setChameleon(cham);
            pbd.setDirection(sd);
            pbd.setLocation(exit);
            pbd.setMalfunction(false);
            pbd.setOutside(false);
            pbd.setPlayer(player);
            pbd.setRebuild(false);
            pbd.setSubmarine(is_next_sub);
            pbd.setTardisID(id);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(pbd);
                    plugin.getUtils().playTARDISSoundNearby(pbd.getLocation(), "tardis_land");
                }
            }, delay);
            if (plugin.getTrackerKeeper().getDamage().containsKey(id)) {
                plugin.getTrackerKeeper().getDamage().remove(id);
            }
            // current
            HashMap<String, Object> setcurrent = new HashMap<String, Object>();
            setcurrent.put("world", exit.getWorld().getName());
            setcurrent.put("x", exit.getBlockX());
            setcurrent.put("y", exit.getBlockY());
            setcurrent.put("z", exit.getBlockZ());
            setcurrent.put("direction", sd.toString());
            setcurrent.put("submarine", (is_next_sub) ? 1 : 0);
            HashMap<String, Object> wherecurrent = new HashMap<String, Object>();
            wherecurrent.put("tardis_id", id);
            // get current location for back
            HashMap<String, Object> wherecu = new HashMap<String, Object>();
            wherecu.put("tardis_id", id);
            ResultSetCurrentLocation rscu = new ResultSetCurrentLocation(plugin, wherecu);
            HashMap<String, Object> setback = new HashMap<String, Object>();
            if (!rscu.resultSet()) {
                // back
                setback.put("world", exit.getWorld().getName());
                setback.put("x", exit.getX());
                setback.put("y", exit.getY());
                setback.put("z", exit.getZ());
                setback.put("direction", exit.getDirection().toString());
                setback.put("submarine", (is_next_sub) ? 1 : 0);
            } else {
                // back
                setback.put("world", rscu.getWorld().getName());
                setback.put("x", rscu.getX());
                setback.put("y", rscu.getY());
                setback.put("z", rscu.getZ());
                setback.put("direction", rscu.getDirection().toString());
                setback.put("submarine", (rscu.isSubmarine()) ? 1 : 0);
            }
            HashMap<String, Object> whereback = new HashMap<String, Object>();
            whereback.put("tardis_id", id);
            // update Police Box door direction
            HashMap<String, Object> setdoor = new HashMap<String, Object>();
            setdoor.put("door_direction", sd.toString());
            HashMap<String, Object> wheredoor = new HashMap<String, Object>();
            wheredoor.put("tardis_id", id);
            wheredoor.put("door_type", 0);
            QueryFactory qf = new QueryFactory(plugin);
            HashMap<String, Object> whereh = new HashMap<String, Object>();
            whereh.put("tardis_id", id);
            if (set.size() > 0) {
                qf.doUpdate("tardis", set, whereh);
            }
            qf.doUpdate("current", setcurrent, wherecurrent);
            qf.doUpdate("back", setback, whereback);
            qf.doUpdate("doors", setdoor, wheredoor);
            return true;
        }
        return false;
    }
}
