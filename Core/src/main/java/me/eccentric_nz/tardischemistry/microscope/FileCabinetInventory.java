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
package me.eccentric_nz.tardischemistry.microscope;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ChemistryEquipment;
import me.eccentric_nz.TARDIS.custommodels.keys.GuiVariant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

class FileCabinetInventory {

    private final TARDIS plugin;

    FileCabinetInventory(TARDIS plugin) {
        this.plugin = plugin;
    }

    ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[54];
        // make screens
        for (ScopeView view : ScopeView.values()) {
            ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(view.getName());
            im.setItemModel(ChemistryEquipment.FOLDER.getKey());
            im.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.STRING, view.getModel().getKey());
            is.setItemMeta(im);
            stacks[view.ordinal()] = is;
        }
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName("Close");
        can.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(can);
        stacks[53] = close;
        return stacks;
    }
}
