/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 *
 * @author eccentric_nz
 */
public class TARDISAdminMenuInventory {

    private TARDIS plugin;
    private List<Integer> ids = new ArrayList<Integer>();
    private ItemStack[] menu;

    public TARDISAdminMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        ids.addAll(Arrays.asList(new Integer[]{6, 37, 38, 39, 40, 50, 131, 260, 262, 266, 280, 281, 287, 288, 289, 291, 295, 296, 297, 318, 320, 321, 322, 323, 326, 328, 331, 332, 334, 336, 337, 338, 75, 339, 340, 341, 344, 345, 347, 349, 352, 355, 357, 358, 359, 360, 364, 365, 368, 385, 386, 390, 393, 2262}));
        this.menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        List<ItemStack> options = new ArrayList<ItemStack>();
        int i = 0;
        Set<String> config = new TreeSet(plugin.getConfig().getKeys(false));
        for (String c : config) {
            String value = plugin.getConfig().getString(c);
            if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) {
                ItemStack is = new ItemStack(ids.get(i), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(c);
                im.setLore(Arrays.asList(new String[]{value}));
                is.setItemMeta(im);
                options.add(is);
                i++;
            }
        }
        ItemStack[] stack = new ItemStack[54];
        for (int s = 0; s < 54; s++) {
            if (s < options.size()) {
                stack[s] = options.get(s);
            } else {
                stack[s] = null;
            }
        }
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
