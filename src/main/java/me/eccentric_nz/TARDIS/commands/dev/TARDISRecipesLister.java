/*
 * Copyright (C) 2023 eccentric_nz
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
package me.eccentric_nz.TARDIS.commands.dev;

import java.util.Map;
import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.customblocks.TARDISDisplayItem;
import me.eccentric_nz.TARDIS.enumeration.RecipeCategory;
import me.eccentric_nz.TARDIS.enumeration.RecipeItem;
import me.eccentric_nz.TARDIS.enumeration.Schematic;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardisweepingangels.utils.Monster;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.ShapelessRecipe;

class TARDISRecipesLister {

    private final TARDIS plugin;

    TARDISRecipesLister(TARDIS plugin) {
        this.plugin = plugin;
    }

    void listRecipes(CommandSender sender, String[] args) {
        if (args.length > 2) {
            for (RecipeCategory category : RecipeCategory.values()) {
                if (category != RecipeCategory.UNUSED && category != RecipeCategory.UNCRAFTABLE) {
                    sender.sendMessage("#### " + category.getName());
                    for (RecipeItem item : RecipeItem.values()) {
                        if (item.getCategory() == category) {
                            sender.sendMessage("| " + TARDISStringUtils.capitalise(item.name()) + " | `/tardisrecipe " + item.toTabCompletionString() + "` |");
                        }
                    }
                    plugin.getMessenger().message(sender, "");
                }
            }
        } else {
            for (Map.Entry<String, ShapedRecipe> shaped : plugin.getFigura().getShapedRecipes().entrySet()) {
                sender.sendMessage(TARDISStringUtils.toUnderscoredUppercase(shaped.getKey()) + "(\"" + shaped.getKey() + "\", Material." + shaped.getValue().getResult().getType() + ", " + RecipeItem.getByName(shaped.getKey()).getCustomModelData() + "),");
            }
            for (Map.Entry<String, ShapelessRecipe> shapeless : plugin.getIncomposita().getShapelessRecipes().entrySet()) {
                sender.sendMessage(TARDISStringUtils.toUnderscoredUppercase(shapeless.getKey()) + "(\"" + shapeless.getKey() + "\", Material." + shapeless.getValue().getResult().getType() + ", " + RecipeItem.getByName(shapeless.getKey()).getCustomModelData() + "),");
            }
            if (plugin.getConfig().getBoolean("creation.seed_block_crafting")) {
                for (Map.Entry<Schematic, ShapedRecipe> seed : plugin.getObstructionum().getSeedRecipes().entrySet()) {
                    int model = 10001;
                    String material;
                    if (seed.getKey().isCustom()) {
                        material = seed.getKey().getSeedMaterial().toString();
                    } else {
                        try {
                            TARDISDisplayItem tdi = TARDISDisplayItem.valueOf(seed.getKey().getPermission().toUpperCase());
                            model = tdi.getCustomModelData();
                            material = tdi.getMaterial().toString();
                        } catch (IllegalArgumentException e) {
                            material = TARDISDisplayItem.CUSTOM.getMaterial().toString();
                        }
                    }
                    sender.sendMessage(seed.getKey().getPermission().toUpperCase() + "_SEED(\"" + seed.getKey().getPermission() + "\", Material." + material + ", " + model + "),");
                }
            }
            if (plugin.getConfig().getBoolean("modules.weeping_angels")) {
                for (Monster m : Monster.values()) {
                    sender.sendMessage(m.toString() + "_HEAD(\"" + m.getName() + " Head\", Material." + m.getMaterial().toString() + ", " + m.getCustomModelData() + "),");
                }
            }
        }
    }
}
