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
package me.eccentric_nz.TARDIS.commands.travel;

import java.util.Collections;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.api.event.TARDISTravelEvent;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.enumeration.TravelType;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 *
 * @author eccentric_nz
 */
public class TARDISTravelStop {

    private final TARDIS plugin;

    public TARDISTravelStop(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean action(Player player, int id) {
        // remove trackers
        plugin.getTrackerKeeper().getMaterialising().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getInVortex().removeAll(Collections.singleton(id));
        plugin.getTrackerKeeper().getHadsDamage().remove(id);
        plugin.getTrackerKeeper().getMalfunction().remove(id);
        if (plugin.getTrackerKeeper().getDidDematToVortex().contains(id)) {
            plugin.getTrackerKeeper().getDidDematToVortex().removeAll(Collections.singleton(id));
        }
        if (plugin.getTrackerKeeper().getDestinationVortex().containsKey(id)) {
            int taskID = plugin.getTrackerKeeper().getDestinationVortex().get(id);
            plugin.getServer().getScheduler().cancelTask(taskID);
            plugin.getTrackerKeeper().getDestinationVortex().remove(id);
        }
        // get home location
        HashMap<String, Object> wherehl = new HashMap<>();
        wherehl.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, wherehl);
        if (!rsh.resultSet()) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "HOME_NOT_FOUND");
            return true;
        }
        // update current, next and back tables
        HashMap<String, Object> setlocs = new HashMap<>();
        setlocs.put("world", rsh.getWorld().getName());
        setlocs.put("x", rsh.getX());
        setlocs.put("y", rsh.getY());
        setlocs.put("z", rsh.getZ());
        setlocs.put("direction", rsh.getDirection().toString());
        setlocs.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
        Location l = new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ());
        plugin.getQueryFactory().updateLocations(setlocs, id);
        // rebuild the exterior
        BuildData bd = new BuildData(player.getUniqueId().toString());
        bd.setDirection(rsh.getDirection());
        bd.setLocation(l);
        bd.setMalfunction(false);
        bd.setOutside(true);
        bd.setPlayer(player);
        bd.setRebuild(true);
        bd.setSubmarine(rsh.isSubmarine());
        bd.setTardisID(id);
        bd.setThrottle(SpaceTimeThrottle.REBUILD);
        if (!rsh.getPreset().isEmpty()) {
            // set the chameleon preset
            HashMap<String, Object> wherep = new HashMap<>();
            wherep.put("tardis_id", id);
            HashMap<String, Object> setp = new HashMap<>();
            setp.put("chameleon_preset", rsh.getPreset());
            // set chameleon adaption to OFF
            setp.put("adapti_on", 0);
            plugin.getQueryFactory().doSyncUpdate("tardis", setp, wherep);
        }
        plugin.getPresetBuilder().buildPreset(bd);
        plugin.getPM().callEvent(new TARDISTravelEvent(player, null, TravelType.STOP, id));
        return true;
    }
}
