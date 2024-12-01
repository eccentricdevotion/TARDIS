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
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

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
            if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_" + underscored.toLowerCase(Locale.ROOT))) {
                String dye = underscored.toUpperCase(Locale.ROOT) + "_DYE";
                ItemStack is = new ItemStack(Material.valueOf(dye), 1);
                ItemMeta im = is.getItemMeta();
                im.setDisplayName(s + " Police Box");
                switch (s) {
                    case "Blue" -> im.setItemModel(ChameleonVariant.BLUE.getKey());
                    case "White" -> im.setItemModel(ChameleonVariant.WHITE.getKey());
                    case "Orange" -> im.setItemModel(ChameleonVariant.ORANGE.getKey());
                    case "Magenta" -> im.setItemModel(ChameleonVariant.MAGENTA.getKey());
                    case "Light Blue" -> im.setItemModel(ChameleonVariant.LIGHT_BLUE.getKey());
                    case "Yellow" -> im.setItemModel(ChameleonVariant.YELLOW.getKey());
                    case "Lime" -> im.setItemModel(ChameleonVariant.LIME.getKey());
                    case "Pink" -> im.setItemModel(ChameleonVariant.PINK.getKey());
                    case "Gray" -> im.setItemModel(ChameleonVariant.GRAY.getKey());
                    case "Light Gray" -> im.setItemModel(ChameleonVariant.LIGHT_GRAY.getKey());
                    case "Cyan" -> im.setItemModel(ChameleonVariant.CYAN.getKey());
                    case "Purple" -> im.setItemModel(ChameleonVariant.PURPLE.getKey());
                    case "Brown" -> im.setItemModel(ChameleonVariant.BROWN.getKey());
                    case "Green" -> im.setItemModel(ChameleonVariant.GREEN.getKey());
                    case "Red" -> im.setItemModel(ChameleonVariant.RED.getKey());
                    case "Black" -> im.setItemModel(ChameleonVariant.BLACK.getKey());
                }
                is.setItemMeta(im);
                boxes[i] = is;
            }
            i++;
        }
        // tennant
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tennant")) {
            ItemStack david = new ItemStack(Material.CYAN_STAINED_GLASS_PANE, 1);
            ItemMeta tennant = david.getItemMeta();
            tennant.setDisplayName("Tennant Era Police Box");
            tennant.setItemModel(ChameleonVariant.TENNANT.getKey());
            david.setItemMeta(tennant);
            boxes[i] = david;
            i++;
        }
        // weeping angel
        if (TARDISPermission.hasPermission(player, "tardis.preset.weeping_angel")) {
            ItemStack is = new ItemStack(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta im = is.getItemMeta();
            im.setDisplayName("Weeping Angel");
            im.setItemModel(ChameleonVariant.WEEPING_ANGEL.getKey());
            is.setItemMeta(im);
            boxes[i] = is;
            i++;
        }
        // pandorica
        if (TARDISPermission.hasPermission(player, "tardis.preset.pandorica")) {
            ItemStack pan = new ItemStack(Material.ENDER_PEARL, 1);
            ItemMeta ica = pan.getItemMeta();
            ica.setDisplayName("Pandorica");
            ica.setItemModel(ChameleonVariant.PANDORICA.getKey());
            pan.setItemMeta(ica);
            boxes[i] = pan;
            i++;
        }
        // any colour
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tinted")) {
            ItemStack any = new ItemStack(Material.LEATHER_HORSE_ARMOR, 1);
            ItemMeta colour = any.getItemMeta();
            colour.setDisplayName("Pick a colour Police Box");
            colour.setItemModel(ColouredVariant.TINTED.getKey());
            any.setItemMeta(colour);
            boxes[i] = any;
            i++;
        }
        for (String custom : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (i < 50) {
                try {
                    Material cm = Material.valueOf(plugin.getCustomModelConfig().getString("models." + custom + ".item"));
                    ItemStack cis = new ItemStack(cm);
                    ItemMeta cim = cis.getItemMeta();
                    cim.setDisplayName(custom);
                    cim.setItemModel(new NamespacedKey(plugin, "block/police_box/" + custom + "_closed"));
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
        one.setItemModel(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.key());
        page.setItemMeta(one);
        boxes[GUIChameleonPoliceBoxes.GO_TO_PAGE_1.slot()] = page;
        // back
        ItemStack back = new ItemStack(GUIChameleonPoliceBoxes.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.setDisplayName("Back");
        but.setItemModel(GUIChameleonPoliceBoxes.BACK.key());
        back.setItemMeta(but);
        boxes[GUIChameleonPoliceBoxes.BACK.slot()] = back;
        // Cancel / close
        ItemStack close = new ItemStack(GUIChameleonPoliceBoxes.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        can.setItemModel(GUIChameleonPoliceBoxes.CLOSE.key());
        close.setItemMeta(can);
        boxes[GUIChameleonPoliceBoxes.CLOSE.slot()] = close;
        return boxes;
    }

    public ItemStack[] getBoxes() {
        return boxes;
    }
}
