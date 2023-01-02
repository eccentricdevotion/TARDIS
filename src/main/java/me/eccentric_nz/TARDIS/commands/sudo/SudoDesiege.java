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
package me.eccentric_nz.TARDIS.commands.sudo;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.BuildData;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetHomeLocation;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.UUID;

/**
 * @author eccentric_nz
 */
class SudoDesiege {

    private final TARDIS plugin;

    SudoDesiege(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean restore(CommandSender sender, UUID uuid, int id) {
        // turn off siege mode
        HashMap<String, Object> wheres = new HashMap<>();
        wheres.put("tardis_id", id);
        HashMap<String, Object> sets = new HashMap<>();
        sets.put("siege_on", 0);
        plugin.getQueryFactory().doUpdate("tardis", sets, wheres);
        // clear trackers
        plugin.getTrackerKeeper().getInSiegeMode().remove(id);
        plugin.getTrackerKeeper().getIsSiegeCube().remove(id);
        plugin.getTrackerKeeper().getSiegeCarrying().remove(uuid);
        // get home location
        HashMap<String, Object> whereh = new HashMap<>();
        whereh.put("tardis_id", id);
        ResultSetHomeLocation rsh = new ResultSetHomeLocation(plugin, whereh);
        if (rsh.resultSet()) {
            // set current location
            HashMap<String, Object> wherec = new HashMap<>();
            wherec.put("tardis_id", id);
            HashMap<String, Object> setc = new HashMap<>();
            setc.put("world", rsh.getWorld().getName());
            setc.put("x", rsh.getX());
            setc.put("y", rsh.getY());
            setc.put("z", rsh.getZ());
            setc.put("direction", rsh.getDirection().toString());
            setc.put("submarine", (rsh.isSubmarine()) ? 1 : 0);
            plugin.getQueryFactory().doUpdate("current", setc, wherec);
            // rebuild the TARDIS
            BuildData bd = new BuildData(uuid.toString());
            bd.setDirection(rsh.getDirection());
            bd.setLocation(new Location(rsh.getWorld(), rsh.getX(), rsh.getY(), rsh.getZ()));
            bd.setMalfunction(false);
            bd.setOutside(false);
            bd.setPlayer(plugin.getServer().getPlayer(uuid));
            bd.setRebuild(true);
            bd.setSubmarine(rsh.isSubmarine());
            bd.setTardisID(id);
            bd.setThrottle(SpaceTimeThrottle.REBUILD);
            plugin.getPresetBuilder().buildPreset(bd);
            TARDISMessage.send(sender, "SIEGE_REBUILT");
        }
        return true;
    }
}
