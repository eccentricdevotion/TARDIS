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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSMap implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISARSMap(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Map", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System Map.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // direction pad
        // up
        ItemStack pad_up = ItemStack.of(GUIMap.BUTTON_UP.material(), 1);
        ItemMeta up = pad_up.getItemMeta();
        up.displayName(Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        pad_up.setItemMeta(up);
        // down
        ItemStack pad_down = ItemStack.of(GUIMap.BUTTON_DOWN.material(), 1);
        ItemMeta down = pad_down.getItemMeta();
        down.displayName(Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        pad_down.setItemMeta(down);
        // left
        ItemStack pad_left = ItemStack.of(GUIMap.BUTTON_LEFT.material(), 1);
        ItemMeta left = pad_left.getItemMeta();
        left.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        pad_left.setItemMeta(left);
        // right
        ItemStack pad_right = ItemStack.of(GUIMap.BUTTON_RIGHT.material(), 1);
        ItemMeta right = pad_right.getItemMeta();
        right.displayName(Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        pad_right.setItemMeta(right);
        // level selected
        ItemStack level_sel = ItemStack.of(GUIMap.BUTTON_LEVEL.material(), 1);
        ItemMeta main = level_sel.getItemMeta();
        main.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        level_sel.setItemMeta(main);
        // level top
        ItemStack level_top = ItemStack.of(GUIMap.BUTTON_LEVEL_T.material(), 1);
        ItemMeta top = level_top.getItemMeta();
        top.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        level_top.setItemMeta(top);
        // level top
        ItemStack level_bot = ItemStack.of(GUIMap.BUTTON_LEVEL_B.material(), 1);
        ItemMeta bot = level_bot.getItemMeta();
        bot.displayName(Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        level_bot.setItemMeta(bot);
        // stone
        ItemStack black = ItemStack.of(GUIMap.BUTTON_MAP_ON.material(), 1);
        ItemMeta wool = black.getItemMeta();
        wool.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        black.setItemMeta(wool);
        // load map
        ItemStack loa = ItemStack.of(GUIMap.BUTTON_MAP.material(), 1);
        ItemMeta der = loa.getItemMeta();
        der.displayName(Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        loa.setItemMeta(der);
        // close
        ItemStack close = ItemStack.of(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(gui);
        // transmat
        ItemStack tran = ItemStack.of(GUIMap.BUTTON_TRANSMAT.material(), 1);
        ItemMeta smat = tran.getItemMeta();
        smat.displayName(Component.text(plugin.getLanguage().getString("BUTTON_TRANSMAT", "Transmat")));
        tran.setItemMeta(smat);
        // where am I?
        ItemStack where = ItemStack.of(GUIMap.BUTTON_WHERE.material(), 1);
        ItemMeta ami = where.getItemMeta();
        ami.displayName(Component.text(plugin.getLanguage().getString("BUTTON_WHERE")));
        where.setItemMeta(ami);

        return new ItemStack[]{
                null, pad_up, null, null, black, black, black, black, black,
                pad_left, loa, pad_right, null, black, black, black, black, black,
                null, pad_down, null, null, black, black, black, black, black,
                level_bot, level_sel, level_top, null, black, black, black, black, black,
                null, null, null, null, black, black, black, black, black,
                close, tran, where, null, null, null, null, null, null
        };
    }
}
