/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.blueprints.TARDISPermission;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
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
public class ModelledPresetInventory implements InventoryHolder {

    private final List<String> colours = List.of("Blue", "White", "Orange", "Magenta", "Light Blue", "Yellow", "Lime", "Pink", "Gray", "Light Gray", "Cyan", "Purple", "Brown", "Green", "Red", "Black");
    private final TARDIS plugin;
    private final Player player;
    private final Inventory inventory;

    public ModelledPresetInventory(TARDIS plugin, Player player) {
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
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(s + " Police Box"));
                switch (s) {
                    case "Blue" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BLUE_CLOSED.getKey());
                    case "White" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.WHITE_CLOSED.getKey());
                    case "Orange" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.ORANGE_CLOSED.getKey());
                    case "Magenta" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.MAGENTA_CLOSED.getKey());
                    case "Light Blue" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIGHT_BLUE_CLOSED.getKey());
                    case "Yellow" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.YELLOW_CLOSED.getKey());
                    case "Lime" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIME_CLOSED.getKey());
                    case "Pink" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PINK_CLOSED.getKey());
                    case "Gray" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.GRAY_CLOSED.getKey());
                    case "Light Gray" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.LIGHT_GRAY_CLOSED.getKey());
                    case "Cyan" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.CYAN_CLOSED.getKey());
                    case "Purple" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PURPLE_CLOSED.getKey());
                    case "Brown" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BROWN_CLOSED.getKey());
                    case "Green" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.GREEN_CLOSED.getKey());
                    case "Red" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.RED_CLOSED.getKey());
                    case "Black" -> is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BLACK_CLOSED.getKey());
                }
                boxes[i] = is;
            }
            i++;
        }
        // tennant
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tennant")) {
            ItemStack tennant = ItemStack.of(Material.CYAN_STAINED_GLASS_PANE, 1);
            tennant.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Tennant Era Police Box"));
            tennant.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.TENNANT_CLOSED.getKey());
            boxes[i] = tennant;
            i++;
        }
        // weeping angel
        if (TARDISPermission.hasPermission(player, "tardis.preset.weeping_angel")) {
            ItemStack is = ItemStack.of(Material.GRAY_STAINED_GLASS_PANE, 1);
            is.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Weeping Angel"));
            is.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.WEEPING_ANGEL_CLOSED.getKey());
            boxes[i] = is;
            i++;
        }
        // pandorica
        if (TARDISPermission.hasPermission(player, "tardis.preset.pandorica")) {
            ItemStack pandorica = ItemStack.of(Material.ENDER_PEARL, 1);
            pandorica.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Pandorica"));
            pandorica.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.PANDORICA_CLOSED.getKey());
            boxes[i] = pandorica;
            i++;
        }
        // SIDRAT
        if (TARDISPermission.hasPermission(player, "tardis.preset.sidrat")) {
            ItemStack sidrat = ItemStack.of(Material.GREEN_STAINED_GLASS_PANE, 1);
            sidrat.setData(DataComponentTypes.CUSTOM_NAME, Component.text("SIDRAT"));
            sidrat.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.SIDRAT_CLOSED.getKey());
            boxes[i] = sidrat;
            i++;
        }
        // BATTLE
        if (TARDISPermission.hasPermission(player, "tardis.preset.battle")) {
            ItemStack battle = ItemStack.of(Material.RED_STAINED_GLASS_PANE, 1);
            battle.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Battle TARDIS"));
            battle.setData(DataComponentTypes.ITEM_MODEL, ChameleonVariant.BATTLE_CLOSED.getKey());
            boxes[i] = battle;
            i++;
        }
        if (TARDISPermission.hasPermission(player, "tardis.preset.police_box_tinted")) {
            ItemStack any = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
            any.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Pick a colour Police Box"));
            any.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINTED_CLOSED.getKey());
            boxes[i] = any;
            i++;
        }
        for (String custom : plugin.getCustomModelConfig().getConfigurationSection("models").getKeys(false)) {
            if (i < 50) {
                try {
                    Material cm = Material.valueOf(plugin.getCustomModelConfig().getString("models." + custom + ".item"));
                    ItemStack cis = ItemStack.of(cm);
                    cis.setData(DataComponentTypes.CUSTOM_NAME, Component.text(custom));
                    String key = TARDISStringUtils.toUnderscoredLowercase(custom);
                    cis.setData(DataComponentTypes.ITEM_MODEL, new NamespacedKey(plugin, key + "_closed"));
                    boxes[i] = cis;
                    i++;
                } catch (IllegalArgumentException e) {
                    plugin.debug("Invalid material specified for custom model preset: " + custom + "!");
                }
            }
        }
        // custom page
        ItemStack custom = ItemStack.of(GUIChameleonPresets.CUSTOM.material(), 1);
        custom.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Custom presets"));
        boxes[GUIChameleonPresets.CUSTOM.slot()] = custom;
        // page one
        ItemStack page = ItemStack.of(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        page.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_PAGE_1", "Go to page 1")));
        boxes[GUIChameleonPoliceBoxes.GO_TO_PAGE_1.slot()] = page;
        // back
        ItemStack back = ItemStack.of(GUIChameleonPoliceBoxes.BACK.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        boxes[GUIChameleonPoliceBoxes.BACK.slot()] = back;
        // Cancel / close
        boxes[GUIChameleonPoliceBoxes.CLOSE.slot()] = GUIItemFactory.close();
        return boxes;
    }
}
