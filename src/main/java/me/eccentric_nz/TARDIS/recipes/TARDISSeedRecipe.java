/*
 * Copyright (C) 2026 eccentric_nz
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
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItemRegistry;
import me.eccentric_nz.TARDIS.customblocks.TARDISSeedDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.Desktops;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.rooms.TARDISWalls;
import me.eccentric_nz.TARDIS.utility.ComponentUtils;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import net.kyori.adventure.text.Component;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class TARDISSeedRecipe {

    private final TARDIS plugin;
    private final HashMap<Schematic, ShapedRecipe> seedRecipes;

    public TARDISSeedRecipe(TARDIS plugin) {
        this.plugin = plugin;
        seedRecipes = new HashMap<>();
    }

    public void addSeedRecipes() {
        for (Schematic schematic : Desktops.getBY_MATERIALS().values()) {
            ShapedRecipe recipe = makeSeedRecipe(schematic);
            plugin.getServer().addRecipe(recipe);
            plugin.getFigura().getShapedRecipes().put(TARDISStringUtils.capitalise(schematic.getPermission()), recipe);
        }
    }

    private ShapedRecipe makeSeedRecipe(Schematic s) {
        ItemStack is;
        // catch custom consoles, archives, templates not being in model data list
        NamespacedKey model = TARDISSeedDisplayItem.CUSTOM.getCustomModel();
        if (s.isCustom()) {
            is = ItemStack.of(s.getSeedMaterial(), 1);
        } else {
            try {
                TARDISDisplayItem tdi = TARDISDisplayItemRegistry.valueOf(s.getPermission().toUpperCase(Locale.ROOT));
                model = tdi.getCustomModel();
                is = ItemStack.of(tdi.getMaterial(), 1);
            } catch (IllegalArgumentException e) {
                plugin.debug("Could not get display item for console! " + e.getMessage());
                is = ItemStack.of(TARDISSeedDisplayItem.CUSTOM.getMaterial(), 1);
            }
        }
        ItemMeta im = is.getItemMeta();
        im.getPersistentDataContainer().set(plugin.getCustomBlockKey(), PersistentDataType.STRING, model.getKey());
        im.displayName(ComponentUtils.toGold("TARDIS Seed Block"));
        im.lore(List.of(Component.text(s.getPermission().toUpperCase(Locale.ROOT))));
        is.setItemMeta(im);
        NamespacedKey key = new NamespacedKey(plugin, s.getPermission() + "_seed");
        ShapedRecipe r = new ShapedRecipe(key, is);
        // set shape
        r.shape("T  ", "L W", "S F");
        // get torch item
        Material torch = Material.REDSTONE_TORCH;
        if (!plugin.getConfig().getBoolean("creation.seed_block.legacy")) {
            String difficulty;
            World world = plugin.getServer().getWorlds().getFirst();
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
        r.setIngredient('W', TARDISWalls.CHOICES);
        r.setIngredient('S', s.getSeedMaterial());
        r.setIngredient('F', TARDISWalls.CHOICES);
        seedRecipes.put(s, r);

        return r;
    }

    public HashMap<Schematic, ShapedRecipe> getSeedRecipes() {
        return seedRecipes;
    }
}
