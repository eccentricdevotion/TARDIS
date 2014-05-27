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
package me.eccentric_nz.TARDIS.artron;

import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBeaconToggler {

    private final TARDIS plugin;

    public TARDISBeaconToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(String uuid, boolean on) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("uuid", uuid);
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        if (rs.resultSet()) {
            // toggle beacon
            String beacon = rs.getBeacon();
            String[] beaconData;
            int plusy = 0;
            if (beacon.isEmpty()) {
                // get the location from the TARDIS size and the creeper location
                switch (rs.getSchematic()) {
                    // TODO - Find out how far away the bedrock block is
                    case REDSTONE:
                        plusy = 18;
                        break;
                    case ELEVENTH:
                        plusy = 17;
                        break;
                    case DELUXE:
                        plusy = 16;
                        break;
                    case BIGGER:
                        plusy = 13;
                        break;
                    default: // BUDGET & STEAMPUNK
                        plusy = 12;
                        break;
                }
                String creeper = rs.getCreeper();
                beaconData = creeper.split(":");
            } else {
                beaconData = beacon.split(":");
            }
            World w = plugin.getServer().getWorld(beaconData[0]);
            int bx = plugin.getUtils().parseInt(beaconData[1]);
            int by = plugin.getUtils().parseInt(beaconData[2]) + plusy;
            int bz = plugin.getUtils().parseInt(beaconData[3]);
            if (beacon.isEmpty()) {
                // update the tardis table so we don't have to do this again
                String beacon_loc = beaconData[0] + ":" + beaconData[1] + ":" + by + ":" + beaconData[3];
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("beacon", beacon_loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", rs.getTardis_id());
                new QueryFactory(plugin).doUpdate("tardis", set, where);
            }
            Location bl = new Location(w, bx, by, bz);
            Block b = bl.getBlock();
            b.setTypeId((on) ? 20 : 7);
        }
    }
}
