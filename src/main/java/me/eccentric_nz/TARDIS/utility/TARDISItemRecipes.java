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
        ItemStack is = new ItemStack(mat, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Stattenheim Remote");
        im.setLore(Arrays.asList(new String[]{"Right-click block", "to call TARDIS"}));
        is.setItemMeta(im);
        ShapedRecipe stattenheim = new ShapedRecipe(is);
        stattenheim.shape("OBO", "OLO", "RRR");
        stattenheim.setIngredient('O', Material.OBSIDIAN);
        stattenheim.setIngredient('B', Material.STONE_BUTTON);
        if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
            stattenheim.setIngredient('L', Material.MAP, 1963);
        } else {
            stattenheim.setIngredient('L', Material.INK_SACK, 4);
        }
        stattenheim.setIngredient('R', Material.REDSTONE);
        plugin.getServer().addRecipe(stattenheim);
        return stattenheim;
    }

    public ShapedRecipe locatorCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1965);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Locator Circuit");
        is.setItemMeta(im);
        ShapedRecipe loccircuit = new ShapedRecipe(is);
        loccircuit.shape("RQR", "RIR", "DRL");
        loccircuit.setIngredient('R', Material.REDSTONE);
        loccircuit.setIngredient('Q', Material.QUARTZ);
        loccircuit.setIngredient('I', Material.IRON_INGOT);
        loccircuit.setIngredient('D', Material.DIODE);
        loccircuit.setIngredient('L', Material.INK_SACK, 4);
        plugin.getServer().addRecipe(loccircuit);
        return loccircuit;
    }

    public ShapedRecipe materialisationCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1964);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Materialisation Circuit");
        is.setItemMeta(im);
        ShapedRecipe matcircuit = new ShapedRecipe(is);
        matcircuit.shape("IDI", "DLD", "QRQ");
        if (plugin.getConfig().getString("difficulty").equalsIgnoreCase("hard")) {
            matcircuit.setIngredient('I', Material.EYE_OF_ENDER);
        } else {
            matcircuit.setIngredient('I', Material.IRON_INGOT);
        }
        matcircuit.setIngredient('D', Material.DIODE);
        matcircuit.setIngredient('R', Material.REDSTONE);
        matcircuit.setIngredient('L', Material.INK_SACK, 4);
        matcircuit.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(matcircuit);
        return matcircuit;
    }

    public ShapedRecipe stattenheimCircuit() {
        ItemStack is = new ItemStack(Material.MAP, 1, (short) 1963);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Stattenheim Circuit");
        is.setItemMeta(im);
        ShapedRecipe statcircuit = new ShapedRecipe(is);
        statcircuit.shape("   ", "LRM", "QQQ");
        statcircuit.setIngredient('L', Material.MAP, 1965);
        statcircuit.setIngredient('R', Material.REDSTONE);
        statcircuit.setIngredient('M', Material.MAP, 1964);
        statcircuit.setIngredient('Q', Material.QUARTZ);
        plugin.getServer().addRecipe(statcircuit);
        return statcircuit;
    }

    public ShapedRecipe locator() {
        ItemStack is = new ItemStack(Material.COMPASS, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("TARDIS Locator");
        is.setItemMeta(im);
        ShapedRecipe locator = new ShapedRecipe(is);
        locator.shape("OIO", "ICI", "OIO");
        locator.setIngredient('O', Material.OBSIDIAN);
        locator.setIngredient('I', Material.IRON_INGOT);
        locator.setIngredient('C', Material.MAP, 1965);
        plugin.getServer().addRecipe(locator);
        return locator;
    }

    public ShapedRecipe sonic() {
        ItemStack is = new ItemStack(Material.STICK, 1);
        ItemMeta im = is.getItemMeta();
        im.setDisplayName("Sonic Screwdriver");
        im.setLore(Arrays.asList(new String[]{"Enter and exit your TARDIS"}));
        is.setItemMeta(im);
        ShapedRecipe sonic = new ShapedRecipe(is);
        sonic.shape(" R ", " S ", " S ");
        sonic.setIngredient('R', Material.REDSTONE);
        sonic.setIngredient('S', Material.STICK);
        plugin.getServer().addRecipe(sonic);
        return sonic;
    }
}
