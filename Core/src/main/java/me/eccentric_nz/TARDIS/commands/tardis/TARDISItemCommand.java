/*
 * Copyright (C) 2024 eccentric_nz
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

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
        if (!args[1].equalsIgnoreCase("hand") && !args[1].equalsIgnoreCase("inventory")) {
            plugin.getMessenger().send(player, TardisModule.TARDIS, "ARG_ITEM");
            return true;
        }
        if (args[1].equalsIgnoreCase("hand")) {
            ItemStack inHand = player.getInventory().getItemInMainHand();
            if (inHand == null || !inHand.hasItemMeta()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_IN_HAND");
                return true;
            }
            ItemMeta im = inHand.getItemMeta();
            if (!im.hasDisplayName()) {
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_IN_HAND");
                return true;
            }
            // strip color codes
            String stripped = ChatColor.stripColor(im.getDisplayName());
            // look up display name
            RecipeItem recipeItem = RecipeItem.getByName(stripped);
            if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                im.setDisplayName(ChatColor.WHITE + stripped);
                inHand.setItemMeta(im);
                player.updateInventory();
                plugin.getMessenger().send(player, TardisModule.TARDIS, "ITEM_UPDATED");
            }
        } else {
            int i = 0;
            for (ItemStack is : player.getInventory()) {
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName()) {
                        // strip color codes
                        String stripped = ChatColor.stripColor(im.getDisplayName());
                        // look up display name
                        RecipeItem recipeItem = RecipeItem.getByName(stripped);
                        if (!recipeItem.equals(RecipeItem.NOT_FOUND)) {
                            im.setDisplayName(ChatColor.WHITE + stripped);
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
        return true;
    }
}
