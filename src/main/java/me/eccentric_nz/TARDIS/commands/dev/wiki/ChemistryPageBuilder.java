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
package me.eccentric_nz.TARDIS.commands.dev.wiki;

import me.eccentric_nz.TARDIS.TARDIS;
import me.eccentric_nz.TARDIS.info.TARDISDescription;
import me.eccentric_nz.TARDIS.utility.TARDISStringUtils;
import me.eccentric_nz.tardischemistry.block.ChemistryBlock;
import me.eccentric_nz.tardischemistry.block.RecipeData;
import me.eccentric_nz.tardischemistry.compound.Compound;
import me.eccentric_nz.tardischemistry.element.Element;
import me.eccentric_nz.tardischemistry.product.Product;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Material;
import org.bukkit.inventory.RecipeChoice;

import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ChemistryPageBuilder extends PageBuilder {

    final Pattern regex = Pattern.compile("([A-Z_])+");
    private final TARDIS plugin;

    public ChemistryPageBuilder(TARDIS plugin) {
        super(plugin);
        this.plugin = plugin;
    }

    public boolean compile() {
        // chemistry lab products & compounds
        String data;
        for (Product p : Product.values()) {
            plugin.debug(p.toString());
            data = formatProduct(p);
            if (!data.isEmpty()) {
                save(TARDISStringUtils.toDashedLowercase(p.toString()), data);
            }
        }
        for (RecipeData block : ChemistryBlock.RECIPES.values()) {
            plugin.debug(block.nameSpacedKey());
            data = formatChemistryBlock(block);
            if (!data.isEmpty()) {
                save(TARDISStringUtils.toDashedLowercase(block.displayName()), data);
            }
        }
        return true;
    }

    private String formatChemistryBlock(RecipeData item) {
        String crafting = TARDISStringUtils.toLowercaseDashed(item.displayName());
        // get ingredients
        String mat = TARDISStringUtils.capitalise(item.craftMaterial().toString());
        String ingredients = "[" + mat + "](https://minecraft.wiki/w/" + mat.replaceAll(" ", "_") + ")<br/>[Crafting Table](https://minecraft.wiki/w/Crafting_Table)";
        // get recipe array
        String icon = TARDISStringUtils.toLowercaseDashed(mat);
        String table = "['" + icon + "','" + icon + "','" + icon + "','" + icon + "','crafting-table','" + icon + "','" + icon + "','" + icon + "','" + icon + "','" + crafting + "']";
        String SHAPELESS = """
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

                | Ingredients | Crafting recipe |
                | ----------- | --------------- |
                | %s | <Recipe icons={%s} /> |
                """;
        return String.format(SHAPELESS, item.displayName(), item.displayName(), StringUtils.join(item.lore(), " "), crafting, ingredients, table);
    }

    private String formatProduct(Product product) {
        String item = product.toString().replaceAll("_", " ");
        String crafting = TARDISStringUtils.toLowercaseDashed(item);
        // get recipe array
        String[] data = getChemistryTable(product);
        String ingredients = data[0];
        String hardTable = data[1];
        String desc = String.format("The %s is used to ", item);
        try {
            String info = TARDISStringUtils.toEnumUppercase(item) + "_INFO";
            TARDISDescription description = TARDISDescription.valueOf(info);
            desc = description.getDesc();
        } catch (IllegalArgumentException ignored) {
        }
        // itemName, itemName, itemName, List.of(spacedIngredientName, scoredIngredientName), List.of(scoredIngredientName...) -> x2
        String PROD = """
                ---
                layout: default
                title: %s
                ---

                import Recipe from "@site/src/components/Recipe";

                %s
                ===================

                %s

                ## Formula
                            
                `/tchemistry formula %s`

                | Ingredients | Lab recipe |
                | ----------- | --------------- |
                | %s | <Recipe icons={%s} /> |
                """;
        return String.format(PROD, item, item, desc, crafting, ingredients, hardTable);
    }

    private String[] getChemistryTable(Product p) {
        StringBuilder ingredientBuilder = new StringBuilder();
        StringBuilder tableBuilder = new StringBuilder("[");
        String[] shape = p.getRecipe().split("\\|");
        String[][] data = new String[3][3];
        data[0] = shape[0].split(",");
        data[1] = shape[1].split(",");
        data[2] = shape[2].split(",");
        Set<String> ingredients = new HashSet<>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (data[i][j] == null || data[i][j].equals("-")) {
                    tableBuilder.append("'air',");
                } else {
                    String dashed = "";
                    try {
                        // is it a Spigot material?
                        Material material = Material.valueOf(data[i][j]);
                        ingredients.add(material.toString());
                        dashed = TARDISStringUtils.toLowercaseDashed(TARDISStringUtils.capitalise(material.toString()));
                    } catch (IllegalArgumentException me) {
                        // is it a compound?
                        try {
                            Compound compound = Compound.valueOf(data[i][j].replace(" ", "_"));
                            ingredients.add(data[i][j]);
                            dashed = TARDISStringUtils.toLowercaseDashed(data[i][j]);
                        } catch (IllegalArgumentException ce) {
                            // is it an element?
                            try {
                                Element element = Element.valueOf(data[i][j]);
                                ingredients.add(data[i][j]);
                                dashed = data[i][j].toLowerCase(Locale.ROOT);
                            } catch (IllegalArgumentException ee) {
                                // don't know what it is
                            }
                        }
                    }
                    tableBuilder.append("'").append(dashed).append("'").append(",");
                }
            }
        }
        String result = TARDISStringUtils.toDashedLowercase(p.toString());
        tableBuilder.append("'").append(result).append("'").append("]");
        String prefix = "";
        for (String s : ingredients) {
            Matcher matcher = regex.matcher(s);
            if (matcher.matches()) {
                Material mat = Material.valueOf(s);
                String link = getLink(new RecipeChoice.MaterialChoice(mat));
                ingredientBuilder.append(prefix);
                prefix = "<br/>";
                ingredientBuilder.append(link);
            } else {
                ingredientBuilder.append(prefix);
                prefix = "<br/>";
                ingredientBuilder.append(s);
            }
        }
        return new String[]{ingredientBuilder.toString(), tableBuilder.toString()};
    }
}
