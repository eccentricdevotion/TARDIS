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
package me.eccentric_nz.TARDIS.commands.tardis;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import io.papermc.paper.datacomponent.item.TooltipDisplay;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ItemCommand {

    private final TARDIS plugin;

    public ItemCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void update(Player player, String which) {
        if (!which.equalsIgnoreCase("hand") && !which.equalsIgnoreCase("inventory") && !which.equalsIgnoreCase("cell")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_ITEM");
            return;
        }
        switch (which.toLowerCase(Locale.ROOT)) {
            case "hand" -> {
                ItemStack inHand = player.getInventory().getItemInMainHand();
                if (!inHand.hasData(DataComponentTypes.CUSTOM_NAME)) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_IN_HAND");
                    return;
                }
                Component component = inHand.getData(DataComponentTypes.CUSTOM_NAME);
                // strip color codes
                String stripped = ComponentUtils.stripColour(component);
                if (!component.children().isEmpty()) {
                    stripped = ComponentUtils.stripColour(component.children().getFirst());
                }
                // look up display name
                RecipeItem recipeItem = RecipeItem.getByName(stripped);
                if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                    inHand.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(stripped));
                    inHand.unsetData(DataComponentTypes.ITEM_MODEL);
                    player.updateInventory();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_UPDATED");
                }
            }
            case "inventory" -> {
                int i = 0;
                for (ItemStack is : player.getInventory()) {
                    if (is != null && is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                        Component component = is.getData(DataComponentTypes.CUSTOM_NAME);
                        // strip color codes
                        String stripped = ComponentUtils.stripColour(component);
                        if (!component.children().isEmpty()) {
                            stripped = ComponentUtils.stripColour(component.children().getFirst());
                        }
                        // look up display name
                        RecipeItem recipeItem = RecipeItem.getByName(stripped);
                        if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                            is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(stripped));
                            is.unsetData(DataComponentTypes.ITEM_MODEL);
                            i++;
                        }
                    }
                }
                if (i > 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEMS_UPDATED", "" + i);
                    player.updateInventory();
                }
            }
            case "cell" -> {
                int i = 0;
                for (ItemStack is : player.getInventory()) {
                    if (is != null) {
                        if (is.getType() != Material.BUCKET) {
                            continue;
                        }
                        if (is.hasData(DataComponentTypes.CUSTOM_NAME)) {
                            Component component = is.getData(DataComponentTypes.CUSTOM_NAME);
                            // strip color codes
                            String stripped = ComponentUtils.stripColour(component);
                            if (!component.children().isEmpty()) {
                                stripped = ComponentUtils.stripColour(component.children().getFirst());
                            }
                            // look up display name
                            RecipeItem recipeItem = RecipeItem.getByName(stripped);
                            if (recipeItem.equals(RecipeItem.ARTRON_STORAGE_CELL)) {
                                is.setData(DataComponentTypes.CUSTOM_NAME, ComponentUtils.toWhite(stripped));
                                is.unsetData(DataComponentTypes.ITEM_MODEL);
                                if (is.hasData(DataComponentTypes.LORE)) {
                                    // get / set lore
                                    List<Component> lore = new ArrayList<>(is.getData(DataComponentTypes.LORE).lines());
                                    int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
                                    if (level < 0) {
                                        level = 0;
                                    }
                                    lore.set(0, Component.text("Charge Level"));
                                    lore.set(1, Component.text(level));
                                    is.setData(DataComponentTypes.LORE, ItemLore.lore(lore));
                                    if (level > 0) {
                                        is.setData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE, true);
                                    } else {
                                        is.unsetData(DataComponentTypes.ENCHANTMENT_GLINT_OVERRIDE);
                                    }
                                    is.setData(DataComponentTypes.TOOLTIP_DISPLAY, TooltipDisplay.tooltipDisplay()
                                            .addHiddenComponents(DataComponentTypes.ATTRIBUTE_MODIFIERS)
                                            .hideTooltip(true)
                                            .build());
                                }
                                i++;
                            }
                        }
                    }
                }
                if (i > 0) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEMS_UPDATED_CELL", "" + i);
                    player.updateInventory();
                }
            }
            default -> {
            }
        }
    }
}
