package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodeldata.GUILights;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISLightEmittingInventory {

    private final TARDIS plugin;
    private final ItemStack[] GUI;

    public TARDISLightEmittingInventory(TARDIS plugin) {
        this.plugin = plugin;
        this.GUI = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[27];
        // 0 -> lightEmitting.size() - 1
        for (int i = 0; i < Sequences.LIGHT_EMITTING.size(); i++) {
            ItemStack light = new ItemStack(Sequences.LIGHT_EMITTING.get(i), 1);
            stacks[i] = light;
        }
        // back button
        ItemStack back = new ItemStack(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName("Back");
        bk.setItemModel(GUIChameleonTemplate.BACK_HELP.key());
        back.setItemMeta(bk);
        stacks[24] = back;
        // 26 close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setItemModel(GUILights.CLOSE.key());
        close.setItemMeta(clim);
        stacks[26] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
