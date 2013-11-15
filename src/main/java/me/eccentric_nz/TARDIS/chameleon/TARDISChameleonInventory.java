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
import org.bukkit.ChatColor;
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

    private final ItemStack[] terminal;
    private final boolean bool;
    private final boolean adapt;

    public TARDISChameleonInventory(boolean bool, boolean adapt) {
        this.bool = bool;
        this.adapt = adapt;
        this.terminal = getItemStack();
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
        String on_off = (bool) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF";
        String to_engage = (bool) ? "OFF" : "ON";
        ing.setLore(Arrays.asList(new String[]{on_off, "Click to turn " + to_engage}));
        con.setItemMeta(ing);
        // Apply preset
        ItemStack apply = new ItemStack(Material.BOOKSHELF, 1);
        ItemMeta now = apply.getItemMeta();
        now.setDisplayName("Apply preset now");
        apply.setItemMeta(now);
        // New Police Box
        ItemStack box = new ItemStack(Material.WOOL, 1, (short) 11);
        ItemMeta day = box.getItemMeta();
        day.setDisplayName("New Police Box");
        box.setItemMeta(day);
        // disengaged
        ItemStack off = new ItemStack(Material.WOOL, 1, (short) 8);
        ItemMeta ht = off.getItemMeta();
        ht.setDisplayName("Factory Fresh");
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
        // Telephone Box
        ItemStack tel = new ItemStack(Material.WOOL, 1, (short) 14);
        ItemMeta pho = tel.getItemMeta();
        pho.setDisplayName("Red Telephone Box");
        tel.setItemMeta(pho);
        // Partly Submerged
        ItemStack sub = new ItemStack(Material.DIRT, 1);
        ItemMeta mer = sub.getItemMeta();
        mer.setDisplayName("Submerged");
        sub.setItemMeta(mer);
        // Daisy Flower
        ItemStack flo = new ItemStack(Material.WOOL, 1, (short) 0);
        ItemMeta wer = flo.getItemMeta();
        wer.setDisplayName("Daisy Flower");
        flo.setItemMeta(wer);
        // Chalice
        ItemStack chal = new ItemStack(Material.QUARTZ_BLOCK, 1);
        ItemMeta ice = chal.getItemMeta();
        ice.setDisplayName("Quartz Chalice");
        chal.setItemMeta(ice);
        // Angel
        ItemStack ang = new ItemStack(Material.FEATHER, 1);
        ItemMeta wee = ang.getItemMeta();
        wee.setDisplayName("Weeping Angel");
        ang.setItemMeta(wee);
        // Windmill
        ItemStack win = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta mill = win.getItemMeta();
        mill.setDisplayName("Windmill");
        win.setItemMeta(mill);
        // Well
        ItemStack well = new ItemStack(Material.MOSSY_COBBLESTONE, 1);
        ItemMeta ivy = well.getItemMeta();
        ivy.setDisplayName("Mossy Well");
        well.setItemMeta(ivy);
        // cake
        ItemStack cake = new ItemStack(Material.WOOL, 1, (short) 12);
        ItemMeta candle = cake.getItemMeta();
        candle.setDisplayName("Birthday Cake");
        cake.setItemMeta(candle);
        // grave
        ItemStack grave = new ItemStack(Material.ENDER_STONE, 1);
        ItemMeta epitaph = grave.getItemMeta();
        epitaph.setDisplayName("Gravestone");
        grave.setItemMeta(epitaph);
        // topsy
        ItemStack topsy = new ItemStack(Material.WOOL, 1, (short) 6);
        ItemMeta turvey = topsy.getItemMeta();
        turvey.setDisplayName("Topsy-turvey");
        topsy.setItemMeta(turvey);
        // mushroom
        ItemStack mush = new ItemStack(Material.HUGE_MUSHROOM_1, 1);
        ItemMeta shroom = mush.getItemMeta();
        shroom.setDisplayName("Mushroom");
        mush.setItemMeta(shroom);
        // custom
        ItemStack custom = new ItemStack(Material.PISTON_STICKY_BASE, 1);
        ItemMeta pre = custom.getItemMeta();
        pre.setDisplayName("Custom");
        custom.setItemMeta(pre);
        // Biome
        ItemStack bio = new ItemStack(Material.LOG, 1, (short) 2);
        ItemMeta me = bio.getItemMeta();
        me.setDisplayName("Biome Adaption");
        String biome = (adapt) ? ChatColor.GREEN + "ON" : ChatColor.RED + "OFF";
        String to_turn = (adapt) ? "OFF" : "ON";
        me.setLore(Arrays.asList(new String[]{biome, "Click to turn " + to_turn}));
        bio.setItemMeta(me);
        // Cancel / close
        ItemStack close = new ItemStack(Material.WOOL, 1, (short) 15);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName("Close");
        close.setItemMeta(can);

        ItemStack[] is = {
            null, con, null, apply, null, bio, null, close, null,
            box, null, off, null, jungle, null, nether, null, def,
            null, swamp, null, tent, null, village, null, yellow, null,
            tel, null, ang, null, sub, null, flo, null, stone,
            null, chal, null, desert, null, well, null, win, null,
            cake, null, grave, null, topsy, null, mush, null, custom
        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
