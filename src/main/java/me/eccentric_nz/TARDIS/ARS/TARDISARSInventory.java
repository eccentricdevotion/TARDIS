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
package me.eccentric_nz.TARDIS.ARS;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIArs;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Collections;
import java.util.List;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSInventory {

    private final ItemStack[] ars;
    private final TARDIS plugin;

    public TARDISARSInventory(TARDIS plugin) {
        this.plugin = plugin;
        ars = getItemStack();
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] is = new ItemStack[54];
        // direction pad up
        ItemStack pad_up = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName(plugin.getLanguage().getString("BUTTON_UP"));
        up.setCustomModelData(GUIArs.BUTTON_UP.getCustomModelData());
        pad_up.setItemMeta(up);
        is[1] = pad_up;
        // black wool
        ItemStack black = new ItemStack(Material.BLACK_WOOL, 1);
        ItemMeta wool = black.getItemMeta();
        wool.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP_NO"));
        wool.setCustomModelData(GUIArs.BUTTON_MAP_ON.getCustomModelData());
        black.setItemMeta(wool);
        for (int j = 0; j < 37; j += 9) {
            for (int k = 0; k < 5; k++) {
                int slot = 4 + j + k;
                is[slot] = black;
            }
        }
        // direction pad left
        ItemStack pad_left = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName(plugin.getLanguage().getString("BUTTON_LEFT"));
        left.setCustomModelData(GUIArs.BUTTON_LEFT.getCustomModelData());
        pad_left.setItemMeta(left);
        is[9] = pad_left;
        // load map
        ItemStack map = new ItemStack(Material.MAP, 1);
        ItemMeta load = map.getItemMeta();
        load.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP"));
        load.setCustomModelData(GUIArs.BUTTON_MAP.getCustomModelData());
        map.setItemMeta(load);
        is[10] = map;
        // direction pad right
        ItemStack pad_right = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName(plugin.getLanguage().getString("BUTTON_RIGHT"));
        right.setCustomModelData(GUIArs.BUTTON_RIGHT.getCustomModelData());
        pad_right.setItemMeta(right);
        is[11] = pad_right;
        // set
        ItemStack s = new ItemStack(Material.PINK_WOOL, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_RECON"));
        sim.setCustomModelData(GUIArs.BUTTON_RECON.getCustomModelData());
        s.setItemMeta(sim);
        is[12] = s;
        // direction pad down
        ItemStack pad_down = new ItemStack(Material.CYAN_WOOL, 1);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName(plugin.getLanguage().getString("BUTTON_DOWN"));
        down.setCustomModelData(GUIArs.BUTTON_DOWN.getCustomModelData());
        pad_down.setItemMeta(down);
        is[19] = pad_down;
        // level bottom
        ItemStack level_bot = new ItemStack(Material.WHITE_WOOL, 1);
        ItemMeta bot = level_bot.getItemMeta();
        bot.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_B"));
        bot.setCustomModelData(GUIArs.BUTTON_LEVEL_B.getCustomModelData());
        level_bot.setItemMeta(bot);
        is[27] = level_bot;
        // level selected
        ItemStack level_sel = new ItemStack(Material.YELLOW_WOOL, 1);
        ItemMeta main = level_sel.getItemMeta();
        main.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL"));
        main.setCustomModelData(GUIArs.BUTTON_LEVEL.getCustomModelData());
        level_sel.setItemMeta(main);
        is[28] = level_sel;
        // level top
        ItemStack level_top = new ItemStack(Material.WHITE_WOOL, 1);
        ItemMeta top = level_top.getItemMeta();
        top.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_T"));
        top.setCustomModelData(GUIArs.BUTTON_LEVEL_T.getCustomModelData());
        level_top.setItemMeta(top);
        is[29] = level_top;
        // reset
        ItemStack reset = new ItemStack(Material.COBBLESTONE, 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.setDisplayName(plugin.getLanguage().getString("BUTTON_RESET"));
        cobble.setCustomModelData(GUIArs.BUTTON_RESET.getCustomModelData());
        reset.setItemMeta(cobble);
        is[30] = reset;
        // scroll left
        ItemStack scroll_left = new ItemStack(Material.RED_WOOL, 1);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_L"));
        nim.setCustomModelData(GUIArs.BUTTON_SCROLL_L.getCustomModelData());
        scroll_left.setItemMeta(nim);
        is[36] = scroll_left;
        // scroll right
        ItemStack scroll_right = new ItemStack(Material.LIME_WOOL, 1);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_R"));
        pim.setCustomModelData(GUIArs.BUTTON_SCROLL_R.getCustomModelData());
        scroll_right.setItemMeta(pim);
        is[38] = scroll_right;
        // jettison
        ItemStack jettison = new ItemStack(Material.TNT, 1);
        ItemMeta tnt = jettison.getItemMeta();
        tnt.setDisplayName(plugin.getLanguage().getString("BUTTON_JETT"));
        tnt.setCustomModelData(GUIArs.BUTTON_JETT.getCustomModelData());
        jettison.setItemMeta(tnt);
        is[39] = jettison;

        int i = 45;
        for (TARDISARS a : TARDISARS.values()) {
            if (a.getOffset() != 0 && i < 54) {
                ItemStack room = new ItemStack(Material.getMaterial(a.getMaterial()), 1);
                ItemMeta im = room.getItemMeta();
                im.setDisplayName(a.getDescriptiveName());
                List<String> lore = Collections.singletonList("Cost: " + plugin.getRoomsConfig().getInt("rooms." + a + ".cost"));
                im.setLore(lore);
                im.setCustomModelData(1);
                room.setItemMeta(im);
                is[i] = room;
                i++;
            }
        }
        return is;
    }

    public ItemStack[] getARS() {
        return ars;
    }
}
