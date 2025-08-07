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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

class SlideInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    SlideInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Slides", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItems());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    ItemStack[] getItems() {
        ItemStack[] stacks = new ItemStack[54];
        // make slides
        for (Slide slide : Slide.values()) {
            ItemStack is = ItemStack.of(Material.GLASS, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(slide.getName()));
            im.setItemModel(ChemistryEquipment.GLASS_SLIDE.getKey());
            im.getPersistentDataContainer().set(plugin.getMicroscopeKey(), PersistentDataType.STRING, slide.getModel().getKey());
            is.setItemMeta(im);
            stacks[slide.ordinal()] = is;
        }
        // Cancel / close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text("Close"));
        can.setItemModel(GuiVariant.CLOSE.getKey());
        close.setItemMeta(can);
        stacks[53] = close;
        return stacks;
    }
}
