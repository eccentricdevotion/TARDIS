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
package me.eccentric_nz.TARDIS.desktop;

import me.eccentric_nz.TARDIS.TARDIS;
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
public class TARDISWallsInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;

    public TARDISWallsInventory(TARDIS plugin) {
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
        stack[GUIWallFloor.BUTTON_SCROLL_D.slot()] = scroll_down;
        // default wall
        ItemStack wall = new ItemStack(GUIWallFloor.WALL.material(), 1);
        ItemMeta wim = wall.getItemMeta();
        wim.setDisplayName("Default Wall Block");
        wim.setItemModel(GUIWallFloor.WALL.key());
        wall.setItemMeta(wim);
        stack[GUIWallFloor.WALL.slot()] = wall;
        // default floor
        ItemStack floor = new ItemStack(GUIWallFloor.FLOOR.material(), 1);
        ItemMeta fim = floor.getItemMeta();
        fim.setDisplayName("Default Floor Block");
        fim.setItemModel(GUIWallFloor.FLOOR.key());
        floor.setItemMeta(fim);
        stack[GUIWallFloor.FLOOR.slot()] = floor;
        // close
        ItemStack close = new ItemStack(GUIWallFloor.BUTTON_ABORT.material(), 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Abort upgrade");
        close_im.setItemModel(GUIWallFloor.BUTTON_ABORT.key());
        close.setItemMeta(close_im);
        stack[GUIWallFloor.BUTTON_ABORT.slot()] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
