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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.enumeration.CraftingDifficulty;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author eccentric_nz
 */
public class TARDISVariableLightRecipe {

    private final TARDIS plugin;

    public TARDISVariableLightRecipe(TARDIS plugin) {
        this.plugin = plugin;
    }

    public void addRecipe() {
        ItemStack is;
        is = new ItemStack(Material.GLASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(ChatColor.WHITE + "Variable Light");
        im.setCustomModelData(1003);
        im.getPersistentDataContainer().set(TARDIS.plugin.getCustomBlockKey(), PersistentDataType.INTEGER, 1003);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, "variable_light");
        ShapedRecipe r = new ShapedRecipe(key, is);
        r.shape("###", "TDT", "###");
        r.setIngredient('#', plugin.getCraftingDifficulty() == CraftingDifficulty.HARD ? Material.GLASS : Material.GLASS_PANE);
        r.setIngredient('T', Material.TORCH);
        r.setIngredient('D', TARDISWalls.CHOICES);
        plugin.getServer().addRecipe(r);
        plugin.getFigura().getShapedRecipes().put("Variable Light", r);
    }
}
