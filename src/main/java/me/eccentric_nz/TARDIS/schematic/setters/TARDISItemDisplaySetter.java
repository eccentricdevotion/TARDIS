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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Display;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

/**
 * @author eccentric_nz
 */
public class TARDISItemDisplaySetter {

    public static void fakeBlock(JsonObject json, Location start, int id) {
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
            if (stack.has("door")) {
                HashMap<String, Object> setd = new HashMap<>();
                String doorloc = block.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                setd.put("tardis_id", id);
                setd.put("door_type", 1);
                setd.put("door_location", doorloc);
                setd.put("door_direction", "SOUTH");
                TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
                // if create_worlds is true, set the world spawn
                if (TARDIS.plugin.getConfig().getBoolean("creation.create_worlds")) {
                    block.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), (l.getBlockZ() + 1));
                }
            }
            Material material = Material.valueOf(stack.get("type").getAsString());
            TARDISDisplayItem tdi = TARDISDisplayItem.getByMaterialAndData(material, model);
            if (tdi != null) {
                TARDISDisplayItemUtils.set(tdi, block);
            } else {
                setInRoom(block, material, model);
            }
        }
    }

    public static void setInRoom(Block block, Material material, int model) {
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 0.25d, 0.5d), EntityType.ITEM_DISPLAY);
        ItemStack is = new ItemStack(material);
        if (model != -1) {
            ItemMeta im = is.getItemMeta();
            im.setCustomModelData(model);
            is.setItemMeta(im);
        }
        display.setItemStack(is);
        display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
        display.setBillboard(Display.Billboard.VERTICAL);
        display.setInvulnerable(true);
    }
}
