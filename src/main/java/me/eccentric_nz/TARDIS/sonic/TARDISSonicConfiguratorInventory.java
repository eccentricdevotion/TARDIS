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
package me.eccentric_nz.TARDIS.sonic;

import me.eccentric_nz.TARDIS.custommodeldata.GUISonicConfigurator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class TARDISSonicConfiguratorInventory {

    private final ItemStack[] configurator;

    public TARDISSonicConfiguratorInventory() {
        configurator = getItemStack();
    }

    /**
     * Constructs an inventory for the Sonic Configurator Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[27];
        for (GUISonicConfigurator gui : GUISonicConfigurator.values()) {
            if (gui.getSlot() != -1) {
                ItemStack is = new ItemStack(gui.getMaterial(), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(gui.getName());
                if (!gui.getLore().isEmpty()) {
                    im.setLore(Arrays.asList(gui.getLore().split("~")));
                }
                im.setCustomModelData(gui.getCustomModelData());
                is.setItemMeta(im);
                stack[gui.getSlot()] = is;
            }
        }
        ItemStack place = new ItemStack(GUISonicConfigurator.PLACE_SONIC.getMaterial(), 1);
        ItemMeta pim = place.getItemMeta();
        pim.setDisplayName(GUISonicConfigurator.PLACE_SONIC.getName());
        pim.setCustomModelData(GUISonicConfigurator.PLACE_SONIC.getCustomModelData());
        place.setItemMeta(pim);
        stack[9] = place;
        ItemStack wool = new ItemStack(GUISonicConfigurator.WAITING.getMaterial(), 1);
        ItemMeta wim = wool.getItemMeta();
        wim.setDisplayName(" ");
        wim.setCustomModelData(GUISonicConfigurator.WAITING.getCustomModelData());
        wool.setItemMeta(wim);
        for (int i = 10; i < 17; i++) {
            stack[i] = wool;
        }
        return stack;
    }

    public ItemStack[] getConfigurator() {
        return configurator;
    }
}
