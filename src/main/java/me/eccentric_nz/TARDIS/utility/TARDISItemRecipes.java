/*
 * Copyright (C) 2013 eccentric_nz
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
package me.eccentric_nz.TARDIS.utility;

import java.util.Arrays;
import me.eccentric_nz.TARDIS.TARDIS;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * The Stattenheim remote control is a remote control used by Time Lords to
 * control their TARDISes. The Rani and the Second Doctor each have used a
 * Stattenheim remote control for their TARDISes, which allowed them 'call' the
 * TARDIS to their location.
 *
 * @author eccentric_nz
 */
public class TARDISItemRecipes {

    private final TARDIS plugin;
    Material mat;

    public TARDISItemRecipes(TARDIS plugin) {
        this.plugin = plugin;
        mat = Material.valueOf(plugin.getConfig().getString("stattenheim"));
    }

    public ShapedRecipe stattenheim() {
        ItemStack is = new ItemStack(Material.FLINT, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Stattenheim Remote");
        im.setLore(Arrays.asList(new String[]{"Right-click block", "to call TARDIS"}));
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("OBO", "OLO", "RRR");
        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('B', Material.STONE_BUTTON);
        if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
            recipe.setIngredient('L', Material.MAP, 1963);
        } else {
            recipe.setIngredient('L', Material.INK_SACK, 4);
        }
        recipe.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(recipe);
        return recipe;
    }

    public ShapedRecipe locatorCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1965);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Locator Circuit");
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("RQR", "RIR", "DRL");
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('Q', Material.QUARTZ);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('D', Material.DIODE);
        recipe.setIngredient('L', Material.INK_SACK, 4);
        plugin.getServer().addRecipe(recipe);
        return recipe;
    }

    public ShapedRecipe materialisationCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1964);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Materialisation Circuit");
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("IDI", "DLD", "QRQ");
        if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
            recipe.setIngredient('L', Material.EYE_OF_ENDER);
        } else {
            recipe.setIngredient('I', Material.IRON_INGOT);
        }
        recipe.setIngredient('D', Material.DIODE);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('L', Material.INK_SACK, 4);
        recipe.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(recipe);
        return recipe;
    }

    public ShapedRecipe stattenheimCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1963);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Stattenheim Circuit");
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("AAA", "LRM", "QQQ");
        recipe.setIngredient('A', Material.AIR);
        recipe.setIngredient('R', Material.REDSTONE);
        recipe.setIngredient('Q', Material.QUARTZ);
        recipe.setIngredient('L', Material.MAP, 1965);
        recipe.setIngredient('M', Material.MAP, 1964);
        plugin.getServer().addRecipe(recipe);
        return recipe;
    }

    public ShapedRecipe locator() {
        ItemStack is = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Locator");
        is.setItemMeta(im);
        ShapedRecipe recipe = new ShapedRecipe(is);
        recipe.shape("OIO", "ICI", "OIO");
        recipe.setIngredient('O', Material.OBSIDIAN);
        recipe.setIngredient('I', Material.IRON_INGOT);
        recipe.setIngredient('C', Material.MAP, 1963);
        plugin.getServer().addRecipe(recipe);
        return recipe;
    }
}
