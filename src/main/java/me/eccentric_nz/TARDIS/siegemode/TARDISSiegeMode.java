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
package me.eccentric_nz.TARDIS.siegemode;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.TARDISMaterialisationData;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetCurrentLocation;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

/**
 * Siege mode is a feature of the TARDIS that can be activated using a lever
 * under the console to prevent entry or exit. Additionally, it makes the TARDIS
 * impervious to all external damage. Siege mode requires power to activate or
 * deactivate.
 *
 * @author eccentric_nz
 */
public class TARDISSiegeMode {

    private final TARDIS plugin;

    public TARDISSiegeMode(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void toggleViaSwitch(int id, Player p) {
        // get the current siege status
        HashMap<String, Object> where = new HashMap<String, Object>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false);
        if (!rs.resultSet()) {
            return;
        }
        boolean cham = (plugin.getConfig().getBoolean("travel.chameleon")) ? rs.isChamele_on() : false;
        // get current location
        HashMap<String, Object> wherec = new HashMap<String, Object>();
        wherec.put("tardis_id", id);
        ResultSetCurrentLocation rsc = new ResultSetCurrentLocation(plugin, wherec);
        if (!rsc.resultSet()) {
            return;
        }
        Location current = new Location(rsc.getWorld(), rsc.getX(), rsc.getY(), rsc.getZ());
        Block siege = current.getBlock();
        HashMap<String, Object> wheres = new HashMap<String, Object>();
        wheres.put("tardis_id", id);
        HashMap<String, Object> set = new HashMap<String, Object>();
        if (rs.isSiege_on()) {
            // must have at least 10% power
            int min = (plugin.getArtronConfig().getInt("full_charge") / 100) * plugin.getArtronConfig().getInt("siege_transfer");
            if (min > rs.getArtron_level()) {
                TARDISMessage.send(p, "SIEGE_POWER");
                return;
            }
            // remove siege block
            siege.setType(Material.AIR);
            // rebuild preset
            final TARDISMaterialisationData pbd = new TARDISMaterialisationData();
            pbd.setChameleon(cham);
            pbd.setDirection(rsc.getDirection());
            pbd.setLocation(current);
            pbd.setMalfunction(false);
            pbd.setOutside(false);
            pbd.setPlayer(p);
            pbd.setRebuild(true);
            pbd.setSubmarine(rsc.isSubmarine());
            pbd.setTardisID(id);
            pbd.setBiome(rsc.getBiome());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
                @Override
                public void run() {
                    plugin.getPresetBuilder().buildPreset(pbd);
                }
            }, 10L);
            set.put("siege_on", 0);
            if (plugin.getTrackerKeeper().getInSiegeMode().contains(id)) {
                plugin.getTrackerKeeper().getInSiegeMode().remove(Integer.valueOf(id));
            }
            if (plugin.getTrackerKeeper().getSiegeBlocks().containsKey(current.toString())) {
                plugin.getTrackerKeeper().getSiegeBlocks().remove(current.toString());
            }
            TARDISMessage.send(p, "SIEGE_OFF");
        } else {
            // destroy tardis
            final TARDISMaterialisationData pdd = new TARDISMaterialisationData();
            pdd.setChameleon(false);
            pdd.setDirection(rsc.getDirection());
            pdd.setLocation(current);
            pdd.setDematerialise(false);
            pdd.setPlayer(p.getPlayer());
            pdd.setHide(false);
            pdd.setOutside(false);
            pdd.setSubmarine(rsc.isSubmarine());
            pdd.setTardisID(id);
            pdd.setBiome(rsc.getBiome());
            plugin.getPresetDestroyer().destroyPreset(pdd);
            // place siege block
            siege.setType(Material.HUGE_MUSHROOM_1);
            siege.setData((byte) 14, true);
            // track this siege block
            plugin.getTrackerKeeper().getInSiegeMode().add(id);
            plugin.getTrackerKeeper().getSiegeBlocks().put(current.toString(), id);
            set.put("siege_on", 1);
            TARDISMessage.send(p, "SIEGE_ON");
        }
        // update the database
        new QueryFactory(plugin).doUpdate("tardis", set, wheres);
    }
}
