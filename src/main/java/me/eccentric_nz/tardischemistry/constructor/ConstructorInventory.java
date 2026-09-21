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
package me.eccentric_nz.tardischemistry.constructor;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class ConstructorInventory implements InventoryHolder {

    private final Inventory inventory;

    public ConstructorInventory(TARDIS plugin) {
        this.inventory = plugin.getServer().createInventory(this, 27, Component.text("Element constructor", NamedTextColor.DARK_RED));
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
                Component.text("Add or subtract protons,"),
                Component.text("neutrons and electrons to"),
                Component.text("construct an atomic element.")
        )));
        info.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.INFO.key());
        stack[9] = info;
        // proton count
        ItemStack p_zero = ItemStack.of(Material.PAPER, 1);
        p_zero.setData(DataComponentTypes.CUSTOM_NAME, Component.text("0"));
        p_zero.setData(DataComponentTypes.ITEM_MODEL, HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        stack[3] = p_zero;
        // protons
        ItemStack protons = ItemStack.of(GUIChemistry.PROTONS.material(), 1);
        protons.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Protons"));
        protons.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.PROTONS.key());
        stack[GUIChemistry.PROTONS.slot()] = protons;
        // proton down
        ItemStack proton_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        proton_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text("-"));
        proton_down.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.MINUS.key());
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        proton_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text("+"));
        proton_up.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.PLUS.key());
        stack[6] = proton_up;
        // neutron count
        ItemStack n_zero = ItemStack.of(Material.PAPER, 1);
        n_zero.setData(DataComponentTypes.CUSTOM_NAME, Component.text("0"));
        n_zero.setData(DataComponentTypes.ITEM_MODEL, HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        stack[12] = n_zero;
        // neutrons
        ItemStack neutrons = ItemStack.of(GUIChemistry.NEUTRONS.material(), 1);
        neutrons.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Neutrons"));
        neutrons.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.NEUTRONS.key());
        stack[GUIChemistry.NEUTRONS.slot()] = neutrons;
        // neutron down
        ItemStack neutron_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        neutron_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text("-"));
        neutron_down.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.MINUS.key());
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        neutron_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text("+"));
        neutron_up.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.PLUS.key());
        stack[15] = neutron_up;
        // electron count
        ItemStack e_zero = ItemStack.of(Material.PAPER, 1);
        e_zero.setData(DataComponentTypes.CUSTOM_NAME, Component.text("0"));
        e_zero.setData(DataComponentTypes.ITEM_MODEL, HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        stack[21] = e_zero;
        // electrons
        ItemStack electrons = ItemStack.of(GUIChemistry.ELECTRONS.material(), 1);
        electrons.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Electrons"));
        electrons.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.ELECTRONS.key());
        stack[GUIChemistry.ELECTRONS.slot()] = electrons;
        // electron down
        ItemStack electron_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        electron_down.setData(DataComponentTypes.CUSTOM_NAME, Component.text("-"));
        electron_down.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.MINUS.key());
        stack[GUIChemistry.MINUS.slot()] = electron_down;
        // electron up
        ItemStack electron_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        electron_up.setData(DataComponentTypes.CUSTOM_NAME, Component.text("+"));
        electron_up.setData(DataComponentTypes.ITEM_MODEL, GUIChemistry.PLUS.key());
        stack[GUIChemistry.PLUS.slot()] = electron_up;
        return stack;
    }
}
