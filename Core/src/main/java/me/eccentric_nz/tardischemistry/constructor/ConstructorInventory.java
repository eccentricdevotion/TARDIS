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
package me.eccentric_nz.tardischemistry.constructor;

import me.eccentric_nz.TARDIS.custommodels.GUIChemistry;
import me.eccentric_nz.TARDIS.custommodels.keys.HandlesVariant;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;
import java.util.List;

public class ConstructorInventory {

    private final ItemStack[] menu;

    public ConstructorInventory() {
        menu = getItemStack();
    }

    private ItemStack[] getItemStack() {
        ItemStack[] stack = new ItemStack[27];
        // info
        ItemStack info = new ItemStack(GUIChemistry.INFO.material(), 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(List.of("Add or subtract protons,", "neutrons and electrons to", "construct an atomic element."));
        info_im.setItemModel(GUIChemistry.INFO.key());
        info.setItemMeta(info_im);
        stack[9] = info;
        // proton count
        ItemStack p_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta pzim = p_zero.getItemMeta();
        pzim.setDisplayName("0");
        pzim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        p_zero.setItemMeta(pzim);
        stack[3] = p_zero;
        // protons
        ItemStack protons = new ItemStack(GUIChemistry.PROTONS.material(), 1);
        ItemMeta pim = protons.getItemMeta();
        pim.setDisplayName("Protons");
        pim.setItemModel(GUIChemistry.PROTONS.key());
        protons.setItemMeta(pim);
        stack[GUIChemistry.PROTONS.slot()] = protons;
        // proton down
        ItemStack proton_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta pdim = proton_down.getItemMeta();
        pdim.setDisplayName("-");
        pdim.setItemModel(GUIChemistry.MINUS.key());
        proton_down.setItemMeta(pdim);
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta puim = proton_up.getItemMeta();
        puim.setDisplayName("+");
        puim.setItemModel(GUIChemistry.PLUS.key());
        proton_up.setItemMeta(puim);
        stack[6] = proton_up;
        // neutron count
        ItemStack n_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta nzim = n_zero.getItemMeta();
        nzim.setDisplayName("0");
        nzim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        n_zero.setItemMeta(nzim);
        stack[12] = n_zero;
        // neutrons
        ItemStack neutrons = new ItemStack(GUIChemistry.NEUTRONS.material(), 1);
        ItemMeta nim = neutrons.getItemMeta();
        nim.setDisplayName("Neutrons");
        nim.setItemModel(GUIChemistry.NEUTRONS.key());
        neutrons.setItemMeta(nim);
        stack[GUIChemistry.NEUTRONS.slot()] = neutrons;
        // neutron down
        ItemStack neutron_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta ndim = neutron_down.getItemMeta();
        ndim.setDisplayName("-");
        ndim.setItemModel(GUIChemistry.MINUS.key());
        neutron_down.setItemMeta(ndim);
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta nuim = neutron_up.getItemMeta();
        nuim.setDisplayName("+");
        nuim.setItemModel(GUIChemistry.PLUS.key());
        neutron_up.setItemMeta(nuim);
        stack[15] = neutron_up;
        // electron count
        ItemStack e_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta ezim = e_zero.getItemMeta();
        ezim.setDisplayName("0");
        ezim.setItemModel(HandlesVariant.HANDLES_NUMBER_ZERO.getKey());
        e_zero.setItemMeta(ezim);
        stack[21] = e_zero;
        // electrons
        ItemStack electrons = new ItemStack(GUIChemistry.ELECTRONS.material(), 1);
        ItemMeta eim = electrons.getItemMeta();
        eim.setDisplayName("Electrons");
        eim.setItemModel(GUIChemistry.ELECTRONS.key());
        electrons.setItemMeta(eim);
        stack[GUIChemistry.ELECTRONS.slot()] = electrons;
        // electron down
        ItemStack electron_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta edim = electron_down.getItemMeta();
        edim.setDisplayName("-");
        edim.setItemModel(GUIChemistry.MINUS.key());
        electron_down.setItemMeta(edim);
        stack[GUIChemistry.MINUS.slot()] = electron_down;
        // electron up
        ItemStack electron_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta euim = electron_up.getItemMeta();
        euim.setDisplayName("+");
        euim.setItemModel(GUIChemistry.PLUS.key());
        electron_up.setItemMeta(euim);
        stack[GUIChemistry.PLUS.slot()] = electron_up;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
