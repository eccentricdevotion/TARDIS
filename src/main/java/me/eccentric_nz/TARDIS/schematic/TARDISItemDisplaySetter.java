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
package me.eccentric_nz.TARDIS.schematic;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;

/**
 * @author macgeek
 */
public class TARDISItemDisplaySetter {

    public static void fakeBlock(JsonObject json, Location start) {
        JsonObject rel = json.get("rel_location").getAsJsonObject();
        int px = rel.get("x").getAsInt();
        int py = rel.get("y").getAsInt();
        int pz = rel.get("z").getAsInt();
        Location l = new Location(start.getWorld(), start.getBlockX() + px, start.getBlockY() + py, start.getBlockZ() + pz);
        Block block = l.getBlock();
        int model = -1;
        if (json.has("stack")) {
            JsonObject stack = json.get("stack").getAsJsonObject();
            if (stack.has("cmd")) {
                model = stack.get("cmd").getAsInt();
            }
            Material material = Material.valueOf(stack.get("type").getAsString());
            TARDISDisplayItem tdi = TARDISDisplayItem.getByMaterialAndData(material, model);
            if (tdi != null) {
                TARDISDisplayItemUtils.set(tdi, block);
            }
        }
    }
}
