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
package me.eccentric_nz.TARDIS.commands.preferences;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIKeyPreferences;
import me.eccentric_nz.TARDIS.custommodels.keys.KeyVariant;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

import java.util.List;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
class TARDISKeyMenuInventory {

    private final ItemStack[] menu;

    TARDISKeyMenuInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] itemStacks = new ItemStack[27];
        Material material;
        try {
            material = Material.valueOf(TARDIS.plugin.getConfig().getString("preferences.key"));
        } catch (IllegalArgumentException e) {
            material = Material.GOLD_NUGGET;
        }
        for (GUIKeyPreferences key : GUIKeyPreferences.values()) {
            ItemStack is = new ItemStack(key.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            if (key == GUIKeyPreferences.CLOSE || key == GUIKeyPreferences.INSTRUCTIONS || key == GUIKeyPreferences.NAME || key == GUIKeyPreferences.DISPLAY_NAME_COLOUR) {
                im.setDisplayName(key.getName());
            } else {
                is.setType(material);
                im.setDisplayName(ChatColor.WHITE + "TARDIS Key");
            }
            if (!key.getLore().isEmpty()) {
                if (key.getLore().contains("~")) {
                    String[] split = key.getLore().split("~");
                    im.setLore(List.of(split));
                } else {
                    im.setLore(List.of(key.getLore()));
                }
            }
            if (key.getSlot() < 17) {
                try {
                    KeyVariant variant = KeyVariant.valueOf(key.toString());
                    CustomModelDataComponent component = im.getCustomModelDataComponent();
                    component.setFloats(variant.getFloats());
                    im.setCustomModelDataComponent(component);
                } catch (IllegalArgumentException ignored) {
                }
            }
            is.setItemMeta(im);
            itemStacks[key.getSlot()] = is;
        }

        return itemStacks;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
