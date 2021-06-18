/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.chemistry.constructor;

import me.eccentric_nz.tardis.custommodeldata.GuiChemistry;
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
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        assert info_im != null;
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Add or subtract protons,", "neutrons and electrons to", "construct an atomic element."));
        info_im.setCustomModelData(GuiChemistry.INFO.getCustomModelData());
        info.setItemMeta(info_im);
        stack[9] = info;
        // proton count
        ItemStack p_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta pzim = p_zero.getItemMeta();
        assert pzim != null;
        pzim.setDisplayName("0");
        pzim.setCustomModelData(26);
        p_zero.setItemMeta(pzim);
        stack[3] = p_zero;
        // protons
        ItemStack protons = new ItemStack(Material.BOWL, 1);
        ItemMeta pim = protons.getItemMeta();
        assert pim != null;
        pim.setDisplayName("Protons");
        pim.setCustomModelData(GuiChemistry.PROTONS.getCustomModelData());
        protons.setItemMeta(pim);
        stack[4] = protons;
        // proton down
        ItemStack proton_down = new ItemStack(Material.ARROW, 1);
        ItemMeta pdim = proton_down.getItemMeta();
        assert pdim != null;
        pdim.setDisplayName("-");
        pdim.setCustomModelData(GuiChemistry.MINUS.getCustomModelData());
        proton_down.setItemMeta(pdim);
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = new ItemStack(Material.ARROW, 1);
        ItemMeta puim = proton_up.getItemMeta();
        assert puim != null;
        puim.setDisplayName("+");
        puim.setCustomModelData(GuiChemistry.PLUS.getCustomModelData());
        proton_up.setItemMeta(puim);
        stack[6] = proton_up;
        // neutron count
        ItemStack n_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta nzim = n_zero.getItemMeta();
        assert nzim != null;
        nzim.setDisplayName("0");
        nzim.setCustomModelData(26);
        n_zero.setItemMeta(nzim);
        stack[12] = n_zero;
        // neutrons
        ItemStack neutrons = new ItemStack(Material.BOWL, 1);
        ItemMeta nim = neutrons.getItemMeta();
        assert nim != null;
        nim.setDisplayName("Neutrons");
        nim.setCustomModelData(GuiChemistry.NEUTRONS.getCustomModelData());
        neutrons.setItemMeta(nim);
        stack[13] = neutrons;
        // neutron down
        ItemStack neutron_down = new ItemStack(Material.ARROW, 1);
        ItemMeta ndim = neutron_down.getItemMeta();
        assert ndim != null;
        ndim.setDisplayName("-");
        ndim.setCustomModelData(GuiChemistry.MINUS.getCustomModelData());
        neutron_down.setItemMeta(ndim);
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = new ItemStack(Material.ARROW, 1);
        ItemMeta nuim = neutron_up.getItemMeta();
        assert nuim != null;
        nuim.setDisplayName("+");
        nuim.setCustomModelData(GuiChemistry.PLUS.getCustomModelData());
        neutron_up.setItemMeta(nuim);
        stack[15] = neutron_up;
        // electron count
        ItemStack e_zero = new ItemStack(Material.PAPER, 1);
        ItemMeta ezim = e_zero.getItemMeta();
        assert ezim != null;
        ezim.setDisplayName("0");
        ezim.setCustomModelData(26);
        e_zero.setItemMeta(ezim);
        stack[21] = e_zero;
        // electrons
        ItemStack electrons = new ItemStack(Material.BOWL, 1);
        ItemMeta eim = electrons.getItemMeta();
        assert eim != null;
        eim.setDisplayName("Electrons");
        eim.setCustomModelData(GuiChemistry.ELECTRONS.getCustomModelData());
        electrons.setItemMeta(eim);
        stack[22] = electrons;
        // electron down
        ItemStack electron_down = new ItemStack(Material.ARROW, 1);
        ItemMeta edim = electron_down.getItemMeta();
        assert edim != null;
        edim.setDisplayName("-");
        edim.setCustomModelData(GuiChemistry.MINUS.getCustomModelData());
        electron_down.setItemMeta(edim);
        stack[23] = electron_down;
        // electron up
        ItemStack electron_up = new ItemStack(Material.ARROW, 1);
        ItemMeta euim = electron_up.getItemMeta();
        assert euim != null;
        euim.setDisplayName("+");
        euim.setCustomModelData(GuiChemistry.PLUS.getCustomModelData());
        electron_up.setItemMeta(euim);
        stack[24] = electron_up;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
