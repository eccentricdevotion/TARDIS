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
package me.eccentric_nz.TARDIS.commands.tardis;

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISNumberParsers;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Locale;
import java.util.Map;

public class TARDISItemCommand {

    private final TARDIS plugin;

    public TARDISItemCommand(TARDIS plugin) {
        this.plugin = plugin;
    }

    public boolean update(Player player, String[] args) {
        if (args.length < 2) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "TOO_FEW_ARGS");
            return true;
        }
        if (!args[1].equalsIgnoreCase("hand") && !args[1].equalsIgnoreCase("inventory") && !args[1].equalsIgnoreCase("cell")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_ITEM");
            return true;
        }
        switch (args[1].toLowerCase(Locale.ROOT)) {
            case "hand" -> {
                ItemStack inHand = player.getInventory().getItemInMainHand();
                if (!inHand.hasItemMeta()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_IN_HAND");
                    return true;
                }
                ItemMeta im = inHand.getItemMeta();
                if (!im.hasDisplayName()) {
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_IN_HAND");
                    return true;
                }
                Component component = im.displayName();
                // strip color codes
                String stripped = ComponentUtils.stripColour(component);
                if (!component.children().isEmpty()) {
                    stripped = ComponentUtils.stripColour(component.children().getFirst());
                }
                // look up display name
                RecipeItem recipeItem = RecipeItem.getByName(stripped);
                if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                    im.displayName(ComponentUtils.toWhite(stripped));
                    im.setItemModel(null);
                    inHand.setItemMeta(im);
                    player.updateInventory();
                    plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_UPDATED");
                }
            }
            case "inventory" -> {
                int i = 0;
                for (ItemStack is : player.getInventory()) {
                    if (is != null && is.hasItemMeta()) {
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            Component component = im.displayName();
                            // strip color codes
                            String stripped = ComponentUtils.stripColour(component);
                            if (!component.children().isEmpty()) {
                                stripped = ComponentUtils.stripColour(component.children().getFirst());
                            }
                            // look up display name
                            RecipeItem recipeItem = RecipeItem.getByName(stripped);
                            if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                                im.displayName(ComponentUtils.toWhite(stripped));
                                im.setItemModel(null);
                                is.setItemMeta(im);
                                i++;
                            }
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
                    if (is != null && is.hasItemMeta()) {
                        if (is.getType() != Material.BUCKET) {
                            continue;
                        }
                        ItemMeta im = is.getItemMeta();
                        if (im.hasDisplayName()) {
                            Component component = im.displayName();
                            // strip color codes
                            String stripped = ComponentUtils.stripColour(component);
                            if (!component.children().isEmpty()) {
                                stripped = ComponentUtils.stripColour(component.children().getFirst());
                            }
                            // look up display name
                            RecipeItem recipeItem = RecipeItem.getByName(stripped);
                            if (recipeItem.equals(RecipeItem.ARTRON_STORAGE_CELL)) {
                                im.displayName(ComponentUtils.toWhite(stripped));
                                im.setItemModel(null);
                                if (im.hasLore()) {
                                    // get / set lore
                                    List<Component> lore = im.lore();
                                    int level = TARDISNumberParsers.parseInt(ComponentUtils.stripColour(lore.get(1)));
                                    if (level < 0) {
                                        level = 0;
                                    }
                                    lore.set(0, Component.text("Charge Level"));
                                    lore.set(1, Component.text(level));
                                    im.lore(lore);
                                    im.setEnchantmentGlintOverride(level > 0);
                                    im.addItemFlags(ItemFlag.values());
                                    im.setAttributeModifiers(Multimaps.forMap(Map.of()));
                                }
                                is.setItemMeta(im);
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
        return true;
    }
}
