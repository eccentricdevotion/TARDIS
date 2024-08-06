package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPoliceBoxes;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonPresets;
import me.eccentric_nz.TARDIS.custommodeldata.GUIGeneticManipulator;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
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
            if (monster != Monster.FLYER) {
                ItemStack mon = new ItemStack(monster.getMaterial(), 1);
                ItemMeta ster = mon.getItemMeta();
                ster.setDisplayName(monster.toString());
                int cmd;
                switch (monster) {
                    case K9 -> cmd = 6;
                    case SILENT -> cmd = 4;
                    case TOCLAFANE -> cmd = 3;
                    case VASHTA_NERADA -> cmd = 2;
                    default -> cmd = 1;
                }
                ster.setCustomModelData(cmd);
                mon.setItemMeta(ster);
                stacks[i] = mon;
                i++;
            }
        }
        // page one
        ItemStack page1 = new ItemStack(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.material(), 1);
        ItemMeta one = page1.getItemMeta();
        one.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_1"));
        one.setCustomModelData(GUIChameleonPoliceBoxes.GO_TO_PAGE_1.customModelData());
        page1.setItemMeta(one);
        stacks[42] = page1;
        // page two
        ItemStack page2 = new ItemStack(GUIChameleonPresets.GO_TO_PAGE_2.material(), 1);
        ItemMeta two = page2.getItemMeta();
        two.setDisplayName(plugin.getLanguage().getString("BUTTON_PAGE_2"));
        two.setCustomModelData(GUIChameleonPresets.GO_TO_PAGE_2.customModelData());
        page2.setItemMeta(two);
        stacks[43] = page2;
        // add skins
        ItemStack down = new ItemStack(GUIGeneticManipulator.BUTTON_SKINS.material(), 1);
        ItemMeta load = down.getItemMeta();
        load.setDisplayName("TARDIS Television");
        load.setCustomModelData(GUIGeneticManipulator.BUTTON_SKINS.customModelData());
        down.setItemMeta(load);
        stacks[44] = down;
        // master
        ItemStack the = new ItemStack(GUIGeneticManipulator.BUTTON_MASTER.material(), 1);
        ItemMeta master = the.getItemMeta();
        master.setDisplayName(plugin.getLanguage().getString("BUTTON_MASTER"));
        master.setLore(List.of(plugin.getLanguage().getString("SET_OFF")));
        master.setCustomModelData(GUIGeneticManipulator.BUTTON_MASTER.customModelData());
        the.setItemMeta(master);
        stacks[GUIGeneticManipulator.BUTTON_MASTER.slot()] = the;
        // add buttons
        ItemStack rem = new ItemStack(GUIGeneticManipulator.BUTTON_RESTORE.material(), 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        ove.setCustomModelData(GUIGeneticManipulator.BUTTON_RESTORE.customModelData());
        rem.setItemMeta(ove);
        stacks[GUIGeneticManipulator.BUTTON_RESTORE.slot()] = rem;
        // set
        ItemStack s = new ItemStack(GUIGeneticManipulator.BUTTON_DNA.material(), 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        sim.setCustomModelData(GUIGeneticManipulator.BUTTON_DNA.customModelData());
        s.setItemMeta(sim);
        stacks[GUIGeneticManipulator.BUTTON_DNA.slot()] = s;
        // cancel
        ItemStack can = new ItemStack(GUIGeneticManipulator.BUTTON_CANCEL.material(), 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        cel.setCustomModelData(GUIGeneticManipulator.BUTTON_CANCEL.customModelData());
        can.setItemMeta(cel);
        stacks[GUIGeneticManipulator.BUTTON_CANCEL.slot()] = can;

        return stacks;
    }

    public ItemStack[] getMonsters() {
        return monsters;
    }
}
