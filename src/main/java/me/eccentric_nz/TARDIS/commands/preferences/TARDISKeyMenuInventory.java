/*
 * Copyright (C) 2021 eccentric_nz
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

import me.eccentric_nz.TARDIS.custommodeldata.GUIKeyPreferences;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;

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

        for (GUIKeyPreferences key : GUIKeyPreferences.values()) {
            ItemStack is = new ItemStack(key.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            if (key == GUIKeyPreferences.CLOSE || key == GUIKeyPreferences.INSTRUCTIONS) {
                im.setDisplayName(key.getName());
            } else {
                im.setDisplayName(key.getChatColor() + "TARDIS Key");
            }
            if (!key.getLore().isEmpty()) {
                if (key.getLore().contains("~")) {
                    String[] split = key.getLore().split("~");
                    im.setLore(Arrays.asList(split));
                } else {
                    im.setLore(Collections.singletonList(key.getLore()));
                }
            }
            im.setCustomModelData(key.getCustomModelData());
            is.setItemMeta(im);
            itemStacks[key.getSlot()] = is;
        }

        return itemStacks;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
