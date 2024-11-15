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
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIArs;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural
 * Configuration software to relocate the console outside the ship (as it was
 * too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class TARDISARSInventory {

    private final ItemStack[] ars;
    private final TARDIS plugin;

    public TARDISARSInventory(TARDIS plugin, Player p) {
        this.plugin = plugin;
        ars = getItemStack(p);
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack(Player player) {

        ItemStack[] is = new ItemStack[54];
        // direction pad up
        ItemStack pad_up = new ItemStack(GUIArs.BUTTON_UP.material(), 1);
        ItemMeta up = pad_up.getItemMeta();
        up.setDisplayName(plugin.getLanguage().getString("BUTTON_UP"));
        up.setItemModel(GUIArs.BUTTON_UP.key());
        pad_up.setItemMeta(up);
        is[GUIArs.BUTTON_UP.slot()] = pad_up;
        // black wool
        ItemStack black = new ItemStack(GUIArs.BUTTON_MAP_ON.material(), 1);
        ItemMeta wool = black.getItemMeta();
        wool.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP_NO"));
        wool.setItemModel(GUIArs.BUTTON_MAP_ON.key());
        black.setItemMeta(wool);
        for (int j = 0; j < 37; j += 9) {
            for (int k = 0; k < 5; k++) {
                int slot = 4 + j + k;
                is[slot] = black;
            }
        }
        // direction pad left
        ItemStack pad_left = new ItemStack(GUIArs.BUTTON_LEFT.material(), 1);
        ItemMeta left = pad_left.getItemMeta();
        left.setDisplayName(plugin.getLanguage().getString("BUTTON_LEFT"));
        left.setItemModel(GUIArs.BUTTON_LEFT.key());
        pad_left.setItemMeta(left);
        is[GUIArs.BUTTON_LEFT.slot()] = pad_left;
        // load map
        ItemStack map = new ItemStack(GUIArs.BUTTON_MAP.material(), 1);
        ItemMeta load = map.getItemMeta();
        load.setDisplayName(plugin.getLanguage().getString("BUTTON_MAP"));
        load.setItemModel(GUIArs.BUTTON_MAP.key());
        map.setItemMeta(load);
        is[GUIArs.BUTTON_MAP.slot()] = map;
        // direction pad right
        ItemStack pad_right = new ItemStack(GUIArs.BUTTON_RIGHT.material(), 1);
        ItemMeta right = pad_right.getItemMeta();
        right.setDisplayName(plugin.getLanguage().getString("BUTTON_RIGHT"));
        right.setItemModel(GUIArs.BUTTON_RIGHT.key());
        pad_right.setItemMeta(right);
        is[GUIArs.BUTTON_RIGHT.slot()] = pad_right;
        // set
        ItemStack s = new ItemStack(GUIArs.BUTTON_RECON.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_RECON"));
        sim.setItemModel(GUIArs.BUTTON_RECON.key());
        s.setItemMeta(sim);
        is[GUIArs.BUTTON_RECON.slot()] = s;
        // direction pad down
        ItemStack pad_down = new ItemStack(GUIArs.BUTTON_DOWN.material(), 1);
        ItemMeta down = pad_down.getItemMeta();
        down.setDisplayName(plugin.getLanguage().getString("BUTTON_DOWN"));
        down.setItemModel(GUIArs.BUTTON_DOWN.key());
        pad_down.setItemMeta(down);
        is[GUIArs.BUTTON_DOWN.slot()] = pad_down;
        // level bottom
        ItemStack level_bot = new ItemStack(GUIArs.BUTTON_LEVEL_B.material(), 1);
        ItemMeta bot = level_bot.getItemMeta();
        bot.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_B"));
        bot.setItemModel(GUIArs.BUTTON_LEVEL_B.key());
        level_bot.setItemMeta(bot);
        is[GUIArs.BUTTON_LEVEL_B.slot()] = level_bot;
        // level selected
        ItemStack level_sel = new ItemStack(GUIArs.BUTTON_LEVEL.material(), 1);
        ItemMeta main = level_sel.getItemMeta();
        main.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL"));
        main.setItemModel(GUIArs.BUTTON_LEVEL.key());
        level_sel.setItemMeta(main);
        is[GUIArs.BUTTON_LEVEL.slot()] = level_sel;
        // level top
        ItemStack level_top = new ItemStack(GUIArs.BUTTON_LEVEL_T.material(), 1);
        ItemMeta top = level_top.getItemMeta();
        top.setDisplayName(plugin.getLanguage().getString("BUTTON_LEVEL_T"));
        top.setItemModel(GUIArs.BUTTON_LEVEL_T.key());
        level_top.setItemMeta(top);
        is[GUIArs.BUTTON_LEVEL_T.slot()] = level_top;
        // reset
        ItemStack reset = new ItemStack(GUIArs.BUTTON_RESET.material(), 1);
        ItemMeta cobble = reset.getItemMeta();
        cobble.setDisplayName(plugin.getLanguage().getString("BUTTON_RESET"));
        cobble.setItemModel(GUIArs.BUTTON_RESET.key());
        reset.setItemMeta(cobble);
        is[GUIArs.BUTTON_RESET.slot()] = reset;
        // scroll left
        ItemStack scroll_left = new ItemStack(GUIArs.BUTTON_SCROLL_L.material(), 1);
        ItemMeta nim = scroll_left.getItemMeta();
        nim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_L"));
        nim.setItemModel(GUIArs.BUTTON_SCROLL_L.key());
        scroll_left.setItemMeta(nim);
        is[GUIArs.BUTTON_SCROLL_L.slot()] = scroll_left;
        // scroll right
        ItemStack scroll_right = new ItemStack(GUIArs.BUTTON_SCROLL_R.material(), 1);
        ItemMeta pim = scroll_right.getItemMeta();
        pim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_R"));
        pim.setItemModel(GUIArs.BUTTON_SCROLL_R.key());
        scroll_right.setItemMeta(pim);
        is[GUIArs.BUTTON_SCROLL_R.slot()] = scroll_right;
        // jettison
        ItemStack jettison = new ItemStack(GUIArs.BUTTON_JETT.material(), 1);
        ItemMeta tnt = jettison.getItemMeta();
        tnt.setDisplayName(plugin.getLanguage().getString("BUTTON_JETT"));
        tnt.setItemModel(GUIArs.BUTTON_JETT.key());
        jettison.setItemMeta(tnt);
        is[GUIArs.BUTTON_JETT.slot()] = jettison;

        int i = 45;
        for (TARDISARS a : TARDISARS.values()) {
            if (a.getOffset() != 0 && i < 54) {
                ItemStack room = new ItemStack(Material.getMaterial(a.getMaterial()), 1);
                ItemMeta im = room.getItemMeta();
                im.setDisplayName(a.getDescriptiveName());
                List<String> lore = new ArrayList<>();
                lore.add("Cost: " + plugin.getRoomsConfig().getInt("rooms." + a + ".cost"));
                String roomName = TARDISARS.ARSFor(room.getType().toString()).getConfigPath();
                if (player != null && !TARDISPermission.hasPermission(player, "tardis.room." + roomName.toLowerCase(Locale.ROOT))) {
                    lore.add(ChatColor.RED + plugin.getLanguage().getString("NO_PERM_CONSOLE"));
                }
                im.setLore(lore);
                im.setItemModel(a.getKey());
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
