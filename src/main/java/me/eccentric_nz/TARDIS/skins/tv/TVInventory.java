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
package me.eccentric_nz.TARDIS.skins.tv;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIData;
import me.eccentric_nz.TARDIS.custommodels.GUITelevision;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class TVInventory implements InventoryHolder {

    private final Inventory inventory;

    public TVInventory(TARDIS plugin) {
        this.inventory = plugin.getServer().createInventory(this, 36, Component.text("TARDIS Television", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Television GUI.
     *
     * @return an Array of item stacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[36];
        /*
        Doctors,
        Companions,
        Characters,
        Monsters,
        Close
         */
        for (GUIData tv : GUITelevision.values()) {
            ItemStack is = ItemStack.of(tv.material(), 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text(tv.name()));
            if (tv == GUITelevision.DOWNLOAD) {
                CustomModelDataComponent component = im.getCustomModelDataComponent();
                component.setFloats(SwitchVariant.DOWNLOAD_OFF.getFloats());
                im.setCustomModelDataComponent(component);
            }
            is.setItemMeta(im);
            stack[tv.slot()] = is;
        }
        return stack;
    }
}
