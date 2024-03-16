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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.Consoles;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
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
    private final HashMap<Schematic, ShapedRecipe> seedRecipes;
    private final RecipeChoice.MaterialChoice choices;

    public TARDISSeedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        seedRecipes = new HashMap<>();
        choices = new RecipeChoice.MaterialChoice(TARDISWalls.BLOCKS);
    }

    public void addSeedRecipes() {
        for (Schematic schematic : Consoles.getBY_MATERIALS().values()) {
            ShapedRecipe recipe = makeSeedRecipe(schematic);
            plugin.getServer().addRecipe(recipe);
            plugin.getFigura().getShapedRecipes().put(TARDISStringUtils.capitalise(schematic.getPermission()), recipe);
        }
    }

    private ShapedRecipe makeSeedRecipe(Schematic s) {
        ItemStack is;
        // catch custom consoles, archives, templates not being in model data list
        int model = 10001;
        if (s.isCustom()) {
            is = new ItemStack(s.getSeedMaterial(), 1);
        } else {
            try {
                TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(s.getPermission().toUpperCase());
                model = tdi.getCustomModelData();
                is = new ItemStack(tdi.getMaterial(), 1);
            } catch (IllegalArgumentException e) {
                plugin.debug("Could not get display item for console! " + e.getMessage());
                is = new ItemStack(TARDISDisplayItem.CUSTOM.getMaterial(), 1);
            }
        }
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.INTEGER, model);
        im.setDisplayName(ChatColor.GOLD + "TARDIS Seed Block");
        im.setCustomModelData(model);
        List<String> lore = new ArrayList<>();
        lore.add(s.getPermission().toUpperCase(Locale.ENGLISH));
        im.setLore(lore);
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.getPermission() + "_seed");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("T  ", "L W", "S F");
        // get torch item
        Material torch = Material.REDSTONE_TORCH;
        if (!plugin.getConfig().getBoolean("creation.seed_block.legacy")) {
            String difficulty;
            World world = plugin.getServer().getWorlds().get(0);
            switch (world.getDifficulty()) {
                case HARD -> difficulty = "hard";
                case NORMAL -> difficulty = "normal";
                default -> difficulty = "easy";
            }
            try {
                torch = Material.valueOf(plugin.getConfig().getString("creation.seed_block." + difficulty));
            } catch (IllegalArgumentException ignored) {
            }
        }
        // set ingredients
        r.setIngredient('T', torch);
        r.setIngredient('L', Material.LAPIS_BLOCK);
        r.setIngredient('W', choices);
        r.setIngredient('S', s.getSeedMaterial());
        r.setIngredient('F', choices);
        seedRecipes.put(s, r);

        return r;
    }

    public HashMap<Schematic, ShapedRecipe> getSeedRecipes() {
        return seedRecipes;
    }
}
