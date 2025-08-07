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
package me.eccentric_nz.TARDIS.schematic.setters;

import com.google.gson.JsonObject;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemUtils;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetDoors;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
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
        NamespacedKey model = null;
        if (json.has("stack")) {
            JsonObject stack = json.get("stack").getAsJsonObject();
            if (stack.has("cmd")) {
                String key = stack.get("cmd").getAsString();
                model = new NamespacedKey(TARDIS.plugin, key);
            }
            if (stack.has("door")) {
                if (id > 0) {
                    HashMap<String, Object> setd = new HashMap<>();
                    String doorloc = block.getWorld().getName() + ":" + l.getBlockX() + ":" + l.getBlockY() + ":" + l.getBlockZ();
                    setd.put("door_location", doorloc);
                    setd.put("door_direction", "SOUTH");
                    // check if there is an existing record
                    HashMap<String, Object> where = new HashMap<>();
                    where.put("tardis_id", id);
                    where.put("door_type", 1);
                    ResultSetDoors rsd = new ResultSetDoors(TARDIS.plugin, where, false);
                    if (rsd.resultSet()) {
                        // update
                        HashMap<String, Object> whered = new HashMap<>();
                        whered.put("tardis_id", id);
                        whered.put("door_type", 1);
                        TARDIS.plugin.getQueryFactory().doUpdate("doors", setd, whered);
                    } else {
                        // insert
                        setd.put("tardis_id", id);
                        setd.put("door_type", 1);
                        TARDIS.plugin.getQueryFactory().doInsert("doors", setd);
                    }
                    // if create_worlds is true, set the world spawn
                    if (TARDIS.plugin.getConfig().getBoolean("creation.create_worlds")) {
                        block.getWorld().setSpawnLocation(l.getBlockX(), l.getBlockY(), (l.getBlockZ() + 1));
                    }
                } else {
                    String name = getSchematicName(id);
                    if (!name.isEmpty()) {
                        // remember spawn location for this console preview
                        HashMap<String, Object> set = new HashMap<>();
                        set.put("tardis_id", id);
                        set.put("name", name);
                        set.put("world", block.getWorld().getName());
                        set.put("x", l.getBlockX() + 0.5d);
                        set.put("y", l.getBlockY());
                        set.put("z", (l.getBlockZ() + 1));
                        TARDIS.plugin.getQueryFactory().doInsert("transmats", set);
                    }
                }
            }
            Material material = Material.valueOf(stack.get("type").getAsString());
            TARDISDisplayItem tdi = model != null ? TARDISDisplayItem.getByModel(model): null;
            if (tdi != null) {
                ItemDisplay display = TARDISDisplayItemUtils.set(tdi, block, id);
                if (json.has("name")) {
                    ItemStack is = display.getItemStack();
                    ItemMeta im = is.getItemMeta();
                    im.displayName(ComponentUtils.fromJson(json.get("name")));
                    is.setItemMeta(im);
                    display.setItemStack(is);
                }
            } else {
                setInRoom(block, material, model);
            }
        }
    }

    private static String getSchematicName(int id) {
        for (Schematic schematic : Consoles.getBY_NAMES().values()) {
            if (schematic.getPreview() == id) {
                return schematic.getPermission();
            }
        }
        return "";
    }

    public static void setInRoom(Block block, Material material, NamespacedKey model) {
        ItemDisplay display = (ItemDisplay) block.getWorld().spawnEntity(block.getLocation().clone().add(0.5d, 0.25d, 0.5d), EntityType.ITEM_DISPLAY);
        ItemStack is = ItemStack.of(material);
        if (model != null) {
            ItemMeta im = is.getItemMeta();
            im.displayName(model.getKey().equals("xray")
                    ? Component.text("X-ray")
                    : Component.text(TARDISStringUtils.capitalise(model.getKey()))
            );
            is.setItemMeta(im);
            display.setItemDisplayTransform(ItemDisplay.ItemDisplayTransform.GROUND);
            display.setBillboard(Display.Billboard.VERTICAL);
        }
        display.setItemStack(is);
        display.setInvulnerable(true);
    }
}
