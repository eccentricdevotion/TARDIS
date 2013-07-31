/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.files;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetGravity;

/**
 * An anti-gravity spiral is a projectable beam used for removing gravity from
 * an object. The Seventh Doctor used his TARDIS to project a beam around a bus
 * in space after it crashed. He manoeuvred it down to Earth and dropped it
 * outside Shangri-La camp in southern Wales.
 *
 * @author eccentric_nz
 */
public class TARDISBlockLoader {

    private final TARDIS plugin;

    public TARDISBlockLoader(TARDIS plugin) {
        this.plugin = plugin;
    }

    /**
     * Loads Police Box and precious TARDIS blocks for protection from griefing
     * and harvesting. This speeds the looking up of block locations, as no
     * database interaction is required.
     */
    public void loadProtectBlocks() {
        ResultSetBlocks rsb = new ResultSetBlocks(plugin, null, true);
        if (rsb.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsb.getData();
            for (HashMap<String, String> map : data) {
                plugin.protectBlockMap.put(map.get("location"), Integer.valueOf(map.get("tardis_id")));
            }
            plugin.debug("Loaded blocks for protection");
        }
    }

    /**
     * Loads Gravity Well blocks to speed up block lookups (no database
     * interaction is required).
     */
    public void loadGravityWells() {
        ResultSetGravity rsg = new ResultSetGravity(plugin, null, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsg.getData();
            for (HashMap<String, String> map : data) {
                int i = plugin.utils.parseNum(map.get("direction"));
                Double[] values = new Double[3];
                values[0] = Double.valueOf(map.get("direction"));
                values[1] = Double.valueOf(map.get("distance"));
                values[2] = Double.valueOf(map.get("velocity"));
                switch (i) {
                    case 1:
                        // going up
                        plugin.gravityUpList.put(map.get("location"), values);
                        break;
                    case 2:
                        // going north
                        plugin.gravityNorthList.put(map.get("location"), values);
                        break;
                    case 3:
                        // going west
                        plugin.gravityWestList.put(map.get("location"), values);
                        break;
                    case 4:
                        // going south
                        plugin.gravitySouthList.put(map.get("location"), values);
                        break;
                    case 5:
                        // going east
                        plugin.gravityEastList.put(map.get("location"), values);
                        break;
                    default:
                        // going down
                        plugin.gravityDownList.add(map.get("location"));
                        break;
                }
            }
            plugin.debug("Loaded Gravity Wells");
        }
    }
}
