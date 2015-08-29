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
public class TARDISPresetInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;
    private final boolean bool;
    private final boolean adapt;

    public TARDISPresetInventory(TARDIS plugin, boolean bool, boolean adapt) {
        this.plugin = plugin;
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
        ItemMeta two = page.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_3"));
        page.setItemMeta(two);
        // Rubber duck
        ItemStack rub = new ItemStack(Material.STAINED_CLAY, 1, (short) 4);
        ItemMeta ber = rub.getItemMeta();
        ber.setDisplayName("Rubber Ducky");
        rub.setItemMeta(ber);
        // Mineshaft
        ItemStack mine = new ItemStack(Material.RAILS, 1);
        ItemMeta sht = mine.getItemMeta();
        sht.setDisplayName("Mineshaft");
        mine.setItemMeta(sht);
        // Creepy
        ItemStack cre = new ItemStack(Material.WEB, 1);
        ItemMeta epy = cre.getItemMeta();
        epy.setDisplayName("Creepy");
        cre.setItemMeta(epy);
        // Peanut Butter
        ItemStack pea = new ItemStack(Material.HARD_CLAY, 1);
        ItemMeta nut = pea.getItemMeta();
        nut.setDisplayName("Peanut Butter Jar");
        pea.setItemMeta(nut);
        // Lamp Post
        ItemStack lamp = new ItemStack(Material.GLOWSTONE, 1);
        ItemMeta post = lamp.getItemMeta();
        post.setDisplayName("Lamp Post");
        lamp.setItemMeta(post);
        // Candy Cane
        ItemStack candy = new ItemStack(Material.STAINED_CLAY, 1, (short) 14);
        ItemMeta cane = candy.getItemMeta();
        cane.setDisplayName("Candy Cane");
        candy.setItemMeta(cane);
        // Toilet
        ItemStack toi = new ItemStack(Material.HOPPER, 1);
        ItemMeta let = toi.getItemMeta();
        let.setDisplayName("Water Closet");
        toi.setItemMeta(let);
        // Robot
        ItemStack rob = new ItemStack(Material.IRON_BLOCK, 1);
        ItemMeta oto = rob.getItemMeta();
        oto.setDisplayName("Robot");
        rob.setItemMeta(oto);
        // Torch
        ItemStack tor = new ItemStack(Material.NETHERRACK, 1);
        ItemMeta ch = tor.getItemMeta();
        ch.setDisplayName("Flaming Torch");
        tor.setItemMeta(ch);
        // Pine Tree
        ItemStack pine = new ItemStack(Material.LEAVES, 1, (short) 1);
        ItemMeta tree = pine.getItemMeta();
        tree.setDisplayName("Pine Tree");
        pine.setItemMeta(tree);
        // Punked
        ItemStack pun = new ItemStack(Material.COAL_BLOCK, 1);
        ItemMeta ked = pun.getItemMeta();
        ked.setDisplayName("Steam Punked");
        pun.setItemMeta(ked);
        // Nether Portal
        ItemStack por = new ItemStack(Material.QUARTZ_ORE, 1);
        ItemMeta tal = por.getItemMeta();
        tal.setDisplayName("Nether Portal");
        por.setItemMeta(tal);
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
        // Random Fence
        ItemStack fen = new ItemStack(Material.BRICK, 1, (short) 6);
        ItemMeta cer = fen.getItemMeta();
        cer.setDisplayName("Random Fence");
        // custom
        ItemStack custom = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta pre = custom.getItemMeta();
        pre.setDisplayName("Custom");
        custom.setItemMeta(pre);
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
            custom, null, cake, null, grave, null, topsy, null, mush,
            null, rub, null, mine, null, cre, null, pea, null,
            lamp, null, candy, null, toi, null, rob, null, tor,
            null, pine, null, pun, null, fen, null, por, null

        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
