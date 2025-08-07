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
import me.eccentric_nz.TARDIS.database.data.Current;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetCurrentFromId;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLamps;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.block.data.Levelled;

import java.util.HashMap;

public class LightLevelAction {

    private final TARDIS plugin;

    public LightLevelAction(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void illuminate(int level, int control, boolean powered, int type, boolean policebox, int id, boolean lightsOn) {
        // save the level to the database
        int setLevel = (level + 1) > 7 ? 0 : level + 1;
        HashMap<String, Object> set = new HashMap<>();
        set.put("secondary", setLevel);
        HashMap<String, Object> where = new HashMap<>();
        where.put("c_id", control);
        plugin.getQueryFactory().doSyncUpdate("controls", set, where);
        // alter light levels
        if (powered) {
            int light_level;
            if (type == 49) {
                // exterior
                if (!policebox) {
                    return;
                }
                light_level = LightLevel.exterior_level[setLevel];
                // get current TARDIS location
                ResultSetCurrentFromId rsc = new ResultSetCurrentFromId(plugin, id);
                if (rsc.resultSet()) {
                    Current current = rsc.getCurrent();
                    if (current.location().getWorld() == null) {
                        return;
                    }
                    Location location = current.location();
                    while (!location.getChunk().isLoaded()) {
                        location.getChunk().load();
                    }
                    Block light = location.getBlock().getRelative(BlockFace.UP, 2);
                    if (light.getBlockData() instanceof Levelled levelled) {
                        levelled.setLevel(light_level);
                        light.setBlockData(levelled);
                    }
                }
            } else if (lightsOn) {
                // interior
                light_level = LightLevel.interior_level[setLevel];
                // get TARDIS lights
                HashMap<String, Object> whereLight = new HashMap<>();
                whereLight.put("tardis_id", id);
                ResultSetLamps rsl = new ResultSetLamps(plugin, whereLight, true);
                if (rsl.resultSet()) {
                    for (Block block : rsl.getData()) {
                        if (block.getBlockData() instanceof Levelled levelled) {
                            levelled.setLevel(light_level);
                            block.setBlockData(levelled);
                        }
                    }
                }
            }
        }
    }

}
