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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package me.eccentric_nz.TARDIS.files;

import java.util.ArrayList;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.database.ResultSetBlocks;
import me.eccentric_nz.TARDIS.database.ResultSetGravity;

/**
 *
 * @author eccentric_nz
 */
public class TARDISBlockLoader {

    private final TARDIS plugin;

    public TARDISBlockLoader(TARDIS plugin) {
        this.plugin = plugin;


    }

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

    public void loadGravityWells() {
        ResultSetGravity rsg = new ResultSetGravity(plugin, null, true);
        if (rsg.resultSet()) {
            ArrayList<HashMap<String, String>> data = rsg.getData();
            for (HashMap<String, String> map : data) {
                if (map.get("direction").endsWith("1")) {
                    // going up
                    plugin.gravityUpList.add(map.get("location"));
                } else {
                    // going down
                    plugin.gravityDownList.add(map.get("location"));
                }
            }
            plugin.debug("Loaded Gravity Wells");
        }
    }
}
