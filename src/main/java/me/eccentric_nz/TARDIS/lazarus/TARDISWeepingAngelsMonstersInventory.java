package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIGeneticManipulator;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISWeepingAngelsMonstersInventory {

    private final ItemStack[] monsters;
    private final TARDIS plugin;

    public TARDISWeepingAngelsMonstersInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.monsters = getItemStack();
    }

    /**
     * Constructs an inventory for the Genetic Manipulator GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[54];
        int i = 0;
        for (Monster monster : Monster.values()) {
            ItemStack mon = new ItemStack(monster.getMaterial(), 1);
            ItemMeta ster = mon.getItemMeta();
            ster.setDisplayName(monster.toString());
            GUIGeneticManipulator gui = GUIGeneticManipulator.valueOf(monster.toString());
            ster.setCustomModelData(gui.getCustomModelData());
            mon.setItemMeta(ster);
            stacks[i] = mon;
            i++;
        }
        // page one
        ItemStack page1 = new ItemStack(Material.ARROW, 1);
        ItemMeta one = page1.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setCustomModelData(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.getCustomModelData());
        page1.setItemMeta(one);
        stacks[43] = page1;
        // page two
        ItemStack page2 = new ItemStack(Material.ARROW, 1);
        ItemMeta two = page2.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        two.setCustomModelData(GUIChameleonPresets.GO_TO_PAGE_2.getCustomModelData());
        page2.setItemMeta(two);
        stacks[44] = page2;
        // master
        ItemStack the = new ItemStack(Material.COMPARATOR, 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(List.of(plugin.getLanguage().getString("SET_OFF")));
        master.setCustomModelData(GUIGeneticManipulator.BUTTON_MASTER.getCustomModelData());
        the.setItemMeta(master);
        stacks[45] = the;
        // add buttons
        ItemStack rem = new ItemStack(Material.APPLE, 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        ove.setCustomModelData(GUIGeneticManipulator.BUTTON_RESTORE.getCustomModelData());
        rem.setItemMeta(ove);
        stacks[51] = rem;
        // set
        ItemStack s = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        sim.setCustomModelData(GUIGeneticManipulator.BUTTON_DNA.getCustomModelData());
        s.setItemMeta(sim);
        stacks[52] = s;
        // cancel
        ItemStack can = new ItemStack(Material.BOWL, 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        cel.setCustomModelData(GUIGeneticManipulator.BUTTON_CANCEL.getCustomModelData());
        can.setItemMeta(cel);
        stacks[53] = can;

        return stacks;
    }

    public ItemStack[] getMonsters() {
        return monsters;
    }
}
