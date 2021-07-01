/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.ars;

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.custommodeldata.GuiMap;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS's Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TardisArsMap {

    private final ItemStack[] map;
    private final TardisPlugin plugin;

    public TardisArsMap(TardisPlugin plugin) {
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
        ItemStack pad_up = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta up = pad_up.getItemMeta();
        assert up != null;
        up.setDisplayName(plugin.getLanguage().getString("BUTTON_UP"));
        up.setCustomModelData(GuiMap.BUTTON_UP.getCustomModelData());
        pad_up.setItemMeta(up);
        // down
        ItemStack pad_down = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta down = pad_down.getItemMeta();
        assert down != null;
        down.setDisplayName(plugin.getLanguage().getString("BUTTON_DOWN"));
        down.setCustomModelData(GuiMap.BUTTON_DOWN.getCustomModelData());
        pad_down.setItemMeta(down);
        // left
        ItemStack pad_left = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta left = pad_left.getItemMeta();
        assert left != null;
        left.setDisplayName(plugin.getLanguage().getString("BUTTON_LEFT"));
        left.setCustomModelData(GuiMap.BUTTON_LEFT.getCustomModelData());
        pad_left.setItemMeta(left);
        // right
        ItemStack pad_right = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta right = pad_right.getItemMeta();
        assert right != null;
        right.setDisplayName(plugin.getLanguage().getString("BUTTON_RIGHT"));
        right.setCustomModelData(GuiMap.BUTTON_RIGHT.getCustomModelData());
        pad_right.setItemMeta(right);
        // level selected
        ItemStack level_sel = new ItemStack(Material.YELLOW_WOOL, 1);
        ItemMeta main = level_sel.getItemMeta();
        assert main != null;
        main.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL"));
        main.setCustomModelData(GuiMap.BUTTON_LEVEL.getCustomModelData());
        level_sel.setItemMeta(main);
        // level top
        ItemStack level_top = new ItemStack(Material.WHITE_WOOL, 1);
        ItemMeta top = level_top.getItemMeta();
        assert top != null;
        top.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_T"));
        top.setCustomModelData(GuiMap.BUTTON_LEVEL_T.getCustomModelData());
        level_top.setItemMeta(top);
        // level top
        ItemStack level_bot = new ItemStack(Material.WHITE_WOOL, 1);
        ItemMeta bot = level_bot.getItemMeta();
        assert bot != null;
        bot.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_B"));
        bot.setCustomModelData(GuiMap.BUTTON_LEVEL_B.getCustomModelData());
        level_bot.setItemMeta(bot);
        // stone
        ItemStack black = new ItemStack(Material.BLACK_WOOL, 1);
        ItemMeta wool = black.getItemMeta();
        assert wool != null;
        wool.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP_ON"));
        wool.setCustomModelData(GuiMap.BUTTON_MAP_ON.getCustomModelData());
        black.setItemMeta(wool);
        // load map
        ItemStack loa = new ItemStack(Material.MAP, 1);
        ItemMeta der = loa.getItemMeta();
        assert der != null;
        der.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP"));
        der.setCustomModelData(GuiMap.BUTTON_MAP.getCustomModelData());
        loa.setItemMeta(der);
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta gui = close.getItemMeta();
        assert gui != null;
        gui.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        gui.setCustomModelData(GuiMap.BUTTON_CLOSE.getCustomModelData());
        close.setItemMeta(gui);
        // transmat
        ItemStack tran = new ItemStack(Material.BOWL, 1);
        ItemMeta smat = tran.getItemMeta();
        assert smat != null;
        smat.setDisplayName(plugin.getLanguage().getString("BUTTON_TRANSMAT"));
        smat.setCustomModelData(GuiMap.BUTTON_TRANSMAT.getCustomModelData());
        tran.setItemMeta(smat);
        // where am I?
        ItemStack where = new ItemStack(Material.COMPASS, 1);
        ItemMeta ami = where.getItemMeta();
        assert ami != null;
        ami.setDisplayName(plugin.getLanguage().getString("BUTTON_WHERE"));
        ami.setCustomModelData(GuiMap.BUTTON_WHERE.getCustomModelData());
        where.setItemMeta(ami);

        return new ItemStack[]{null, pad_up, null, null, black, black, black, black, black, pad_left, loa, pad_right, null, black, black, black, black, black, null, pad_down, null, null, black, black, black, black, black, level_bot, level_sel, level_top, null, black, black, black, black, black, null, null, null, null, black, black, black, black, black, close, tran, where, null, null, null, null, null, null};
    }

    public ItemStack[] getMap() {
        return map;
    }
}
