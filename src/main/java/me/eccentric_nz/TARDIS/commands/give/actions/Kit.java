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
package me.eccentric_nz.TARDIS.commands.give.actions;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class Kit {

    private final TARDIS plugin;

    public Kit(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(String item, Player player) {
        ItemStack result = null;
        if (plugin.getIncomposita().getShapelessRecipes().containsKey(item)) {
            ShapelessRecipe recipe = plugin.getIncomposita().getShapelessRecipes().get(item);
            result = recipe.getResult();
        } else if (plugin.getFigura().getShapedRecipes().containsKey(item)) {
            ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get(item);
            result = recipe.getResult();
            if (result.hasItemMeta()) {
                ItemMeta im = result.getItemMeta();
                if (im.hasDisplayName()) {
                    String dn = ComponentUtils.stripColour(im.displayName());
                    if (dn.contains("Key") || dn.contains("Authorised Control Disk")) {
                        im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
                        if (im.hasLore()) {
                            List<Component> lore = im.lore();
                            String what = dn.contains("Key") ? "key" : "disk";
                            lore.add(Component.text("This " + what + " belongs to", NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                            lore.add(Component.text(player.getName(), NamedTextColor.AQUA).decorate(TextDecoration.ITALIC));
                            im.lore(lore);
                        }
                        result.setItemMeta(im);
                    }
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
