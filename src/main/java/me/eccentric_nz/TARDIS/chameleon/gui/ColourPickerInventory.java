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
import io.papermc.paper.datacomponent.item.DyedItemColor;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.TARDISConstants;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.keys.ColouredVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ColourPickerInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ColourPickerInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Colour Picker", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        // display
        ItemStack display = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        display.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Colour"));
        display.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Red: 255"),
                Component.text("Green: 255"),
                Component.text("Blue: 255")
        )));
        display.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINT.getKey());
        display.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(255, 255, 255)) // white
                .build());
        display.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        // red
        ItemStack red = ItemStack.of(Material.RED_WOOL, 1);
        red.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Red"));
        // green
        ItemStack green = ItemStack.of(Material.LIME_WOOL, 1);
        green.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Green"));
        // blue
        ItemStack blue = ItemStack.of(Material.LIGHT_BLUE_WOOL, 1);
        blue.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Blue"));
        // red tint
        ItemStack redtint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        redtint.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(255, 0, 0)) // red
                .build());
        redtint.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINT.getKey());
        redtint.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Red"));
        redtint.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        // green tint
        ItemStack greentint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        greentint.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(0, 255, 0)) // green
                .build());
        greentint.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINT.getKey());
        greentint.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Green"));
        greentint.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        // blue tint
        ItemStack bluetint = ItemStack.of(Material.LEATHER_HORSE_ARMOR, 1);
        bluetint.setData(DataComponentTypes.DYED_COLOR, DyedItemColor.dyedItemColor()
                .color(Color.fromRGB(0, 0, 255)) // blue
                .build());
        bluetint.setData(DataComponentTypes.ITEM_MODEL, ColouredVariant.TINT.getKey());
        bluetint.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Blue"));
        bluetint.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                .addHiddenComponents(TARDISConstants.HIDE)
                .build());
        // less
        ItemStack less = ItemStack.of(Material.ARROW, 1);
        less.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Less"));
        // more
        ItemStack more = ItemStack.of(Material.ARROW, 1);
        more.setData(DataComponentTypes.CUSTOM_NAME, Component.text("More"));
        // select
        ItemStack select = ItemStack.of(Material.BOWL, 1);
        select.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Select colour"));
        // close
        ItemStack close = GUIItemFactory.close();
        return new ItemStack[]{
                null, null, null, null, display, null, null, null, null,
                null, null, null, null, null, null, null, null, null,
                red, null, less, null, redtint, null, more, null, null,
                green, null, less, null, greentint, null, more, null, select,
                blue, null, less, null, bluetint, null, more, null, null,
                null, null, null, null, null, null, null, null, close
        };
    }
}
