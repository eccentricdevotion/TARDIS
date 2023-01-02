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
package me.eccentric_nz.TARDIS.builders;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.TARDIS.messaging.TARDISMessage;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISEmergencyRelocation {

    private final TARDIS plugin;

    public TARDISEmergencyRelocation(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void relocate(int id, Player p) {
        TARDISMessage.send(p, "EMERGENCY");
        // get the TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            // get the servers main world
            World w = plugin.getServer().getWorlds().get(0);
            Location emergency = new TARDISTimeTravel(plugin).randomDestination(p, 4, 4, 4, COMPASS.EAST, "THIS", w, false, w.getSpawnLocation());
            if (emergency != null) {
                BuildData bd = new BuildData(p.getUniqueId().toString());
                bd.setPlayer(p);
                bd.setLocation(emergency);
                bd.setTardisID(id);
                bd.setDirection(COMPASS.EAST);
                bd.setMalfunction(false);
                bd.setSubmarine(false);
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                Tardis tardis = rs.getTardis();
                if (tardis.getPreset().usesItemFrame()) {
                    new TARDISInstantPoliceBox(plugin, bd, tardis.getPreset()).buildPreset();
                } else {
                    new TARDISInstantPreset(plugin, bd, tardis.getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
                }
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("world", emergency.getWorld().getName());
                setc.put("x", emergency.getBlockX());
                setc.put("y", emergency.getBlockY());
                setc.put("z", emergency.getBlockZ());
                setc.put("direction", "EAST");
                setc.put("submarine", 0);
                plugin.getQueryFactory().doUpdate("current", setc, wherec);
                HashMap<String, Object> whereb = new HashMap<>();
                whereb.put("tardis_id", id);
                HashMap<String, Object> setb = new HashMap<>();
                setb.put("world", emergency.getWorld().getName());
                setb.put("x", emergency.getBlockX());
                setb.put("y", emergency.getBlockY());
                setb.put("z", emergency.getBlockZ());
                setb.put("direction", "EAST");
                setb.put("submarine", 0);
                plugin.getQueryFactory().doUpdate("current", setb, whereb);
                TARDISMessage.send(p, "EMERGENCY_DONE");
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("tardis_id", id);
                plugin.getQueryFactory().alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("travel"), wherea, p);
            }
        }
    }
}
