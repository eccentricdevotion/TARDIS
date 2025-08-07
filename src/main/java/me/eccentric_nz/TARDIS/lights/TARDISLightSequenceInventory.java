/*
 * Copyright (C) 2025 eccentric_nz
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
package me.eccentric_nz.TARDIS.lights;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class TARDISLightSequenceInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Inventory inventory;

    public TARDISLightSequenceInventory(TARDIS plugin, int id) {
        this.plugin = plugin;
        this.id = id;
        this.inventory = plugin.getServer().createInventory(this, 45, Component.text("TARDIS Light Sequence", NamedTextColor.DARK_RED));
        this.inventory.setContents(getItemStack());
    }

    @Override
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Constructs an inventory for the TARDIS Lights GUI.
     *
     * @return an Array of itemStacks (an inventory)
     */
    private ItemStack[] getItemStack() {
        ItemStack[] stacks = new ItemStack[45];
        // 4 info
        ItemStack lights = ItemStack.of(GUILights.SEQUENCE_INFO.material(), 1);
        ItemMeta sim = lights.getItemMeta();
        sim.displayName(Component.text("TARDIS Light Sequence"));
        sim.lore(List.of(
                Component.text("Click a block in"),
                Component.text("the second row to"),
                Component.text("change its colour.")
        ));
        lights.setItemMeta(sim);
        stacks[GUILights.SEQUENCE_INFO.slot()] = lights;
        // get light sequence, delay, level from light prefs
        ResultSetLightPrefs rs = new ResultSetLightPrefs(plugin);
        if (rs.fromID(id)) {
            // 9->17 lights (cycle)
            String[] sequence = rs.getSequence().split(":");
            for (int i = 9; i < 18; i++) {
                Material material = Material.valueOf(sequence[i - 9] + "_WOOL");
                ItemStack wool = ItemStack.of(material, 1);
                stacks[i] = wool;
            }
            // 18->26 delay (cycle)
            String[] delays = rs.getDelays().split(":");
            for (int i = 18; i < 27; i++) {
                int amount = TARDISNumberParsers.parseInt(delays[i - 18]);
                if (amount < 5) {
                    amount = 5;
                }
                ItemStack delay = ItemStack.of(Material.COAL_BLOCK, 1);
                ItemMeta dim = delay.getItemMeta();
                dim.displayName(Component.text("Display time"));
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
                ItemStack level = ItemStack.of(Material.COAL_BLOCK, 1);
                ItemMeta lim = level.getItemMeta();
                lim.displayName(Component.text("Light level"));
                level.setItemMeta(lim);
                level.setAmount(amount);
                stacks[i] = level;
            }
        }
        // 36 preset (cycle)
        ItemStack preset = ItemStack.of(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        ItemMeta pim = preset.getItemMeta();
        pim.displayName(Component.text("Preset Sequence"));
        pim.lore(List.of(
                Component.text("Click to cycle through"),
                Component.text("various light sequences."),
                Component.text("1")
        ));
        preset.setItemMeta(pim);
        stacks[36] = preset;
        // 40 save
        ItemStack save = ItemStack.of(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        ItemMeta se = save.getItemMeta();
        se.displayName(Component.text("Save Sequence"));
        save.setItemMeta(se);
        stacks[40] = save;
        // back button
        ItemStack back = ItemStack.of(GUIChameleonTemplate.BACK_HELP.material(), 1);
        ItemMeta bk = back.getItemMeta();
        bk.displayName(Component.text("Back"));
        back.setItemMeta(bk);
        stacks[42] = back;
        // 44 close
        ItemStack close = ItemStack.of(GUILights.CLOSE.material(), 1);
        ItemMeta clim = close.getItemMeta();
        clim.displayName(Component.text(plugin.getLanguage().getString("BUTTON_CLOSE", "Close")));
        close.setItemMeta(clim);
        stacks[44] = close;
        return stacks;
    }
}
