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
package me.eccentric_nz.TARDIS.sonic;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUISonicConfigurator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

public class SonicConfiguratorInventory implements InventoryHolder {

    private final Inventory inventory;

    public SonicConfiguratorInventory(TARDIS plugin) {
        this.inventory = plugin.getServer().createInventory(this, 54, Component.text("Sonic Configurator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Sonic Configurator Menu GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {

        ItemStack[] stack = new ItemStack[54];
        for (GUISonicConfigurator gui : GUISonicConfigurator.values()) {
            if (gui.getSlot() != -1) {
                ItemStack is = ItemStack.of(gui.getMaterial(), 1);
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(gui.getName()));
                if (!gui.getLore().isEmpty()) {
                    ItemLore.Builder lore = ItemLore.lore();
                    for (String s : gui.getLore().split("~")) {
                        lore.addLine(Component.text(s));
                    }
                    is.setData(DataComponentTypes.LORE, lore.build());
                }
                stack[gui.getSlot()] = is;
            }
        }
        ItemStack wool = ItemStack.of(GUISonicConfigurator.WAITING.getMaterial(), 1);
        wool.setData(DataComponentTypes.CUSTOM_NAME, Component.text(" "));
        for (int i = 9; i < 18; i++) {
            stack[i] = wool;
        }
        stack[27] = wool;
        ItemStack place = ItemStack.of(GUISonicConfigurator.PLACE_SONIC.getMaterial(), 1);
        place.setData(DataComponentTypes.CUSTOM_NAME, Component.text(GUISonicConfigurator.PLACE_SONIC.getName()));
        stack[36] = place;
        return stack;
    }
}
