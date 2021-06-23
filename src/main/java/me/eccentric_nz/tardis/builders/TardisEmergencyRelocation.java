/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.builders;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.database.data.Tardis;
import me.eccentric_nz.tardis.database.resultset.ResultSetTardis;
import me.eccentric_nz.tardis.enumeration.CardinalDirection;
import me.eccentric_nz.tardis.enumeration.SpaceTimeThrottle;
import me.eccentric_nz.tardis.messaging.TardisMessage;
import me.eccentric_nz.tardis.travel.TardisTimeTravel;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Objects;

/**
 * @author eccentric_nz
 */
public class TardisEmergencyRelocation {

    private final TardisPlugin plugin;

    public TardisEmergencyRelocation(TardisPlugin plugin) {
        this.plugin = plugin;
    }

    public void relocate(int id, Player p) {
        TardisMessage.send(p, "EMERGENCY");
        // get the TARDIS
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        ResultSetTardis rs = new ResultSetTardis(plugin, where, "", false, 0);
        if (rs.resultSet()) {
            // get the servers main world
            World w = plugin.getServer().getWorlds().get(0);
            Location emergency = new TardisTimeTravel(plugin).randomDestination(p, 4, 4, 4, CardinalDirection.EAST, "THIS", w, false, w.getSpawnLocation());
            if (emergency != null) {
                BuildData bd = new BuildData(p.getUniqueId().toString());
                bd.setLocation(emergency);
                bd.setTardisId(id);
                bd.setDirection(CardinalDirection.EAST);
                bd.setMalfunction(false);
                bd.setSubmarine(false);
                bd.setThrottle(SpaceTimeThrottle.REBUILD);
                Tardis tardis = rs.getTardis();
                if (tardis.getPreset().usesItemFrame()) {
                    new TardisInstantPoliceBox(plugin, bd, tardis.getPreset()).buildPreset();
                } else {
                    new TardisInstantPreset(plugin, bd, tardis.getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
                }
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("world", Objects.requireNonNull(emergency.getWorld()).getName());
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
                TardisMessage.send(p, "EMERGENCY_DONE");
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("tardis_id", id);
                plugin.getQueryFactory().alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("travel"), wherea, p);
            }
        }
    }
}
