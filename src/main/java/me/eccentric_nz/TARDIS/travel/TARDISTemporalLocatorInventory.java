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
package me.eccentric_nz.TARDIS.travel;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUITemporalLocator;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

/**
 * Time travel is, as the name suggests, the (usually controlled) process of travelling through time, even in a
 * non-linear direction. In the 26th century individuals who time travel are sometimes known as persons of meta-temporal
 * displacement.
 *
 * @author eccentric_nz
 */
public class TARDISTemporalLocatorInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public TARDISTemporalLocatorInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Temporal Locator", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the Temporal Locator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] clocks = new ItemStack[27];
        for (GUITemporalLocator clock : GUITemporalLocator.values()) {
            ItemStack is = ItemStack.of(clock.getMaterial(), 1);
            if (clock.ordinal() < 4) {
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(plugin.getLanguage().getString(clock.toString())));
            } else {
                is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(clock.getName()));
            }
            if (clock.getLore().contains("~")) {
                ItemLore.Builder lore = ItemLore.lore();
                for (String s : clock.getLore().split("~")) {
                    lore.addLine(Component.text(s));
                }
                is.setData(DataComponentTypes.LORE, lore.build());
            } else {
                is.setData(DataComponentTypes.LORE, ItemLore.lore().addLine(Component.text(clock.getLore())).build());
            }
            is.setData(DataComponentTypes.ITEM_MODEL, clock.getModel());
            clocks[clock.getSlot()] = is;
        }
        return clocks;
    }
}
