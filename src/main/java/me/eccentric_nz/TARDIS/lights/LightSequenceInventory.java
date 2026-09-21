/*
 * Copyright (C) 2026 eccentric_nz
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

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonConstructor;
import me.eccentric_nz.TARDIS.custommodels.GUIChameleonTemplate;
import me.eccentric_nz.TARDIS.custommodels.GUIItemFactory;
import me.eccentric_nz.TARDIS.custommodels.GUILights;
import me.eccentric_nz.TARDIS.database.resultset.ResultSetLightPrefs;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class LightSequenceInventory implements InventoryHolder {

    private final TARDIS plugin;
    private final int id;
    private final Inventory inventory;

    public LightSequenceInventory(TARDIS plugin, int id) {
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
        lights.setData(DataComponentTypes.CUSTOM_NAME, Component.text("TARDIS Light Sequence"));
        lights.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click a block in"),
                Component.text("the second row to"),
                Component.text("change its colour.")
        )));
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
                delay.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Display time"));
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
                level.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Light level"));
                level.setAmount(amount);
                stacks[i] = level;
            }
        }
        // 36 preset (cycle)
        ItemStack preset = ItemStack.of(GUILights.BUTTON_LIGHT_SEQUENCE.material(), 1);
        preset.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Preset Sequence"));
        preset.setData(DataComponentTypes.LORE, ItemLore.lore(List.of(
                Component.text("Click to cycle through"),
                Component.text("various light sequences."),
                Component.text("1")
        )));
        stacks[36] = preset;
        // 40 save
        ItemStack save = ItemStack.of(GUIChameleonConstructor.SAVE_CONSTRUCT.material(), 1);
        save.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Save Sequence"));
        stacks[40] = save;
        // back button
        ItemStack back = ItemStack.of(GUIChameleonTemplate.BACK_HELP.material(), 1);
        back.setData(DataComponentTypes.CUSTOM_NAME, Component.text("Back"));
        stacks[42] = back;
        // 44 close
        stacks[44] = GUIItemFactory.close();
        return stacks;
    }
}
