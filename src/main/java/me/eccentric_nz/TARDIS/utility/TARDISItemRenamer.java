/*
 * Copyright (C) 2021 eccentric_nz
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
package me.eccentric_nz.tardis.utility;

import me.eccentric_nz.tardis.TARDISPlugin;
import me.eccentric_nz.tardis.enumeration.RecipeItem;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

/**
 * Soon after taking Amy Pond on board for the first time, the tardis console provided the Doctor with a new sonic
 * screwdriver, as the previous one had been destroyed.
 *
 * @author eccentric_nz
 */
public class TARDISItemRenamer {

    private final TARDISPlugin plugin;
    private final Player player;
    private final ItemStack itemStack;

    public TARDISItemRenamer(TARDISPlugin plugin, Player player, ItemStack itemStack) {
        this.plugin = plugin;
        this.player = player;
        this.itemStack = itemStack;
    }

    /**
     * Sets the name of the held item to the specified string. Also adds some lore.
     *
     * @param name    the name to give the item
     * @param setLore whether to set lore on the item
     */
    public void setName(String name, boolean setLore) {
        ItemMeta im = itemStack.getItemMeta();
        if (im == null) {
            TARDISPlugin.plugin.debug("ItemMeta was null for ItemStack: " + itemStack);
        } else {
            im.setDisplayName(name);
            if (setLore) {
                ArrayList<String> lore = new ArrayList<>();
                lore.add("Enter and exit your TARDIS");
                String format = ChatColor.AQUA + "" + ChatColor.ITALIC;
                lore.add(format + "This key belongs to");
                lore.add(format + player.getName());
                im.setLore(lore);
                im.getPersistentDataContainer().set(plugin.getTimeLordUuidKey(), plugin.getPersistentDataTypeUUID(), player.getUniqueId());
            }
            try {
                RecipeItem recipeItem = RecipeItem.valueOf(TARDISStringUtils.toScoredUppercase(name));
                im.setCustomModelData(recipeItem.getCustomModelData());
            } catch (IllegalArgumentException e) {
                // do nothing
            }
            itemStack.setItemMeta(im);
        }
    }
}
