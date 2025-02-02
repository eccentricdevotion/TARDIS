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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISLightEmittingInventory {

    private final TARDIS plugin;
    private final ItemStack[] GUI;

    public TARDISLightEmittingInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.GUI = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[27];
        // 0 -> lightEmitting.size() - 1
        for (int i = 0; i < Sequences.LIGHT_EMITTING.size(); i++) {
            ItemStack light = new ItemStack(Sequences.LIGHT_EMITTING.get(i), 1);
            stacks[i] = light;
        }
        // back button
        ItemStack back = new ItemStack(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName("Back");
        bk.setItemModel(GUIChameleonTemplate.BACK_HELP.key());
        back.setItemMeta(bk);
        stacks[24] = back;
        // 26 close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setItemModel(GUILights.CLOSE.key());
        close.setItemMeta(clim);
        stacks[26] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
