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
package me.eccentric_nz.TARDIS.commands.dev;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.advanced.SerializeInventory;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.enumeration.Storage;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FixStorageCommand {

    private final TARDIS plugin;

    public FixStorageCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void convertStacks() {
        try {
            for (Storage storage : Storage.values()) {
                ItemStack[] stacks = SerializeInventory.itemStacksFromString(storage.getEmpty());
                // convert stacks to component display names
                for (ItemStack is : stacks) {
                    if (is != null && is.hasItemMeta()) {
                        if (is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                            Component component = is.getData(DataComponentTypes.CUSTOM_NAME);
                            // strip color codes
                            String stripped = ComponentUtils.stripColour(component);
                            if (!component.children().isEmpty()) {
                                stripped = ComponentUtils.stripColour(component.children().getFirst());
                            }
                            is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(stripped));
                            if (is.getType() == Material.GLOWSTONE_DUST) {
                                is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                                        .addFloats(CircuitVariant.GALLIFREY.getFloats())
                                        .build());
                            }
                            is.unsetData(DataComponentTypes.ITEM_MODEL);
                            is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                                    .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                                    .hideTooltip(true)
                                    .build());
                        }
                    }
                }
                // rewrite serialised string
                String out = SerializeInventory.itemStacksToString(stacks);
                // write the string to file
                String file = plugin.getDataFolder() + File.separator + "storage_" + storage + ".txt";
                try (BufferedWriter bw = new BufferedWriter(new FileWriter(file, false))) {
                    bw.write(out);
                }
            }
        } catch (IOException ignored) {
        }
    }
}
