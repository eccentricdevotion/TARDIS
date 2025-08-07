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
package me.eccentric_nz.TARDIS.info;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISIndexFileSection implements InventoryHolder {

    private final TARDIS plugin;
    private final TISCategory category;
    private final Inventory inventory;

    public TARDISIndexFileSection(TARDIS plugin, TISCategory category) {
        this.plugin = plugin;
        this.category = category;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Info Category", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        for (TARDISInfoMenu tim : TARDISInfoMenu.values()) {
            if (category == TISCategory.ITEMS && tim.isItem()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.CONSOLE_BLOCKS && tim.isConsoleBlock()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.ACCESSORIES && tim.isAccessory()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.COMPONENTS && tim.isComponent()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.SONIC_COMPONENTS && tim.isSonicComponent()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.SONIC_UPGRADES && tim.isSonicUpgrade()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.CONSOLES && tim.isConsole()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.DISKS && tim.isDisk()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.ROOMS && tim.isRoom()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.PLANETS && tim.isPlanet()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.TIME_TRAVEL && tim.isTimeTravel()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.FOOD && tim.isFood()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.UPDATEABLE_BLOCKS && tim.isUpdateable()) {
                stack[i] = makeButton(tim);
                i++;
            } else if (category == TISCategory.MONSTERS && tim.isMonster()) {
                stack[i] = makeButton(tim);
                i++;
            }
        }
        // back
        ItemStack back = ItemStack.of(GUIChameleonPoliceBoxes.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.displayName(Component.text("Back"));
        back.setItemMeta(but);
        stack[45] = back;
        // close
        ItemStack close = ItemStack.of(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(close_im);
        stack[53] = close;
        return stack;
    }

    private ItemStack makeButton(TARDISInfoMenu tim) {
        ItemStack is = ItemStack.of(Material.WRITTEN_BOOK, 1);
        ItemMeta im = is.getItemMeta();
        im.displayName(Component.text(TARDISStringUtils.capitalise(tim.toString())));
        is.setItemMeta(im);
        return is;
    }
}
