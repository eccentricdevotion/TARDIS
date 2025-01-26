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
package me.eccentric_nz.TARDIS.travel;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUITemporalLocator;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
public class TARDISTemporalLocatorInventory {

    private final ItemStack[] temporal;
    private final TARDIS plugin;

    public TARDISTemporalLocatorInventory(TARDIS plugin) {
        this.plugin = plugin;
        temporal = getItemStack();
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] clocks = new ItemStack[27];
        for (GUITemporalLocator clock : GUITemporalLocator.values()) {
            ItemStack is = new ItemStack(clock.getMaterial(), 1);
            ItemMeta im = is.getItemMeta();
            if (clock.ordinal() < 4) {
                im.setDisplayName(plugin.getLanguage().getString(clock.toString()));
            } else {
                im.setDisplayName(clock.getName());
            }
            if (clock.getLore().contains("~")) {
                im.setLore(List.of(clock.getLore().split("~")));
            } else {
                im.setLore(List.of(clock.getLore()));
            }
            im.setItemModel(clock.getModel());
            is.setItemMeta(im);
            clocks[clock.getSlot()] = is;
        }
        return clocks;
    }

    public ItemStack[] getTemporal() {
        return temporal;
    }
}
