/*
 * Copyright (C) 2018 eccentric_nz
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
package me.eccentric_nz.TARDIS.sonic;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill, doesn't wound, doesn't maim. But I'll tell
 * you what it does do. It is very good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISSonicMenuInventory {

    private final ItemStack[] menu;

    public TARDISSonicMenuInventory() {
        menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Sonic Screwdriver Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */

    private ItemStack[] getItemStack() {
        // \u00a7 = § (ChatColor code)
        // mark I
        ItemStack markone = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markone_im = markone.getItemMeta();
        markone_im.setDisplayName(ChatColor.DARK_GRAY + "Sonic Screwdriver");
        markone_im.setLore(Collections.singletonList("Mark I"));
        markone.setItemMeta(markone_im);
        // mark II
        ItemStack marktwo = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta marktwo_im = marktwo.getItemMeta();
        marktwo_im.setDisplayName(ChatColor.YELLOW + "Sonic Screwdriver");
        marktwo_im.setLore(Collections.singletonList("Mark II"));
        marktwo.setItemMeta(marktwo_im);
        // mark III
        ItemStack markthree = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markthree_im = markthree.getItemMeta();
        markthree_im.setDisplayName(ChatColor.DARK_PURPLE + "Sonic Screwdriver");
        markthree_im.setLore(Collections.singletonList("Mark III"));
        markthree.setItemMeta(markthree_im);
        // mark IV
        ItemStack markfour = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markfour_im = markfour.getItemMeta();
        markfour_im.setDisplayName(ChatColor.GRAY + "Sonic Screwdriver");
        markfour_im.setLore(Collections.singletonList("Mark IV"));
        markfour.setItemMeta(markfour_im);
        // mcgann
        ItemStack mcgann = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta mcgann_im = mcgann.getItemMeta();
        mcgann_im.setDisplayName(ChatColor.BLUE + "Sonic Screwdriver");
        mcgann_im.setLore(Collections.singletonList("Eighth Doctor"));
        mcgann.setItemMeta(mcgann_im);
        // eccelston
        ItemStack eccelston = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta eccelston_im = eccelston.getItemMeta();
        eccelston_im.setDisplayName(ChatColor.GREEN + "Sonic Screwdriver");
        eccelston_im.setLore(Collections.singletonList("Ninth Doctor"));
        eccelston.setItemMeta(eccelston_im);
        // eccelston open
        ItemStack eccelston_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta eccelston_open_im = eccelston_open.getItemMeta();
        eccelston_open_im.setDisplayName(ChatColor.DARK_GREEN + "Sonic Screwdriver");
        eccelston_open_im.setLore(Collections.singletonList("Ninth Doctor Open"));
        eccelston_open.setItemMeta(eccelston_open_im);
        // tennant
        ItemStack tennant = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tennant_im = tennant.getItemMeta();
        tennant_im.setDisplayName(ChatColor.AQUA + "Sonic Screwdriver");
        tennant_im.setLore(Collections.singletonList("Tenth Doctor"));
        tennant.setItemMeta(tennant_im);
        // tennant open
        ItemStack tennant_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tennant_open_im = tennant_open.getItemMeta();
        tennant_open_im.setDisplayName(ChatColor.DARK_AQUA + "Sonic Screwdriver");
        tennant_open_im.setLore(Collections.singletonList("Tenth Doctor Open"));
        tennant_open.setItemMeta(tennant_open_im);
        // smith
        ItemStack smith = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta smith_im = smith.getItemMeta();
        smith_im.setDisplayName("Sonic Screwdriver");
        smith_im.setLore(Collections.singletonList("Eleventh Doctor"));
        smith.setItemMeta(smith_im);
        // smith open
        ItemStack smith_open = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta smith_open_im = smith_open.getItemMeta();
        smith_open_im.setDisplayName(ChatColor.LIGHT_PURPLE + "Sonic Screwdriver");
        smith_open_im.setLore(Collections.singletonList("Eleventh Doctor Open"));
        smith_open.setItemMeta(smith_open_im);
        // hurt
        ItemStack hurt = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta hurt_im = hurt.getItemMeta();
        hurt_im.setDisplayName(ChatColor.DARK_RED + "Sonic Screwdriver");
        hurt_im.setLore(Collections.singletonList("War Doctor"));
        hurt.setItemMeta(hurt_im);
        // master
        ItemStack master = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta master_im = master.getItemMeta();
        master_im.setDisplayName(ChatColor.DARK_BLUE + "Sonic Screwdriver");
        master_im.setLore(Collections.singletonList("Master"));
        master.setItemMeta(master_im);
        // sarah jane
        ItemStack sarahjane = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta sarahjane_im = sarahjane.getItemMeta();
        sarahjane_im.setDisplayName(ChatColor.RED + "Sonic Screwdriver");
        sarahjane_im.setLore(Collections.singletonList("Sarah Jane"));
        sarahjane.setItemMeta(sarahjane_im);
        // river song
        ItemStack song = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta song_im = song.getItemMeta();
        song_im.setDisplayName(ChatColor.GOLD + "Sonic Screwdriver");
        song_im.setLore(Collections.singletonList("River Song"));
        song.setItemMeta(song_im);
        // twelfth doctor (peter capaldi)
        ItemStack peter = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta capaldi = peter.getItemMeta();
        capaldi.setDisplayName(ChatColor.UNDERLINE + "Sonic Screwdriver");
        capaldi.setLore(Collections.singletonList("Twelfth Doctor"));
        peter.setItemMeta(capaldi);
        // thirteenth doctor (jodie whittaker)
        ItemStack whittaker = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta jodie = whittaker.getItemMeta();
        jodie.setDisplayName(ChatColor.BLACK + "Sonic Screwdriver");
        jodie.setLore(Collections.singletonList("Thirteenth Doctor"));
        whittaker.setItemMeta(jodie);
        // info
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions");
        List<String> lore = Arrays.asList("Put your Sonic Screwdriver", "in the bottom left most slot", "and then click on the", "Sonic of your choice.");
        info_im.setLore(lore);
        info.setItemMeta(info_im);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);

        return new ItemStack[]{
                markone, marktwo, markthree, markfour, mcgann, eccelston, tennant, smith, hurt,
                whittaker, master, sarahjane, song, null, eccelston_open, tennant_open, smith_open, peter,
                null, null, null, null, info, null, null, null, close
        };
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
