/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUITelevision;
import me.eccentric_nz.TARDIS.custommodels.keys.SwitchVariant;
import net.kyori.adventure.text.Component;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class TVGUI implements InventoryHolder {

    protected Inventory inventory;

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    public void addDefaults(ItemStack[] stack) {
        // download
        ItemStack download = ItemStack.of(GUITelevision.DOWNLOAD.material(), 1);
        download.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Toggle skin download"));
        download.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                .addFloats(SwitchVariant.DOWNLOAD_OFF.getFloats())
                .build());
        stack[GUITelevision.DOWNLOAD.slot()] = download;
        // remove
        ItemStack remove = ItemStack.of(GUITelevision.REMOVE.material(), 1);
        remove.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Remove skin"));
        stack[GUITelevision.REMOVE.slot()] = remove;
        // back
        ItemStack back = ItemStack.of(GUITelevision.BACK.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        stack[GUITelevision.BACK.slot()] = back;
        // close
        stack[GUITelevision.CLOSE.slot()] = GUIItemFactory.close();
    }
}
