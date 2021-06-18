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

import me.eccentric_nz.tardis.TardisPlugin;
import me.eccentric_nz.tardis.blueprints.TardisPermission;
import me.eccentric_nz.tardis.custommodeldata.GuiChameleonPoliceBoxes;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
class TardisPoliceBoxInventory {

    private final List<String> colours = Arrays.asList("Blue", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Brown", "Green", "Red", "Black");
    private final ItemStack[] boxes;
    private final TardisPlugin plugin;
    private final Player player;

    TardisPoliceBoxInventory(TardisPlugin plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        boxes = getItemStack();
    }

    /**
     * Constructs an inventory for the Chameleon Circuit GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] boxes = new ItemStack[27];
        int i = 0;
        // coloured police boxes
        for (String s : colours) {
            String underscored = s.replace(" ", "_");
            if (TardisPermission.hasPermission(player, "tardis.preset.police_box_" + underscored.toLowerCase())) {
                String dye = underscored.toUpperCase() + "_DYE";
                ItemStack is = new ItemStack(Material.valueOf(dye), 1);
                ItemMeta im = is.getItemMeta();
                assert im != null;
                im.setDisplayName(s + " Police Box");
                im.setCustomModelData(1001);
                is.setItemMeta(im);
                boxes[i] = is;
            }
            i++;
        }
        // weeping angel
        ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta im = is.getItemMeta();
        assert im != null;
        im.setDisplayName("Weeping Angel");
        im.setCustomModelData(1001);
        is.setItemMeta(im);
        boxes[i] = is;
        // page one
        ItemStack page = new ItemStack(Material.ARROW, 1);
        ItemMeta one = page.getItemMeta();
        assert one != null;
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setCustomModelData(GuiChameleonPoliceBoxes.GO_TO_PAGE_1.getCustomModelData());
        page.setItemMeta(one);
        boxes[24] = page;
        // back
        ItemStack back = new ItemStack(Material.BOWL, 1);
        ItemMeta but = back.getItemMeta();
        assert but != null;
        but.setDisplayName("Back");
        but.setCustomModelData(GuiChameleonPoliceBoxes.BACK.getCustomModelData());
        back.setItemMeta(but);
        boxes[25] = back;
        // Cancel / close
        ItemStack close = new ItemStack(Material.BOWL, 1);
        ItemMeta can = close.getItemMeta();
        assert can != null;
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GuiChameleonPoliceBoxes.CLOSE.getCustomModelData());
        close.setItemMeta(can);
        boxes[26] = close;

        return boxes;
    }

    ItemStack[] getBoxes() {
        return boxes;
    }
}
