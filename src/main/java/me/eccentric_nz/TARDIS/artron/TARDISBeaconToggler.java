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

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.QueryFactory;
import me.eccentric_nz.TARDIS.database.ResultSetTardis;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBeaconToggler {

    private final TARDIS plugin;
    private final List<String> no_beacon = Arrays.asList("plank", "tom");

    public TARDISBeaconToggler(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void flickSwitch(UUID uuid, boolean on) {
        HashMap<String, Object> whereb = new HashMap<String, Object>();
        whereb.put("uuid", uuid.toString());
        ResultSetTardis rs = new ResultSetTardis(plugin, whereb, "", false);
        if (rs.resultSet()) {
            SCHEMATIC schm = rs.getSchematic();
            if (no_beacon.contains(schm.getPermission())) {
                // doesn't have a beacon!
                return;
            }
            // toggle beacon
            String beacon = rs.getBeacon();
            String[] beaconData;
            int plusy = 0;
            if (beacon.isEmpty()) {
                // get the location from the TARDIS size and the creeper location
                if (schm.getDescription().equals("redstone")) {
                    plusy = 14;
                } else if (schm.getDescription().equals("eleventh")) {
                    plusy = 22;
                } else if (schm.getDescription().equals("deluxe")) {
                    plusy = 23;
                } else if (schm.getDescription().equals("bigger") || schm.getDescription().equals("ars")) {
                    plusy = 12;
                } else { // BUDGET, STEAMPUNK, WAR, CUSTOM?
                    plusy = 11;
                }
                String creeper = rs.getCreeper();
                beaconData = creeper.split(":");
            } else {
                beaconData = beacon.split(":");
            }
            World w = plugin.getServer().getWorld(beaconData[0]);
            boolean stuffed = (beaconData[1].contains(".5"));
            int bx, bz;
            // get rid of decimal places due to incorrectly copied values from creeper field...
            if (stuffed) {
                bx = (int) TARDISNumberParsers.parseFloat(beaconData[1]) * 1;
                bz = (int) TARDISNumberParsers.parseFloat(beaconData[3]) * 1;
            } else {
                bx = TARDISNumberParsers.parseInt(beaconData[1]);
                bz = TARDISNumberParsers.parseInt(beaconData[3]);
            }
            int by = (int) TARDISNumberParsers.parseFloat(beaconData[2]) * 1 + plusy;
            if (beacon.isEmpty() || stuffed) {
                // update the tardis table so we don't have to do this again
                String beacon_loc = beaconData[0] + ":" + bx + ":" + by + ":" + bz;
                HashMap<String, Object> set = new HashMap<String, Object>();
                set.put("beacon", beacon_loc);
                HashMap<String, Object> where = new HashMap<String, Object>();
                where.put("tardis_id", rs.getTardis_id());
                new QueryFactory(plugin).doUpdate("tardis", set, where);
            }
            Location bl = new Location(w, bx, by, bz);
            Block b = bl.getBlock();
            while (!b.getChunk().isLoaded()) {
                b.getChunk().load();
            }
            b.setType((on) ? Material.GLASS : Material.REDSTONE_BLOCK);
            if (!plugin.getGeneralKeeper().getProtectBlockMap().containsKey(bl.toString())) {
                plugin.getGeneralKeeper().getProtectBlockMap().put(bl.toString(), rs.getTardis_id());
            }
        }
    }
}
