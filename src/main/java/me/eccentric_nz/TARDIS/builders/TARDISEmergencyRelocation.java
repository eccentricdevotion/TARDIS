/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.database.data.Tardis;
import me.eccentric_nz.TARDIS.enumeration.COMPASS;
import me.eccentric_nz.TARDIS.travel.TARDISTimeTravel;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

/**
 *
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
            Location emergency = new TARDISTimeTravel(plugin).randomDestination(p, (byte) 15, (byte) 15, (byte) 15, COMPASS.EAST, "THIS", w, false, w.getSpawnLocation());
            if (emergency != null) {
                BuildData bd = new BuildData(plugin, p.getUniqueId().toString());
                bd.setLocation(emergency);
                bd.setTardisID(id);
                bd.setDirection(COMPASS.EAST);
                bd.setMalfunction(false);
                bd.setSubmarine(false);
                Tardis tardis = rs.getTardis();
                new TARDISInstaPreset(plugin, bd, tardis.getPreset(), Material.LIGHT_GRAY_TERRACOTTA.createBlockData(), false).buildPreset();
                QueryFactory qf = new QueryFactory(plugin);
                HashMap<String, Object> wherec = new HashMap<>();
                wherec.put("tardis_id", id);
                HashMap<String, Object> setc = new HashMap<>();
                setc.put("world", emergency.getWorld().getName());
                setc.put("x", emergency.getBlockX());
                setc.put("y", emergency.getBlockY());
                setc.put("z", emergency.getBlockZ());
                setc.put("direction", "EAST");
                setc.put("submarine", 0);
                qf.doUpdate("current", setc, wherec);
                HashMap<String, Object> whereb = new HashMap<>();
                whereb.put("tardis_id", id);
                HashMap<String, Object> setb = new HashMap<>();
                setb.put("world", emergency.getWorld().getName());
                setb.put("x", emergency.getBlockX());
                setb.put("y", emergency.getBlockY());
                setb.put("z", emergency.getBlockZ());
                setb.put("direction", "EAST");
                setb.put("submarine", 0);
                qf.doUpdate("current", setb, whereb);
                TARDISMessage.send(p, "EMERGENCY_DONE");
                HashMap<String, Object> wherea = new HashMap<>();
                wherea.put("tardis_id", id);
                qf.alterEnergyLevel("tardis", -plugin.getArtronConfig().getInt("travel"), wherea, p);
            }
        }
    }
}
