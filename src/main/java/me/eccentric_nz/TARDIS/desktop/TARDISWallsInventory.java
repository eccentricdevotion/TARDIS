/*
 * Copyright (C) 2016 eccentric_nz
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
package me.eccentric_nz.TARDIS.desktop;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls.Pair;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * By the time of his eleventh incarnation, the Doctor's console room had gone
 * through at least twelve redesigns, though the TARDIS revealed that she had
 * archived 30 versions. Once a control room was reconfigured, the TARDIS
 * archived the old design "for neatness". The TARDIS effectively "curated" a
 * museum of control rooms — both those in the Doctor's personal past and future
 *
 * @author eccentric_nz
 */
public class TARDISWallsInventory {

    private final ItemStack[] menu;
    private final TARDIS plugin;
    private final List<String> notthese = Arrays.asList("PINE_WOOD", "PINE_LOG", "GREY_WOOL", "LIGHT_GREY_WOOL", "GREY_CLAY", "LIGHT_GREY_CLAY", "STONE_BRICK", "CHISELED_STONE", "HUGE_MUSHROOM_STEM");

    public TARDISWallsInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.menu = getItemStack();
    }

    /**
     * Constructs an inventory for the Player Preferences Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    @SuppressWarnings("deprecation")
    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[54];
        int i = 0;
        // get blocks
        for (Map.Entry<String, Pair> entry : plugin.getTardisWalls().blocks.entrySet()) {
            if (i > 52) {
                break;
            }
            if (!notthese.contains(entry.getKey())) {
                Pair value = entry.getValue();
                ItemStack is = new ItemStack(value.getType(), 1, value.getData());
                stack[i] = is;
                if (i % 9 == 7) {
                    i += 2;
                } else {
                    i++;
                }
            }
        }

        // scroll up
        ItemStack scroll_up = new ItemStack(Material.ARROW, 1);
        ItemMeta uim = scroll_up.getItemMeta();
        uim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_U"));
        scroll_up.setItemMeta(uim);
        stack[8] = scroll_up;
        // scroll down
        ItemStack scroll_down = new ItemStack(Material.ARROW, 1);
        ItemMeta dim = scroll_down.getItemMeta();
        dim.setDisplayName(plugin.getLanguage().getString("BUTTON_SCROLL_D"));
        scroll_down.setItemMeta(dim);
        stack[35] = scroll_down;
        // close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta close_im = close.getItemMeta();
        close_im.setDisplayName("Abort upgrade");
        close.setItemMeta(close_im);
        stack[53] = close;

        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
