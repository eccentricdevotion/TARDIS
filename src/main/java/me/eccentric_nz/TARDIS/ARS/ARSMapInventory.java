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
package me.eccentric_nz.TARDIS.ARS;

import io.papermc.paper.datacomponent.DataComponentTypes;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUIMap;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * During his exile on Earth, the Third Doctor altered the TARDIS' Architectural Configuration software to relocate the
 * console outside the ship (as it was too big to go through the doors), allowing him to work on it in his lab.
 *
 * @author eccentric_nz
 */
public class ARSMapInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ARSMapInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("TARDIS Map", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Architectural Reconfiguration System Map.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        // direction pad
        // up
        ItemStack pad_up = ItemStack.of(GUIMap.BUTTON_UP.material(), 1);
        pad_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_UP", "Up")));
        // down
        ItemStack pad_down = ItemStack.of(GUIMap.BUTTON_DOWN.material(), 1);
        pad_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_DOWN", "Down")));
        // left
        ItemStack pad_left = ItemStack.of(GUIMap.BUTTON_LEFT.material(), 1);
        pad_left.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEFT", "Left")));
        // right
        ItemStack pad_right = ItemStack.of(GUIMap.BUTTON_RIGHT.material(), 1);
        pad_right.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_RIGHT", "Right")));
        // level selected
        ItemStack level_sel = ItemStack.of(GUIMap.BUTTON_LEVEL.material(), 1);
        level_sel.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL", "Main level")));
        // level top
        ItemStack level_top = ItemStack.of(GUIMap.BUTTON_LEVEL_T.material(), 1);
        level_top.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_T", "Top level")));
        // level top
        ItemStack level_bot = ItemStack.of(GUIMap.BUTTON_LEVEL_B.material(), 1);
        level_bot.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_LEVEL_B", "Bottom level")));
        // stone
        ItemStack black = ItemStack.of(GUIMap.BUTTON_MAP_ON.material(), 1);
        black.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP", "Load map")));
        // load map
        ItemStack loader = ItemStack.of(GUIMap.BUTTON_MAP.material(), 1);
        loader.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_MAP_NO", "Load the map!")));
        // close
        ItemStack close = GUIItemFactory.close();
        // transmat
        ItemStack transmat = ItemStack.of(GUIMap.BUTTON_TRANSMAT.material(), 1);
        transmat.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_TRANSMAT", "Transmat")));
        // where am I?
        ItemStack where = ItemStack.of(GUIMap.BUTTON_WHERE.material(), 1);
        where.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString("BUTTON_WHERE", "Where am I?")));

        return new ItemStack[]{
                null, pad_up, null, null, black, black, black, black, black,
                pad_left, loader, pad_right, null, black, black, black, black, black,
                null, pad_down, null, null, black, black, black, black, black,
                level_bot, level_sel, level_top, null, black, black, black, black, black,
                null, null, null, null, black, black, black, black, black,
                close, transmat, where, null, null, null, null, null, null
        };
    }
}
