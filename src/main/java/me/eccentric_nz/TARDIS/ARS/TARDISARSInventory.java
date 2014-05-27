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

import me.eccentric_nz.TARDIS.TARDIS;
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
    private final TARDIS plugin;

    public TARDISARSInventory(TARDIS plugin) {
        this.plugin = plugin;
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
        up.setDisplayName(plugin.getLanguage().getString("BUTTON_UP"));
        pad_up.setItemMeta(up);
        // down
        ItemStack pad_down = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName(plugin.getLanguage().getString("BUTTON_DOWN"));
        pad_down.setItemMeta(down);
        // left
        ItemStack pad_left = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName(plugin.getLanguage().getString("BUTTON_LEFT"));
        pad_left.setItemMeta(left);
        // right
        ItemStack pad_right = new ItemStack(Material.WOOL, 1, (byte) 9);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName(plugin.getLanguage().getString("BUTTON_RIGHT"));
        pad_right.setItemMeta(right);
        // level selected
        ItemStack level_sel = new ItemStack(Material.WOOL, 1, (byte) 4);
        ItemMeta main = level_sel.getItemMeta();
        main.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL"));
        level_sel.setItemMeta(main);
        // level top
        ItemStack level_top = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta top = level_top.getItemMeta();
        top.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_T"));
        level_top.setItemMeta(top);
        // level top
        ItemStack level_bot = new ItemStack(Material.WOOL, 1, (byte) 0);
        ItemMeta bot = level_bot.getItemMeta();
        bot.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_B"));
        level_bot.setItemMeta(bot);
        // black wool
        ItemStack black = new ItemStack(Material.WOOL, 1, (byte) 15);
        ItemMeta wool = black.getItemMeta();
        wool.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP_NO"));
        black.setItemMeta(wool);
        // scroll left
        ItemStack scroll_left = new ItemStack(Material.WOOL, 1, (byte) 14);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_L"));
        scroll_left.setItemMeta(nim);
        // scroll right
        ItemStack scroll_right = new ItemStack(Material.WOOL, 1, (byte) 5);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_R"));
        scroll_right.setItemMeta(pim);
        // set
        ItemStack s = new ItemStack(Material.WOOL, 1, (byte) 6);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_RECON"));
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
        // library
        ItemStack gene = new ItemStack(Material.FURNACE, 1);
        ItemMeta tic = gene.getItemMeta();
        tic.setDisplayName("Genetic Manipulator");
        gene.setItemMeta(tic);
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
        // jettison
        ItemStack jettison = new ItemStack(Material.TNT, 1);
        ItemMeta tnt = jettison.getItemMeta();
        tnt.setDisplayName(plugin.getLanguage().getString("BUTTON_JETT"));
        jettison.setItemMeta(tnt);
        // reset
        ItemStack reset = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.setDisplayName(plugin.getLanguage().getString("BUTTON_RESET"));
        reset.setItemMeta(cobble);
        // load map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta load = map.getItemMeta();
        load.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP"));
        map.setItemMeta(load);

        ItemStack[] is = {
            null, pad_up, null, null, black, black, black, black, black,
            pad_left, map, pad_right, s, black, black, black, black, black,
            null, pad_down, null, null, black, black, black, black, black,
            level_bot, level_sel, level_top, reset, black, black, black, black, black,
            scroll_left, null, scroll_right, jettison, black, black, black, black, black,
            passage, arboretum, bedroom, kitchen, library, gene, pool, vault, workshop
        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
