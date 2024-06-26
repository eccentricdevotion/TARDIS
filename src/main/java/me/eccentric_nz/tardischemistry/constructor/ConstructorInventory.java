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

import me.eccentric_nz.TARDIS.custommodeldata.GUIChemistry;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

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
        info_im.setLore(Arrays.asList("Add or subtract protons,", "neutrons and electrons to", "construct an atomic element."));
        info_im.setCustomModelData(GUIChemistry.INFO.customModelData());
        info.setItemMeta(info_im);
        stack[9] = info;
        // proton count
        ItemStack p_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta pzim = p_zero.getItemMeta();
        pzim.setDisplayName("0");
        pzim.setCustomModelData(26);
        p_zero.setItemMeta(pzim);
        stack[3] = p_zero;
        // protons
        ItemStack protons = new ItemStack(GUIChemistry.PROTONS.material(), 1);
        ItemMeta pim = protons.getItemMeta();
        pim.setDisplayName("Protons");
        pim.setCustomModelData(GUIChemistry.PROTONS.customModelData());
        protons.setItemMeta(pim);
        stack[GUIChemistry.PROTONS.slot()] = protons;
        // proton down
        ItemStack proton_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta pdim = proton_down.getItemMeta();
        pdim.setDisplayName("-");
        pdim.setCustomModelData(GUIChemistry.MINUS.customModelData());
        proton_down.setItemMeta(pdim);
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta puim = proton_up.getItemMeta();
        puim.setDisplayName("+");
        puim.setCustomModelData(GUIChemistry.PLUS.customModelData());
        proton_up.setItemMeta(puim);
        stack[6] = proton_up;
        // neutron count
        ItemStack n_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta nzim = n_zero.getItemMeta();
        nzim.setDisplayName("0");
        nzim.setCustomModelData(26);
        n_zero.setItemMeta(nzim);
        stack[12] = n_zero;
        // neutrons
        ItemStack neutrons = new ItemStack(GUIChemistry.NEUTRONS.material(), 1);
        ItemMeta nim = neutrons.getItemMeta();
        nim.setDisplayName("Neutrons");
        nim.setCustomModelData(GUIChemistry.NEUTRONS.customModelData());
        neutrons.setItemMeta(nim);
        stack[GUIChemistry.NEUTRONS.slot()] = neutrons;
        // neutron down
        ItemStack neutron_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta ndim = neutron_down.getItemMeta();
        ndim.setDisplayName("-");
        ndim.setCustomModelData(GUIChemistry.MINUS.customModelData());
        neutron_down.setItemMeta(ndim);
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta nuim = neutron_up.getItemMeta();
        nuim.setDisplayName("+");
        nuim.setCustomModelData(GUIChemistry.PLUS.customModelData());
        neutron_up.setItemMeta(nuim);
        stack[15] = neutron_up;
        // electron count
        ItemStack e_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta ezim = e_zero.getItemMeta();
        ezim.setDisplayName("0");
        ezim.setCustomModelData(26);
        e_zero.setItemMeta(ezim);
        stack[21] = e_zero;
        // electrons
        ItemStack electrons = new ItemStack(GUIChemistry.ELECTRONS.material(), 1);
        ItemMeta eim = electrons.getItemMeta();
        eim.setDisplayName("Electrons");
        eim.setCustomModelData(GUIChemistry.ELECTRONS.customModelData());
        electrons.setItemMeta(eim);
        stack[GUIChemistry.ELECTRONS.slot()] = electrons;
        // electron down
        ItemStack electron_down = new ItemStack(GUIChemistry.MINUS.material(), 1);
        ItemMeta edim = electron_down.getItemMeta();
        edim.setDisplayName("-");
        edim.setCustomModelData(GUIChemistry.MINUS.customModelData());
        electron_down.setItemMeta(edim);
        stack[GUIChemistry.MINUS.slot()] = electron_down;
        // electron up
        ItemStack electron_up = new ItemStack(GUIChemistry.PLUS.material(), 1);
        ItemMeta euim = electron_up.getItemMeta();
        euim.setDisplayName("+");
        euim.setCustomModelData(GUIChemistry.PLUS.customModelData());
        electron_up.setItemMeta(euim);
        stack[GUIChemistry.PLUS.slot()] = electron_up;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
