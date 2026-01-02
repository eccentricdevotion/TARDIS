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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        ItemMeta info_im = info.getItemMeta();
        info_im.displayName(Component.text("Info"));
        info_im.lore(List.of(
                Component.text("Add or subtract protons,"),
                Component.text("neutrons and electrons to"),
                Component.text("construct an atomic element.")
        ));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[9] = info;
        // proton count
        ItemStack p_zero = ItemStack.of(Material.PAPER, 1);
        ItemMeta pzim = p_zero.getItemMeta();
        pzim.displayName(Component.text("0"));
        pzim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        p_zero.setItemMeta(pzim);
        stack[3] = p_zero;
        // protons
        ItemStack protons = ItemStack.of(GUIChemistry.PROTONS.material(), 1);
        ItemMeta pim = protons.getItemMeta();
        pim.displayName(Component.text("Protons"));
        pim.setItemModel(GUIChemistry.PROTONS.key());
        protons.setItemMeta(pim);
        stack[GUIChemistry.PROTONS.slot()] = protons;
        // proton down
        ItemStack proton_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        ItemMeta pdim = proton_down.getItemMeta();
        pdim.displayName(Component.text("-"));
        pdim.setItemModel(GUIChemistry.MINUS.key());
        proton_down.setItemMeta(pdim);
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        ItemMeta puim = proton_up.getItemMeta();
        puim.displayName(Component.text("+"));
        puim.setItemModel(GUIChemistry.PLUS.key());
        proton_up.setItemMeta(puim);
        stack[6] = proton_up;
        // neutron count
        ItemStack n_zero = ItemStack.of(Material.PAPER, 1);
        ItemMeta nzim = n_zero.getItemMeta();
        nzim.displayName(Component.text("0"));
        nzim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        n_zero.setItemMeta(nzim);
        stack[12] = n_zero;
        // neutrons
        ItemStack neutrons = ItemStack.of(GUIChemistry.NEUTRONS.material(), 1);
        ItemMeta nim = neutrons.getItemMeta();
        nim.displayName(Component.text("Neutrons"));
        nim.setItemModel(GUIChemistry.NEUTRONS.key());
        neutrons.setItemMeta(nim);
        stack[GUIChemistry.NEUTRONS.slot()] = neutrons;
        // neutron down
        ItemStack neutron_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        ItemMeta ndim = neutron_down.getItemMeta();
        ndim.displayName(Component.text("-"));
        ndim.setItemModel(GUIChemistry.MINUS.key());
        neutron_down.setItemMeta(ndim);
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        ItemMeta nuim = neutron_up.getItemMeta();
        nuim.displayName(Component.text("+"));
        nuim.setItemModel(GUIChemistry.PLUS.key());
        neutron_up.setItemMeta(nuim);
        stack[15] = neutron_up;
        // electron count
        ItemStack e_zero = ItemStack.of(Material.PAPER, 1);
        ItemMeta ezim = e_zero.getItemMeta();
        ezim.displayName(Component.text("0"));
        ezim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        e_zero.setItemMeta(ezim);
        stack[21] = e_zero;
        // electrons
        ItemStack electrons = ItemStack.of(GUIChemistry.ELECTRONS.material(), 1);
        ItemMeta eim = electrons.getItemMeta();
        eim.displayName(Component.text("Electrons"));
        eim.setItemModel(GUIChemistry.ELECTRONS.key());
        electrons.setItemMeta(eim);
        stack[GUIChemistry.ELECTRONS.slot()] = electrons;
        // electron down
        ItemStack electron_down = ItemStack.of(GUIChemistry.MINUS.material(), 1);
        ItemMeta edim = electron_down.getItemMeta();
        edim.displayName(Component.text("-"));
        edim.setItemModel(GUIChemistry.MINUS.key());
        electron_down.setItemMeta(edim);
        stack[GUIChemistry.MINUS.slot()] = electron_down;
        // electron up
        ItemStack electron_up = ItemStack.of(GUIChemistry.PLUS.material(), 1);
        ItemMeta euim = electron_up.getItemMeta();
        euim.displayName(Component.text("+"));
        euim.setItemModel(GUIChemistry.PLUS.key());
        electron_up.setItemMeta(euim);
        stack[GUIChemistry.PLUS.slot()] = electron_up;
        return stack;
    }
}
