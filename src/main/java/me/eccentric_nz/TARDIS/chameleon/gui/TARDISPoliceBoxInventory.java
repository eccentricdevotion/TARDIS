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
package me.eccentric_nz.TARDIS.chameleon.gui;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.keys.ChameleonVariant;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
public class TARDISPoliceBoxInventory implements InventoryHolder {

    private final List<String> colours = List.of("Blue", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Brown", "Green", "Red", "Black");
    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    public TARDISPoliceBoxInventory(TARDIS plugin, Player player) {
        this.plugin = plugin;
        this.player = player;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Chameleon Police Boxes", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
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
                ItemStack is = ItemStack.of(Material.valueOf(dye), 1);
                ItemMeta im = is.getItemMeta();
                im.displayName(Component.text(s + " Police Box"));
                switch (s) {
                    case "Blue" -> im.setItemModel(ChameleonVariant.BLUE_CLOSED.getKey());
                    case "White" -> im.setItemModel(ChameleonVariant.WHITE_CLOSED.getKey());
                    case "Orange" -> im.setItemModel(ChameleonVariant.ORANGE_CLOSED.getKey());
                    case "Magenta" -> im.setItemModel(ChameleonVariant.MAGENTA_CLOSED.getKey());
                    case "Light Blue" -> im.setItemModel(ChameleonVariant.LIGHT_BLUE_CLOSED.getKey());
                    case "Yellow" -> im.setItemModel(ChameleonVariant.YELLOW_CLOSED.getKey());
                    case "Lime" -> im.setItemModel(ChameleonVariant.LIME_CLOSED.getKey());
                    case "Pink" -> im.setItemModel(ChameleonVariant.PINK_CLOSED.getKey());
                    case "Gray" -> im.setItemModel(ChameleonVariant.GRAY_CLOSED.getKey());
                    case "Light Gray" -> im.setItemModel(ChameleonVariant.LIGHT_GRAY_CLOSED.getKey());
                    case "Cyan" -> im.setItemModel(ChameleonVariant.CYAN_CLOSED.getKey());
                    case "Purple" -> im.setItemModel(ChameleonVariant.PURPLE_CLOSED.getKey());
                    case "Brown" -> im.setItemModel(ChameleonVariant.BROWN_CLOSED.getKey());
                    case "Green" -> im.setItemModel(ChameleonVariant.GREEN_CLOSED.getKey());
                    case "Red" -> im.setItemModel(ChameleonVariant.RED_CLOSED.getKey());
                    case "Black" -> im.setItemModel(ChameleonVariant.BLACK_CLOSED.getKey());
                }
                is.setItemMeta(im);
                boxes[i] = is;
            }
            i++;
        }
        // tennant
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tennant")) {
            ItemStack david = ItemStack.of(Material.CYAN_STAINED_GLASS_PANE, 1);
            ItemMeta tennant = david.getItemMeta();
            tennant.displayName(Component.text("Tennant Era Police Box"));
            tennant.setItemModel(ChameleonVariant.TENNANT_CLOSED.getKey());
            david.setItemMeta(tennant);
            boxes[i] = david;
            i++;
        }
        // weeping angel
        if (TARDISPermission.hasPermission(player, "tardis.preset.weeping_angel")) {
            ItemStack is = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE, 1);
            ItemMeta im = is.getItemMeta();
            im.displayName(Component.text("Weeping Angel"));
            im.setItemModel(ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey());
            is.setItemMeta(im);
            boxes[i] = is;
            i++;
        }
        // pandorica
        if (TARDISPermission.hasPermission(player, "tardis.preset.pandorica")) {
            ItemStack pan = ItemStack.of(Material.ENDER_PEARL, 1);
            ItemMeta ica = pan.getItemMeta();
            ica.displayName(Component.text("Pandorica"));
            ica.setItemModel(ChameleonVariant.PANDORICA_CLOSED.getKey());
            pan.setItemMeta(ica);
            boxes[i] = pan;
            i++;
        }
        // SIDRAT
        if (TARDISPermission.hasPermission(player, "tardis.preset.sidrat")) {
            ItemStack sid = ItemStack.of(Material.GREEN_STAINED_GLASS_PANE, 1);
            ItemMeta rat = sid.getItemMeta();
            rat.displayName(Component.text("SIDRAT"));
            rat.setItemModel(ChameleonVariant.SIDRAT_CLOSED.getKey());
            sid.setItemMeta(rat);
            boxes[i] = sid;
            i++;
        }
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tinted")) {
            ItemStack any = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
            ItemMeta colour = any.getItemMeta();
            colour.displayName(Component.text("Pick a colour Police Box"));
            colour.setItemModel(ColouredVariant.TINTED_CLOSED.getKey());
            any.setItemMeta(colour);
            boxes[i] = any;
            i++;
        }
        for (String custom : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (i < 50) {
                try {
                    Material cm = Material.valueOf(plugin.getCustomModelConfig().getString("models." + custom + ".item"));
                    ItemStack cis = ItemStack.of(cm);
                    ItemMeta cim = cis.getItemMeta();
                    cim.displayName(Component.text(custom));
                    String key = TARDISStringUtils.toUnderscoredLowercase(custom);
                    cim.setItemModel(new NamespacedKey(plugin, key + "_closed"));
                    cis.setItemMeta(cim);
                    boxes[i] = cis;
                    i++;
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid material specified for custom model preset: " + custom + "!");
                }
            }
        }
        // custom page
        ItemStack custom = ItemStack.of(GUIChameleonPresets.CUSTOM.material(), 1);
        ItemMeta customMeta = custom.getItemMeta();
        customMeta.displayName(Component.text("Custom presets"));
        custom.setItemMeta(customMeta);
        boxes[GUIChameleonPresets.CUSTOM.slot()] = custom;
        // page one
        ItemStack page = ItemStack.of(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page.getItemMeta();
        one.displayName(Component.text(plugin.getLanguage().getString("BUTTON_PAGE_1")));
        page.setItemMeta(one);
        boxes[GUIChameleonPoliceBoxes.GO_TO_PAGE_1.slot()] = page;
        // back
        ItemStack back = ItemStack.of(GUIChameleonPoliceBoxes.BACK.material(), 1);
        ItemMeta but = back.getItemMeta();
        but.displayName(Component.text("Back"));
        back.setItemMeta(but);
        boxes[GUIChameleonPoliceBoxes.BACK.slot()] = back;
        // Cancel / close
        ItemStack close = ItemStack.of(GUIChameleonPoliceBoxes.CLOSE.material(), 1);
        ItemMeta can = close.getItemMeta();
        can.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(can);
        boxes[GUIChameleonPoliceBoxes.CLOSE.slot()] = close;
        return boxes;
    }
}
