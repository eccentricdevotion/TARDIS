/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.chameleon.gui;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;
import java.util.Map;

public class TARDISColourPickerGUI {

    private final TARDIS plugin;
    private final ItemStack[] gui;

    public TARDISColourPickerGUI(TARDIS plugin) {
        this.plugin = plugin;
        gui = getItemStack();
    }

    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta play = (LeatherArmorMeta) dis.getItemMeta();
        play.setDisplayName("Colour");
        play.setLore(List.of("Red: 255", "Green: 255", "Blue: 255"));
        play.setItemModel(ColouredVariant.TINT.getKey());
        play.setColor(Color.fromRGB(255, 255, 255)); // white
        play.addItemFlags(ItemFlag.values());
        play.setAttributeModifiers(Multimaps.forMap(Map.of()));
        dis.setItemMeta(play);
        // red
        ItemStack red = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta rrr = red.getItemMeta();
        rrr.setDisplayName("Red");
        red.setItemMeta(rrr);
        // green
        ItemStack green = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta ggg = green.getItemMeta();
        ggg.setDisplayName("Green");
        green.setItemMeta(ggg);
        // blue
        ItemStack blue = new ItemStack(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta bbb = blue.getItemMeta();
        bbb.setDisplayName("Blue");
        blue.setItemMeta(bbb);
        // red tint
        ItemStack redtint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta rrrtint = (LeatherArmorMeta) redtint.getItemMeta();
        rrrtint.setColor(Color.fromRGB(255, 0, 0)); // red
        rrrtint.setItemModel(ColouredVariant.TINT.getKey());
        rrrtint.setDisplayName("Red");
        rrrtint.addItemFlags(ItemFlag.values());
        rrrtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        redtint.setItemMeta(rrrtint);
        // green tint
        ItemStack greentint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta gggtint = (LeatherArmorMeta) greentint.getItemMeta();
        gggtint.setColor(Color.fromRGB(0, 255, 0)); // green
        gggtint.setItemModel(ColouredVariant.TINT.getKey());
        gggtint.setDisplayName("Green");
        gggtint.addItemFlags(ItemFlag.values());
        gggtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        greentint.setItemMeta(gggtint);
        // blue tint
        ItemStack bluetint = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta bbbtint = (LeatherArmorMeta) bluetint.getItemMeta();
        bbbtint.setColor(Color.fromRGB(0, 0, 255)); // blue
        bbbtint.setItemModel(ColouredVariant.TINT.getKey());
        bbbtint.setDisplayName("Blue");
        bbbtint.addItemFlags(ItemFlag.values());
        bbbtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        bluetint.setItemMeta(bbbtint);
        // less
        ItemStack less = new ItemStack(Material.ARROW, 1);
        ItemMeta lll = less.getItemMeta();
        lll.setDisplayName("Less");
        less.setItemMeta(lll);
        // more
        ItemStack more = new ItemStack(Material.ARROW, 1);
        ItemMeta mmm = more.getItemMeta();
        mmm.setDisplayName("More");
        more.setItemMeta(mmm);
        // select
        ItemStack select = new ItemStack(Material.BOWL, 1);
        ItemMeta sss = select.getItemMeta();
        sss.setDisplayName("Select colour");
        select.setItemMeta(sss);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        close.setItemMeta(win);
        return new ItemStack[]{
                null, null, null, null, dis, null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                red, null, less, null, redtint, null, more, null, null,
                green, null, less, null, greentint, null, more, null, select,
                blue, null, less, null, bluetint, null, more, null, null,
                null, null, null, null, null, null, null, null, close
        };
    }

    public ItemStack[] getGUI() {
        return gui;
    }
}
