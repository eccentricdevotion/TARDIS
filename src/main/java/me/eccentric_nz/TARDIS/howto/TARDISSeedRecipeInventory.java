/*
 * Copyright (C) 2016 eccentric_nz
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

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISSeedRecipeInventory {

    private final ItemStack[] menu;
    private final Material block;

    public TARDISSeedRecipeInventory(Material block) {
        this.block = block;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // redstone torch
        ItemStack red = new ItemStack(Material.REDSTONE_TORCH, 1);
        // lapis block
        ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK, 1);
        // interior wall
        ItemStack in_wall = new ItemStack(Material.ORANGE_WOOL, 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.setDisplayName("Interior walls");
        in_meta.setLore(Arrays.asList("Any valid Wall/Floor block", "Click to see blocks..."));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = new ItemStack(Material.LIGHT_GRAY_WOOL, 1);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.setDisplayName("Interior floors");
        fl_meta.setLore(Arrays.asList("Any valid Wall/Floor block", "Click to see blocks..."));
        in_floor.setItemMeta(fl_meta);
        // tardis type
        ItemStack tardis = new ItemStack(block, 1);
        stack[0] = red;
        stack[9] = lapis;
        stack[11] = in_wall;
        stack[18] = tardis;
        stack[20] = in_floor;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);
        stack[26] = close;
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta back_im = back.getItemMeta();
        back_im.setDisplayName("Back");
        back.setItemMeta(back_im);
        stack[8] = back;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
