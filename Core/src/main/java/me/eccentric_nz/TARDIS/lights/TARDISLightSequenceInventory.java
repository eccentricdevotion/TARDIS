package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodeldata.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodeldata.GUILights;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISLightSequenceInventory {

    private final TARDIS plugin;
    private final int id;
    private final ItemStack[] GUI;

    public TARDISLightSequenceInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.GUI = getItemStack();
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[45];
        // 4 info
        ItemStack lights = new ItemStack(GUILights.SEQUENCE_INFO.material(), 1);
        ItemMeta sim = lights.getItemMeta();
        sim.setDisplayName("TARDIS Light Sequence");
        sim.setLore(List.of("Click a block in", "the second row to", "change its colour."));
        sim.setCustomModelData(GUILights.SEQUENCE_INFO.customModelData());
        lights.setItemMeta(sim);
        stacks[GUILights.SEQUENCE_INFO.slot()] = lights;
        // get light sequence, delay, level from light prefs
        ResultSetLightPrefs rs = new ResultSetLightPrefs(plugin);
        if (rs.fromID(id)) {
            // 9->17 lights (cycle)
            String[] sequence = rs.getSequence().split(":");
            for (int i = 9; i < 18; i++) {
                Material material = Material.valueOf(sequence[i - 9] + "_WOOL");
                ItemStack wool = new ItemStack(material, 1);
                stacks[i] = wool;
            }
            // 18->26 delay (cycle)
            String[] delays = rs.getDelays().split(":");
            for (int i = 18; i < 27; i++) {
                int amount = TARDISNumberParsers.parseInt(delays[i - 18]);
                if (amount < 5) {
                    amount = 5;
                }
                ItemStack delay = new ItemStack(Material.COAL_BLOCK, 1);
                ItemMeta dim = delay.getItemMeta();
                dim.setDisplayName("Display time");
                dim.setCustomModelData(GUILights.DELAY.customModelData());
                delay.setItemMeta(dim);
                delay.setAmount(amount);
                stacks[i] = delay;
            }
            // 27->35 levels (cycle)
            String[] levels = rs.getLevels().split(":");
            for (int i = 27; i < 36; i++) {
                int amount = TARDISNumberParsers.parseInt(levels[i - 27]);
                if (amount < 1) {
                    amount = 15;
                }
                ItemStack level = new ItemStack(Material.COAL_BLOCK, 1);
                ItemMeta lim = level.getItemMeta();
                lim.setDisplayName("Light level");
                lim.setCustomModelData(GUILights.LEVEL.customModelData());
                level.setItemMeta(lim);
                level.setAmount(amount);
                stacks[i] = level;
            }
        }
        // 36 preset (cycle)
        ItemStack preset = new ItemStack(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        ItemMeta pim = preset.getItemMeta();
        pim.setDisplayName("Preset Sequence");
        pim.setLore(List.of("Click to cycle through", "various light sequences.", "1"));
        pim.setCustomModelData(GUILights.BUTTON_LIGHT_SEQUENCE.customModelData());
        preset.setItemMeta(pim);
        stacks[36] = preset;
        // 40 save
        ItemStack save = new ItemStack(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        ItemMeta se = save.getItemMeta();
        se.setDisplayName("Save Sequence");
        se.setCustomModelData(GUIChameleonConstructor.SAVE_CONSTRUCT.customModelData());
        save.setItemMeta(se);
        stacks[40] = save;
        // back button
        ItemStack back = new ItemStack(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.setDisplayName("Back");
        bk.setCustomModelData(GUIChameleonTemplate.BACK_HELP.customModelData());
        back.setItemMeta(bk);
        stacks[42] = back;
        // 44 close
        ItemStack close = new ItemStack(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.setDisplayName(plugin.getLanguage().getString("BUTTON_CLOSE"));
        clim.setCustomModelData(GUILights.CLOSE.customModelData());
        close.setItemMeta(clim);
        stacks[44] = close;
        return stacks;
    }

    public ItemStack[] getGUI() {
        return GUI;
    }
}
