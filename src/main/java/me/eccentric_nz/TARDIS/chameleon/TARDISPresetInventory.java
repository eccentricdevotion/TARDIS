/*
 * Copyright (C) 2019 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
class TARDISPresetInventory {

    private final ItemStack[] terminal;
    private final TARDIS plugin;

    public TARDISPresetInventory(TARDIS plugin) {
        this.plugin = plugin;
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // page two
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta two = page.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        page.setItemMeta(two);
        // New Police Box
        ItemStack box = new ItemStack(Material.BLUE_WOOL, 1);
        ItemMeta day = box.getItemMeta();
        day.setDisplayName("New Police Box");
        box.setItemMeta(day);
        // Stone Brick Column
        ItemStack stone = new ItemStack(Material.STONE_BRICKS, 1);
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
        ItemStack nether = new ItemStack(Material.NETHER_BRICKS, 1);
        ItemMeta frt = nether.getItemMeta();
        frt.setDisplayName("Nether Fortress");
        nether.setItemMeta(frt);
        // Old Police Box
        ItemStack def = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta dpb = def.getItemMeta();
        dpb.setDisplayName("Old Police Box");
        def.setItemMeta(dpb);
        // Swamp Hut
        ItemStack swamp = new ItemStack(Material.OAK_LOG, 1);
        ItemMeta hut = swamp.getItemMeta();
        hut.setDisplayName("Swamp Hut");
        swamp.setItemMeta(hut);
        // Party Tent
        ItemStack tent = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta pry = tent.getItemMeta();
        pry.setDisplayName("Party Tent");
        tent.setItemMeta(pry);
        // Village House
        ItemStack village = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta hse = village.getItemMeta();
        hse.setDisplayName("Village House");
        village.setItemMeta(hse);
        // Yellow Submarine
        ItemStack yellow = new ItemStack(Material.YELLOW_WOOL, 1);
        ItemMeta sme = yellow.getItemMeta();
        sme.setDisplayName("Yellow Submarine");
        yellow.setItemMeta(sme);
        // Telephone Box
        ItemStack tel = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta pho = tel.getItemMeta();
        pho.setDisplayName("Red Telephone Box");
        tel.setItemMeta(pho);
        // Partly Submerged
        ItemStack sub = new ItemStack(Material.DIRT, 1);
        ItemMeta mer = sub.getItemMeta();
        mer.setDisplayName("Submerged");
        sub.setItemMeta(mer);
        // Daisy Flower
        ItemStack flo = new ItemStack(Material.WHITE_WOOL, 1);
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
        ItemStack win = new ItemStack(Material.ORANGE_WOOL, 1);
        ItemMeta mill = win.getItemMeta();
        mill.setDisplayName("Windmill");
        win.setItemMeta(mill);
        // Well
        ItemStack well = new ItemStack(Material.MOSSY_COBBLESTONE, 1);
        ItemMeta ivy = well.getItemMeta();
        ivy.setDisplayName("Mossy Well");
        well.setItemMeta(ivy);
        // Rubber duck
        ItemStack rub = new ItemStack(Material.YELLOW_TERRACOTTA, 1);
        ItemMeta ber = rub.getItemMeta();
        ber.setDisplayName("Rubber Ducky");
        rub.setItemMeta(ber);
        // Mineshaft
        ItemStack mine = new ItemStack(Material.RAIL, 1);
        ItemMeta sht = mine.getItemMeta();
        sht.setDisplayName("Mineshaft");
        mine.setItemMeta(sht);
        // Creepy
        ItemStack cre = new ItemStack(Material.COBWEB, 1);
        ItemMeta epy = cre.getItemMeta();
        epy.setDisplayName("Creepy");
        cre.setItemMeta(epy);
        // Peanut Butter
        ItemStack pea = new ItemStack(Material.TERRACOTTA, 1);
        ItemMeta nut = pea.getItemMeta();
        nut.setDisplayName("Peanut Butter Jar");
        pea.setItemMeta(nut);
        // Lamp Post
        ItemStack lamp = new ItemStack(Material.GLOWSTONE, 1);
        ItemMeta post = lamp.getItemMeta();
        post.setDisplayName("Lamp Post");
        lamp.setItemMeta(post);
        // Candy Cane
        ItemStack candy = new ItemStack(Material.RED_TERRACOTTA, 1);
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
        ItemStack pine = new ItemStack(Material.SPRUCE_LEAVES, 1);
        ItemMeta tree = pine.getItemMeta();
        tree.setDisplayName("Pine Tree");
        pine.setItemMeta(tree);
        // Punked
        ItemStack pun = new ItemStack(Material.COAL_BLOCK, 1);
        ItemMeta ked = pun.getItemMeta();
        ked.setDisplayName("Steam Punked");
        pun.setItemMeta(ked);
        // Nether Portal
        ItemStack por = new ItemStack(Material.NETHER_QUARTZ_ORE, 1);
        ItemMeta tal = por.getItemMeta();
        tal.setDisplayName("Nether Portal");
        por.setItemMeta(tal);
        // cake
        ItemStack cake = new ItemStack(Material.BROWN_WOOL, 1);
        ItemMeta candle = cake.getItemMeta();
        candle.setDisplayName("Birthday Cake");
        cake.setItemMeta(candle);
        // grave
        ItemStack grave = new ItemStack(Material.END_STONE, 1);
        ItemMeta epitaph = grave.getItemMeta();
        epitaph.setDisplayName("Gravestone");
        grave.setItemMeta(epitaph);
        // topsy
        ItemStack topsy = new ItemStack(Material.PINK_WOOL, 1);
        ItemMeta turvey = topsy.getItemMeta();
        turvey.setDisplayName("Topsy-turvey");
        topsy.setItemMeta(turvey);
        // mushroom
        ItemStack mush = new ItemStack(Material.BROWN_MUSHROOM_BLOCK, 1);
        ItemMeta shroom = mush.getItemMeta();
        shroom.setDisplayName("Mushroom");
        mush.setItemMeta(shroom);
        // Random Fence
        ItemStack fen = new ItemStack(Material.BRICKS, 1);
        ItemMeta cer = fen.getItemMeta();
        cer.setDisplayName("Random Fence");
        // Gazebo
        ItemStack gaz = new ItemStack(Material.OAK_FENCE, 1);
        ItemMeta ebo = gaz.getItemMeta();
        ebo.setDisplayName("Gazebo");
        gaz.setItemMeta(ebo);
        // Apperture Science
        ItemStack app = new ItemStack(Material.IRON_TRAPDOOR, 1);
        ItemMeta sci = app.getItemMeta();
        sci.setDisplayName("Apperture Science");
        app.setItemMeta(sci);
        // Lighthouse
        ItemStack lig = new ItemStack(Material.REDSTONE_LAMP, 1);
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
        ItemStack jail = new ItemStack(Material.IRON_BARS, 1);
        ItemMeta gaol = jail.getItemMeta();
        gaol.setDisplayName("Jail Cell");
        jail.setItemMeta(gaol);
        // Pandorica
        ItemStack pan = new ItemStack(Material.BEDROCK, 1);
        ItemMeta dor = pan.getItemMeta();
        dor.setDisplayName("Pandorica");
        pan.setItemMeta(dor);
        // Double Helix
        ItemStack dou = new ItemStack(Material.STONE_BRICK_STAIRS, 1);
        ItemMeta lix = dou.getItemMeta();
        lix.setDisplayName("Double Helix");
        dou.setItemMeta(lix);
        // Prismarine
        ItemStack pris = new ItemStack(Material.PRISMARINE, 1);
        ItemMeta mar = pris.getItemMeta();
        mar.setDisplayName("Guardian Temple");
        pris.setItemMeta(mar);
        // Chorus
        ItemStack cho = new ItemStack(Material.CHORUS_FLOWER, 1);
        ItemMeta rus = cho.getItemMeta();
        rus.setDisplayName("Chorus Flower");
        cho.setItemMeta(rus);
        // Andesite
        ItemStack and = new ItemStack(Material.ANDESITE, 1);
        ItemMeta esi = and.getItemMeta();
        esi.setDisplayName("Andesite Box");
        and.setItemMeta(esi);
        // Diorite
        ItemStack dio = new ItemStack(Material.DIORITE, 1);
        ItemMeta rit = dio.getItemMeta();
        rit.setDisplayName("Diorite Box");
        dio.setItemMeta(rit);
        // Granite
        ItemStack gra = new ItemStack(Material.GRANITE, 1);
        ItemMeta nit = gra.getItemMeta();
        nit.setDisplayName("Granite Box");
        gra.setItemMeta(nit);
        // custom
        ItemStack custom = new ItemStack(Material.ENDER_CHEST, 1);
        ItemMeta pre = custom.getItemMeta();
        pre.setDisplayName("Custom");
        custom.setItemMeta(pre);
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        back.setItemMeta(but);
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(can);

        return new ItemStack[]{
                box, jungle, nether, def, swamp, tent, village, yellow, tel,
                ang, sub, flo, stone, chal, desert, well, win, rub,
                mine, cre, pea, lamp, candy, toi, rob, tor, pine,
                pun, por, cake, grave, topsy, mush, fen, gaz, app,
                lig, lib, sno, jail, pan, dou, pris, cho, and,
                dio, gra, null, custom, null, null, null, back, close
        };
    }

    public ItemStack[] getPresets() {
        return terminal;
    }
}
