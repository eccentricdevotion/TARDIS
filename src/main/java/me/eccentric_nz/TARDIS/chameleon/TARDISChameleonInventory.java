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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of
 * travelling through time, even in a non-linear direction. In the 26th century
 * individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
public class TARDISChameleonInventory {

    private ItemStack[] terminal;
    private boolean bool;

    public TARDISChameleonInventory(boolean bool) {
        this.terminal = getItemStack();
        this.bool = bool;
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // on/off
        ItemStack con = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta ing = con.getItemMeta();
        ing.setDisplayName("Circuit");
        String on_off = (bool) ? "ON" : "OFF";
        ing.setLore(Arrays.asList(new String[]{on_off}));
        con.setItemMeta(ing);
        // New Police Box
        ItemStack box = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta day = box.getItemMeta();
        day.setDisplayName("New Police Box");
        box.setItemMeta(day);
        // disengaged
        ItemStack off = new ItemStack(Material.WOOL, 1, (short) 8);
        ItemMeta ht = off.getItemMeta();
        ht.setDisplayName("Disable Circuit");
        off.setItemMeta(ht);
        // Stone Brick Column
        ItemStack stone = new ItemStack(Material.SMOOTH_BRICK, 1);
        ItemMeta hrs = stone.getItemMeta();
        hrs.setDisplayName("Stone Brick Column");
        stone.setItemMeta(hrs);
        // Desert Temple
        ItemStack desert = new ItemStack(Material.SANDSTONE, 1);
        ItemMeta tmp = desert.getItemMeta();
        tmp.setDisplayName("Desert Temple");
        desert.setItemMeta(tmp);
        // Jungle Temple
        ItemStack jungle = new ItemStack(Material.MOSSY_COBBLESTONE, 1);
        ItemMeta tpl = jungle.getItemMeta();
        tpl.setDisplayName("Jungle Temple");
        jungle.setItemMeta(tpl);
        // Nether Fortress
        ItemStack nether = new ItemStack(Material.NETHER_BRICK, 1);
        ItemMeta frt = nether.getItemMeta();
        frt.setDisplayName("Nether Fortress");
        nether.setItemMeta(frt);
        // Old Police Box
        ItemStack def = new ItemStack(Material.WOOL, 1, (short) 3);
        ItemMeta dpb = def.getItemMeta();
        dpb.setDisplayName("Old Police Box");
        def.setItemMeta(dpb);
        // Swamp Hut
        ItemStack swamp = new ItemStack(Material.LOG, 1);
        ItemMeta hut = swamp.getItemMeta();
        hut.setDisplayName("Swamp Hut");
        swamp.setItemMeta(hut);
        // Party Tent
        ItemStack tent = new ItemStack(Material.WOOL, 1, (short) 5);
        ItemMeta pry = tent.getItemMeta();
        pry.setDisplayName("Party Tent");
        tent.setItemMeta(pry);
        // Village House
        ItemStack village = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta hse = village.getItemMeta();
        hse.setDisplayName("Village House");
        village.setItemMeta(hse);
        // Yellow Submarine
        ItemStack yellow = new ItemStack(Material.WOOL, 1, (short) 4);
        ItemMeta sme = yellow.getItemMeta();
        sme.setDisplayName("Yellow Submarine");
        yellow.setItemMeta(sme);

        ItemStack[] is = {
            null, null, con, null, box, null, off, null, null,
            stone, null, desert, null, jungle, null, nether, null, def,
            null, swamp, null, tent, null, village, null, yellow, null
        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
