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
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.components.CustomModelDataComponent;

public class TVGUI {

    public void addDefaults(ItemStack[] stack) {
        // download
        ItemStack down = new ItemStack(GUITelevision.DOWNLOAD.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("Toggle skin download");
        CustomModelDataComponent component = load.getCustomModelDataComponent();
        component.setFloats(SwitchVariant.DOWNLOAD_OFF.getFloats());
        load.setCustomModelDataComponent(component);
        down.setItemMeta(load);
        stack[GUITelevision.DOWNLOAD.slot()] = down;
        // remove
        ItemStack remove = new ItemStack(GUITelevision.REMOVE.material(), 1);
        ItemMeta rim = remove.getItemMeta();
        rim.setDisplayName("Remove skin");
        remove.setItemMeta(rim);
        stack[GUITelevision.REMOVE.slot()] = remove;
        // back
        ItemStack back = new ItemStack(GUITelevision.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        back.setItemMeta(but);
        stack[GUITelevision.BACK.slot()] = back;
        // close
        ItemStack close = new ItemStack(GUITelevision.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(TARDIS.plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(close_im);
        stack[GUITelevision.CLOSE.slot()] = close;
    }
}
