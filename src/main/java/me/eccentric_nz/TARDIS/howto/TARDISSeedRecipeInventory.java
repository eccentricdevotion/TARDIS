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
        ItemStack red = new ItemStack(Material.REDSTONE_TORCH_ON, 1);
        // lapis block
        ItemStack lapis = new ItemStack(Material.LAPIS_BLOCK, 1);
//        // restone lamp
//        ItemStack lamp = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);
//        ItemMeta lamp_meta = lamp.getItemMeta();
//        lamp_meta.setDisplayName("Police Box lamp");
//        lamp_meta.setLore(Arrays.asList("Any valid lamp item:", "Redstone Lamp", "Glowstone", "Torch", "Redstone Torch"));
//        lamp.setItemMeta(lamp_meta);
//        // police box wall
//        ItemStack pb_wall = new ItemStack(Material.WOOL, 1, (byte) 11);
//        ItemMeta pb_meta = pb_wall.getItemMeta();
//        pb_meta.setDisplayName("Police Box walls");
//        pb_meta.setLore(Arrays.asList("Any valid Chameleon block", "Click to see blocks..."));
//        pb_wall.setItemMeta(pb_meta);
        // interior wall
        ItemStack in_wall = new ItemStack(Material.WOOL, 1, (byte) 1);
        ItemMeta in_meta = in_wall.getItemMeta();
        in_meta.setDisplayName("Interior walls");
        in_meta.setLore(Arrays.asList("Any valid Wall/Floor block", "Click to see blocks..."));
        in_wall.setItemMeta(in_meta);
        // interior floor
        ItemStack in_floor = new ItemStack(Material.WOOL, 1, (byte) 8);
        ItemMeta fl_meta = in_floor.getItemMeta();
        fl_meta.setDisplayName("Interior floors");
        fl_meta.setLore(Arrays.asList("Any valid Wall/Floor block", "Click to see blocks..."));
        in_floor.setItemMeta(fl_meta);
        // tardis type
        ItemStack tardis = new ItemStack(block, 1);
        stack[0] = red;
        stack[9] = lapis;
//        stack[10] = lamp;
        stack[11] = in_wall;
        stack[18] = tardis;
//        stack[19] = pb_wall;
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
