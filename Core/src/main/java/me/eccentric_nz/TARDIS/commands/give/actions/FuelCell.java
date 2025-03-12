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

import com.google.common.collect.Multimaps;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.TardisModule;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.Map;

public class FuelCell {

    private final TARDIS plugin;

    public FuelCell(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void give(CommandSender sender, int amount, Player player) {
        if (amount > 64) {
            plugin.getMessenger().send(sender, TardisModule.TARDIS, "ARG_MAX");
            return;
        }
        ShapedRecipe recipe = plugin.getFigura().getShapedRecipes().get("Artron Storage Cell");
        ItemStack result = recipe.getResult();
        result.setAmount(amount);
        // add lore and enchantment
        ItemMeta im = result.getItemMeta();
        List<String> lore = im.getLore();
        int max = plugin.getArtronConfig().getInt("full_charge");
        lore.set(1, "" + max);
        im.setLore(lore);
        im.setEnchantmentGlintOverride(true);
        im.addItemFlags(ItemFlag.values());
        im.setAttributeModifiers(Multimaps.forMap(Map.of()));
        result.setItemMeta(im);
        player.getInventory().addItem(result);
        player.updateInventory();
        plugin.getMessenger().send(player, TardisModule.TARDIS, "GIVE_ITEM", sender.getName(), amount + " Full Artron Storage Cell");
    }
}
