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
package me.eccentric_nz.TARDIS.recipes;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.custommodeldata.TARDISSeedModel;
import me.eccentric_nz.TARDIS.enumeration.CONSOLES;
import me.eccentric_nz.TARDIS.enumeration.SCHEMATIC;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TARDISSeedRecipe {

    private final TARDIS plugin;
    private final HashMap<SCHEMATIC, ShapedRecipe> seedRecipes;
    private final RecipeChoice.MaterialChoice choices;

    public TARDISSeedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        seedRecipes = new HashMap<>();
        choices = new RecipeChoice.MaterialChoice(TARDISWalls.BLOCKS);
    }

    public void addSeedRecipes() {
        for (SCHEMATIC schematic : CONSOLES.getBY_MATERIALS().values()) {
            plugin.getServer().addRecipe(makeSeedRecipe(schematic));
        }
    }

    private ShapedRecipe makeSeedRecipe(SCHEMATIC s) {
        ItemStack is = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
        // catch custom consoles, archives, templates not being in model data list
        int model;
        if (TARDISSeedModel.materialMap.containsKey(s.getSeedMaterial())) {
            model = TARDISSeedModel.modelByMaterial(s.getSeedMaterial());
            is = new ItemStack(Material.RED_MUSHROOM_BLOCK, 1);
        } else {
            model = 45;
            is = new ItemStack(Material.MUSHROOM_STEM, 1);
        }
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
        im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
        im.setCustomModelData(10000000 + model);
        List<String> lore = new ArrayList<>();
        lore.add(s.getPermission().toUpperCase(Locale.ENGLISH));
        im.setLore(lore);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.getPermission() + "_seed");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("T  ", "L W", "S F");
        // set ingredients
        r.setIngredient('T', Material.REDSTONE_TORCH);
        r.setIngredient('L', Material.LAPIS_BLOCK);
        r.setIngredient('W', choices);
        r.setIngredient('S', s.getSeedMaterial());
        r.setIngredient('F', choices);
        seedRecipes.put(s, r);
        return r;
    }

    public HashMap<SCHEMATIC, ShapedRecipe> getSeedRecipes() {
        return seedRecipes;
    }
}
