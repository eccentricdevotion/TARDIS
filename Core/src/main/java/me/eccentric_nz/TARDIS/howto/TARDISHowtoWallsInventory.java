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
package me.eccentric_nz.TARDIS.howto;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIWallFloor;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone through at least twelve redesigns, though
 * the TARDIS revealed that she had archived 30 versions. Once a control room was reconfigured, the TARDIS archived the
 * old design "for neatness". The TARDIS effectively "curated" a museum of control rooms — both those in the Doctor's
 * personal past and future
 *
 * @author eccentric_nz
 */
class TARDISHowtoWallsInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;

    public TARDISHowtoWallsInventory(TARDIS plugin) {
        this.plugin = plugin;
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get BLOCKS
        for (Material entry : TARDISWalls.BLOCKS) {
            if (i > 52) {
                break;
            }
            ItemStack is = new ItemStack(entry, 1);
            stack[i] = is;
            if (i % 9 == 7) {
                i += 2;
            } else {
                i++;
            }
        }

        // scroll up
        ItemStack scroll_up = new ItemStack(GUIWallFloor.BUTTON_SCROLL_U.material(), 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_U"));
        uim.setItemModel(GUIWallFloor.BUTTON_SCROLL_U.key());
        scroll_up.setItemMeta(uim);
        stack[GUIWallFloor.BUTTON_SCROLL_U.slot()] = scroll_up;
        // scroll down
        ItemStack scroll_down = new ItemStack(GUIWallFloor.BUTTON_SCROLL_D.material(), 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_D"));
        dim.setItemModel(GUIWallFloor.BUTTON_SCROLL_D.key());
        scroll_down.setItemMeta(dim);
        stack[26] = scroll_down;
        // back
        ItemStack back = new ItemStack(GUIChameleonPresets.BACK.material(), 1);
        ItemMeta back_im = back.getItemMeta();
        back_im.setDisplayName("Back");
        back_im.setItemModel(GUIChameleonPresets.BACK.key());
        back.setItemMeta(back_im);
        stack[44] = back;
        // close
        ItemStack close = new ItemStack(GUIChameleonPresets.CLOSE.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close_im.setItemModel(GUIChameleonPresets.CLOSE.key());
        close.setItemMeta(close_im);
        stack[GUIChameleonPresets.CLOSE.slot()] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
