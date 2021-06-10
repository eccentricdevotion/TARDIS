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
package me.eccentric_nz.tardis.chameleon;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.blueprints.TARDISPermission;
import me.eccentric_nz.tardis.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.tardis.enumeration.PRESET;
import org.bukkit.Material;
import org.bukkit.entity.Player;
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
    private final TARDISPlugin plugin;
    private final Player player;

    TARDISPresetInventory(TARDISPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        terminal = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];

        for (PRESET preset : PRESET.values()) {
            if (!PRESET.NOT_THESE.contains(preset.getCraftMaterial()) && !preset.usesItemFrame()) {
                if (TARDISPermission.hasPermission(player, "tardis.preset." + preset.toString().toLowerCase())) {
                    ItemStack is = new ItemStack(preset.getGuiDisplay(), 1);
                    ItemMeta im = is.getItemMeta();
                    assert im != null;
                    im.setDisplayName(preset.getDisplayName());
                    is.setItemMeta(im);
                    stacks[preset.getSlot()] = is;
                }
            }
        }
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        assert but != null;
        but.setDisplayName("Back");
        but.setCustomModelData(GUIChameleonPresets.BACK.getCustomModelData());
        back.setItemMeta(but);
        stacks[51] = back;
        // page two
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta two = page.getItemMeta();
        assert two != null;
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        two.setCustomModelData(GUIChameleonPresets.GO_TO_PAGE_2.getCustomModelData());
        page.setItemMeta(two);
        stacks[52] = page;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        assert can != null;
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIChameleonPresets.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        stacks[53] = close;

        return stacks;
    }

    public ItemStack[] getPresets() {
        return terminal;
    }
}
