/*
 * Copyright (C) 2014 eccentric_nz
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
package me.eccentric_nz.TARDIS.ARS;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural
 * Configuration software to relocate the console outside the ship (as it was
 * too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSMap {

    private final ItemStack[] map;

    public TARDISARSMap() {
        this.map = getItemStack();
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System Map.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // direction pad
        // up
        ItemStack pad_up = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName("Up");
        pad_up.setItemMeta(up);
        // down
        ItemStack pad_down = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName("Down");
        pad_down.setItemMeta(down);
        // left
        ItemStack pad_left = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName("Left");
        pad_left.setItemMeta(left);
        // right
        ItemStack pad_right = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName("Right");
        pad_right.setItemMeta(right);
        // level selected
        ItemStack level_sel = new ItemStack(Material.WOOL, 1, (byte) 4);
        ItemMeta main = level_sel.getItemMeta();
        main.setDisplayName("Main level");
        level_sel.setItemMeta(main);
        // level top
        ItemStack level_top = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta top = level_top.getItemMeta();
        top.setDisplayName("Top level");
        level_top.setItemMeta(top);
        // level top
        ItemStack level_bot = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta bot = level_bot.getItemMeta();
        bot.setDisplayName("Bottom level");
        level_bot.setItemMeta(bot);
        // stone
        ItemStack black = new ItemStack(Material.WOOL, 1, (byte) 15);
        ItemMeta wool = black.getItemMeta();
        wool.setDisplayName("Load the map!");
        black.setItemMeta(wool);
        // load map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta load = map.getItemMeta();
        load.setDisplayName("Load map");
        map.setItemMeta(load);
        // close
        ItemStack close = new ItemStack(Material.WOOL, 1, (byte) 6);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName("Close");
        close.setItemMeta(gui);
        // where am I?
        ItemStack where = new ItemStack(Material.COMPASS, 1);
        ItemMeta ami = where.getItemMeta();
        ami.setDisplayName("Where am I?");
        where.setItemMeta(ami);

        ItemStack[] is = {
            null, pad_up, null, null, black, black, black, black, black,
            pad_left, map, pad_right, null, black, black, black, black, black,
            null, pad_down, null, null, black, black, black, black, black,
            level_bot, level_sel, level_top, null, black, black, black, black, black,
            null, null, null, null, black, black, black, black, black,
            close, null, where, null, null, null, null, null, null
        };
        return is;
    }

    public ItemStack[] getMap() {
        return map;
    }
}
