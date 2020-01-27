/*
 * Copyright (C) 2020 eccentric_nz
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

import me.eccentric_nz.TARDIS.enumeration.RECIPE_ITEM;
import me.eccentric_nz.TARDIS.utility.TARDISMessage;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class TARDISItemCommand {

    public boolean update(Player player, String[] args) {
        if (args.length < 2) {
            TARDISMessage.send(player, "TOO_FEW_ARGS");
            return true;
        }
        if (!args[1].equalsIgnoreCase("hand") && !args[1].equalsIgnoreCase("inventory")) {
            TARDISMessage.send(player, "ARG_ITEM");
            return true;
        }
        if (args[1].equalsIgnoreCase("hand")) {
            ItemStack inHand = player.getInventory().getItemInMainHand();
            if (inHand == null || !inHand.hasItemMeta()) {
                TARDISMessage.send(player, "ITEM_IN_HAND");
                return true;
            }
            ItemMeta im = inHand.getItemMeta();
            if (!im.hasDisplayName()) {
                TARDISMessage.send(player, "ITEM_IN_HAND");
                return true;
            }
            if (im.hasCustomModelData()) {
                TARDISMessage.send(player, "ITEM_HAS_DATA");
                return true;
            }
            // strip color codes
            String stripped = ChatColor.stripColor(im.getDisplayName());
            // look up display name
            int cmd = RECIPE_ITEM.getByName(stripped).getCustomModelData();
            im.setCustomModelData(cmd);
            inHand.setItemMeta(im);
            player.updateInventory();
            TARDISMessage.send(player, "ITEM_UPDATED");
            return true;
        } else {
            int i = 0;
            for (ItemStack is : player.getInventory()) {
                if (is != null && is.hasItemMeta()) {
                    ItemMeta im = is.getItemMeta();
                    if (im.hasDisplayName() && !im.hasCustomModelData()) {
                        // strip color codes
                        String stripped = ChatColor.stripColor(im.getDisplayName());
                        // look up display name
                        int cmd = RECIPE_ITEM.getByName(stripped).getCustomModelData();
                        im.setCustomModelData(cmd);
                        is.setItemMeta(im);
                        i++;
                    }
                }
            }
            if (i > 0) {
                TARDISMessage.send(player, "ITEMS_UPDATED", "" + i);
            }
            return true;
        }
    }
}
