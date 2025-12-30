/*
 * Copyright (C) 2026 eccentric_nz
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
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import java.util.List;
import java.util.Map;

public class TARDISColourPickerGUI implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISColourPickerGUI(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Colour Picker", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        // display
        ItemStack dis = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta play = (LeatherArmorMeta) dis.getItemMeta();
        play.displayName(Component.text("Colour"));
        play.lore(List.of(
                Component.text("Red: 255"),
                Component.text("Green: 255"),
                Component.text("Blue: 255")
        ));
        play.setItemModel(ColouredVariant.TINT.getKey());
        play.setColor(Color.fromRGB(255, 255, 255)); // white
        play.addItemFlags(ItemFlag.values());
        play.setAttributeModifiers(Multimaps.forMap(Map.of()));
        dis.setItemMeta(play);
        // red
        ItemStack red = ItemStack.of(Material.RED_WOOL, 1);
        ItemMeta rrr = red.getItemMeta();
        rrr.displayName(Component.text("Red"));
        red.setItemMeta(rrr);
        // green
        ItemStack green = ItemStack.of(Material.LIME_WOOL, 1);
        ItemMeta ggg = green.getItemMeta();
        ggg.displayName(Component.text("Green"));
        green.setItemMeta(ggg);
        // blue
        ItemStack blue = ItemStack.of(Material.LIGHT_BLUE_WOOL, 1);
        ItemMeta bbb = blue.getItemMeta();
        bbb.displayName(Component.text("Blue"));
        blue.setItemMeta(bbb);
        // red tint
        ItemStack redtint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta rrrtint = (LeatherArmorMeta) redtint.getItemMeta();
        rrrtint.setColor(Color.fromRGB(255, 0, 0)); // red
        rrrtint.setItemModel(ColouredVariant.TINT.getKey());
        rrrtint.displayName(Component.text("Red"));
        rrrtint.addItemFlags(ItemFlag.values());
        rrrtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        redtint.setItemMeta(rrrtint);
        // green tint
        ItemStack greentint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta gggtint = (LeatherArmorMeta) greentint.getItemMeta();
        gggtint.setColor(Color.fromRGB(0, 255, 0)); // green
        gggtint.setItemModel(ColouredVariant.TINT.getKey());
        gggtint.displayName(Component.text("Green"));
        gggtint.addItemFlags(ItemFlag.values());
        gggtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        greentint.setItemMeta(gggtint);
        // blue tint
        ItemStack bluetint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        LeatherArmorMeta bbbtint = (LeatherArmorMeta) bluetint.getItemMeta();
        bbbtint.setColor(Color.fromRGB(0, 0, 255)); // blue
        bbbtint.setItemModel(ColouredVariant.TINT.getKey());
        bbbtint.displayName(Component.text("Blue"));
        bbbtint.addItemFlags(ItemFlag.values());
        bbbtint.setAttributeModifiers(Multimaps.forMap(Map.of()));
        bluetint.setItemMeta(bbbtint);
        // less
        ItemStack less = ItemStack.of(Material.ARROW, 1);
        ItemMeta lll = less.getItemMeta();
        lll.displayName(Component.text("Less"));
        less.setItemMeta(lll);
        // more
        ItemStack more = ItemStack.of(Material.ARROW, 1);
        ItemMeta mmm = more.getItemMeta();
        mmm.displayName(Component.text("More"));
        more.setItemMeta(mmm);
        // select
        ItemStack select = ItemStack.of(Material.BOWL, 1);
        ItemMeta sss = select.getItemMeta();
        sss.displayName(Component.text("Select colour"));
        select.setItemMeta(sss);
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta win = close.getItemMeta();
        win.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
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
}
