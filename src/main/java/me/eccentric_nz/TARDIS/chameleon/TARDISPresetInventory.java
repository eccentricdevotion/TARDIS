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
import org.bukkit.enchantments.Enchantment;
import org.bukkit.enchantments.EnchantmentWrapper;
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
    private final boolean bool;
    private final boolean adapt;

    public TARDISPresetInventory(boolean bool, boolean adapt) {
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
        // page one
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta one = page.getItemMeta();
        one.setDisplayName("Back to page 1");
        Enchantment e = EnchantmentWrapper.ARROW_FIRE;
        one.addEnchant(e, 1, true);
        page.setItemMeta(one);
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
        ItemStack pine = new ItemStack(Material.LEAVES, 1, (short) 5);
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
        // Apperture Science
        ItemStack app = new ItemStack(Material.WOOL, 1, (short) 1);
        ItemMeta sci = app.getItemMeta();
        sci.setDisplayName("Apperture Science");
        app.setItemMeta(sci);
        // Lighthouse
        ItemStack lig = new ItemStack(Material.REDSTONE_LAMP_ON, 1);
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
        // Random Fence
        ItemStack fen = new ItemStack(Material.BRICK, 1, (short) 6);
        ItemMeta cer = fen.getItemMeta();
        cer.setDisplayName("Random Fence");
        fen.setItemMeta(cer);
        // Gazebo
        ItemStack gaz = new ItemStack(Material.FENCE, 1);
        ItemMeta ebo = gaz.getItemMeta();
        ebo.setDisplayName("Gazebo");
        gaz.setItemMeta(ebo);
        // custom
        ItemStack custom = new ItemStack(Material.ENDER_CHEST, 1);
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
            custom, null, rub, null, mine, null, cre, null, pea,
            null, lamp, null, candy, null, toi, null, rob, null,
            tor, null, pine, null, pun, null, fen, null, por,
            null, gaz, null, app, null, lig, null, lib, null,
            sno, null, jail, null, pan, null, dou, null, page
        };
        return is;
    }

    public ItemStack[] getTerminal() {
        return terminal;
    }
}
