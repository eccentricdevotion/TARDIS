/*
 * Copyright (C) 2013 eccentric_nz
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
public class TARDISARSInventory {

    private final ItemStack[] terminal;

    public TARDISARSInventory() {
        this.terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System GUI.
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
        ItemStack stone = new ItemStack(Material.WOOL, 1, (byte) 15);
        ItemMeta s1 = stone.getItemMeta();
        s1.setDisplayName("Empty slot");
        stone.setItemMeta(s1);
        // scroll left
        ItemStack scroll_left = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.setDisplayName("Scroll left");
        scroll_left.setItemMeta(nim);
        // scroll right
        ItemStack scroll_right = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.setDisplayName("Scroll right");
        scroll_right.setItemMeta(pim);
        // set
        ItemStack s = new ItemStack(Material.WOOL, 1, (byte) 6);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName("Reconfigure!");
        s.setItemMeta(sim);
        // passage
        ItemStack passage = new ItemStack(Material.CLAY, 1);
        ItemMeta clay = passage.getItemMeta();
        clay.setDisplayName("Passage");
        passage.setItemMeta(clay);
        // arboretum
        ItemStack arboretum = new ItemStack(Material.LEAVES, 1);
        ItemMeta leaves = arboretum.getItemMeta();
        leaves.setDisplayName("Arboretum");
        arboretum.setItemMeta(leaves);
        // bedroom
        ItemStack bedroom = new ItemStack(Material.GLOWSTONE, 1);
        ItemMeta glow = bedroom.getItemMeta();
        glow.setDisplayName("Bedroom");
        bedroom.setItemMeta(glow);
        // kitchen
        ItemStack kitchen = new ItemStack(Material.PUMPKIN, 1);
        ItemMeta pumpkin = kitchen.getItemMeta();
        pumpkin.setDisplayName("Kitchen");
        kitchen.setItemMeta(pumpkin);
        // library
        ItemStack library = new ItemStack(Material.ENCHANTMENT_TABLE, 1);
        ItemMeta book = library.getItemMeta();
        book.setDisplayName("Library");
        library.setItemMeta(book);
        // pool
        ItemStack pool = new ItemStack(Material.SNOW_BLOCK, 1);
        ItemMeta snow = pool.getItemMeta();
        snow.setDisplayName("Pool");
        pool.setItemMeta(snow);
        // vault
        ItemStack vault = new ItemStack(Material.DISPENSER, 1);
        ItemMeta dispenser = vault.getItemMeta();
        dispenser.setDisplayName("Storage Vault");
        vault.setItemMeta(dispenser);
        // workshop
        ItemStack workshop = new ItemStack(Material.NETHER_BRICK, 1);
        ItemMeta brick = workshop.getItemMeta();
        brick.setDisplayName("Workshop");
        workshop.setItemMeta(brick);
        // empty
        ItemStack empty = new ItemStack(Material.GLASS, 1);
        ItemMeta glass = empty.getItemMeta();
        glass.setDisplayName("Empty");
        empty.setItemMeta(glass);
        // jettison
        ItemStack jettison = new ItemStack(Material.TNT, 1);
        ItemMeta tnt = jettison.getItemMeta();
        tnt.setDisplayName("Jettison");
        jettison.setItemMeta(tnt);
        // reset
        ItemStack reset = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.setDisplayName("Reset selected");
        reset.setItemMeta(cobble);
        // load map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta load = map.getItemMeta();
        load.setDisplayName("Load map");
        map.setItemMeta(load);

        ItemStack[] is = {
            null, pad_up, null, null, stone, stone, stone, stone, stone,
            pad_left, map, pad_right, s, stone, stone, stone, stone, stone,
            null, pad_down, null, null, stone, stone, stone, stone, stone,
            level_bot, level_sel, level_top, reset, stone, stone, stone, stone, stone,
            scroll_left, null, scroll_right, jettison, stone, stone, stone, stone, stone,
            passage, arboretum, bedroom, kitchen, library, pool, vault, workshop, empty
        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
