/*
 * Copyright (C) 2024 eccentric_nz
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
import me.eccentric_nz.TARDIS.custommodeldata.GUIMap;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSMap {

    private final ItemStack[] map;
    private final TARDIS plugin;

    public TARDISARSMap(TARDIS plugin) {
        this.plugin = plugin;
        map = getItemStack();
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System Map.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // direction pad
        // up
        ItemStack pad_up = new ItemStack(GUIMap.BUTTON_UP.material(), 1);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName(plugin.getLanguage().getString("BUTTON_UP"));
        up.setCustomModelData(GUIMap.BUTTON_UP.customModelData());
        pad_up.setItemMeta(up);
        // down
        ItemStack pad_down = new ItemStack(GUIMap.BUTTON_DOWN.material(), 1);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName(plugin.getLanguage().getString("BUTTON_DOWN"));
        down.setCustomModelData(GUIMap.BUTTON_DOWN.customModelData());
        pad_down.setItemMeta(down);
        // left
        ItemStack pad_left = new ItemStack(GUIMap.BUTTON_LEFT.material(), 1);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName(plugin.getLanguage().getString("BUTTON_LEFT"));
        left.setCustomModelData(GUIMap.BUTTON_LEFT.customModelData());
        pad_left.setItemMeta(left);
        // right
        ItemStack pad_right = new ItemStack(GUIMap.BUTTON_RIGHT.material(), 1);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName(plugin.getLanguage().getString("BUTTON_RIGHT"));
        right.setCustomModelData(GUIMap.BUTTON_RIGHT.customModelData());
        pad_right.setItemMeta(right);
        // level selected
        ItemStack level_sel = new ItemStack(GUIMap.BUTTON_LEVEL.material(), 1);
        ItemMeta main = level_sel.getItemMeta();
        main.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL"));
        main.setCustomModelData(GUIMap.BUTTON_LEVEL.customModelData());
        level_sel.setItemMeta(main);
        // level top
        ItemStack level_top = new ItemStack(GUIMap.BUTTON_LEVEL_T.material(), 1);
        ItemMeta top = level_top.getItemMeta();
        top.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_T"));
        top.setCustomModelData(GUIMap.BUTTON_LEVEL_T.customModelData());
        level_top.setItemMeta(top);
        // level top
        ItemStack level_bot = new ItemStack(GUIMap.BUTTON_LEVEL_B.material(), 1);
        ItemMeta bot = level_bot.getItemMeta();
        bot.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_B"));
        bot.setCustomModelData(GUIMap.BUTTON_LEVEL_B.customModelData());
        level_bot.setItemMeta(bot);
        // stone
        ItemStack black = new ItemStack(GUIMap.BUTTON_MAP_ON.material(), 1);
        ItemMeta wool = black.getItemMeta();
        wool.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP_ON"));
        wool.setCustomModelData(GUIMap.BUTTON_MAP_ON.customModelData());
        black.setItemMeta(wool);
        // load map
        ItemStack loa = new ItemStack(GUIMap.BUTTON_MAP.material(), 1);
        ItemMeta der = loa.getItemMeta();
        der.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP"));
        der.setCustomModelData(GUIMap.BUTTON_MAP.customModelData());
        loa.setItemMeta(der);
        // close
        ItemStack close = new ItemStack(GUIMap.BUTTON_CLOSE.material(), 1);
        ItemMeta gui = close.getItemMeta();
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setCustomModelData(GUIMap.BUTTON_CLOSE.customModelData());
        close.setItemMeta(gui);
        // transmat
        ItemStack tran = new ItemStack(GUIMap.BUTTON_TRANSMAT.material(), 1);
        ItemMeta smat = tran.getItemMeta();
        smat.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
        smat.setCustomModelData(GUIMap.BUTTON_TRANSMAT.customModelData());
        tran.setItemMeta(smat);
        // where am I?
        ItemStack where = new ItemStack(GUIMap.BUTTON_WHERE.material(), 1);
        ItemMeta ami = where.getItemMeta();
        ami.setDisplayName(plugin.getLanguage().getString("BUTTON_WHERE"));
        ami.setCustomModelData(GUIMap.BUTTON_WHERE.customModelData());
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

    public ItemStack[] getMap() {
        return map;
    }
}
