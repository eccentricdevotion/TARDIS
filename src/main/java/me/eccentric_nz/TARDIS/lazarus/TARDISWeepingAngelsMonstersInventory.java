package me.eccentric_nz.TARDIS.lazarus;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIGeneticManipulator;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        ItemStack[] eggs = new ItemStack[54];
        int i = 0;
        for (Monster monster : Monster.values()) {
            ItemStack mon = new ItemStack(monster.getMaterial(), 1);
            ItemMeta ster = mon.getItemMeta();
            ster.setDisplayName(monster.getName());
            GUIGeneticManipulator gui = GUIGeneticManipulator.valueOf(monster.toString());
            ster.setCustomModelData(gui.getCustomModelData());
            mon.setItemMeta(ster);
            eggs[i] = mon;
            i++;
        }
        // add buttons
        ItemStack rem = new ItemStack(Material.APPLE, 1);
        ItemMeta ove = rem.getItemMeta();
        ove.setDisplayName(plugin.getLanguage().getString("BUTTON_RESTORE"));
        ove.setCustomModelData(GUIGeneticManipulator.BUTTON_RESTORE.getCustomModelData());
        rem.setItemMeta(ove);
        eggs[51] = rem;
        // set
        ItemStack s = new ItemStack(Material.WRITABLE_BOOK, 1);
        ItemMeta sim = s.getItemMeta();
        sim.setDisplayName(plugin.getLanguage().getString("BUTTON_DNA"));
        sim.setCustomModelData(GUIGeneticManipulator.BUTTON_DNA.getCustomModelData());
        s.setItemMeta(sim);
        eggs[52] = s;
        // cancel
        ItemStack can = new ItemStack(Material.BOWL, 1);
        ItemMeta cel = can.getItemMeta();
        cel.setDisplayName(plugin.getLanguage().getString("BUTTON_CANCEL"));
        cel.setCustomModelData(GUIGeneticManipulator.BUTTON_CANCEL.getCustomModelData());
        can.setItemMeta(cel);
        eggs[53] = can;
        return eggs;
    }
}
