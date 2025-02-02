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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;

import java.util.Map;

public class ShapedPageBuilder extends PageBuilder {

    private final TARDIS plugin;

    public ShapedPageBuilder(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public boolean compile() {
        // all shaped recipes
        for (String key : plugin.getFigura().getShapedRecipes().keySet()) {
            plugin.debug(key);
            String data = formatShaped(key);
            if (!data.isEmpty()) {
                save(TARDISStringUtils.toDashedLowercase(key), data);
            }
        }
        return true;
    }

    private String formatShaped(String item) {
        String crafting = TARDISStringUtils.toLowercaseDashed(item);
        // get ingredients
        String easyIngredients;
        String hardIngredients;
        // get recipe arrays
        String easyTable;
        String hardTable;
        easyIngredients = getIngredients(item);
        hardIngredients = getIngredients(item);
        easyTable = getShapedTable(item);
        hardTable = getShapedTable(item);
        String desc = String.format("The %s is used to ", item);
        try {
            String info = TARDISStringUtils.toEnumUppercase(item) + "_INFO";
            TARDISDescription description = TARDISDescription.valueOf(info);
            desc = description.getDesc();
        } catch (IllegalArgumentException ignored) {
        }
        String PAGE = """
                ---
                layout: default
                title: %s
                ---

                import Recipe from "@site/src/components/Recipe";

                %s
                ===================

                %s

                ## Crafting
                            
                `/trecipe %s`

                | Ingredients | Crafting recipe | Difficulty |
                | ----------- | --------------- | ------------- |
                | %s | <Recipe icons={%s} /> | easy |
                | %s | <Recipe icons={%s} /> | hard |
                """;
        // itemName, itemName, itemName, List.of(spacedIngredientName, scoredIngredientName), List.of(scoredIngredientName...) -> x2
        return String.format(PAGE, item, item, desc, crafting, easyIngredients, easyTable, hardIngredients, hardTable);
    }

    private String getIngredients(String item) {
        StringBuilder ingredientBuilder = new StringBuilder();
        String prefix = "";
        for (Map.Entry<Character, RecipeChoice> map : plugin.getFigura().getShapedRecipes().get(item).getChoiceMap().entrySet()) {
            RecipeChoice choice = map.getValue();
            String link = getLink(choice);
            ingredientBuilder.append(prefix);
            prefix = "<br/>";
            ingredientBuilder.append(link);
        }
        return ingredientBuilder.toString();
    }

    private String getShapedTable(String item) {
        StringBuilder tableBuilder = new StringBuilder("[");
        ShapedRecipe shapedRecipe = plugin.getFigura().getShapedRecipes().get(item);
        Map<Character, RecipeChoice> choices = shapedRecipe.getChoiceMap();
        String[] shape = shapedRecipe.getShape();
        for (String row : shape) {
            for (int i = 0; i < row.length(); i++) {
                Character c = row.charAt(i);
                switch (c) {
                    case ',' -> {
                        // do nothing
                    }
                    case ' ' -> tableBuilder.append("'air',");
                    default -> {
                        // get ingredient
                        String dashed = "";
                        RecipeChoice choice = choices.get(c);
                        if (choice instanceof RecipeChoice.MaterialChoice mat) {
                            dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(mat.getChoices().getFirst().toString()));
                        }
                        if (choice instanceof RecipeChoice.ExactChoice exact) {
                            ItemStack is = exact.getItemStack();
                            switch (is.getType()) {
                                case POTION -> {
                                    PotionMeta pm = (PotionMeta) is.getItemMeta();
                                    String potion = pm.getBasePotionType().name();
                                    dashed = TARDISStringUtils.toLowercaseDashed("Potion of " + TARDISStringUtils.capitalise(potion));
                                }
                                case ENCHANTED_BOOK -> {
                                    EnchantmentStorageMeta bm = (EnchantmentStorageMeta) is.getItemMeta();
                                    String enchant = bm.getEnchants().keySet().stream().findFirst().toString();
                                    String cap = TARDISStringUtils.capitalise(enchant);
                                    dashed = TARDISStringUtils.toLowercaseDashed("Enchanted Book of " + cap);
                                }
                                default -> {
                                    ItemMeta im = is.getItemMeta();
                                    dashed = TARDISStringUtils.toLowercaseDashed(im.getDisplayName());
                                }
                            }
                        }
                        tableBuilder.append("'").append(dashed).append("'").append(",");
                    }
                }
            }
        }
        // get result
        String result = TARDISStringUtils.toLowercaseDashed(item);
        tableBuilder.append("'").append(result).append("'").append("]");
        return tableBuilder.toString();
    }
}
