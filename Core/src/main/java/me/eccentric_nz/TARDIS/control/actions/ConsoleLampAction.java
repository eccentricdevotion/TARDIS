/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.control.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.builders.utility.LightLevel;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetControls;
import me.eccentric_nz.TARDIS.utility.TARDISStaticLocationGetters;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.Light;

import java.util.HashMap;

public class ConsoleLampAction {

    private final TARDIS plugin;

    public ConsoleLampAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void illuminate(int id, int level, int control) {
        int setLevel = (level + 1) > 7 ? 0 : level + 1;
        // set control record
        HashMap<String, Object> set = new HashMap<>();
        HashMap<String, Object> wherec = new HashMap<>();
        if (control != -1) {
            set.put("secondary", setLevel);
            wherec.put("c_id", control);
        } else {
            set.put("secondary", setLevel);
            wherec.put("tardis_id", id);
            wherec.put("type", 56);
        }
        plugin.getQueryFactory().doSyncUpdate("controls", set, wherec);
        // get console lamp record
        HashMap<String, Object> where = new HashMap<>();
        where.put("tardis_id", id);
        where.put("type", 56);
        ResultSetControls rs = new ResultSetControls(plugin, where, false);
        if (rs.resultSet()) {
            // get the console lamp light block
            Location location = TARDISStaticLocationGetters.getLocationFromBukkitString(rs.getLocation());
            if (location != null) {
                Block block = location.getBlock();
                if (block.getType() == Material.LIGHT) {
                    Light light = (Light) block.getBlockData();
                    light.setLevel(LightLevel.interior_level[control == -1 ? level : setLevel]);
                    block.setBlockData(light);
                }
            }
        }
    }
}
