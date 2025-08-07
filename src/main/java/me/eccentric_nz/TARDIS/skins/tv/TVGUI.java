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
import me.eccentric_nz.TARDIS.custommodels.GUITelevision;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class TVGUI implements InventoryHolder {

    protected Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void addDefaults(ItemStack[] stack) {
        // download
        ItemStack down = ItemStack.of(GUITelevision.DOWNLOAD.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.displayName(Component.text("Toggle skin download"));
        CustomModelDataComponent component = load.getCustomModelDataComponent();
        component.setFloats(SwitchVariant.DOWNLOAD_OFF.getFloats());
        load.setCustomModelDataComponent(component);
        down.setItemMeta(load);
        stack[GUITelevision.DOWNLOAD.slot()] = down;
        // remove
        ItemStack remove = ItemStack.of(GUITelevision.REMOVE.material(), 1);
        ItemMeta rim = remove.getItemMeta();
        rim.displayName(Component.text("Remove skin"));
        remove.setItemMeta(rim);
        stack[GUITelevision.REMOVE.slot()] = remove;
        // back
        ItemStack back = ItemStack.of(GUITelevision.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.displayName(Component.text("Back"));
        back.setItemMeta(but);
        stack[GUITelevision.BACK.slot()] = back;
        // close
        ItemStack close = ItemStack.of(GUITelevision.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[GUITelevision.CLOSE.slot()] = close;
    }
}
