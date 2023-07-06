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
package me.eccentric_nz.TARDIS.floodgate;

import com.google.gson.JsonObject;
import java.util.HashMap;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.TARDISBlockSetters;
import org.bukkit.Location;
import org.bukkit.Material;

/**
 * @author eccentric_nz
 */
public class TARDISFloodgateDisplaySetter {

    public static void regularBlock(JsonObject json, Location start, int id) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int px = start.getBlockX() + rel.get("x").getAsInt();
        int py = start.getBlockY() + rel.get("y").getAsInt();
        int pz = start.getBlockZ() + rel.get("z").getAsInt();
        if (json.has("stack")) {
            JsonObject stack = json.get("stack").getAsJsonObject();
            if (stack.has("door")) {
                HashMap<String, Object> setd = new HashMap<>();
                String doorloc = start.getWorld().getName() + ":" + px + ":" + py + ":" + pz;
                setd.put("tardis_id", id);
                setd.put("door_type", 1);
                setd.put("door_location", doorloc);
                setd.put("door_direction", "SOUTH");
                TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
                // if create_worlds is true, set the world spawn
                if (TARDIS.plugin.getConfig().getBoolean("creation.create_worlds")) {
                    start.getWorld().setSpawnLocation(px, py, (pz + 1));
                }
            }
            Material material = Material.valueOf(stack.get("type").getAsString());
            if (material == Material.REDSTONE_LAMP || material == Material.SEA_LANTERN) {
                material = Material.LIGHT;
            }
            TARDISBlockSetters.setBlock(start.getWorld(), px, py, pz, material);
        }
    }
}
