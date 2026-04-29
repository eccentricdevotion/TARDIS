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
package me.eccentric_nz.tardischemistry.reducer;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ReducerInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final Inventory inventory;

    public ReducerInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Material reducer", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = ItemStack.of(GUIChemistry.INFO.material(), 1);
        info.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Info"));
        info.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Reduce a substance to its"),
                Component.text("component elements."),
                Component.text("Place an item in the first slot,"),
                Component.text("then click the reduce button.")
        )));
        info.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.INFO.key());
        stack[GUIChemistry.INFO.slot()] = info;
        // check formula
        ItemStack check = ItemStack.of(GUIChemistry.REDUCE.material(), 1);
        check.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Reduce"));
        check.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.REDUCE.key());
        stack[GUIChemistry.REDUCE.slot()] = check;
        // close
        stack[GUIChemistry.CLOSE.slot()] = GUIItemFactory.close();;
        return stack;
    }
}
