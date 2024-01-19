/*
 * Copyright (C) 2024 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUISonicPreferences;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicMenuInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public TARDISSonicMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Sonic Screwdriver Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[36];
        for (GUISonicPreferences sonic : GUISonicPreferences.values()) {
            if (sonic.getMaterial() == Material.BLAZE_ROD) {
                ItemStack is = new ItemStack(sonic.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName("Sonic Screwdriver");
                im.setLore(Collections.singletonList(sonic.getName()));
                im.setCustomModelData(sonic.getCustomModelData());
                is.setItemMeta(im);
                stack[sonic.getSlot()] = is;
            }
        }
        // coloured wool
        ItemStack wool = new ItemStack(Material.WHITE_WOOL);
        ItemMeta wool_im = wool.getItemMeta();
        wool_im.setDisplayName("Display name colour");
        wool_im.setLore(List.of("Click to select"));
        wool.setItemMeta(wool_im);
        stack[28] = wool;
        // info
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions");
        List<String> lore = Arrays.asList("Put your Sonic Screwdriver", "in the bottom left most slot", "and then click on the", "Sonic of your choice.");
        info_im.setLore(lore);
        info_im.setCustomModelData(GUISonicPreferences.INSTRUCTIONS.getCustomModelData());
        info.setItemMeta(info_im);
        stack[31] = info;
        // info 2
        ItemStack name = new ItemStack(Material.BOOK, 1);
        ItemMeta name_im = name.getItemMeta();
        name_im.setDisplayName("Name");
        List<String> display = Arrays.asList("If you want to have", "a coloured display name", "click the wool block", "to choose a colour.");
        name_im.setLore(display);
        name_im.setCustomModelData(GUISonicPreferences.INSTRUCTIONS.getCustomModelData());
        name.setItemMeta(name_im);
        stack[32] = name;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setCustomModelData(GUISonicPreferences.CLOSE.getCustomModelData());
        close.setItemMeta(close_im);
        stack[35] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
