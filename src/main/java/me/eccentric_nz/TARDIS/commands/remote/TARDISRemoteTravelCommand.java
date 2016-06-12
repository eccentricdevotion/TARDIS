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
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetNextLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.destroyers.DestroyData;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import me.eccentric_nz.TARDIS.utility.TARDISSounds;
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
        ResultSetTardis rs = new ResultSetTardis(plugin, wherei, "", false, 2);
        if (rs.resultSet()) {
            Tardis tardis = rs.getTardis();
            boolean cham = tardis.isChamele_on();
            boolean hidden = tardis.isHidden();
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
                TARDISMessage.send(sender, "DEST_NO_LOAD");
                return true;
            }
            boolean is_next_sub = rsn.isSubmarine();
            Location exit = new Location(rsn.getWorld(), rsn.getX(), rsn.getY(), rsn.getZ());
            COMPASS sd = rsn.getDirection();
            // Removes Blue Box and loads chunk if it unloaded somehow
            if (!exit.getWorld().isChunkLoaded(exit.getChunk())) {
                exit.getWorld().loadChunk(exit.getChunk());
            }
//            boolean mat = plugin.getConfig().getBoolean("police_box.materialise");
            HashMap<String, Object> set = new HashMap<String, Object>();
            if (!plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
                plugin.getTrackerKeeper().getInVortex().add(id);
                final DestroyData dd = new DestroyData(plugin, player.getUniqueId().toString());
                dd.setChameleon(cham);
                dd.setDirection(cd);
                dd.setLocation(l);
                dd.setPlayer(player);
                dd.setHide(false);
                dd.setOutside(false);
                dd.setSubmarine(sub);
                dd.setTardisID(id);
                dd.setBiome(biome);
                if (!hidden && !plugin.getTrackerKeeper().getReset().contains(resetw)) {
                    plugin.getTrackerKeeper().getDematerialising().add(id);
                    plugin.getPresetDestroyer().destroyPreset(dd);
                } else {
                    // set hidden false!
                    set.put("hidden", 0);
                    plugin.getPresetDestroyer().removeBlockProtection(id, new QueryFactory(plugin));
                    // restore biome
                    plugin.getUtils().restoreBiome(l, biome);
                }
            }
//            long delay = (mat) ? 500L : 1L;
            long delay = (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) ? 1L : 500L;
            final BuildData bd = new BuildData(plugin, player.getUniqueId().toString());
            bd.setChameleon(cham);
            bd.setDirection(sd);
            bd.setLocation(exit);
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(player);
            bd.setRebuild(false);
            bd.setSubmarine(is_next_sub);
            bd.setTardisID(id);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(bd);
                    TARDISSounds.playTARDISSound(bd.getLocation(), "tardis_land");
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
