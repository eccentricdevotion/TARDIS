/*
 * Copyright (C) 2019 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
class TARDISPoliceBoxInventory {

    private final ItemStack[] boxes;
    private final TARDIS plugin;
    List<String> colours = Arrays.asList("Blue", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Brown", "Green", "Red", "Black");

    public TARDISPoliceBoxInventory(TARDIS plugin) {
        this.plugin = plugin;
        boxes = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] boxes = new ItemStack[27];
        int i = 0;
        // coloured police boxes
        for (String s : colours) {
            ItemStack is = new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(s + " Police Box");
            im.setCustomModelData(10000003 + i);
            is.setItemMeta(im);
            boxes[i] = is;
            i++;
        }
        // page one
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta one = page.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        page.setItemMeta(one);
        boxes[24] = page;
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setCustomModelData(GUIChameleonPoliceBoxes.BACK.getCustomModelData());
        back.setItemMeta(but);
        boxes[25] = back;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIChameleonPoliceBoxes.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        boxes[26] = close;

        return boxes;
    }

    public ItemStack[] getBoxes() {
        return boxes;
    }
}
