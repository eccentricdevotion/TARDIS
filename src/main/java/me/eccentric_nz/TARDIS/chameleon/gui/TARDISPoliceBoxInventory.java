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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of
 * travelling through time, even in a non-linear direction. In the 26th century
 * individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
public class TARDISPoliceBoxInventory {

    private final List<String> colours = Arrays.asList("Blue", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Brown", "Green", "Red", "Black");
    private final ItemStack[] boxes;
    private final TARDIS plugin;
    private final Player player;

    public TARDISPoliceBoxInventory(TARDIS plugin, Player player) {
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
        ItemStack[] boxes = new ItemStack[54];
        int i = 0;
        // coloured police boxes
        for (String s : colours) {
            String underscored = s.replace(" ", "_");
            if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_" + underscored.toLowerCase())) {
                String dye = underscored.toUpperCase() + "_DYE";
                ItemStack is = new ItemStack(Material.valueOf(dye), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(s + " Police Box");
                im.setCustomModelData(1001);
                is.setItemMeta(im);
                boxes[i] = is;
            }
            i++;
        }
        // tennant
        ItemStack david = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
        ItemMeta tennant = david.getItemMeta();
        tennant.setDisplayName("Tennant Era Police Box");
        tennant.setCustomModelData(1001);
        david.setItemMeta(tennant);
        boxes[i] = david;
        i++;
        // weeping angel
        ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Weeping Angel");
        im.setCustomModelData(1001);
        is.setItemMeta(im);
        boxes[i] = is;
        i++;
        // pandorica
        ItemStack pan = new ItemStack(Material.ENDER_PEARL, 1);
        ItemMeta ica = pan.getItemMeta();
        ica.setDisplayName("Pandorica");
        ica.setCustomModelData(1001);
        pan.setItemMeta(ica);
        boxes[i] = pan;
        i++;
        // any colour
        ItemStack any = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
        ItemMeta colour = any.getItemMeta();
        colour.setDisplayName("Pick a colour Police Box");
        colour.setCustomModelData(1001);
        any.setItemMeta(colour);
        boxes[i] = any;
        i++;
        for (String custom : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (i < 50) {
                try {
                    Material cm = Material.valueOf(plugin.getCustomModelConfig().getString("models." + custom + ".item"));
                    ItemStack cis = new ItemStack(cm);
                    ItemMeta cim = cis.getItemMeta();
                    cim.setDisplayName(custom);
                    cim.setCustomModelData(1001);
                    cis.setItemMeta(cim);
                    boxes[i] = cis;
                    i++;
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid material specified for custom model preset: " + custom + "!");
                }
            }
        }
        // page one
        ItemStack page = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setCustomModelData(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.customModelData());
        page.setItemMeta(one);
        boxes[GUIChameleonPoliceBoxes.GO_TO_PAGE_1.slot()] = page;
        // back
        ItemStack back = new ItemStack(GUIChameleonPoliceBoxes.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setCustomModelData(GUIChameleonPoliceBoxes.BACK.customModelData());
        back.setItemMeta(but);
        boxes[GUIChameleonPoliceBoxes.BACK.slot()] = back;
        // Cancel / close
        ItemStack close = new ItemStack(GUIChameleonPoliceBoxes.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setCustomModelData(GUIChameleonPoliceBoxes.CLOSE.customModelData());
        close.setItemMeta(can);
        boxes[GUIChameleonPoliceBoxes.CLOSE.slot()] = close;
        return boxes;
    }

    public ItemStack[] getBoxes() {
        return boxes;
    }
}
