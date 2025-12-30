/*
 * Copyright (C) 2026 eccentric_nz
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
package me.eccentric_nz.TARDIS.console.models;

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.ItemDisplay;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.UUID;

public class ConsoleColourChanger {

    private final TARDIS plugin;
    private final Location location;
    private final String uuids;
    private final String colour;

    public ConsoleColourChanger(TARDIS plugin, Location location, String uuids, String colour) {
        this.plugin = plugin;
        this.location = location;
        this.uuids = uuids;
        this.colour = colour;
    }

    public boolean paint() {
        String[] split = uuids.split("~");
        for (String s : split) {
            try {
                UUID uuid = UUID.fromString(s);
                for (Entity e : location.getWorld().getNearbyEntities(location, 5, 5, 5, (d) -> d.getType() == EntityType.ITEM_DISPLAY)) {
                    UUID p = e.getPersistentDataContainer().get(plugin.getInteractionUuidKey(), plugin.getPersistentDataTypeUUID());
                    if (uuid.equals(p) && e instanceof ItemDisplay display) {
                        // get the item stack
                        ItemStack is = display.getItemStack();
                        if (is != null) {
                            ItemMeta im = is.getItemMeta();
                            NamespacedKey model;
                            if (im.hasItemModel()) {
                                String[] key = im.getItemModel().getKey().split("_");
                                if (key[1].equals("centre")) {
                                    model = new NamespacedKey(plugin, "console_centre_" + colour);
                                } else if (key[1].equals("division")) {
                                    model = new NamespacedKey(plugin, "console_division_" + colour);
                                } else {
                                    model = new NamespacedKey(plugin, "console_side_" + colour);
                                }
                                im.setItemModel(model);
                                is.setItemMeta(im);
                                display.setItemStack(is);
                            }
                        }
                    }
                }
            } catch (IllegalArgumentException ignored) {
                return false;
            }
        }
        return true;
    }
}
