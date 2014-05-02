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
package me.eccentric_nz.TARDIS.sonic;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Administrator of Solos is the Earth Empire's civilian overseer for that
 * planet.
 *
 * @author eccentric_nz
 */
public class TARDISSonicMenuInventory {

    private final TARDIS plugin;
    private final ItemStack[] menu;

    public TARDISSonicMenuInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        // italic \u00a7o
        // tennant
        ItemStack markone = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markone_im = markone.getItemMeta();
        markone_im.setDisplayName(ChatColor.AQUA + "Sonic Screwdriver");
        markone_im.setLore(Arrays.asList("Mark I"));
        markone.setItemMeta(markone_im);
        // tennant
        ItemStack marktwo = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta marktwo_im = marktwo.getItemMeta();
        marktwo_im.setDisplayName(ChatColor.YELLOW + "Sonic Screwdriver");
        marktwo_im.setLore(Arrays.asList("Mark II"));
        marktwo.setItemMeta(marktwo_im);
        // tennant
        ItemStack markthree = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markthree_im = markthree.getItemMeta();
        markthree_im.setDisplayName(ChatColor.DARK_PURPLE + "Sonic Screwdriver");
        markthree_im.setLore(Arrays.asList("Mark III"));
        markthree.setItemMeta(markthree_im);
        // tennant
        ItemStack markfour = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta markfour_im = markfour.getItemMeta();
        markfour_im.setDisplayName(ChatColor.GRAY + "Sonic Screwdriver");
        markfour_im.setLore(Arrays.asList("Mark IV"));
        markfour.setItemMeta(markfour_im);
        // tennant
        ItemStack mcgann = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta mcgann_im = mcgann.getItemMeta();
        mcgann_im.setDisplayName(ChatColor.BLUE + "Sonic Screwdriver");
        mcgann_im.setLore(Arrays.asList("McGann"));
        mcgann.setItemMeta(mcgann_im);
        // tennant
        ItemStack tennant = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta tennant_im = tennant.getItemMeta();
        tennant_im.setDisplayName(ChatColor.AQUA + "Sonic Screwdriver");
        tennant_im.setLore(Arrays.asList("Tennant"));
        tennant.setItemMeta(tennant_im);
        // smith
        ItemStack smith = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta smith_im = smith.getItemMeta();
        smith_im.setDisplayName("Sonic Screwdriver");
        smith_im.setLore(Arrays.asList("Smith"));
        smith.setItemMeta(smith_im);
        // song
        ItemStack song = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta song_im = song.getItemMeta();
        song_im.setDisplayName(ChatColor.GOLD + "Sonic Screwdriver");
        song_im.setLore(Arrays.asList("Song"));
        song.setItemMeta(song_im);
        // hurt
        ItemStack hurt = new ItemStack(Material.BLAZE_ROD, 1);
        ItemMeta hurt_im = hurt.getItemMeta();
        hurt_im.setDisplayName(ChatColor.DARK_RED + "Sonic Screwdriver");
        hurt_im.setLore(Arrays.asList("Hurt"));
        hurt.setItemMeta(hurt_im);
        // info
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions");
        List<String> lore = Arrays.asList(new String[]{"Put your Sonic Screwdriver", "in the left most slot", "and then click on the", "Sonic of your choice."});
        info_im.setLore(lore);
        info.setItemMeta(info_im);
        // close
        ItemStack close = new ItemStack(Material.TRAP_DOOR, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);

        ItemStack[] stack = {markone, marktwo, markthree, markfour, mcgann, tennant, smith, song, hurt,
            null, null, null, null, info, null, null, null, close};
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
