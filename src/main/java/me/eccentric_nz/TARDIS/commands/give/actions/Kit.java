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
package me.eccentric_nz.TARDIS.commands.give.actions;

import io.papermc.paper.datacomponent.DataComponentTypes;
import io.papermc.paper.datacomponent.item.ItemLore;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

public class Kit {

    private final TARDIS plugin;

    public Kit(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(String item, Player player) {
        ItemStack result;
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else if (plugin.getFigura().getShapedRecipes().containsKey(item)) {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
            result = recipe.getResult();
            if (result.hasData(DataComponentTypes.CUSTOM_NAME)) {
                String dn = ComponentUtils.stripColour(result.getData(DataComponentTypes.CUSTOM_NAME));
                if (dn.contains("Key") || dn.contains("Authorised Control Disk")) {
                    result.editPersistentDataContainer(pdc -> pdc.set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId()));
                    ItemLore.Builder lore = ItemLore.lore();
                    String what = dn.contains("Key") ? "key" : "disk";
                    lore.addLine(Component.text("This " + what + " belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                    lore.addLine(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                    result.setData(DataComponentTypes.LORE, lore.build());
                }
            }
        } else {
            // blueprint
            result = plugin.getTardisAPI().getTARDISBlueprintItem(item, player);
        }
        if (result != null) {
            result.setAmount(1);
            player.getInventory().addItem(result);
            player.updateInventory();
        }
    }
}
