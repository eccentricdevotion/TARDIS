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
package me.eccentric_nz.TARDIS.commands.admin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that
 * planet.
 *
 * @author eccentric_nz
 */
public class TARDISAdminMenuInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public TARDISAdminMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Admin Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        List<ItemStack> options = new ArrayList<ItemStack>();
        int i = 0;
        Set<String> config = new TreeSet(plugin.getConfig().getKeys(true));
        for (String c : config) {
            String value = plugin.getConfig().getString(c);
            if ((value.equalsIgnoreCase("true") || value.equalsIgnoreCase("false")) && !c.equals("conversion_done") && !c.equals("location_conversion_done")) {
                ItemStack is = new ItemStack(TARDISConstants.GUI_ITEMS.get(i), 1);
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
