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
package me.eccentric_nz.TARDIS.chameleon;

import java.util.Arrays;
import java.util.List;
import me.eccentric_nz.TARDIS.TARDIS;
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
public class TARDISPageThreeInventory {

    private final ItemStack[] pageThree;
    private final TARDIS plugin;
    private final boolean bool;
    private final boolean adapt;

    public TARDISPageThreeInventory(TARDIS plugin, boolean bool, boolean adapt) {
        this.plugin = plugin;
        this.bool = bool;
        this.adapt = adapt;
        this.pageThree = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // on / off
        ItemStack con = new ItemStack(Material.REDSTONE_COMPARATOR, 1);
        ItemMeta ing = con.getItemMeta();
        ing.setDisplayName(plugin.getLanguage().getString("BUTTON_CIRC"));
        String on_off = (bool) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        String to_engage = (bool) ? plugin.getLanguage().getString("SET_OFF") : plugin.getLanguage().getString("SET_ON");
        ing.setLore(Arrays.asList(on_off, String.format(plugin.getLanguage().getString("CHAM_CLICK"), to_engage)));
        con.setItemMeta(ing);
        // Apply preset
        ItemStack apply = new ItemStack(Material.BOOKSHELF, 1);
        ItemMeta now = apply.getItemMeta();
        now.setDisplayName(plugin.getLanguage().getString("BUTTON_APPLY"));
        apply.setItemMeta(now);
        // page one
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta one = page.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        page.setItemMeta(one);
        // Gazebo
        ItemStack gaz = new ItemStack(Material.FENCE, 1);
        ItemMeta ebo = gaz.getItemMeta();
        ebo.setDisplayName("Gazebo");
        gaz.setItemMeta(ebo);
        // Apperture Science
        ItemStack app = new ItemStack(Material.IRON_TRAPDOOR, 1);
        ItemMeta sci = app.getItemMeta();
        sci.setDisplayName("Apperture Science");
        app.setItemMeta(sci);
        // Lighthouse
        ItemStack lig = new ItemStack(Material.REDSTONE_LAMP_OFF, 1);
        ItemMeta hou = lig.getItemMeta();
        hou.setDisplayName("Tiny Lighthouse");
        lig.setItemMeta(hou);
        // Library
        ItemStack lib = new ItemStack(Material.BOOK, 1);
        ItemMeta rar = lib.getItemMeta();
        rar.setDisplayName("Library");
        lib.setItemMeta(rar);
        // Snowman
        ItemStack sno = new ItemStack(Material.SNOW_BLOCK, 1);
        ItemMeta man = sno.getItemMeta();
        man.setDisplayName("Snowman");
        sno.setItemMeta(man);
        // Jail
        ItemStack jail = new ItemStack(Material.IRON_FENCE, 1);
        ItemMeta gaol = jail.getItemMeta();
        gaol.setDisplayName("Jail Cell");
        jail.setItemMeta(gaol);
        // Pandorica
        ItemStack pan = new ItemStack(Material.BEDROCK, 1);
        ItemMeta dor = pan.getItemMeta();
        dor.setDisplayName("Pandorica");
        pan.setItemMeta(dor);
        // Double Helix
        ItemStack dou = new ItemStack(Material.SMOOTH_STAIRS, 1);
        ItemMeta lix = dou.getItemMeta();
        lix.setDisplayName("Double Helix");
        dou.setItemMeta(lix);
        // Prismarine
        ItemStack pris = new ItemStack(Material.PRISMARINE, 1);
        ItemMeta mar = pris.getItemMeta();
        mar.setDisplayName("Guardian Temple");
        pris.setItemMeta(mar);
        // Andesite
        ItemStack and = new ItemStack(Material.STONE, 1, (byte) 6);
        ItemMeta esi = and.getItemMeta();
        esi.setDisplayName("Andesite Box");
        and.setItemMeta(esi);
        // Diorite
        ItemStack dio = new ItemStack(Material.STONE, 1, (byte) 4);
        ItemMeta rit = dio.getItemMeta();
        rit.setDisplayName("Diorite Box");
        dio.setItemMeta(rit);
        // Granite
        ItemStack gra = new ItemStack(Material.STONE, 1, (byte) 2);
        ItemMeta nit = gra.getItemMeta();
        nit.setDisplayName("Granite Box");
        gra.setItemMeta(nit);
        // Invivibility
        ItemStack inv;
        if (plugin.getConfig().getBoolean("allow.invisibility")) {
            inv = new ItemStack(Material.GLASS, 1);
            ItemMeta isi = inv.getItemMeta();
            isi.setDisplayName("Invisibility");
            if (plugin.getConfig().getBoolean("circuits.damage") && plugin.getConfig().getInt("circuits.uses.invisibility") > 0) {
                List<String> warn = Arrays.asList(plugin.getLanguage().getString("INVISIBILITY_LORE_1"), plugin.getLanguage().getString("INVISIBILITY_LORE_2"));
                isi.setLore(warn);
            }
            inv.setItemMeta(isi);
        } else {
            inv = null;
        }
        // construction GUI
        ItemStack construct = new ItemStack(Material.BOWL, 1);
        ItemMeta ct = construct.getItemMeta();
        ct.setDisplayName("Chameleon construction");
        construct.setItemMeta(ct);
        // Biome
        ItemStack bio = new ItemStack(Material.LOG, 1, (short) 2);
        ItemMeta me = bio.getItemMeta();
        me.setDisplayName(plugin.getLanguage().getString("BUTTON_ADAPT"));
        String biome = (adapt) ? ChatColor.GREEN + plugin.getLanguage().getString("SET_ON") : ChatColor.RED + plugin.getLanguage().getString("SET_OFF");
        String to_turn = (adapt) ? plugin.getLanguage().getString("SET_OFF") : plugin.getLanguage().getString("SET_ON");
        me.setLore(Arrays.asList(biome, String.format(plugin.getLanguage().getString("CHAM_CLICK"), to_turn)));
        bio.setItemMeta(me);
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);

        ItemStack[] is = {
            con, null, apply, null, bio, null, close, null, page,
            null, null, null, null, null, null, null, null, null,
            gaz, null, app, null, lig, null, lib, null, sno,
            null, jail, null, pan, null, dou, null, pris, null,
            null, null, and, null, dio, null, gra, null, null,
            null, null, null, inv, null, construct, null, null, null
        };
        return is;
    }

    public ItemStack[] getPageThree() {
        return pageThree;
    }
}
