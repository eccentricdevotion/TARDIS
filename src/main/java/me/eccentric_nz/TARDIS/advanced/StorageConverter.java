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
package me.eccentric_nz.TARDIS.advanced;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.CustomModelData;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.custommodels.keys.CircuitVariant;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;

public class StorageConverter {

    public static ItemStack[] updateDisks(String serialized) {
        try {
            ItemStack[] stacks = SerializeInventory.itemStacksFromString(serialized);
            // convert stacks to component custom names
            for (ItemStack is : stacks) {
                if (is != null) {
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
            return stacks;
        } catch (IOException ignored) {
        }
        return null;
    }

    public static ItemStack[] updateCircuits(String serialized) {
        try {
            ItemStack[] stacks = SerializeInventory.itemStacksFromString(serialized);
            // convert stacks to component custom names
            for (ItemStack is : stacks) {
                if (is != null && is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                    Component component = is.getData(DataComponentTypes.CUSTOM_NAME);
                    // strip color codes
                    String stripped = ComponentUtils.stripColour(component);
                    if (!component.children().isEmpty()) {
                        stripped = ComponentUtils.stripColour(component.children().getFirst());
                    }
                    is.setData(DataComponentTypes.CUSTOM_NAME, Component.text(stripped));
                    if (is.getType() == Material.GLOWSTONE_DUST) {
                        is.setData(DataComponentTypes.CUSTOM_MODEL_DATA, CustomModelData.customModelData()
                                .addFloats(CircuitVariant.fromDisplayName(stripped).getFloats())
                                .build());
                    }
                    is.unsetData(DataComponentTypes.ITEM_MODEL);
                    is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                            .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                            .hideTooltip(true)
                            .build());
                }
            }
            return stacks;
        } catch (Exception ignored) {
        }
        return null;
    }
}
