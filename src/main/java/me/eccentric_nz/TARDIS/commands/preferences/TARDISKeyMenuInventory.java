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
package me.eccentric_nz.TARDIS.commands.preferences;

import java.util.Arrays;
import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Oh, yes. Harmless is just the word. That's why I like it! Doesn't kill,
 * doesn't wound, doesn't maim. But I'll tell you what it does do. It is very
 * good at opening doors!
 *
 * @author eccentric_nz
 */
public class TARDISKeyMenuInventory {

    private final ItemStack[] menu;

    public TARDISKeyMenuInventory() {
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        // \u00a7 = § (ChatColor code)
        // brass yale
        ItemStack brass = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta yale = brass.getItemMeta();
        yale.setDisplayName(ChatColor.AQUA + "TARDIS Key");
        yale.setLore(Arrays.asList("Brass Yale", "First & Sixth Doctors"));
        brass.setItemMeta(yale);
        // brass plain
        ItemStack plain = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta second = plain.getItemMeta();
        second.setDisplayName(ChatColor.DARK_BLUE + "TARDIS Key");
        second.setLore(Arrays.asList("Brass Plain", "Second Doctor"));
        plain.setItemMeta(second);
        // spade
        ItemStack spade = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta third = spade.getItemMeta();
        third.setDisplayName(ChatColor.LIGHT_PURPLE + "TARDIS Key");
        third.setLore(Arrays.asList("Spade-shaped", "Third, Fourth & Eighth Doctors"));
        spade.setItemMeta(third);
        // silver yale
        ItemStack silver = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta fifth = silver.getItemMeta();
        fifth.setDisplayName(ChatColor.DARK_RED + "TARDIS Key");
        fifth.setLore(Arrays.asList("Silver Yale", "Fifth Doctor"));
        silver.setItemMeta(fifth);
        // seal of rassilon
        ItemStack seal = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta seventh = seal.getItemMeta();
        seventh.setDisplayName(ChatColor.GRAY + "TARDIS Key");
        seventh.setLore(Arrays.asList("Seal of Rassilon", "Seventh Doctor"));
        seal.setItemMeta(seventh);
        // silver variant
        ItemStack variant = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta ninth = variant.getItemMeta();
        ninth.setDisplayName(ChatColor.DARK_PURPLE + "TARDIS Key");
        ninth.setLore(Arrays.asList("Silver Variant", "Ninth Doctor"));
        variant.setItemMeta(ninth);
        // silver plain
        ItemStack s_plain = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta tenth = s_plain.getItemMeta();
        tenth.setDisplayName(ChatColor.GREEN + "TARDIS Key");
        tenth.setLore(Arrays.asList("Silver Plain", "Tenth Doctor, Martha Jones & Donna Noble"));
        s_plain.setItemMeta(tenth);
        // eleventh, clara
        ItemStack clara = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta oswald = clara.getItemMeta();
        oswald.setDisplayName("TARDIS Key");
        oswald.setLore(Arrays.asList("Silver New", "Eleventh Doctor & Clara Oswald"));
        clara.setItemMeta(oswald);
        // silver era
        ItemStack era = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta rose = era.getItemMeta();
        rose.setDisplayName(ChatColor.RED + "TARDIS Key");
        rose.setLore(Arrays.asList("Silver ERA", "Rose Tyler"));
        era.setItemMeta(rose);
        // sally sparrow
        ItemStack sally = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta sparrow = sally.getItemMeta();
        sparrow.setDisplayName(ChatColor.DARK_AQUA + "TARDIS Key");
        sparrow.setLore(Arrays.asList("Silver String", "Sally Sparrow"));
        sally.setItemMeta(sparrow);
        // perception filter
        ItemStack perception = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta filter = perception.getItemMeta();
        filter.setDisplayName(ChatColor.BLUE + "TARDIS Key");
        filter.setLore(Arrays.asList("Perception Filter", "Tenth Doctor, Martha Jones & Jack Harkness"));
        perception.setItemMeta(filter);
        // susan foreman
        ItemStack susan = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta foreman = susan.getItemMeta();
        foreman.setDisplayName(ChatColor.YELLOW + "TARDIS Key");
        foreman.setLore(Arrays.asList("Brass String", "Susan Foreman"));
        susan.setItemMeta(foreman);
        // susan foreman
        ItemStack bromley = new ItemStack(Material.GOLD_NUGGET, 1);
        ItemMeta gold = bromley.getItemMeta();
        gold.setDisplayName(ChatColor.GOLD + "TARDIS Key");
        gold.setLore(Arrays.asList("Bromley Gold", "eccentric_nz"));
        bromley.setItemMeta(gold);
        // info
        ItemStack info = new ItemStack(Material.BOOK, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Instructions");
        List<String> lore = Arrays.asList(new String[]{"Put your TARDIS Key", "in the bottom left most slot", "and then click on the", "key of your choice."});
        info_im.setLore(lore);
        info.setItemMeta(info_im);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Close");
        close.setItemMeta(close_im);

        ItemStack[] stack = {brass, plain, spade, silver, seal, variant, s_plain, clara, perception,
            null, susan, null, era, null, sally, null, bromley, null,
            null, null, null, null, info, null, null, null, close};
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
