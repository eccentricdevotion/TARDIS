package me.eccentric_nz.TARDIS.chemistry.constructor;

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
        ItemStack info = new ItemStack(Material.BOWL, 1);
        ItemMeta info_im = info.getItemMeta();
        info_im.setDisplayName("Info");
        info_im.setLore(Arrays.asList("Add or subtract protons,", "neutrons and electrons to", "construct an atomic element."));
        info_im.setCustomModelData(GUIChemistry.INFO.getCustomModelData());
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
        ItemStack protons = new ItemStack(Material.BOWL, 1);
        ItemMeta pim = protons.getItemMeta();
        pim.setDisplayName("Protons");
        pim.setCustomModelData(GUIChemistry.PROTONS.getCustomModelData());
        protons.setItemMeta(pim);
        stack[4] = protons;
        // proton down
        ItemStack proton_down = new ItemStack(Material.ARROW, 1);
        ItemMeta pdim = proton_down.getItemMeta();
        pdim.setDisplayName("-");
        pdim.setCustomModelData(GUIChemistry.MINUS.getCustomModelData());
        proton_down.setItemMeta(pdim);
        stack[5] = proton_down;
        // proton up
        ItemStack proton_up = new ItemStack(Material.ARROW, 1);
        ItemMeta puim = proton_up.getItemMeta();
        puim.setDisplayName("+");
        puim.setCustomModelData(GUIChemistry.PLUS.getCustomModelData());
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
        ItemStack neutrons = new ItemStack(Material.BOWL, 1);
        ItemMeta nim = neutrons.getItemMeta();
        nim.setDisplayName("Neutrons");
        nim.setCustomModelData(GUIChemistry.NEUTRONS.getCustomModelData());
        neutrons.setItemMeta(nim);
        stack[13] = neutrons;
        // neutron down
        ItemStack neutron_down = new ItemStack(Material.ARROW, 1);
        ItemMeta ndim = neutron_down.getItemMeta();
        ndim.setDisplayName("-");
        ndim.setCustomModelData(GUIChemistry.MINUS.getCustomModelData());
        neutron_down.setItemMeta(ndim);
        stack[14] = neutron_down;
        // neutron up
        ItemStack neutron_up = new ItemStack(Material.ARROW, 1);
        ItemMeta nuim = neutron_up.getItemMeta();
        nuim.setDisplayName("+");
        nuim.setCustomModelData(GUIChemistry.PLUS.getCustomModelData());
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
        ItemStack electrons = new ItemStack(Material.BOWL, 1);
        ItemMeta eim = electrons.getItemMeta();
        eim.setDisplayName("Electrons");
        eim.setCustomModelData(GUIChemistry.ELECTRONS.getCustomModelData());
        electrons.setItemMeta(eim);
        stack[22] = electrons;
        // electron down
        ItemStack electron_down = new ItemStack(Material.ARROW, 1);
        ItemMeta edim = electron_down.getItemMeta();
        edim.setDisplayName("-");
        edim.setCustomModelData(GUIChemistry.MINUS.getCustomModelData());
        electron_down.setItemMeta(edim);
        stack[23] = electron_down;
        // electron up
        ItemStack electron_up = new ItemStack(Material.ARROW, 1);
        ItemMeta euim = electron_up.getItemMeta();
        euim.setDisplayName("+");
        euim.setCustomModelData(GUIChemistry.PLUS.getCustomModelData());
        electron_up.setItemMeta(euim);
        stack[24] = electron_up;
        return stack;
    }

    public ItemStack[] getMenu() {
        return menu;
    }
}
